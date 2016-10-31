package com.example.monica.nextgentest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.net.URI;

/**
 * Created by monica on 10/25/16.
 */

public class DownloadJob extends Job {

    public DownloadJob(int priority, URI downloadLink) {
        //use singleWith so that if the same job has already been added and is not yet running,
        //it will only run once.
        // This job requires network connectivity,
        // and should be persisted in case the application exits before job is completed.
        super(new Params(priority).requireNetwork().groupBy("download").singleInstanceBy(String.valueOf(downloadLink)).persist());
    }

    @Override
    public void onAdded() {
        // Job has been saved to disk.
        // This is a good place to dispatch a UI event to indicate the job will eventually run.
        // In this example, it would be good to update the UI with the newly posted tweet.
    }

    @Override
    public void onRun() throws Throwable {
        //this is called automatically when it is time for the downloadjob to run
        //connect listener to start the download
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // Job has exceeded retry attempts or shouldReRunOnThrowable() has returned false.
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        // An error occurred in onRun.
        // Return value determines whether this job should retry running (true) or abort (false).
        return null;
    }
}
