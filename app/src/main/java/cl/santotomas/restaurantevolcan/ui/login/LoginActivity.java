package cl.santotomas.restaurantevolcan.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import cl.santotomas.restaurantevolcan.R;
import cl.santotomas.restaurantevolcan.databinding.ActivityLoginBinding;
import cl.santotomas.restaurantevolcan.ui.dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureActions();
    }

    private void configureActions() {
        binding.buttonLogin.setOnClickListener(view -> validateAndLogin());

        binding.checkRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.editUsername.setText("admin");
                binding.editPassword.setText("admin123");
            } else {
                binding.editUsername.setText("");
                binding.editPassword.setText("");
            }
        });

        binding.editPassword.setOnEditorActionListener((view, actionId, event) -> {
            validateAndLogin();
            return true;
        });
    }

    private void validateAndLogin() {
        binding.layoutUsername.setError(null);
        binding.layoutPassword.setError(null);

        String username = binding.editUsername.getText() == null
                ? "" : binding.editUsername.getText().toString().trim();
        String password = binding.editPassword.getText() == null
                ? "" : binding.editPassword.getText().toString();

        if (username.isEmpty()) {
            binding.layoutUsername.setError(getString(R.string.error_required_user));
            binding.editUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.layoutPassword.setError(getString(R.string.error_required_password));
            binding.editPassword.requestFocus();
            return;
        }

        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager keyboard = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        setLoading(true);
        handler.postDelayed(() -> {
            setLoading(false);

            if ("admin".equals(username) && "admin123".equals(password)) {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra(DashboardActivity.EXTRA_USERNAME, username);
                intent.putExtra(DashboardActivity.EXTRA_FULL_NAME, "Administrador de Bodega");
                intent.putExtra(DashboardActivity.EXTRA_ROLE_NAME, "Administrador");
                startActivity(intent);
                return;
            }

            Snackbar.make(
                    binding.getRoot(),
                    R.string.error_credentials,
                    Snackbar.LENGTH_LONG
            ).show();
        }, 450);
    }

    private void setLoading(boolean loading) {
        binding.progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.buttonLogin.setEnabled(!loading);
        binding.editUsername.setEnabled(!loading);
        binding.editPassword.setEnabled(!loading);
    }
}