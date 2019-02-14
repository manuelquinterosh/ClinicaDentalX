package com.developer.manuelquinteros.clinicadentalx;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.developer.manuelquinteros.clinicadentalx.model.Users;
import com.developer.manuelquinteros.clinicadentalx.prefs.UserSessionManager;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UpdateUserActivity extends AppCompatActivity {


    TextInputEditText txUser, txName, txEmail, txPhone, txAddress, txPassword;
    Button btUpdateUser, btDeleteUser;
    CircleImageView imgAvatarUser;
    CoordinatorLayout pemise;

    private RequestQueue request;
    private VolleyUse volley;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA


    //SE MODIFICA
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;

    ProgressDialog progress;


    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txUser = (TextInputEditText) findViewById(R.id.textUserModify);
        txName = (TextInputEditText) findViewById(R.id.textNameModify);
        txEmail = (TextInputEditText) findViewById(R.id.textEmailModify);
        txPhone = (TextInputEditText) findViewById(R.id.textPhoneModify);
        txAddress = (TextInputEditText) findViewById(R.id.textAddressModify);
        txPassword = (TextInputEditText) findViewById(R.id.textPasswordModify);
        pemise = (CoordinatorLayout) findViewById(R.id.baseCoordinatorUpdate);

        imgAvatarUser = (CircleImageView) findViewById(R.id.circleAvatarMod);

        btUpdateUser = (Button) findViewById(R.id.btnModify);
        btDeleteUser = (Button) findViewById(R.id.btnDelete);

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();
        session = new UserSessionManager(getApplicationContext());


        HashMap<String, String> detail = session.getUserDetail();

        final String id = detail.get(UserSessionManager.KEY_ID);

        cargarWebService(id);

        if (solicitaPermisosVersionesSuperiores()) {
            pemise.setEnabled(true);
        } else {
            pemise.setEnabled(false);
        }

        imgAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesCargarAvatar();
            }
        });

        btUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceActualizar(id);
            }
        });

        btDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceEliminar(id);
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progress!=null && progress.isShowing() ){
            progress.cancel();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void opcionesCargarAvatar() {
        final CharSequence[] opciones ={"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setTitle("Elige una Opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Tomar Foto")){ //si selecciona la opcion 1 llamamos la camara
                    //llamado a metodo para activar la camara
                    abrirCamara();
                } else {
                    if (opciones[which].equals("Elegir de Galeria")) { //Si selecciona la opcion 2 llamamos a galeria
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");

                        startActivityForResult(intent.createChooser(intent, "Seleccione"),COD_SELECCIONA);
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN); //le mandamos la ruta o el directorio donde vamos a almacenar la foto
        boolean isCreada = miFile.exists(); //nos permitira validar si el directorio fue creado en nuestra memoria

        if (isCreada ==false) {  //en caso de que el directorio no se haya creado
            isCreada=miFile.mkdirs(); //le decimos que lo cree nuevamente
        }
        if (isCreada ==true) {  //y cuando ya nuestro directorio si fue creado. Vamos a preguntar si:

            //hacemos todo el proceso de construirla
            Long consecutivo = System.currentTimeMillis()/1000; //capturamos la fecha y hora en la que se inicia el proceso
            String nombre=consecutivo.toString()+".jpg"; //para luego ser convertida en nombre con extension .jpg

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre; //Indicamos la ruta de almacenamiento en la que se va guardar la imagen
            fileImagen=new File(path); //luego construimos el archivo

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getApplicationContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getApplicationContext(),authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent,COD_FOTO);

            ////
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                imgAvatarUser.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                    imgAvatarUser.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path);
                imgAvatarUser.setImageBitmap(bitmap);
                break;
        }
    }

    //permisos
    ////////////////

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getApplicationContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getApplicationContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(UpdateUserActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                imgAvatarUser.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(UpdateUserActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",UpdateUserActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(UpdateUserActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(UpdateUserActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);

            }
        });
        dialogo.show();
    }

    private void cargarWebService(String id_usuario) {

        progress=new ProgressDialog(UpdateUserActivity.this);
        progress.setMessage("Cargando...");
        progress.show();

        String url = "https://codonticapp.000webhostapp.com/wsDetailUser.php?idUser="+ id_usuario;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();

                Users user = new Users();

                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    user.setUsuario(jsonObject.optString("usuario"));
                    user.setNombre(jsonObject.optString("nombre"));
                    user.setEmail(jsonObject.optString("email"));
                    user.setTelefono(jsonObject.optString("telefono"));
                    user.setDireccion(jsonObject.optString("direccion"));
                    user.setContrasena(jsonObject.optString("contrasena"));
                    user.setDato(jsonObject.optString("avatar"));

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                txUser.setText(user.getUsuario());
                txName.setText(user.getNombre());
                txEmail.setText(user.getEmail());
                txPhone.setText(user.getTelefono());
                txAddress.setText(user.getDireccion());
                txPassword.setText(user.getContrasena());

                String urlImagen = "http://codonticapp.000webhostapp.com/imagenes/" + user.getUsuario()+".jpg";
                Log.d("Direccion Imagen: ", urlImagen);

                cargarWebServiceImagen(urlImagen);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateUserActivity.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        });

        VolleyUse.addToQueue(jsonObjectRequest, request, this, volley);

    }

    private void cargarWebServiceImagen(String urlImagen) {

        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap =response; //SE MODIFICA
                imgAvatarUser.setImageBitmap(response);

            }
        }, 0, 0, CircleImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateUserActivity.this,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                Log.i("ERROR IMAGEN","Response -> "+error);
            }
        });
        VolleyUse.addToQueue(imageRequest, request, this, volley);
    }

    private void webServiceActualizar(final String id_actualizar) {
        progress = new ProgressDialog(UpdateUserActivity.this);
        progress.setMessage("Cargando...");
        progress.show();

        String urlUpdate = "https://codonticapp.000webhostapp.com/wsUpdateUser.php?idUser=" + id_actualizar;

        stringRequest=new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progress.hide();
                Log.i("RESPUESTA: ",""+response);
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(UpdateUserActivity.this,"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateUserActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(UpdateUserActivity.this,"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateUserActivity.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String user=txUser.getText().toString();
                String name=txName.getText().toString();
                String email=txEmail.getText().toString();
                String phone=txPhone.getText().toString();
                String addres=txAddress.getText().toString();
                String password=txPassword.getText().toString();

                String avatar = convertirImgString(bitmap);

                session.createUserLoginSession(user, name, email, phone, addres, password, id_actualizar);

                Map<String, String> parametros = new HashMap<>();

                parametros.put("usuario", user);
                parametros.put("nombre", name);
                parametros.put("email", email);
                parametros.put("telefono", phone);
                parametros.put("direccion", addres);
                parametros.put("contrasena", password);
                parametros.put("avatar", avatar);

                return parametros;
            }
        };

        VolleyUse.addToQueue(stringRequest, request, this, volley);

    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private void webServiceEliminar(String id_eliminar) {

        progress=new ProgressDialog(UpdateUserActivity.this);
        progress.setMessage("Cargando...");
        progress.show();

        String url = "https://codonticapp.000webhostapp.com/wsDeleteAccountUser.php?idUser="+id_eliminar;

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    txUser.setText("");
                    txName.setText("");
                    txEmail.setText("");
                    txPhone.setText("");
                    txAddress.setText("");
                    txPassword.setText("");

                    imgAvatarUser.setImageResource(R.drawable.avataruser);
                    Toast.makeText(getApplicationContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getApplicationContext(),"No se encuentra la persona ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplicationContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        });

    }
}
