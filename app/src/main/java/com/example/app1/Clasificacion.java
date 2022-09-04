package com.example.app1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.app1.ml.Clasificador;
import com.example.app1.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Clasificacion extends AppCompatActivity {

    Button Analizar, gallery;
    ImageView imageView;
    TextView result;
    TextView SeguridadClas;
    //int imageSize = 32;
    int imageSize = 224;
    Bitmap image = null;
    //ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        //camera = findViewById(R.id.button);
        //Analizar = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        SeguridadClas = findViewById(R.id.SeguridadClas);
        imageView = findViewById(R.id.imageView);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionFoto.launch("image/*");
            }
        });
    }

    ActivityResultLauncher<String> SeleccionFoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(),result); //Obtiene el bitmap de la imagen de la galeria
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageURI(result); //Pone la imagen seleccionada para ser vista
            //image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false); //Escala la imagen a la tamano necesario para el modelo
            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,true);
            //clasificarimagen(image);
            ClasificarImagenes(image);
        }
    });

    public void ClasificarImagenes(Bitmap image){
        try {
            Clasificador modelo = Clasificador.newInstance(getApplicationContext()); //Carga el modelo a usar

            TensorImage imagen = TensorImage.fromBitmap(image);
            Clasificador.Outputs salida = modelo.process(imagen);
            // Definimos una ArrayList
            List<Category> probabilidad = salida.getProbabilityAsCategoryList();

            // Encontrar el indice de la clase con la mejor prediccion
            int PosicionMax = 0;
            float ProbabilidadMax = 0;
            for (int i=0; i<probabilidad.size();i++){
                if (probabilidad.get(i).getScore() > ProbabilidadMax){
                    ProbabilidadMax = probabilidad.get(i).getScore();
                    PosicionMax = i;
                }
            }
            result.setText(probabilidad.get(PosicionMax).getLabel());
            SeguridadClas.setText(new StringBuilder().append(String.valueOf(probabilidad.get(PosicionMax).getScore() * 100)).append("%").toString());
            // Libera los recursos del modelo si no se necesitan mas
            modelo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        /*public void clasificarimagen (Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);

            // 4 numero de bytes que toma la imagen (float) * # pixeles * canales
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Ejecuta la inferencia del modelo y obtiene el resultado
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // Encontrar el indice de la clase con la mejor prediccion
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Centic", "E3T", "Biblioteca"};
            result.setText(classes[maxPos]);

            // Libera los recursos del modelo si no se necesitan mas
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }*/
}