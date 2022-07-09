package test;

import main.DemandSupplyMatcher;
import main.pojo.Order;
import main.pojo.OrderMatchResult;
import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DemandSupplyMatcherTest {
    @Test
    public void example1() throws Exception {
        List<Order> orders = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        orders.add(new Order("s1", LocalTime.parse("09:45", formatter),"tomato", BigDecimal.valueOf(24), 100L));
        orders.add(new Order("s2", LocalTime.parse("09:46", formatter),"tomato", BigDecimal.valueOf(20), 90L));
        orders.add(new Order("d1", LocalTime.parse("09:47", formatter),"tomato", BigDecimal.valueOf(22), 110L));
        orders.add(new Order("d2", LocalTime.parse("09:48", formatter),"tomato", BigDecimal.valueOf(21), 10L));
        orders.add(new Order("d3", LocalTime.parse("09:49", formatter),"tomato", BigDecimal.valueOf(21), 40L));
        orders.add(new Order("s3", LocalTime.parse("09:50", formatter),"tomato", BigDecimal.valueOf(19), 50L));

        List<OrderMatchResult> results = DemandSupplyMatcher.matchDemandSupply(orders);

        /* Expected Result
               d1 s2 20/kg 90kg
               d1 s3 19/kg 20kg
               d2 s3 19/kg 10kg
               d3 s3 19/kg 20kg
         */
        assertEquals("d1", results.get(0).getDemandOrderId());
        assertEquals("s2", results.get(0).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(20), results.get(0).getPrice());
        assertEquals(90L, results.get(0).getQuantity());

        assertEquals("d1", results.get(1).getDemandOrderId());
        assertEquals("s3", results.get(1).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(19), results.get(1).getPrice());
        assertEquals(20L, results.get(1).getQuantity());

        assertEquals("d2", results.get(2).getDemandOrderId());
        assertEquals("s3", results.get(2).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(19), results.get(2).getPrice());
        assertEquals(10L, results.get(2).getQuantity());

        assertEquals("d3", results.get(3).getDemandOrderId());
        assertEquals("s3", results.get(3).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(19), results.get(3).getPrice());
        assertEquals(20L, results.get(3).getQuantity());
    }

    @Test
    public void example2() throws Exception {
        List<Order> orders = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        orders.add(new Order("d1", LocalTime.parse("09:47", formatter),"tomato", BigDecimal.valueOf(110), 1L));
        orders.add(new Order("d2", LocalTime.parse("09:45", formatter),"potato", BigDecimal.valueOf(110), 10L));
        orders.add(new Order("d3", LocalTime.parse("09:48", formatter),"tomato", BigDecimal.valueOf(110), 10L));
        orders.add(new Order("s1", LocalTime.parse("09:45", formatter),"potato", BigDecimal.valueOf(110), 1L));
        orders.add(new Order("s2", LocalTime.parse("09:45", formatter),"potato", BigDecimal.valueOf(110), 7L));
        orders.add(new Order("s3", LocalTime.parse("09:45", formatter),"potato", BigDecimal.valueOf(110), 2L));
        orders.add(new Order("s4", LocalTime.parse("09:45", formatter),"tomato", BigDecimal.valueOf(110), 11L));

        List<OrderMatchResult> results = DemandSupplyMatcher.matchDemandSupply(orders);

        /* Expected Result
            d2 s1 110/kg 1kg
            d2 s2 110/kg 7kg
            d2 s3 110/kg 2kg
            d1 s4 110/kg 1kg
            d3 s4 110/kg 10kg
        */
        assertEquals("d2", results.get(0).getDemandOrderId());
        assertEquals("s1", results.get(0).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(110), results.get(0).getPrice());
        assertEquals(1L, results.get(0).getQuantity());


        assertEquals("d2", results.get(1).getDemandOrderId());
        assertEquals("s2", results.get(1).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(110), results.get(1).getPrice());
        assertEquals(7L, results.get(1).getQuantity());


        assertEquals("d2", results.get(2).getDemandOrderId());
        assertEquals("s3", results.get(2).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(110), results.get(2).getPrice());
        assertEquals(2L, results.get(2).getQuantity());


        assertEquals("d1", results.get(3).getDemandOrderId());
        assertEquals("s4", results.get(3).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(110), results.get(3).getPrice());
        assertEquals(1L, results.get(3).getQuantity());

        assertEquals("d3", results.get(4).getDemandOrderId());
        assertEquals("s4", results.get(4).getSupplyOrderId());
        assertEquals(BigDecimal.valueOf(110), results.get(4).getPrice());
        assertEquals(10L, results.get(4).getQuantity());
    }

}
