package com.vetroumova.sixjars.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String LANG = "language";
    private static final String TAG = "VOlga";
    private String lang;
    private List<Integer> persentageList;
    private List<Integer> previousPersentageList;
    private List<Integer> defaultPercentage = Arrays.asList(55, 10, 5, 10, 10, 10);
    private EditText necPercentEdit;
    private EditText playPercentEdit;
    private EditText givePercentEdit;
    private EditText eduPercentEdit;
    private EditText ltssPercentEdit;
    private EditText ffaPercentEdit;
    private Button saveButton;
    private Button restoreButton;
    private Button defaultButton;
    private AppCompatSpinner langSpinner;
    private int langItem = 0;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String lang) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(LANG, lang);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lang = getArguments().getString(LANG, Prefs.with(getContext()).getPrefLanguage());
        } else {
            lang = Prefs.with(getContext()).getPrefLanguage();
        }
        persentageList = Prefs.with(getContext()).getPercentage();
        previousPersentageList = persentageList;
        if (persentageList == null) {
            persentageList = defaultPercentage;
            Log.d(TAG, "default");
        }
        Log.d(TAG, "Pref list : " + persentageList.toString());
        for (Integer i : persentageList) {
            Log.d(TAG, "Pref list : " + i);
        }
        //checkTheColorsMigration();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        necPercentEdit = (EditText) view.findViewById(R.id.settingsNECEdit);
        playPercentEdit = (EditText) view.findViewById(R.id.settingsPLAYEdit);
        givePercentEdit = (EditText) view.findViewById(R.id.settingsGIVEEdit);
        eduPercentEdit = (EditText) view.findViewById(R.id.settingsEDUEdit);
        ltssPercentEdit = (EditText) view.findViewById(R.id.settingsLTSSEdit);
        ffaPercentEdit = (EditText) view.findViewById(R.id.settingsFFAEdit);
        necPercentEdit.setText(String.valueOf(persentageList.get(0)));
        playPercentEdit.setText(String.valueOf(persentageList.get(1)));
        givePercentEdit.setText(String.valueOf(persentageList.get(2)));
        eduPercentEdit.setText(String.valueOf(persentageList.get(3)));
        ltssPercentEdit.setText(String.valueOf(persentageList.get(4)));
        ffaPercentEdit.setText(String.valueOf(persentageList.get(5)));
        saveButton = (Button) view.findViewById(R.id.saveSettingsButton);
        saveButton.setOnClickListener(this);
        restoreButton = (Button) view.findViewById(R.id.restoreSettingsButton);
        restoreButton.setOnClickListener(this);
        defaultButton = (Button) view.findViewById(R.id.defaultSettingsButton);
        defaultButton.setOnClickListener(this);
        langSpinner = (AppCompatSpinner) view.findViewById(R.id.settingsLangSpinner);
        langSpinner.setOnItemSelectedListener(this);
        if (lang.equals("ru")) {
            langItem = 1;
        } else if (lang.equals("uk")) {
            langItem = 2;
        } else langItem = 0;
        langSpinner.setSelection(langItem, true);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveSettingsButton: {
                previousPersentageList = persentageList;
                persentageList.clear();
                int sum = 0;
                persentageList.add(Integer.parseInt(necPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(playPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(givePercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(eduPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(ltssPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(ffaPercentEdit.getText().toString()));
                for (Integer percent : persentageList) {
                    sum += percent;
                }
                if (sum == 100) {
                    Prefs.with(getContext()).setPercentage(persentageList);
                    //save new prefs to user in realm
                    RealmManager.setUserPrefsFromSharedPrefs(getContext(),
                            RealmManager.with(this).getJar("NEC").getUser());   //any of jar to refresh user
                    Toast.makeText(getContext(), getString(R.string.changes_saved_text),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.changes_not_saved_text),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.restoreSettingsButton: {
                persentageList = previousPersentageList;
                necPercentEdit.setText(String.valueOf(previousPersentageList.get(0)));
                playPercentEdit.setText(String.valueOf(previousPersentageList.get(1)));
                givePercentEdit.setText(String.valueOf(previousPersentageList.get(2)));
                eduPercentEdit.setText(String.valueOf(previousPersentageList.get(3)));
                ltssPercentEdit.setText(String.valueOf(previousPersentageList.get(4)));
                ffaPercentEdit.setText(String.valueOf(previousPersentageList.get(5)));
                Toast.makeText(getContext(), R.string.hint_settings_text, Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.defaultSettingsButton: {
                previousPersentageList = persentageList;
                necPercentEdit.setText(String.valueOf(defaultPercentage.get(0)));
                playPercentEdit.setText(String.valueOf(defaultPercentage.get(1)));
                givePercentEdit.setText(String.valueOf(defaultPercentage.get(2)));
                eduPercentEdit.setText(String.valueOf(defaultPercentage.get(3)));
                ltssPercentEdit.setText(String.valueOf(defaultPercentage.get(4)));
                ffaPercentEdit.setText(String.valueOf(defaultPercentage.get(5)));
                Toast.makeText(getContext(), R.string.hint_settings_text, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Configuration config = getContext().getResources().getConfiguration();
        Log.d("VOlga", "item " + i);
        if (i != langItem) {
            if (i == 0) {
                lang = "en";
            } else if (i == 1) {
                lang = "ru";
            } else if (i == 2) {
                lang = "uk";
            }
            if (!"".equals(lang) && !Prefs.with(getContext().getApplicationContext()).equals(lang)) {
                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                config.locale = locale;
                Prefs.with(getContext().getApplicationContext()).setPrefLanguage(lang);
                //save new prefs to user in realm
                RealmManager.setUserPrefsFromSharedPrefs(getContext(),
                        RealmManager.with(this).getJar("NEC").getUser());
                getContext().getResources().updateConfiguration(config,
                        getContext().getResources().getDisplayMetrics());
                Intent intent = getContext().getApplicationContext().getPackageManager()
                        .getLaunchIntentForPackage(getContext().getApplicationContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
