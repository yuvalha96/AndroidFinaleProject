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

    // ******* The observable vars *********************

    //  The MutableLiveData class has the setValue(T) and postValue(T)
    //  methods publicly and you must use these if you need to edit the value stored in a LiveData object.
    //  Usually, MutableLiveData is used in the ViewModel and then the ViewModel only exposes

    private MutableLiveData<ArrayList<Note>> noteLiveData;
    private MutableLiveData<Integer> positionSelected;
    // *****************************
    // lab 9
//    private MutableLiveData<Boolean> saveRemoved;
    private int[] intArr;
//    private ArrayList<String> removedNoteList = new ArrayList<>();



    private MainViewModel(@NonNull Application application, Context context, Activity activity, boolean checkBoxFilter) {
        super(application);
        //super(application);
        // call your Rest API in init method
        this.activity = activity;
        this.context = context;

        // Lab 8 + Lab 9
        init(application, checkBoxFilter);


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

//    public boolean getSaveRemove(){
//        return saveRemoved.getValue();
//    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application, Context context, Activity activity, boolean checkBoxFilter){
        if(instance ==null){
            instance = new MainViewModel(application, context, activity, checkBoxFilter);
        }
        return instance;
    }

    // This use the setValue
    public void init(Application application, boolean checkBoxFilter){
        noteLiveData = new MutableLiveData<>();
        ArrayList<Note> tempNoteList = getNotesFromSP();

        if (tempNoteList == null) {
            tempNoteList = new ArrayList<Note>();
        }

        Note smsNote = getSMSfromFile();
        if (smsNote != null)
            tempNoteList.add(0, smsNote);

        noteLiveData.setValue(tempNoteList);
        Log.d("yuval", "init: " + noteLiveData.getValue()); //prints the notes but still not working


        positionSelected = new MutableLiveData<>();
        positionSelected.setValue(-1);



        //checkRemoveList(application); // this is also connect to lab 8 and 9
    }

    private ArrayList<Note> getNotesFromSP() {
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


//    // Lab 8 (only set the country list) + Lab 9 ( remove from original list the remove country)
//    public void checkRemoveList(Application application){
//        ArrayList<Note> notesList = new ArrayList<Note>();
//
//
//        if(saveRemoved.getValue()) {
//
//            String s = getRemoveListByFile();
//            //String s = getRemoveListBySP();
//
//            String[] removeArray = s.split(",",notesList.size());
//                for(int i = 0; i < removeArray.length; i++) {
//                    int finalI = i;
//                    //notesList.removeIf(obj -> obj.getName().equals(removeArray[finalI]));
//                }
//        }else{// clear files
//            clearListByFile();
//            //clearListBySP();
//        }
//        noteLiveData.setValue(notesList);
//
//
//    }

    // ******************** file ************
//    public String getRemoveListByFile() {
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("remove.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                int size = inputStream.available();
//                char[] buffer = new char[size];
//
//                inputStreamReader.read(buffer);
//
//                inputStream.close();
//                ret = new String(buffer);
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return ret;
//    }

//    public void setRemoveListByFile(String name)
//    {
//        if(!removedNoteList.contains(name)){
//            String removelist = getRemoveListByFile();
//            if(removelist.length() == 0)
//                removelist = name;
//            else{
//                removelist += "," + name;
//            }
//            try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("remove.txt", Context.MODE_PRIVATE));
//                outputStreamWriter.write(removelist);
//                outputStreamWriter.flush();
//                outputStreamWriter.close();
//            }
//            catch (IOException e) {
//                Log.e("Exception", "File write failed: " + e.toString());
//            }
//        }
//
//    }

//
//    private void clearListByFile() {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("remove.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write("");
//            outputStreamWriter.flush();
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//
//    }


    // ******************* SP **********************
//    public void setRemoveListBySP(String name) {
//
//        if (!removedNoteList.contains(name)) {
//            String removelist = getRemoveListBySP();
//            if (removelist.length() == 0)
//                removelist = name;
//            else
//                removelist += "," + name;
//
//            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("removelist", removelist);
//            editor.apply();
//
//        }
//    }
//
//    public String getRemoveListBySP() {
//
//        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
//        return sharedPref.getString("removelist", "");
//    }
//
//    private void clearListBySP() {
//
//        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("removelist", "");
//        editor.apply();
//    }

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
        Log.d("yuval", "addNewNote: " + noteLiveData.getValue());
        for (int i = 0 ; i < noteList.size(); i++) {
            Log.d("yuval", "addNewNote: " + i + " " + noteLiveData.getValue().get(i).getTitle());
        }
        saveToSp(noteList);
    }

    public void saveToSp(ArrayList<Note> noteList) {
        if (noteList.get(0).isSmsNote()) {
            noteList.remove(0);
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
        Log.d("yuval", "addNewSMSNote: "+ noteLiveData.getValue());
        for (int i = 0 ; i < noteList.size(); i++) {
            Log.d("yuval", "addNewNote: " + i + " " + noteList.get(i).getTitle());
        }

        Gson gson = new Gson();
        String json = gson.toJson(newNote);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("sms.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
