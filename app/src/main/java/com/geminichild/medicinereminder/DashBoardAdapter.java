package com.geminichild.medicinereminder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.geminichild.medicinereminder.dashboardfragments.AlarmsFragment;
import com.geminichild.medicinereminder.dashboardfragments.OthersFragment;
import com.geminichild.medicinereminder.dashboardfragments.ProfileFragment;

public class DashBoardAdapter extends FragmentStateAdapter {
    public DashBoardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new OthersFragment();
           case 1:
               return new AlarmsFragment();
           case 2:
               return new ProfileFragment();
           default:
               return new OthersFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
