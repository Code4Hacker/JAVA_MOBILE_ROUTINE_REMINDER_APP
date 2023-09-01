package com.geminichild.medicinereminder.dashboardfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.databinding.FragmentAlarmsBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class AlarmsFragment extends Fragment {
    Button addAlarm;
    MaterialTimePicker materialTimePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                addAlarm = (Button) view.findViewById(R.id.setAlarm);

                addAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialTimePicker = new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setTitleText("Set Your New Alarm")
                                .setHour(12)
                                .setMinute(0)
                                .build();
                        materialTimePicker.show(getActivity().getSupportFragmentManager(), "geminichild");
                    }
                });


    }
    private void getAlarmsNotify(){
    }
}
