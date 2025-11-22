package org.example;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/** Class that deals with the main Logic of the system, all logic is operated on a person from the Person class.
 * Has a Person in constructor to call the current object instance of person. Thus depends on if person is
 * initialized.
 */
public class SensoryArduino {

    /// Defining arduino object to access important values
    private static final ArduinoConnection arduino = new ArduinoConnection("COM4");

    /// New Person object
    private final Person newperson;

    ///  String variable defining the current emotional state
    private String currentEmotion;

    /// String variable defining an emotionalUpdate/change in state
    private String emotionUpdate;

    /// Integer Array list holding all light values gathered in 10 seconds
    List<Integer> lightArray = new ArrayList<>();

    /// Integer Array list holding all light values gathered in 10 seconds
    List<Integer> soundArray = new ArrayList<>();

    /// Declaring random method, assigning an object rand to it
    Random rand = new Random();

    /// Initializing average value variables, double's for accuracy
    double avgLight;
    double avgSound;

    /// Related to execution service
    private int elapsedTime = 0;
    /// Scheduled Executor service that runs one thread pool
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    /// Defining variables for original state values
    private final int ogSens;
    private final String ogState;

    /// Random integer from 0 -> 1;
    double random0to1 = Math.random();

    /**
     * Initializing variables for ogSens and ogState
     * SensoryArduino depends on if person is initialized, must be a name typed for Person class to work
     *
     * @param p Takes in an object instance of Person
     */
    public SensoryArduino(Person p) {
        this.newperson = p;

        ogSens = newperson.getSensitivityToInput();
        ogState = newperson.getEmotionalState();
    }

    /**
     * Method to calculate the average of all the elements in an integer ArrayList
     *
     * @param array Takes in any integer ArrayList
     * @return The average of all elements in array
     */
    private int calculateAverage(List<Integer> array) {
        /// Debug to fix null array arithmetic error
        if (array.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int Value : array) {
            sum += Value;
        }
        return sum / array.size();
    }

    /**
     * Calculating the average value of the updated array lists
     * lightArray and soundArray
     */
    private void findReaValue() {
        avgLight = calculateAverage(lightArray);
        avgSound = calculateAverage(soundArray);
        System.out.println("Light average is: " + avgLight);
        System.out.println("Sound average is: " + avgSound);

    }

    /** Getter method to return avg Light value
     *
     * @return Returns average sound value
     */
    public double getAvgSound() {
        return avgSound;
    }

    /** Getter method to return avgLight value
     *
     * @return Returns average light value
     */
    public double getAvgLight() {
        return avgLight;
    }

    /**
     * Method to update arrays holding light and sound sensor values.
     * Updating for 10 seconds, taking the mean and
     *
     * @throws IOException Indicates a problem with board communication: Thrown by getPIns()
     */
    private void updateArrays() throws IOException {
        arduino.getPins();
        /// Long variable defining the raw light sensor value
        int light = (int) arduino.getLightSensorValue();
        /// Long variable defining the raw sound sensor value
        int sound = (int) arduino.getSoundSensorValue();
        lightArray.add(light);
        soundArray.add(sound);
    }

    /**Getter method to return a new copy of light array
     *
     * @return Returns light array list
     */
    public List<Integer> getlightList() {

        return new ArrayList<>(lightArray);
    }

    /**Getter method to return a new copy of sound array
     *
     * @return Returns sound array list
     */
    public List<Integer> getsoundList() {
        return new ArrayList<>(soundArray);
    }

