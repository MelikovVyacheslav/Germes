package org.slavik.repository;

import org.slavik.entity.category.Category;
import org.slavik.entity.product.Product;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbsProductRepository implements ProductRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbsProductRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Product create(Product product) {
        String sql = """
                INSERT INTO oc_product (
                    product_id, model, sku, ean, quantity, stock_status_id, image, manufacturer_id,
                    price, date_available, weight, weight_class_id, length, width, height,
                    length_class_id, subtract, status, date_added, date_modify, dn_id
                ) VALUES (
                    :productId, :model, :sku, :ean, :quantity, :stockStatusId, :image, :manufacturerId,
                    :price, :dateAvailable, :weight, :weightClassId, :length, :width, :height,
                    :lengthClassId, :subtract, :status, :dateAdded, :dateModify, :dnId
                );
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("model", product.getModel());
        params.addValue("sku", product.getSku());
        params.addValue("ean", product.getEan());
        params.addValue("quantity", product.getQuantity());
        params.addValue("stockStatusId", product.getStockStatusId());
        params.addValue("image", product.getImage());
        params.addValue("manufacturerId", product.getManufacturerId());
        params.addValue("price", product.getPrice());
        params.addValue("dateAvailable", product.getDateAvailable());
        params.addValue("weight", product.getWeight());
        params.addValue("weightClassId", product.getWeightClassId());
        params.addValue("length", product.getLength());
        params.addValue("width", product.getWidth());
        params.addValue("height", product.getHeight());
        params.addValue("lengthClassId", product.getLengthClassId());
        params.addValue("subtract", product.getSubtract());
        params.addValue("status", product.getStatus());
        params.addValue("dateAdded", product.getDateAdded());
        params.addValue("dateModify", product.getDateModify());
        params.addValue("dnId", product.getDnId());
Product createdProduct = jdbcOperations.queryForObject(sql, params, new Product.Mapper());

        return createdProduct;
    }

    @Override
    public Product find(int id) {
        String sql = """
                SELECT * FROM oc_product
                WHERE product_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Product product = jdbcOperations.queryForObject(sql, params, new Product.Mapper());
        return product;
    }

    @Override
    public List<Product> findAll() {
        String sql = """
                Select * FROM oc_product
                """;
        List<Product> products = jdbcOperations.query(sql, new Product.Mapper());
        return products;
    }

    @Override
    public Product update(Product product) {
        String sql = """
            UPDATE oc_product SET
                model = :model,
                sku = :sku,
                ean = :ean,
                quantity = :quantity,
                stock_status_id = :stockStatusId,
                image = :image,
                manufacturer_id = :manufacturerId,
                price = :price,
                date_available = :dateAvailable,
                weight = :weight,
                weight_class_id = :weightClassId,
                length = :length,
                width = :width,
                height = :height,
                length_class_id = :lengthClassId,
                subtract = :subtract,
                status = :status,
                date_added = :dateAdded,
                date_modify = :dateModify,
                dn_id = :dnId
            WHERE product_id = :productId;
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("model", product.getModel());
        params.addValue("sku", product.getSku());
        params.addValue("ean", product.getEan());
        params.addValue("quantity", product.getQuantity());
        params.addValue("stockStatusId", product.getStockStatusId());
        params.addValue("image", product.getImage());
        params.addValue("manufacturerId", product.getManufacturerId());
        params.addValue("price", product.getPrice());
        params.addValue("dateAvailable", product.getDateAvailable());
        params.addValue("weight", product.getWeight());
        params.addValue("weightClassId", product.getWeightClassId());
        params.addValue("length", product.getLength());
        params.addValue("width", product.getWidth());
        params.addValue("height", product.getHeight());
        params.addValue("lengthClassId", product.getLengthClassId());
        params.addValue("subtract", product.getSubtract());
        params.addValue("status", product.getStatus());
        params.addValue("dateAdded", product.getDateAdded());
        params.addValue("dateModify", product.getDateModify());
        params.addValue("dnId", product.getDnId());

        Product productUpdate = jdbcOperations.queryForObject(sql, params, new Product.Mapper());

        return productUpdate;
    }

}