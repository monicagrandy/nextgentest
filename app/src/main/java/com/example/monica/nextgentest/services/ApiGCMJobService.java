package com.example.monica.nextgentest.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.example.monica.nextgentest.NextGenApplication;

/**
 * Created by monica on 10/25/16.
 */

public class ApiGCMJobService extends GcmJobSchedulerService {
    @NonNull
    @Override
    protected JobManager getJobManager() {
        return NextGenApplication.getInstance().getApiJobManager();
    }
}
