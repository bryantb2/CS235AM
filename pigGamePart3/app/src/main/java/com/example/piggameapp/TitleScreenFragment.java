package com.example.piggameapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TitleScreenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View view = inflater.inflate(R.layout.title_screen_fragment, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //mounting the XML to the main activity
        //will convert the XML items to java objects that can be displayed in the activity
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //determines which menu item was selected based off element IDs
            case R.id.about:
                Toast.makeText(getActivity(),"About", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(getActivity(),"Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                return true;
            default:
                //allows for default OptionsItemSelected behavior from the super class
                return super.onOptionsItemSelected((item));
        }
    }
}
