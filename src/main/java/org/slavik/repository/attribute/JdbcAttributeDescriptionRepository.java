package org.slavik.repository.attribute;

import org.slavik.entity.attribute.AttributeDescription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcAttributeDescriptionRepository implements AttributeDescriptionRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    private final int LANGUAGE_ID = 1;

    public JdbcAttributeDescriptionRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
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
}
