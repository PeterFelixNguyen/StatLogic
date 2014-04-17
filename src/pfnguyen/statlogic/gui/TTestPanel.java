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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JRadioButton jrbLowerTail = new JRadioButton("Lower Tail");
    private JRadioButton jrbUpperTail = new JRadioButton("Upper Tail");
    private JRadioButton jrbTwoTail = new JRadioButton("Two Tail");
    private JRadioButton jrbProvideXBar = new JRadioButton(
            "Provide " + "X\u0305" + ", \u03C3\u0302");
    private JRadioButton jrbEnterData = new JRadioButton(
            "Enter data");
    //"Calculate " + "X\u0305"  + ", \u03C3\u0302"
    private JRadioButton jrbImportData = new JRadioButton(
            "Import data");
    //"Calculate " + "X\u0305" + ", \u03C3\u0302" + " from File"
    private JLabel jlblTestValue = new JLabel("Test Value");
    private JTextField jtfTestValue = new JTextField(4);
    private JTextField jtfAlpha = new JTextField(4);
    private JButton jbtCalculate = new JButton("Calculate");
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
    // Chooser
    private String[] calcName = { "Hypothesis Test",
            "Confidence Interval", "Both Options"};
    private JComboBox<String> jcboCalcOptions = new JComboBox<String>(calcName);

    public TTestPanel(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        loader = new TLoader(jtaOutput, statusBar, outputString);
        // Styling
        setBorder(new TitledBorder("1-Sample t"));
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
        row2.add(jrbLowerTail);
        row2.add(jrbUpperTail);
        row2.add(jrbTwoTail);
        row3.add(jrbProvideXBar);
        row3.add(jrbEnterData);
        row3.add(jrbImportData);
        row4.add(jlblTestValue);
        row4.add(jtfTestValue);
        row4.add(new JLabel("alpha \u03b1"));
        row4.add(jtfAlpha);
        row5.add(jbtCalculate);
        row5.add(jcboCalcOptions);
        // Component configuration
        jrbTwoTail.setSelected(true);
        jrbEnterData.setSelected(true);
        jcboCalcOptions.setSelectedIndex(2);
        // Grouping
        tailBtnGroup.add(jrbLowerTail);
        tailBtnGroup.add(jrbUpperTail);
        tailBtnGroup.add(jrbTwoTail);
        xBarBtnGroup.add(jrbProvideXBar);
        xBarBtnGroup.add(jrbEnterData);
        xBarBtnGroup.add(jrbImportData);
        // Listeners
        jrbLowerTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLowerTail();
                hypothesis = Hypothesis.LESS_THAN;
            }
        });
        jrbUpperTail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpperTail();
                hypothesis = Hypothesis.GREATER_THAN;
            }
        });
        jrbTwoTail.addActionListener(new ActionListener() {
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

        jtfAlpha.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    double temp = new Double(jtfAlpha.getText());
                    if (temp <= 0 || temp >= 1) {
                        jtfAlpha.setBackground(Color.RED);
                    }
                    else {
                        jtfAlpha.setBackground(Color.WHITE);
                    }
                }
                catch (NumberFormatException ex) {
                    if (jtfAlpha.getText().length() != 0) {
                        jtfAlpha.setBackground(Color.RED);
                    }
                    else {
                        jtfAlpha.setBackground(Color.WHITE);
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    double temp = new Double(jtfAlpha.getText());
                    if (temp <= 0 || temp >= 1) {
                        jtfAlpha.setBackground(Color.RED);
                    }
                    else {
                        jtfAlpha.setBackground(Color.WHITE);
                    }                }
                catch (NumberFormatException ex) {
                    if (jtfAlpha.getText().length() != 0) {
                        jtfAlpha.setBackground(Color.RED);
                    }
                    else {
                        jtfAlpha.setBackground(Color.WHITE);
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

        });

        jbtCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ((new Double(jtfAlpha.getText()) > 0.0)
                            && (new Double(jtfAlpha.getText()) < 1.0)) {
                        if (jrbProvideXBar.isSelected()) {
                            JTextField jtfXBar = new JTextField();
                            JTextField jtfSampleSize = new JTextField();
                            JTextField jtfStdDev = new JTextField();

                            if (xBar != null) {
                                jtfXBar.setText(xBar.toString());
                            }

                            // Should provide error handling if sampleSize <= 1
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
                                significance = new Double(jtfAlpha.getText());

                                xBar = new BigDecimal(jtfXBar.getText());
                                sampleSize = new Integer(jtfSampleSize.getText());
                                stdDev = new BigDecimal(jtfStdDev.getText());

                                if (jcboCalcOptions.getSelectedIndex() == 0 ||
                                        jcboCalcOptions.getSelectedIndex() == 2) {
                                    testValue = new BigDecimal(jtfTestValue.getText());
                                    loader.loadXIntoCalc(hypothesis, testValue,
                                            xBar, stdDev, sampleSize, significance,
                                            Option.TEST_HYPOTHESIS);
                                }
                                if (jcboCalcOptions.getSelectedIndex() == 1 ||
                                        jcboCalcOptions.getSelectedIndex() == 2)
                                {
                                    testValue = BigDecimal.ONE;
                                    loader.loadXIntoCalc(hypothesis, testValue,
                                            xBar, stdDev, sampleSize, significance,
                                            Option.CONFIDENCE_INTERVAl);
                                }
                            }
                        }
                        else if (jrbEnterData.isSelected()) {
                            JTextArea jtaValues = new JTextArea(20, 20);
                            jtaValues.setLineWrap(true);
                            jtaValues.setWrapStyleWord(true);

                            BoxPanel calcXBarPanel = new BoxPanel();
                            calcXBarPanel.add(new JLabel(
                                    "Enter values to calculate Sample Mean"));
                            calcXBarPanel.add(new JScrollPane(jtaValues));

                            int selected = JOptionPane.showConfirmDialog(
                                    null, calcXBarPanel, "1-Sample t",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);

                            if (selected != JOptionPane.CANCEL_OPTION) {
                                significance = new Double(jtfAlpha.getText());
                                if (jcboCalcOptions.getSelectedIndex() == 0 ||
                                        jcboCalcOptions.getSelectedIndex() == 2) {
                                    testValue = new BigDecimal(jtfTestValue.getText());
                                    loader.stringToBigDecimalArray(
                                            jtaValues.getText(),hypothesis,
                                            testValue, significance,
                                            Option.TEST_HYPOTHESIS);
                                }
                                if (jcboCalcOptions.getSelectedIndex() == 1 ||
                                        jcboCalcOptions.getSelectedIndex() == 2) {
                                    testValue = BigDecimal.ONE;
                                    loader.stringToBigDecimalArray(
                                            jtaValues.getText(), hypothesis,
                                            testValue, significance,
                                            Option.CONFIDENCE_INTERVAl);
                                }
                            }

                        }
                        else if (jrbImportData.isSelected()) {
                            try {
                                testValue = new BigDecimal(jtfTestValue.getText());
                                significance = new Double(jtfAlpha.getText());
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
                    JOptionPane.showMessageDialog(null, "Something is wrong, invalid values");
                }
            }
        });

        jcboCalcOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcboCalcOptions.getSelectedIndex() == 1) {
                    h0.setEnabled(false);
                    h1.setEnabled(false);
                    jrbLowerTail.setEnabled(false);
                    jrbUpperTail.setEnabled(false);
                    jrbTwoTail.setEnabled(false);
                    jlblTestValue.setEnabled(false);
                    jtfTestValue.setEnabled(false);
                }
                else {
                    h0.setEnabled(true);
                    h1.setEnabled(true);
                    jrbLowerTail.setEnabled(true);
                    jrbUpperTail.setEnabled(true);
                    jrbTwoTail.setEnabled(true);
                    jlblTestValue.setEnabled(true);
                    jtfTestValue.setEnabled(true);
                }
            }
        });
    }

    private void setTail() {
        if (jrbLowerTail.isSelected())
            setLowerTail();
        else if (jrbUpperTail.isSelected())
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