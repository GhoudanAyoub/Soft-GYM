package com.exemple.stage.Youtube;
/**
 * Created By GHOUADN AYOUB
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.exemple.stage.Adapters.AboutUsAdapter;
import com.exemple.stage.Company.ContactUs;
import com.exemple.stage.Payment.OnlineCoaching;
import com.exemple.stage.Payment.OnlineCoaching2;
import com.exemple.stage.Profile.Authentification;
import com.exemple.stage.R;
import com.exemple.stage.Services.Config;
import com.exemple.stage.modele.AboutUsItems;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends YouTubeBaseActivity implements NavigationView.OnNavigationItemSelectedListener, YouTubePlayer.OnInitializedListener {
    /*
        RecyclerView recyclerView;
        Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();
        */
    private static final int RECOVERY_REQUEST = 1;
    Button button2;
    RecyclerView NewsRecyclerview;
    AboutUsAdapter newsAdapter;
    List<AboutUsItems> mData;
    boolean isDark = false;
    private YouTubePlayerView youTubeView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OnlineCoaching2.class);
                startActivity(intent);
            }
        });
        /*
        //youtube video
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        youtubeVideos.add(new YouTubeVideos( "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/channel/UCBlDukkjqyLtDb8yw09zIFw\" frameborder=\"0\" allowfullscreen></iframe>"));
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

        recyclerView.setAdapter(videoAdapter);
*/

        try {
            youTubeView = findViewById(R.id.videoWebView);
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        } catch (Exception e) {
        }
        //****************


        NewsRecyclerview = findViewById(R.id.news_rv);
        mData = new ArrayList<>();

        mData.add(new AboutUsItems("Diplômé", "Nous exigeons au minimum un BPJEPS AF ou une licence STAPS, mais la plupart des coachs sélectionnés disposent de formations supplémentaires (Nutrition , Pilates, Yoga, Boxe, TRX, CrossFit, Kettlebell, Trigger point, etc.).", R.drawable.ic_school_black_24dp));
        mData.add(new AboutUsItems("D’expérience", "Enfin, afin de vous proposer un service de haute qualité, nous sélectionnons uniquement des entraîneurs personnels avec au minimum 2 ans d’expérience dans le coaching sportif.", R.drawable.ic_poll_black_24dp));
        mData.add(new AboutUsItems("Votre avis compte énormément !", "Rien de tel que le retour client pour jauger de la qualité du service proposé.\n" +
                "Ainsi, nous réalisons régulièrement des enquêtes de satisfaction afin d’obtenir un maximum d’avis sur les coachs de notre réseau.\n" +
                "En cas de retour négatif nous réalisons une enquête plus approfondie sur le coach concerné.\n" +
                "S’il s’avère que ce dernier a manqué à plusieurs reprises de professionnalisme, il est alors exclu du réseau.", R.drawable.ic_sentiment_very_satisfied_black_24dp));
        mData.add(new AboutUsItems("Un coaching 100 % personnalisé !", "Se faire accompagner par un véritable coach sportif professionnel c’est bien… avec un suivi individuel c’est encore mieux !\n" +
                "\n" +
                "Bénéficier des services d’un entraîneur qui s’occupe uniquement de vous c’est la garantie de progresser plus vite en évitant tout risque de blessure.\n" +
                "Votre programme est 100 % personnalisé, en fonction de vos objectifs mais également de vos capacités et disponibilités.\n" +
                "Votre coach est toujours à vos côtés, pour vous motiver, et à votre écoute, pour ajuster votre entraînement si besoin.", R.drawable.ic_fitness_center_black_24dp));


        newsAdapter = new AboutUsAdapter(this, mData, isDark);
        NewsRecyclerview.setAdapter(newsAdapter);
        NewsRecyclerview.setLayoutManager(new LinearLayoutManager(this));


        //************* ADS
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            showInterstitial();
        } else {
            super.onBackPressed();
            showInterstitial();
        }
    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Faild", Toast.LENGTH_SHORT);
            }

            @Override
            public void onAdClosed() {
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT);
        }
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_online) {
            Intent intent = new Intent(getApplicationContext(), OnlineCoaching.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), Authentification.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo("5Wf-k35JXnA");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
