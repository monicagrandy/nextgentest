package com.example.monica.nextgentest.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.monica.nextgentest.Data;
import com.example.monica.nextgentest.models.Github;
import com.example.monica.nextgentest.services.GithubService;
import com.example.monica.nextgentest.services.ServiceFactory;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by monica on 10/25/16.
 */

public class NetworkJob extends Job {

    public NetworkJob(int priority, String requestType) {
        //use singleWith so that if the same job has already been added and is not yet running,
        //it will only run once.
        super(new Params(priority).requireNetwork().groupBy("network").singleInstanceBy(requestType));
    }

    @Override
    public void onAdded() {
        Log.d("job added", "job added");
    }

    @Override
    public void onRun() throws Throwable {
        //connect listener to where the call is being made
        GithubService service = ServiceFactory.createRetrofitService(GithubService.class, GithubService.SERVICE_ENDPOINT);
        for(String login : Data.githubList) {
            service.getUser(login)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Github>() {
                        @Override
                        public final void onCompleted() {
                            // do nothing
                        }

                        @Override
                        public final void onError(Throwable e) {
                            Log.e("GithubDemo", e.getMessage());
                        }

                        @Override
                        public final void onNext(Github response) {
                            ServiceFactory.apiResponse(response);
                        }
                    });
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
