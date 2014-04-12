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

import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pfnguyen.statlogic.zscore.ZScore;

@SuppressWarnings("serial")
public class ZScorePanel extends JPanel {
    private JLabel jlblXBar = new JLabel("X\u0305");
    private JTextField jtfXBar = new JTextField(6);
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
    //private JPanel layoutContainer = new JPanel(new GridLayout(5, 1)); // This layout is for style
    // Values
    private double xBar;
    private double populationMean;
    private double stdDev;
    private ZScore zScore;

    public ZScorePanel(final JTextArea jtaOutput, final JLabel statusBar) {
        setBorder(new TitledBorder("Z-Score"));
        //setLayout(new FlowLayout(FlowLayout.LEADING)); // This Layout is for style
        setLayout(new GridLayout(5, 1));
        jlblZScore.setToolTipText(jlblZScore.getText());
        jlblPValue.setToolTipText(jlblPValue.getText());
        //add(layoutContainer);
        /*layoutContainer.*/add(row1);
        /*layoutContainer.*/add(row2);
        /*layoutContainer.*/add(row3);
        /*layoutContainer.*/add(row4);
        /*layoutContainer.*/add(row5);
        row1.add(jlblXBar);
        row1.add(jtfXBar);
        row1.add(jlblPopulationMean);
        row1.add(jtfPopulationMean);
        row2.add(jlblStdDev);
        row2.add(jtfStdDev);
        row3.add(jlblZScore);
        row4.add(jlblPValue);
        row5.add(jbtOutput);
        setMaximumSize(new Dimension(190, 185));
        setMinimumSize(new Dimension(190, 185));
        System.out.println(getPreferredSize()); // So I can adjust size accordinly

        jtfXBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    xBar = new Double(jtfXBar.getText());
                    System.out.println("(1) changed, new xBar value " + xBar);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("1 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    xBar = new Double(jtfXBar.getText());
                    System.out.println("(2) removed, new xBar value " + xBar);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("1 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    xBar = new Double(jtfXBar.getText());
                    System.out.println("(3) inserted, new xBar value " + xBar);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("1 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }
        });

        jtfPopulationMean.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    populationMean = new Double(jtfPopulationMean.getText());
                    System.out.println("(1) changed, new populationMean value " + populationMean);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("2 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    populationMean = new Double(jtfPopulationMean.getText());
                    System.out.println("(2) removed, new populationMean value " + populationMean);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("2 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    populationMean = new Double(jtfPopulationMean.getText());
                    System.out.println("(3) inserted, new populationMean value " + populationMean);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("2 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }
        });

        jtfStdDev.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    stdDev = new Double(jtfStdDev.getText());
                    System.out.println("(1) changed, new stdDev value " + stdDev);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("3 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    stdDev = new Double(jtfStdDev.getText());
                    System.out.println("(2) removed, new stdDev value " + stdDev);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("3 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    stdDev = new Double(jtfStdDev.getText());
                    System.out.println("(3) inserted, new stdDev value " + stdDev);
                    calculateZScore();
                }
                catch (NumberFormatException ex) {
                    System.out.println("3 Not A Valid number");
                    jlblZScore.setText(String.valueOf("Z = ?"));
                    jlblPValue.setText(String.valueOf("P = ?"));
                }
            }
        });
    }

    private boolean emptyFields() { // Not necessary
        if (jtfXBar.getText().length() == 0 &&
                jtfPopulationMean.getText().length() == 0 &&
                jtfStdDev.getText().length() == 0)
            return true;
        else
            return false;
    }

    public void calculateZScore() {
        if (new Double(jtfStdDev.getText()) != 0 && !emptyFields()) {
            zScore = new ZScore(xBar, populationMean, stdDev);
            jlblZScore.setText("Z = " + String.valueOf(zScore.getZScore()));
            jlblPValue.setText("P = " + String.valueOf(zScore.getProbability()));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(190, 185);
    }
}
