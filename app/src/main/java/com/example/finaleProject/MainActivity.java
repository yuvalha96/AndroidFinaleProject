package com.example.finaleProject;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements NotesAdapter.INotesAdapterListener {

    private static final int RECEIVE_SMS_REQUEST_CODE   = 1;
    private static final int READ_SMS_REQUEST_CODE      = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayUseLogoEnabled(true);

        askForSmsDangerousPermissions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.notes_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.exit:
                MyExit exitFragment = MyExit.newInstance();
                exitFragment.show(getSupportFragmentManager(), "exitDialog");
                break;
            case R.id.newNote:
                NewNoteDialog newNoteFragment = NewNoteDialog.newInstance();
                newNoteFragment.show(getSupportFragmentManager(), "newNoteFrag");
                break;
            case R.id.newDateNote:
                NewDateNoteDialog newDateNoteFragment = NewDateNoteDialog.newInstance();
                newDateNoteFragment.show(getSupportFragmentManager(), "newNoteFrag");
                break;
        }
        return  true;
    }

    @Override
    public void noteClicked() {
        DetailsFragment fragB;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragB = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        }
        else //I am in portrait
        {
            fragB = new DetailsFragment();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.mainActivity, fragB).//add on top of the static fragment
                    addToBackStack("BBB").//cause the back button scrolling through the loaded fragments
                    commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    private void askForSmsDangerousPermissions() {
        requestSmsDangerousPermission(Manifest.permission.READ_SMS, READ_SMS_REQUEST_CODE);
        requestSmsDangerousPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_REQUEST_CODE);
    }

    private void requestSmsDangerousPermission(String permission, int permissionRequestCode)
    {
        // check if permission already granted
        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
            return;

        // Permission is not granted. show an explanation.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
            Toast.makeText(this, "You must grant this permission in order to see SMS messages", Toast.LENGTH_LONG).show();

        // request the permission
        ActivityCompat.requestPermissions(this, new String[] { permission }, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0)
            return;

        boolean firstPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

        switch (requestCode) {
            case RECEIVE_SMS_REQUEST_CODE:
                Toast.makeText(this, "RECEIVE_SMS permission granted: " + firstPermissionGranted, Toast.LENGTH_SHORT).show();
                break;
            case READ_SMS_REQUEST_CODE:
                Toast.makeText(this, "READ_SMS permission granted: " + firstPermissionGranted, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}