package com.example.monica.nextgentest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.example.monica.nextgentest.NextGenApplication;
import com.example.monica.nextgentest.jobs.NetworkJob;
import com.example.monica.nextgentest.models.Github;

import retrofit.RestAdapter;

public class ServiceFactory extends Service {
    public static JobManager jobManager;
    private static ServiceInterface serviceInterface;
    private static String LOG_TAG = "BoundService";
    private IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        Log.d(LOG_TAG, "log tag" + binder);
        return binder;
    }

    public class LocalBinder extends Binder {
        public ServiceFactory getService() {
            return ServiceFactory.this;
        }
    }

    public void setCallbacks(ServiceInterface callbacks) {
        serviceInterface = callbacks;
    }

    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     * @param clazz Java interface of the retrofit service
     * @param endPoint REST endpoint url
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }

    public static void queueNetworkJob(int priority, String requestType){
        NextGenApplication nextGenApplication = NextGenApplication.getInstance();
        jobManager = nextGenApplication.getApiJobManager();
        jobManager.addJobInBackground(new NetworkJob(priority, requestType));
    }

    public static void queueDownloadJob(int priority, String URI){
        jobManager = NextGenApplication.getInstance().getDownloadJobManager();
        jobManager.addJobInBackground(new NetworkJob(priority, URI));
    }

    public static void apiResponse(Github response){
        serviceInterface.responseRecieved(response);
    }

    public interface ServiceInterface {
        void responseRecieved(Github response);
    }
}
