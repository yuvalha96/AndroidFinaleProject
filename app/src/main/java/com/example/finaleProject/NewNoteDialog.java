package com.example.finaleProject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NewNoteDialog extends DialogFragment {

    public static NewNoteDialog newInstance() {
        NewNoteDialog fragment = new NewNoteDialog();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        return new AlertDialog.Builder(getActivity()).setView(inflater.inflate(R.layout.new_note_title, null))
                .setTitle("Create a new note")
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // create a new note
                                EditText newTitle = ((AlertDialog) dialog).findViewById(R.id.newTitle);
                                MainViewModel myViewModel = MainViewModel.getInstance(getActivity().getApplication(), getContext(), getActivity());
                                myViewModel.addNewNote(newTitle.getText().toString(), null);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // close the dialog box
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
