/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dto.Product;
import java.util.Map;

/**
 *
 * @author Minul
 */
public interface ProductsDao {
    
    public Map<String, Product> getAllProducts() throws ProductPersistenceException;
    
}
