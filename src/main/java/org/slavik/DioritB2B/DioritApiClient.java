package org.slavik.DioritB2B;

import org.slavik.ApiClient;
import org.slavik.DioritB2B.model.DioritCategory;
import org.slavik.entity.category.Category;

import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<DioritCategory> getAll();
}
