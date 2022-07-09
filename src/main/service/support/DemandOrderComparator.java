package main.service.support;

import main.pojo.Order;

import java.util.Comparator;

public class DemandOrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getPrice().compareTo(o2.getPrice()) == 0) {
            return o1.getTime().isBefore(o2.getTime())==true?-1:1;
        } else {
            return o2.getPrice().compareTo(o1.getPrice());
        }
    }
}
