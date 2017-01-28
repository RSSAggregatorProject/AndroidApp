package com.rssaggregator.android.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.login.LoginActivity;
import com.rssaggregator.android.login.presenter.SignUpPresenterImpl;
import com.rssaggregator.android.utils.Globals;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment for the Sign Up View.
 */
public class SignUpFragment extends Fragment implements SignUpView {

  /**
   * View attributes.
   */
  @BindView(R.id.rootView) RelativeLayout rootViewRl;
  @BindView(R.id.emailSignUpEt) AppCompatEditText emailEt;
  @BindView(R.id.passwordSignUpEt) AppCompatEditText passwordEt;
  @BindView(R.id.buttonSignUp) ActionProcessButton signUpBt;

  private SignUpPresenterImpl presenter;

  private LoginActivity activity;

  /**
   * Create a new instance of SignUpFragment.
   *
   * @return Fragment SignUpFragment.
   */
  public static SignUpFragment newInstance() {
    return new SignUpFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signup, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.activity = (LoginActivity) getActivity();
    this.signUpBt.setMode(ActionProcessButton.Mode.ENDLESS);
    this.signUpBt.setProgress(0);
    injectDependencies();
  }

  /**
   * Injects dependencies
   */
  private void injectDependencies() {
    AppComponent appComponent = RssAggregatorApplication.get(getActivity()).getAppComponent();
    this.presenter = appComponent.signUpPresenterImpl();
    this.presenter.setSignUpView(this);
  }

  /**
   * Handles action, user signs up to the application.
   */
  @OnClick(R.id.buttonSignUp)
  public void signUp() {
    if (!verifyFields()) {
      return;
    }
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
        Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(passwordEt.getWindowToken(), 0);
    presenter.signUp(emailEt.getText().toString(), passwordEt.getText().toString());
  }

  /**
   * Check if all the fields are filled.
   *
   * @return boolean - true if all the fields are filled.
   */
  private boolean verifyFields() {
    String emailStr = emailEt.getText().toString();
    String passwordStr = passwordEt.getText().toString();
    if (emailStr.isEmpty()) {
      Snackbar.make(rootViewRl, R.string.login_empty_email,
          Snackbar.LENGTH_SHORT).show();
      return false;
    }
    if (passwordStr.isEmpty()) {
      Snackbar.make(rootViewRl, R.string.login_empty_password,
          Snackbar.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  //
  //
  // Methods called from the Presenter.
  //
  //
  @Override
  public void showLoading() {
    this.signUpBt.setProgress(30);
  }

  @Override
  public void showErrorSnackbar(String errorMessage) {
    this.signUpBt.setProgress(0);
    Snackbar.make(this.rootViewRl, errorMessage, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void signUpSuccessful() {
    this.signUpBt.setProgress(100);

    Intent intent = new Intent(activity, LoginActivity.class);
    intent.putExtra(Globals.EXTRA_KEY_SIGNUP_USERNAME, emailEt.getText().toString());
    intent.putExtra(Globals.EXTRA_KEY_SIGNUP_PASSWORD, passwordEt.getText().toString());

    getActivity().finish();
    startActivity(intent);
  }
}
