
package com.websyh.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/root-context.xml"
})
public class DataSourceTest {

    @Inject 
    private DataSource ds;
    
    @Test 
    public void testConnection() {
        try (Connection con = ds.getConnection()) {
            if (con != null) {
                System.out.println("Successfully connected to the database. Connection: " + con.toString());
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}