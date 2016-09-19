package com.example.sunillakkad.travelmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.fragments.AttractionFragment;
import com.example.sunillakkad.travelmate.fragments.HomeFragment;
import com.example.sunillakkad.travelmate.fragments.NotesListFragment;
import com.example.sunillakkad.travelmate.fragments.TaxiRateFragment;
import com.example.sunillakkad.travelmate.fragments.YoutubeVideoListFragment;
import com.example.sunillakkad.travelmate.model.LocationInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFindTaxiFareClickedCallbacks,
        YoutubeVideoListFragment.OnVideoItemSelectCallbacks {

    private final static String TAG = HomeActivity.class.getSimpleName();
    private Fragment mContent;
    private FragmentManager mFragmentManager;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_drawer)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        setupToolbar();
        setupNavigationDrawer();

        if (savedInstanceState != null)
            mContent = mFragmentManager.getFragment(savedInstanceState, "mContent");
        else {
            mContent = HomeFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.container, mContent)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mContent != null && mContent.isAdded())
            mFragmentManager.putFragment(bundle, "mContent", mContent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.taxi:
                mContent = HomeFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mContent)
                        .commit();
                break;
            case R.id.attraction:
                mContent = AttractionFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mContent)
                        .commit();
                break;

            case R.id.video:
                mContent = YoutubeVideoListFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mContent)
                        .commit();
                break;
            case R.id.notes:
                mContent = NotesListFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mContent)
                        .commit();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupNavigationDrawer() {
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onFindTaxiFareClicked(LocationInfo locationInfo) {
        mContent = TaxiRateFragment.newInstance(locationInfo);
        mFragmentManager.beginTransaction()
                .replace(R.id.container, mContent)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemSelected(String videoID) {
        Intent intent = new Intent(getApplication(), YoutubePlayerActivity.class);
        intent.putExtra("VIDEO_ID", videoID);
        startActivity(intent);
    }
}
