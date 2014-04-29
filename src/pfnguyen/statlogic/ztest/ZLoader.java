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
package pfnguyen.statlogic.ztest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;
import pfnguyen.statlogic.options.CalculatorOptions.Option;


public class ZLoader {
    // Calculators
    private OneSampleZTest oneSampleZTest;
    private ZConfidenceInterval oneSampleCI;
    private Option option = Option.NONE;
    // Data
    private ArrayList<Double> values = new ArrayList<Double>();
    private Scanner input;
    private java.io.File inFile;
    // Visual Components
    private JTextArea outputArea;
    private JLabel statusBar;
    private StringBuilder outputString;

    public ZLoader(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        outputArea = jtaOutput;
        this.statusBar = statusBar;
        this.outputString = outputString;
    }

    public void loadXIntoCalc(Hypothesis hAlternative, double testValue,
            double xBar, double stdDev, int n, double significance,
            Option option) {

        this.option = option;

        if (option == Option.TEST_HYPOTHESIS) {
            oneSampleZTest = new OneSampleZTest(hAlternative, testValue, xBar,
                    stdDev, n, significance);
        }
        if (option == Option.CONFIDENCE_INTERVAl) {
            oneSampleCI = new ZConfidenceInterval(xBar, stdDev, n, significance);
        }
        buildString();
        writeToOutput();
    }

    public void stringToDoubleArray(String stringValues, Hypothesis hAlternative,
            double testValue, double stdDev, double significance, Option option) {
        String[] stringArray = stringValues.split("\\s+");
        values = new ArrayList<Double>();

        this.option = option;

        for (int i = 0; i < stringArray.length; i++) {
            values.add(new Double(stringArray[i]));
        }

        oneSampleZTest = new OneSampleZTest(hAlternative, testValue,
                values, stdDev, significance);

        // move into if statement
        double xBar = new Double(oneSampleZTest.getXBar());
        int n = new Integer(oneSampleZTest.getN());

        if (option == Option.CONFIDENCE_INTERVAl) {
            oneSampleCI = new ZConfidenceInterval(xBar, stdDev, n, significance);
        }

        buildString();
        writeToOutput();
    }

    /** Load values from .txt file for calculation */
    public void loadFileIntoArray(Hypothesis hypothesis,
            double testValue, double stdDev,
            double significance, Option option) throws IOException {
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
            oneSampleZTest = new OneSampleZTest(hypothesis, testValue,
                    values, stdDev, significance);
            //}
            if (option == Option.CONFIDENCE_INTERVAl ||
                    option == Option.BOTH) {
                double xBar = new Double(oneSampleZTest.getXBar());
                int n = new Integer(oneSampleZTest.getN());
                oneSampleCI = new ZConfidenceInterval(xBar, stdDev, n, significance);
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
                    + "Test Hypothesis for One sample Z-test" + "\n"
                    + oneSampleZTest.getNullHypothesis() + "\n"
                    + oneSampleZTest.getAltHypothesis() + "\n" + "\n"
                    + "xbar = " + oneSampleZTest.getXBar() + "    n = " + oneSampleZTest.getN() + "\n"
                    + "sigma = " + oneSampleZTest.getSigma() + "    alpha = " + oneSampleZTest.getAlpha() + "\n" + "\n"
                    + "Critical Value = " + oneSampleZTest.getCriticalRegionAsString() + "    Test Statistic = "
                    + oneSampleZTest.getTestStatistics() + "\n"
                    + oneSampleZTest.getConclusionAsString() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
        }
        else if (option == Option.CONFIDENCE_INTERVAl) {
            outputString.append("Confidence Interval for One sample Z-test" + "\n"
                    + oneSampleCI.confidenceInterval() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
        }
        else if (option == Option.BOTH) {
            outputString.append("Date created: " + new java.util.Date() + "\n\n"
                    + "Test Hypothesis for One sample Z-test" + "\n"
                    + oneSampleZTest.getNullHypothesis() + "\n"
                    + oneSampleZTest.getAltHypothesis() + "\n" + "\n"
                    + "xbar = " + oneSampleZTest.getXBar() + "    n = " + oneSampleZTest.getN() + "\n"
                    + "sigma = " + oneSampleZTest.getSigma() + "    alpha = " + oneSampleZTest.getAlpha() + "\n" + "\n"
                    + "Critical Value = " + oneSampleZTest.getCriticalRegionAsString() + "    Test Statistic = "
                    + oneSampleZTest.getTestStatistics() + "\n"
                    + oneSampleZTest.getConclusionAsString() + "\n\n" +
                    "**************************************************************************************" +
                    "\n");
            outputString.append("Confidence Interval for One sample Z-test" + "\n"
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
