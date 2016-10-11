package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;

import java.util.Date;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * CashInfoFragment.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link CashInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashInfoFragment extends DialogFragment implements View.OnClickListener {
    private static final String CASH_ID = "cashflowId";
    private static final String VALUE = "valueString";
    TextView cashIDText;
    Button jarNECButton;
    Button jarPLAYButton;
    Button jarGIVEButton;
    Button jarEDUButton;
    Button jarLTSSButton;
    Button jarFFAButton;
    TextView dateViewClicable;
    TextView timeViewClickable;
    TextView inputSumView;
    EditText descriptionEdit;
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
    Button buttonSave;
    PublishSubject<Boolean> finishEditSubject = PublishSubject.create();
    private long cashflowID;
    private Cashflow cashflow;
    private RealmManager realmManager;
    private StringBuilder valueString = new StringBuilder();

    //private OnFragmentInteractionListener mListener;

    public CashInfoFragment() {
        // Required empty public constructor
    }

    public static CashInfoFragment newInstance(long cashflowID) {
        CashInfoFragment fragment = new CashInfoFragment();
        Bundle args = new Bundle();
        args.putLong(CASH_ID, cashflowID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmManager = RealmManager.with(this);

        if (getArguments() != null) {
            cashflowID = getArguments().getLong(CASH_ID);
            cashflow = realmManager.getCashflowByID(cashflowID);
            valueString.append(getArguments().getString(VALUE, String.valueOf(cashflow.getSum())));
        } else {
            cashflow = realmManager.getCashflowByID(cashflowID);
            valueString.append(String.valueOf(cashflow.getSum()));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cash_info, container, false);

        cashIDText = (TextView) view.findViewById(R.id.cashIDText);
        cashIDText.setText(getString(R.string.cash_id_text, cashflowID));
        inputSumView = (TextView) view.findViewById(R.id.cashEditInputText);
        inputSumView.setText(valueString);
        descriptionEdit = (EditText) view.findViewById(R.id.cashEditDescriptionText);
        descriptionEdit.setText(cashflow.getDescription());

        jarNECButton = (Button) view.findViewById(R.id.cashEditNECButton);
        jarPLAYButton = (Button) view.findViewById(R.id.cashEditPLAYButton);
        jarGIVEButton = (Button) view.findViewById(R.id.cashEditGIVEButton);
        jarEDUButton = (Button) view.findViewById(R.id.cashEditEDUButton);
        jarLTSSButton = (Button) view.findViewById(R.id.cashEditLTSSButton);
        jarFFAButton = (Button) view.findViewById(R.id.cashEditFFAButton);
        dateViewClicable = (TextView) view.findViewById(R.id.dateTextView);
        timeViewClickable = (TextView) view.findViewById(R.id.timeTextView);

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
        buttonSave = (Button) view.findViewById(R.id.spendSaveButton);

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
        buttonSave.setOnClickListener(this);
        jarNECButton.setOnClickListener(this);
        jarPLAYButton.setOnClickListener(this);
        jarGIVEButton.setOnClickListener(this);
        jarEDUButton.setOnClickListener(this);
        jarLTSSButton.setOnClickListener(this);
        jarFFAButton.setOnClickListener(this);
        dateViewClicable.setOnClickListener(this);
        timeViewClickable.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void setCashflowID(long cashflowID) {
        this.cashflowID = cashflowID;
        cashIDText.setText(String.valueOf(cashflowID));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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
        //mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VALUE, valueString.toString());
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
                if (valueString.length() != 0) {
                    valueString.deleteCharAt(valueString.length() - 1);
                    if (valueString.length() == 0) {
                        valueString.append("0");
                    }
                }
                break;
            }
            case R.id.cashEditNECButton: {
                break;
            }
            case R.id.cashEditPLAYButton: {
                break;
            }
            case R.id.cashEditGIVEButton: {
                break;
            }
            case R.id.cashEditEDUButton: {
                break;
            }
            case R.id.cashEditLTSSButton: {
                break;
            }
            case R.id.cashEditFFAButton: {
                break;
            }
            case R.id.dateTextView: {
                break;
            }
            case R.id.timeTextView: {
                break;
            }
            case R.id.spendSaveButton: {
                float newSum = 0;
                try {
                    newSum = -(Float.parseFloat(valueString.toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newSum > 0) {
                    boolean isEdited = realmManager.editCash(cashflowID, newSum,
                            new Date(System.currentTimeMillis()), descriptionEdit.getText().toString());
                    if (isEdited) {
                        valueString.delete(0, valueString.length());
                        valueString.append("0");
                        Toast.makeText(getContext(), R.string.edited_cashflow, Toast.LENGTH_SHORT).show();
                        finishEditSubject.onNext(isEdited);
                        break;
                    } else {
                        Toast.makeText(getContext(), R.string.not_added_sum_text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        inputSumView.setText(valueString);
    }

    public Observable<Boolean> isFinishEdit() {
        return finishEditSubject.asObservable();
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
