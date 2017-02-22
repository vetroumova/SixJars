package com.vetroumova.sixjars.ui.fragments;

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
        OnNewDateListener listener = (OnNewDateListener) getTargetFragment();
        listener.onNewDate(year, month, day);
        dismiss();
    }
    public interface OnNewDateListener {
        void onNewDate(int year, int month, int day);
    }
}
