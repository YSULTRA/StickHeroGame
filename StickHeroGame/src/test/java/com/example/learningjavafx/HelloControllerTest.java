package com.example.learningjavafx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
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
    }

    @Test
    public void testGenerateRandomWidth() {
        // When
        double randomWidth = helloController.generateRandomWidth();

        // Then
        assertTrue(randomWidth >= 85 && randomWidth <= 280);
    }
     @Test
    public void testforPlayerHashCode(){
        assertEquals(helloController.getPlayer().hashCode(),Player.getInstance().hashCode());
     }
    @Test
    public void testDefaultValues() {
        assertEquals(3, helloController.getNoOfLife());
        assertNotNull(helloController.getPlayer());
        assertEquals(0, helloController.getSuccessfulMovements());
    }

}
