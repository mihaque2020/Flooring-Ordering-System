/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Minul
 */
public class OrderDaoFileImplTest {

    OrderDao testOrderDao;

    public OrderDaoFileImplTest() {
    }
    
    String testFile = "testOrderFile.txt";
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testOrderFile.txt";
        // Use FileWriter to quickly blank the file
        new FileWriter(testFile);
        testOrderDao = new OrderDaoFileImpl();
        testOrderDao.setFullOrderFile(testFile);
    }

    @Test
    public void testSetGetOrderFile() throws Exception {
        // Arrange
        String orderDate = "07302020";
        String orderFile = "Orders_" + orderDate + ".txt";

        // Act
        testOrderDao.setOrderFile(orderDate);

        // Assert
        assertEquals(orderFile, testOrderDao.getOrderFile(),
                "The order file should be Orders_07302020");
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Arrange
        int orderNumber = 1;
        String orderDate = "07292020";
        Order newOrder = new Order(orderNumber);
        newOrder.setCustomerName("Software Guild");
        newOrder.setStateAbbrev("NY");
        newOrder.setProductType("Marble");
        newOrder.setArea(new BigDecimal("200"));
        newOrder.setCostPerSqFt(BigDecimal.TEN);
        newOrder.setLaborCostPerSqFt(BigDecimal.TEN);
        newOrder.setLaborCost(new BigDecimal("12"));
        newOrder.setTax(BigDecimal.TEN);
        newOrder.setTaxRate(BigDecimal.ONE);
        newOrder.setTotal(BigDecimal.ONE);
        newOrder.setMaterialCost(BigDecimal.TEN);
        
        // ACT
        testOrderDao.setOrderFile(testFile);
        testOrderDao.createOrder(orderNumber, newOrder);
        Order retrievedOrder = testOrderDao.getOrder(orderNumber, "07032020");

        // ASSERT
        assertEquals(newOrder.getOrderNumber(),
                retrievedOrder.getOrderNumber(),
                "Check order number");
        assertEquals(newOrder.getCustomerName(),
                retrievedOrder.getCustomerName(),
                "Check customer name");
        assertEquals(newOrder.getCostPerSqFt(),
                retrievedOrder.getCostPerSqFt(),
                "Check cost/sqft");
    }
/*
    @Test
    public void testGetAllOrders() throws Exception {
        // Arrange
        String orderDate = "07302020";
        Order newOrder1 = new Order(1);
        newOrder1.setCustomerName("Software Guild");
        newOrder1.setStateAbbrev("NY");
        newOrder1.setProductType("Marble");
        newOrder1.setArea(new BigDecimal("200"));
        newOrder1.setCostPerSqFt(BigDecimal.TEN);
        newOrder1.setLaborCostPerSqFt(BigDecimal.TEN);

        Order newOrder2 = new Order(2);
        newOrder2.setCustomerName("mthree");
        newOrder2.setStateAbbrev("NY");
        newOrder2.setProductType("Stone");
        newOrder2.setArea(new BigDecimal("260"));
        newOrder2.setCostPerSqFt(BigDecimal.TEN);
        newOrder2.setLaborCostPerSqFt(BigDecimal.TEN);

        // Act
        //testOrderDao.setOrderFile(testFile);
        // Add both orders to DAO
        //testOrderDao.setOrderFile(orderDate);
        testOrderDao.createOrder(newOrder1.getOrderNumber(), newOrder1);
        testOrderDao.createOrder(newOrder2.getOrderNumber(), newOrder2);

        // Retrieve from DAO
        List<Order> allOrders = testOrderDao.getAllOrders(orderDate);

        // Assert
        // General Check
        assertNotNull(allOrders, "The list of orders must not be null");
        assertEquals(2, allOrders.size(), "List of orders should have two orders");

        // Specifics check
        assertTrue(testOrderDao.getAllOrders(orderDate).contains(newOrder1));
        assertTrue(testOrderDao.getAllOrders(orderDate).contains(newOrder2));

    }

    @Test
    public void getOrder() throws Exception {
        fail("The test case is a prototype.");
    }

    @Test
    public void editOrder() throws Exception {
        fail("The test case is a prototype.");
    }

    @Test
    public void removeOrder() throws Exception {
        fail("The test case is a prototype.");
    }
 */
}
