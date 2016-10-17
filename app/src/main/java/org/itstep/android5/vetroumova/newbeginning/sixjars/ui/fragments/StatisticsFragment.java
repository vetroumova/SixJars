package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

//import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    BarChart barChart;
    PieChart pieChart;
    //private PlusOneButton mPlusOneButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RealmManager realmManager;
    private OnFragmentInteractionListener mListener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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

        realmManager = RealmManager.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // in this example, a LineChart is initialized from xml
        /*LineChart chart = (LineChart) view.findViewById(R.id.chartLinear);
        //List <Cashflow> cashflows = new ArrayList<>();
        Point[] points = {new Point(1, 1), new Point(2, 2), new Point(3, 2), new Point(4, 4)};
        List<Entry> entries = new ArrayList<Entry>();
        for (Point data : points) {
            // turn your data into Entry objects
            entries.add(new Entry(data.x, data.y));        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.DKGRAY); // styling, ...
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh*/

        barChart = (BarChart) view.findViewById(R.id.statJarBarChart);
        pieChart = (PieChart) view.findViewById(R.id.statJarPieChart);
        setRealmCharts();
        return view;
    }

    public void setRealmCharts() {
        //Using MPAndroidChart with Realm.io is easier than you think.

        // get realm instance
        Realm realm = realmManager.getRealm();

        // load your data from Realm.io database
        RealmResults<Jar> results = realm.where(Jar.class).findAll();

        AxisValueFormatter formatter = new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return results.get((int) value).getJar_id();
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        AxisBase jarAxis = barChart.getXAxis();
        jarAxis.setDrawGridLines(false);
        jarAxis.setDrawLabels(true);
        jarAxis.setTextColor(getResources().getColor(R.color.colorAccent));
        jarAxis.setTextSize(12f);

        // create a DataSet and specify fields, MPAndroidChart-Realm does the rest
        RealmBarDataSet<Jar> dataSet = new RealmBarDataSet<Jar>(results, "jar_float_id", "totalCash");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));

        ArrayList<IBarDataSet> dataSetList = new ArrayList<IBarDataSet>();
        dataSetList.add(dataSet); // add the dataset

        // create a data object with the dataset list
        BarData data = new BarData(dataSetList);
        // additional data styling...
        data.setBarWidth(0.9f); // set custom bar width

        // set data
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        //barChart.setDrawBarShadow(true);

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        int[] colors = {56, 156, 5, 6, 61, 97};
        String[] labels = {"NEC", "NEC", "NEC", "NEC", "NEC", "NEC"};
        List<String> ll = Arrays.asList(labels);
        //legend.setExtra(colors,labels);
        legend.setComputedLabels(ll);

        barChart.invalidate(); // refresh

        RealmPieDataSet<Jar> dataPieSet = new RealmPieDataSet<Jar>(results, "totalCash");
        //dataPieSet.setColor(getResources().getColor(R.color.colorPrimary));

        /*ArrayList<IPieDataSet> dataPieSetList = new ArrayList<IPieDataSet>();
        dataPieSetList.add(dataPieSet); // add the dataset*/

        // create a data object with the dataset list
        PieData dataPie = new PieData(dataPieSet);
        // additional data styling...

        // set data
        pieChart.setData(dataPie);
        //pieChart.setDrawBarShadow(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorAccent));
        pieChart.animate();
        //pieChart.invalidate(); // refresh
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mListener = null;
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
