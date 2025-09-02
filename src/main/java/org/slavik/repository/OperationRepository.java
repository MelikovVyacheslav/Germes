package org.slavik.repository;

import java.util.List;

public interface OperationRepository {

    void createAllByRequest(List<Object[]> values);

    void updateAllByRequest(List<Object[]> values);

}