    /**Method to control the general logic for state changes, depends on if certain parameters are inputted
     * Class is to be called in calculateEmotion() method
     *
     * @param emotion Takes in an emotion state: ogState variable
     * @param rand  Takes in a random value between 0->1: random0to1 variable
     * @param light Takes in the average light value: avgLight variable
     * @param sound Takes in the average sound value: avgSound variable
     */
    protected void stateChangeLogic(String emotion, double rand, double light, double sound) {
        /// Checks if rand is between 0 and 1
        if(rand > 1 || rand < 0) {
            System.out.println("DEBUG: random number must be between 0 and 1");
            return;
        }
        /// Checks for null state
        if (emotion == null) {
            System.out.println("DEBUG: State can not be null");
            return;
        }

        /// Map taking in String and Double key value pair - String: State - Double: Associated weighting for logic
        HashMap<String, Double> map = new HashMap<>();
        map.put("Happy", 0.80);
        map.put("Sad", 0.20);
        map.put("Angry", 0.20);
        map.put("Calm", 0.70);
        map.put("Anxious", 0.70);

        currentEmotion = emotion;
        double resistance = map.getOrDefault(emotion, 0.50);

        /// Conditionals defining general rules
        /// The actual change depends on the resistance, the resistance resists change depending on probability, based on the hash map
        try {
            ///Bright and quiet = happy
            random0to1 = Math.random();
            System.out.println("DEBUG: stateChangeLogic: random0to1: " + random0to1);
            if ((light >= 700) && (sound <= 600)) {
                if (rand > resistance) {
                    currentEmotion = "Happy";
                    System.out.println("DEBUG: stateChangeLogic: State: " + currentEmotion);
                }
            }
            /// Dim and quiet = sad
            else if ((light <= 300) && (sound < 400)) {
                if (rand > resistance) {
                    currentEmotion = "Sad";
                    System.out.println("DEBUG: stateChangeLogic: State: " + currentEmotion);
                }
            }
            ///Bright and loud = Angry
            else if ((light >= 700) && (sound >= 600)) {
                if (rand > resistance) {
                    currentEmotion = "Angry";
                    System.out.println("DEBUG: stateChangeLogic: State: " + currentEmotion);
                }
            }
            /// Moderately bright and quiet = calm
            else if((light > 300) && (sound <= 500)) {
                if (rand > resistance) {
                    currentEmotion = "Calm";
                    System.out.println("DEBUG: stateChangeLogic: State: " + currentEmotion);
                }
            }
            /// Moderately bright and loud = Anxious
            else if ((light < 700) && (sound >= 500)) {
                if (rand > resistance) {
                    currentEmotion = "Anxious";
                    System.out.println("DEBUG: stateChangeLogic: State: " + currentEmotion);
                }
            }
        } catch (Exception e) {
            /// Print Debug to determine if the state is actually working
            System.out.println("ERROR occurred with state changes");
        }
    }

    /**
     * Getter, Used to graph
     *
     * @return Current emotion after state change logic
     */
    public String getCurrentState() {
        return currentEmotion;
    }

    /**
     * Getter method, Used to graph
     *
     * @return Original state defined by Person class
     */
    public String getOgState() {
        return ogState;
    }

    /**
     * Method to process the emotion depending on the emotional case: The case would be the starting emotion
     * defined within the Person(): Works directly with the stateChangeLogic method to calculate an emotional state.
     */
    protected void calculateEmotion(){
        switch (ogState) {
            case ("Happy"):
            case ("Sad"):
            case ("Anxious"):
            case ("Calm"):
            case ("Angry"):
                stateChangeLogic(ogState, random0to1, avgLight, avgSound);
                /// Integer variable defining the individual persons sensitivity
                int individualSensitivity = ogSens;
                if (individualSensitivity >= 80) {
                    int probabilitySens = rand.nextInt(100);
                    System.out.println("DEBUG: Probability: " + probabilitySens);
                    if (probabilitySens > individualSensitivity) {
                        stateChangeLogic(ogState, random0to1, avgLight, avgSound);
                        emotionUpdate = getCurrentState();
                        System.out.println("Current state is: 1st: " + emotionUpdate);
                    } else {
                        emotionUpdate = getCurrentState();
                        System.out.println("Current state is: 1st: " + emotionUpdate);
                    }
                } else {
                    int probability = rand.nextInt(79);
                    System.out.println("DEBUG: Probability: " + probability);
                    if (probability >= individualSensitivity) {
                        stateChangeLogic(ogState, random0to1, avgLight, avgSound);
                        emotionUpdate = getCurrentState();
                        System.out.println("Current state is: 2nd: " + emotionUpdate);
                    } else {
                        emotionUpdate = getCurrentState();
                        System.out.println("Current state is: 2nd: " + emotionUpdate);
                    }
                }
                break;
            default:
                System.out.println("Case does not exist");
                break;
        }
    }

