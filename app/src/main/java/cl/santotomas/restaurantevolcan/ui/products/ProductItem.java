package cl.santotomas.restaurantevolcan.ui.products;

public class ProductItem {
    private String code;
    private String name;
    private String description;
    private String category;
    private String unit;
    private int stock;
    private boolean available;

    public ProductItem(String code, String name, String description, String category, String unit, int stock, boolean available) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.stock = stock;
        this.available = available;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
