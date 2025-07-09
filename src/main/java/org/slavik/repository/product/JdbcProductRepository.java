package org.slavik.repository.product;

import org.slavik.entity.product.Product;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Arrays;
import java.util.List;

public class JdbcProductRepository implements ProductRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    private final String UPC_VALUE = "";
    private final String JAN_VALUE = "";
    private final String ISBN_VALUE = "";
    private final String MPN_VALUE = "";
    private final String LOCATION_VALUE = "";
    private final String VIDEO_VALUE = "";
    private final int COST_VALUE = 0;
    private final int POINTS_VALUE = 0;
    private final int TAX_CLASS_ID_VALUE = 0;
    private final String SUPPLIER_VALUE = "";

    @Override
    public Product create(Product product) {
        String sql = """
                INSERT INTO oc_product (
                    model, sku, upc, ean, jan, isbn, mpn, location, quantity, stock_status_id, image, video,
                    manufacturer_id, price, cost, points, tax_class_id,
                    date_available, weight, weight_class_id, length, width, height,
                    length_class_id, subtract, status, date_added, date_modified, dn_id, supplier
                ) VALUES (
                    :model, :sku, :upc, :ean, :jan, :isbn, :mpn, :location, :quantity, :stockStatusId, :image,
                    :video, :manufacturerId, :price, :cost, :points, :tax_class_id,
                    :dateAvailable, :weight, :weightClassId, :length, :width,
                    :height, :lengthClassId, :subtract, :status, :dateAdded, :dateModified, :dnId, :supplier
                );
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("model", product.getModel());
        params.addValue("sku", product.getSku());
        params.addValue("upc", UPC_VALUE);
        params.addValue("ean", product.getEan());
        params.addValue("jan", JAN_VALUE);
        params.addValue("isbn", ISBN_VALUE);
        params.addValue("mpn", MPN_VALUE);
        params.addValue("location", LOCATION_VALUE);
        params.addValue("quantity", product.getQuantity());
        params.addValue("stockStatusId", product.getStockStatusId());
        params.addValue("image", product.getImage());
        params.addValue("video", VIDEO_VALUE);
        params.addValue("manufacturerId", product.getManufacturerId());
        params.addValue("price", product.getPrice());
        params.addValue("cost", COST_VALUE);
        params.addValue("points", POINTS_VALUE);
        params.addValue("tax_class_id", TAX_CLASS_ID_VALUE);
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
        params.addValue("dateModified", product.getDateModified());
        params.addValue("dnId", product.getDnId());
        params.addValue("supplier", SUPPLIER_VALUE);
        jdbcOperations.update(sql, params);
        Product newProduct = findBySku(product.getSku());
        return newProduct;
    }

    private Product findBySku(String sku) {
        String sql = """
                select * from oc_product
                where sku = :sku;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("sku", sku);
        Product product = jdbcOperations.queryForObject(sql, params, new Product.Mapper());
        return product;
    }

    public List<Product> findByEAN(String ean) {
        String sql = """
                select * from oc_product
                where ean = :ean;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ean", ean);
        List<Product> productList = jdbcOperations.query(sql, params, new Product.Mapper());
        return productList;
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
                SELECT * FROM oc_product
                """;
        List<Product> products = jdbcOperations.query(sql, new Product.Mapper());
        return products;
    }

    @Override
    public List<Product> findAll(int[] productsIds) {
        String ids = Arrays.toString(productsIds);
        String sql = """
                SELECT * FROM oc_product
                WHERE category_id in (:ids);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
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
                    weight = :weight,
                    weight_class_id = :weightClassId,
                    length = :length,
                    width = :width,
                    height = :height,
                    length_class_id = :lengthClassId,
                    subtract = :subtract,
                    status = :status,
                    date_modified = :dateModify,
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
        params.addValue("weight", product.getWeight());
        params.addValue("weightClassId", product.getWeightClassId());
        params.addValue("length", product.getLength());
        params.addValue("width", product.getWidth());
        params.addValue("height", product.getHeight());
        params.addValue("lengthClassId", product.getLengthClassId());
        params.addValue("subtract", product.getSubtract());
        params.addValue("status", product.getStatus());
        params.addValue("dateModify", product.getDateModified());
        params.addValue("dnId", product.getDnId());

        jdbcOperations.update(sql, params);

        return product;
    }
}