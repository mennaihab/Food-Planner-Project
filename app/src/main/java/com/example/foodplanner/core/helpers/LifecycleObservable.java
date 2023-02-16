package com.example.foodplanner.core.helpers;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public abstract class LifecycleObservable {
    public static Observable<Lifecycle.State> fromLifecycleOwner(LifecycleOwner lifecycleOwner) {
        return BehaviorSubject.create(emitter -> {
            lifecycleOwner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> emitter.onNext(event.getTargetState()));
        });
    }
}
