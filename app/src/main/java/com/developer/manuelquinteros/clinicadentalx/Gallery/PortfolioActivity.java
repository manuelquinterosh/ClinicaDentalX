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
      "https://www.ula.edu.mx/images/oferta_academica/modalidad_presencial/posgrados_presenciales/maestrias/ula_posgrados_maestrias_odontologia_01.jpg",
            "http://www.colegiodentistas.org/sitCol/wp-content/uploads/2015/09/Incorporacio%CC%81n-Odonto%CC%81logo-GENERAL-Costarricense.jpg",
            "http://unicatolicaquixada.edu.br/wp-content/uploads/2016/04/ATENDIMENTO-ODONTOLOGIA-UNICAT%C3%93LICA-QUIXAD%C3%81.png",
            "http://innova.unab.cl/wp-content/uploads/2016/07/DecanoOdontologia1.jpg",
            "http://www.login.cl/images/2015/06/497f.jpg",
            "http://sescam.castillalamancha.es/sites/sescam.castillalamancha.es/files/consulta_de_odontologia_en_a._primaria_04.jpg",
            "http://sescam.castillalamancha.es/sites/sescam.castillalamancha.es/files/notas_de_prensa/fotografias/20140603/consulta_de_odontologia_en_c.s._talavera_v_03-06-14.jpg",
            "http://ss.pue.gob.mx/wp-content/uploads/2018/02/Donacion_Multiorganica.jpg",
            "http://fotos.e-consulta.com/odontologia.jpg",
            "http://revistasentidosconvalores.com/wp-content/uploads/2017/11/semana-de-la-salud-veterinaria-2017-uanl-agronomia-8-800x445.jpg",
            "http://bajio.delasalle.edu.mx/noticias/images/867_3.jpg",
            "https://i2.wp.com/estudidentalbarcelona.com/wp-content/uploads/2016/08/Cirugia-dental-quiste-radicular.jpg?fit=900%2C500&ssl=1",
            "https://www.saludyalimentacion.com/wp-content/uploads/2018/12/cirug%C3%ADa-bucal.jpg",
            "http://dentalcarecenterrd.com/wp-content/uploads/2016/05/cirugia-01.jpg",
            "https://unitecmx-universidadtecno.netdna-ssl.com/wp-content/uploads/2017/08/aprendizaje-odontologia-unitec-800x533.jpg"
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
