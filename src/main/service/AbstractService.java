package main.service;

import main.pojo.OrderMatchResult;

import java.util.List;

public interface AbstractService <Type> {
    void processOrder(Type order, List<OrderMatchResult> orderMatchResults);
}
