/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dao.ProductsDao;
import com.sg.flooringmastery.dao.ProductsDao;
import com.sg.flooringmastery.dto.Product;
import java.util.Map;

/**
 *
 * @author Minul
 */
public class ProductsDaoStubImpl implements ProductsDao{

    public Product onlyProduct;
    
    @Override
    public Map<String, Product> getAllProducts() throws ProductPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
