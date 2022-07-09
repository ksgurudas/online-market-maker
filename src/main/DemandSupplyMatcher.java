package main;

import main.pojo.Order;
import main.pojo.OrderMatchResult;
import main.service.LedgerService;

import java.util.ArrayList;
import java.util.List;

/**
 * DemandSupplyMatcher will match the demand with the supply in the ledger. Whenever a new supply or demand
 * is published matching is done. If the requirement cannot be fulfilled then the orders remain in the ledger.
 * @author Gurudas Sulebhavikar
 */
public class DemandSupplyMatcher {
    public static List<OrderMatchResult> matchDemandSupply(List<Order> orders, boolean clearStaticVar) throws Exception {
        // Avoid matching algorithm if there are orders in the ledger and throw exception.
        if (orders.isEmpty()) {
            throw new Exception("There is no demand-supply present at this moment.");
        }

        System.out.println("Orders Input: ");
        for(Order order : orders) {
            System.out.println(order);
        }

        System.out.println("****************************************************************************\n\n");

        // Empty list to store the matching result.
        List<OrderMatchResult> orderMatchResults = new ArrayList<>();

        // Iterate each order from ledger and try to match them to proper demand/supply orders.
        for (Order order : orders) {
            LedgerService.matchDemandSupplyOrders(order, orderMatchResults, clearStaticVar);
            clearStaticVar = false;
        }

        System.out.println("Orders Output: ");
        for(OrderMatchResult order : orderMatchResults) {
            System.out.println(order);
        }
        System.out.println("****************************************************************************\n\n");
        return orderMatchResults;

    }
}
