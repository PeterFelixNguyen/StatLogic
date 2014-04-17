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
package pfnguyen.statlogic.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pfnguyen.statlogic.zscore.ZScore;

/**
 * 
 * 
 * @author Peter "Felix" Nguyen
 */
@SuppressWarnings("serial")
public class ZScorePanel extends JPanel {
    private JLabel jlblRawScore = new JLabel("X");
    private JTextField jtfRawScore = new JTextField(6);
    private JLabel jlblPopulationMean = new JLabel("\u03BC");
    private JTextField jtfPopulationMean = new JTextField(6);
    private JLabel jlblStdDev = new JLabel("\u03C3");
    private JTextField jtfStdDev = new JTextField(6);
    private JLabel jlblZScore = new JLabel("Z = ?");
    private JLabel jlblPValue = new JLabel("P = ?");
    private JButton jbtOutput = new JButton("Output");
    private FlowPanel row1 = new FlowPanel();
    private FlowPanel row2 = new FlowPanel();
    private FlowPanel row3 = new FlowPanel();
    private FlowPanel row4 = new FlowPanel();
    private FlowPanel row5 = new FlowPanel();
    private FlowPanel row6 = new FlowPanel();
    // Values
    private double rawScore;
    private double populationMean;
    private double stdDev;
    private ZScore zScore;
    // Boolean flags
    private boolean isCalculated = false;

    public ZScorePanel(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        setBorder(new TitledBorder("Z-Score"));
        setLayout(new GridLayout(6, 1));
        jlblZScore.setToolTipText(jlblZScore.getText());
        jlblPValue.setToolTipText(jlblPValue.getText());
        add(row1);
        add(row2);
        add(row3);
        add(row4);
        add(row5);
        add(row6);
        row1.add(jlblRawScore);
        row1.add(jtfRawScore);
        row2.add(jlblPopulationMean);
        row2.add(jtfPopulationMean);
        row3.add(jlblStdDev);
        row3.add(jtfStdDev);
        row4.add(jlblZScore);
        row5.add(jlblPValue);
        row6.add(jbtOutput);

        jtfRawScore.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    rawScore = new Double(jtfRawScore.getText());
                    jtfRawScore.setBackground(Color.WHITE);
                    try {
                        populationMean = new Double(jtfPopulationMean.getText());
                        stdDev = new Double(jtfStdDev.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfRawScore.getText().length() != 0) {
                        jtfRawScore.setBackground(Color.RED);
                    }
                    else {
                        jtfRawScore.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    rawScore = new Double(jtfRawScore.getText());
                    jtfRawScore.setBackground(Color.WHITE);
                    try {
                        populationMean = new Double(jtfPopulationMean.getText());
                        stdDev = new Double(jtfStdDev.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfRawScore.getText().length() != 0) {
                        jtfRawScore.setBackground(Color.RED);
                    }
                    else {
                        jtfRawScore.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }
        });

        jtfPopulationMean.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    populationMean = new Double(jtfPopulationMean.getText());
                    jtfPopulationMean.setBackground(Color.WHITE);
                    try {
                        rawScore = new Double(jtfRawScore.getText());
                        stdDev = new Double(jtfStdDev.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfPopulationMean.getText().length() != 0) {
                        jtfPopulationMean.setBackground(Color.RED);
                    }
                    else {
                        jtfPopulationMean.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    populationMean = new Double(jtfPopulationMean.getText());
                    jtfPopulationMean.setBackground(Color.WHITE);
                    try {
                        rawScore = new Double(jtfRawScore.getText());
                        stdDev = new Double(jtfStdDev.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfPopulationMean.getText().length() != 0) {
                        jtfPopulationMean.setBackground(Color.RED);
                    }
                    else {
                        jtfPopulationMean.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }
        });

        jtfStdDev.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    stdDev = new Double(jtfStdDev.getText());
                    jtfStdDev.setBackground(Color.WHITE);
                    try {
                        rawScore = new Double(jtfRawScore.getText());
                        populationMean = new Double(jtfPopulationMean.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfStdDev.getText().length() != 0) {
                        jtfStdDev.setBackground(Color.RED);
                    }
                    else {
                        jtfStdDev.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    stdDev = new Double(jtfStdDev.getText());
                    jtfStdDev.setBackground(Color.WHITE);
                    try {
                        rawScore = new Double(jtfRawScore.getText());
                        populationMean = new Double(jtfPopulationMean.getText());
                        calculateZScore();
                    }
                    catch (NumberFormatException ex) {
                    }
                }
                catch (NumberFormatException ex) {
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                    if (jtfStdDev.getText().length() != 0) {
                        jtfStdDev.setBackground(Color.RED);
                    }
                    else {
                        jtfStdDev.setBackground(Color.WHITE);
                    }
                    isCalculated = false;
                }
            }
        });

        jbtOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculated) {
                    outputString.append("X = " + jtfRawScore.getText() + "\n" +
                            "\u03BC = " + jtfPopulationMean.getText() + "\n" +
                            "\u03C3 = " + jtfStdDev.getText() + "\n" +
                            jlblZScore.getText() + "\n" +
                            jlblPValue.getText() + "\n\n" +
                            "**************************************************************************************" +
                            "\n");
                    jtaOutput.setText(outputString.toString());
                }
            }
        });
    }

    /**
     * Calculates and outputs the Z-Score and P-Value
     */
    public void calculateZScore() {
        if (new Double(jtfStdDev.getText()) != 0) { // condition NOT required for DOUBLES
            zScore = new ZScore(rawScore, populationMean, stdDev);
            jlblZScore.setText("Z = " + String.valueOf(zScore.getZScore()));
            jlblPValue.setText("P = " + String.valueOf(zScore.getProbability()));
            isCalculated = true;
        }
    }
}
