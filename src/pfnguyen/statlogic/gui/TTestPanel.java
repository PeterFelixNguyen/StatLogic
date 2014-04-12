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

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;
import pfnguyen.statlogic.options.CalculatorOptions.Option;
import pfnguyen.statlogic.ttest.TLoader;

@SuppressWarnings("serial")
public class TTestPanel extends JPanel {
    // Primary components
    private JLabel h0 = new JLabel("H0: \u03BC = ?    ");
    private JLabel h1 = new JLabel("H1: \u03BC \u2260 ?    ");
    private JRadioButton lowerTail = new JRadioButton("Lower Tail");
    private JRadioButton upperTail = new JRadioButton("Upper Tail");
    private JRadioButton twoTail = new JRadioButton("Two Tail");
    private JRadioButton provideXBar = new JRadioButton(
            "Provide " + "X\u0305" + ", \u03C3\u0302");
    private JRadioButton calculateXBar = new JRadioButton(
            "Calculate " + "X\u0305"  + ", \u03C3\u0302");
    private JRadioButton importXBar = new JRadioButton(
            "Calculate " + "X\u0305" + ", \u03C3\u0302" + " from File");
    private JTextField jtfTestValue = new JTextField(4);
    private JTextField alpha = new JTextField(4);
    private JButton jbtCalculate = new JButton("Calculate");
    private JCheckBox jchkConfidenceInterval = new JCheckBox(
            "Confidence Interval");
    // Button groups
    private ButtonGroup tailBtnGroup = new ButtonGroup();
    private ButtonGroup xBarBtnGroup = new ButtonGroup();
    // Rows for layout
    private FlowPanel row1 = new FlowPanel();
    private FlowPanel row2 = new FlowPanel();
    private FlowPanel row3 = new FlowPanel();
    private FlowPanel row4 = new FlowPanel();
    private FlowPanel row5 = new FlowPanel();
    // Calculator
    private TLoader loader;
    // Input for Method 3
    private Hypothesis hypothesis = Hypothesis.NOT_EQUAL;
    private BigDecimal testValue;
    private BigDecimal stdDev;
    private double significance;
    // Input for Method 1
    private BigDecimal xBar;
    private int sampleSize;
    // Layout Container
    private JPanel layoutContainer = new JPanel(new GridLayout(5, 1));

