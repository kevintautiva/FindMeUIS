package com.example.app1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.app1.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    //private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    final String[] PERMISOS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, //Agregado por camerax
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> MultiplesPermisosLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        getSupportActionBar().setTitle("Find Me UIS");
        //cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        /*cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());*/


        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        //Solicita los permisos
        MultiplesPermisosLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            Log.d("PERMISOS", "Resultado del launcher: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Log.d("PERMISOS", "Al menos uno de los permisos no fue consedido, ejecutando de nuevo...");
                //Toast.makeText(getApplicationContext() , "Permita el acceso para poder analizar la imagen",Toast.LENGTH_LONG).show(); //Muestra el texto
                //MultiplesPermisosLauncher.launch(PERMISOS);
            }
        });

        askPermissions(MultiplesPermisosLauncher); //Metodo que al crearse la app se activa
    }

    /*Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(VistaFoto.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        // Video capture use case
        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(30)
                .build();

        // Image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(getExecutor(), this);

        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, videoCapture);
    } */

        //llama a la solicitud de permisos
    private void askPermissions(ActivityResultLauncher<String[]> MultiplesPermisosLauncher) {
        if (!hasPermissions(PERMISOS)) {
            Log.d("PERMISOS", "Iniciando un launcher de multiples permisos para todos los requeridos");
            MultiplesPermisosLauncher.launch(PERMISOS);
        } else {
            Log.d("PERMISOS", "Todos los permisos fueron concedidos");
        }
    }

        //Verifica si se tienen cumplidos los permisos
    private boolean hasPermissions(String[] permissions){
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISOS", "Permisos sin concedir: " + permission);
                    return false;
                }
                Log.d("PERMISOS", "Permisos ya concedidos: " + permission);
            }
                return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /*public ActivityResultLauncher<String[]> MultiplesPermisosLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                Log.d("PERMISOS", "Launcher result: " + isGranted.toString());
                if (isGranted.containsValue(false)) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                    MultiplesPermisosLauncher.launch(PERMISOS);
                } else {
                    Toast myToaste = Toast.makeText(getApplicationContext() , "Permita el acceso para poder analizar la imagen",Toast.LENGTH_LONG); //Crea el mensaje que aparecera
                    myToaste.show(); //Muestra el texto
                }
            });*/
}