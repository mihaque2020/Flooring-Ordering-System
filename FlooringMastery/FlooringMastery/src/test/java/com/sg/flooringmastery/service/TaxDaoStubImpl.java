/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Tax;
import java.util.Map;

/**
 *
 * @author Minul
 */
public class TaxDaoStubImpl implements TaxDao{
    
    public Tax onlyTax;

    @Override
    public Map<String, Tax> getAllTaxes() throws TaxPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
