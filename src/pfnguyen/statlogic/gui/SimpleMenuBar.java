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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

@SuppressWarnings("serial")
public class SimpleMenuBar extends JMenuBar{
    private JMenu fileMenu = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
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
    private JMenuItem jmiView = new JMenuItem("View");
    private JMenuItem jmiFont = new JMenuItem("Font");
    // Tools Menu Choices
    private JMenuItem jmiArithmetic = new JMenuItem("Arithmetic");
    private JMenuItem jmiFileCheck = new JMenuItem("File Check");
    // Settings Menu Choices
    private JMenu jmiPrecision = new JMenu("Calculator Precision");
    private JRadioButtonMenuItem jrbmiPrecisionHi = new JRadioButtonMenuItem("High Precision");
    private JRadioButtonMenuItem jrbmiPrecisionLo = new JRadioButtonMenuItem("Low Precision");
    private JMenuItem jmiUserConfig = new JMenuItem("User Configuration");
    // Help Menu Choices
    private JMenuItem jmiAbout = new JMenuItem("About");
    private JMenuItem jmiFeedback = new JMenuItem("Feedback");
    private JMenuItem jmiManual = new JMenuItem("Manual");
    // Menu Panels
    private AboutPanel aboutPanel = new AboutPanel();

    public SimpleMenuBar() {
        add(fileMenu);
        add(editMenu);
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
        editMenu.addSeparator();
        editMenu.add(jmiView);
        editMenu.add(jmiFont);

        toolsMenu.add(jmiArithmetic);
        toolsMenu.add(jmiFileCheck);

        ButtonGroup group = new ButtonGroup();
        group.add(jrbmiPrecisionHi);
        group.add(jrbmiPrecisionLo);
        jrbmiPrecisionHi.setSelected(true);
        settingsMenu.add(jmiPrecision);
        jmiPrecision.add(jrbmiPrecisionHi);
        jmiPrecision.add(jrbmiPrecisionLo);
        settingsMenu.addSeparator();
        settingsMenu.add(jmiUserConfig);

        helpMenu.add(jmiManual);
        helpMenu.addSeparator();
        helpMenu.add(jmiFeedback);
        helpMenu.add(jmiAbout);

        jmiAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, aboutPanel);
            }
        });
    }
}
