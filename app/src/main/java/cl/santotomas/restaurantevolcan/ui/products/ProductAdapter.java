package cl.santotomas.restaurantevolcan.ui.products;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.santotomas.restaurantevolcan.databinding.ItemProductBinding;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<ProductItem> items;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductItem item);
    }

    public ProductAdapter(List<ProductItem> items, OnProductClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductItem item, OnProductClickListener listener) {
            binding.textProductName.setText(item.getName() + " (" + item.getCode() + ")");
            binding.textProductCategory.setText(item.getCategory() + " · " + item.getDescription());
            binding.textProductStock.setText("Stock: " + item.getStock() + " " + item.getUnit());
            binding.checkAvailable.setChecked(item.isAvailable());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(item);
                }
            });
        }
    }
}
