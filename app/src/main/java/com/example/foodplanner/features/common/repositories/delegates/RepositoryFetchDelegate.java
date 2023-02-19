package com.example.foodplanner.features.common.repositories.delegates;

import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.PlanMealMapper;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class RepositoryFetchDelegate<Arg, M, E> {

    private final Function<Arg, Single<? extends RemoteListWrapper<M>>> remoteService;
    private final Function<List<M>, Completable> remoteCacheService;
    private final Function<Arg, Flowable<List<E>>> localService;
    private final Function<List<E>, Completable> localCacheService;
    private final BaseMapper<M, E> mapper;

    public RepositoryFetchDelegate(Function<Arg, Single<? extends RemoteListWrapper<M>>> remoteService,
                                   Function<Arg, Flowable<List<E>>> localService,
                                   Function<List<M>, Completable> remoteCacheService,
                                   Function<List<E>, Completable> localCacheService,
                                   BaseMapper<M, E> mapper) {
        this.remoteService = remoteService;
        this.localService = localService;
        this.remoteCacheService = remoteCacheService;
        this.localCacheService = localCacheService;
        this.mapper = mapper;
    }


    public Flowable<List<M>> fetch(Arg arg) {
        Flowable<List<M>> accumulation;
        if (remoteService != null) {
            accumulation = fetchFromRemote(arg);
        } else {
            accumulation = fetchFromLocal(arg);
        }
        return accumulation;
    }

    private Flowable<List<M>> fetchFromRemote(Arg arg) {
        Flowable<List<M>> fromRemote = fetchFromRemoteImpl(arg);
        if (localCacheService != null) {
            fromRemote = fromRemote.flatMap(list -> cacheRemoteResults(list).andThen(Flowable.just(list)));
        }
        if (localService != null) {
            fromRemote = fromRemote.onErrorResumeWith(getRemoteFallback(arg));
        }
        return fromRemote;
    }

    private Flowable<List<M>> fetchFromLocal(Arg arg) {
        Flowable<List<M>> fromLocal = fetchFromLocalImpl(arg);
        if (remoteCacheService != null) {
            fromLocal = fromLocal.flatMap(list -> cacheLocalResults(list).andThen(Flowable.just(list)));
        }
        return fromLocal;
    }

    private Flowable<List<M>> getRemoteFallback(Arg arg) {
        return fetchFromLocalImpl(arg);
    }

    private Flowable<List<M>> fetchFromRemoteImpl(Arg arg) {
        return remoteService.apply(arg).map(RemoteListWrapper::getItems)
                .map(list -> {
                    if (list == null) return Collections.<M>emptyList();
                    return list;
                }).toFlowable();
    }

    private Flowable<List<M>> fetchFromLocalImpl(Arg arg) {
        return localService.apply(arg).map(list -> list.stream()
                .map(mapper::toModel).collect(Collectors.toList()));
    }

    private Completable cacheRemoteResults(List<M> objects) {
        return localCacheService.apply(
                objects.stream().map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    private Completable cacheLocalResults(List<M> objects) {
        return remoteCacheService.apply(objects);
    }
}
