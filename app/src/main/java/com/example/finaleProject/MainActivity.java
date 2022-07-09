package com.example.finaleProject;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements NotesAdapter.INotesAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.notes_settings, menu);
        //inflater.inflate(R.menu.settings, menu);
        //inflater.inflate(R.menu.menu_exit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.exit:
                MyExit exitFragment = MyExit.newInstance();
                exitFragment.show(getSupportFragmentManager(), "exitDialog");
                break;
            case R.id.NotesSettings:
                break;
            case R.id.settings:
                FragmentManager fm = getSupportFragmentManager();
                Fragment toHide = fm.findFragmentById(R.id.frag_container);
                FragmentTransaction ft = fm.beginTransaction();
                if (toHide != null) {
                    ft.hide(toHide);    // hide main fragment.
                }

                // This is the parent activity
                // Pay attention on note that the SettingFragment has
                ft.add(R.id.mainActivity, new SettingFragment())
                        .addToBackStack(null)
                        .commit();
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragB = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
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



    // Nested class to show settings frag
    //Note: This class is Knows how to automatically and independently handle all the clicks
    // and changes that the user makes and saves them in a Preference file
    public static class SettingFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_pref, rootKey);
        }

    }
}
