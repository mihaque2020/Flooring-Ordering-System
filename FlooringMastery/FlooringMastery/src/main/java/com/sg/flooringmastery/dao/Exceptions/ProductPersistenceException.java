/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao.Exceptions;

/**
 *
 * @author Minul
 */
public class ProductPersistenceException extends Exception {

    public ProductPersistenceException(String message) {
        super(message);
    }

    public ProductPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
