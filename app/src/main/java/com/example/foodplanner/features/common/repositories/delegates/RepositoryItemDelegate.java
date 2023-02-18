package com.example.foodplanner.features.common.repositories.delegates;

import com.example.foodplanner.features.common.helpers.RemoteItemWrapper;
import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class RepositoryItemDelegate<Arg, M, E> {

    private final Function<Arg, Single<? extends RemoteItemWrapper<M>>> remoteService;
    private final Function<M, Completable> remoteCacheService;
    private final Function<Arg, Flowable<E>> localService;
    private final Function<E, Completable> localCacheService;
    private final BaseMapper<M, E> mapper;

    public RepositoryItemDelegate(Function<Arg, Single<? extends RemoteItemWrapper<M>>> remoteService,
                                  Function<Arg, Flowable<E>> localService,
                                  Function<M, Completable> remoteCacheService,
                                  Function<E, Completable> localCacheService,
                                  BaseMapper<M, E> mapper) {
        this.remoteService = remoteService;
        this.localService = localService;
        this.remoteCacheService = remoteCacheService;
        this.localCacheService = localCacheService;
        this.mapper = mapper;
    }

    public Flowable<M> fetch(Arg arg) {
        Flowable<M> accumulation;
        if (remoteService != null) {
            accumulation = fetchFromRemote(arg);
        } else {
            accumulation = fetchFromLocal(arg);
        }
        return accumulation;
    }

    private Flowable<M> fetchFromRemote(Arg arg) {
        Flowable<M> fromRemote = fetchFromRemoteImpl(arg);
        if (localCacheService != null) {
            fromRemote = fromRemote.flatMap(list -> cacheRemoteResults(list).andThen(Flowable.just(list)));
        }
        if (localService != null) {
            fromRemote = fromRemote.onErrorResumeWith(getRemoteFallback(arg));
        }
        return fromRemote;
    }

    private Flowable<M> fetchFromLocal(Arg arg) {
        Flowable<M> fromLocal = fetchFromLocalImpl(arg);
        if (remoteCacheService != null) {
            fromLocal = fromLocal.flatMap(list -> cacheLocalResults(list).andThen(Flowable.just(list)));
        }
        return fromLocal;
    }

    private Flowable<M> getRemoteFallback(Arg arg) {
        return fetchFromLocalImpl(arg);
    }

    private Flowable<M> fetchFromRemoteImpl(Arg arg) {
        return remoteService.apply(arg).map(RemoteItemWrapper::getItem).toFlowable();
    }

    private Flowable<M> fetchFromLocalImpl(Arg arg) {
        return localService.apply(arg).map(mapper::toModel);
    }

    private Completable cacheRemoteResults(M objects) {
        return localCacheService.apply(mapper.toEntity(objects));
    }

    private Completable cacheLocalResults(M objects) {
        return remoteCacheService.apply(objects);
    }
}
