package com.designfreed.toowatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.designfreed.toowatch.adapters.SerieAdapter;
import com.designfreed.toowatch.model.Serie;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeFlingAdapterView mFlingContainer;

    private Button mSignOutBtn;

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private SerieAdapter mAdapter;

    private List<Serie> mSeries;

    private ProgressDialog mProgress;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        mFlingContainer = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);

        mSeries = new ArrayList<>();

        loadDummyData();

        mAdapter = new SerieAdapter(this, mSeries);

        mFlingContainer.setAdapter(mAdapter);
        mFlingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object o) {
                mSeries.remove(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object o) {
                mSeries.remove(0);
                mAdapter.notifyDataSetChanged();

                Intent mainIntent = new Intent(MainActivity.this, RatingActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainIntent);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUsers.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                } else {

                    checkUserExist();

                }
            }
        };

        mSignOutBtn = (Button) findViewById(R.id.signout_btn);
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mProgress = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");

        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void loadDummyData() {
        Serie serie1 = new Serie();
        serie1.setName("The Walking Dead");
        serie1.setDescription("Loren impsum bla bla bla");
        serie1.setType("Action, Drama & Crime");
        serie1.setYear(2008);
        serie1.setRating(4.5F);
        mSeries.add(serie1);

        Serie serie2 = new Serie();
        serie2.setName("Game of Thrones");
        serie2.setDescription("Loren impsum bla bla bla");
        serie2.setType("Fantasy");
        serie2.setYear(2006);
        serie2.setRating(2.5F);
        mSeries.add(serie2);

        Serie serie3 = new Serie();
        serie3.setName("Black Orphan");
        serie3.setDescription("Loren impsum bla bla bla");
        serie3.setType("Action");
        serie3.setYear(2010);
        serie3.setRating(5.0F);
        mSeries.add(serie3);

        Serie serie4 = new Serie();
        serie4.setName("Lost");
        serie4.setDescription("Loren impsum bla bla bla");
        serie4.setType("Action & Adventure");
        serie4.setYear(2002);
        serie4.setRating(3.5F);
        mSeries.add(serie4);

        Serie serie5 = new Serie();
        serie5.setName("Boardwalk Empire");
        serie5.setDescription("Loren impsum bla bla bla");
        serie5.setType("Drama");
        serie5.setYear(2008);
        serie5.setRating(4.5F);
        mSeries.add(serie5);

        Collections.shuffle(mSeries);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void checkUserExist() {
        if (mAuth != null) {

            mProgress.dismiss();

            final String userId = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(userId)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
