/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.Exceptions.OrderPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.ProductsDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Minul
 */
public class FlooringMasteryServiceImpl implements FlooringMasteryService {

    /*
    private final OrderDaoFileImpl orderDao;
    private final ProductsDaoFileImpl productsDao;
    private final TaxDaoFileImpl taxDao;
    private final FlooringMasteryAuditDaoFileImpl auditDao; */
    private final OrderDao orderDao;
    private final ProductsDao productsDao;
    private final TaxDao taxDao;
    private final FlooringMasteryAuditDao auditDao;

    public FlooringMasteryServiceImpl(OrderDao orderDao,
            ProductsDao productsDao,
            TaxDao taxDao,
            FlooringMasteryAuditDao auditDao) {

        this.orderDao = orderDao;
        this.productsDao = productsDao;
        this.taxDao = taxDao;
        this.auditDao = auditDao;
    }

    /*
    public FlooringMasteryServiceImpl(OrderDaoFileImpl orderDao,
            ProductsDaoFileImpl productsDao,
            TaxDaoFileImpl taxDao,
            FlooringMasteryAuditDaoFileImpl auditDao) {

        this.orderDao = orderDao;
        this.productsDao = productsDao;
        this.taxDao = taxDao;
        this.auditDao = auditDao;
    } */
    @Override
    public boolean isValidDate(String orderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        try {
            LocalDate.parse(orderDate, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    @Override // Create a check to see if that order file exists
    public void setOrderFileDate(String orderDate) {
        orderDao.setOrderFile(orderDate);
    }

    @Override
    public List<Order> getAllOrders(String orderDate)
            throws OrderPersistenceException {
        return orderDao.getAllOrders(orderDate);
    }

    @Override
    public Map<String, Product> getAllProducts()
            throws ProductPersistenceException {
        return productsDao.getAllProducts();
    }

    @Override
    public Map<String, Tax> getAllTaxes()
            throws TaxPersistenceException {
        return taxDao.getAllTaxes();
    }

    @Override
    public int generateOrderNumber(String orderDate)
            throws OrderPersistenceException {

        List<Order> allDateOrders = getAllOrders(orderDate);
        int orderNumber = 1;
        for (Order order : allDateOrders) {
            if (order.getOrderNumber() >= 1) {
                orderNumber = order.getOrderNumber() + 1;
            }
        }
        return orderNumber;
    }

    public int checkOrderNumber(String orderDate, int orderNumber)
            throws OrderPersistenceException {

        List<Order> allDateOrders = getAllOrders(orderDate);
        for (Order order : allDateOrders) {
            if (order.getOrderNumber() == orderNumber) {
                orderNumber = order.getOrderNumber();
            }
        }
        return orderNumber;
    }

    @Override
    public Order calculateSetFields(Order newOrder)
            throws TaxPersistenceException {
        BigDecimal materialCost = newOrder.getArea().multiply(newOrder.getCostPerSqFt());
        BigDecimal laborCost = newOrder.getArea().multiply(newOrder.getLaborCostPerSqFt());
        Map<String, Tax> allTaxes = taxDao.getAllTaxes();
        BigDecimal taxRate = allTaxes.get(newOrder.getStateAbbrev()).getTaxRate();
        BigDecimal tax = (materialCost.add(laborCost)).multiply(taxRate.divide(new BigDecimal("100")));
        BigDecimal total = (materialCost.add(laborCost).add(tax));

        newOrder.setMaterialCost(materialCost);
        newOrder.setLaborCost(laborCost);
        newOrder.setTaxRate(taxRate);
        newOrder.setTax(tax);
        newOrder.setTotal(total);

        return newOrder;
    }

    @Override
    public void createOrder(int orderNumber, Order newOrder)
            throws OrderPersistenceException {
        orderDao.createOrder(orderNumber, newOrder);
    }

    @Override
    public boolean orderExists(String orderDate, int orderNumber)
            throws OrderPersistenceException {
        File tempFile = new File("C:\\Users\\Minul\\Documents\\NetBeansProjects\\mthree Aspire"
                + "\\FlooringMastery\\FlooringMastery\\Orders_" + orderDate + ".txt");
        boolean exists = tempFile.exists();

        if (exists) {
            setOrderFileDate(orderDate);
            int existingOrderNumber = generateOrderNumber(orderDate) - 1;
            if (orderNumber == existingOrderNumber) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean orderRemoveExists(String orderDate, int orderNumber)
            throws OrderPersistenceException {
        File tempFile = new File("C:\\Users\\Minul\\Documents\\NetBeansProjects\\mthree Aspire"
                + "\\FlooringMastery\\FlooringMastery\\Orders_" + orderDate + ".txt");
        boolean exists = tempFile.exists();

        if (exists) {
            setOrderFileDate(orderDate);
            int existingOrderNumber = checkOrderNumber(orderDate, orderNumber);
            if (orderNumber == existingOrderNumber) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public Order getOrder(List<Order> allDatesOrders, int orderNumber)
            throws OrderPersistenceException {
        for (Order order : allDatesOrders) {
            if (orderNumber == order.getOrderNumber()) {
                return order;
            }
        }
        return null;
    }

    @Override
    public void editOrder(String orderdate, int orderNumber, Order editedOrder)
            throws OrderPersistenceException {
        orderDao.editOrder(orderNumber, orderdate, editedOrder);
    }

    @Override
    public void removeOrder(String orderDate, int orderNumber)
            throws OrderPersistenceException {
        setOrderFileDate(orderDate);
        orderDao.removeOrder(orderDate, orderNumber);
        return;
    }

    @Override
    public void exportData() throws OrderPersistenceException {
        orderDao.exportAllOrders();
    }

}
