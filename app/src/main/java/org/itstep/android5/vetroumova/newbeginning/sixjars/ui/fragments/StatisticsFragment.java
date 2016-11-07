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
import android.widget.TextView;

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

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

//import com.google.android.gms.plus.PlusOneButton;

public class StatisticsFragment extends SimpleChartFragment implements RangeSeekBar.OnRangeSeekBarChangeListener {
    BarChart barChart;
    PieChart pieChart;
    RangeSeekBar<Integer> rangeSeekBar;
    TextView periodTextView;
    private String[] jarIDs = {"NEC", "PLAY", "GIVE", "EDU", "LTSS", "FFA"};
    private Date[] dates;
    private Typeface tf;
    private RealmManager realmManager;
    private RealmResults<Cashflow> results;
    private RealmResults<Cashflow> globalResults;

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
        setGlobalRealmData();   // globalResults
        dates = new Date[2];
        Calendar calendar = Calendar.getInstance();
        Date now = new Date(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        dates[0] = new Date(calendar.getTimeInMillis());
        dates[1] = now;
        //primary dates
        setRealmDataForPeriod();    // updating results with dates[]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.statisticsSeekBar);
        rangeSeekBar.setRangeValues(0, 100);
        rangeSeekBar.setOnRangeSeekBarChangeListener(this);
        periodTextView = (TextView) view.findViewById(R.id.statisticsPeriodText);
        setPeriod(dates);
        //todo normal scaling
        rangeSeekBar.setSelectedMinValue(90);
        rangeSeekBar.setSelectedMaxValue(100);
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
        //barChart.setMaxVisibleValueCount(6);

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

    private void setPeriod(Date[] dates) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMMM-yyyy", Locale.getDefault());
        String startDate = dateFormat.format(dates[0]);
        String endDate = dateFormat.format(dates[1]);
        periodTextView.setText(startDate + "   -   " + endDate);
    }

    protected PieData generatePieData() {
        // get realm instance
        //Realm realm = realmManager.getRealm();
        // load your data from Realm.io database

        int count = 6;
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        //RealmResults<Jar> resultsJar = realm.where(Jar.class).findAll();

        for (int i = 0; i < count; i++) {
            //entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i+1)));
            //entries1.add(new PieEntry(results.get(i).getTotalCash(),results.get(i).getJar_id()));
            //String jarID = resultsJar.get(i).getJar_id();
            String jarID = jarIDs[i];
            float sumOfPaymentsInJar = results.where()
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
    protected BarData generateBarData() {
        // get realm instance
        Realm realm = realmManager.getRealm();
        // load your data from Realm.io database

        int dataSets = 6; // bottles
        int count = 2; //income & payments

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for (int i = 0; i < dataSets; i++) {
            ArrayList<BarEntry> entries2 = new ArrayList<>();
            //RealmResults<Jar> resultsJar = realm.where(Jar.class).findAll();
            //Jar jar = resultsJar.get(i);
            //String jarID = jar.getJar_id();
            String jarID = jarIDs[i];

            //TODO ASYNC
            float sumOfPaymentsInJar = results.where()
                    .equalTo("jar.jar_id", jarID)
                    .lessThan("sum", 0f)
                    .findAll()
                    .sum("sum").floatValue();
            //added
            float sumOfIncome = results.where()
                    .equalTo("jar.jar_id", jarID)
                    .greaterThan("sum", 0f)
                    .findAll()
                    .sum("sum").floatValue();
            //float sumOfIncome = jar.getTotalCash() - sumOfPaymentsInJar;

            if (-sumOfPaymentsInJar < 1) {
                sumOfPaymentsInJar = -sumOfPaymentsInJar;
                float[] bars = {-sumOfPaymentsInJar, sumOfIncome};
            }
                //entries2.add(new BarEntry(jar.getJar_float_id(),bars, jarID));
            float jarFloatID = realm.where(Jar.class)
                    .equalTo("jar_id", jarID)
                    .findFirst()
                    .getJar_float_id();
            entries2.add(new BarEntry(jarFloatID, sumOfIncome));
            entries2.add(new BarEntry(jarFloatID, -sumOfPaymentsInJar));
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

    private void setGlobalRealmData() {
        // get realm instance
        Realm realm = realmManager.getRealm();
        globalResults = realm.where(Cashflow.class)
                .findAllSorted("date", Sort.DESCENDING);
    }

    private void setRealmDataForPeriod() {
        results = globalResults.where()
                .between("date", dates[0], dates[1])
                .findAllSorted("date", Sort.DESCENDING);
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        //update dates[]
        getPeriodBySeekValue(minValue, maxValue);
        setPeriod(dates);
        setRealmDataForPeriod();
        barChart.setData(generateBarData());
        pieChart.setData(generatePieData());
        //setData(mSeekBarX.getProgress() + 1 , mSeekBarY.getProgress());
        barChart.invalidate();
        pieChart.invalidate();
    }

    private void getPeriodBySeekValue(Object minValue, Object maxValue) {
        int lengthOfSeek = rangeSeekBar.getAbsoluteMaxValue() - rangeSeekBar.getAbsoluteMinValue();
        Date max = globalResults.maxDate("date");
        Date min = globalResults.minDate("date");
        long millisUnit = (max.getTime() - min.getTime()) / lengthOfSeek;
        dates[0] = new Date(min.getTime() + ((int) minValue * millisUnit));
        dates[1] = new Date(min.getTime() + ((int) maxValue * millisUnit));
    }
}