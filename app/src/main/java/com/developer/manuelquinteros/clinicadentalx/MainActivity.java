package com.developer.manuelquinteros.clinicadentalx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.developer.manuelquinteros.clinicadentalx.Contact.ContactActivity;
import com.developer.manuelquinteros.clinicadentalx.FCM.PromotionsActivity;
import com.developer.manuelquinteros.clinicadentalx.Gallery.PortfolioActivity;
import com.developer.manuelquinteros.clinicadentalx.Maps.MapsActivity;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;

public class MainActivity extends AppCompatActivity {

    RelativeLayout goToAppointment, goToPromotions, goToLocation, goToContact, goToProfile, goToPortfolio;

    private ViewFlipper viewFlipper;
    private Animation fadeIn, fadeOut;

    boolean bandera=true;

    UserSessionManager session;

    private Button Logout;

    private static final String PROCESS_OK = "PROCESS_OK";
    private static final String PROCESS_ERROR = "PROCESS_ERROR";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Procesando....");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);



        session = new UserSessionManager(getApplicationContext());

        // Redirección al Login
        if (!session.isUserLogedIn()) {
            logoutState();
        }

        goToAppointment = (RelativeLayout) findViewById(R.id.goAppointment);
        goToPromotions = (RelativeLayout)findViewById(R.id.goPromotions);
        goToLocation = (RelativeLayout) findViewById(R.id.goLocation);
        goToContact = (RelativeLayout) findViewById(R.id.goContact);
        goToProfile = (RelativeLayout) findViewById(R.id.goProfile);
        goToPortfolio = (RelativeLayout) findViewById(R.id.goPortfolio);

        Logout = (Button) findViewById(R.id.btnLogout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutState();
            }
        });

        goToAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
                startActivity(intent);

            }
        });

        goToPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });

        goToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        goToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        goToPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HardTask downloadTask = new HardTask(MainActivity.this);
                downloadTask.execute("some_param");

                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true);
                    }
                });
            }
        });

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        viewFlipper.setInAnimation(fadeIn);
        viewFlipper.setOutAnimation(fadeOut);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();

    }


    private void logoutState() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        session.logout();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private class HardTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public HardTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i <= 200; i++) {
                publishProgress(i);
                try {
                    // simulate hard work
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return PROCESS_ERROR;
                }
            }
            return PROCESS_OK;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
            startActivity(intent);

        }
    }
}
