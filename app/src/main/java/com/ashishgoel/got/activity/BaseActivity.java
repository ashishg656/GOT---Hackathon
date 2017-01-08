package com.ashishgoel.got.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishgoel.got.R;
import com.ashishgoel.got.extras.AppConstants;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;


/**
 * Created by Ashish on 04/06/16.
 */
public class BaseActivity extends AppCompatActivity {

    Toast toast;
    Snackbar snackbar;

    LinearLayout progressLayout, errorLayout;
    ProgressBar progressBarLoading;
    Button retryButton;

    LinearLayout emptyScreenLayout;
    ImageView emptyScreenImage;
    TextView emptyScreenTextBold, emptyScreenTextLight;

    public void makeToast(String message) {
        if (!isFinishing()) {
            if (toast != null) {
                toast.cancel();
            }

            if (message != null && message.length() > 0) {
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void makeToastLong(String message) {
        if (!isFinishing()) {
            if (toast != null) {
                toast.cancel();
            }

            if (message != null && message.length() > 0) {
                toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void showSnackbar(String message) {
        try {
            hideSnackbar();

            if (message != null && findViewById(R.id.coordinator_layout) != null) {
                snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSnackbar() {
        try {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
                snackbar = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return BaseActivity.this;
    }

    public AppCompatActivity getActivity() {
        return BaseActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProgressAndErrorLayoutVariables() {
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        retryButton = (Button) findViewById(R.id.button_error);
        progressBarLoading = (ProgressBar) findViewById(R.id.progress_bar_loading);
    }

    public void showProgressLayout() {
        try {
            progressLayout.setVisibility(View.VISIBLE);
            progressBarLoading.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showErrorLayout() {
        try {
            errorLayout.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void hideProgressLayout() {
        try {
            progressLayout.setVisibility(View.GONE);
            progressBarLoading.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void hideErrorLayout() {
        try {
            errorLayout.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setEmptyScreenVariables() {
        emptyScreenLayout = (LinearLayout) findViewById(R.id.emptyScreenLayout);
        emptyScreenImage = (ImageView) findViewById(R.id.emptyScreenImage);
        emptyScreenTextBold = (TextView) findViewById(R.id.emptyScreenTextBold);
        emptyScreenTextLight = (TextView) findViewById(R.id.emptyScreenTextLight);

        try {
            emptyScreenImage.setImageResource(R.drawable.empty_search_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showEmptyScreenLayout() {
        emptyScreenLayout.setVisibility(View.VISIBLE);
    }

    void hideEmptyScreenLayout() {
        emptyScreenLayout.setVisibility(View.GONE);
    }

    /*
    * activity opening
    *
    */

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openKingDetailActivity(KingDetailsObject object) {
        Intent intent = new Intent(this, KingProfileActivity.class);
        intent.putExtra(AppConstants.INTENT_DATA.INTENT_DATA_KING_OBJECT, object);
        startActivity(intent);
    }

    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}