package com.example.tiendavirtual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.tiendavirtual.adapter.AdapterProduct;
import com.example.tiendavirtual.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btn_add_fragment;
    RecyclerView mRecycler;
    AdapterProduct mAdapter;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirestore = FirebaseFirestore.getInstance();

        mRecycler = findViewById(R.id.recyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("productos");



        FirestoreRecyclerOptions<Producto> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Producto>().setQuery(query, Producto.class).build();
        mAdapter = new AdapterProduct(firestoreRecyclerOptions,this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        btn_add = findViewById(R.id.btn_agregar);
        btn_add_fragment = findViewById(R.id.btn_add_fragment);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateProductActivity.class));

            }
        });

        //boton agregar con fragmento
        btn_add_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateProductoFragment fn = new CreateProductoFragment();
                fn.show(getSupportFragmentManager(), "Navegar a fragmento");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}