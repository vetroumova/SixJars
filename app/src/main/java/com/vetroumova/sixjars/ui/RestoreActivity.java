package com.vetroumova.sixjars.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;

/**
 * A db restore screen that offers to restore from archive.
 */
public class RestoreActivity extends AppCompatActivity {
    private static final long PAUSE = 10;
    final String TAG = "VOlga";
    ProgressBar progressBar;
    Button restoreButton;
    TextView restoreText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        progressBar = (ProgressBar) findViewById(R.id.restore_progress);
        restoreButton = (Button) findViewById(R.id.restore_button);
        restoreText = (TextView) findViewById(R.id.restore_text);
    }

    public void onRestoreClick(View view) {
        //cannot work with realm in another thread except asynctransactions
        progressBar.setVisibility(View.VISIBLE);
        restoreButton.setVisibility(View.GONE);
        RealmManager.with(this).restore(getApplicationContext());
        restoreText.setText(R.string.restore_process_text);
        progressBar.setVisibility(View.GONE);
        restoreButton.setVisibility(View.VISIBLE);
        Prefs.with(this).setPrefRestoreMark(true);
        Log.d(TAG, "restore mark " + Prefs.with(this).getPrefRestoreMark());
        Intent intentToMain = new Intent(RestoreActivity.this, MainActivity.class);
        startActivity(intentToMain.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onBackPressed() {
        Intent intentToMain = new Intent(RestoreActivity.this, MainActivity.class);
        startActivity(intentToMain.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}


