package org.example;
import java.util.Scanner;
import java.io.IOException;

/**The goal of this class is to call the necessary methods and classes to run the program
 *
 */
public class Main {
    /**
     *
     * @param args Command line arguments. Native to main.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        /// Scanner object to take user input
        Scanner scannerObj = new Scanner(System.in);
        /// user input
        System.out.println("Enter any name: ");
        /// String name holding user input
        String name = scannerObj.nextLine();
        /// Print name
        System.out.println(name);
        /// Define a new person with the name
        Person p = new Person(name);
        /// Display the initial random state and sensitivity of person
        p.displayInitPerson();
        /// Define a new SensoryArduino object with the current instance of person
        SensoryArduino person = new SensoryArduino(p);
        /// Run sensory arduino methods from ScheduleService
        person.ScheduleService(name);
        /// Show graph
        LightGraph.setSensoryArduino(person);
        LightGraph.main(args);
        /// Turn off OLED upon termination
        person.terminationControl();

    }
}