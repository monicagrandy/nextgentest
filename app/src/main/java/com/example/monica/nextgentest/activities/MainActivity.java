package com.example.monica.nextgentest.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.birbit.android.jobqueue.JobManager;
import com.example.monica.nextgentest.NextGenApplication;
import com.example.monica.nextgentest.R;
import com.example.monica.nextgentest.adapters.CardAdapter;
import com.example.monica.nextgentest.jobs.NetworkJob;
import com.example.monica.nextgentest.models.Github;
import com.example.monica.nextgentest.services.GithubService;
import com.example.monica.nextgentest.services.ServiceFactory;
import com.example.monica.nextgentest.Data;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements ServiceFactory.ServiceInterface{
    final CardAdapter mCardAdapter = new CardAdapter();
    ServiceFactory mServiceFactory;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Set up Android CardView/RecycleView
         */
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCardAdapter);

        /**
         * START: button set up
         */
        Button bClear = (Button) findViewById(R.id.button_clear);
        Button bFetch = (Button) findViewById(R.id.button_fetch);
        bClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCardAdapter.clear();
            }
        });

        bFetch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mServiceBound)
                    ServiceFactory.queueNetworkJob(1, "allData");
            }
        });
        /**
         * END: button set up
         */
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceFactory.LocalBinder localBinder = (ServiceFactory.LocalBinder) service;
            mServiceFactory = localBinder.getService();
            mServiceBound = true;
            mServiceFactory.setCallbacks(MainActivity.this);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ServiceFactory.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d("serviceConnection", "serviceConnection" + mServiceConnection);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            mServiceFactory.setCallbacks(null);
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    public void responseRecieved(Github response){
        mCardAdapter.addData(response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
