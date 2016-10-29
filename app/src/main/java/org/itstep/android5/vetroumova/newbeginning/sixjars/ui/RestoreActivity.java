package org.itstep.android5.vetroumova.newbeginning.sixjars.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;

/**
 * A db restore screen that offers to restore from archive.
 */
public class RestoreActivity extends AppCompatActivity
        //implements android.support.v4.app.LoaderManager.LoaderCallbacks
{

    private static final long PAUSE = 10;
    //public static final int LOADER_ID = 1;
    //private Loader mLoader;
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
        //loader
        //Bundle bundle = new Bundle();
        // Инициализируем загрузчик с идентификатором
        // Если загрузчик не существует, то он будет создан,
        // иначе он будет перезапущен.
        //mLoader = getSupportLoaderManager().initLoader(LOADER_ID,bundle,this);

        //cannot work with realm in another thread except asynctransactions
        progressBar.setVisibility(View.VISIBLE);
        restoreButton.setVisibility(View.GONE);
        RealmManager.with(this).restore(getApplicationContext());
        restoreText.setText(R.string.restore_process_text);
        progressBar.setVisibility(View.GONE);
        restoreButton.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), R.string.restore_done_text, Toast.LENGTH_SHORT).show();
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

            /*@Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        restoreButton.setVisibility(View.GONE);
        Loader mLoader = null;
        // условие можно убрать, если вы используете только один загрузчик
        if (id == LOADER_ID) {
            mLoader = new RestoreLoader(this);
            Log.d(TAG, "onCreateLoader");
        }
        return mLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        progressBar.setVisibility(View.GONE);
        restoreButton.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Restore is done",Toast.LENGTH_SHORT).show();
        Intent intentToMain = new Intent(RestoreActivity.this,MainActivity.class);
        startActivity(intentToMain.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        progressBar.setVisibility(View.GONE);
        restoreButton.setVisibility(View.VISIBLE);
    }

    public class RestoreLoader extends AsyncTaskLoader {

        final String TAG = "VOlga";
        final int PAUSE = 3;
        public final String IS_RESTORED = "Not restored";

        public RestoreLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {

            Log.d(TAG, "loadInBackground");
            // cannot work with realm in another thread
            //RealmManager.getInstance().restore(getApplicationContext());
            try {
                TimeUnit.SECONDS.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Restore done";
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            Log.d(TAG, "onStartLoading");
            *//*progressBar.setVisibility(View.VISIBLE);
            restoreButton.setVisibility(View.GONE);*//*
            forceLoad();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            Log.d(TAG, "onStopLoading");
            *//*progressBar.setVisibility(View.GONE);
            restoreButton.setVisibility(View.VISIBLE);*//*
        }

        @Override
        public void deliverResult(Object data) {
            super.deliverResult(data);
        }
    }*/


    /*private static class RestoreTask extends AsyncTask<Activity,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            restoreButton.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Activity... params) {
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RealmManager.getInstance().restore(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/
}


