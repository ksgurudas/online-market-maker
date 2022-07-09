package main.service;

import main.pojo.Order;
import main.pojo.OrderMatchResult;
import main.service.support.SupplyOrderComparator;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class SupplyService implements AbstractService<Order>{
    private Map<String, PriorityQueue<Order>> supplyOrderMap;
    private Map<String, PriorityQueue<Order>> demandOrderMap;

    public SupplyService(ConcurrentHashMap<String, PriorityQueue<Order>> supplyOrderMap ) {
        this.supplyOrderMap = supplyOrderMap;
    }

    public Map<String, PriorityQueue<Order>> getSupplyOrderMap() {
        return supplyOrderMap;
    }

    public void setDemandOrderMap(Map<String, PriorityQueue<Order>> demandOrderMap) {
        this.demandOrderMap = demandOrderMap;
    }

    @Override
    public void processOrder(Order supplyOrder, List<OrderMatchResult> orderMatchResults) {
        if (!this.supplyOrderMap.containsKey(supplyOrder.getProduce())) {
            this.supplyOrderMap
                    .put(supplyOrder.getProduce(), new PriorityQueue<>(new SupplyOrderComparator()));
        }

        boolean isAdded =  this.supplyOrderMap.get(supplyOrder.getProduce()).offer(supplyOrder);
        if (isAdded) {
                fulfillOrder(supplyOrder, orderMatchResults);
        }
    }


    private void fulfillOrder(Order supplyOrder, List<OrderMatchResult> orderMatchResults) {
        if (!this.demandOrderMap.containsKey(supplyOrder.getProduce())) {
            return;
        }

        final PriorityQueue<Order> demandOrders = this.demandOrderMap.get(supplyOrder.getProduce());

        while (demandOrders.size() > 0 && demandOrders.peek().getPrice().compareTo(supplyOrder.getPrice()) >= 0) {

            final Order demandOrder = demandOrders.poll();

            if (demandOrder.getQuantity() <= supplyOrder.getQuantity()) {
                executeCompleteMatch(demandOrder, supplyOrder, orderMatchResults);
            } else {
                executePartialMatch(demandOrder, supplyOrder, orderMatchResults);
                demandOrders.offer(demandOrder);
            }

            if (supplyOrder.getQuantity() == 0) {
                return;
            }
        }
    }

    private void executeCompleteMatch(Order demandOrder, Order supplyOrder, List<OrderMatchResult> orderMatchResults) {

        if (supplyOrder.getQuantity() == demandOrder.getQuantity()) {
            this.supplyOrderMap.get(supplyOrder.getProduce()).remove(supplyOrder);
        }
        supplyOrder.setQuantity(supplyOrder.getQuantity() - demandOrder.getQuantity());

        final OrderMatchResult orderTransaction = new OrderMatchResult(demandOrder.getOrderId(), supplyOrder.getOrderId(), supplyOrder.getPrice(), demandOrder.getQuantity());

        orderMatchResults.add(orderTransaction);

    }

    private void executePartialMatch(Order demandOrder, Order supplyOrder, List<OrderMatchResult> orderMatchResults) {
        long demandOrderQuantity = demandOrder.getQuantity();

        demandOrder.setQuantity(demandOrderQuantity - supplyOrder.getQuantity());
        final OrderMatchResult orderMatchResult = new OrderMatchResult(demandOrder.getOrderId(), supplyOrder.getOrderId(), supplyOrder.getPrice(), supplyOrder.getQuantity());


        this.supplyOrderMap.get(supplyOrder.getProduce()).remove(supplyOrder);
        supplyOrder.setQuantity(supplyOrder.getQuantity() - demandOrderQuantity < 0 ? 0 : supplyOrder.getQuantity() - demandOrder.getQuantity()) ;

        orderMatchResults.add(orderMatchResult);
    }
}
