package cl.santotomas.restaurantevolcan.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cl.santotomas.restaurantevolcan.databinding.ActivityWelcomeBinding;
import cl.santotomas.restaurantevolcan.ui.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStart.setOnClickListener(view ->
                startActivity(new Intent(this, LoginActivity.class)));
    }
}
