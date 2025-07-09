package org.slavik.repository.attribute;

import org.slavik.entity.attribute.Attribute;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcAttributeRepository implements AttributeRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcAttributeRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Attribute> findAll() {
        String sql = """
                select * from oc_attribute;
                """;
        List<Attribute> attributeList = jdbcOperations.query(
                sql,
                new MapSqlParameterSource(),
                new Attribute.Mapper()
        );
        return attributeList;
    }

    @Override
    public Attribute find(int attributeId) {
        String sql = """
                select * from oc_attribute
                where attribute_id = :attributeId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("attributeId", attributeId);
        Attribute attribute = jdbcOperations.queryForObject(sql, params, new Attribute.Mapper());
        return attribute;
    }

    @Override
    public Attribute create(Attribute attribute) {
        String sql = """
                insert into oc_attribute(attribute_group_id, sort_order) values
                (:attributeGroupId, :sortOrder);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("attributeGroupId", attribute.getAttributeGroupId());
        params.addValue("sortOrder", attribute.getSortOrder());
        jdbcOperations.update(sql, params);
        int attributeId = gettingLastId();
        Attribute newAttribute = find(attributeId);
        return newAttribute;
    }

    private int gettingLastId() {
        String sql = """
                select max(attribute_id) from oc_attribute;
                """;
        Integer attributeId = jdbcOperations.queryForObject(
                sql,
                new MapSqlParameterSource(),
                Integer.class
                );
        return attributeId;
    }
}
