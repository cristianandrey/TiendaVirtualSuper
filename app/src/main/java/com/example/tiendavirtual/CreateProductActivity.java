package com.example.tiendavirtual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CreateProductActivity extends AppCompatActivity {

    ImageView photo_Prod;
    Button btn_add, btn_back;
    Button btn_cu_photo, btn_r_photo;
    LinearLayout linearLayout_image_btn;
    EditText name_c, descripcion_c, precio_producto_c;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        this.setTitle("Productos");
       //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_producto");
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        linearLayout_image_btn = findViewById(R.id.images_btn);

        name_c = findViewById(R.id.nombre_c);
        descripcion_c = findViewById(R.id.descripcion_c);
        precio_producto_c = findViewById(R.id.precio_producto_c);

        photo_Prod = findViewById(R.id.pet_photo);
        btn_cu_photo = findViewById(R.id.btn_photo);
        btn_r_photo = findViewById(R.id.btn_remove_photo);
        btn_add = findViewById(R.id.btn_add);
        btn_back=findViewById(R.id.btn_regresar_c);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_cu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        btn_r_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mfirestore.collection("productos").document(idd).update(map);
                Toast.makeText(CreateProductActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });

        if (id == null || id == "") {
            linearLayout_image_btn.setVisibility(View.GONE);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameprod = name_c.getText().toString().trim();
                    String descriprod = descripcion_c.getText().toString().trim();
                    String precio_prod = precio_producto_c.getText().toString().trim();

                    if (nameprod.isEmpty() && descriprod.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        postProducto(nameprod, descriprod, precio_prod);
                    }
                }
            });
        } else {
            idd = id;
            btn_add.setText("Update");
            getProducto(id);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameprod = name_c.getText().toString().trim();
                    String descriprod = descripcion_c.getText().toString().trim();
                    String precio_prod = precio_producto_c.getText().toString().trim();

                    if (nameprod.isEmpty() && descriprod.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        updateProducto(nameprod, descriprod, precio_prod, id);
                    }
                }
            });
        }
    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() + "" + idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;

                if (uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("productos").document(idd).update(map);
                            Toast.makeText(CreateProductActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateProductActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProducto(String nameprod, String descriprod,  String precio_prod, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nameprod);
        map.put("descripcion", descriprod);
        map.put("producto_price", precio_prod);

        mfirestore.collection("productos").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postProducto(String nameprod, String descriprod, String precio_prod) {
        String idUser = mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("productos").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", idUser);
        map.put("id", id.getId());
        map.put("nombre", nameprod);
        map.put("descripcion", descriprod);
        map.put("producto_price", precio_prod);

        mfirestore.collection("productos").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProducto(String id) {
        mfirestore.collection("productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");

                String nameProd = documentSnapshot.getString("nombre");
                String descriProd = documentSnapshot.getString("descripcion");
                String precio_Prod = documentSnapshot.getString("producto_price");
                String photoProd = documentSnapshot.getString("photo");

                name_c.setText(nameProd);
                descripcion_c.setText(descriProd);
                precio_producto_c.setText(precio_Prod);
                try {
                    if (!photoProd.equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 200);
                        toast.show();
                        Picasso.with(CreateProductActivity.this)
                                .load(photoProd)
                                .resize(150, 150)
                                .into(photo_Prod);
                    }
                } catch (Exception e) {
                    Log.v("Error", "e: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}