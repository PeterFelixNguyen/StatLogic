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
package pfnguyen.statlogic.ttest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;
import pfnguyen.statlogic.options.CalculatorOptions.Option;


public class TLoader {
    /* Calculators */
    private OneSampleTTest oneSampleTTest;
    private TConfidenceInterval oneSampleCI;
    private Option option = Option.NONE;
    /* Data */
    private ArrayList<Double> values;
    private Scanner input;
    private java.io.File inFile;
    /* Visual Components */
    private JTextArea outputArea;
    private JLabel statusBar;
    private StringBuilder outputString;

    public TLoader(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        outputArea = jtaOutput;
        this.statusBar = statusBar;
        this.outputString = outputString;
    }

    public void loadXIntoCalc(Hypothesis hAlternative, double testValue,
            double xBar, double stdDev, int n, double significance,
            Option option) {

        this.option = option;

        if (option == Option.TEST_HYPOTHESIS) {
            oneSampleTTest = new OneSampleTTest(hAlternative, testValue, xBar,
                    stdDev, n, significance);
        }
        if (option == Option.CONFIDENCE_INTERVAl) {
            oneSampleCI = new TConfidenceInterval(xBar, stdDev, n, significance);
        }
        buildString();
        writeToOutput();
    }

    public void stringToDoubleArray(String stringValues, Hypothesis hAlternative,
            double testValue, double significance, Option option) {
        String[] stringArray = stringValues.split("\\s+");
        values = new ArrayList<Double>();

        this.option = option;

        for (int i = 0; i < stringArray.length; i++) {
            values.add(new Double(stringArray[i]));
        }

        oneSampleTTest = new OneSampleTTest(hAlternative, testValue,
                values, significance);
        // move into if statement
        double xBar = new Double(oneSampleTTest.getXBar());
        double stdDev = new Double(oneSampleTTest.getSigma());
        int n = new Integer(oneSampleTTest.getN());

        if (option == Option.CONFIDENCE_INTERVAl) {
            oneSampleCI = new TConfidenceInterval(xBar, stdDev, n, significance);
        }

        buildString();
        writeToOutput();
    }

    /** Load values from .txt file for calculation */
    public void loadFileIntoArray(Hypothesis hypothesis,
            double testValue, double significance, Option option)
                    throws IOException {
        values = new ArrayList<Double>();

        this.option = option;

        /* Inputs data from file and assign to values */
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = fileChooser.getSelectedFile();

            input = new Scanner(inFile);

            addToArray();

            input.close();

            //if (option == Option.TEST_HYPOTHESIS) {
            oneSampleTTest = new OneSampleTTest(hypothesis, testValue,
                    values, significance);
            //}
            if (option == Option.CONFIDENCE_INTERVAl ||
                    option == Option.BOTH) { // really need to fix this
                double xBar = new Double(oneSampleTTest.getXBar());
                double stdDev = new Double(oneSampleTTest.getSigma());
                int n = new Integer(oneSampleTTest.getN());
                oneSampleCI = new TConfidenceInterval(xBar, stdDev, n, significance);
            }
            buildString();
            writeToOutput();

            statusBar
            .setText(" \"" + inFile.getName() + "\" loaded to program");
        }
    }

    private void addToArray() {
        if (input.hasNextDouble()) {
            values.add(input.nextDouble());
            addToArray();
        } else if (input.hasNext()) {
            input.next();
            addToArray();
        }
    }

    /**
     * Builds the String used to display information in the JtaOutputArea
     * and/or the output save file.
     */
    private void buildString() {
        if (option == Option.TEST_HYPOTHESIS) {
            outputString.append("Date created: " + new java.util.Date() + "\n\n"
                    + "Test Hypothesis for One sample Student's t-test" + "\n"
                    + oneSampleTTest.getNullHypothesis() + "\n"
                    + oneSampleTTest.getAltHypothesis() + "\n" + "\n"
                    + "xbar = " + oneSampleTTest.getXBar() + "    n = " + oneSampleTTest.getN() + "\n"
                    + "sigma = " + oneSampleTTest.getSigma() + "    alpha = " + oneSampleTTest.getAlpha() + "\n" + "\n"
                    + "Critical Value = " + oneSampleTTest.getCriticalRegionAsString() + "    Test Statistic = "
                    + oneSampleTTest.getTestStatistics() + "\n"
                    + oneSampleTTest.getConclusionAsString() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
        }
        else if (option == Option.CONFIDENCE_INTERVAl) {
            outputString.append("Confidence Interval for One sample Student's t-test" + "\n"
                    + oneSampleCI.confidenceInterval() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
        }
        else if (option == Option.BOTH) {
            outputString.append("Date created: " + new java.util.Date() + "\n\n"
                    + "Test Hypothesis for One sample Student's t-test" + "\n"
                    + oneSampleTTest.getNullHypothesis() + "\n"
                    + oneSampleTTest.getAltHypothesis() + "\n" + "\n"
                    + "xbar = " + oneSampleTTest.getXBar() + "    n = " + oneSampleTTest.getN() + "\n"
                    + "sigma = " + oneSampleTTest.getSigma() + "    alpha = " + oneSampleTTest.getAlpha() + "\n" + "\n"
                    + "Critical Value = " + oneSampleTTest.getCriticalRegionAsString() + "    Test Statistic = "
                    + oneSampleTTest.getTestStatistics() + "\n"
                    + oneSampleTTest.getConclusionAsString() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
            outputString.append("Confidence Interval for One sample Student's t-test" + "\n"
                    + oneSampleCI.confidenceInterval() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
        }
    }

    /** Print results to jtaOutput window */
    public void writeToOutput() {
        outputArea.setText(outputString.toString());
    }
}
