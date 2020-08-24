/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.OrderPersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Minul
 */
public interface OrderDao {

    /**
     * Assigns the given
     *
     * @param orderDate
     */
    public void setOrderFile(String orderDate);

    /**
     *
     * @return the order file name
     */
    public String getOrderFile();

    /**
     * given an order date, returns a list of all the order objects on that date
     * If order date DNE, returns null;
     *
     * @param orderDate
     * @return
     */
    public List<Order> getAllOrders(String orderDate) throws OrderPersistenceException;

    /**
     * adds the new order to the map
     *
     * @param orderNumber
     * @param newOrder
     * @return
     */
    public Order createOrder(int orderNumber, Order newOrder) throws OrderPersistenceException;

    /**
     * Returns an existing order on a given date number or date does not exist,
     * return null
     *
     * @param orderNumber
     * @param orderDate
     * @return
     */
    public Order getOrder(int orderNumber, String orderDate) throws OrderPersistenceException;

    /**
     * Replaces the existing order with the editedOrder Object
     *
     * @param orderDate
     * @param orderNumber
     * @param editedOrder
     * @return
     */
    public Order editOrder(int orderNumber, String orderDate, Order editedOrder) throws OrderPersistenceException;

    /**
     * Validate order date/number exists in service layer and remove from Map
     *
     * @param orderDate
     * @param orderNumber
     * @return Order object that was removed
     */
    public void removeOrder(String orderDate, int orderNumber) throws OrderPersistenceException;

    public void setFullOrderFile(String fullFileName)
            throws OrderPersistenceException;
    
    public void exportAllOrders() 
            throws OrderPersistenceException;
}
