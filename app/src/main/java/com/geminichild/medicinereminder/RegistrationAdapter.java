package com.geminichild.medicinereminder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.geminichild.medicinereminder.collectionregister.Login;
import com.geminichild.medicinereminder.collectionregister.Register;

public class RegistrationAdapter extends FragmentStateAdapter {
    public RegistrationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new Login();
           case 1:
               return new Register();
           default:
               return new Login();
       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
