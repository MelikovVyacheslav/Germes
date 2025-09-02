package org.slavik.repository.attribute;

import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;

public class JdbcAttributeDescriptionRepository implements AttributeDescriptionRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private final int LANGUAGE_ID = 1;

    public JdbcAttributeDescriptionRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AttributeDescription> findAll() {
        String sql = """
                select * from oc_attribute_description;
                """;
        List<AttributeDescription> attributeDescriptionList = jdbcOperations.query(
                sql,
                new MapSqlParameterSource(),
                new AttributeDescription.Mapper()
        );
        return attributeDescriptionList;
    }

    @Override
    public AttributeDescription find(int attributeId) {
        String sql = """
                select * from oc_attribute_description
                where attribute_id = :attributeId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("attributeId", attributeId);
        AttributeDescription attributeDescription = jdbcOperations.queryForObject(
                sql,
                params,
                new AttributeDescription.Mapper()
        );
        return attributeDescription;
    }

    @Override
    public List<AttributeDescription> findByName(String name) {
        String sql = """
                select * from oc_attribute_description
                where name = :name
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        List<AttributeDescription> attributeDescriptionList = jdbcOperations.query(
                sql,
                params,
                new AttributeDescription.Mapper()
        );
        return attributeDescriptionList;
    }

    @Override
    public AttributeDescription create(AttributeDescription attributeDescription) {
        String sql = """
                insert into oc_attribute_description(attribute_id, language_id, name) values
                (:attributeId, :languageId, :name);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("attributeId", attributeDescription.getAttributeId());
        params.addValue("languageId", LANGUAGE_ID);
        params.addValue("name", attributeDescription.getName());
        jdbcOperations.update(sql, params);
        return attributeDescription;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {
        String sql = "insert into oc_attribute_description(attribute_id, language_id, name) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, values);
    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {}
}
