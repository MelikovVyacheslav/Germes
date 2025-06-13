package org.slavik.Items.Product;

public class ProductDescription {
    private final int productId;
    private final String name;
    private final String description;

    public ProductDescription(int productId, String name, String description) {
        this.productId = productId;
        this.name = name;
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
