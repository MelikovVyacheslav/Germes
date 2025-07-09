package org.slavik.repository.attribute;

import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;

import java.util.List;

public interface AttributeDescriptionRepository {
    List<AttributeDescription> findAll();
    AttributeDescription find(int attributeId);
    List<AttributeDescription> findByName(String name);
    AttributeDescription create(AttributeDescription attributeDescription);
}
