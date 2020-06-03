package com.exemple.stage.Company;
/**
 * Created By GHOUADN AYOUB
 */


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.exemple.stage.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivitySetting extends PreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            preference.setSummary(
                    index >= 0 ? listPreference.getEntries()[index] : null
            );

        } else if (preference instanceof RingtonePreference) {
            if (TextUtils.isEmpty(stringValue)) {
                preference.setSummary(R.string.pref_ringtone_silent);

            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                if (ringtone == null) {
                    preference.setSummary(null);
                } else {
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }

        } else {
            preference.setSummary(stringValue);
        }
        return true;
    };
    private AppCompatDelegate mDelegate;
    private ActionBar actionBar;
    private ImageLoader imgloader = ImageLoader.getInstance();
    private View parent_view;

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "")
        );
    }

    public static int colorDarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int getThemeColorInt() {
        if (f() == R.color.darkblue)
            return R.color.darkblue;
        return 0;
    }

    public static int f() {
        return R.color.darkblue;
    }

    public static void setActionBarColor(Context ctx, ActionBar actionbar) {
        ColorDrawable colordrw = new ColorDrawable(getThemeColorInt());
        actionbar.setBackgroundDrawable(colordrw);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_notification);
        parent_view = findViewById(android.R.id.content);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ringtone)));

        Preference notifPref = findPreference(getString(R.string.pref_key_notif));
        Preference themePref = findPreference(getString(R.string.pref_key_theme));
        Preference ratePref = findPreference("key_rate");
        Preference morePref = findPreference("key_more");
        Preference aboutPref = findPreference("key_about");
        final Preference prefTerm = findPreference(getString(R.string.pref_title_term));

        PreferenceCategory prefCat = (PreferenceCategory) findPreference(getString(R.string.pref_category_notif));
        notifPref.setEnabled(true);


        notifPref.setOnPreferenceChangeListener((preference, o) -> {
            boolean flag = (boolean) o;
            return true;
        });

        ratePref.setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                getApplicationContext().startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("" + getApplicationContext().getPackageName())));
            }
            return true;
        });

        aboutPref.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getApplicationContext().getString(R.string.AB));
            builder.setMessage(getApplicationContext().getString(R.string.Apropos));
            builder.setPositiveButton("OK", null);
            builder.show();
            return true;
        });

        prefTerm.setOnPreferenceClickListener(preference -> {
            dialogTerm(ActivitySetting.this);
            return true;
        });

    }

    @Override
    protected void onResume() {
        //  initToolbar();
        super.onResume();
    }

    public void dialogTerm(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.pref_title_term));
        builder.setMessage(activity.getString(R.string.content_term));
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.activity_title_settings);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(colorDarker(getThemeColorInt()));
        setActionBarColor(this, actionBar);
    }

    //***************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

}
