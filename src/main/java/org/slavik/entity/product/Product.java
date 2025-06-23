package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

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
    private final Date dateAvailable;
    private final Double weight;
    private final int weightClassId;
    private final Double length;
    private final Double width;
    private final Double height;
    private final int lengthClassId;
    private final int subtract;
    private final int status;
    private final Date dateAdded;
    private final Date dateModified;
    private final int dnId;

    public Product(int productId, String model, String sku, String ean, int quantity, int stockStatusId,
                   String image, int manufacturerId, int price, String dateAvailable, Double weight,
                   int weightClassId, Double length, Double wight, Double height, int lengthClassId, int subtract,
                   int status, String dateAdded, String dateModified, int dnId) {
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
        this.dateModified = dateModified;
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

    public Date getDateAvailable() {
        return dateAvailable;
    }

    public double getWeight() {
        if (weight == null) {
            return 0;
        }
        return weight;
    }

    public int getWeightClassId() {
        return weightClassId;
    }

    public double getLength() {
        if (length == null) {
            return 0;
        }
        return length;
    }

    public double getWidth() {
        if (width == null) {
            return 0;
        }
        return width;
    }

    public double getHeight() {
        if (height == null) {
            return 0;
        }
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public int getDnId() {
        return dnId;
    }

    public String convertImageUrlToPath(String originalUrl) {
        String fileName = originalUrl.substring(originalUrl.lastIndexOf('/') + 1);
        return "catalog/b2b/" + fileName;
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
            Date dateAvailable = rs.getDate("date_available");
            Double weight = rs.getDouble("weight");
            int weightClassId = rs.getInt("weigh_clas_id");
            Double length = rs.getDouble("length");
            Double wight = rs.getDouble("wight");
            Double height = rs.getDouble("height");
            int lengthClassId = rs.getInt("length_class_id");
            int subtract = rs.getInt("subtract");
            int status = rs.getInt("status");
            Date dateAdded = rs.getDate("date_added");
            Date dateModify = rs.getDate("date_modify");
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
