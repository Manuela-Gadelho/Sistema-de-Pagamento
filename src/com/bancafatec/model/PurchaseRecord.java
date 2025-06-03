package com.bancafatec.model;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class PurchaseRecord {
    private List<CartItem> items; 
    private double totalAmount;   
    private Date purchaseDate;    

    public PurchaseRecord(List<CartItem> items, double totalAmount) {
        this.items = items.stream().map(item -> new CartItem(item.getName(), item.getPrice())).collect(Collectors.toList());
        this.totalAmount = totalAmount;
        this.purchaseDate = new Date(); 
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateStr = sdf.format(purchaseDate);
        
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------\n");
        sb.append("Data: ").append(dateStr).append("\n");
        sb.append("Total: R$").append(String.format("%.2f", totalAmount)).append("\n");
        sb.append("Itens:\n");
        for (CartItem item : items) {
            sb.append("  - ").append(item.getName()).append(" (R$").append(String.format("%.2f", item.getPrice())).append(")\n");
        }
        sb.append("--------------------------------------\n");
        return sb.toString();
    }
}
