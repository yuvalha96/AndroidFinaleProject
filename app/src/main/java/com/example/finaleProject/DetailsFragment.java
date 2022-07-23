package com.example.finaleProject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    private TextView titleTextView;
    private TextView contentTextView;
    private TextView dueDateTextView;
    private MainViewModel myViewModel;
    private EditText noteText;
    private Note note;
    ArrayList<Note> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        titleTextView = view.findViewById(R.id.tvNoteTitle);
        dueDateTextView = view.findViewById(R.id.tvDueDate);
        noteText = view.findViewById(R.id.edNoteContent);

        myViewModel = MainViewModel.getInstance(getActivity().getApplication(),getContext(), getActivity());



        // OBSERVE
        // Here we will observe and update the selected row
        Observer<Integer> observeSelectedIndex = new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                int selected = myViewModel.getPositionSelected().getValue();
                list  = myViewModel.getNoteLiveData().getValue();
                if(selected != -1){
                    note = list.get(selected);
                    noteText.setVisibility(View.VISIBLE);
                    if(note != null){
                        titleTextView.setText(note.getTitle());
                        noteText.setText(note.getContent());
                        noteText.addTextChangedListener(new handleTextChanged());
                        if (note.getDueDate() != null)
                            dueDateTextView.setText(note.getDueDate().toString());
                        else
                            dueDateTextView.setText("");
                    }
                }else{
                    titleTextView.setText("No note was chosen");
                    noteText.setVisibility(View.GONE);
                }
            }
        };

        myViewModel.getPositionSelected().observe((LifecycleOwner)getContext(), observeSelectedIndex);

        super.onViewCreated(view, savedInstanceState);

    }

    public class handleTextChanged  implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String input = noteText.getText().toString();
            note.setContent(input);
            myViewModel.saveToSp(list);
        }
    }
}