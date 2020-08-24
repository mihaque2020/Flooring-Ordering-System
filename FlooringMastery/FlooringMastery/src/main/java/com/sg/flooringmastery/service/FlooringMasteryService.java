/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.Exceptions.OrderPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Minul
 */
public interface FlooringMasteryService {

    public boolean isValidDate(String orderDate);

    public void setOrderFileDate(String orderDate);

    public List<Order> getAllOrders(String OrderDate) 
            throws OrderPersistenceException;

    public Map<String, Product> getAllProducts() 
            throws ProductPersistenceException;

    public Map<String, Tax> getAllTaxes() 
            throws TaxPersistenceException;

    public int generateOrderNumber(String orderDate) 
            throws OrderPersistenceException;

    public Order calculateSetFields(Order newOrder) 
            throws TaxPersistenceException;

    public void createOrder(int orderNumber, Order newOrder) 
            throws OrderPersistenceException;

    public boolean orderExists(String orderDate, int orderNumber) 
            throws OrderPersistenceException;

    public Order getOrder(List<Order> allDatesOrders, int orderNumber) 
            throws OrderPersistenceException;

    public void editOrder(String orderdate, int orderNumber, Order editedOrder) 
            throws OrderPersistenceException;
    
    public void removeOrder(String orderDate, int orderNumber) 
            throws OrderPersistenceException;
    
    public void exportData() throws OrderPersistenceException;
    
    public boolean orderRemoveExists(String orderDate, int orderNumber)
            throws OrderPersistenceException;
}
