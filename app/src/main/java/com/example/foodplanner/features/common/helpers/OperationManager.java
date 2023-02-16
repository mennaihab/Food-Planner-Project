package com.example.foodplanner.features.common.helpers;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public interface OperationManager {
    int submitOperation(Completable operation);
    Completable retrieve(int operationKey);
    void close();
}
