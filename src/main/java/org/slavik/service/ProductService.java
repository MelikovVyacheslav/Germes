package org.slavik.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductService {
    void sync() throws JsonProcessingException;
}
