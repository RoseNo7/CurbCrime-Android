package com.roseno.curbcrime.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 뒤로 가기 버튼 추가
        Toolbar toolbar = findViewById(R.id.toolbar_activity_settings_navigation);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        
        // 애니메이션
        overridePendingTransition(R.anim.slide_in_right, R.anim.none);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_activity_settings_preference, new SettingsFragment())
                .commit();
    }

    /**
     * FragmentPreference 클릭 시, 설정한 Fragment로 이동
     * @param caller The fragment requesting navigation
     * @param pref   The preference requesting the fragment
     * @return
     */
    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_activity_settings_preference, fragment, "this")
                .addToBackStack("this")
                .commit();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();

        if (isFinishing()) {
            overridePendingTransition(R.anim.none, R.anim.slide_out_left);
        }
    }
}
