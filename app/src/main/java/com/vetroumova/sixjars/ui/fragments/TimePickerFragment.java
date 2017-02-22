package com.vetroumova.sixjars.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int hour;
    private int minute;

    public TimePickerFragment() {
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        OnNewTimeListener listener = (OnNewTimeListener) getTargetFragment();
        listener.onNewTime(hourOfDay, minute);
        dismiss();
    }

    public interface OnNewTimeListener {
        void onNewTime(int hour, int minute);
    }
}
