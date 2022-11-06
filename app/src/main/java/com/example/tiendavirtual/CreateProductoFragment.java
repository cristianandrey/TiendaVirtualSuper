package com.example.tiendavirtual;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CreateProductoFragment extends DialogFragment {
    String id_product;
    Button btn_add;
    EditText nombreProducto, descripcionProducto, precioProducto, precioProducto2;
    private FirebaseFirestore mfirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_product = getArguments().getString("id_product");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_producto, container, false);
        //instanciar firestore
        mfirestore = FirebaseFirestore.getInstance();

        nombreProducto = v.findViewById(R.id.nombre_producto);
        descripcionProducto = v.findViewById(R.id.descripcion_producto);
        precioProducto = v.findViewById(R.id.precio_producto);

        btn_add = v.findViewById(R.id.btn_add);


        if (id_product == null || id_product == "") {
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameProduct = nombreProducto.getText().toString().trim();
                    String descriptionProduct = descripcionProducto.getText().toString().trim();
                    String priceProduct = precioProducto.getText().toString().trim();

                    if (nameProduct.isEmpty() && descriptionProduct.isEmpty() && priceProduct.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar los datos!", Toast.LENGTH_LONG).show();

                    } else {
                        postProducts(nameProduct, descriptionProduct, priceProduct);
                    }
                }
            });
        } else {
            getProduct();
            btn_add.setText("Actualizar");

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameProduct = nombreProducto.getText().toString().trim();
                    String descriptionProduct = descripcionProducto.getText().toString().trim();
                    String priceProduct = precioProducto.getText().toString().trim();

                    if (nameProduct.isEmpty() && descriptionProduct.isEmpty() && priceProduct.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar los datos!", Toast.LENGTH_LONG).show();

                    } else {
                        updateProducts(nameProduct, descriptionProduct, priceProduct);
                    }
                }
            });
        }

        return v;
    }

    private void updateProducts(String nameProduct, String descriptionProduct, String priceProduct) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nameProduct);
        map.put("description", descriptionProduct);
        map.put("price", priceProduct);

        mfirestore.collection("productos").document(id_product).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Actualizado exitosamente!", Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al actualizar!!", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getProduct() {
        mfirestore.collection("productos").document(id_product).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameProduct = documentSnapshot.getString("name");
                String descriptionProduct = documentSnapshot.getString("description");
                String priceProduct = documentSnapshot.getString("price");

                nombreProducto.setText(nameProduct);
                descripcionProducto.setText(descriptionProduct);
                precioProducto.setText(priceProduct);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener datos!!!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Creado exitosamente!", Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al ingresar!", Toast.LENGTH_LONG).show();
            }
        });
    }
}