package main.pojo;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Order {
    private String orderId;

    private LocalTime time;

    private String produce;

    private BigDecimal price;

    private long quantity;

    public Order(String orderId, LocalTime time, String produce, BigDecimal price, long quantity) {
        this.orderId = orderId;
        this.time = time;
        this.produce = produce;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getProduce() {
        return produce;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", time=" + time +
                ", produce='" + produce + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
