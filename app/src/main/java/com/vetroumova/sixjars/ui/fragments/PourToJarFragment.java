package com.vetroumova.sixjars.ui.fragments;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vetroumova.sixjars.utils.BottleDrawableManager;
import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Jar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class PourToJarFragment extends DialogFragment implements View.OnClickListener {

    private static final String CHOSEN_JAR = "chosenJar";
    ImageView bottleLTSS;
    ImageView bottleFFA;
    TextView currBalanceLTSS;
    TextView currBalanceFFA;
    RelativeLayout noJars;
    RelativeLayout jarLTSS;
    RelativeLayout jarFFA;
    Button saveButton;
    private float pourSum;
    private String chosenJar = "NoJar";
    private Realm realm;
    private List<Jar> jars;

    public PourToJarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PourToJarFragment newInstance(float pourSum, String chosenJar) {
        PourToJarFragment fragment = new PourToJarFragment();
        Bundle args = new Bundle();
        args.putString(CHOSEN_JAR, chosenJar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chosenJar = getArguments().getString(CHOSEN_JAR, "NoJar"); //default - no pour
        }
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_FRAME, theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar;
            /*case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;*/
        setStyle(style, theme);
        jars = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pour_to_jar, container, false);
        currBalanceLTSS = (TextView) view.findViewById(R.id.pourLTSSJarBalanceText);
        currBalanceFFA = (TextView) view.findViewById(R.id.pourFFAJarBalanceText);

        bottleLTSS = (ImageView) view.findViewById(R.id.pourLTSSJar_imageView);
        bottleFFA = (ImageView) view.findViewById(R.id.pourFFAJar_imageView);

        noJars = (RelativeLayout) view.findViewById(R.id.pourNoJar);
        jarLTSS = (RelativeLayout) view.findViewById(R.id.pourLTSSJar);
        jarFFA = (RelativeLayout) view.findViewById(R.id.pourFFAJar);
        noJars.setClickable(true);
        jarLTSS.setClickable(true);
        jarFFA.setClickable(true);

        noJars.setOnClickListener(this);
        jarLTSS.setOnClickListener(this);
        jarFFA.setOnClickListener(this);

        saveButton = (Button) view.findViewById(R.id.savePourButton);
        saveButton.setOnClickListener(this);

        setBalance();
        return view;
    }

    private void setBalance() {
        // get all Jars
        jars = RealmManager.with(this).getJars();
        Log.d("VOlga", "Getting jars elements: " + jars.size());
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        currBalanceLTSS.setText(getString(R.string.cash_balance_text, f.format(jars.get(4).getTotalCash())));
        currBalanceFFA.setText(getString(R.string.cash_balance_text, f.format(jars.get(5).getTotalCash())));
        setBottles();
    }

    private void setBottles() {
        bottleLTSS.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "LTSS"));
        bottleFFA.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), "FFA"));
        setCheckedJar();
    }

    private void setCheckedJar() {
        noJars.setBackgroundResource(chosenJar.equals("NoJar") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarLTSS.setBackgroundResource(chosenJar.equals("LTSS") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);
        jarFFA.setBackgroundResource(chosenJar.equals("FFA") ? R.drawable.jar_widget_selected_bg
                : R.drawable.jar_widget_bg);

        if (chosenJar.equals("LTSS")) {
            Toast.makeText(getContext(), getString(R.string.global_pour_to_ltss_text),
                    Toast.LENGTH_SHORT).show();
        } else if (chosenJar.equals("FFA")) {
            Toast.makeText(getContext(), getString(R.string.global_pour_to_ffa_text),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setStyle(int style, @StyleRes int theme) {
        super.setStyle(style, theme);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pourNoJar: {
                chosenJar = "NoJar";
                break;
            }
            case R.id.pourLTSSJar: {
                chosenJar = "LTSS";
                break;
            }
            case R.id.pourFFAJar: {
                chosenJar = "FFA";
                break;
            }
            case R.id.savePourButton: {
                //GLOBAL INCOME
                pourSum = 0;
                if (chosenJar.equals("LTSS")) {
                    //deleting from other jars
                    for (int i = 0; i < 4; i++) {
                        Jar jar = jars.get(i);
                        if (jar.getTotalCash() > 0) {
                            pourSum += jar.getTotalCash();
                            boolean resultDeleteSum = RealmManager.with(this).addCashToJar(jar.getJar_id(),
                                    -jar.getTotalCash(), new Date(System.currentTimeMillis()),
                                    Prefs.with(getContext()).getPercentJar(jar.getJar_id()),
                                    getString(R.string.pour_to_ltss_text));
                            if (!resultDeleteSum) {
                                Log.d("VOlga", "not poured from " + jar.getJar_id()
                                        + " to LTSS - " + resultDeleteSum);
                                //break;
                            }
                        }
                    }
                    //pourSum is full
                    //adding in DB
                    if (pourSum > 0) {
                        boolean resultAdd = RealmManager.with(this).addCashToJar(chosenJar, pourSum,
                                new Date(System.currentTimeMillis()),
                                Prefs.with(getContext()).getPercentJar(chosenJar),
                                getString(R.string.global_pour_to_ltss_text));
                        Log.d("VOlga", "pour to " + chosenJar + " rest of jars "
                                + pourSum + " - " + resultAdd);
                        if (resultAdd) {
                            //new MaxVolume for bottle
                            Prefs.with(getContext()).setMaxVolumeInJar(RealmManager.with(this)
                                    .getJar(chosenJar).getTotalCash(), chosenJar);
                        }
                        //setBalance();
                    }

                } else if (chosenJar.equals("FFA")) {
                    //deleting from other jars
                    for (int i = 0; i < 4; i++) {   // skip LTSS, or 5 - with LTSS
                        Jar jar = jars.get(i);
                        if (jar.getTotalCash() > 0) {
                            pourSum += jar.getTotalCash();
                            boolean resultDeleteSum = RealmManager.with(this).addCashToJar(jar.getJar_id(),
                                    -jar.getTotalCash(), new Date(System.currentTimeMillis()),
                                    Prefs.with(getContext()).getPercentJar(jar.getJar_id()),
                                    getString(R.string.pour_to_ffa_text));
                            if (!resultDeleteSum) {
                                Log.d("VOlga", "not poured from " + jar.getJar_id()
                                        + " to FFA - " + resultDeleteSum);
                                //break;
                            }
                        }
                    }
                    //pourSum is full
                    //adding in DB
                    if (pourSum > 0) {
                        boolean resultAdd = RealmManager.with(this).addCashToJar(chosenJar, pourSum,
                                new Date(System.currentTimeMillis()),
                                Prefs.with(getContext()).getPercentJar(chosenJar),
                                getString(R.string.global_pour_to_ffa_text));
                        Log.d("VOlga", "pour to " + chosenJar + " rest of jars "
                                + pourSum + " - " + resultAdd);
                        if (resultAdd) {
                            //new MaxVolume for bottle
                            Prefs.with(getContext()).setMaxVolumeInJar(RealmManager.with(this)
                                    .getJar(chosenJar).getTotalCash(), chosenJar);
                        }
                        //setBalance();
                    }
                }
                //for all cases
                OnPourInJarListener listener = (OnPourInJarListener) getTargetFragment();
                listener.onChooseToPour(chosenJar);
                //chosenJar = "NoJar";
                dismiss();
                break;
            }
        }
        setCheckedJar(); //to other cases different from save
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(CHOSEN_JAR, chosenJar);
    }

    public interface OnPourInJarListener {
        void onChooseToPour(String jar);
    }
}
