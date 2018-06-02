package com.jsoft.pos.ui.utils;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class BindingUtil {

    @BindingAdapter({"cardBackgroundColor"})
    public static void cardBackgroundColor(CardView cardView, String colorValue) {
        cardView.setCardBackgroundColor(Color.parseColor(colorValue));
    }

    @BindingAdapter({"android:background"})
    public static void setBackground(View view, String value) {
        int color = value != null ? Color.parseColor(value) : Color.parseColor("#9E9E9E");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(new ColorDrawable(color));
        } else {
            view.setBackgroundDrawable(new ColorDrawable(color));
        }
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
        if (value > 0) {

            if ((value - (int) value) % 10 == 0) {
                textView.setText(String.valueOf((int) value));
            } else {
                textView.setText(String.valueOf(value));
            }

        }
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
            layout.setError(message);
            //layout.setHintTextAppearance(R.style.ErrorText);
        }
    }

    @BindingAdapter({"checkResult"})
    public static void errorCheck(TextInputEditText editText, boolean valid) {
        if (!valid) {
            editText.setHintTextColor(Color.parseColor("#D13638"));
        }
    }

    @BindingAdapter({"checkResult"})
    public static void errorCheck(EditText editText, boolean valid) {
        if (!valid) {
            editText.setHintTextColor(Color.parseColor("#D13638"));
        }
    }

}
