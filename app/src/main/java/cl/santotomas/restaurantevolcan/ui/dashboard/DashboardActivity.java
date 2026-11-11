package cl.santotomas.restaurantevolcan.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cl.santotomas.restaurantevolcan.R;
import cl.santotomas.restaurantevolcan.databinding.ActivityDashboardBinding;
import cl.santotomas.restaurantevolcan.ui.login.LoginActivity;
import cl.santotomas.restaurantevolcan.ui.movements.MovementsActivity;
import cl.santotomas.restaurantevolcan.ui.products.ProductsActivity;
import cl.santotomas.restaurantevolcan.ui.stock.StockActivity;

public class DashboardActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_FULL_NAME = "extra_full_name";
    public static final String EXTRA_ROLE_NAME = "extra_role_name";

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        String fullName = getIntent().getStringExtra(EXTRA_FULL_NAME);
        String roleName = getIntent().getStringExtra(EXTRA_ROLE_NAME);

        if (fullName == null) {
            fullName = getString(R.string.demo_full_name);
        }
        if (roleName == null) {
            roleName = getString(R.string.demo_role);
        }

        binding.textWelcome.setText(getString(R.string.welcome_name, fullName));
        binding.textRole.setText(getString(R.string.role_label, roleName));

        binding.cardProducts.setOnClickListener(view ->
                startActivity(new Intent(this, ProductsActivity.class)));

        binding.cardMovements.setOnClickListener(view -> {
            Intent intent = new Intent(this, MovementsActivity.class);
            intent.putExtra(EXTRA_USERNAME, username);
            startActivity(intent);
        });

        binding.cardStock.setOnClickListener(view ->
                startActivity(new Intent(this, StockActivity.class)));

        binding.buttonLogout.setOnClickListener(view -> logout());
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}