    public TTestPanel(final JTextArea jtaOutput, final JLabel statusBar) {
        loader = new TLoader(jtaOutput, statusBar);
        // Styling
        setBorder(new TitledBorder("1-Sample T-Test"));
        setLayout(new FlowLayout(FlowLayout.LEADING));
        // Components
        add(layoutContainer);
        layoutContainer.add(row1);
        layoutContainer.add(row2);
        layoutContainer.add(row3);
        layoutContainer.add(row4);
        layoutContainer.add(row5);
        row1.add(h0);
        row1.add(h1);
        row2.add(lowerTail);
        row2.add(upperTail);
        row2.add(twoTail);
        row3.add(provideXBar);
        row3.add(calculateXBar);
        row3.add(importXBar);
        row4.add(new JLabel("Test Value"));
        row4.add(jtfTestValue);
        row4.add(new JLabel("alpha \u03b1"));
        row4.add(alpha);
        row5.add(jbtCalculate);
        row5.add(jchkConfidenceInterval);
        // Component configuration
        twoTail.setSelected(true);
        calculateXBar.setSelected(true);
        // Grouping
        tailBtnGroup.add(lowerTail);
        tailBtnGroup.add(upperTail);
        tailBtnGroup.add(twoTail);
        xBarBtnGroup.add(provideXBar);
        xBarBtnGroup.add(calculateXBar);
        xBarBtnGroup.add(importXBar);
        // Listeners
        lowerTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLowerTail();
                hypothesis = Hypothesis.LESS_THAN;
            }
        });
        upperTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpperTail();
                hypothesis = Hypothesis.GREATER_THAN;
            }
        });
        twoTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hypothesis = Hypothesis.NOT_EQUAL;
                setTwoTail();
            }
        });

        // Listen for changes in the text
        jtfTestValue.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setTail();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setTail();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setTail();
            }
        });
        jbtCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ((new Double(alpha.getText()) > 0.0)
                            && (new Double(alpha.getText()) < 1.0)) {
                        if (provideXBar.isSelected()) {
                            JTextField jtfXBar = new JTextField();
                            JTextField jtfSampleSize = new JTextField();
                            JTextField jtfStdDev = new JTextField();

                            if (xBar != null) {
                                jtfXBar.setText(xBar.toString());
                            }

                            // Should provide error handling is sampleSize <= 1
                            jtfSampleSize.setText(Integer.toString(sampleSize));
                            if (jtfSampleSize.getText().contains("0")) {
                                jtfSampleSize.setText("");
                            }
                            else {
                                jtfSampleSize.setText(Integer.toString(sampleSize));
                            }

                            if (stdDev != null) {
                                jtfStdDev.setText(stdDev.toString());
                            }

                            Object[] message = { "X\u0305: ", jtfXBar,
                                    "Sample Size: ", jtfSampleSize,
                                    "\u03C3\u0302", jtfStdDev};

                            int selected = JOptionPane.showConfirmDialog(
                                    jbtCalculate.getParent(), message,
                                    "Sample Mean", JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);

                            if (selected != JOptionPane.CANCEL_OPTION) {
                                testValue = new BigDecimal(jtfTestValue.getText());
                                significance = new Double(alpha.getText());

                                xBar = new BigDecimal(jtfXBar.getText());
                                sampleSize = new Integer(jtfSampleSize.getText());
                                stdDev = new BigDecimal(jtfStdDev.getText());

                                if (!jchkConfidenceInterval.isSelected()) {
                                    loader.loadXIntoCalc(hypothesis, testValue,
                                            xBar, stdDev, sampleSize, significance,
                                            Option.TEST_HYPOTHESIS);
                                } else {
                                    loader.loadXIntoCalc(hypothesis, testValue,
                                            xBar, stdDev, sampleSize, significance,
                                            Option.CONFIDENCE_INTERVAl);
                                }
                            }
                        } else if (calculateXBar.isSelected()) {
                            JTextArea jtaValues = new JTextArea(20, 20);
                            jtaValues.setLineWrap(true);
                            jtaValues.setWrapStyleWord(true);

                            BoxPanel calcXBarPanel = new BoxPanel();
                            calcXBarPanel.add(new JLabel(
                                    "Enter values to calculate Sample Mean"));
                            calcXBarPanel.add(new JScrollPane(jtaValues));
                            // first parameter should be null?
                            int selected = JOptionPane.showConfirmDialog(
                                    jbtCalculate.getParent(), calcXBarPanel,
                                    "Options", JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);

                            if (selected != JOptionPane.CANCEL_OPTION) {
                                testValue = new BigDecimal(jtfTestValue.getText());
                                significance = new Double(alpha.getText());
                                loader.stringToBigDecimalArray(jtaValues.getText(), hypothesis, testValue, significance);
                                // gotta change from public access to private << it's been awhile, i forgot what this means
                            }

                        } else if (importXBar.isSelected()) {
                            try {
                                testValue = new BigDecimal(jtfTestValue.getText());
                                significance = new Double(alpha.getText());
                                loader.loadFileIntoArray(hypothesis, testValue, significance);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Signifiance level must be between 0 and 1 (exclusive)");
                    }
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Significance level is not a number");
                }
            }
        });
    }

    private void setTail() {
        if (lowerTail.isSelected())
            setLowerTail();
        else if (upperTail.isSelected())
            setUpperTail();
        else
            setTwoTail();
    }

    private void setLowerTail() {
        if (jtfTestValue.getText().length() == 0) {
            h0.setText("H0: \u03BC = ?    ");
            h1.setText("H1: \u03BC < ?");
            h0.setToolTipText(h0.getText());
            h1.setToolTipText(h1.getText());
        } else if (jtfTestValue.getText().length() < 10) {
            h0.setText("H0: \u03BC = " + jtfTestValue.getText() + "    ");
            h1.setText("H1: \u03BC < " + jtfTestValue.getText());
            h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
            h1.setToolTipText(h1.getText());
        } else {
            try {
                h0.setText("H0: \u03BC = " + jtfTestValue.getText(0, 9)
                        + "...    ");
                h1.setText("H1: \u03BC < " + jtfTestValue.getText(0, 9) + "...");
                h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
                h1.setToolTipText("H1: \u03BC < " + jtfTestValue.getText());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setUpperTail() {
        if (jtfTestValue.getText().length() == 0) {
            h0.setText("H0: \u03BC = ?    ");
            h1.setText("H1: \u03BC > ?");
            h0.setToolTipText(h0.getText());
            h1.setToolTipText(h1.getText());
        } else if (jtfTestValue.getText().length() < 10) {
            h0.setText("H0: \u03BC = " + jtfTestValue.getText() + "    ");
            h1.setText("H1: \u03BC > " + jtfTestValue.getText());
            h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
            h1.setToolTipText(h1.getText());
        } else {
            try {
                h0.setText("H0: \u03BC = " + jtfTestValue.getText(0, 9)
                        + "...    ");
                h1.setText("H1: \u03BC > " + jtfTestValue.getText(0, 9) + "...");
                h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
                h1.setToolTipText("H1: \u03BC > " + jtfTestValue.getText());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setTwoTail() {
        if (jtfTestValue.getText().length() == 0) {
            h0.setText("H0: \u03BC = ?    ");
            h1.setText("H1: \u03BC \u2260 ?");
            h0.setToolTipText(h0.getText());
            h1.setToolTipText(h1.getText());
        } else if (jtfTestValue.getText().length() < 10) {
            h0.setText("H0: \u03BC = " + jtfTestValue.getText() + "    ");
            h1.setText("H1: \u03BC \u2260 " + jtfTestValue.getText());
            h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
            h1.setToolTipText(h1.getText());
        } else {
            try {
                h0.setText("H0: \u03BC = " + jtfTestValue.getText(0, 9)
                        + "...    ");
                h1.setText("H1: \u03BC \u2260 " + jtfTestValue.getText(0, 9)
                        + "...");
                h0.setToolTipText("H0: \u03BC = " + jtfTestValue.getText());
                h1.setToolTipText("H1: \u03BC \u2260 " + jtfTestValue.getText());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
}