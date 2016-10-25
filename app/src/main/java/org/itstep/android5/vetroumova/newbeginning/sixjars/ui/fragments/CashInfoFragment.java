package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class CashInfoFragment extends DialogFragment implements View.OnClickListener,
        DatePickerFragment.OnNewDateListener, TimePickerFragment.OnNewTimeListener {

    private static final String CASH_ID = "cashflowId";
    private static final String VALUE = "valueString";
    private static final String NEW_JAR = "newJarID";
    private static final String NEW_DATE = "newDate";
    private static final String NEW_TIME = "newTime";
    private static final String FULL_DATE = "fullDate";

    private static final int DATE_RQ_CODE = 1;
    private static final int TIME_RQ_CODE = 2;


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
    Button buttonNegative;
    Button buttonSave;

    PublishSubject<Boolean> finishEditSubject = PublishSubject.create();
    private RealmManager realmManager;

    private long cashflowID;
    private Cashflow cashflow;
    private StringBuilder valueString = new StringBuilder();
    private String newJarID = "NoID";
    private String newDate = "NoDate"; //if newInstance - will be empty
    private String newTime = "NoTime"; //if newInstance - will be empty
    private Date fullDate;
    private int[] partsOfDate = new int[5];

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
            newJarID = getArguments().getString(NEW_JAR, cashflow.getJar().getJar_id());
            newDate = getArguments().getString(NEW_DATE, "NoDate");
            newTime = getArguments().getString(NEW_TIME, "NoTime");
            //partsOfDate = getArguments().getIntArray("parts");
        } else {
            cashflow = realmManager.getCashflowByID(cashflowID);
            valueString.append(String.valueOf(cashflow.getSum()));
            newJarID = cashflow.getJar().getJar_id();
        }
        fullDate = cashflow.getDate();
        splitDateToParts(fullDate);
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

        setJarDrawable();

        dateViewClicable = (TextView) view.findViewById(R.id.dateTextView);
        timeViewClickable = (TextView) view.findViewById(R.id.timeTextView);

        setDateTimeView(fullDate);

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
        buttonNegative = (Button) view.findViewById(R.id.spendNegativeButton);
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
        buttonNegative.setOnClickListener(this);
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

    public void setJarDrawable() {

        jarNECButton.setBackgroundResource(newJarID.equals("NEC") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
        jarPLAYButton.setBackgroundResource(newJarID.equals("PLAY") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
        jarGIVEButton.setBackgroundResource(newJarID.equals("GIVE") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
        jarEDUButton.setBackgroundResource(newJarID.equals("EDU") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
        jarLTSSButton.setBackgroundResource(newJarID.equals("LTSS") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
        jarFFAButton.setBackgroundResource(newJarID.equals("FFA") ?
                R.drawable.jar_widget_selected_bg : R.drawable.jar_widget_bg);
    }

    public void setDateTimeView(Date fullDate) {
        // from date to string
        //to cooperate with pickers
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        newDate = dateFormat.format(fullDate);
        dateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        newTime = dateFormat.format(fullDate);

        // to correct a visible date
        dateFormat = new SimpleDateFormat("d-MMMM-yyyy", Locale.getDefault());
        dateViewClicable.setText(dateFormat.format(fullDate));

        timeViewClickable.setText(newTime);
    }

    public Date getDateTimeDataFromStrings(String date) {
        // from string to date
        //Toast.makeText(getContext(), "Concat : " + date, Toast.LENGTH_SHORT).show();
        Log.d("VOlga", "Concat : " + date);

        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy H:mm");
        try {
            Date fullDate = format.parse(date);
            //Toast.makeText(getContext(), "Parsed : " + fullDate, Toast.LENGTH_SHORT).show();
            Log.d("VOlga", "Parsed : " + fullDate);
            return fullDate;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //TODO normal return
            return new Date(System.currentTimeMillis());
        }
    }

    public void splitDateToParts(Date fullDate) {
        //partsOfDate = new int[5];
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy-H-mm", Locale.getDefault());
        String dateString = dateFormat.format(fullDate);
        String[] str = dateString.split("-");
        for (int i = 0; i < partsOfDate.length; i++) {
            partsOfDate[i] = Integer.parseInt(str[i]);
            Log.d("VOlga", "[" + i + "] " + partsOfDate[i]);
        }
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
        outState.putString(NEW_JAR, newJarID);
        outState.putString(NEW_DATE, newDate);
        outState.putString(NEW_TIME, newTime);
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
            case R.id.spendNegativeButton: {

                float newSum = 0;
                try {
                    newSum = Float.parseFloat(valueString.toString());
                    if (newSum == 0) {
                        valueString.deleteCharAt(0);
                        valueString.append("-");
                    } else {
                        newSum = -newSum;
                        valueString.delete(0, valueString.length());
                        if (newSum - ((int) newSum) != 0) {
                            valueString.append(newSum);
                        } else {
                            valueString.append((int) newSum);
                        }
                    }
                } catch (NumberFormatException e) {
                    valueString.delete(0, valueString.length());
                    valueString.append("0");
                }

                break;
            }
            case R.id.cashEditNECButton: {
                newJarID = getString(R.string.db_jar_id_NEC);
                setJarDrawable();
                break;
            }
            case R.id.cashEditPLAYButton: {
                newJarID = getString(R.string.db_jar_id_PLAY);
                setJarDrawable();
                break;
            }
            case R.id.cashEditGIVEButton: {
                newJarID = getString(R.string.db_jar_id_GIVE);
                setJarDrawable();
                break;
            }
            case R.id.cashEditEDUButton: {
                newJarID = getString(R.string.db_jar_id_EDU);
                setJarDrawable();
                break;
            }
            case R.id.cashEditLTSSButton: {
                newJarID = getString(R.string.db_jar_id_LTSS);
                setJarDrawable();
                break;
            }
            case R.id.cashEditFFAButton: {
                newJarID = getString(R.string.db_jar_id_FFA);
                setJarDrawable();
                break;
            }
            case R.id.dateTextView: {
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.setDay(partsOfDate[0]);
                dateFragment.setMonth(partsOfDate[1] - 1);
                dateFragment.setYear(partsOfDate[2]);
                // SETS the target fragment for use later when sending results
                dateFragment.setTargetFragment(CashInfoFragment.this, DATE_RQ_CODE);
                dateFragment.show(getFragmentManager(), "datePicker");
                break;
            }
            case R.id.timeTextView: {
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.setHour(partsOfDate[3]);
                timeFragment.setMinute(partsOfDate[4]);
                // SETS the target fragment for use later when sending results
                timeFragment.setTargetFragment(CashInfoFragment.this, TIME_RQ_CODE);
                timeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                break;
            }
            case R.id.spendSaveButton: {
                float newSum = 0;
                float oldSum = cashflow.getSum();   // for Prefs
                try {
                    newSum = Float.parseFloat(valueString.toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newSum != 0) {
                    boolean isEdited = realmManager.editCashflow(cashflowID, fullDate,
                            newSum, descriptionEdit.getText().toString(), newJarID);
                    if (isEdited) {
                        /*valueString.delete(0, valueString.length());
                        valueString.append("0");*/
                        Toast.makeText(getContext(), R.string.edited_cashflow, Toast.LENGTH_SHORT).show();
                        descriptionEdit.setText("");

                        //to correct maxvalue in prefs
                        // не учитывает перевод в разные кувшины
                        if (oldSum > 0 && oldSum < newSum) {
                            Prefs.with(getContext()).setMaxVolumeInJar(
                                    cashflow.getJar().getTotalCash(), cashflow.getJar().getJar_id());
                        }
                        finishEditSubject.onNext(isEdited);
                        break;
                    } else {
                        Toast.makeText(getContext(), R.string.not_added_sum_text,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.delete_not_edit_text,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        inputSumView.setText(valueString);
    }

    public Observable<Boolean> isFinishEdit() {
        return finishEditSubject.asObservable();
    }

    @Override
    public void onNewDate(int year, int month, int day) {
        //data from DatePickerDialog    "d-M-yyyy H:mm", Locale.getDefault()
        StringBuilder sb = new StringBuilder();
        /*String dayString = String.valueOf(day);
        if (day < 10) {
            dayString = "0".concat(dayString);
        }*/
        sb.append(day).append("-").append(month + 1).append("-").append(year);
        Log.d("VOlga", "StringBuilder " + sb);
        newDate = sb.toString();
        sb.append(" ").append(newTime);
        fullDate = getDateTimeDataFromStrings(sb.toString());
        splitDateToParts(fullDate);
        setDateTimeView(fullDate);
    }

    @Override
    public void onNewTime(int hour, int minute) {
        //time from TimePickerDialog
        StringBuilder sb = new StringBuilder();
        sb.append(hour).append(":");
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute); //.append(":").append(second);
        Log.d("VOlga", "StringBuilder " + sb);
        newTime = sb.toString();
        sb = new StringBuilder(newDate).append(" ").append(newTime);
        fullDate = getDateTimeDataFromStrings(sb.toString());
        splitDateToParts(fullDate);
        setDateTimeView(fullDate);
    }
}
