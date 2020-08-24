/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Minul
 */
public class FlooringMasteryView {

    private final UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuGetSelection() {
        io.print("1. Display Orders");
        io.print("2. New Order");
        io.print("3. Edit Order");
        io.print("4. Delete Order");
        io.print("5. Export All Orders");
        io.print("6. Quit");

        return io.readInt("Enter Choice: ", 1, 6);
    }

    public String getNewOrderDate() {
        String date = "";
        LocalDate dateNow = LocalDate.now();
        LocalDate dateOrder;
        do {
            date = io.readString("Enter order date in MMDDYYYY Format: ");
            dateOrder = LocalDate.parse(date, DateTimeFormatter.ofPattern("MMddyyyy"));
            if (dateOrder.isBefore(dateNow) || dateOrder.isEqual(dateNow)) {
                io.print("Error...must be a future date.");
            }
        } while (dateOrder.isBefore(dateNow) || dateOrder.isEqual(dateNow));
        
        return date;
    }
    
    public String getOrderDate() {
        return io.readString("Enter order date in MMDDYYYY Format: ");
    }

    public Order getOrderInfo(int orderNumber,
            Map<String, Product> allProducts, Map<String, Tax> allTaxes) {
        Order newOrder = new Order(orderNumber);
        String customerName = io.readString("Enter Name: ");

        String stateAbbrev = "";
        Tax tax = null;
        boolean invalidState = true;
        do {
            stateAbbrev = io.readString("Enter State Abbreviation (Ex: NY): ");
            if (allTaxes.containsKey(stateAbbrev)) {
                invalidState = false;
                tax = allTaxes.get(stateAbbrev);
                break;
            }
            io.print("Invalid state entry...");
        } while (invalidState);

        printAllProducts(allProducts);

        String productType = "";
        Product product = null;
        boolean invalidProduct = true;
        do {
            productType = io.readString("Enter Product Type: ");
            if (allProducts.containsKey(productType)) {
                invalidProduct = false;
                product = allProducts.get(productType);
                break;
            }
            io.print("Invalid product entry...");
        } while (invalidProduct);

        BigDecimal area = io.readBigDecimal("Enter Area (min: 100): ",
                new BigDecimal("100"), new BigDecimal("100000000"));

        newOrder.setCustomerName(customerName);
        newOrder.setStateAbbrev(stateAbbrev);
        newOrder.setProductType(productType);
        newOrder.setArea(area);
        newOrder.setCostPerSqFt(product.getCostPerSqFt());
        newOrder.setLaborCostPerSqFt(product.getLaborCostPerSqFt());
        newOrder.setTaxRate(tax.getTaxRate());

        return newOrder;
    }

    public void printAllProducts(Map<String, Product> allProducts) {
        List<Product> listOfProducts = new ArrayList<>(allProducts.values());
        for (Product product : listOfProducts) {
            io.print(String.format("Product Type: %s\n"
                    + "Cost per SqFt: $%s\n"
                    + "Labor Cost per SqFt: $%s\n"
                    + "",
                    product.getProductType(),
                    product.getCostPerSqFt(),
                    product.getLaborCostPerSqFt()));
        }
    }

    public void displayOrders(List<Order> datesOrder) {
        for (Order order : datesOrder) {
            String orderInfo = String.format(""
                    + "Order Number: %s\n"
                    + "Customer Name: %s\n"
                    + "State: %s\n"
                    + "Tax Rate: %s\n"
                    + "Product Type: %s\n"
                    + "Area: %s\n"
                    + "Cost/SqFt: %s\n"
                    + "Labor Cost/SqFt: $%s\n"
                    + "Material Cost: $%s\n"
                    + "Labor Cost: $%s\n"
                    + "Tax: $%s\n"
                    + "Total: $%s",
                    Integer.toString(order.getOrderNumber()),
                    order.getCustomerName(), order.getStateAbbrev(),
                    order.getTaxRate().toString(), order.getProductType(),
                    order.getArea(), order.getCostPerSqFt(),
                    order.getLaborCostPerSqFt(), order.getMaterialCost(),
                    order.getLaborCost(), order.getTax(),
                    order.getTotal());
            io.print(orderInfo);
            io.print("");
        }
    }

