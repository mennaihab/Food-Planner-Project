package com.example.foodplanner.features.common.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;

public class RequiredAuthFragment extends DialogFragment {
    private final static String TAG = "RequiredAuthFragment";

    private Disposable disposable;

    public RequiredAuthFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setMessage(R.string.require_auth_to_continue)
                .setPositiveButton(android.R.string.yes, (dialogInterface, id) -> {
                    dismiss();
                    Navigation.findNavController(requireActivity(), R.id.main_nav_host)
                            .navigate(R.id.action_global_signup);
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, id) -> {
                    dismiss();
                })
                .create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