    /**
     * Method for getting emotion update, used in graph titles
     *
     * @return emotionUpdate variable storing a String defining a state
     */
    private String getEmotionUpdate() {
        return emotionUpdate;
    }

    /**
     * Method to display decisions and the current state to the OLED display, works dynamically depending on certain conditions
     *
     * @throws InterruptedException Thrown if thread.sleep() is interrupted while sleeping: Thrown by updateDisplay()
     */
    private void displayOLEDMessage() throws InterruptedException {
        arduino.updateDisplay("User was:" + ogState);
        if(ogState.equals("Happy")) {
            arduino.updateDisplay("User is probably having a good day.");
            if(random0to1 > 0.80) {
                arduino.updateDisplay("User has been affected by external stimuli.");
            }
            if (emotionUpdate.equals(ogState)) {
            arduino.updateDisplay("External stimuli had minimal effect on users emotions."); }
        }
        if (ogState.equals("Sad")) {
            arduino.updateDisplay("User is probably having a bad day.");
            if(random0to1 > 0.20) {
                arduino.updateDisplay("User has been affected by external stimuli.");
            }
            if (emotionUpdate.equals(ogState)) {
                arduino.updateDisplay("External stimuli had minimal effect on users emotions."); }
        }
        if (ogState.equals("Anxious")) {
            arduino.updateDisplay("User is probably nervous.");
            if(random0to1 > 0.70) {
                arduino.updateDisplay("User has been affected by external stimuli.");
            }
            if (emotionUpdate.equals(ogState)) {
                arduino.updateDisplay("External stimuli had minimal effect on users emotions."); }
        }
        if (ogState.equals("Angry")) {
            arduino.updateDisplay("User is probably in a perceived unstable environment.");
            if(random0to1 > 0.20) {
                arduino.updateDisplay("User has been affected by external stimuli.");
            }
            if (emotionUpdate.equals(ogState)) {
                arduino.updateDisplay("External stimuli had minimal effect on users emotions."); }
        }
        if (ogState.equals("Calm")) {
            arduino.updateDisplay("User is probably in a relaxing setting.");
            if(random0to1 > 0.70) {
                arduino.updateDisplay("User has been affected by external stimuli.");
            }
            if (emotionUpdate.equals(ogState)) {
                arduino.updateDisplay("External stimuli had minimal effect on users emotions."); }
        }
        arduino.updateDisplay("Final state: " + emotionUpdate);
    }

    /**
     * Scheduled Executor Service set up so the data collection runs for 10 seconds only if
     * the user has inputted a name, then the service repeats until termination. Name must be passed
     * into this method inorder for it to function.
     *
     * @param name Takes the name typed into main class
     */
    public void ScheduleService(String name) {
        service.scheduleAtFixedRate(() -> {
            if (!name.isEmpty()) {
                try {
                    updateArrays();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                elapsedTime++;

                if (elapsedTime >= 10) {
                    service.shutdown();
                    ///Debug checks if the states are correctly being passed
                    System.out.println("Debug: " + ogSens);
                    System.out.println("Debug: " + ogState);
                    ///Run methods
                    findReaValue();
                    calculateEmotion();
                    try {
                        displayOLEDMessage();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                System.out.println("No Name is Typed");
            }
        }
        , 0, 1, TimeUnit.SECONDS);
    }

    public void terminationControl() {
        /// Shut down hook runs when termination occurs.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                arduino.stopDisplay();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}

