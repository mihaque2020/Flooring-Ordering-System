/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dao.Exceptions.FlooringMasteryPersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Minul
 */
public class FlooringMasteryAuditDaoFileImpl implements FlooringMasteryAuditDao {

    public static final String AUDIT_FILE = "audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not persist adudit "
                    + "information.", e);
        }
        // Format date time entries to audit.txt for each dispense
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(formatter);
        //LocalDateTime timestamp = LocalDateTime.now();
        //out.println(timestamp.toString() + " : " + entry);
        out.println(formattedTimestamp + " : " + entry);
        out.flush();
    }
}
