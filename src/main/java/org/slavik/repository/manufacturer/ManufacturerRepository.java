package org.slavik.repository.manufacturer;

import org.slavik.entity.manufacturer.Manufacturer;

import java.util.List;

public interface ManufacturerRepository {
    List<Manufacturer> findAll();
    Manufacturer find(String name);
    Manufacturer create(Manufacturer manufacturer);

}
