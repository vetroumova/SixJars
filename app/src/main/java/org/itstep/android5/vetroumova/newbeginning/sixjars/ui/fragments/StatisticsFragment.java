package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

//import com.google.android.gms.plus.PlusOneButton;

public class StatisticsFragment extends SimpleChartFragment {
    BarChart barChart;
    PieChart pieChart;
    String[] jarIDs = {"NEC", "PLAY", "GIVE", "EDU", "LTSS", "FFA"};
    Typeface tf;
    private RealmManager realmManager;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmManager = RealmManager.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        barChart = (BarChart) view.findViewById(R.id.statJarBarChart);
        pieChart = (PieChart) view.findViewById(R.id.statJarPieChart);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        pieChart.setCenterTextTypeface(tf);
        pieChart.setCenterText(generateCenterText());
        pieChart.setCenterTextSize(10f);
        pieChart.setCenterTextTypeface(tf);
        pieChart.setDescription(getString(R.string.statistics_pie_title));

        // radius of the center hole in percent of maximum radius
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.animateY(500, Easing.EasingOption.EaseInOutQuad);
        pieChart.setDrawEntryLabels(false);

        Legend l = pieChart.getLegend();
        //l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        /*l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);*/
        l.setMaxSizePercent(0.8f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setPosition(LegendPosition.ABOVE_CHART_CENTER);
        l.setDrawInside(false);

        pieChart.setData(generatePieData());

        //barchart
        barChart.setDescription(getString(R.string.statistics_bar_title));
        barChart.setDescriptionPosition(0f, 1f);
        barChart.setFitBars(true);
        barChart.setNoDataText(getString(R.string.no_data_chart_text));

        /*XAxis xLabels = barChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);*/

        Legend lb = barChart.getLegend();
        lb.setForm(Legend.LegendForm.SQUARE);
        lb.setPosition(LegendPosition.ABOVE_CHART_CENTER);
        lb.setFormSize(6f);
        lb.setFormToTextSpace(2f);
        lb.setXEntrySpace(4f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTypeface(tf);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.LTGRAY);
        xAxis.setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);

