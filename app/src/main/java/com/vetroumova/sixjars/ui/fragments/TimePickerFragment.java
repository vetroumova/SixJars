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
        // Use the current time as the default values for the picker
        /*final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);*/

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //Toast.makeText(getContext(), "Hour: " + view.getCurrentHour() + " Minute: " + view.getCurrentMinute(),
        //Toast.LENGTH_SHORT).show();

        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        OnNewTimeListener listener = (OnNewTimeListener) getTargetFragment();
        listener.onNewTime(hourOfDay, minute);
        //TODO check if needed
        dismiss();
    }

    public interface OnNewTimeListener {
        void onNewTime(int hour, int minute);
    }
}
