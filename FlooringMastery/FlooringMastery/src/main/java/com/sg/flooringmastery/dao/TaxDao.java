/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dto.Tax;
import java.util.Map;

/**
 *
 * @author Minul
 */
public interface TaxDao {
    
    public /*List<Tax>*/Map<String,Tax> getAllTaxes() throws TaxPersistenceException;
    
}
