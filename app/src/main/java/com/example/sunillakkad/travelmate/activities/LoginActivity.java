package com.example.sunillakkad.travelmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.fragments.LoginFragment;
import com.example.sunillakkad.travelmate.fragments.RegisterUserFragment;

public class LoginActivity extends BaseActivity implements LoginFragment.LoginActivityCallbacks {

    private final static String TAG = LoginActivity.class.getSimpleName();
    private Fragment mContent;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null)
            mContent = mFragmentManager.getFragment(savedInstanceState, "mContent");
        else {
            mContent = LoginFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.login_container, mContent)
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
    public void onRegisterClicked() {
        mContent = RegisterUserFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.login_container, mContent)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginDone() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
