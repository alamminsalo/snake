/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakegame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author G7719
 */

public class SnakeTest {
    
    public SnakeTest() {
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
     * Test of getLenght method, of class Snake.
     */
    @org.junit.Test
    public void testGetLenght() {
        System.out.println("getLenght");
        Snake instance = new Snake();
        instance.initializeGame(30);
        int expResult = instance.getLenght()+2;
        instance.growLenght(instance.getLenght());
        int result = instance.getLenght();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getX method, of class Snake.
     */
    @org.junit.Test
    public void testGetX() {
        System.out.println("getX");
        int i = 0;
        Snake instance = new Snake();
        instance.initializeGame(30);
        int expResult = 0;
        int result = instance.getX(i);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getY method, of class Snake.
     */
    @org.junit.Test
    public void testGetY() {
        System.out.println("getY");
        int i = 0;
        Snake instance = new Snake();
        instance.initializeGame(30);
        int expResult = 0;
        int result = instance.getY(i);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setPos method, of class Snake.
     */
    @org.junit.Test
    public void testSetPos() {
        System.out.println("setPos");
        int a = 10;
        int b = 20;
        Snake instance = new Snake();
        instance.initializeGame(30);
        instance.setPos(a, b);
        instance.setPos(a-1, b+1);
        
        int expResult = 9;
        assertEquals(expResult, instance.getX(instance.getLenght()));
        
        expResult = 21;
        assertEquals(expResult, instance.getY(instance.getLenght()));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setFood method, of class Snake.
     */
    @org.junit.Test
    public void testSetFood() {
        System.out.println("setFood");
        int a = 0;
        int b = 0;
        Snake instance = new Snake();
        
        instance.setFood(a, b);
        assertEquals(instance.getFoodX(), a);
        assertEquals(instance.getFoodY(), b);
    }
    
}
