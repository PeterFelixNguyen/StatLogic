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

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
    private JMenu fileMenu = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu viewMenu = new JMenu("View");
    private JMenu toolsMenu = new JMenu("Tools");
    private JMenu settingsMenu = new JMenu("Settings");
    private JMenu helpMenu = new JMenu("Help");
    // File Menu Choices
    private JMenuItem jmiSave = new JMenuItem("Save");
    private JMenuItem jmiSaveAs = new JMenuItem("Save As...");
    private JMenuItem jmiExit = new JMenuItem("Exit");
    // Edit Menu Choices
    private JMenuItem jmiClear = new JMenuItem("Clear");
    private JMenuItem jmiFind = new JMenuItem("Find");
    private JMenuItem jmiSelectAll = new JMenuItem("Select All");
    // View Menu Choices
    private JMenu jmCalcView = new JMenu("Calculator View");
    private JMenuItem jmiFontSize = new JMenuItem("Font Size");
    private JRadioButtonMenuItem jrbmiHideCalc = new JRadioButtonMenuItem("Hide Calculator");
    private JRadioButtonMenuItem jrbmiShowCalc = new JRadioButtonMenuItem("Show Calculator");
    private FontPanel fontPanel = new FontPanel();
    // Tools Menu Choices
    private JMenuItem jmiArithmetic = new JMenuItem("Arithmetic");
    private JMenuItem jmiFileCheck = new JMenuItem("File Check");
    // Settings Menu Choices
    private JMenuItem jmiUserConfig = new JMenuItem("User Configuration");
    // Help Menu Choices
    private JMenuItem jmiAbout = new JMenuItem("About");
    private JMenuItem jmiManual = new JMenuItem("Manual");
    // Menu Panels
    private AboutPanel aboutPanel = new AboutPanel();
    // Visual Components
    private JTextArea jtaOutput;

    public MenuBar(final JTextArea jtaOutput, final JLabel statusBar, final StringBuilder outputString) {
        this.jtaOutput = jtaOutput;
        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(toolsMenu);
        add(settingsMenu);
        add(helpMenu);

        fileMenu.add(jmiSave);
        fileMenu.add(jmiSaveAs);
        fileMenu.addSeparator();
        fileMenu.add(jmiExit);

        editMenu.add(jmiClear);
        editMenu.addSeparator();
        editMenu.add(jmiFind);
        editMenu.addSeparator();
        editMenu.add(jmiSelectAll);

        viewMenu.add(jmCalcView);
        viewMenu.add(jmiFontSize);

        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(jrbmiHideCalc);
        viewGroup.add(jrbmiShowCalc);
        jmCalcView.add(jrbmiHideCalc);
        jmCalcView.add(jrbmiShowCalc);

        toolsMenu.add(jmiArithmetic);
        toolsMenu.add(jmiFileCheck);

        settingsMenu.add(jmiUserConfig);

        helpMenu.add(jmiManual);
        helpMenu.addSeparator();
        helpMenu.add(jmiAbout);

        jmiSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Text Files", "txt");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(filter);

                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                    java.io.File outFile = fileChooser.getSelectedFile();

                    if (outFile.exists()) {
                        final int option = JOptionPane.showConfirmDialog(null,
                                "File already exists, " + "do you want to replace file?",
                                "Retep's StatCalc", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (!(option == JOptionPane.YES_OPTION)) {
                            System.out.println("Cancelled Save");
                        }
                    }
                    PrintWriter output;
                    try {
                        output = new PrintWriter(new BufferedWriter(new FileWriter(
                                outFile, false)));
                        output.print(jtaOutput.getText());
                        output.close();
                    }
                    catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    statusBar.setText(" Saved to file \"" + outFile.getName()
                            + "\"");
                    try {
                        Desktop.getDesktop().open(outFile);
                    }
                    catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });

        jmiExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jmiClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jtaOutput.setText(null);
                statusBar.setText("Output cleared");
                outputString.setLength(0);
            }
        });

        jmiFontSize.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Point point = new Point(getRootPane().getLocationOnScreen());
                fontPanel.setLocation((int)point.getX() + 50, (int)point.getY() + 50);
                fontPanel.setVisible(true);
                System.out.println(getRootPane().getLocationOnScreen());
            }
        });

        jmiAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClassLoader cLoader = getClass().getClassLoader();

                ImageIcon windowIcon = new ImageIcon(cLoader.getResource("images/WindowIcon02-50x50.png"));
                JOptionPane.showMessageDialog(null, aboutPanel, "About StatLogic", JOptionPane.PLAIN_MESSAGE, windowIcon);
            }
        });
    }

    class FontPanel extends JFrame {
        private JSlider fontSlider = new JSlider(JSlider.HORIZONTAL);

        FontPanel() {
            setTitle("Font Size");
            setSize(250, 100);
            setResizable(false);
            add(fontSlider);
            fontSlider.setMinimum(12);
            fontSlider.setMaximum(24);
            fontSlider.setPaintLabels(true);
            fontSlider.setPaintTicks(true);
            fontSlider.setMajorTickSpacing(1);
            fontSlider.setMinorTickSpacing(0);

            fontSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    int fontSize = fontSlider.getValue();

                    Font font = new Font(jtaOutput.getFont().getName(), Font.PLAIN, fontSize);
                    jtaOutput.setFont(font);
                }

            });
        }
    }
}