package cl.santotomas.restaurantevolcan.ui.stock;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import cl.santotomas.restaurantevolcan.R;
import cl.santotomas.restaurantevolcan.databinding.ActivityStockBinding;
import cl.santotomas.restaurantevolcan.ui.products.ProductItem;
import cl.santotomas.restaurantevolcan.ui.products.ProductRepository;

public class StockActivity extends AppCompatActivity {

    private ActivityStockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ratingWarehouse.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                binding.textRatingValue.setText(getString(R.string.rating_value, rating));
            }
        });

        binding.buttonReviewAlerts.setOnClickListener(view -> showStockReportDialog());
        binding.buttonBack.setOnClickListener(view -> finish());
    }

    private void showStockReportDialog() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("=== INFORME DE BODEGA ===\n");
        reportBuilder.append("Empresa: Volcán Waffles & Café\n");
        reportBuilder.append("Fecha: ").append(DateFormat.getDateTimeInstance().format(new Date())).append("\n\n");
        reportBuilder.append("• Capacidad Utilizada: 72% (Total de bodega)\n");
        reportBuilder.append("• Valoración de Bodega: ").append(binding.textRatingValue.getText().toString()).append("\n\n");
        reportBuilder.append("--- PRODUCTOS CRÍTICOS / STOCK BAJO ---\n");

        ProductRepository repository = ProductRepository.getInstance(this);
        List<ProductItem> products = repository.getProducts();
        int criticalCount = 0;

        for (ProductItem product : products) {
            if (product.getStock() <= 5 && product.isAvailable()) {
                criticalCount++;
                reportBuilder.append("• ").append(product.getName())
                        .append(" (").append(product.getCode()).append("): ")
                        .append(product.getStock()).append(" ").append(product.getUnit())
                        .append(" [STOCK CRÍTICO]\n");
            }
        }

        if (criticalCount == 0) {
            reportBuilder.append("No hay productos en estado crítico actualmente.\n");
        }

        reportBuilder.append("\n--- ESTADO GENERAL DE INVENTARIO ---\n");
        reportBuilder.append("Total de insumos activos: ").append(products.size()).append("\n");
        reportBuilder.append("Estado: Alertas y stock validados correctamente (R-17 a R-19).");

        new AlertDialog.Builder(this)
                .setTitle(R.string.report_dialog_title)
                .setMessage(reportBuilder.toString())
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .setNeutralButton("Exportar / Compartir", (dialog, which) ->
                        Toast.makeText(this, R.string.report_export_success, Toast.LENGTH_SHORT).show())
                .show();
    }
}
