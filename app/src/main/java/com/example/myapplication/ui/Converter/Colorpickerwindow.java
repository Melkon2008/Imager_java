package com.example.myapplication.ui.Converter;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class Colorpickerwindow extends DialogFragment {

    public interface OnColorPickedListener {
        void onColorPicked(int color);
    }

    private OnColorPickedListener onColorPickedListener;

    public void setOnColorPickedListener(OnColorPickedListener listener) {
        this.onColorPickedListener = listener;
    }

    public int colorpic;

    public boolean true_or_false = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new ColorPickerDialog.Builder(requireContext())
                .setTitle("ColorPicker Dialog")
                .setPositiveButton("Confirm", new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        setLayoutColor(envelope.getColor());
                        if (onColorPickedListener != null) {
                            onColorPickedListener.onColorPicked(envelope.getColor());
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .create();
    }

    public void setLayoutColor(int color) {
        colorpic = color;
    }
}
