package com.example.tiendavirtual.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendavirtual.CreateProductActivity;
import com.example.tiendavirtual.R;
import com.example.tiendavirtual.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AdapterProduct extends FirestoreRecyclerAdapter<Producto, AdapterProduct.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    public AdapterProduct(@NonNull FirestoreRecyclerOptions<Producto> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Producto producto) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getBindingAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.name.setText(producto.getNombre());
        viewHolder.descripcion.setText(producto.getDescripcion());
        viewHolder.product_price.setText("$ "+producto.getProducto_price());
        String photoProd = producto.getPhoto();
        try {
            if (!photoProd.equals(""))
                Picasso.with(activity.getApplicationContext())
                        .load(photoProd)
                        .resize(150, 150)
                        .into(viewHolder.photo_Prod);
        } catch (Exception e) {
            Log.d("Exception", "e: " + e);
        }

        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          SEND DATA ACTIVITY
                Intent i = new Intent(activity, CreateProductActivity.class);
                i.putExtra("id_producto", id);
                activity.startActivity(i);


            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProducto(id);
            }
        });
    }

    private void deleteProducto(String id) {
        mFirestore.collection("productos").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, descripcion, product_price;
        ImageView btn_delete, btn_edit, photo_Prod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nombre_s);
            descripcion = itemView.findViewById(R.id.descripcion_s);
            product_price = itemView.findViewById(R.id.precio_producto_s);

            photo_Prod = itemView.findViewById(R.id.photo);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }
}
