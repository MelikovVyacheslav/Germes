package org.slavik.repository.product;

import org.slavik.entity.product.ProductToProductDescription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductToProductDescription {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductToProductDescription(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }
    public List<ProductToProductDescription> findAll() {
        String sql = """
                SELECT op.product_id, opd.name, opd.description, op.ean
                FROM oc_product op
                JOIN oc_product_description opd ON op.product_id = opd.product_id
                order by product_id;
                """;
        List<ProductToProductDescription> productToProductDescriptionList =
                jdbcOperations.query(sql, new MapSqlParameterSource(), new ProductToProductDescription.Mapper());
        return productToProductDescriptionList;
    }

    public List<ProductToProductDescription> findAllByEAN(String ean) {
        String sql = """
                SELECT op.product_id, opd.name, opd.description, op.ean
                FROM oc_product op
                JOIN oc_product_description opd ON op.product_id = opd.product_id
                where ean = :ean
                order by product_id;
                """ ;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ean", ean);
        List<ProductToProductDescription> productToProductDescriptionList =
                jdbcOperations.query(sql, params, new ProductToProductDescription.Mapper());
        return productToProductDescriptionList;
    }

    public ProductToProductDescription findAllByNameAndEAN(String name, String ean) {
        String sql = """
                SELECT op.product_id, opd.name, opd.description, op.ean
                FROM oc_product op
                JOIN oc_product_description opd ON op.product_id = opd.product_id
                where name = :name, ean = :ean;
                """ ;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("ean", ean);
        ProductToProductDescription productToProductDescription =
                jdbcOperations.queryForObject(sql, params, new ProductToProductDescription.Mapper());
        return productToProductDescription;
    }
}
