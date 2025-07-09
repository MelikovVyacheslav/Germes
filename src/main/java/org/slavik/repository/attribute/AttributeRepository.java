package org.slavik.repository.attribute;

import org.slavik.entity.attribute.Attribute;

import java.util.List;

public interface AttributeRepository {
    List<Attribute> findAll();
    Attribute find(int attributeId);
    Attribute create(Attribute attribute);
}
