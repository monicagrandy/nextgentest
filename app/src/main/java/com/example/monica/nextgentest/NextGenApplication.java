package com.example.monica.nextgentest;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.example.monica.nextgentest.services.ApiGCMJobService;
import com.example.monica.nextgentest.services.ApiJobService;
import com.example.monica.nextgentest.services.DownloadGCMJobService;
import com.example.monica.nextgentest.services.DownloadJobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by monica on 10/25/16.
 */

public class NextGenApplication extends Application {
    private static NextGenApplication instance;
    private JobManager downloadJobManager;
    private JobManager apiJobManager;

    public NextGenApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //get the job managers
        getApiJobManager();
        getDownloadJobManager();
    }

    //configure the api job manager
    private void configureApiJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(this,
                    ApiJobService.class), true);
        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(this,
                        ApiGCMJobService.class), true);
            }
        }
        apiJobManager = new JobManager(builder.build());
    }

    //configure the download job manager
    private void configureDownloadJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(this,
                    DownloadJobService.class), true);
        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(this,
                        DownloadGCMJobService.class), true);
            }
        }
        downloadJobManager = new JobManager(builder.build());
    }

    //api job manager getter
    public synchronized JobManager getApiJobManager() {
        if (apiJobManager == null) {
            configureApiJobManager();
        }
        return apiJobManager;
    }
    //download job manager getter
    public synchronized JobManager getDownloadJobManager() {
        if (downloadJobManager == null) {
            configureDownloadJobManager();
        }
        return downloadJobManager;
    }

    //instance getter
    public static NextGenApplication getInstance() {
        return instance;
    }

}
