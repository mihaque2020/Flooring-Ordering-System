/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.OrderPersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Minul
 */
public class OrderDaoFileImpl implements OrderDao {

    private String orderFile;
    private static final String DELIMITER = ",";
    private static final String REPLACEMENT = "--";
    private static final int NUMBER_OF_FIELDS = 12;

    public OrderDaoFileImpl() {
    }

    public OrderDaoFileImpl(String testFile) {
        this.orderFile = testFile;
    }

    private Map<Integer, Order> allOrders = new HashMap<>();

    @Override
    public void setOrderFile(String orderDate) {
        this.orderFile = "Orders_" + orderDate + ".txt";
    }

    @Override
    public void setFullOrderFile(String fullFileName)
            throws OrderPersistenceException {
        this.orderFile = fullFileName;
    }

    @Override
    public String getOrderFile() {
        return orderFile;
    }

    @Override
    public List<Order> getAllOrders(String orderDate)
            throws OrderPersistenceException {
        loadOrdersFromFile();
        List<Order> retrievedOrders = new ArrayList<Order>(allOrders.values());
        allOrders.clear();
        return retrievedOrders;
    }

    @Override
    public Order createOrder(int orderNumber, Order newOrder)
            throws OrderPersistenceException {
        loadOrdersFromFile();
        Order previousOrder = allOrders.put(orderNumber, newOrder);
        writeOrdersToFile();
        return previousOrder;
    }

    @Override
    public Order getOrder(int orderNumber, String orderDate)
            throws OrderPersistenceException {
        loadOrdersFromFile();
        return allOrders.get(orderNumber);
    }

    @Override
    public Order editOrder(int orderNumber, String orderDate, Order editedOrder)
            throws OrderPersistenceException {
        loadOrdersFromFile();
        Order previousOrder = allOrders.put(orderNumber, editedOrder);
        writeOrdersToFile();
        return previousOrder;
    }

    @Override
    public void removeOrder(String orderDate, int orderNumber)
            throws OrderPersistenceException {
        loadOrdersFromFile();
        allOrders.remove(orderNumber);
        writeOrdersToFile();
        return;
    }

