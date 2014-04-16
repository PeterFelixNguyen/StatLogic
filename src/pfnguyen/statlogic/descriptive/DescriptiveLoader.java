/**
 * Copyright 2014 Peter "Felix" Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pfnguyen.statlogic.descriptive;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class that loads data from one or more input streams
 * for computation of descriptive statistics.
 * 
 * @author Peter "Felix" Nguyen
 */
public class DescriptiveLoader {
    // Calculators
    private Mean mean;
    private Quartiles quartile;
    private Mode mode;
    private Dispersion dispersion;
    private Extrema extrema;
    // Data
    private ArrayList<BigDecimal> values;
    private Scanner input;
    private PrintWriter output;
    private java.io.File inFile;
    private java.io.File outFile;
    // Visual Components
    private JTextArea outputArea;
    private JLabel statusBar;
    private StringBuilder outputString;
    // Boolean States
    private boolean isSaved = false;
    private boolean isLoaded = false;

    @SuppressWarnings("unused")
    private DescriptiveLoader() {
    }

    /**
     * Constructs a new DescriptiveLoader with the specified
     * JtaOutputArea and JlblStatusBar used for visual output.
     *
     * @param  jtaOutput  the output area this class can use
     * @param  statusBar  the status bar this class can use
     */
    public DescriptiveLoader(final JTextArea jtaOutput,
            final JLabel statusBar, final StringBuilder outputString) {
        outputArea = jtaOutput;
        this.statusBar = statusBar;
        this.outputString = outputString;
    }
    // I plan to turn JTextArea and JLabel into special classes
    // So that only these two classes could be passed into the
    // constructor.

