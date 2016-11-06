package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    public DatePickerFragment() {
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        /*final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);*/

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        Calendar now = Calendar.getInstance();
        now.set(year, month, day);
        //min date is now - 2 months
        now.add(Calendar.MONTH, -2);
        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
        //max date is now + 1 year
        now.add(Calendar.MONTH, +2);
        now.add(Calendar.YEAR, +1);
        datePickerDialog.getDatePicker().setMaxDate(now.getTimeInMillis());
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Toast.makeText(getContext(), "Year: " + view.getYear() + " Month: " + view.getMonth()
        // + " Day: " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        OnNewDateListener listener = (OnNewDateListener) getTargetFragment();
        listener.onNewDate(year, month, day);

        //TODO check if needed
        dismiss();
    }

    public interface OnNewDateListener {
        void onNewDate(int year, int month, int day);
    }
}
