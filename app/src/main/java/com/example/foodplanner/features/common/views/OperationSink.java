package com.example.foodplanner.features.common.views;

import io.reactivex.rxjava3.core.Completable;

public interface OperationSink {
    int submitOperation(Completable operation);
    Completable retrieve(int operationKey);
}
