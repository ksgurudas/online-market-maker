package main.service;

import main.pojo.Order;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OrderTypeFactory will create the instances for DemandService and SupplyService classes
 * based on the order type passed and returns the appropriate service instance.
 * @author Gurudas Sulebhavikar
 */
public class OrderTypeFactory {
    private static DemandService demandService;
    private static SupplyService supplyService;

    static {

        demandService = new DemandService(new ConcurrentHashMap<String, PriorityQueue<Order>>());

        supplyService = new SupplyService(new ConcurrentHashMap<String, PriorityQueue<Order>>());

        // make demand/supply orders available in both the services.
        demandService.setSupplyOrderMap(supplyService.getSupplyOrderMap());
        supplyService.setDemandOrderMap(demandService.getDemandOrderMap());

    }


    public static AbstractService<Order> getService(String orderId) throws Exception {
        if (orderId.startsWith("s")) {
            return supplyService;
        } else if(orderId.startsWith("d")){
            return demandService;
        } else { // throw error if invalid orders are being processed. At this moment we are just supporting Demand and Supply orders.
            throw new Exception("Invalid Order Type");
        }
    }

}
