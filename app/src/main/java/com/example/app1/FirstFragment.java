package com.example.app1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.example.app1.databinding.FragmentFirstBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class FirstFragment extends Fragment {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FragmentFirstBinding binding;
    //static final int PEDIR_CAPTURA_IMAGEN = 1;
    ImageView VistaImagen;
    //String RutaImagen;
    PreviewView previewView;
    private ImageCapture imageCapture;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.TomarFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
        view.findViewById(R.id.AnalizarFoto).setOnClickListener(new View.OnClickListener() { //Hacer accion al hacer click en el boton del id
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity(), "Analizando Imagen",Toast.LENGTH_SHORT); //Crea el mensaje que aparecera
                myToast.show(); //Muestra el texto
                IniciarReconocimiento();
            }
        });
        view.findViewById(R.id.Mapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        VistaImagen = view.findViewById(R.id.VistaFoto);
        previewView = view.findViewById(R.id.preview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ActivityResultLauncher<Intent> CamaraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Bundle extras = result.getData().getExtras();
                Bitmap imgBitmap = (Bitmap) extras.get("data");
                //Bitmap imgBitmap = BitmapFactory.decodeFile(RutaImagen);
                VistaImagen.setImageBitmap(imgBitmap);
            }
        }
    });

    /*ActivityResultLauncher<Intent> TomarFoto = registerForActivityResult(ActivityResultContracts.TakePicture){
        VistaImagen.setImageURI(null);
        VistaImagen.setImageURI(Path);
    }*/

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(getActivity());
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        /*// Image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(getExecutor(), this);*/

        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    private void capturePhoto() {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getActivity().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(getActivity(), "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getActivity(), "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                //final Bitmap bitmap previewView.getBitmap();
        );
    }

    public void IniciarReconocimiento(){
        Intent intent = new Intent(getActivity(),Clasificacion.class);
        startActivity(intent);
    }
    /*private void GuardarFoto(){
        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //CamaraLauncher.launch(intento);// Actividad a ejecutar (toma foto)
        if (intento.resolveActivity(getActivity().getPackageManager()) !=null){
            File ImagenArchivo = null;
            try {
                ImagenArchivo = CrearImagen();
            } catch (IOException ex){
                Log.e("Error",ex.toString());
            }
            if (ImagenArchivo != null){
                Uri FotoUri = FileProvider.getUriForFile(getActivity(),"com.example.app1.fileprovider", ImagenArchivo); //Nombre en app/java
                intento.putExtra(MediaStore.EXTRA_OUTPUT,FotoUri);
                CamaraLauncher.launch(intento);// Actividad a ejecutar (toma foto)
            }
        }
    }*/

    //ActivityResultLauncher<Intent> GuardarFoto =
    /*private File CrearImagen() throws IOException {
        String NombreImagen = "Edificio_";
        //getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File Directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(NombreImagen,".jpg",Directorio);
        RutaImagen = img.getAbsolutePath();
        return img;
    }*/
}