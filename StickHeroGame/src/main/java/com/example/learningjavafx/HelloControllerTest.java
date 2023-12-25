package com.example.learningjavafx;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelloControllerTest {

    private HelloController helloController;

    @Before
    public void setUp() {
        helloController = new HelloController();
    }

    @Test
    public void testProcessPlayerDetails_ValidInput() {
        // Given
        String playerDetails = "Highest Score: 100";

        // When
        helloController.processPlayerDetails(playerDetails);

        // Then
        assertEquals(100, helloController.getPlayer().getHighestScore());
        System.out.println("Test 1 passed");
    }

    @Test
    public void testGenerateRandomWidth() {
        // When
        double randomWidth = helloController.generateRandomWidth();

        // Then
        assertTrue(randomWidth >= 85 && randomWidth <= 280);
        System.out.println("Test 2 passed");
    }

    @Test
    public void testforPlayerHashCode() {
        // Then
        assertEquals(helloController.getPlayer().hashCode(), Player.getInstance().hashCode());
        System.out.println("Test 3 passed");
    }

    @Test
    public void testDefaultValues() {
        // Then
        assertEquals(3, helloController.getNoOfLife());
        assertNotNull(helloController.getPlayer());
        assertEquals(0, helloController.getSuccessfulMovements());
        System.out.println("Test 4 passed");
    }
}
