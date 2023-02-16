package com.example.foodplanner.features.common.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.helpers.Operation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;

public class LoadingFragment extends DialogFragment implements OnBackPressedListener {
    private final static String TAG = "LoadingFragment";

    private OperationSink operationSink;
    private Operation.Canceller disposable;

    public LoadingFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        operationSink = (OperationSink) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setCancelable(false);
        int operationKey = LoadingFragmentArgs.fromBundle(requireArguments()).getOperationKey();
        Operation<?> operation = operationSink.retrieve(operationKey);
        if (operation == null) {
            dismiss();
        } else {
            disposable = operation.addListener((a, b) -> {
                new Handler(Looper.myLooper()).post(this::dismiss);
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireActivity())
                .setView(R.layout.fragment_loading)
                .setCancelable(false)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onDestroy() {
        if (disposable != null) disposable.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        Log.d(TAG, "onBackPressed");
        return !isCancelable();
    }
}
