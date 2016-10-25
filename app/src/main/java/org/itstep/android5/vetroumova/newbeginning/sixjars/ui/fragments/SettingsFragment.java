package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

import java.util.Arrays;
import java.util.List;

//import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

                //TODO make a check for non-integer values
                persentageList.add(Integer.parseInt(necPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(playPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(givePercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(eduPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(ltssPercentEdit.getText().toString()));
                persentageList.add(Integer.parseInt(ffaPercentEdit.getText().toString()));

                for (Integer i : persentageList) {
                    Toast.makeText(getContext(), "add in list : " + i, Toast.LENGTH_SHORT).show();
                }
                Prefs.with(getContext()).setPercentage(persentageList);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonSettings(View view) {

        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*mListener = null;*/
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
