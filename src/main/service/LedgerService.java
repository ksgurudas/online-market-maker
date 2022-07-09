package main.service;

import main.pojo.Order;
import main.pojo.OrderMatchResult;

import java.util.List;

/**
 * Ledger service will get the appropriate service from the OrderTypeFactory and process order based on order type.
 * If order is of type Supply then look for matching Demand orders.
 * If order is of type Demand then look for supply matching orders.
 * @author Gurudas Sulebhavikar
 */
public class LedgerService {
    public static void matchDemandSupplyOrders(Order order, List<OrderMatchResult> orderMatchResults) throws Exception {
        OrderTypeFactory.getService(order.getOrderId()).processOrder(order, orderMatchResults);
    }
}
