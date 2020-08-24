/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.ProductPersistenceException;
import com.sg.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Minul
 */
public class ProductsDaoFileImpl implements ProductsDao{

    private String PRODUCTS_FILE;
    private static final String DELIMITER = ",";
    private static final String REPLACEMENT = "--";
    private static final int NUMBER_OF_FIELDS = 3;
    
    public ProductsDaoFileImpl() {
        PRODUCTS_FILE = "products.txt";
    }
    
    public ProductsDaoFileImpl(String productsTextFile) {
        PRODUCTS_FILE = productsTextFile;
    }
    
    private Map<String,Product> allProducts = new HashMap<>();
    
    @Override
    public Map<String, Product> getAllProducts() 
        throws ProductPersistenceException {
        loadProductsFromFile();
        return allProducts;
    }
    
    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        
        if (productTokens.length == NUMBER_OF_FIELDS) {
            
            String productType = productTokens[0];
            Product productFromFile = new Product(productType);
            productFromFile.setCostPerSqFt(new BigDecimal(productTokens[1]));
            productFromFile.setLaborCostPerSqFt(new BigDecimal(productTokens[2]));
            return productFromFile;
        } else {
            return null;
        }
    }
    
        private void loadProductsFromFile() throws ProductPersistenceException {
        Scanner scanner;

        try {
            //Create Scnner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException ex) {
            throw new ProductPersistenceException(
                    "Could not load product data into memory", ex);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentProduct holds the most recent order unmarshalled
        Product currentProduct;
  
        // Skip header
        String headers = scanner.nextLine();
        while (scanner.hasNextLine()) {
            //get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an order
            currentProduct = unmarshallProduct(currentLine);

            // User order number as key for Order Objects
            if (currentProduct != null) {
                allProducts.put(currentProduct.getProductType(), currentProduct);
            }
        }
        // Close Scanner
        scanner.close();
    }
        
        
}

