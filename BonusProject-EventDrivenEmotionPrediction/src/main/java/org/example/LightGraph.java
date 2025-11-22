package org.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.List;

/**
 * Class for graphing data related to light and sound sensor values, graph shows raw analog values for both light and sound sensors.
 * The graph also contains the average value of both sensor and light values. This classes functions depends on if the array lists
 * actually update within SensoryArduino. This graph will show once the data collection fully runs for 10 seconds in main. This class
 * used the javaFx library to graph.
 *
 * How to read graphs: If graphs don't show results based on thresholds, that indicates other factors contributing to the results seen
 * such as probabilities.
 */
public class LightGraph extends Application{
    /// Defining Sensory Arduino object
    private static SensoryArduino sense;

    /// Setter method to pass existing object of SensoryArduino into this class
    public static void setSensoryArduino(SensoryArduino currentList) {
        sense = currentList;
    }

    /**Abstract method native to javafx. This method when called
     * is used to carry out the function of the UI. Specifically, it initializes
     * UI components.
     *
     * @param stage defines a stage or an instance of the UI itself.
     */
    @Override
    public void start(Stage stage) {

        /// New lists that get the copy of the created lists in SensoryArduino
        List <Integer> lightList = sense.getlightList();
        List <Integer> soundList = sense.getsoundList();

        ///Initializing X and Y axis.
        NumberAxis X = new NumberAxis();
        NumberAxis Y = new NumberAxis();
        X.setLabel("Time (10 Seconds)");
        Y.setLabel("Light and Sound Data");

        /// Defining a line chart.
        LineChart<Number, Number> lineChart = new LineChart<>(X, Y);
        lineChart.setTitle("Light Data Over Time for: " + sense.getOgState() + " to " + sense.getCurrentState() + " Transition");

        /// Series to hold light data
        XYChart.Series<Number, Number> lightseries = new XYChart.Series<>();
        lightseries.setName("Light Data");

        /// Series to hold sound data
        XYChart.Series<Number, Number> soundseries = new XYChart.Series<>();
        soundseries.setName("Sound Data");

        /// Series to hold avgSound data
        XYChart.Series<Number, Number> avgsoundseries = new XYChart.Series<>();
        avgsoundseries.setName("Avg Sound");

        /// Series to gold avgLight data
        XYChart.Series<Number, Number> avglightseries = new XYChart.Series<>();
        avglightseries.setName("Avg Light");

        /// Adding points to the graph for all the series
        for(int j = 0; j < lightList.size(); j++) {
            lightseries.getData().add(new XYChart.Data<>(j, lightList.get(j)));
            avglightseries.getData().add(new XYChart.Data<>(j , sense.getAvgLight()));

        }
        for(int j = 0; j < soundList.size(); j++) {
            soundseries.getData().add(new XYChart.Data<>(j, soundList.get(j)));
            avgsoundseries.getData().add(new XYChart.Data<>(j, sense.getAvgSound()));
        }

        ///Adds series to the graph.
        lineChart.getData().add(lightseries);
        lineChart.getData().add(soundseries);
        lineChart.getData().add(avgsoundseries);
        lineChart.getData().add(avglightseries);


        /// Setting up the scene for the lineChart: Defining the bounds, scene, stage title, and calling the graph to display.
        Scene scene = new Scene(lineChart, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Light Data");
        stage.show();
    }

    /** Main class to launch the graph after the program has run for 60 seconds,
     * the specific instance of the UI is called in the MAIN class.
     *
     * @param args Command line arguments. Native to main.
     */
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000);
        launch(args);
    }
}

