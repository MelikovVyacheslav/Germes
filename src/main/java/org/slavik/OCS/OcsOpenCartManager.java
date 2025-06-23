package org.slavik.OCS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slavik.ConnectionManager;
import org.slavik.entity.product.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OcsOpenCartManager {
    private final ConnectionManager connection;

    public OcsOpenCartManager(ConnectionManager connection) {
        this.connection = connection;
    }

    public void saveProductsFromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(json);

        if (!root.isArray()) {
            throw new IllegalArgumentException("Ожидается JSON-массив.");
        }

        for (JsonNode node : root) {
          Product product = parseProduct(node);
            saveProductToDB(product);
        }
    }

    private Product parseProduct(JsonNode node) {
        return new Product(
                node.path("product_id").asInt(),
                node.path("model").asText(),
                node.path("sku").asText(),
                node.path("ean").asText(),
                node.path("quantity").asInt(),
                node.path("stock_status_id").asInt(),
                node.path("image").asText(),
                node.path("manufacturer_id").asInt(),
                node.path("price").asInt(),
                node.path("date_available").asText(),
                node.path("weight").asDouble(),
                node.path("weight_class_id").asInt(),
                node.path("length").asDouble(),
                node.path("wight").asDouble(),
                node.path("height").asDouble(),
                node.path("length_class_id").asInt(),
                node.path("subtract").asInt(),
                node.path("status").asInt(),
                node.path("date_added").asText(),
                node.path("date_modify").asText(),
                node.path("dn_id").asInt()
        );
    }

    private void saveProductToDB(Product product) throws SQLException {
        String sql = """
                INSERT INTO oc_product (
                    product_id, model, sku, ean, quantity, stock_status_id, image, manufacturer_id,
                    price, date_available, weight, weight_class_id, length, width, height,
                    length_class_id, subtract, status, date_added, date_modified, dn_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    model = VALUES(model),
                    sku = VALUES(sku),
                    ean = VALUES(ean),
                    quantity = VALUES(quantity),
                    stock_status_id = VALUES(stock_status_id),
                    image = VALUES(image),
                    manufacturer_id = VALUES(manufacturer_id),
                    price = VALUES(price),
                    date_available = VALUES(date_available),
                    weight = VALUES(weight),
                    weight_class_id = VALUES(weight_class_id),
                    length = VALUES(length),
                    width = VALUES(width),
                    height = VALUES(height),
                    length_class_id = VALUES(length_class_id),
                    subtract = VALUES(subtract),
                    status = VALUES(status),
                    date_added = VALUES(date_added),
                    date_modified = VALUES(date_modified),
                    dn_id = VALUES(dn_id)
                """;

        try (
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getModel());
            stmt.setString(3, product.getSku());
            stmt.setString(4, product.getEan());
            stmt.setInt(5, product.getQuantity());
            stmt.setInt(6, product.getStockStatusId());
            stmt.setString(7, product.getImage());
            stmt.setInt(8, product.getManufacturerId());
            stmt.setInt(9, product.getPrice());
            stmt.setDate(10, product.getDateAvailable());
            stmt.setDouble(11, product.getWeight());
            stmt.setInt(12, product.getWeightClassId());
            stmt.setDouble(13, product.getLength());
            stmt.setDouble(14, product.getWidth());
            stmt.setDouble(15, product.getHeight());
            stmt.setInt(16, product.getLengthClassId());
            stmt.setInt(17, product.getSubtract());
            stmt.setInt(18, product.getStatus());
            stmt.setDate(19, product.getDateAdded());
            stmt.setDate(20, product.getDateModified());
            stmt.setInt(21, product.getDnId());

            stmt.executeUpdate();
        }

    }
}
