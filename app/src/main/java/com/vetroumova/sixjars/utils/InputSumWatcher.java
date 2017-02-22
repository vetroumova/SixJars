package com.vetroumova.sixjars.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by OLGA on 03.11.2016.
 */

public class InputSumWatcher implements TextWatcher {
    EditText edt;
    int keyDel = 0;

    public InputSumWatcher(EditText edt) {
        this.edt = edt;
    }

    //to edit a TextView
    public static String editSum(String input) {
        int len = input.length();
        if (input.startsWith(".") || input.startsWith(",")) {
            Log.d("VOlga", input);
            input = "0".concat(input);
        } else if (len == 8) {
            input = input.substring(0, input.length() - 1);
        } else if (len > 4) {
            char maybePoint = input.charAt(len - 4);
            Log.d("VOlga", input + " , " + maybePoint);
            if (len != 0 && (maybePoint == ',' || maybePoint == '.')) {
                input = input.substring(0, input.length() - 1);
            }
        }
        return input;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int len = edt.getText().length();
        String input = edt.getText().toString();

        if (input.startsWith(".") || input.startsWith(",")) {
            Log.d("VOlga", input);
            edt.setText("0".concat(edt.getText().toString()));
            Log.d("VOlga", edt.getText().toString());
        }
        if (len > 4) {
            char maybePoint = input.charAt(len - 4);
            Log.d("VOlga", input + " , " + maybePoint);
            if (len != 0 && (maybePoint == ',' || maybePoint == '.')) {
                edt.setText(edt.getText().delete(edt.getText().length() - 1, edt.getText().length()));
                Log.d("VOlga", edt.getText().toString());
            }
        }
        if (len == 8) {
            edt.setText(edt.getText().delete(edt.getText().length() - 1, edt.getText().length()));
        }
        edt.setSelection(edt.getText().length());
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
