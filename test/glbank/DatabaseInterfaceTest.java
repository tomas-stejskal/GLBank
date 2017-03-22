/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomi
 */
public class DatabaseInterfaceTest {
    
    public DatabaseInterfaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of Login method, of class DatabaseInterface.
     */
    @Test
    public void testLogin() {
        System.out.println("Login");
        String username = "";
        String password = "";
        DatabaseInterface instance = new DatabaseInterface();
        int expResult = -1;
        int result = instance.Login(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of HistoryOfLogin method, of class DatabaseInterface.
     */
    @Test
    public void testHistoryOfLogin() {
        System.out.println("HistoryOfLogin");
        int id = 1;
        DatabaseInterface instance = new DatabaseInterface();
        instance.HistoryOfLogin(id);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getEmpNames method, of class DatabaseInterface.
     */
    @Test
    public void testGetEmpNames() {
        String[] s = {"Tomas","Stejskal","tomi.stejsi@gmail.com","T"};
        System.out.println("getEmpNames");
        int id = 1;
        DatabaseInterface instance = new DatabaseInterface();
        String[] expResult = s;
        String[] result = instance.getEmpNames(id);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
