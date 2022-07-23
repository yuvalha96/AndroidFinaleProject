package com.example.finaleProject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;

    public Context context;
    public Activity activity;
    private MutableLiveData<ArrayList<Note>> noteLiveData;
    private MutableLiveData<Integer> positionSelected;
    private int[] intArr;



    private MainViewModel(@NonNull Application application, Context context, Activity activity) {
        super(application);

        this.activity = activity;
        this.context = context;

        init(application);
    }

    public MutableLiveData<ArrayList<Note>> getNoteLiveData() {
        return noteLiveData;
    }

    public void setPositionSelected(Integer index){
        positionSelected.setValue(index);
    }

    public MutableLiveData<Integer> getPositionSelected(){
        return positionSelected;
    }

    public void setNoteLiveData(ArrayList<Note> list){
        noteLiveData.setValue(list);
    }


    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application, Context context, Activity activity){
        if(instance ==null){
            instance = new MainViewModel(application, context, activity);
        }
        return instance;
    }

    // This use the setValue
    public void init(Application application ){
        noteLiveData = new MutableLiveData<>();
        ArrayList<Note> tempNoteList = getNotesFromSP();

        if (tempNoteList == null) {
            tempNoteList = new ArrayList<Note>();
        }

        Note smsNote = getSMSfromFile();
        if (smsNote != null)
            tempNoteList.add(0, smsNote);

        noteLiveData.setValue(tempNoteList);

        positionSelected = new MutableLiveData<>();
        positionSelected.setValue(-1);

    }

    public ArrayList<Note> getNotesFromSP() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type arrayListNoteType = new TypeToken<ArrayList<Note>>() {}.getType();
        ArrayList<Note> staff = gson.fromJson(sharedPref.getString("noteListJson", ""), arrayListNoteType);
        return staff;
    }

    private Note getSMSfromFile() {
        String json = "";

        try {
            InputStream inputStream = context.openFileInput("sms.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int size = inputStream.available();
                char[] buffer = new char[size];

                inputStreamReader.read(buffer);

                inputStream.close();
                json = new String(buffer);
                if (json == "") {
                    return null;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Note staff = gson.fromJson(json, (new Note("")).getClass());
        return staff;
    }


    public void addNewNote(String title, Date date) {
        Note newNote = new Note(title);
        if (date != null) {
            newNote.setDueDate(date);
            // setup alarm for notification
            AlarmManager myAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent_min = new Intent(context, NotificationService.class);
            alarmIntent_min.putExtra("noteTitle", title);

            PendingIntent alarmPendingIntent_min = PendingIntent.getService(context, 668, alarmIntent_min, 0);
            Intent alarmInfoIntent = new Intent(context, MainActivity.class);
            PendingIntent alarmInfoPendingIntent = PendingIntent.getActivity(context, 777,alarmInfoIntent,0);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(date.getTime(), alarmInfoPendingIntent);
            myAlarmManager.setAlarmClock(alarmClockInfo, alarmPendingIntent_min);
        }
        ArrayList<Note> noteList = getNoteLiveData().getValue();
        noteList.add(newNote);
        setNoteLiveData(noteList); // check if this line is necessary
        saveToSp(noteList);
    }

    public void saveToSp(ArrayList<Note> noteList) {
        if(noteList.size()>0){
            if (noteList.get(0).isSmsNote()) {
                // can't remove here, because it will change the live data. so make a deep copy instead
                ArrayList<Note> newNoteList = new ArrayList<>();
                for (int i = 1; i < noteList.size(); i++) {
                  newNoteList.add(noteList.get(i));
                }
                noteList = newNoteList;
            }
        }
        Gson gson = new Gson();
        Type arrayListNoteType = new TypeToken<ArrayList<Note>>() {}.getType();
        String json = gson.toJson(noteList, arrayListNoteType);
        // save to SP
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("noteListJson", json);
        editor.apply();
    }

    public void addNewSMSNote(String sender, String content, Date date) {
        Note newNote = new Note(sender);
        newNote.setContent(content);
        newNote.setDueDate(date);
        newNote.setSmsNote();
        ArrayList<Note> noteList = noteLiveData.getValue();
        if (noteList.size() > 0 && noteList.get(0).isSmsNote()) {
            noteList.remove(0);
        }
        noteList.add(0,newNote);
        setNoteLiveData(noteList);

        Gson gson = new Gson();
        String json = gson.toJson(newNote);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("sms.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }

}