    public void displayOrder(Order currentOrder) {
        String orderInfo = String.format(""
                + "Order Number: %s\n"
                + "Customer Name: %s\n"
                + "State: %s\n"
                + "Tax Rate: %s\n"
                + "Product Type: %s\n"
                + "Area: %s\n"
                + "Cost/SqFt: %s\n"
                + "Labor Cost/SqFt: $%s\n"
                + "Material Cost: $%s\n"
                + "Labor Cost: $%s\n"
                + "Tax: $%s\n"
                + "Total: $%s",
                Integer.toString(currentOrder.getOrderNumber()),
                currentOrder.getCustomerName(), currentOrder.getStateAbbrev(),
                currentOrder.getTaxRate().toString(),
                currentOrder.getProductType(),
                currentOrder.getArea(), currentOrder.getCostPerSqFt(),
                currentOrder.getLaborCostPerSqFt(),
                currentOrder.getMaterialCost(),
                currentOrder.getLaborCost(), currentOrder.getTax(),
                currentOrder.getTotal());
        io.print(orderInfo);
    }

    public Order editOrderInfo(Order editOrder, Map<String, Product> allProducts,
            Map<String, Tax> allTaxes) {
        String currentName = editOrder.getCustomerName();
        String currentState = editOrder.getStateAbbrev();
        String currentProduct = editOrder.getProductType();
        BigDecimal currentArea = editOrder.getArea();

        String newName = io.readString("Enter customer name (" + currentName + "): ");

        String stateAbbrev = "";
        Tax tax = null;
        boolean invalidState = true;
        do {
            stateAbbrev = io.readString("Enter State (" + currentState + "): ");
            if (stateAbbrev.equals("")) {
                break;
            }
            if (allTaxes.containsKey(stateAbbrev)) {
                invalidState = false;
                tax = allTaxes.get(stateAbbrev);
                break;
            }
            io.print("Invalid state entry...");
        } while (invalidState);

        printAllProducts(allProducts);

        String productType = "";
        Product product = null;
        boolean invalidProduct = true;
        do {
            productType = io.readString("Enter product type (" + currentProduct + "): ");
            if (productType.equals("")) {
                break;
            }
            if (allProducts.containsKey(productType)) {
                invalidProduct = false;
                product = allProducts.get(productType);
                break;
            }
            io.print("Invalid product entry...");
        } while (invalidProduct);

        BigDecimal bigNewArea = BigDecimal.ZERO;
        String newArea;
        do {
            newArea = io.readString("Enter area (" + currentArea + "): ");
            if (newArea.equals("")) {
                break;
            }
            bigNewArea = new BigDecimal(newArea);
        } while (bigNewArea.compareTo(new BigDecimal("100")) < 0);

        if (!newName.equals("")) {
            editOrder.setCustomerName(newName);
        }
        if (!stateAbbrev.equals("")) {
            editOrder.setStateAbbrev(stateAbbrev);
        }
        if (!productType.equals("")) {
            editOrder.setProductType(productType);
        }
        if (!newArea.equals("")) {
            editOrder.setArea(bigNewArea);
        }
        return editOrder;
    }

    public int getOrderNumber() {
        return io.readInt("Enter Order Number: ", 1, 10000);
    }

    public int getConfirmation() {
        return io.readInt("1. Confirm Order \n2. Cancel Order", 1, 2);
    }

    public int getRemovalConfirmation() {
        return io.readInt("1. Confirm Remove Order \n2. Cancel Order Removal", 1, 2);
    }

    public void editSuccessfulBanner() {
        io.print("=== Order Edits Saved ===");
        io.print("");
    }

    public void displayNewOrderSuccessBanner() {
        io.print("=== New Order Created! ===");
        io.print("");
    }
    
    public void displayOrderCanceledBanner() {
        io.print("=== New Order Canceled ===");
    }
    
    public void displayOrderEditsNotSavedBanner() {
        io.print("=== Order Edits Not Saved ===");
        io.print("");
    }

    public void displayOrderDNEBanner() {
        io.print("=== Order Does NOT Exist ===");
        io.print("");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }

    public void displayDateErrorMessage() {
        io.print("=== ERROR ===");
        io.print("Please enter a valid date in format(MMDDYYYY)");
    }

    public void displayRemoveSuccessBanner() {
        io.print("=== Order Successfully Removed ===");
        io.print("");
    }

    public void displayExportSuccessBanner() {
        io.print("=== Export Successful ===");
        io.print("");
    }

    public void displayUnknownCommandMessage() {
        io.print("=== Unknown Command ===");
        io.print("");
    }
}
