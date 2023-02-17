package com.example.foodplanner.features.common.helpers;

import com.example.foodplanner.features.common.services.OperationManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;

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
