package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private final int productId;
    private final String model;
    private final String sku;
    private final String ean;
    private final int quantity;
    private final int stockStatusId;
    private final String image;
    private final int manufacturerId;
    private final int price;
    private final String dateAvailable;
    private final int weight;
    private final int weightClassId;
    private final int length;
    private final int width;
    private final int height;
    private final int lengthClassId;
    private final int subtract;
    private final int status;
    private final String dateAdded;
    private final String dateModify;
    private final int dnId;

    public Product(int productId, String model, String sku, String ean, int quantity, int stockStatusId,
                   String image, int manufacturerId, int price, String dateAvailable, int weight,
                   int weightClassId, int length, int wight, int height, int lengthClassId, int subtract,
                   int status, String dateAdded, String dateModify, int dnId) {
        this.productId = productId;
        this.model = model;
        this.sku = sku;
        this.ean = ean;
        this.quantity = quantity;
        this.stockStatusId = stockStatusId;
        this.image = image;
        this.manufacturerId = manufacturerId;
        this.price = price;
        this.dateAvailable = dateAvailable;
        this.weight = weight;
        this.weightClassId = weightClassId;
        this.length = length;
        this.width = wight;
        this.height = height;
        this.lengthClassId = lengthClassId;
        this.subtract = subtract;
        this.status = status;
        this.dateAdded = dateAdded;
        this.dateModify = dateModify;
        this.dnId = dnId;
    }

    public int getProductId() {
        return productId;
    }

    public String getModel() {
        return model;
    }

    public String getSku() {
        return sku;
    }

    public String getEan() {
        return ean;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStockStatusId() {
        return stockStatusId;
    }

    public String getImage() {
        return image;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public int getPrice() {
        return price;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }

    public int getWeight() {
        return weight;
    }

    public int getWeightClassId() {
        return weightClassId;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLengthClassId() {
        return lengthClassId;
    }

    public int getSubtract() {
        return subtract;
    }

    public int getStatus() {
        return status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDateModify() {
        return dateModify;
    }

    public int getDnId() {
        return dnId;
    }

    public static class Mapper implements RowMapper<Product> {

        @Override
        public @Nullable Product mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            String model = rs.getString("model");
            String sku = rs.getString("sku");
            String ean = rs.getString("ean");
            int quantity = rs.getInt("quantity");
            int stockStatusId = rs.getInt("stockStatusId");
            String image = rs.getString("image");
            int manufacturerId = rs.getInt("manufacturer_id");
            int price = rs.getInt("price");
            String dateAvailable = rs.getString("date_available");
            int weight = rs.getInt("weight");
            int weightClassId = rs.getInt("weigh_clas_id");
            int length = rs.getInt("length");
            int wight = rs.getInt("wight");
            int height = rs.getInt("height");
            int lengthClassId = rs.getInt("length_class_id");
            int subtract = rs.getInt("subtract");
            int status = rs.getInt("status");
            String dateAdded = rs.getString("date_added");
            String dateModify = rs.getString("date_modify");
            int dnId = rs.getInt("dn_id");
            return new Product(
                productId,
                    model,
                    sku,
                    ean,
                    quantity,
                    stockStatusId,
                    image,
                    manufacturerId,
                    price,
                    dateAvailable,
                    weight,
                    weightClassId,
                    length,
                    wight,
                    height,
                    lengthClassId,
                    subtract,
                    status,
                    dateAdded,
                    dateModify,
                    dnId
            );
        }
    }
}
