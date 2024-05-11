package com.imager.edit_it.ui.home.home_navigation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.imager.edit_it.R;
import com.imager.edit_it.databinding.SettingsBinding;

public class Home_settings extends Fragment {
    private SettingsBinding binding;

    private Switch night_light_switch;

    private TextView night_light_mode_txt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = SettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        night_light_switch = root.findViewById(R.id.night_light_mode);
        night_light_mode_txt = root.findViewById(R.id.night_light_mode_txt);

        night_light_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (night_light_switch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                // Обновить фон фрагмента
                updateBackground(root);
            }
        });


        updateBackground(root);

        return root;
    }

    // Метод для обновления фона фрагмента в соответствии с текущей темой
    private void updateBackground(View root) {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int backgroundColor;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Темный режим
            backgroundColor = getResources().getColor(R.color.Yellow);
        } else {
            // Светлый режим
            backgroundColor = getResources().getColor(R.color.white);
        }
        root.setBackgroundColor(backgroundColor);
    }
}
