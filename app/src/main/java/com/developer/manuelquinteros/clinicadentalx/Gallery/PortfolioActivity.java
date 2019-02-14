package com.developer.manuelquinteros.clinicadentalx.Gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Gallery;

import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {

    PortfolioAdapter mAdapter;
    RecyclerView mRecyclerView;

    ArrayList<Gallery> data = new ArrayList<>();

    public static String IMGS[] = {
      "https://images.unsplash.com/photo-1444090542259-0af8fa96557e?q=80&fm=jpg&w=1080&fit=max&s=4b703b77b42e067f949d14581f35019b",
            "https://images.unsplash.com/photo-1439546743462-802cabef8e97?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1441155472722-d17942a2b76a?q=80&fm=jpg&w=1080&fit=max&s=80cb5dbcf01265bb81c5e8380e4f5cc1",
            "https://images.unsplash.com/photo-1437651025703-2858c944e3eb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1431538510849-b719825bf08b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            "https://images.unsplash.com/photo-1434873740857-1bc5653afda8?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);



        for (int i =0; i < IMGS.length; i++) {
            Gallery gallery = new Gallery();
            gallery.setName("Image " + i);
            gallery.setUrl(IMGS[i]);
            data.add(gallery);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_portfolio);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mRecyclerView = (RecyclerView) findViewById(R.id.list_portfolio);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PortfolioAdapter(PortfolioActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PortfolioActivity.this, DetailPortfolioActivity.class);
                intent.putParcelableArrayListExtra("data", data);
                intent.putExtra("pos", position);
                startActivity(intent);

            }
        }));
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
