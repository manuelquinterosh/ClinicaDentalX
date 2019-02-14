package com.developer.manuelquinteros.clinicadentalx;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developer.manuelquinteros.clinicadentalx.volley.VolleyUse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegisterUserActivity extends AppCompatActivity {

    TextInputEditText txUser, txName, txEmail, txPhone, txAddress, txPassword;
    Button btJoin;
    CircleImageView imgAvatar;
    CoordinatorLayout pemise;

    private static final String FILE_MAIN ="misImagenesApp/"; //directorio principal
    private static final String FILE_IMAGE = "imagenes"; //carpeta donde se guardan las fotos
    private static final String DIRECTORY_IMAGE = FILE_MAIN + FILE_IMAGE; //ruta carpeta de directorios
    private String path; //almacena la ruta de la imagen

    //vamos a utilizar al momento de tomar la foto
    File fileImagen;
    Bitmap bitmap;

    private static final int COD_SELECT=10; //Sera la encargada de diferenciar cuando seleccionamos la opcion de cargar imagen
    private static final int COD_PHOTO=20;

    private final int MIS_PERMISSION = 100;

    private RequestQueue request;
    private VolleyUse volley;
    StringRequest stringRequest;

    ProgressDialog progress;

    String url = "https://codonticapp.000webhostapp.com/wsJSONsaveUser.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register_user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txUser = (TextInputEditText) findViewById(R.id.textUser);
        txName = (TextInputEditText) findViewById(R.id.textName);
        txEmail = (TextInputEditText) findViewById(R.id.textEmail);
        txPhone = (TextInputEditText) findViewById(R.id.textPhone);
        txAddress = (TextInputEditText) findViewById(R.id.textAddress);
        txPassword = (TextInputEditText) findViewById(R.id.textPassword);

        btJoin = (Button) findViewById(R.id.btnJoin);
        imgAvatar = (CircleImageView) findViewById(R.id.circleAvatarRegister);
        pemise = (CoordinatorLayout) findViewById(R.id.coordinatorID);

        volley = VolleyUse.getInstance(this);
        request = volley.getRequestQueue();


        if (solicitaPermisosVersionesSuperiores()) {
            pemise.setEnabled(true);
        } else {
            pemise.setEnabled(false);
        }

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesCargarAvatar();
            }
        });

        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
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

    private void opcionesCargarAvatar() {
        final CharSequence[] opciones ={"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(RegisterUserActivity.this);
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

                        startActivityForResult(intent.createChooser(intent, "Seleccione"),COD_SELECT);
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORY_IMAGE); //le mandamos la ruta o el directorio donde vamos a almacenar la foto
        boolean isCreada = miFile.exists(); //nos permitira validar si el directorio fue creado en nuestra memoria

        if (isCreada ==false) {  //en caso de que el directorio no se haya creado
            isCreada=miFile.mkdirs(); //le decimos que lo cree nuevamente
        }
        if (isCreada ==true) {  //y cuando ya nuestro directorio si fue creado. Vamos a preguntar si:

            //hacemos todo el proceso de construirla
            Long consecutivo = System.currentTimeMillis()/1000; //capturamos la fecha y hora en la que se inicia el proceso
            String nombre=consecutivo.toString()+".jpg"; //para luego ser convertida en nombre con extension .jpg

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORY_IMAGE+File.separator+nombre; //Indicamos la ruta de almacenamiento en la que se va guardar la imagen
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
            startActivityForResult(intent,COD_PHOTO);

            ////


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_SELECT:
                Uri miPath=data.getData();
                imgAvatar.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                    imgAvatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_PHOTO:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path);
                imgAvatar.setImageBitmap(bitmap);
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
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISSION);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISSION){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(RegisterUserActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT);
                imgAvatar.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }


    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegisterUserActivity.this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",RegisterUserActivity.this.getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterUserActivity.this,"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegisterUserActivity.this);
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

    ///////////////

    private void cargarWebService() {

        progress=new ProgressDialog(RegisterUserActivity.this);
        progress.setMessage("Cargando...");
        progress.show();

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progress.hide();

                if (response.trim().equalsIgnoreCase("registra")) {
                    txUser.setText("");
                    txName.setText("");
                    txEmail.setText("");
                    txPhone.setText("");
                    txAddress.setText("");
                    txPassword.setText("");

                    Toast.makeText(getApplicationContext(),  "Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
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

                Map<String, String> parametros = new HashMap<>();

                parametros.put("usuario", user);
                parametros.put("nombre", name);
                parametros.put("email", email);
                parametros.put("telefono", phone);
                parametros.put("direccion", addres);
                parametros.put("contrasena", password);
                parametros.put("avatar", avatar);
                Log.d("Estado",  user);
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyUse.addToQueue(stringRequest, request, this, volley);
    }


    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
}
