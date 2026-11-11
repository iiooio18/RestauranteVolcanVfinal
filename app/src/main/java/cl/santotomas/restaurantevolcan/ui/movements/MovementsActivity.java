package cl.santotomas.restaurantevolcan.ui.movements;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.santotomas.restaurantevolcan.R;
import cl.santotomas.restaurantevolcan.databinding.ActivityMovementsBinding;
import cl.santotomas.restaurantevolcan.ui.dashboard.DashboardActivity;
import cl.santotomas.restaurantevolcan.ui.products.ProductItem;
import cl.santotomas.restaurantevolcan.ui.products.ProductRepository;

public class MovementsActivity extends AppCompatActivity {

    private ActivityMovementsBinding binding;
    private ProductRepository productRepository;
    private String username;
    private final List<ProductItem> availableProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productRepository = ProductRepository.getInstance(this);
        username = getIntent().getStringExtra(DashboardActivity.EXTRA_USERNAME);

        binding.spinnerProduct.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        showCurrentStock(availableProducts.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        binding.textCurrentStock.setText("");
                    }
                });

        loadProducts();
        showRecentMovements();

        binding.buttonRegisterMovement.setOnClickListener(view -> {
            if (binding.radioEntry.isChecked()) {
                registerEntry();
            } else if (binding.radioExit.isChecked()) {
                registerExit();
            }
        });

        binding.buttonBack.setOnClickListener(view -> finish());
    }

    private void loadProducts() {
        List<String> productLabels = new ArrayList<>();
        availableProducts.clear();

        for (ProductItem product : productRepository.getProducts()) {
            if (product.isAvailable()) {
                availableProducts.add(product);
                productLabels.add(product.getCode() + " - " + product.getName());
            }
        }

        ArrayAdapter<String> products = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, productLabels);
        products.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerProduct.setAdapter(products);
    }

    private void registerEntry() {
        int position = binding.spinnerProduct.getSelectedItemPosition();
        if (position < 0 || position >= availableProducts.size()) {
            showMessage("Selecciona un producto disponible.");
            return;
        }

        if (username == null || username.trim().isEmpty()) {
            showMessage("Vuelve a iniciar sesión para registrar la entrada.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(binding.editQuantity.getText().toString().trim());
        } catch (NumberFormatException exception) {
            binding.editQuantity.setError("Ingresa un número entero mayor que cero.");
            return;
        }

        if (quantity <= 0) {
            binding.editQuantity.setError("La cantidad debe ser mayor que cero.");
            return;
        }

        ProductItem product = availableProducts.get(position);
        int newStock;

        try {
            newStock = productRepository.registerEntry(product.getCode(), quantity, username);
        } catch (ArithmeticException exception) {
            binding.editQuantity.setError("La cantidad supera el stock máximo permitido.");
            return;
        } catch (IllegalStateException exception) {
            showMessage("El producto ya no está disponible.");
            loadProducts();
            return;
        } catch (SQLiteException exception) {
            showMessage("No se pudo guardar la entrada.");
            return;
        }

        product.setStock(newStock);
        showCurrentStock(product);
        binding.editQuantity.setError(null);
        binding.editQuantity.setText("");
        showRecentMovements();
        showMessage("Entrada registrada.");
    }

    private void registerExit() {
        int position = binding.spinnerProduct.getSelectedItemPosition();
        if (position < 0 || position >= availableProducts.size()) {
            showMessage("Selecciona un producto disponible.");
            return;
        }

        if (username == null || username.trim().isEmpty()) {
            showMessage("Vuelve a iniciar sesión para registrar la salida.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(binding.editQuantity.getText().toString().trim());
        } catch (NumberFormatException exception) {
            binding.editQuantity.setError("Ingresa un número entero mayor que cero.");
            return;
        }

        if (quantity <= 0) {
            binding.editQuantity.setError("La cantidad debe ser mayor que cero.");
            return;
        }

        ProductItem product = availableProducts.get(position);
        int newStock;

        try {
            newStock = productRepository.registerExit(product.getCode(), quantity, username);
        } catch (IllegalArgumentException exception) {
            binding.editQuantity.setError(exception.getMessage());
            return;
        } catch (IllegalStateException exception) {
            showMessage("El producto ya no está disponible.");
            loadProducts();
            return;
        } catch (SQLiteException exception) {
            showMessage("No se pudo guardar la salida.");
            return;
        }

        product.setStock(newStock);
        showCurrentStock(product);
        binding.editQuantity.setError(null);
        binding.editQuantity.setText("");
        showRecentMovements();
        showMessage("Salida registrada.");
    }

    private void showCurrentStock(ProductItem product) {
        binding.textCurrentStock.setText(getString(
                R.string.entry_current_stock, product.getStock(), product.getUnit()));
    }

    private void showRecentMovements() {
        int previousRows = binding.tableRecentMovements.getChildCount() - 1;
        if (previousRows > 0) {
            binding.tableRecentMovements.removeViews(1, previousRows);
        }

        LayoutInflater inflater = getLayoutInflater();

        for (ProductRepository.StockMovement movement : productRepository.getRecentMovements()) {
            TableRow row = new TableRow(this);
            Date timestamp = new Date(movement.registeredAt);
            String date = DateFormat.getDateFormat(this).format(timestamp);
            String time = DateFormat.getTimeFormat(this).format(timestamp);

            String movementTypeLabel = "ENTRADA".equalsIgnoreCase(movement.type)
                    ? getString(R.string.entry)
                    : getString(R.string.exit);

            addCell(inflater, row, movement.productName);
            addCell(inflater, row, movementTypeLabel);
            addCell(inflater, row, movement.quantity + " " + movement.unit);
            addCell(inflater, row, date + " " + time);
            addCell(inflater, row, movement.username);

            binding.tableRecentMovements.addView(row);
        }
    }

    private void addCell(LayoutInflater inflater, TableRow row, String value) {
        TextView cell = (TextView) inflater.inflate(R.layout.item_movement_cell, row, false);
        cell.setText(value);
        row.addView(cell);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}