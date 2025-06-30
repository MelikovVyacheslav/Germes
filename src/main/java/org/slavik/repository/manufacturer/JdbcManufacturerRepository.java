package org.slavik.repository.manufacturer;

import org.slavik.entity.manufacturer.Manufacturer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcManufacturerRepository implements ManufacturerRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcManufacturerRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    private final int SORT_ORDER = 0;
    private final int NOINDEX_VALUE = 1;

    @Override
    public List<Manufacturer> findAll() {
        String sql = """
                select * from oc_manufacturer;
                """;
        List<Manufacturer> manufacturers = jdbcOperations.query(sql, new Manufacturer.Mapper());
        return manufacturers;
    }

    @Override
    public Manufacturer find(String name) {
        String sql = """
                select * from oc_manufacturer
                where name = :name;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        Manufacturer manufacturer;
        try {
            params.addValue("name", name);
            manufacturer = jdbcOperations.queryForObject(sql, params, new Manufacturer.Mapper());
        } catch (EmptyResultDataAccessException e ) {
            manufacturer = create(new Manufacturer(
                    0,
                    name
            ));
        }
        return manufacturer;
    }

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String sql = """
                insert into oc_manufacturer(name, sort_order, noindex) values (
                :name, :sortOrder, :noindex
                );
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", manufacturer.getName());
        params.addValue("sortOrder", SORT_ORDER);
        params.addValue("noindex", NOINDEX_VALUE);
        jdbcOperations.update(sql, params);
        return manufacturer;
    }
}
