package com.example.tiendavirtual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProductActivity extends AppCompatActivity {

    Button btn_add;
    EditText nombreProducto, descripcionProducto, precioProducto;
    private FirebaseFirestore mfirestore;
    //FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        this.setTitle("Crear Producto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id_product");
        //instanciar firestore
        mfirestore = FirebaseFirestore.getInstance();

        //LEER LOS DATOS
        nombreProducto = findViewById(R.id.nombre_producto);
        descripcionProducto = findViewById(R.id.descripcion_producto);
        precioProducto = findViewById(R.id.precio_producto);
        btn_add = findViewById(R.id.btn_add);

        if (id == null || id == "") {
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameProduct = nombreProducto.getText().toString().trim();
                    String descriptionProduct = descripcionProducto.getText().toString().trim();
                    String priceProduct = precioProducto.getText().toString().trim();
                    if (nameProduct.isEmpty() && descriptionProduct.isEmpty() && priceProduct.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingresar los datos!", Toast.LENGTH_LONG).show();

                    } else {
                        postProducts(nameProduct, descriptionProduct, priceProduct);
                    }
                }
            });
        } else {
            btn_add.setText("Actualizar");
            getProduct(id);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameProduct = nombreProducto.getText().toString().trim();
                    String descriptionProduct = descripcionProducto.getText().toString().trim();
                    String priceProduct = precioProducto.getText().toString().trim();

                    if (nameProduct.isEmpty() && descriptionProduct.isEmpty() && priceProduct.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingresar los datos!", Toast.LENGTH_LONG).show();

                    } else {
                        updateProducts(nameProduct, descriptionProduct, priceProduct, id);
                    }
                }
            });
        }


    }

    private void updateProducts(String nameProduct, String descriptionProduct, String priceProduct, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nameProduct);
        map.put("description", descriptionProduct);
        map.put("price", priceProduct);

        mfirestore.collection("productos").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado exitosamente!", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar!!", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void postProducts(String nameProduct, String descriptionProduct, String priceProduct) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nameProduct);
        map.put("description", descriptionProduct);
        map.put("price", priceProduct);
        mfirestore.collection("productos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Creado exitosamente!", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getProduct(String id){
        mfirestore.collection("productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameProduct =documentSnapshot.getString("name");
                String descriptionProduct =documentSnapshot.getString("description");
                String priceProduct =documentSnapshot.getString("price");

                nombreProducto.setText(nameProduct);
                descripcionProducto.setText(descriptionProduct);
                precioProducto.setText(priceProduct);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener datos!!!", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}