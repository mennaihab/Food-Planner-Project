package com.example.foodplanner.features.common.repositories.delegates;

import com.example.foodplanner.features.common.helpers.RemoteModelWrapper;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class RepositoryFetchDelegate<Arg, M, E> {

    private final Function<Arg, Single<? extends RemoteModelWrapper<M>>> remoteService;
    private final Function<Arg, Flowable<List<E>>> localService;
    private final Function<List<E>, Completable> localCacheService;
    private final BaseMapper<M, E> mapper;

    public RepositoryFetchDelegate(Function<Arg, Single<? extends RemoteModelWrapper<M>>> remoteService,
                                   Function<Arg, Flowable<List<E>>> localService,
                                   Function<List<E>, Completable> localCacheService,
                                   BaseMapper<M, E> mapper) {
        this.remoteService = remoteService;
        this.localService = localService;
        this.localCacheService = localCacheService;
        this.mapper = mapper;
    }

    public Flowable<List<M>> fetch(Arg arg) {
        Flowable<List<M>> fromRemote = fetchFromRemote(arg);
        if (localService != null) {
            fromRemote = fromRemote.onErrorResumeWith(getFallback(arg));
        }
        return fromRemote;
    }

    private Flowable<List<M>> fetchFromRemote(Arg arg) {
        Single<List<M>> fromRemote = remoteService.apply(arg).map(RemoteModelWrapper::getItems)
                .map(list -> {
                    if (list == null) return Collections.emptyList();
                    return list;
                });
        if (localCacheService != null) {
            fromRemote = fromRemote.flatMap(list -> cacheRemoteResults(list).andThen(Single.just(list)));
        }
        return fromRemote.toFlowable();
    }

    private Flowable<List<M>> getFallback(Arg arg) {
        return localService.apply(arg).map(list -> list.stream()
                .map(mapper::toModel).collect(Collectors.toList()));
    }

    private Completable cacheRemoteResults(List<M> objects) {
        return localCacheService.apply(
                objects.stream().map(mapper::toEntity)
                .collect(Collectors.toList())
        );
    }
}
