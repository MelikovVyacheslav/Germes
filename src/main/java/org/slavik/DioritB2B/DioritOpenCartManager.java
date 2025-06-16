package org.slavik.DioritB2B;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.Nullable;
import org.slavik.entity.product.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DioritOpenCartManager {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final DioritAPIClient dioritAPIClient;
    Connection con;

    public DioritOpenCartManager(NamedParameterJdbcOperations jdbcOperations, Connection connection, DioritAPIClient dioritAPIClient) {
        this.jdbcOperations = jdbcOperations;
        this.con = connection;
        this.dioritAPIClient = dioritAPIClient;
        List<Product> products = jdbcOperations.query("", new RowMapper<>() {
            @Override
            public @Nullable Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Product(
                        rs.getInt("product_id"),


                );
            }
        });
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void addAllNewProducts() throws JsonProcessingException, SQLException {
        String jsonAllProducts = dioritAPIClient.gettingListOfProducts();
        JsonNode jsonAllProductsNode = objectMapper.readTree(jsonAllProducts);
        JsonNode data = jsonAllProductsNode.get("data");
        for (JsonNode node : data) {
            if (!checkingForProductAvailability(node.get("name").asText())) {
                System.out.println("Product not found: " + node.get("name").asText());
                addProduct(node);
                System.out.println("Product: " + node.get("name").asText() + ", added");
            }
        }
    }

    private boolean checkingForProductAvailability(String name) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement("""
                select * from oc_product_description
                where name = ?;
                """);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private void addProduct(JsonNode jsonAddedProductNode) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("""
                    insert into oc_product(
                        model, sku, upc, ean, jan, isbn, mpn, location, quantity, stock_status_id,
                        image, video, manufacturer_id, shipping, price, cost, points, tax_class_id, date_available, weight,
                        weight_class_id, `length`, width, height, length_class_id, subtract, minimum, sort_order, status,
                        viewed, date_added, date_modified, noindex, dn_id, supplier
                    ) values (
                        ?, ?, '','dioritb2b', '', '', '', '', ?, 6,
                        ?, '', 0, 1, ?, 0, 0, 0, current_date(), ?,
                        1, ?, ?, ?, 1, 1, 1, 0, 1,
                        0, current_date(), current_date(), 1, 0, ''
                    )
                    """);

            String image = jsonAddedProductNode.get("main_photo").asText();
            preparedStatement.setString(1, jsonAddedProductNode.get("sku").asText());
            preparedStatement.setString(2, jsonAddedProductNode.get("sku").asText());
            preparedStatement.setInt(3, jsonAddedProductNode.get("stock").asInt());
            preparedStatement.setString(4, image);
            preparedStatement.setDouble(5, jsonAddedProductNode.get("price").asDouble());
            preparedStatement.setDouble(6, extractAttributes(jsonAddedProductNode, "Вес (кг)"));
            preparedStatement.setDouble(7, extractAttributes(jsonAddedProductNode, "Глубина (см)"));
            preparedStatement.setDouble(8, extractAttributes(jsonAddedProductNode, "Ширина (см)"));
            preparedStatement.setDouble(9, extractAttributes(jsonAddedProductNode, "Высота (см)"));

            preparedStatement.executeUpdate();
            addedProductDescription(jsonAddedProductNode,gettingProductId(image));

        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении товара: " + jsonAddedProductNode.get("name").asText());
            throw new RuntimeException("Ошибка добавления товара: " + e.getMessage(), e);
        }
    }

    private double extractAttributes(JsonNode root, String field) {
        for (JsonNode group : root.get("attributes")) {
            if (group.has(field)) {
                return group.get(field).asDouble();
            }
        }
        return 0;
    }

    private void addedProductDescription(JsonNode node, int productId) throws SQLException {
        PreparedStatement preparedStatement2 = con.prepareStatement("""
                INSERT INTO oc_product_description(
                    product_id, language_id, name, description, tag, meta_title,
                    meta_description, meta_keyword, meta_h1
                ) VALUES (?, 1, ?, '', '', ?, ?, '', ?);
                """);
        preparedStatement2.setInt(1, productId);
        preparedStatement2.setString(2, node.get("name").asText());
        preparedStatement2.setString(3, node.get("name").asText());
        preparedStatement2.setString(4, node.get("name").asText());
        preparedStatement2.setString(5, node.get("name").asText());
        preparedStatement2.executeUpdate();
    }

    private int gettingProductId(String image) throws SQLException {
        PreparedStatement preparedStatement1 = con.prepareStatement("""
            select * from oc_product
            where date_available = current_date()
            and ean = 'dioritb2b'
            and image = ?
            """);
        preparedStatement1.setString(1, image);
        ResultSet resultSet = preparedStatement1.executeQuery();
        return resultSet.getInt("product_id");
    }
}


