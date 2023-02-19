package com.example.foodplanner.core.utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public abstract class FirestoreUtils {
    public static <T> Flowable<List<T>> toFlowableList(@NonNull CollectionReference collectionRef, Class<T> clazz) {
        return Flowable.create(emitter -> {
            ListenerRegistration registration = collectionRef.addSnapshotListener((value, error) -> {
                if (value != null) {
                    emitter.onNext(value.getDocuments()
                            .stream().map(snapshot -> snapshot.toObject(clazz))
                            .collect(Collectors.toList()));
                } else if (error != null) {
                    emitter.onError(error);
                }
            });
            emitter.setCancellable(registration::remove);
        }, BackpressureStrategy.LATEST);
    }

    public static <T> Flowable<Optional<T>> toFlowable(@NonNull DocumentReference documentRef, Class<T> clazz) {
        return Flowable.create(emitter -> {
            ListenerRegistration registration = documentRef.addSnapshotListener((value, error) -> {
                if (value != null) {
                    emitter.onNext(Optional.ofNullable(value.toObject(clazz)));
                } else if (error != null) {
                    emitter.onError(error);
                }
            });
            emitter.setCancellable(registration::remove);
        }, BackpressureStrategy.LATEST);
    }

    @SafeVarargs
    public static <T> Completable insertAll(@NonNull CollectionReference collectionRef,
                                            @NonNull Function<T, String> getId,
                                            T... items) {
        return Completable.create(emitter -> {
            collectionRef.getFirestore().runBatch(batch -> {
                Arrays.stream(items).forEach(item -> {
                    batch.set(collectionRef.document(getId.apply(item)), item);
                });
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onComplete();
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }

    public static <T> Completable insertAll(@NonNull CollectionReference collectionRef,
                                            @NonNull Function<T, String> getId,
                                            List<T> items) {
        return Completable.create(emitter -> {
            collectionRef.getFirestore().runBatch(batch -> {
                items.forEach(item -> {
                    batch.set(collectionRef.document(getId.apply(item)), item);
                });
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onComplete();
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }
}
