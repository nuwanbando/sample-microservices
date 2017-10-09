package com.madewell.service.ordermgt;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nuwanbando on 3/29/17.
 */
public class Order {
    private static final Random rand = new Random();
    private static final Map<String, Double> priceList = new HashMap<String, Double>();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
    private static final NumberFormat currencyFormat = new DecimalFormat("#.##");

    private String orderId;
    private String itemName;
    private String itemCode;
    private double cost;
    private boolean locked;
    private long timestamp;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String customerId;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Order(String itemName, String location, String customerId) {
        this.orderId = UUID.randomUUID().toString();
        this.itemName = itemName;
        this.itemCode = location;
        this.cost = calculateCost();
        this.timestamp = System.currentTimeMillis();
        this.customerId = customerId;
    }
    public String getOrderId() {
        return orderId;
    }

    public String getitemName() {
        return itemName;
    }

    public void setitemName(String itemName) {
        this.itemName = itemName;
        this.cost = calculateCost();
        this.timestamp = System.currentTimeMillis();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
        this.cost = calculateCost();
        this.timestamp = System.currentTimeMillis();
    }

    public String getCost() {
        return currencyFormat.format(cost);
    }

    public String getTimestamp() {
        return dateFormat.format(new Date(timestamp));
    }

    boolean isAmountAcceptable(double amount) {
        return amount >= cost;
    }

    private double calculateCost() {
        double cost = getPrice(itemName, false);
        if (itemCode != null && !"".equals(itemCode)) {
            String[] locations = itemCode.split(" ");
            for (String item : locations) {
                cost += getPrice(item, true);
            }
        }
        return Double.parseDouble(currencyFormat.format(cost));
    }

    private double getPrice(String item, boolean location) {
        synchronized (priceList) {
            Double price = priceList.get(item);
            if (price == null) {
                if (location) {
                    price = rand.nextDouble() * 5;
                } else {
                    price = rand.nextInt(8) + 2 - 0.01;
                }
                priceList.put(item, price);
            }
            return price;
        }
    }

}
