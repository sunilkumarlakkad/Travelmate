package com.example.sunillakkad.travelmate.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sunillakkad.travelmate.R;
import com.example.sunillakkad.travelmate.activities.LoginActivity;
import com.example.sunillakkad.travelmate.events.CheckInternetRequest;
import com.example.sunillakkad.travelmate.events.CheckInternetResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getSimpleName();
    private LoginActivityCallbacks mCallback;
    private Unbinder unbinder;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindString(R.string.no_internet_connection)
    String mConnectionError;
    @BindString(R.string.oops_description)
    String mOtherError;
    @BindString(R.string.authentication_error)
    String mAuthError;
    @BindString(R.string.required_fields)
    String mRequiredField;

    @BindView(R.id.txtLoginUsername)
    TextInputEditText mUsernameText;
    @BindView(R.id.txtLoginPassword)
    TextInputEditText mPasswordText;
    @BindView(R.id.btnLogin)
    Button mButtonLogin;
    @BindView(R.id.progressbar_login)
    ProgressBar mProgressBarLogin;
    @BindView(R.id.fragment_login)
    RelativeLayout mRelativeLayoutLogin;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (LoginActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mAuth = FirebaseAuth.getInstance();
        setAuthListener();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnLogin)
    public void callSignIn() {
        hideLoginButton();
        hideKeyBoard(mButtonLogin);
        if (!validateForm()) return;

        EventBus.getDefault().post(new CheckInternetRequest());
    }

    @OnClick(R.id.btnRegister)
    public void openRegisterUserFragment() {
        mCallback.onRegisterClicked();
    }

    @OnEditorAction(R.id.txtLoginPassword)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callSignIn();
            handled = true;
        }
        return handled;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CheckInternetResult checkInternetResult) {
        if (checkInternetResult.hasInternet()) signIn();
        else showSnackBar(mConnectionError);
    }

    private void setAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                /*if (user != null)
                    mCallback.onLoginDone();
                else
                    showSnackBar(mAuthError);*/
            }
        };
    }


    private void signIn() {
/*        if (!NetworkConnectionUtils.checkInternet()){
            showSnackBar(mConnectionError);
            return;
        }*/
        mAuth.signInWithEmailAndPassword(mUsernameText.getText().toString(), mPasswordText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            showSnackBar(mAuthError);
                        }
                        else{
                            mCallback.onLoginDone();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = false;
        String email = mUsernameText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showSnackBar(mRequiredField);
        }
        else
            valid = true;

        return valid;
    }

    private void showSnackBar(String message) {
        showLoginButton();
        Snackbar.make(mRelativeLayoutLogin, message, Snackbar.LENGTH_LONG)
                .show();
    }

    private void hideKeyBoard(Button button) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
    }

    private void hideLoginButton() {
        mButtonLogin.setVisibility(View.GONE);
        mProgressBarLogin.setVisibility(View.VISIBLE);
    }

    private void showLoginButton() {
        mProgressBarLogin.setVisibility(View.GONE);
        mButtonLogin.setVisibility(View.VISIBLE);
    }

    public interface LoginActivityCallbacks {
        void onRegisterClicked();

        void onLoginDone();
    }
}
