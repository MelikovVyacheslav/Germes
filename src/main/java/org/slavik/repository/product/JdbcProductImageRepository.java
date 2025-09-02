package org.slavik.repository.product;

import org.slavik.entity.product.ProductImage;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductImageRepository implements ProductImageRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductImageRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductImage> findAll() {
        String sql = """
                select * from oc_product_image;
                """;
        List<ProductImage> allProductImageList = jdbcOperations.query(sql, new MapSqlParameterSource(), new ProductImage.Mapper());
        return allProductImageList;
    }

    @Override
    public List<ProductImage> find(int productId) {
        String sql = """
                select * from oc_product_image
                where product_id = :productId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        List<ProductImage> productImage = jdbcOperations.query(sql, params, new ProductImage.Mapper());
        return productImage;
    }

    @Override
    public void create(ProductImage productImage) {
        String sql = """
                insert into oc_product_image(product_id, image, sort_order) values
                (:productId, :image, :sortOrder);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productImage.getProductId());
        params.addValue("image", productImage.getImage());
        params.addValue("sortOrder", productImage.getSortOrder());
        jdbcOperations.update(sql, params);
    }

    @Override
    public ProductImage update(ProductImage productImage) {
        String sql = """
                update oc_product_image set
                product_image_id = :productImageId,
                image = :image,
                sort_order = :sortOrder
                where product_id = :productId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productImageId", productImage.getProductImageId());
        params.addValue("image", productImage.getImage());
        params.addValue("sortOrder", productImage.getSortOrder());
        jdbcOperations.update(sql, params);
        return productImage;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {
        String sql = "insert into oc_product_image(product_id, image, sort_order) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, values);
    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {}
}
