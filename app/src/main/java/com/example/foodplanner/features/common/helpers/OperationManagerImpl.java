package com.example.foodplanner.features.common.helpers;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

public class OperationManagerImpl implements OperationManager {

    private final Map<Integer, Operation<?>> operations = new HashMap<>();
    private final Handler handler = new Handler(Looper.myLooper());

    @Override
    public int submitOperation(Operation<?> operation) {
        int key = operation.hashCode();
        operations.put(key, operation);
        Operation.Canceller[] canceller = new Operation.Canceller[]{null};
        canceller[0] = operation.addListener((a, e) -> handler.post(() -> {
            synchronized (operations) {
                operations.remove(key);
                canceller[0].cancel();
            }
        }));
        return key;
    }

    @Override
    public Operation<?> retrieve(int operationKey) {
        return operations.get(operationKey);
    }

    @Override
    public void close() {
        operations.clear();
        handler.removeCallbacksAndMessages(null);
    }
}
