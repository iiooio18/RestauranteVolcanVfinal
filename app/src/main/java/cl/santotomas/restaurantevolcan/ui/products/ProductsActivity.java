package cl.santotomas.restaurantevolcan.ui.products;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cl.santotomas.restaurantevolcan.R;
import cl.santotomas.restaurantevolcan.databinding.ActivityProductsBinding;

public class ProductsActivity extends AppCompatActivity {

    private ActivityProductsBinding binding;
    private ProductRepository repository;
    private ProductAdapter adapter;
    private List<ProductItem> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = ProductRepository.getInstance(this);

        ArrayAdapter<CharSequence> categories = ArrayAdapter.createFromResource(
                this, R.array.product_categories, android.R.layout.simple_spinner_item);
        categories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categories);

        loadProducts();

        binding.buttonApplyFilter.setOnClickListener(view -> filterProducts());
        binding.buttonAddProduct.setOnClickListener(view -> showAddProductDialog());
        binding.buttonBack.setOnClickListener(view -> finish());
    }

    private void loadProducts() {
        displayList = new ArrayList<>(repository.getProducts());

        adapter = new ProductAdapter(displayList, this::showProductOptionsDialog);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerProducts.setAdapter(adapter);
    }

    private void filterProducts() {
        String selectedCategory = binding.spinnerCategory.getSelectedItem().toString();
        List<ProductItem> allProducts = repository.getProducts();
        displayList.clear();

        if (selectedCategory.equalsIgnoreCase("Todas") || selectedCategory.isEmpty()) {
            displayList.addAll(allProducts);
        } else {
            for (ProductItem item : allProducts) {
                if (item.getCategory().equalsIgnoreCase(selectedCategory)) {
                    displayList.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(
                this,
                getString(R.string.filter_applied, selectedCategory),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showAddProductDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_product_form, null);

        EditText editCode = dialogView.findViewById(R.id.editCode);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);
        Spinner spinnerCat = dialogView.findViewById(R.id.spinnerDialogCategory);
        EditText editUnit = dialogView.findViewById(R.id.editUnit);
        EditText editStock = dialogView.findViewById(R.id.editStock);

        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this, R.array.product_categories, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(catAdapter);

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_title)
                .setView(dialogView)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String code = editCode.getText().toString().trim();
                    String name = editName.getText().toString().trim();
                    String description = editDescription.getText().toString().trim();
                    String category = spinnerCat.getSelectedItem().toString();
                    String unit = editUnit.getText().toString().trim();
                    String stockText = editStock.getText().toString().trim();

                    if (code.isEmpty() || name.isEmpty() || unit.isEmpty() || stockText.isEmpty()) {
                        Toast.makeText(
                                this, "Completa los campos obligatorios", Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    int stock = Integer.parseInt(stockText);
                    ProductItem newItem = new ProductItem(
                            code, name, description, category, unit, stock, true);

                    if (!repository.addProduct(newItem)) {
                        Toast.makeText(
                                this, "Ya existe un producto con ese código.", Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    loadProducts();
                    Toast.makeText(
                            this, R.string.product_registered_success, Toast.LENGTH_SHORT
                    ).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showProductOptionsDialog(ProductItem item) {
        String[] options = {
                getString(R.string.action_edit),
                getString(R.string.action_stock),
                item.isAvailable()
                        ? getString(R.string.action_disable)
                        : getString(R.string.action_enable)
        };

        new AlertDialog.Builder(this)
                .setTitle(item.getName() + " (" + item.getCode() + ")")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showEditProductDialog(item);
                    } else if (which == 1) {
                        showUpdateStockDialog(item);
                    } else if (which == 2) {
                        boolean newStatus = !item.isAvailable();
                        repository.setAvailable(item.getCode(), newStatus);
                        loadProducts();

                        int message = newStatus
                                ? R.string.product_enabled_success
                                : R.string.product_disabled_success;
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showEditProductDialog(ProductItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_product_form, null);

        EditText editCode = dialogView.findViewById(R.id.editCode);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);
        Spinner spinnerCat = dialogView.findViewById(R.id.spinnerDialogCategory);
        EditText editUnit = dialogView.findViewById(R.id.editUnit);
        EditText editStock = dialogView.findViewById(R.id.editStock);

        editCode.setText(item.getCode());
        editCode.setEnabled(false);
        editName.setText(item.getName());
        editDescription.setText(item.getDescription());
        editUnit.setText(item.getUnit());
        editStock.setText(String.valueOf(item.getStock()));

        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                this, R.array.product_categories, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(catAdapter);

        for (int i = 0; i < catAdapter.getCount(); i++) {
            if (catAdapter.getItem(i).toString().equalsIgnoreCase(item.getCategory())) {
                spinnerCat.setSelection(i);
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_edit_title)
                .setView(dialogView)
                .setPositiveButton(R.string.update, (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String description = editDescription.getText().toString().trim();
                    String category = spinnerCat.getSelectedItem().toString();
                    String unit = editUnit.getText().toString().trim();
                    String stockText = editStock.getText().toString().trim();

                    if (name.isEmpty() || unit.isEmpty() || stockText.isEmpty()) {
                        Toast.makeText(
                                this, "Completa los campos obligatorios", Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    int stock = Integer.parseInt(stockText);
                    repository.updateProduct(
                            item.getCode(), name, description, category,
                            unit, stock, item.isAvailable());

                    loadProducts();
                    Toast.makeText(
                            this, R.string.product_updated_success, Toast.LENGTH_SHORT
                    ).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showUpdateStockDialog(ProductItem item) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(item.getStock()));
        input.setHint(R.string.stock_hint);

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_stock_title)
                .setMessage("Producto: " + item.getName())
                .setView(input)
                .setPositiveButton(R.string.update, (dialog, which) -> {
                    String stockText = input.getText().toString().trim();

                    if (!stockText.isEmpty()) {
                        int newStock = Integer.parseInt(stockText);
                        repository.updateStock(item.getCode(), newStock);
                        loadProducts();
                        Toast.makeText(
                                this, R.string.stock_updated_success, Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}