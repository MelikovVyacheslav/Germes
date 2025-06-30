package org.slavik.entity.attribute;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Attribute {

    private final int attributeId;
    private final int attributeGroupId;
    private final int sortOrder;

    public Attribute(int attributeId, int attributeGroupId, int sortOrder) {
        this.attributeId = attributeId;
        this.attributeGroupId = attributeGroupId;
        this.sortOrder = sortOrder;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public int getAttributeGroupId() {
        return attributeGroupId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public static class Mapper implements RowMapper<Attribute> {

        @Override
        public @Nullable Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
            int attributeId = rs.getInt("attribute_id");
            int attributeGroupId = rs.getInt("attribute_group_id");
            int sortOrder = rs.getInt("sort_order");
            return new Attribute(
                    attributeId,
                    attributeGroupId,
                    sortOrder
            );
        }
    }
}
