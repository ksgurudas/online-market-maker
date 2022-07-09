package main.pojo;

import java.math.BigDecimal;

public class OrderMatchResult {

    private String demandOrderId;

    private String supplyOrderId;

    private BigDecimal price;

    private long quantity;

    public OrderMatchResult(String demandOrderId, String supplyOrderId, BigDecimal price, long quantity) {
        this.demandOrderId = demandOrderId;
        this.supplyOrderId = supplyOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getDemandOrderId() {
        return demandOrderId;
    }

    public String getSupplyOrderId() {
        return supplyOrderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderMatchResult{" +
                "demandOrderId='" + demandOrderId + '\'' +
                ", supplyOrderId='" + supplyOrderId + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
