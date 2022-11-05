package com.example.tiendavirtual.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendavirtual.R;
import com.example.tiendavirtual.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdapterProduct extends FirestoreRecyclerAdapter<Producto, AdapterProduct.ViewHolder> {
   private FirebaseFirestore mFirebase= FirebaseFirestore.getInstance();
   Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterProduct(@NonNull FirestoreRecyclerOptions<Producto> options , Activity activity) {
        super(options);
        this.activity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterProduct.ViewHolder holder, int position, @NonNull Producto model) {
        holder.name.setText(model.getName());
        //viewHolder.description.setText(Product.getDescription());
        holder.price.setText(model.getPrice());

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            deleteProduct(id);
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
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            //description = itemView.findViewById(R.id.descripcion_producto);
            price = itemView.findViewById(R.id.precio);
            btn_delete = itemView.findViewById(R.id.btn_eliminar);

        }
    }
}
