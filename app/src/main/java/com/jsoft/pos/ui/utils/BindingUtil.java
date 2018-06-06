package com.jsoft.pos.ui.utils;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jsoft.pos.ui.custom.CustomEditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BindingUtil {

    @BindingAdapter({"cardBackgroundColor"})
    public static void cardBackgroundColor(CardView cardView, String colorValue) {
        cardView.setCardBackgroundColor(Color.parseColor(colorValue));
    }

    @BindingAdapter({"android:background"})
    public static void setBackground(View view, String value) {
        int color = value != null ? Color.parseColor(value) : Color.parseColor("#9E9E9E");
        view.setBackground(new ColorDrawable(color));
    }

    @BindingAdapter({"android:checked"})
    public static void setChecked(RadioButton btn, String color) {
        btn.setChecked(btn.getContentDescription().toString().equalsIgnoreCase(color));
    }

    @BindingAdapter({"android:text"})
    public static void setInt(TextView textView, int value) {
        if (value > 0) {
            textView.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getInt(EditText editText) {
        String value = editText.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(value);
    }

    @BindingAdapter({"android:text"})
    public static void setDouble(TextView textView, double value) {
        //if (value > 0) {

            if ((value - (int) value) % 10 == 0) {
                textView.setText(String.valueOf((int) value));
            } else {
                DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
                df.applyPattern("#.##");
                df.setRoundingMode(RoundingMode.HALF_UP);
                df.setMinimumFractionDigits(2);
                textView.setText(df.format(value));
            }

        //}
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getDouble(EditText editText) {
        String value = editText.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }

        return Double.parseDouble(value);
    }

    @BindingAdapter({"checkResult", "errorMessage"})
    public static void errorCheck(TextInputLayout layout, boolean valid, String message) {
        if (!valid) {
            //layout.setError(message);
            //layout.setHintTextAppearance(R.style.ErrorText);
        }
    }

    @BindingAdapter({"checkResult"})
    public static void errorCheck(TextInputEditText editText, boolean valid) {
        if (!valid) {
            editText.setError("Error");
            //editText.setHintTextColor(Color.parseColor("#D13638"));
        }
    }

    @BindingAdapter({"checkResult", "errorMessage"})
    public static void errorCheck(CustomEditText editText, boolean valid, String message) {
        if (!valid) {
            editText.setError(message);
            editText.setHasError(true);
            //editText.setHintTextColor(Color.parseColor("#D13638"));
        }
    }

}