    @Override
    public void exportAllOrders()
            throws OrderPersistenceException {
        File path = new File("C:\\Users\\Minul\\Documents\\NetBeansProjects\\mthree Aspire"
                + "\\FlooringMastery\\FlooringMastery");
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter("DataExport.txt"));
        } catch (IOException e) {
            throw new OrderPersistenceException(
                    "Could not save export data.", e);
        }
        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,"
                + "Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                + "LaborCost,Tax,Total,Date";
        out.println(header);
        out.flush();
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.getName().startsWith("Orders_")) {
                setFullOrderFile(file.getName());
                loadOrdersFromFile();

                writeAllOrdersToFile();
                allOrders.clear();
            }
        }
        out.close();
    }

    private void setOrderFile(int orderDate) {
        this.orderFile = "Orders_" + orderDate + ".txt";
    }

    private Order unmarshallOrder(String orderAsText) {
        // orderAsText expecting a line read in from our file.
        // For Example the text in the file looks like:
        // 1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06
        // Split that line on our DELIMITER (::) 
        // Results in an array of Strings, stored in orderTokens.
        // Looks LIKE:
        // ________________________________________________________________________________________________________________
        // |   |               |      |       |       |         |      |       |        |          |         |          |
        // | 1 |  Ada Lovelace    CA    25.00   Tile    249.00    3.50    4.15   871.50    1033.35    476.21    2381.06
        // |   |               |      |       |       |         |      |       |        |          |         |          | 
        // ----------------------------------------------------------------------------------------------------------------
        //  [0]       [1]        [2]     [3]     [4]      [5]     [6]      [7]     [8]       [9]         [10]      [11]

        String[] orderTokens = orderAsText.split(DELIMITER);

        // Chekc to make sure I have expected number of items from split
        if (orderTokens.length == NUMBER_OF_FIELDS) {

            // orderNumber is Index 0
            String orderNumber = orderTokens[0];

            // We can create new Order Object using the name 
            // - all we need to satisfy constructor requirements
            Order orderFromFile = new Order(Integer.parseInt(orderNumber));

            // Get remaining tokens and use setters to apply to current Item Object
            // Index 1 - customer name
            orderFromFile.setCustomerName(orderTokens[1]);

            // Index 2 -  State Abbrev
            orderFromFile.setStateAbbrev(orderTokens[2]);

            // Index 3 - Tax Rate
            orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));

            // Index 4 - Product Type
            orderFromFile.setProductType(orderTokens[4]);

            // Index 5 - Area
            orderFromFile.setArea(new BigDecimal(orderTokens[5]));

            // Index 6 - Cost/SqFt
            orderFromFile.setCostPerSqFt(new BigDecimal(orderTokens[6]));

            // Index 7 - Labor Cost/SqFt
            orderFromFile.setLaborCostPerSqFt(new BigDecimal(orderTokens[7]));

            // Index 8 - Material Cost
            orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));

            // Index 9 - Labor Cost
            orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));

            // Index 10 - Tax
            orderFromFile.setTax(new BigDecimal(orderTokens[10]));

            // Index 11 - Total
            orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

            // We have now created an Order, return it!
            return orderFromFile;
        } else {
            return null;
        }
    }

    private void loadOrdersFromFile() throws OrderPersistenceException {
        Scanner scanner;
        // if file exists 
        // 
        try {
            //Create Scnner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(orderFile)));
        } catch (FileNotFoundException ex) {
            allOrders = new HashMap<>();
            return;
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentOrder holds the most recent order unmarshalled
        Order currentOrder;
        // Go through the orderFile line by line, decoding each line into a 
        // Order object by calling unamrshallOrder method
        // Process while we have more lines:
        // Skip header
        String headers = scanner.nextLine();
        while (scanner.hasNextLine()) {
            //get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an order
            currentOrder = unmarshallOrder(currentLine);

            // User order number as key for Order Objects
            if (currentOrder != null) {
                allOrders.put(currentOrder.getOrderNumber(), currentOrder);
            }
        }
        // Close Scanner
        scanner.close();
    }

    private String replaceControlCharacters(String value) {
        return value.replace(DELIMITER, REPLACEMENT);
    }

    private String marshallOrder(Order anOrder) {
        // Turn Order object into a line of text for our file
        // Want in form: 1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06
        // Start with orderNumber because thats supposed to be first
        String orderAsText = replaceControlCharacters(Integer.toString(anOrder.getOrderNumber())) + DELIMITER;
        // Customer Name
        orderAsText += replaceControlCharacters(anOrder.getCustomerName()) + DELIMITER;
        // State Abbrev
        orderAsText += replaceControlCharacters(anOrder.getStateAbbrev()) + DELIMITER;
        // Tax Rate
        orderAsText += replaceControlCharacters(anOrder.getTaxRate().toString()) + DELIMITER;
        // Product Type
        orderAsText += replaceControlCharacters(anOrder.getProductType()) + DELIMITER;
        // Area
        orderAsText += replaceControlCharacters(anOrder.getArea().toString()) + DELIMITER;
        // Cost/SqFt
        orderAsText += replaceControlCharacters(anOrder.getCostPerSqFt().toString()) + DELIMITER;
        // Labor Cost/SqFt
        orderAsText += replaceControlCharacters(anOrder.getLaborCostPerSqFt().toString()) + DELIMITER;
        // Material Cost
        orderAsText += replaceControlCharacters(anOrder.getMaterialCost().toString()) + DELIMITER;
        // Labor Cost
        orderAsText += replaceControlCharacters(anOrder.getLaborCost().toString()) + DELIMITER;
        // Tax
        orderAsText += replaceControlCharacters(anOrder.getTax().toString()) + DELIMITER;
        // Total - Skip delimeter on last piece of data
        orderAsText += replaceControlCharacters(anOrder.getTotal().toString());

        // We have the Order as text for out file, return it
        return orderAsText;
    }

    private void writeOrdersToFile() throws OrderPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(orderFile));
        } catch (IOException e) {
            throw new OrderPersistenceException(
                    "Could not save order data.", e);
        }

        String orderAsText;
        List<Order> orderList = new ArrayList<Order>(allOrders.values());
        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,"
                + "Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                + "LaborCost,Tax,Total";
        out.println(header);
        out.flush();
        for (Order currentOrder : orderList) {
            // turn an Order into a string
            orderAsText = marshallOrder(currentOrder);
            // write order object to the file
            out.println(orderAsText);
            // force PrintWrite to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }

    private void writeAllOrdersToFile() throws OrderPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter("DataExport.txt", true));
        } catch (IOException e) {
            throw new OrderPersistenceException(
                    "Could not save order data.", e);
        }
        String orderAsText;
        List<Order> orderList = new ArrayList<Order>(allOrders.values());

        for (Order currentOrder : orderList) {
            // turn an Order into a string
            orderAsText = marshallOrderExport(currentOrder);
            // write order object to the file
            out.println(orderAsText);
            // force PrintWrite to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }

    private String marshallOrderExport(Order anOrder) {
        // Turn Order object into a line of text for our file
        // Want in form: 1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06
        // Start with orderNumber because thats supposed to be first
        String orderAsText = replaceControlCharacters(Integer.toString(anOrder.getOrderNumber())) + DELIMITER;
        // Customer Name
        orderAsText += replaceControlCharacters(anOrder.getCustomerName()) + DELIMITER;
        // State Abbrev
        orderAsText += replaceControlCharacters(anOrder.getStateAbbrev()) + DELIMITER;
        // Tax Rate
        orderAsText += replaceControlCharacters(anOrder.getTaxRate().toString()) + DELIMITER;
        // Product Type
        orderAsText += replaceControlCharacters(anOrder.getProductType()) + DELIMITER;
        // Area
        orderAsText += replaceControlCharacters(anOrder.getArea().toString()) + DELIMITER;
        // Cost/SqFt
        orderAsText += replaceControlCharacters(anOrder.getCostPerSqFt().toString()) + DELIMITER;
        // Labor Cost/SqFt
        orderAsText += replaceControlCharacters(anOrder.getLaborCostPerSqFt().toString()) + DELIMITER;
        // Material Cost
        orderAsText += replaceControlCharacters(anOrder.getMaterialCost().toString()) + DELIMITER;
        // Labor Cost
        orderAsText += replaceControlCharacters(anOrder.getLaborCost().toString()) + DELIMITER;
        // Tax
        orderAsText += replaceControlCharacters(anOrder.getTax().toString()) + DELIMITER;
        // Total - Skip delimeter on last piece of data
        orderAsText += replaceControlCharacters(anOrder.getTotal().toString());

        String orderFileName = orderFile;
        String date = orderFileName.replaceAll("[^0-9]+", "");
        orderAsText += DELIMITER + date;
        
        // We have the Order as text for out file, return it
        return orderAsText;
    }
}
