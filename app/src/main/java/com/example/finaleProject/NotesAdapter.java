package com.example.finaleProject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private static ArrayList<Note> noteArrayList;
    private int selectedRow = -1;
    private MainViewModel myViewModel;
    private Application Mycontext;
    private INotesAdapterListener listener;
    private NotesViewHolder viewHolder;
    private Context context;


    public NotesAdapter(Application application, Context context, Activity activity, boolean checkBoxFilter) {

        Mycontext = application;
        myViewModel = MainViewModel.getInstance(application, Mycontext, activity, checkBoxFilter);
        noteArrayList = myViewModel.getNoteLiveData().getValue();
        this.context = context;

        //observe data changes
        Observer<ArrayList<Note>> observeDataChanges = new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> list) {
                noteArrayList = list;
                notifyDataSetChanged();
            }
        };

        myViewModel.getNoteLiveData().observe((LifecycleOwner)context, observeDataChanges);
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listener = (INotesAdapterListener)parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // instance of the inflater
        View noteView = inflater.inflate(R.layout.note_item, parent, false); // get view of the country view object
        viewHolder = new NotesViewHolder(noteView);

        return viewHolder; // return country view holder
    }


    // this call by recyclerView for each row in in view
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = noteArrayList.get(position);

        // OBSERVE
        // Here we will observe and update the selected row
        Observer<Integer> observeSelectedIndex = new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                selectedRow = index;
            }
        };

        myViewModel.getPositionSelected().observe((LifecycleOwner)context, observeSelectedIndex);

        if (selectedRow == position){
            holder.row_linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.row_linearLayout.setBackgroundColor(Color.parseColor("#03A9F4"));
        }


        holder.row_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRow = position;
                notifyItemChanged(selectedRow);
                notifyDataSetChanged();
                myViewModel.setPositionSelected(selectedRow); // Lab 8
                listener.noteClicked(); // This what will open the frag from the MainActivity listener
            }
        });

        //Long Press
//        holder.row_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                int position = holder.getAdapterPosition();
//
//                boolean removeList =  PreferenceManager.getDefaultSharedPreferences(Mycontext).getBoolean("remember", false);
//                if(removeList) {
//                    String removeNoteName = noteArrayList.get(position).getTitle();
//                    //myViewModel.setRemoveListBySP(removeNoteName);
//                    myViewModel.setRemoveListByFile(removeNoteName);
//                }
//
//                // ************ lab 8
//                noteArrayList.remove(position);
//
//                myViewModel.setNoteLiveData(noteArrayList);
//
//                // Here we do some logic
//                // if the position equals to the current selected row so we need to unselected completely the selected row
//                if(position == myViewModel.getPositionSelected().getValue()){
//                    myViewModel.setPositionSelected(-1);
//                }
//
//                if(position < myViewModel.getPositionSelected().getValue()){
//                    myViewModel.setPositionSelected(myViewModel.getPositionSelected().getValue()-1);
//                }
//
//
//                notifyDataSetChanged();
//                return true;
//            }
//        });

        holder.bindData(note.getTitle(), note.getDueDate());
    }

    // This function just tell to the recycler view how many items in the data
    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }



    // Each row in RecyclerView will get reference of this CountriesViewHolder
    // *** Include the function that can remove the row
    public class NotesViewHolder extends RecyclerView.ViewHolder
    {
        private final Context   context;
        private final View noteItem;
        private final TextView  nameTextView;
        private final TextView dueDateTextView;
        private LinearLayout row_linearLayout;

        public LinearLayout getRow(){
            return row_linearLayout;
        }
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            context             = itemView.getContext();
            noteItem = itemView.findViewById(R.id.note_item);
            nameTextView        = itemView.findViewById(R.id.titleTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            row_linearLayout    = itemView.findViewById(R.id.note_item);
        }

        //******** This function bind\connect the row widgets with the data
        public void bindData(String title, Date dueDate){
            nameTextView.setText(title);
            if (dueDate != null) {
                dueDateTextView.setText(dueDate.toString());
            }
        }



    }

    private static int getDrawableId(Context context, String drawableName) {
        Resources resources = context.getResources();
       return resources.getIdentifier(drawableName, "drawable", context.getPackageName());
    }



    public interface INotesAdapterListener {
        void noteClicked();
    }
}
