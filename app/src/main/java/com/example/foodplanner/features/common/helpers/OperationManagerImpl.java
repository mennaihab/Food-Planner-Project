package com.example.foodplanner.features.common.helpers;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public class OperationManagerImpl implements OperationManager {

    private final Map<Integer, Completable> operations = new HashMap<>();

    @Override
    public int submitOperation(Completable operation) {
        int key = operation.hashCode();
        operations.put(key, operation.doFinally(() -> {
            synchronized (operations) {
                operations.remove(key);
            }
        }));
        return key;
    }

    @Override
    public Completable retrieve(int operationKey) {
        return operations.get(operationKey);
    }

    @Override
    public void close() {
        operations.clear();
    }
}
