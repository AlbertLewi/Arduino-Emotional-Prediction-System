package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/// Write all possible outcomes of a happy to -> state transition, makes sure all state transitions work
class SensoryArduinoTest{

    /**
     * Testing a corner case where I had 700 and 300 thresholds but did not directly account for 700 and 300;\
     * Testing the first happy -> calm transition
     */
    @Test
    void stateChangeLogicCalm() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.9;
        double avgLight = 600;
        double avgSound  = 300;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Calm",state);
        System.out.println("Expected: Calm, got, " + state);

    }

    /**
     * Testing the second possible outcome in the Happy -> Calm transition
     */
    @Test
    void stateChangeLogicHappy() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.7;
        double avgLight = 700;
        double avgSound  = 300;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Happy",state);
        System.out.println("Expected: Happy, got, " + state);

    }

    /**
     * Test to see how method deals with null state cases
     */
    @Test
    void stateChangeLogicNull() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.7;
        double avgLight = 700;
        double avgSound  = 300;
        String ogState = null;
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertNull(state);
        System.out.println("Expected: null, got, " + state);
    }

    /**Testing if state returns as null and debug print occurs if rand is not between 0 and 1
     *
     */
    @Test
    void stateChangeLogic01Error() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 1.1;
        double avgLight = 700;
        double avgSound  = 300;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertNull(state);
    }

    /**
     * Testing the second possible outcome in the Happy -> Sad transition
     */
    @Test
    void stateChangeLogicSad() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.9;
        double avgLight = 300;
        double avgSound  = 300;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Sad",state);
        System.out.println("Expected: Sad, got, " + state);

    }
    /**
     * Testing the second possible outcome in the Happy -> Angry transition
     */
    @Test
    void stateChangeLogicAngry() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.9;
        double avgLight = 800;
        double avgSound = 705;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Angry", state);
        System.out.println("Expected: Angry, got, " + state);
    }

    /**Testing Happy -> Anxious transition
     *
     */
    @Test
    void stateChangeLogicAnxious() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.9;
        double avgLight = 400;
        double avgSound = 705;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Anxious", state);
        System.out.println("Expected: Anxious, got, " + state);
    }

    /** Testing a happy -> happy transition due to Rand being below 0.8 resistance
     *
     */
    @Test
    void stateChangeLogicLowRand() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.3; /// Rand is below 0.8
        double avgLight = 400;
        double avgSound = 705;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Happy", state);
        System.out.println("Expected: Happy, got, " + state);
    }

    /**Test to test non case. My non cases exist, and they seem to output the default value.
     * FIXED: Set up code for non cases by drawing out a number line on paper: Now we get an expected value of anxious
     *
     */
    @Test
    void stateChangeLogicNONcase() {
        SensoryArduino s = new SensoryArduino(new Person("name"));
        double random0to1 = 0.9; /// Rand is below 0.8
        double avgLight = 550;
        double avgSound = 550;
        String ogState = "Happy";
        s.stateChangeLogic(ogState, random0to1, avgLight, avgSound);
        String state = s.getCurrentState();
        assertEquals("Anxious", state);
        System.out.println("Expected: Anxious, got, " + state);
    }

}