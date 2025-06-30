package org.slavik.entity.attribute;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttributeDescription {

    private final int attributeId;
    private final int languageId;
    private final String name;

    public AttributeDescription(int attributeId, int languageId, String name) {
        this.attributeId = attributeId;
        this.languageId = languageId;
        this.name = name;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public String getName() {
        return name;
    }

    public static class Mapper implements RowMapper<AttributeDescription> {

        @Override
        public @Nullable AttributeDescription mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int attributeId = rs.getInt("attribute_id");
            int languageId = rs.getInt("language_id");
            String name = rs.getString("name");
            return new AttributeDescription(
                    attributeId,
                    languageId,
                    name
            );
        }
    }
}
