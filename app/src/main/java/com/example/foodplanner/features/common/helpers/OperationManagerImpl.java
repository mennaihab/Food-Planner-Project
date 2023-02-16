package com.example.foodplanner.features.common.helpers;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OperationManagerImpl implements OperationManager {

    private final Map<Integer, Completable> operations = new HashMap<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public int submitOperation(Completable operation) {
        int key = operation.hashCode();
        disposable.add(operation.subscribe());
        operations.put(key, operation);
        return key;
    }

    @Override
    public Completable retrieve(int operationKey) {
        return operations.get(operationKey);
    }

    @Override
    public void close() {
        disposable.dispose();
        operations.clear();
    }
}