        //barChart.setData(generateStackedBarData());
        barChart.setData(generateBarData());
        return view;
    }

    protected PieData generatePieData() {
        // get realm instance
        Realm realm = realmManager.getRealm();
        // load your data from Realm.io database

        int count = 6;
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        RealmResults<Jar> resultsJar = realm.where(Jar.class).findAll();

        for (int i = 0; i < count; i++) {
            //entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i+1)));
            //entries1.add(new PieEntry(results.get(i).getTotalCash(),results.get(i).getJar_id()));
            String jarID = resultsJar.get(i).getJar_id();
            float sumOfPaymentsInJar = realm.where(Cashflow.class)
                    .equalTo("jar.jar_id", jarID)
                    .lessThan("sum", 0f)
                    .findAll()
                    .sum("sum").floatValue();
            if (-sumOfPaymentsInJar > 0) {      //to not add jars with no payments
                entries1.add(new PieEntry(-sumOfPaymentsInJar, jarID));
            }
        }

        PieDataSet ds1 = new PieDataSet(entries1, getString(R.string.statistics_pie_title));
        int[] colors = {R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorChart,
                R.color.colorChartItem, R.color.colorBottomNavigationPrimary, R.color.colorChartDifferent};
        ds1.setColors(ColorTemplate.createColors(getResources(), colors));
        ds1.setSliceSpace(2f);
        ds1.setSelectionShift(5f);
        ds1.setValueTextColor(Color.WHITE);
        //ds1.setValueTypeface(Typeface.DEFAULT_BOLD);
        ds1.setValueTextSize(14f);
        ds1.setDrawValues(true);

        /*ds1.setValueLinePart1OffsetPercentage(80.f);
        ds1.setValueLinePart1Length(0.5f);
        ds1.setValueLinePart2Length(0.1f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/
        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);
        return d;
    }

    protected BarData generateStackedBarData() {
        // get realm instance
        Realm realm = realmManager.getRealm();
        // load your data from Realm.io database
        int count = 6;
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        RealmResults<Jar> resultsJar = realm.where(Jar.class).findAll();

        for (int i = 0; i < count; i++) {
            Jar jar = resultsJar.get(i);
            String jarID = jar.getJar_id();
            //TODO ASYNC
            float sumOfPaymentsInJar = realm.where(Cashflow.class)
                    .equalTo("jar.jar_id", jarID)
                    .lessThan("sum", 0f)
                    .findAll()
                    .sum("sum").floatValue();
            float sumOfIncome = jar.getTotalCash() - sumOfPaymentsInJar;
            if (-sumOfPaymentsInJar > 0 || sumOfIncome > 0) {      //to not add jars with no cashflow
                float[] bars = {-sumOfPaymentsInJar, sumOfIncome};
                entries2.add(new BarEntry(jar.getJar_float_id(), bars, jarID));
            }
        }

        /*String [] jarIDs = {resultsJar.get(0).getJar_id(),resultsJar.get(1).getJar_id(),
                resultsJar.get(2).getJar_id(),resultsJar.get(3).getJar_id(),
                resultsJar.get(4).getJar_id(),resultsJar.get(5).getJar_id()};*/
        String[] labels = {getString(R.string.statistics_payments_text),
                getString(R.string.statistics_income_text)};
        BarDataSet ds2 = new BarDataSet(entries2, getString(R.string.statistics_bar_title));
        int[] colors = {R.color.colorPrimaryDark, R.color.colorAccent};
        ds2.setColors(ColorTemplate.createColors(getResources(), colors));
        ds2.setValueTextColor(Color.BLACK);
        ds2.setValueTextSize(10f);
        ds2.setStackLabels(jarIDs);
        ds2.calcMinMax();
        ds2.setStackLabels(labels);
        ds2.setDrawValues(true);

        /*ds1.setValueLinePart1OffsetPercentage(80.f);
        ds1.setValueLinePart1Length(0.5f);
        ds1.setValueLinePart2Length(0.1f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/
        BarData d = new BarData(ds2);
        d.setValueTypeface(tf);
        return d;
    }

    protected BarData generateBarData() {
        //int dataSets, float range, int count      1, 20000, 12

        // get realm instance
        Realm realm = realmManager.getRealm();
        // load your data from Realm.io database

        int dataSets = 6; // bottles
        int count = 2; //income & payments

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for (int i = 0; i < dataSets; i++) {
            ArrayList<BarEntry> entries2 = new ArrayList<>();
            RealmResults<Jar> resultsJar = realm.where(Jar.class).findAll();
            Jar jar = resultsJar.get(i);
            String jarID = jar.getJar_id();

            //TODO ASYNC
            float sumOfPaymentsInJar = realm.where(Cashflow.class)
                    .equalTo("jar.jar_id", jarID)
                    .lessThan("sum", 0f)
                    .findAll()
                    .sum("sum").floatValue();
            float sumOfIncome = jar.getTotalCash() - sumOfPaymentsInJar;
            /*if (-sumOfPaymentsInJar > 1f || sumOfIncome > 1f) { */     //to not add jars with no cashflow
            if (-sumOfPaymentsInJar < 1) {
                sumOfPaymentsInJar = -sumOfPaymentsInJar;
                float[] bars = {-sumOfPaymentsInJar, sumOfIncome};
            }
                //entries2.add(new BarEntry(jar.getJar_float_id(),bars, jarID));
                entries2.add(new BarEntry(jar.getJar_float_id(), sumOfIncome));
                entries2.add(new BarEntry(jar.getJar_float_id(), -sumOfPaymentsInJar));
            BarDataSet ds2 = new BarDataSet(entries2, getLabel(i));
            int[] colors = {R.color.colorPrimaryDark, R.color.colorAccent};
            ds2.setColors(ColorTemplate.createColors(getResources(), colors));
            ds2.setValueTextColor(Color.BLACK);
            ds2.setValueTextSize(10f);
            ds2.setDrawValues(true);
            sets.add(ds2);
        }
        BarData d = new BarData(sets);
        d.setValueTypeface(tf);
        return d;
    }

    private String getLabel(int i) {
        return jarIDs[i];
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString(getString(R.string.statistics_pie_hole_title));
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

}
