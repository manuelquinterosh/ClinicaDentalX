package com.developer.manuelquinteros.clinicadentalx.Contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.developer.manuelquinteros.clinicadentalx.Contact.fragment.FacebookFragment;
import com.developer.manuelquinteros.clinicadentalx.Contact.fragment.TwitterFragment;
import com.developer.manuelquinteros.clinicadentalx.Contact.fragment.WorldFragment;
import com.developer.manuelquinteros.clinicadentalx.R;

public class ContactActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FrameLayout frameLayout;

    private FacebookFragment facebookFragment;
    private TwitterFragment twitterFragment;
    private WorldFragment worldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       navigationView = (BottomNavigationView)findViewById(R.id.bottom_nav);
       frameLayout = (FrameLayout) findViewById(R.id.frame);

        facebookFragment = new FacebookFragment();
        twitterFragment = new TwitterFragment();
        worldFragment = new WorldFragment();

       navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId()) {
                   case R.id.facebook:
                       setFragment(facebookFragment);
                       return true;
                   case R.id.twitter:
                       setFragment(twitterFragment);
                       return true;
                   case R.id.world:
                       setFragment(worldFragment);
                       return true;

                       default:
                           return false;
               }
           }
       });

       navigationView.setSelectedItemId(R.id.facebook);

//https://www.youtube.com/watch?v=8qwaLC1_Trw

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_toolbar_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_phone:
                //openSearch();
                String phone = "+50375020637";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return true;
            case R.id.action_email:
                //openSettings();
                String[] TO = {"manuelq_hernandez@outlook.com"};
                Uri uri = Uri.parse("mailto:manuelq_hernandez@outlook.com")
                        .buildUpon()
                        .appendQueryParameter("subject", "subject")
                        .appendQueryParameter("body", "body")
                        .build();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
