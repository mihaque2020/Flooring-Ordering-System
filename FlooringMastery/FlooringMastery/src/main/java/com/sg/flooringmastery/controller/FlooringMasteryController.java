/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.Exceptions.OrderPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.util.List;
import java.util.Map;
import com.sg.flooringmastery.service.FlooringMasteryService;

/**
 *
 * @author Minul
 */
public class FlooringMasteryController {

    private FlooringMasteryService service;
    private FlooringMasteryView view;

    public FlooringMasteryController(FlooringMasteryService service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();
                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        createNewOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
        } catch (OrderPersistenceException
                | ProductPersistenceException
                | TaxPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuGetSelection();
    }

    private void displayOrders() throws OrderPersistenceException {
        boolean hasDateErrors = true;
        do {
            String orderDate = view.getOrderDate();
            if (service.isValidDate(orderDate)) {
                service.setOrderFileDate(orderDate);
                List<Order> datesOrder = service.getAllOrders(orderDate);
                view.displayOrders(datesOrder);
                datesOrder.clear();
                hasDateErrors = false;
            } else {
                view.displayDateErrorMessage();
            }
        } while (hasDateErrors);
    }

    private void createNewOrder() throws
            ProductPersistenceException,
            OrderPersistenceException,
            TaxPersistenceException {
        boolean isInvalid = true;
        do {
            String orderDate = view.getNewOrderDate();
            if (service.isValidDate(orderDate)) {
                // Generate order and get info
                service.setOrderFileDate(orderDate);
                int orderNumber = service.generateOrderNumber(orderDate);
                Map<String, Product> allProducts = service.getAllProducts();
                Map<String, Tax> allTaxes = service.getAllTaxes();
                Order newOrder = view.getOrderInfo(orderNumber, allProducts, allTaxes);
                newOrder = service.calculateSetFields(newOrder);

                // Display and confirm order creation
                view.displayOrder(newOrder);
                int confirmation = 0;
                confirmation = view.getConfirmation();
                if (confirmation == 1) {
                    view.displayNewOrderSuccessBanner();
                    service.createOrder(orderNumber, newOrder);
                } else {
                    view.displayOrderCanceledBanner();
                }
                isInvalid = false;
                break;
            } else {
                view.displayDateErrorMessage();
                orderDate = view.getNewOrderDate();
            }
        } while (isInvalid);
    }

    private void editOrder() throws
            OrderPersistenceException,
            TaxPersistenceException,
            ProductPersistenceException {
        boolean orderDNE = true;
        do {
            String orderDate = view.getOrderDate();
            int orderNumber = view.getOrderNumber();
            if (service.orderExists(orderDate, orderNumber)) {
                Map<String, Product> allProducts = service.getAllProducts();
                Map<String, Tax> allTaxes = service.getAllTaxes();
                List<Order> allDatesOrders = service.getAllOrders(orderDate);
                Order editOrder = service.getOrder(allDatesOrders, orderNumber);
                Order editedOrder = view.editOrderInfo(editOrder, allProducts, allTaxes);
                editedOrder = service.calculateSetFields(editedOrder);
                view.displayOrder(editedOrder);
                int confirmation = view.getConfirmation();
                if (confirmation == 1) {
                    service.editOrder(orderDate, orderNumber, editedOrder);
                    view.editSuccessfulBanner();;
                } else {
                    view.displayOrderEditsNotSavedBanner();;
                }
                orderDNE = false;
            } else {
                view.displayOrderDNEBanner();
            }
        } while (orderDNE);
    }

    private void removeOrder() throws OrderPersistenceException {
        String orderDate = view.getOrderDate();
        int orderNumber = view.getOrderNumber();
        if (service.orderRemoveExists(orderDate, orderNumber)) {
            service.setOrderFileDate(orderDate);
            List<Order> allDatesOrders = service.getAllOrders(orderDate);
            Order removeOrder = service.getOrder(allDatesOrders, orderNumber);
            view.displayOrder(removeOrder);
            int confirmation = view.getRemovalConfirmation();
            if (confirmation == 1) {
                view.displayRemoveSuccessBanner();
                service.removeOrder(orderDate, orderNumber);
            } 
        }
    }

    private void exportData() throws OrderPersistenceException {
        service.exportData();
        view.displayExportSuccessBanner();
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandMessage();
    }
}
