package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class SpendFragment extends Fragment implements View.OnClickListener,
        DatePickerFragment.OnNewDateListener, TimePickerFragment.OnNewTimeListener {
    private static final String JAR_ID = "jarId";
    private static final String VALUE = "valueString";

    private static final String NEW_DATE = "newDate";
    private static final String NEW_TIME = "newTime";
    private static final String FULL_DATE = "fullDate";

    private static final int DATE_RQ_CODE = 1;
    private static final int TIME_RQ_CODE = 2;

    TextView dateViewClicable;
    TextView timeViewClickable;
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

    private String newDate = "NoDate";
    private String newTime = "NoTime";
    private Date fullDate;
    private int[] partsOfDate = new int[5];

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

            newDate = getArguments().getString(NEW_DATE, "NoDate");
            newTime = getArguments().getString(NEW_TIME, "NoTime");
            //partsOfDate = getArguments().getIntArray("parts");
        }
        realmManager = RealmManager.with(this);
        //fullDate = cashflow.getDate(); was in Cash info
        fullDate = new Date(System.currentTimeMillis());
        splitDateToParts(fullDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spend, container, false);

        dateViewClicable = (TextView) view.findViewById(R.id.dateTextView);
        timeViewClickable = (TextView) view.findViewById(R.id.timeTextView);
        setDateTimeView(fullDate);

        spendCashValueText = (TextView) view.findViewById(R.id.spendCashInputText);
        spendCashDescriptionEdit = (EditText) view.findViewById(R.id.spendCashDescriptionText);
        //spendCashDescriptionEdit.setText("");

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
        dateViewClicable.setOnClickListener(this);
        timeViewClickable.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
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

            case R.id.dateTextView: {
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.setDay(partsOfDate[0]);
                dateFragment.setMonth(partsOfDate[1] - 1);
                dateFragment.setYear(partsOfDate[2]);
                // SETS the target fragment for use later when sending results
                dateFragment.setTargetFragment(SpendFragment.this, DATE_RQ_CODE);
                dateFragment.show(getFragmentManager(), "datePicker");
                break;
            }
            case R.id.timeTextView: {
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.setHour(partsOfDate[3]);
                timeFragment.setMinute(partsOfDate[4]);
                // SETS the target fragment for use later when sending results
                timeFragment.setTargetFragment(SpendFragment.this, TIME_RQ_CODE);
                timeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                break;
            }
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
                //Toast.makeText(getContext(), "Click!!", Toast.LENGTH_SHORT).show();
                Log.d("VOlga", "Click!!");
                float spendSum = 0;
                try {
                    spendSum = -(Float.parseFloat(valueString.toString()));
                    //Toast.makeText(getContext(), "sum " + spendSum, Toast.LENGTH_SHORT).show();
                    Log.d("VOlga", "sum " + spendSum);
                } catch (NumberFormatException e) {
                    //Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(getContext(), "before adding", Toast.LENGTH_SHORT).show();
                Log.d("VOlga", "before adding");

                if (spendSum != 0) {
                    boolean isAdded = realmManager.addCashToJar(jarId, spendSum, fullDate,
                            Prefs.with(getContext()).getPercentJar(jarId),
                            spendCashDescriptionEdit.getText().toString());
                    Log.d("VOlga", "Trying to add a cashflow");
                    if (isAdded) {
                        valueString.delete(0, valueString.length());
                        valueString.append("0");
                        spendCashDescriptionEdit.setText("");
                        Toast.makeText(getContext(), R.string.added_sum_text, Toast.LENGTH_SHORT).show();
                        Log.d("VOlga", getString(R.string.added_sum_text));
                        finishSpendSubject.onNext(isAdded);
                        break;
                    } else {
                        Toast.makeText(getContext(), R.string.not_added_sum_text,
                                Toast.LENGTH_SHORT).show();
                        Log.d("VOlga", getString(R.string.not_added_sum_text));
                    }
                }
            }

        }
        spendCashValueText.setText(valueString);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VALUE, valueString.toString());
        outState.putString(NEW_DATE, newDate);
        outState.putString(NEW_TIME, newTime);
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
