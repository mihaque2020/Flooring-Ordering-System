/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.TaxPersistenceException;
import com.sg.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Minul
 */
public class TaxDaoFileImpl implements TaxDao {

    private String TAX_FILE;
    private static final String DELIMITER = ",";
    private static final String REPLACEMENT = "--";
    private static final int NUMBER_OF_FIELDS = 3;

    public TaxDaoFileImpl() {
        TAX_FILE = "taxes.txt";
    }

    public TaxDaoFileImpl(String taxesTestFile) {
        TAX_FILE = taxesTestFile;
    }

    private Map<String, Tax> allTaxes = new HashMap<>();

    @Override
    public Map<String, Tax>/*List<Tax>*/ getAllTaxes() throws TaxPersistenceException {
        loadTaxesFromFile();
        //return new ArrayList<Tax>(allTaxes.values());
        return allTaxes;
    }

    private Tax unmarshallTax(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMITER);

        if (taxTokens.length == NUMBER_OF_FIELDS) {

            String stateAbbrev = taxTokens[0];
            Tax taxFromFile = new Tax(stateAbbrev);
            taxFromFile.setStateName(taxTokens[1]);
            taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));
            return taxFromFile;
        } else {
            return null;
        }
    }

    private void loadTaxesFromFile() throws TaxPersistenceException {
        Scanner scanner;

        try {
            //Create Scnner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException ex) {
            throw new TaxPersistenceException(
                    "Could not load tax data into memory", ex);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        Tax currentTax;

        // Skip header
        String headers = scanner.nextLine();
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            if (currentTax != null) {
                allTaxes.put(currentTax.getStateAbbrev(), currentTax);
            }
        }
        // Close Scanner
        scanner.close();
    }
}
