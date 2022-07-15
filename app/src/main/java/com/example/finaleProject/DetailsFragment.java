package com.example.finaleProject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private TextView gghghghhghghg;
    private TextView gf;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // lab 9
        boolean checkBoxFilter =  PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("remember", false);

/** *
 * https://developer.android.com/topic/libraries/architecture/livedata
 */

/***
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing
 */

        titleTextView = view.findViewById(R.id.tvNoteTitle);
        contentTextView = view.findViewById(R.id.tvNoteContent);
        dueDateTextView = view.findViewById(R.id.tvDueDate);
        myViewModel = MainViewModel.getInstance(getActivity().getApplication(),getContext(), getActivity(), checkBoxFilter);



        // OBSERVE
        // Here we will observe and update the selected row
        Observer<Integer> observeSelectedIndex = new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                int selected = myViewModel.getPositionSelected().getValue();
                ArrayList<Note> list  = myViewModel.getNoteLiveData().getValue();
                if(selected != -1){
                    Note note = list.get(selected);

                    if(note != null){
                        titleTextView.setText(note.getTitle());
                        contentTextView.setText(note.getContent());
                        if (note.getDueDate() != null)
                            dueDateTextView.setText(note.getDueDate().toString());
                    }
                }else{
                    titleTextView.setText("");
                }
            }
        };

        myViewModel.getPositionSelected().observe((LifecycleOwner)getContext(), observeSelectedIndex);

        super.onViewCreated(view, savedInstanceState);

    }

}
