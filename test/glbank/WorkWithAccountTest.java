/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glbank;

import java.util.Arrays;
import java.util.List;
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
public class WorkWithAccountTest {
    
    public WorkWithAccountTest() {
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
     * Test of loadAccountsByIDC method, of class WorkWithAccount.
     */
    @Test
    public void testLoadAccountsByIDC() {
        String[] r = {"1358024679","2704644349","2802757408"};
        System.out.println("loadAccountsByIDC");
        String idc = "1";
        WorkWithAccount instance = new WorkWithAccount();
        List<String> expResult = Arrays.asList(r);
        List<String> result = instance.loadAccountsByIDC(idc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getBalance method, of class WorkWithAccount.
     */
    @Test
    public void testGetBalance() {
        System.out.println("getBalance");
        String idAcc = "1358024679";
        WorkWithAccount instance = new WorkWithAccount();
        String expResult = "26.30";
        String result = instance.getBalance(idAcc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addToBalance method, of class WorkWithAccount.
     */
    @Test
    public void testAddToBalance() {
        System.out.println("addToBalance");
        double sum = 0.0;
        String idAcc = "";
        WorkWithAccount instance = new WorkWithAccount();
        instance.addToBalance(sum, idAcc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitFromBalance method, of class WorkWithAccount.
     */
    @Test
    public void testSubmitFromBalance() {
        System.out.println("submitFromBalance");
        double sum = 0.0;
        String idAcc = "";
        WorkWithAccount instance = new WorkWithAccount();
        instance.submitFromBalance(sum, idAcc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
