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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MainGUI {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();

        frame.setTitle("StatLogic v0.2.5");
        frame.setMinimumSize(new Dimension(800, 450));
        frame.setSize(700, 400);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

@SuppressWarnings("serial")
class ContainerForChooser extends JPanel {

    public ContainerForChooser() {
        setLayout(new GridLayout());
        setBorder(new TitledBorder("Choose Calculator"));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setMinimumSize(new Dimension(60, 60));
    }
}

@SuppressWarnings("serial")
class MainFrame extends JFrame {
    // Visual components
    private JTextArea jtaOutput = new JTextArea();
    private JLabel statusBar = new JLabel(" ", SwingConstants.LEFT);
    private StringBuilder outputString = new StringBuilder();
    // Primary containers
    private MenuBar menuBar;
    private UpperPanel upperPanel = new UpperPanel(jtaOutput, statusBar, outputString);
    private LowerPanel lowerPanel = new LowerPanel(statusBar);
    // Subcontainers
    private ContainerForChooser containerForChooser = new ContainerForChooser();
    private JComboBox<String> jcboCalcChooser;
    private String[] calcName = { "Descriptive Statistics", "1-Sample Z",
            "1-Sample t", "Standard Score"};
    private JPanel leftInnerPanel = new JPanel(), rightInnerPanel = new JPanel();
    // Output window
    private JScrollPane scrollerForOutput = new JScrollPane(jtaOutput);
    private Border borderForOutput;
    private JPanel panelForCollapseBtn = new JPanel();
    private int panelIndex = 1; // set default index here
    private boolean hiddenPanel = false;
    // Currently related to setting font size
    private boolean outputFocused = false;
    private int minFontSize = 12, fontSize = 12, maxFontSize = 24;
    private boolean ctrlPressed = false;
    // Other visual-related components
    private final JLabel collapseBtn;
    private final ClassLoader cLoader;

    MainFrame() {
        cLoader = getClass().getClassLoader();
        ImageIcon collapseIcon = new ImageIcon(cLoader.getResource("images/Toggle_03_Hide.png"));
        collapseBtn = new JLabel(collapseIcon);

        ImageIcon windowIcon = new ImageIcon(cLoader.getResource("images/WindowIcon02-50x50.png"));
        setIconImage(windowIcon.getImage());

        menuBar = new MenuBar(jtaOutput, statusBar, outputString, upperPanel);
        setJMenuBar(menuBar);
        jtaOutput.setLineWrap(true);
        jtaOutput.setWrapStyleWord(true);
        jtaOutput.setEditable(false);
        upperPanel.getRightPanel().add(containerForChooser);
        containerForChooser.add(leftInnerPanel);
        containerForChooser.add(rightInnerPanel);
        leftInnerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rightInnerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jcboCalcChooser = new JComboBox<String>(calcName);
        jcboCalcChooser.setSelectedIndex(panelIndex);
        leftInnerPanel.add(jcboCalcChooser);
        rightInnerPanel.add(collapseBtn);

        jtaOutput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = false;
                }
            }
        });

        jtaOutput.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                if (ctrlPressed == true) {
                    if (outputFocused == true) {
                        int wheel = e.getWheelRotation();

                        if (wheel < 0) {
                            if (fontSize < maxFontSize) {
                                fontSize++;
                            }
                        }
                        else {
                            if (fontSize > minFontSize) {
                                fontSize--;
                            }
                        }

                        Font font = new Font(jtaOutput.getFont().getName(), Font.PLAIN, fontSize);
                        jtaOutput.setFont(font);
                    }
                }
            }
        });

        jtaOutput.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                outputFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                outputFocused = false;
            }
        });

        jcboCalcChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapseBtn.setIcon(new ImageIcon(cLoader.getResource("images/Toggle_03_Hide.png")));
                hiddenPanel = false;
                upperPanel.setPanel(jcboCalcChooser.getSelectedIndex());
                menuBar.setHideButton(false);
            }
        });
        collapseBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                upperPanel.collapseBtnHelper();
            }
        });

        upperPanel.getRightPanel().add(scrollerForOutput);
        panelForCollapseBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelForCollapseBtn.add(new JLabel("Hello World"));
        borderForOutput = BorderFactory.createCompoundBorder(new TitledBorder(
                "Output"), new BevelBorder(BevelBorder.LOWERED));
        scrollerForOutput.setBorder(borderForOutput);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(upperPanel, BorderLayout.NORTH); // BorderLayout should be removed
        add(lowerPanel, BorderLayout.SOUTH); // BorderLayout should be removed
    }

    class UpperPanel extends JPanel {
        private JPanel leftPanel;
        private JPanel rightPanel = new JPanel();
        private DescriptivePanel descriptiveCalc;
        private ZTestPanel zTestCalc;
        private TTestPanel tTestCalc;
        private ZScorePanel zScoreCalc;

        UpperPanel(JTextArea jtaOutput, JLabel statusBar, StringBuilder outputString) {
            descriptiveCalc = new DescriptivePanel(jtaOutput, statusBar, outputString);
            zTestCalc  = new ZTestPanel(jtaOutput, statusBar, outputString);
            tTestCalc = new TTestPanel(jtaOutput, statusBar, outputString);
            zScoreCalc = new ZScorePanel(jtaOutput, statusBar, outputString);

            leftPanel = new JPanel();
            leftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
            leftPanel.add(zTestCalc); // Default calculator
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(new TitledBorder(""));
            add(leftPanel);
            add(rightPanel);
        }

        void showPanel() {
            switch (panelIndex) {
                case 0:
                    leftPanel.add(descriptiveCalc);
                    break;
                case 1:
                    leftPanel.add(zTestCalc);
                    break;
                case 2:
                    leftPanel.add(tTestCalc);
                    break;
                case 3:
                    leftPanel.add(zScoreCalc);
                    break;
            }
            leftPanel.revalidate();
        }

        void setPanel(int index) {
            leftPanel.removeAll();

            switch (panelIndex = index) {
                case 0:
                    leftPanel.add(descriptiveCalc);
                    break;
                case 1:
                    leftPanel.add(zTestCalc);
                    break;
                case 2:
                    leftPanel.add(tTestCalc);
                    break;
                case 3:
                    leftPanel.add(zScoreCalc);
                    break;
            }
            leftPanel.revalidate();
            leftPanel.repaint();
        }

        public void collapseBtnHelper() {
            if (hiddenPanel == false) {
                collapseBtn.setIcon(new ImageIcon(cLoader.getResource("images/Toggle_03_Show.png")));
                upperPanel.getLeftPanel().removeAll();
                upperPanel.getLeftPanel().revalidate();
                hiddenPanel = true;
            } else if (hiddenPanel == true) {
                collapseBtn.setIcon(new ImageIcon(cLoader.getResource("images/Toggle_03_Hide.png")));
                upperPanel.showPanel();
                hiddenPanel = false;
            }
            menuBar.setHideButton(hiddenPanel);
        }

        public JPanel getLeftPanel() {
            return leftPanel;
        }

        public JPanel getRightPanel() {
            return rightPanel;
        }
    }

    class LowerPanel extends JPanel {
        LowerPanel(JLabel statusBar) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
            setBorder(new EtchedBorder(EtchedBorder.RAISED));
            add(statusBar);
        }
    }
}
