package org.slavik.Items;

public class Product {
    private final int product_id;
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
    private final int wight;
    private final int height;
    private final int lengthClassId;
    private final int subtract;
    private final int status;
    private final String dateAdded;
    private final String dateModify;
    private final int dnId;

    public Product(int product_id, String model, String sku, String ean, int quantity, int stockStatusId,
                   String image, int manufacturerId, int price, String dateAvailable, int weight,
                   int weightClassId, int length, int wight, int height, int lengthClassId, int subtract,
                   int status, String dateAdded, String dateModify, int dnId) {
        this.product_id = product_id;
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
        this.wight = wight;
        this.height = height;
        this.lengthClassId = lengthClassId;
        this.subtract = subtract;
        this.status = status;
        this.dateAdded = dateAdded;
        this.dateModify = dateModify;
        this.dnId = dnId;
    }

    public int getProduct_id() {
        return product_id;
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

    public int getWight() {
        return wight;
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
}
