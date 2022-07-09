package main.service;

import main.pojo.Order;
import main.pojo.OrderMatchResult;
import main.service.support.DemandOrderComparator;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DemandService implements AbstractService<Order> {
    private Map<String, PriorityQueue<Order>> supplyOrderMap;
    private Map<String, PriorityQueue<Order>> demandOrderMap;

    public DemandService(Map<String, PriorityQueue<Order>> demandOrderMap) {
        this.demandOrderMap = demandOrderMap;
    }

    public void setSupplyOrderMap(Map<String, PriorityQueue<Order>> supplyOrderMap) {
        this.supplyOrderMap = supplyOrderMap;
    }

    public Map<String, PriorityQueue<Order>> getDemandOrderMap() {
        return demandOrderMap;
    }

    @Override
    public void processOrder(final Order demandOrder, List<OrderMatchResult> orderMatchResults) {
        // create key in the map if it's not present.
        if (!demandOrderMap.containsKey(demandOrder.getProduce())) {
            demandOrderMap.put(demandOrder.getProduce(), new PriorityQueue<>(new DemandOrderComparator()));
        }

        // add demand order to Map based on provided Produce.
        boolean isAdded =  this.demandOrderMap.get(demandOrder.getProduce()).offer(demandOrder);

        if(isAdded) {
            fulfillOrder(demandOrder, orderMatchResults);
        }
    }

    /**
     * If quantity of demand order is less than equals quantity of supply order; then its a complete Match.
     * else partial order will be fulfilled.
     * @param demandOrder
     * @param orderMatchResults
     */
    private void fulfillOrder(Order demandOrder, List<OrderMatchResult> orderMatchResults) {
        // if there is no supply available for given Produce then don't proceed.
        if (!this.supplyOrderMap.containsKey(demandOrder.getProduce())) {
            return;
        }

        PriorityQueue<Order> supplyOrders = this.supplyOrderMap.get(demandOrder.getProduce());

        while (supplyOrders.size() > 0 && demandOrder.getPrice().compareTo(supplyOrders.peek().getPrice()) >= 0) {

            Order supplyOrder = supplyOrders.poll();

            if (demandOrder.getQuantity() <= supplyOrder.getQuantity()) {
                executeCompleteMatch(demandOrder, supplyOrder, orderMatchResults);
                this.demandOrderMap.get(demandOrder.getProduce()).remove(demandOrder);
            } else {
                executePartialMatch(demandOrder, supplyOrder, orderMatchResults);
            }

            if (demandOrder.getQuantity() == 0) {
                return;
            }
        }
    }

    private void executeCompleteMatch(Order demandOrder, Order supplyOrder, List<OrderMatchResult> orderMatchResults) {

        if (supplyOrder.getQuantity() != demandOrder.getQuantity()) {
            supplyOrder.setQuantity(supplyOrder.getQuantity() - demandOrder.getQuantity());
        }

        OrderMatchResult orderMatchResult = new OrderMatchResult(demandOrder.getOrderId(), supplyOrder.getOrderId() ,supplyOrder.getPrice(), demandOrder.getQuantity());

        this.demandOrderMap.get(demandOrder.getProduce()).remove(demandOrder);

        // add to the result list
        orderMatchResults.add(orderMatchResult);
    }

    private void executePartialMatch(Order demandOrder, Order supplyOrder, List<OrderMatchResult> orderMatchResults) {

        long demandOrderQuantity = demandOrder.getQuantity();

        demandOrder.setQuantity(demandOrderQuantity - supplyOrder.getQuantity());

        OrderMatchResult orderMatchResult = new OrderMatchResult(demandOrder.getOrderId(), supplyOrder.getOrderId() ,supplyOrder.getPrice(), supplyOrder.getQuantity());
        this.supplyOrderMap.get(supplyOrder.getProduce()).remove(supplyOrder);
        supplyOrder.setQuantity(supplyOrder.getQuantity() - demandOrderQuantity < 0 ? 0 : supplyOrder.getQuantity() - demandOrder.getQuantity());

        // add to the result list
        orderMatchResults.add(orderMatchResult);
    }
}
