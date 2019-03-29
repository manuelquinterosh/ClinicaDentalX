package com.developer.manuelquinteros.clinicadentalx.FCM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.developer.manuelquinteros.clinicadentalx.LoginActivity;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.adapter.ItemClickListener;
import com.developer.manuelquinteros.clinicadentalx.adapter.NotificationsAdapter;
import com.developer.manuelquinteros.clinicadentalx.model.Notifications;
import com.developer.manuelquinteros.clinicadentalx.prefs.DatabaseHelper;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity implements ItemClickListener {


    public static final String ACTION_NOTIFY_NEW_PROMO = "NOTIFY_NEW_PROMO";


    private RecyclerView mRecyclerView;
    private NotificationsAdapter mNotificatiosAdapter;
    private View mEmptyView;

    List<Notifications> list = new ArrayList<>();
    private DatabaseHelper db;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_promotions);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new DatabaseHelper(this);

        session = new UserSessionManager(getApplicationContext());

        // Redirección al Login
        if (!session.isUserLogedIn()) {
            session.logout();
            //Starting login activity
            Intent intent = new Intent(PromotionsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        list.addAll(db.getAllNotifications());

        mEmptyView = findViewById(R.id.noMessagesNotification);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_notifications_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PromotionsActivity.this));
        mNotificatiosAdapter = new NotificationsAdapter(list,  PromotionsActivity.this,PromotionsActivity.this);
        mNotificatiosAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mNotificatiosAdapter);


        //Mostrar un mensaje si la lista esta vacía [cuando carga la app]
        int numItems =  mNotificatiosAdapter.getItemCount();
        if(numItems == 0)
            emptyList();
        else
            dateList();


        mNotificatiosAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                if(itemCount == 0)
                    emptyList();
                else
                    dateList();
            }

        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(PromotionsActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });

    }



    @Override
    public void OnItemDeleteClick(int position, Notifications delete) {
         deleteNotification(position);
    }

    @Override
    public void OnItemClickNotification(int position, Notifications id) {
       detailNotification(position);
    }

    private void detailNotification(int position) {

        Intent intent = new Intent(PromotionsActivity.this, NotificationDetailActivity.class);
        intent.putExtra("id", list.get(position).getId());
        intent.putExtra("title", list.get(position).getmTitle());
        intent.putExtra("description", list.get(position).getmDescription());
        intent.putExtra("expiry_date", list.get(position).getmExpiryDate());
        intent.putExtra("discount", String.valueOf(String.format("%d%%", (int) (list.get(position).getmDiscount()* 100))));
        startActivity(intent);
    }

    private void deleteNotification(int position) {
        //deleting the crud from db
        db.deleteNotifications(list.get(position));

        //removing the crud from the list
        list.remove(position);
        mNotificatiosAdapter.notifyItemRemoved(position);

        //Mostrar un mensaje si la lista esta vacía [cuando carga la app]
        int numItems =  mNotificatiosAdapter.getItemCount();
        if(numItems == 0)
            emptyList();
        else
            dateList();


        mNotificatiosAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                if(itemCount == 0)
                    emptyList();
                else
                    dateList();
            }

        });


    }

    public void emptyList(){

        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void dateList(){

        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}

