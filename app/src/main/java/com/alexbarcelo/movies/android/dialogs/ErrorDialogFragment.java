package com.alexbarcelo.movies.android.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexbarcelo.movies.R;

/**
 * Dialog to alert from errors that are not planned to be reported in the activity layout.
 */

public class ErrorDialogFragment extends DialogFragment {

    private static final String ERROR_MESSAGE_ARG = "ERROR_MESSAGE";

    public ErrorDialogFragment() {
    }

    public static ErrorDialogFragment newInstance(String errorMessage) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ErrorDialogFragment.ERROR_MESSAGE_ARG, errorMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String errorMessage = getArguments().getString(ErrorDialogFragment.ERROR_MESSAGE_ARG);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_error_dialog, null);

        TextView errorMessageView = v.findViewById(R.id.error_dialog_message);
        errorMessageView.setText(errorMessage);

        Button errorPositiveButton = v.findViewById(R.id.error_dialog_positive_button);
        errorPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setView(v).create();
    }

}
