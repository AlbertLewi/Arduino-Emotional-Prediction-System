package org.example;
import org.firmata4j.firmata.*;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import java.io.IOException;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;

/**This class is for managing connections to analog pins 20 and 16,
 * as well as managing connections to the OLED display.
 *
 */
public class ArduinoConnection {
    /// Defining arduino board IODevice object
    private final IODevice myGroveBoard;
    /// Defining SSD1306 object
    private SSD1306 theOledObject;
    /// Light sensor value
    private long lightSensorValue = 0;
    /// Sound sensor value
    private long soundSensorValue = 0;

    
    /**Initializing the board and the OLED display.
     *
     * @param Port Takes in the COM port that the board is connected to.
     */
    protected ArduinoConnection(String Port) {

        /// Taken from in class labs
        myGroveBoard = new FirmataDevice(Port);

        // try to communicate with the board
        try {
            myGroveBoard.start();
            myGroveBoard.ensureInitializationIsDone();
            System.out.println("Board started.");

            I2CDevice i2cObject = this.myGroveBoard.getI2CDevice((byte) 0x3C); // Use 0x3C for the Grove OLED
            this.theOledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64); // 128x64 OLED SSD1515
            this.theOledObject.init();

        } catch (Exception ex) {
            System.out.println("couldn't connect to board.");
        }

    }

    /**Method that gets the pins associated with the light sensor and the sound sensor
     *
     * @throws IOException Indicates an issue with board communication; thrown by setMode()
     */
    protected void getPins() throws IOException {
    //If you get range error again you have to re-upload the standard firmata IDE package on the board
        var lightSensor = myGroveBoard.getPin(20);
        lightSensor.setMode(Pin.Mode.ANALOG);

        var soundSensor = myGroveBoard.getPin(16);
        soundSensor.setMode(Pin.Mode.ANALOG);

        soundSensorValue = soundSensor.getValue();
        lightSensorValue = lightSensor.getValue();

        try {
            System.out.println("Light Sensor Value: " + lightSensorValue);
            System.out.println("Sound Sensor Value: " + soundSensorValue);
        } catch (Exception ex) {
            System.out.println("couldn't read values");
        }
    }



    /** Getter method
     *
     * @return Light sensor reading
     */
    public long getLightSensorValue() {
        return lightSensorValue;
    }

    /** Getter method
     *
     * @return Sound sensor reading
     */
    public long getSoundSensorValue() {
        return soundSensorValue;
    }

    /** Method to update the display and output a message.
     *
     * @param message String message that can be manually inputted when method is called.
     * @throws InterruptedException Thrown if thread.sleep() is interrupted while sleeping
     */
    protected synchronized void updateDisplay(String message) throws InterruptedException {
        theOledObject.getCanvas().clear(); // Clear the display
        theOledObject.getCanvas().setTextsize(1); //Font size 2
        int width = theOledObject.getCanvas().getWidth();
        int height = theOledObject.getCanvas().getHeight();
        /// Setting the coordinates of where message will be displayed on OLED.
        int X = width/10;
        int Y = height/8;
        theOledObject.getCanvas().setCursor(X, Y);
        /// Writing and displaying message then waiting 2 seconds.
        theOledObject.getCanvas().write(message);
        theOledObject.display();
        Thread.sleep(3000);
    }

    /**Method to stop the display if the OLED object exists. Used in the class PlantLogic
     * in the method terminationControl.
     *
     * @throws IOException Indicates an issue with board communication: Thrown by stop()
     */
    public void stopDisplay() throws IOException {
        theOledObject.turnOff();
        myGroveBoard.stop();
    }

    /**Main class to individually test sensors for calibration
     *
     * @param args Command line arguments. Native to main.
     * @throws IOException Indicates an issue with board communication: Thrown by getPins()
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ArduinoConnection aTest = new ArduinoConnection("COM4");
        aTest.getPins();
        aTest.updateDisplay("SAD");
    }

}





