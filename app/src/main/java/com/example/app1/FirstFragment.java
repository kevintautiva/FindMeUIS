package com.example.app1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.camera.core.ImageCapture;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.app1.databinding.FragmentFirstBinding;

import java.io.File;
import java.io.IOException;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    static final int PEDIR_CAPTURA_IMAGEN = 1;
    ImageView VistaImagen;
    String RutaImagen;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /*ImageCapture capturaimagen =
            new ImageCapture.Builder()
                    .setTargetRotation(view.getDisplay().getRotation())
                    .build();
cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageCapture, imageAnalysis, preview);
*/

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VistaImagen = view.findViewById(R.id.VistaFoto);
        /*binding.TomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
        view.findViewById(R.id.TomarFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    CamaraLauncher.launch(intento);// Actividad a ejecutar (toma foto)
            }
        });
        view.findViewById(R.id.AnalizarFoto).setOnClickListener(new View.OnClickListener() { //Hacer accion al hacer click en el boton del id
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity(), "Analizando Imagen",Toast.LENGTH_SHORT); //Crea el mensaje que aparecera
                myToast.show(); //Muestra el texto
            }
        });
        view.findViewById(R.id.Mapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
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