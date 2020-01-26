package com.example.houserent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Houselist extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private HouseAdapter mAdapter;

    private DatabaseReference mDatabaseReference;
    private List<UploadDetails> mUploadDetails;
    private ProgressBar mProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houselist);
        mRecyclerView=findViewById(R.id.recycler_view);
        mProgressCircle=findViewById(R.id.progress_cirle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploadDetails = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("houses");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapShot : dataSnapshot.getChildren()){
                    UploadDetails uploadDetails = postsnapShot.getValue(UploadDetails.class);
                    mUploadDetails.add(uploadDetails);
                }

                mAdapter = new HouseAdapter(Houselist.this,mUploadDetails);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Houselist.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}
