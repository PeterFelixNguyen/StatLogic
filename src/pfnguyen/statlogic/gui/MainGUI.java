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

        frame.setTitle("StatLogic v0.2.4");
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
    private MenuBar menuBar = new MenuBar(jtaOutput, statusBar, outputString);
    private UpperPanel upperPanel = new UpperPanel(jtaOutput, statusBar, outputString);
    private LowerPanel lowerPanel = new LowerPanel(statusBar);
    // Subcontainers
    private ContainerForChooser containerForChooser = new ContainerForChooser();
    private JComboBox<String> jcboCalcChooser;
    private String[] calcName = { "Descriptive Statistics", "1-Sample Z",
            "1-Sample t", "Standard Score"};
    private JPanel leftInnerPanel = new JPanel();
    private JPanel rightInnerPanel = new JPanel();
    // Output window
    private JScrollPane scrollerForOutput = new JScrollPane(jtaOutput);
    private Border borderForOutput;
    private JPanel panelForCollapseBtn = new JPanel();
    private int panelIndex = 1; // set default index here
    private boolean hiddenPanel = false;
    private boolean outputFocused = false;
    private int minFontSize = 12;
    private int fontSize = 12;
    private int maxFontSize = 24;
    private boolean ctrlPressed = false;

    MainFrame() {
        final ClassLoader cLoader = getClass().getClassLoader();
        ImageIcon collapseIcon = new ImageIcon(cLoader.getResource("images/Toggle_03_Hide.png"));
        final JLabel collapseBtn = new JLabel(collapseIcon);

        ImageIcon windowIcon = new ImageIcon(cLoader.getResource("images/WindowIcon02-50x50.png"));
        setIconImage(windowIcon.getImage());

        setJMenuBar(menuBar);
        jtaOutput.setLineWrap(true);
        jtaOutput.setWrapStyleWord(true);
        jtaOutput.setEditable(false);
        upperPanel.rightPanel.add(containerForChooser);
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
                setPanel(jcboCalcChooser.getSelectedIndex());
            }
        });
        collapseBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (hiddenPanel == false) {
                    collapseBtn.setIcon(new ImageIcon(cLoader.getResource("images/Toggle_03_Show.png")));
                    upperPanel.leftPanel.removeAll();
                    upperPanel.leftPanel.revalidate();
                    hiddenPanel = true;
                } else if (hiddenPanel == true) {
                    collapseBtn.setIcon(new ImageIcon(cLoader.getResource("images/Toggle_03_Hide.png")));
                    showPanel();
                    upperPanel.leftPanel.revalidate();
                    hiddenPanel = false;
                }
            }
        });

        upperPanel.rightPanel.add(scrollerForOutput);
        panelForCollapseBtn.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelForCollapseBtn.add(new JLabel("Hello World"));
        borderForOutput = BorderFactory.createCompoundBorder(new TitledBorder(
                "Output"), new BevelBorder(BevelBorder.LOWERED));
        scrollerForOutput.setBorder(borderForOutput);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.SOUTH);
    }

    public void showPanel() {
        switch (panelIndex) {
            case 0:
                upperPanel.leftPanel.add(upperPanel.leftPanel.descriptiveCalc);
                break;
            case 1:
                upperPanel.leftPanel.add(upperPanel.leftPanel.zTestCalc);
                break;
            case 2:
                upperPanel.leftPanel.add(upperPanel.leftPanel.tTestCalc);
                break;
            case 3:
                upperPanel.leftPanel.add(upperPanel.leftPanel.zScoreCalc);
                break;
        }
    }

    public void setPanel(int index) {
        switch (panelIndex = index) {
            case 0:
                upperPanel.leftPanel.removeAll();
                upperPanel.leftPanel.add(upperPanel.leftPanel.descriptiveCalc);
                upperPanel.leftPanel.revalidate();
                upperPanel.leftPanel.repaint();
                break;
            case 1:
                upperPanel.leftPanel.removeAll();
                upperPanel.leftPanel.add(upperPanel.leftPanel.zTestCalc);
                upperPanel.leftPanel.revalidate();
                upperPanel.leftPanel.repaint();
                break;
            case 2:
                upperPanel.leftPanel.removeAll();
                upperPanel.leftPanel.add(upperPanel.leftPanel.tTestCalc);
                upperPanel.leftPanel.revalidate();
                upperPanel.leftPanel.repaint();
                break;
            case 3:
                upperPanel.leftPanel.removeAll();
                upperPanel.leftPanel.add(upperPanel.leftPanel.zScoreCalc);
                upperPanel.leftPanel.revalidate();
                upperPanel.leftPanel.repaint();
                break;
        }
    }
}

@SuppressWarnings("serial")
class UpperPanel extends JPanel {
    RightPanel rightPanel = new RightPanel();
    LeftPanel leftPanel;
    //	private int panelIndex = 0;

    UpperPanel(JTextArea jtaOutput, JLabel statusBar, StringBuilder outputString) {
        leftPanel = new LeftPanel(jtaOutput, statusBar, outputString);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new TitledBorder(""));
        add(leftPanel);
        add(rightPanel);
    }

    // Need to make fields private
    //	public void setPanel(int index) {
    //		switch (panelIndex = index) {
    //		case 0:
    //			leftPanel.removeAll();
    //			leftPanel.add(leftPanel.descriptiveCalc);
    //			leftPanel.revalidate();
    //			leftPanel.repaint();
    //			break;
    //		case 1:
    //			leftPanel.removeAll();
    //			leftPanel.add(leftPanel.zTestCalc);
    //			leftPanel.revalidate();
    //			leftPanel.repaint();
    //			break;
    //		case 2:
    //			leftPanel.removeAll();
    //			leftPanel.add(leftPanel.tTestCalc);
    //			leftPanel.revalidate();
    //			leftPanel.repaint();
    //			break;
    //		case 3:
    //			leftPanel.removeAll();
    //			leftPanel.add(leftPanel.zScoreCalc);
    //			leftPanel.revalidate();
    //			leftPanel.repaint();
    //			break;
    //		case 4:
    //			leftPanel.removeAll();
    //			leftPanel.add(leftPanel.unusedCalc);
    //			leftPanel.revalidate();
    //			leftPanel.repaint();
    //			break;
    //		}
    //	}
}

@SuppressWarnings("serial")
class LeftPanel extends JPanel {
    DescriptivePanel descriptiveCalc;
    ZTestPanel zTestCalc;
    TTestPanel tTestCalc;
    ZScorePanel zScoreCalc;

    LeftPanel(JTextArea jtaOutput, JLabel statusBar, final StringBuilder outputString) {
        descriptiveCalc = new DescriptivePanel(jtaOutput, statusBar, outputString);
        zTestCalc = new ZTestPanel(jtaOutput, statusBar, outputString);
        tTestCalc = new TTestPanel(jtaOutput, statusBar, outputString);
        zScoreCalc = new ZScorePanel(jtaOutput, statusBar, outputString);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(zTestCalc); // Default calculator
    }
}

@SuppressWarnings("serial")
class RightPanel extends JPanel {

    RightPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}

@SuppressWarnings("serial")
class LowerPanel extends JPanel {
    LowerPanel(JLabel statusBar) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        setBorder(new EtchedBorder(EtchedBorder.RAISED));
        add(statusBar);
    }
}

class CheckBoxListener implements ActionListener {
    int dummyValue;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (dummyValue) {
        }
    }
}
