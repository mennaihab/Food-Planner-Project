package com.example.foodplanner.features.common.views;

import com.example.foodplanner.features.common.helpers.Operation;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public interface OperationSink {
    int submitOperation(Operation<?> operation);
    Operation<?> retrieve(int operationKey);
}
