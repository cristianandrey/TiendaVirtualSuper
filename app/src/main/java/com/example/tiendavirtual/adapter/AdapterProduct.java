package com.example.tiendavirtual.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.tiendavirtual.CreateProductoFragment;
import com.example.tiendavirtual.R;
import com.example.tiendavirtual.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdapterProduct extends FirestoreRecyclerAdapter<Producto, AdapterProduct.ViewHolder> {
    private FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterProduct(@NonNull FirestoreRecyclerOptions<Producto> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterProduct.ViewHolder holder, int position, @NonNull Producto model) {
        holder.name.setText(model.getName());
        //viewHolder.description.setText(Product.getDescription());
        holder.price.setText(model.getPrice());


        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id = documentSnapshot.getId();
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(id);
            }


        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CreateProductActivity.class);
                i.putExtra("id_product", id);
                //activity.startActivity(i);

                //send data fragment

                CreateProductoFragment createProductoFragment=new CreateProductoFragment();
                Bundle bundle =new Bundle();
                bundle.putString("id_product",id);
                createProductoFragment.setArguments(bundle);
                createProductoFragment.show(fm,"Open Fragment");
            }
        });
    }

    private void deleteProduct(String id) {
        mFirebase.collection("productos").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado con exito!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public AdapterProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_single, parent, false);
        return new AdapterProduct.ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView btn_delete, btn_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            //description = itemView.findViewById(R.id.descripcion_producto);
            price = itemView.findViewById(R.id.precio);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);
            btn_edit = itemView.findViewById(R.id.btn_editar);

        }
    }
}
