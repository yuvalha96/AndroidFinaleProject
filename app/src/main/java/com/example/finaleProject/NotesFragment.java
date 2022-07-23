package com.example.finaleProject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NotesFragment extends Fragment  {

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notesfrag, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)  {
        recyclerView = view.findViewById(R.id.recycle_view);
                super.onViewCreated(view, savedInstanceState);
    }

    //*******************************************************************
    // After activity created we can set the ADAPTER for the recycle view
    //*******************************************************************
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notesAdapter = new NotesAdapter(getActivity().getApplication(), getContext(), getActivity()); // create an instance of the adapter
        recyclerView.setAdapter(notesAdapter); // set that adapter for the recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // What is the position of the list vertical or linear

    }

    //the interface of this fragment that include the methods
    public interface NotesFragmentListener {
        //put here methods you want to utilize to communicate with the hosting activity
    }
}

