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
    private Quartiles median;
    private Mode mode;
    @SuppressWarnings("unused")
    private Dispersion stdDev; // this class needs some work
    private Extrema extrema;
    // Data
    private ArrayList<BigDecimal> values = new ArrayList<BigDecimal>();
    private Scanner input;
    private PrintWriter output;
    private java.io.File inFile;
    private java.io.File outFile;
    private boolean useable;
    // Visual Components
    private JTextArea outputArea;
    private JLabel statusBar;

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
            final JLabel statusBar) {
        outputArea = jtaOutput;
        this.statusBar = statusBar;
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
    public void loadIntoArrayList() throws IOException {
        if (values.size() != 0) {
            values.clear();
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = fileChooser.getSelectedFile();
            input = new Scanner(inFile);

            addToArrayList();
            input.close();

            /* Calculators are instantiated here.
             * I should implement a seperate method */
            mean = new Mean(values);
            stdDev = new Dispersion(values, mean.getMean());
            median = new Quartiles(values); // this also sorts the ArrayList
            extrema = new Extrema(values); // works if the ArrayList is sorted
            mode = new Mode(values);
            statusBar.setText(
                    " \"" + inFile.getName() + "\" loaded to program");
        }
    }

    /**
     * Adds each value into the ArrayList and
     * skips any values that cannot be added.
     */
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
    @SuppressWarnings("unused")
    private void strictAddToArrayList() {
        try {
            while (input.hasNext()) {
                values.add(input.nextBigDecimal());
            }
        }
        catch (InputMismatchException ex) {
            JOptionPane.showMessageDialog(null, "Input mismatch, "
                    + "make sure the data set contains numbers only",
                    "Failure", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Saves results into a .txt file. (This class will be rewritten to
     * get rid of duplicate code)
     * 
     * @throws  IOException  If an input or output exception occured
     */
    public void saveToFile() throws IOException {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text Files", "txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(filter);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            outFile = fileChooser.getSelectedFile();
            if (!outFile.exists()) {
                output = new PrintWriter(new BufferedWriter(new FileWriter(
                        outFile, false)));
                output.println("Date created: " + new java.util.Date() + "\n");
                output.println("Mean: " + mean.toString());
                output.println("Median: " + median.getMedian());
                output.println("Mode: " + mode.toString());
                output.println("Sample Size: " + values.size());
                output.println("Min: " + extrema.getMinima());
                output.println("Max: " + extrema.getMaxima());
                output.println();
                printValues();
                output.close();
                statusBar.setText(" Saved to file \"" + outFile.getName()
                        + "\"");
                Desktop.getDesktop().open(getOutputFile());
                setUseable(false);
            }
            else {
                final int option = JOptionPane.showConfirmDialog(null,
                        "File already exists, " + "do you want to replace file?",
                        "Retep's StatCalc", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    output = new PrintWriter(new BufferedWriter(new FileWriter(
                            outFile, false)));
                    output.println("Date created: " + new java.util.Date()
                    + "\n");
                    output.println("Mean: " + mean.toString());
                    output.println("Median: " + median.getMedian());
                    output.println("Mode: " + mode.toString());
                    output.println("Sample Size: " + values.size());
                    output.println("Min: " + extrema.getMinima());
                    output.println("Max: " + extrema.getMaxima());
                    output.println();
                    printValues();
                    output.close();
                    statusBar.setText(" Saved to file \"" + outFile.getName()
                            + "\"");
                    Desktop.getDesktop().open(getOutputFile());
                    setUseable(false);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void writeFile() {
        // This is a helper method to prevent duplicate code
    }

    /**
     * Sets the text of JtaOutputArea to display any calculations.
     */
    public void writeToOutput() {
        outputArea.setText("Date created: " + new java.util.Date() + "\n\n"
                + "Mean: " + mean.toString() + "\n" + "Median: "
                + median.getMedian() + "\n" + "Mode: " + mode.toString() + "\n"
                + "Sample Size: " + values.size() + "\n" + "Min: "
                + extrema.getMinima() + "\n" + "Max: "
                + extrema.getMaxima() + "\n\n");
        printValuesToOutput();
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
     * Prints all values in ascending order. (Sort is not actually implemented)
     */
    public void printValues() {
        output.println("Sorted values: ");
        for (int i = 0; i < values.size(); i++) {
            output.print(values.get(i).toString() + " ");
            if ((i + 1) % 5 == 0) {
                output.println();
            }
        }
    }

    /**
     * Prints all values in ascending order to JtaOutputArea.
     * (Sort is not actually implemented)
     */
    public void printValuesToOutput() {
        outputArea.append("Sorted values: \n");
        for (int i = 0; i < values.size(); i++) {
            outputArea.append(values.get(i).toString() + " ");
            if ((i + 1) % 5 == 0) {
                outputArea.append("\n");
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
     * @return  the median as a String
     */
    public String getStringOfMedian() {
        return median.toString();
    }

    /**
     * @return  the median as a BigDecimal
     */
    public BigDecimal getMedian() {
        return median.getMedian();
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
     * Sets the useable state of the DescriptiveLoader.
     * (Possible reimplementation)
     * 
     * @param  useable  the useable state for related components
     */
    @Deprecated
    public void setUseable(boolean useable) {
        this.useable = useable;
    }

    /**
     * Returns the useable state of the DescriptiveLoader.
     * (Possible reimplementation)
     * 
     * @return  boolean to indicate useability of related components
     */
    @Deprecated
    public boolean isUseable() {
        return useable;
    }
}