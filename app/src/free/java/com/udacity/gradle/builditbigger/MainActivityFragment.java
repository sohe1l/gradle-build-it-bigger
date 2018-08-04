package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.jokeui.DisplayJokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {


    private InterstitialAd mInterstitialAd;
    private String joke_to_show = "";
    private boolean showAds = true;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);


        Button b = root.findViewById(R.id.btn_tell_joke);
        b.setOnClickListener(this);

        showAds = getResources().getBoolean(R.bool.show_ads);


        if(showAds){

            MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544~3347511713");
            AdView mAdView = root.findViewById(R.id.adView);
            if(mAdView != null){ // if the ad view was found
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
                mAdView.loadAd(adRequest);
            }
        }



        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mInterstitialAd == null || !mInterstitialAd.isLoaded()){
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    show_joke_intent();
                }
            });
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tell_joke:
                new EndpointsAsyncTask().execute();
                break;
        }
    }



    private void show_joke_intent(){
        if(!joke_to_show.equals("")){
            Intent intent = new Intent(getActivity(), DisplayJokeActivity.class);
            intent.putExtra(DisplayJokeActivity.JOKE_KEY, joke_to_show);
            startActivity(intent);
        }
    }


    // leak possibility noted, will follow the instructions in the next projects
    class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
        private MyApi myApiService = null;
        final String API_URL = "http://andjokeapp.appspot.com/_ah/api/";

        @Override
        protected String doInBackground(Context... params) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl(API_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });

                myApiService = builder.build();
            }

            try {
                return myApiService.tellJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && !result.equals("")){
                joke_to_show = result;
                if(showAds && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    show_joke_intent();
                }
            }
        }
    }

}

