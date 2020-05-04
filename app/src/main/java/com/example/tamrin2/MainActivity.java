package com.example.tamrin2;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tamrin2.alarmFeature.AlarmFragment;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        loadFragment(new AlarmFragment(), R.id.first_fragment);
//        loadFragment(new SecondFragment(), R.id.second_fragment);
//        loadFragment(new ThirdFragment(), R.id.third_fragment);



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void loadFragment(Fragment fragment, int layout) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(layout, fragment);
        ft.commit();

    }
}
