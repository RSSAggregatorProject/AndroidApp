package com.rssaggregator.android.login.view;

import android.content.Context;
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
import com.orhanobut.logger.Logger;
import com.rssaggregator.android.R;
import com.rssaggregator.android.RssAggregatorApplication;
import com.rssaggregator.android.dependency.AppComponent;
import com.rssaggregator.android.login.presenter.LoginPresenterImpl;
import com.rssaggregator.android.network.model.AccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements LoginView {

  @BindView(R.id.rootView) RelativeLayout rootViewRl;
  @BindView(R.id.emailLoginEt) AppCompatEditText emailEt;
  @BindView(R.id.passwordLoginEt) AppCompatEditText passwordEt;
  @BindView(R.id.buttonLogin) ActionProcessButton loginBt;

  private AppComponent appComponent;
  private LoginPresenterImpl presenter;

  private static String loginUser;
  private static String passwordUser;

  public static LoginFragment newInstance(String login, String password) {
    loginUser = login;
    passwordUser = password;
    return new LoginFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setFields();
    this.loginBt.setMode(ActionProcessButton.Mode.ENDLESS);
    this.loginBt.setProgress(0);
    injectDependencies();
  }

  @Override
  public void onDestroyView() {
    this.presenter.onDestroy();
    super.onDestroyView();
  }

  private void injectDependencies() {
    this.appComponent = RssAggregatorApplication.get(getActivity()).getAppComponent();
    this.presenter = appComponent.loginPresenterImpl();
    this.presenter.setLoginView(this);
  }

  /**
   * Set the fields to the user preferences if there are.
   */
  private void setFields() {
    if (loginUser != null && passwordUser != null) {
      emailEt.setText(loginUser);
      passwordEt.setText(passwordUser);
    }
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

  @OnClick(R.id.buttonLogin)
  public void login() {
    if (!verifyFields()) {
      return;
    }
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
        Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(passwordEt.getWindowToken(), 0);
    presenter.logIn(emailEt.getText().toString(), passwordEt.getText().toString());
  }

  //
  //
  // Methods called by the presenter.
  //
  //
  @Override
  public void showLoading() {
    this.loginBt.setProgress(30);
  }

  @Override
  public void showErrorSnackbar(String errorMessage) {
    this.loginBt.setProgress(0);
    Snackbar.make(this.rootViewRl, errorMessage, Snackbar.LENGTH_SHORT).show();
  }

  @Override
  public void loginSuccessful(AccessToken accessToken) {
    this.loginBt.setProgress(100);
    Logger.e("Logged: " + accessToken.getToken() + " | " + accessToken.getEmail());
  }
}
