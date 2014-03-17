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
//import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;
import pfnguyen.statlogic.options.CalculatorOptions.Option;


public class TLoader {
    /** Calculators */
    private OneSampleTTest oneSampleTTest;
    private Option option = Option.NONE;
    /** Data */
    private ArrayList<BigDecimal> arrayValues = new ArrayList<BigDecimal>();
    private Scanner input;
    // private PrintWriter output;
    private java.io.File inFile;
    // private java.io.File outFile;
    /** Reference variables to display information */
    private JTextArea outputArea;
    private JLabel statusBar;

    public TLoader(final JTextArea jtaOutput, final JLabel statusBar) {
        outputArea = jtaOutput;
        this.statusBar = statusBar;
    }

    /** Load values from .txt file for calculation */
    public void loadFileIntoArray(Hypothesis hypothesis,
            BigDecimal testValue, double significance)
                    throws IOException {
        /* If data previously loaded, clear arrayValues */
        if (arrayValues.size() != 0) {
            arrayValues.clear(); // Required to make program reusable
        }

        /* Inputs data from file and assign to arrayValues */
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = fileChooser.getSelectedFile();

            input = new Scanner(inFile);

            addToArray();

            input.close();

            oneSampleTTest = new OneSampleTTest(hypothesis, testValue,
                    arrayValues, significance);

            writeToOutput();

            statusBar
            .setText(" \"" + inFile.getName() + "\" loaded to program");
        }
    }

    public void addToArray() {
        if (input.hasNextBigDecimal()) {
            arrayValues.add(input.nextBigDecimal());
            addToArray();
        } else if (input.hasNext()) {
            input.next();
            addToArray();
        }
    }

    /** Print results to jtaOutput window */
    public void writeToOutput() {
        outputArea.setText("Date created: " + new java.util.Date() + "\n\n"
                + oneSampleTTest.testHypothesis());
        if (this.option == Option.CONFIDENCE_INTERVAl) {
            //outputArea.append("\n" + oneSampleCI.confidenceInterval());
        }
    }

    public void loadXIntoCalc(Hypothesis hAlternative, BigDecimal testValue,
            BigDecimal xBar, BigDecimal stdDev, int n, double significance,
            Option option) {
        oneSampleTTest = new OneSampleTTest(hAlternative, testValue, xBar,
                stdDev, n, significance);

        this.option =  option;

        if (this.option == Option.CONFIDENCE_INTERVAl) {
            //oneSampleCI = new ZConfidenceInterval(xBar, stdDev, n, significance);
        }

        writeToOutput();
    }

    public void loadTextIntoArray() {

    }

    public void stringToBigDecimalArray(String stringValues) {

    }
}
