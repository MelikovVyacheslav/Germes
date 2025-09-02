package org.slavik.builder;

import org.slavik.builder.exception.RequestEmpty;
import org.slavik.repository.OperationRepository;

import java.util.ArrayList;
import java.util.List;

public class SqlBuilder {

    private final OperationRepository operationRepository;

    private List<Object[]> valuesList = new ArrayList<>();
    private List<Object[]> valueForRequest = new ArrayList<>();
    private final int requestLimit = 1000;

    public SqlBuilder(OperationRepository operationRepository, List<Object[]> valuesList) {
        this.operationRepository = operationRepository;
        this.valuesList = valuesList;
    }

    public SqlBuilder(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public void addRequest(Object[] value) {
        valuesList.add(value);
    }

    public void insert() {
        if (valuesList.isEmpty()) {
            throw new RequestEmpty("Empty query list");
        }
        for (Object[] value : valuesList) {
            valueForRequest.add(value);
            if (valueForRequest.size() == requestLimit) {
                sendInsert();
            }
        }
        if (!valueForRequest.isEmpty()) {
            sendInsert();
        }
    }

    public void update() {
        if (valuesList.isEmpty()) {
            throw new RequestEmpty("Empty query list");
        }
        for (Object[] value : valuesList) {
            valueForRequest.add(value);
            if (valueForRequest.size() == requestLimit) {
                sendUpdate();
                valueForRequest = new ArrayList<>();
            }
        }
        if (!valueForRequest.isEmpty()) {
            sendUpdate();
        }
    }

    private void sendInsert() {
        operationRepository.createAllByRequest(valueForRequest);
        valueForRequest = new ArrayList<>();
    }

    private void sendUpdate() {
        operationRepository.updateAllByRequest(valueForRequest);
        valueForRequest = new ArrayList<>();
    }

    public boolean isEmpty() {
        return valuesList.isEmpty();
    }


}
