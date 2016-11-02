package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.BottleDrawableManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class AddCashFlowFragment extends Fragment implements View.OnClickListener,
        PourToJarFragment.OnPourInJarListener {
    public static final String ARG_JAR_ID = "jarID";
    private static final int POUR_RQ_CODE = 6;
    private static final List<String> jarIDs
            = Arrays.asList("NEC", "PLAY", "GIVE", "EDU", "LTSS", "FFA");

    EditText sumEditText;
    TextView getCurrBalanceJars;
    TextView currBalanceNEC;
    TextView currBalancePLAY;
    TextView currBalanceGIVE;
    TextView currBalanceEDU;
    TextView currBalanceLTSS;
    TextView currBalanceFFA;
    ImageView bottlesAll;
    ImageView bottleNEC;
    ImageView bottlePLAY;
    ImageView bottleGIVE;
    ImageView bottleEDU;
    ImageView bottleLTSS;
    ImageView bottleFFA;
    RelativeLayout allJars;
    RelativeLayout jarNEC;
    RelativeLayout jarPLAY;
    RelativeLayout jarGIVE;
    RelativeLayout jarEDU;
    RelativeLayout jarLTSS;
    RelativeLayout jarFFA;
    Button saveButton;
    private String jarID;
    private float sum;
    private int currPercent;
    private List<Jar> jars; //to add in all by percentage
    private Realm realm;
    private float sumOfJars;


    public AddCashFlowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get realm instance
        this.realm = RealmManager.with(this).getRealm();
        if (savedInstanceState == null) {
            jarID = "NoID";
        } else {
            jarID = savedInstanceState.getString(ARG_JAR_ID, "NoID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_cash_flow, container, false);

        sumEditText = (EditText) view.findViewById(R.id.cashAddEdit);
        sumEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        sumEditText.setSelection(sumEditText.getText().length());

        getCurrBalanceJars = (TextView) view.findViewById(R.id.currBalanceJars);
        currBalanceNEC = (TextView) view.findViewById(R.id.currBalanceNECJar);
        currBalancePLAY = (TextView) view.findViewById(R.id.currBalancePLAYJar);
        currBalanceGIVE = (TextView) view.findViewById(R.id.currBalanceGIVEJar);
        currBalanceEDU = (TextView) view.findViewById(R.id.currBalanceEDUJar);
        currBalanceLTSS = (TextView) view.findViewById(R.id.currBalanceLTSSJar);
        currBalanceFFA = (TextView) view.findViewById(R.id.currBalanceFFAJar);

        bottlesAll = (ImageView) view.findViewById(R.id.addAllJars_imageView);
        bottleNEC = (ImageView) view.findViewById(R.id.addNECJar_imageView);
        bottlePLAY = (ImageView) view.findViewById(R.id.addPLAYJar_imageView);
        bottleGIVE = (ImageView) view.findViewById(R.id.addGIVEJar_imageView);
        bottleEDU = (ImageView) view.findViewById(R.id.addEDUJar_imageView);
        bottleLTSS = (ImageView) view.findViewById(R.id.addLTSSJar_imageView);
        bottleFFA = (ImageView) view.findViewById(R.id.addFFAJar_imageView);

        allJars = (RelativeLayout) view.findViewById(R.id.addAllJars);
        jarNEC = (RelativeLayout) view.findViewById(R.id.addNECJar);
        jarPLAY = (RelativeLayout) view.findViewById(R.id.addPLAYJar);
        jarGIVE = (RelativeLayout) view.findViewById(R.id.addGIVEJar);
        jarEDU = (RelativeLayout) view.findViewById(R.id.addEDUJar);
        jarLTSS = (RelativeLayout) view.findViewById(R.id.addLTSSJar);
        jarFFA = (RelativeLayout) view.findViewById(R.id.addFFAJar);

        allJars.setClickable(true);
        jarNEC.setClickable(true);
        jarPLAY.setClickable(true);
        jarGIVE.setClickable(true);
        jarEDU.setClickable(true);
        jarLTSS.setClickable(true);
        jarFFA.setClickable(true);

        allJars.setOnClickListener(this);
        jarNEC.setOnClickListener(this);
        jarPLAY.setOnClickListener(this);
        jarGIVE.setOnClickListener(this);
        jarEDU.setOnClickListener(this);
        jarLTSS.setOnClickListener(this);
        jarFFA.setOnClickListener(this);

        saveButton = (Button) view.findViewById(R.id.saveCashAddButton);
        saveButton.setOnClickListener(this);

        setBalance();
        return view;
    }

    private void setBottles() {

        bottlesAll.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "AllJars"));
        bottleNEC.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "NEC"));
        bottlePLAY.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "PLAY"));
        bottleGIVE.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "GIVE"));
        bottleEDU.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "EDU"));
        bottleLTSS.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "LTSS"));
        bottleFFA.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "FFA"));

        setCheckedJar();
    }

    private void setCheckedJar() {
        allJars.setBackgroundResource(jarID.equals("AllJars") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarNEC.setBackgroundResource(jarID.equals("NEC") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarPLAY.setBackgroundResource(jarID.equals("PLAY") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarGIVE.setBackgroundResource(jarID.equals("GIVE") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarEDU.setBackgroundResource(jarID.equals("EDU") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarLTSS.setBackgroundResource(jarID.equals("LTSS") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarFFA.setBackgroundResource(jarID.equals("FFA") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
    }

    private void setBalance() {
        // get all Jars
        jars = RealmManager.with(this).getJars();
        Log.d("VOlga", "Getting jars elements: " + jars.size());

        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("##,##0.00", s);

        currBalanceNEC.setText(getString(R.string.cash_balance_text, f.format(jars.get(0).getTotalCash())));
        currBalancePLAY.setText(getString(R.string.cash_balance_text, f.format(jars.get(1).getTotalCash())));
        currBalanceGIVE.setText(getString(R.string.cash_balance_text, f.format(jars.get(2).getTotalCash())));
        currBalanceEDU.setText(getString(R.string.cash_balance_text, f.format(jars.get(3).getTotalCash())));
        currBalanceLTSS.setText(getString(R.string.cash_balance_text, f.format(jars.get(4).getTotalCash())));
        currBalanceFFA.setText(getString(R.string.cash_balance_text, f.format(jars.get(5).getTotalCash())));

        sumOfJars = 0;
        for (Jar jar : jars) {
            sumOfJars += jar.getTotalCash();
        }
        getCurrBalanceJars.setText(getString(R.string.cash_balance_text, f.format(sumOfJars)));

        setBottles();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addAllJars: {
                jarID = (jarID.equals("AllJars") ? "NoID" : "AllJars");
                //jarID = "AllJars";  //TODO IN here
                break;
            }
            case R.id.addNECJar: {
                jarID = (jarID.equals(jarIDs.get(0)) ? "NoID" : jarIDs.get(0));
                break;
            }
            case R.id.addPLAYJar: {
                jarID = (jarID.equals(jarIDs.get(1)) ? "NoID" : jarIDs.get(1));
                break;
            }
            case R.id.addGIVEJar: {
                jarID = (jarID.equals(jarIDs.get(2)) ? "NoID" : jarIDs.get(2));
                break;
            }
            case R.id.addEDUJar: {
                jarID = (jarID.equals(jarIDs.get(3)) ? "NoID" : jarIDs.get(3));
                break;
            }
            case R.id.addLTSSJar: {
                jarID = (jarID.equals(jarIDs.get(4)) ? "NoID" : jarIDs.get(4));
                break;
            }
            case R.id.addFFAJar: {
                jarID = (jarID.equals(jarIDs.get(5)) ? "NoID" : jarIDs.get(5));
                break;
            }
            case R.id.saveCashAddButton: {

                if (sumEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
                } else {
                    // add to one jar or to all
                    sum = Float.parseFloat(sumEditText.getText().toString());

                    //GLOBAL INCOME
                    if (jarID.equals("AllJars")) {
                        //CHOOSE if pour to FFA or LTSS jar needed
                        PourToJarFragment pourFragment = new PourToJarFragment();
                        // SETS the target fragment for use later when sending results
                        pourFragment.setTargetFragment(AddCashFlowFragment.this, POUR_RQ_CODE);
                        pourFragment.show(getFragmentManager(), "choosePourInJar");
                        //////
                        //see onChooseToPour()
                    } else if (jarID.equals("NoID")) {
                        Toast.makeText(getContext(), R.string.choose_jar_text, Toast.LENGTH_SHORT).show();
                    } else {
                        currPercent = Prefs.with(getContext()).getPercentJar(jarID);
                        //adding in DB
                        boolean resultAdd = RealmManager.with(this).addCashToJar(jarID, sum,
                                new Date(System.currentTimeMillis()), currPercent, getString(R.string.new_income_text));
                        Log.d("VOlga", "add to " + jarID + " new Cashflow "
                                + sum + " - " + resultAdd);
                        Toast.makeText(getContext(), getString(resultAdd ? R.string.added_sum_text
                                : R.string.not_added_sum_text), Toast.LENGTH_SHORT).show();
                        if (resultAdd) {
                            sumEditText.setText("");
                            //new MaxVolume for bottle
                            Prefs.with(getContext()).setMaxVolumeInJar(RealmManager.with(this)
                                    .getJar(jarID).getTotalCash(), jarID);
                        }
                        setBalance();
                    }
                }
            }
            jarID = "NoID";
            break;
        }
        setCheckedJar(); //to other cases different from save
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(ARG_JAR_ID, jarID);
    }

    @Override
    public void onChooseToPour(String jarIdToPour) {
        boolean resultAdd = false;
        for (String jarID : jarIDs) {
            currPercent = Prefs.with(getContext()).getPercentJar(jarID);
            Log.d("VOlga", "perc in jar before adding sum " + jarID + " " + currPercent);
            float sumInJar = sum * ((float) currPercent / 100);
            //adding in DB
            resultAdd = RealmManager.with(this).addCashToJar(jarID, sumInJar,
                    new Date(System.currentTimeMillis()), currPercent, getString(R.string.new_income_text));
            Log.d("VOlga", "add to " + jarID + " new Cashflow "
                    + sumInJar + " - " + resultAdd);

            if (resultAdd) {
                sumEditText.setText("");
                //new MaxVolume for bottle
                Prefs.with(getContext()).setMaxVolumeInJar(RealmManager.with(this)
                        .getJar(jarID).getTotalCash(), jarID);
            }
        }
        Toast.makeText(getContext(), getString(resultAdd ? R.string.added_sum_text
                : R.string.not_added_sum_text), Toast.LENGTH_SHORT).show();
        if (jarIdToPour.equals("NoJar")) {
            Toast.makeText(getContext(), getString(R.string.no_poured_text),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.poured_text) + jarIdToPour,
                    Toast.LENGTH_SHORT).show();
        }
        jarID = "NoID";
        setBalance();
    }
}
