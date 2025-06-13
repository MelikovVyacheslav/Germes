package org.slavik.Items.Category;

public class CategoryDescription {
    private final int categoryId;
    private final String description;
    private final String meta;

    public CategoryDescription(int categoryId, String description, String meta) {
        this.categoryId = categoryId;
        this.description = description;
        this.meta = meta;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getMeta() {
        return meta;
    }
}
