package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * SpendFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link SpendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpendFragment extends Fragment implements View.OnClickListener {
    private static final String JAR_ID = "jarId";
    private static final String VALUE = "valueString";
    TextView spendCashValueText;
    EditText spendCashDescriptionEdit;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button0;
    Button buttonDot;
    Button buttonBack;
    Button buttonSpend;
    PublishSubject<Boolean> finishSpendSubject = PublishSubject.create();
    private String jarId;
    private StringBuilder valueString = new StringBuilder();
    private RealmManager realmManager;

    //private OnFragmentInteractionListener mListener;

    public SpendFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SpendFragment newInstance(String jarId) {
        SpendFragment fragment = new SpendFragment();
        Bundle args = new Bundle();
        args.putString(JAR_ID, jarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jarId = getArguments().getString(JAR_ID);
            valueString.append(getArguments().getString(VALUE, "0"));
        }
        realmManager = RealmManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spend, container, false);
        spendCashValueText = (TextView) view.findViewById(R.id.spendCashInputText);
        spendCashDescriptionEdit = (EditText) view.findViewById(R.id.spendCashDescriptionText);

        button1 = (Button) view.findViewById(R.id.spend1Button);
        button2 = (Button) view.findViewById(R.id.spend2Button);
        button3 = (Button) view.findViewById(R.id.spend3Button);
        button4 = (Button) view.findViewById(R.id.spend4Button);
        button5 = (Button) view.findViewById(R.id.spend5Button);
        button6 = (Button) view.findViewById(R.id.spend6Button);
        button7 = (Button) view.findViewById(R.id.spend7Button);
        button8 = (Button) view.findViewById(R.id.spend8Button);
        button9 = (Button) view.findViewById(R.id.spend9Button);
        button0 = (Button) view.findViewById(R.id.spend0Button);
        buttonDot = (Button) view.findViewById(R.id.spendDecimalButton);
        buttonBack = (Button) view.findViewById(R.id.spendBackButton);
        buttonSpend = (Button) view.findViewById(R.id.spendSaveButton);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonSpend.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public Observable<Boolean> finishSpend() {
        return finishSpendSubject.asObservable();
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

    @Override
    public void onClick(View v) {
        if (valueString.toString().equals("0") && v.getId() != R.id.spendDecimalButton) {
            valueString.deleteCharAt(0);
        }
        switch (v.getId()) {
            case R.id.spend1Button: {
                valueString.append("1");
                break;
            }
            case R.id.spend2Button: {
                valueString.append("2");
                break;
            }
            case R.id.spend3Button: {
                valueString.append("3");
                break;
            }
            case R.id.spend4Button: {
                valueString.append("4");
                break;
            }
            case R.id.spend5Button: {
                valueString.append("5");
                break;
            }
            case R.id.spend6Button: {
                valueString.append("6");
                break;
            }
            case R.id.spend7Button: {
                valueString.append("7");
                break;
            }
            case R.id.spend8Button: {
                valueString.append("8");
                break;
            }
            case R.id.spend9Button: {
                valueString.append("9");
                break;
            }
            case R.id.spend0Button: {
                if (!valueString.toString().equals("0")) {
                    valueString.append("0");
                }
                break;
            }
            case R.id.spendDecimalButton: {
                if (!valueString.toString().contains(".")) {
                    valueString.append(".");
                }
                break;
            }
            case R.id.spendBackButton: {
                valueString.deleteCharAt(valueString.length() - 1);
                if (valueString.length() == 0) {
                    valueString.append("0");
                }
                break;
            }
            case R.id.spendSaveButton: {
                float spendSum = 0;
                try {
                    spendSum = -(Float.parseFloat(valueString.toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isAdded = realmManager.addCashToJar(jarId, spendSum,
                        Prefs.with(getContext()).getPercentJar(jarId),
                        spendCashDescriptionEdit.getText().toString());
                if (isAdded) {
                    valueString.delete(0, valueString.length());
                    valueString.append("0");
                    Toast.makeText(getContext(), R.string.added_sum_text, Toast.LENGTH_SHORT).show();
                    finishSpendSubject.onNext(isAdded);
                    break;
                } else {
                    Toast.makeText(getContext(), R.string.not_added_sum_text, Toast.LENGTH_SHORT).show();
                }
            }

        }
        spendCashValueText.setText(valueString);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VALUE, valueString.toString());
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
/*    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
