package com.example.foodplanner.features.common.helpers;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public interface Operation<T> {

    static <T> Operation<T> fromSingle(Single<T> source) {
        return new OperationSingleImpl<>(source);
    }

    Canceller addListener(Listener<T> listener);

    interface Canceller {
        void cancel();
    }

    @FunctionalInterface
    interface Listener<T> {
        void onDone(T result, Throwable error);
    }
}

abstract class OperationBaseImpl<T> implements Operation<T> {
    protected final List<Listener<T>> listeners = new LinkedList<>();

    protected T result;
    protected Throwable error;

    public Canceller addListener(Listener<T> listener) {
        Log.d("OperationSingleImpl", listener.toString());
        synchronized (listeners) {
            listeners.add(listener);
            if (result != null || error != null) {
                listener.onDone(result, error);
            }
            return () -> listeners.remove(listener);
        }
    }
}

class OperationSingleImpl<T> extends OperationBaseImpl<T> implements SingleObserver<T> {
    private Disposable disposable;

    @SuppressLint("CheckResult")
    public OperationSingleImpl(Single<T> source) {
        source.subscribeWith(this);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onSuccess(@NonNull T t) {
        synchronized (listeners) {
            result = t;
            listeners.forEach(l -> l.onDone(t, null));
            disposable.dispose();
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        synchronized (listeners) {
            error = e;
            listeners.forEach(l -> l.onDone(null, e));
            disposable.dispose();
        }
    }
}

class OperationCompletableImpl extends OperationBaseImpl<Object> implements CompletableObserver {
    private Disposable disposable;

    @SuppressLint("CheckResult")
    public OperationCompletableImpl(Completable source) {
        source.subscribeWith(this);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }

    @Override
    public void onComplete() {
        synchronized (listeners) {
            result = 0;
            listeners.forEach(l -> l.onDone(0, null));
            disposable.dispose();
        }
    }


    @Override
    public void onError(@NonNull Throwable e) {
        synchronized (listeners) {
            error = e;
            listeners.forEach(l -> l.onDone(null, e));
            disposable.dispose();
        }
    }
}
