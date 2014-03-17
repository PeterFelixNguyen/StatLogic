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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import pfnguyen.statlogic.descriptive.DescriptiveLoader;

@SuppressWarnings("serial")
public class OldDescriptivePanel extends JPanel {
    private JButton jbtLoad = new JButton("Load Data");
    private JButton jbtSave = new JButton("Save Results");
    private JButton jbtHelp = new JButton("Help");
    public JButton jbtConf = new JButton("Options");
    private DescriptiveLoader loader;
    private LoadListener listenerJbtLoad = new LoadListener();
    private SaveListener listenerJbtSave = new SaveListener();
    private JLabel fileLbl = new JLabel();
    private JLabel meanLbl = new JLabel("Mean: ");
    private JLabel medianLbl = new JLabel("Median: ");
    private JLabel nLbl = new JLabel("n: ");

    public OldDescriptivePanel(final JTextArea jtaOutput, final JLabel statusBar) {
        loader = new DescriptiveLoader(jtaOutput, statusBar);
        setBorder(new TitledBorder("Old Descriptive"));
        Font customFont = new Font("TimesRoman", Font.BOLD, 13);
        jbtLoad.setFont(customFont);
        jbtSave.setFont(customFont);
        jbtHelp.setFont(customFont);
        jbtConf.setFont(customFont);
        fileLbl.setText("Data not loaded");
        setLayout(new GridLayout(4, 2));

        add(jbtLoad);
        add(jbtSave);
        add(jbtHelp);
        add(jbtConf);
        add(meanLbl);
        add(medianLbl);
        add(fileLbl);
        add(nLbl);

        jbtLoad.addActionListener(listenerJbtLoad);
        jbtSave.addActionListener(listenerJbtSave);
        jbtHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.io.File file = new java.io.File("txt/Help.txt");

                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /** */
    class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                loader.loadIntoArrayList();
                /*
                 * if statement checks if data loaded before continuing the try
                 * block
                 */
                if (loader.getArraySize() != 0) {
                    fileLbl.setText(loader.getDataFile().getName());
                    fileLbl.setToolTipText(loader.getDataFile().getName());
                    meanLbl.setText("Mean: " + String.valueOf(loader.getMean()));
                    meanLbl.setToolTipText(String.valueOf(loader.getMean()));
                    medianLbl.setText("Median: "
                            + String.valueOf(loader.getMedian()));
                    medianLbl
                    .setToolTipText(String.valueOf(loader.getMedian()));
                    nLbl.setText("n: " + String.valueOf(loader.getArraySize()));
                    nLbl.setToolTipText(String.valueOf(loader.getArraySize()));
                    jbtSave.setVisible(true);
                    jbtSave.setEnabled(true);
                    loader.setUseable(true);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Uncertain IOException");
            }
        }
    }

    /**  */
    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loader.isUseable()) {
                try {
                    loader.saveToFile();
                    loader.setUseable(false);
                    Desktop.getDesktop().open(loader.getOutputFile());
                } catch (IOException ex) {
                }
            }
        }
    }
}