    /**
     * Invokes JFileChooser and stores each filestream input into
     * an ArrayList. (Also: instantiates the calculators)
     * 
     * @throws  IOException  If an input or output exception occured
     */
    public void loadFileIntoArray() throws IOException {
        values = new ArrayList<BigDecimal>();

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = fileChooser.getSelectedFile();
            input = new Scanner(inFile);

            try {
                strictAddToArrayList();
                input.close();

                /* I should implement a seperate method */
                mean = new Mean(values);
                dispersion = new Dispersion(values, mean.getMean());
                quartile = new Quartiles(values);
                extrema = new Extrema(values);
                mode = new Mode(values);
                statusBar.setText(
                        " \"" + inFile.getName() + "\" loaded to program");
                buildString();
                writeToOutput();

                if (values.size() != 0) { // Need to make sure if I really need to check this condition
                    isLoaded = true;
                    isSaved = false;
                }
            }
            catch (InputMismatchException ex) {
                JOptionPane.showMessageDialog(null, "InputMismatchException, "
                        + "make sure the data set contains numbers only",
                        "Failure", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void stringToBigDecimalArray(String stringValues) {
        values = new ArrayList<BigDecimal>();

        String[] stringArray = stringValues.split("\\s+");

        try {
            for (int i = 0; i < stringArray.length; i++) {
                values.add(new BigDecimal(stringArray[i]));
            }

            mean = new Mean(values);
            dispersion = new Dispersion(values, mean.getMean());
            quartile = new Quartiles(values);
            extrema = new Extrema(values);
            mode = new Mode(values);

            statusBar.setText("Manual data entry successful");
            buildString();
            writeToOutput();

            if (values.size() != 0) { // Need to make sure if I really need to check this condition
                isLoaded = true;
                isSaved = false;
            }
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "NumberFormatException, "
                    + "make sure the data set contains numbers only",
                    "Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Adds each value into the ArrayList and
     * skips any values that cannot be added.
     */
    @SuppressWarnings("unused")
    private void addToArrayList() {
        if (input.hasNextBigDecimal()) {
            values.add(input.nextBigDecimal());
            addToArrayList();
        } else if (input.hasNext()) {
            input.next();
            addToArrayList();
        }
    }

    /**
     * Adds all values into the ArrayList until
     * end-of-file or an input mismatch occurs.
     *
     * @throws  InputMismatchException  If the input stream reads any value that
     *                                  cannot be translated into a BigDecimal
     */
    private void strictAddToArrayList() {
        while (input.hasNext()) {
            values.add(input.nextBigDecimal());
        }
    }

    /*
     * Must decide whether to keep this for "individual save"
     * or to discard this in favor of "save all"
     * 
     * 
     * 
     * 
     * 
     * 
     */
    /**
     * Saves results into a .txt file.
     *
     * @throws  IOException  If an input or output exception occured
     * @return  the boolean value true if file saved, otherwise false
     */
    public boolean saveToFile() throws IOException {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text Files", "txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(filter);

        if (isSaveable()) {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                outFile = fileChooser.getSelectedFile();
                if (outFile.exists()) {
                    final int option = JOptionPane.showConfirmDialog(null,
                            "File already exists, " + "do you want to replace file?",
                            "Retep's StatCalc", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (!(option == JOptionPane.YES_OPTION)) {
                        return false;
                    }
                }
                output = new PrintWriter(new BufferedWriter(new FileWriter(
                        outFile, false)));
                output.print(outputString.toString());
                output.close();
                statusBar.setText(" Saved to file \"" + outFile.getName()
                        + "\"");
                Desktop.getDesktop().open(getOutputFile());
                isSaved = true;
            }
            return true;
        } else
            return false;
    }

    /**
     * Builds the String used to display information in the JtaOutputArea
     * and/or the output save file.
     */
    private void buildString() {
        outputString.append("Date created: " + new java.util.Date() + "\n\n"
                + "Mean: " + mean.toString() + "\n"
                + "Median: " + quartile.getMedian() + "\n"
                + "Lower Quartile: " + quartile.getLowerQuartile() + "\n"
                + "Upper Quartile: "+ quartile.getUpperQuartile() + "\n"
                + "Mode: " + mode.toString2() + "\n"
                + "Sample Size: " + values.size() + "\n"
                + "Min: " + extrema.getMinima() + "\n"
                + "Max: " + extrema.getMaxima() + "\n"
                + "Sample Variance: " + dispersion.getSampleVariance() + "\n"
                + "Sample Standard Deviation: " + dispersion.getSampleStdDev() + "\n\n");
        printValues();
        outputString.append("\n\n" +
                "**************************************************************************************" +
                "\n");
    }

    /**
     * Sets the text of JtaOutputArea to display any calculations.
     */
    private void writeToOutput() {
        outputArea.setText(outputString.toString());
    }

    /**
     * Converts ArrayList of BigDecimal values into ArrayList of String values
     * (untested)
     * 
     * @return  the converted values
     */
    public ArrayList<String> convertToString() {
        ArrayList<String> stringValues = new ArrayList<String>();

        for (int i = 0; i < values.size(); i++) {
            stringValues.add(values.get(i).toString());
        }
        return stringValues;
    }

    /**
     * Appends the ArrayList of BigDecimal values to a String Builder.
     */
    private void printValues() {
        outputString.append("Sorted values: \n");
        for (int i = 0; i < values.size(); i++) {
            outputString.append(values.get(i).toString() + " ");
            if ((i + 1) % 5 == 0) {
                outputString.append("\n");
            }
        }
    }

    /**
     * @return  the size of the ArrayList of values loaded from a file
     */
    public int getArraySize() {
        return values.size();
    }

    /**
     * @return  the ArrayList of values loaded from a file
     */
    public ArrayList<BigDecimal> getValues() {
        return values;
    }

    /**
     * @return  the mean as a String
     */
    public String getStringOfMean() {
        return mean.toString();
    }

    /**
     * @return  the mean as a BigDecimal
     */
    public BigDecimal getMean() {
        return mean.getMean();
    }

    /**
     * @return  the quartile as a String
     */
    public String getStringOfMedian() {
        return quartile.toString();
    }

    /**
     * @return  the quartile as a BigDecimal
     */
    public BigDecimal getMedian() {
        return quartile.getMedian();
    }

    /**
     * @return  the data file loaded into the program
     */
    public java.io.File getDataFile() {
        return inFile;
    }

    /**
     * @return the output written into a file
     */
    public java.io.File getOutputFile() {
        return outFile;
    }

    /**
     * Returns the saveable state of the DescriptiveLoader.
     * 
     * @return  boolean to indicate useability of save methods
     */
    private boolean isSaveable() {
        if (!isSaved && isLoaded)
            return true;
        else
            return false;
    }
}