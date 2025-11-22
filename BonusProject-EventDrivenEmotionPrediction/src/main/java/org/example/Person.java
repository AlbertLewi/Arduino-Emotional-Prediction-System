package org.example;
import java.util.Random;

/**Class that defines a new person when a name is typed into the main class, this person comes with a starting sensitivity and
 * an emotional state both defined at random. This class depends on if the name is typed into main. This class is important to
 * calculating state changes in SensoryArduino and is a precursor that needs to be initialized via passing a name before SensoryArduino
 * can be used.
 *
 */
public class Person {
    /// String variable containing original random emotional state
    private String emotionalState;
    /// Int variable containing random sensitivity measure on scale of 0->100
    private int sensitivityToInput;

    /** Defines a new person, constructor
     *
     * @param name Takes in a persons typed name: Typed in main
     */
    protected Person(String name){
        if(!name.isEmpty()) {
            Random rand = new Random();
            sensitivityToInput = rand.nextInt(100);
            String[] states = {"Happy", "Sad", "Angry", "Calm", "Anxious"};
            emotionalState = states[rand.nextInt(states.length)];
        }
        else{
            System.out.println("DEBUG: Name does not exist");
        }
    }

    /**Getter method to get sensitivity
     *
     * @return Random sensitivity to input
     */
    public int getSensitivityToInput() {
        return sensitivityToInput;
    }

    /** Getter method to get emotional state
     *
     * @return Random emotional state
     */
    public String getEmotionalState() {
        return emotionalState;
    }

    /**Method to display current attributes regarding new person
     *
     */
    public void displayInitPerson() {
        System.out.println("Original state is: " + emotionalState + "; Original Sensitivity is: " + sensitivityToInput);
    }
}


