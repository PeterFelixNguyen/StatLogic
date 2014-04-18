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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import pfnguyen.statlogic.descriptive.DescriptiveLoader;

/**
 * The Descriptive Panel contains components that load, save, and display data
 */
@SuppressWarnings("serial")
public class DescriptivePanel extends JPanel {
    private JButton jbtLoad = new JButton("Import data");
    private JButton jbtEnter = new JButton("Enter data");
    private JButton jbtOptions = new JButton("Options");
    private DescriptiveLoader loader;
    private ConfigurationPanel configPanel = new ConfigurationPanel();

    /**
     * Constructs DescriptivePanel and adds components defined in CategoryPanel
     * superclass
     */
    public DescriptivePanel(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        loader = new DescriptiveLoader(jtaOutput, statusBar, outputString);
        setBorder(new TitledBorder("Descriptive Stats"));
        Font customFont = new Font("TimesRoman", Font.BOLD, 13);
        jbtEnter.setFont(customFont);
        jbtLoad.setFont(customFont);
        jbtOptions.setFont(customFont);
        setLayout(new GridLayout(3, 1));

        add(jbtEnter);
        add(jbtLoad);
        add(jbtOptions);

        jbtLoad.addActionListener(new LoadListener());
        jbtEnter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea jtaValues = new JTextArea(20, 20);
                jtaValues.setLineWrap(true);
                jtaValues.setWrapStyleWord(true);

                BoxPanel entryPanel = new BoxPanel();
                entryPanel.add(new JLabel(
                        "Enter values"));
                entryPanel.add(new JScrollPane(jtaValues));

                int selected = JOptionPane.showConfirmDialog(null,
                        entryPanel, "Descriptive Statistics", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (selected == JOptionPane.OK_OPTION) {
                    if (jtaValues.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "No values entered",
                                "Failure", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        loader.stringToBigDecimalArray(jtaValues.getText());
                    }
                }
            }
        });
        jbtOptions.addActionListener(new OptionListener());
    }

    class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                loader.loadFileIntoArray();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Uncertain IOException");
            }
        }
    }

    class OptionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, configPanel, "Options",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public String getStringOfMean() {
        return BigDecimal.ONE.toString();
    }
}

@SuppressWarnings("serial")
class ConfigurationPanel extends JPanel {
    private JPanel calcOptions = new JPanel(new GridLayout(2, 1));
    private JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JCheckBox meanCheckBox = new JCheckBox("mean");
    private JCheckBox medianCheckBox = new JCheckBox("median");
    private JCheckBox modeCheckBox = new JCheckBox("mode");
    private JCheckBox nCheckBox = new JCheckBox("n");
    private JCheckBox minimaCheckBox = new JCheckBox("min");
    private JCheckBox maximaCheckBox = new JCheckBox("max");
    private JCheckBox percentiles = new JCheckBox("25th/75th percentiles");

    private JPanel varianceOptions = new JPanel(new GridLayout(1, 2));
    private JPanel leftPanel = new JPanel();
    private JPanel rightPanel = new JPanel(new GridLayout(2, 1));
    private JPanel subRightPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel subRightPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JRadioButton jrbBothSample = new JRadioButton("s & s^2");
    private JRadioButton jrbSampleVar = new JRadioButton("s^2");
    private JRadioButton jrbSampleStdDev = new JRadioButton("s");
    private JRadioButton jrbBothPop = new JRadioButton("\u03C3 & \u03C3^2");
    private JRadioButton jrbPopVar = new JRadioButton("\u03C3^2");
    private JRadioButton jrbPopStdDev = new JRadioButton("\u03C3");
    private JRadioButton jrbNoneVar = new JRadioButton("none");

    private JPanel outputOptions = new JPanel();
    private JRadioButton dataToggle = new JRadioButton("No values");
    private JRadioButton unsortedDataToggle = new JRadioButton(
            "Unsorted values");
    private JRadioButton sortedDataToggle = new JRadioButton(
            "Sorted values");

    public ConfigurationPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(calcOptions);
        calcOptions.add(row1);
        calcOptions.add(row2);
        calcOptions.setBorder(new TitledBorder("Calculator Options"));
        row1.add(meanCheckBox);
        row1.add(medianCheckBox);
        row1.add(modeCheckBox);
        row1.add(nCheckBox);
        row2.add(minimaCheckBox);
        row2.add(maximaCheckBox);
        row2.add(percentiles);

        add(varianceOptions);
        varianceOptions.add(leftPanel);
        varianceOptions.add(rightPanel);
        ButtonGroup sigmaGroup = new ButtonGroup();
        sigmaGroup.add(jrbBothSample);
        sigmaGroup.add(jrbSampleVar);
        sigmaGroup.add(jrbSampleStdDev);
        sigmaGroup.add(jrbBothPop);
        sigmaGroup.add(jrbPopVar);
        sigmaGroup.add(jrbPopStdDev);
        sigmaGroup.add(jrbNoneVar);
        leftPanel.add(jrbNoneVar);
        rightPanel.add(subRightPanel1);
        rightPanel.add(subRightPanel2);
        subRightPanel1.add(jrbBothSample);
        subRightPanel1.add(jrbSampleVar);
        subRightPanel1.add(jrbSampleStdDev);
        subRightPanel2.add(jrbBothPop);
        subRightPanel2.add(jrbPopVar);
        subRightPanel2.add(jrbPopStdDev);
        jrbNoneVar.setSelected(true);
        
        varianceOptions.setLayout(new FlowLayout(FlowLayout.LEFT));
        varianceOptions.setBorder(new TitledBorder("Variance Options"));
        add(outputOptions);
        outputOptions.setLayout(new FlowLayout(FlowLayout.LEFT));
        outputOptions.setBorder(new TitledBorder("Output Options"));
        outputOptions.add(dataToggle);
        outputOptions.add(unsortedDataToggle);
        outputOptions.add(sortedDataToggle);
        ButtonGroup valuesGroup = new ButtonGroup();
        valuesGroup.add(dataToggle);
        valuesGroup.add(unsortedDataToggle);
        valuesGroup.add(sortedDataToggle);
        dataToggle.setSelected(true);
    }
}
