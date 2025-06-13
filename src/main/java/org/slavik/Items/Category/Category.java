package org.slavik.Items.Category;

public class Category {
    private final int categoryId;
    private final String image;
    private final int parentId;
    private final String dateAdded;
    private final String dateModify;

    public Category(int categoryId, String image, int parentId, int top, int column,
                    int sortOrder, int status, String dateAdded, String dateModify, int noIndex) {
        this.categoryId = categoryId;
        this.image = image;
        this.parentId = parentId;
        this.dateAdded = dateAdded;
        this.dateModify = dateModify;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getImage() {
        return image;
    }

    public int getParentId() {
        return parentId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDateModify() {
        return dateModify;
    }
}
