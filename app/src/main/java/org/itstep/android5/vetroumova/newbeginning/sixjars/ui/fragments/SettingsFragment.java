package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

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

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

import java.util.Arrays;
import java.util.List;

//import com.google.android.gms.plus.PlusOneButton;
public class SettingsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        persentageList = Prefs.with(getContext()).getPercentage();
        previousPersentageList = persentageList;
        if (persentageList == null) {
            persentageList = defaultPercentage;
            Log.d("VOlya", "default");
            //Toast.makeText(getContext(), "default", Toast.LENGTH_SHORT).show();
        }
        DebugLogger.log("Pref Logger list : " + persentageList.toString());
        Log.d("Volya", "Pref list : " + persentageList.toString());
        for (Integer i : persentageList) {
            DebugLogger.log("Pref Logger list : " + i);
            Log.d("Volya", "Pref list : " + i);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        //restoreButton.setEnabled(false);
        restoreButton.setOnClickListener(this);
        defaultButton = (Button) view.findViewById(R.id.defaultSettingsButton);
        defaultButton.setOnClickListener(this);

        langSpinner = (AppCompatSpinner) view.findViewById(R.id.settingsLangSpinner);
        //langSpinner.setOnItemClickListener(this);
        //langSpinner.setSelection(langItem);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveSettingsButton: {

                previousPersentageList = persentageList;
                //restoreButton.setEnabled(true);
                persentageList.clear();
                int sum = 0;

                //TODO make a check for non-integer values
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
                //Snackbar.make(getActivity().findViewById(),"")
                Toast.makeText(getContext(), R.string.hint_settings_text, Toast.LENGTH_SHORT).show();
                //restoreButton.setEnabled(false);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /*Resources res = getContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("RU".toLowerCase()));
        res.getConfiguration().updateFrom(conf);
                //updateConfiguration(conf, dm);*/
    }

}
