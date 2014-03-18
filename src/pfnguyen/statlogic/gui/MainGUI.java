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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        frame.setTitle("StatLogic v0.2.2");
        frame.setMinimumSize(new Dimension(800, 450));
        frame.setSize(700, 400);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

@SuppressWarnings("serial")
class CollapseButton extends ImageIcon {
    public CollapseButton() {

    }
}

@SuppressWarnings("serial")
class ContainerForChooser extends JPanel {
    // private ImageIcon collapseIcon = new ImageIcon(
    // "D:/Dropbox/SourceCode/StatLogicProject/StatCalc0.2.4/images/1.png");

    // private JLabel collapseBtn = new JLabel(collapseIcon);
    // private JPanel leftInnerPanel = new JPanel();
    // private JPanel rightInnerPanel = new JPanel();

    public ContainerForChooser() {
        setLayout(new GridLayout());
        setBorder(new TitledBorder("Choose Calculator"));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setMinimumSize(new Dimension(60, 60));

        // add(leftInnerPanel);
        // add(rightInnerPanel);

        // leftInnerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // rightInnerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // leftInnerPanel.add(jcboCalcChooser);
        // rightInnerPanel.add(collapseBtn);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 60);
    }
}

@SuppressWarnings("serial")
class MainFrame extends JFrame {
    private JTextArea jtaOutput = new JTextArea();
    private JLabel statusBar = new JLabel(" ", SwingConstants.LEFT);
    private SimpleMenuBar menuBar = new SimpleMenuBar();
    private UpperPanel upperPanel = new UpperPanel(jtaOutput, statusBar);
    private LowerPanel lowerPanel = new LowerPanel(statusBar);
    //
    private ContainerForChooser containerForChooser = new ContainerForChooser();
    private JComboBox<String> jcboCalcChooser;
    private String[] calcName = { "Descriptive Statistics", "1-Sample Z-Test",
            "1-Sample T-Test", "Standard Score"};
    private JPanel leftInnerPanel = new JPanel();
    private JPanel rightInnerPanel = new JPanel();
    private ImageIcon collapseIcon = new ImageIcon("images/6.png");
    private JLabel collapseBtn = new JLabel(collapseIcon);
    //
    private JScrollPane scrollerForOutput = new JScrollPane(jtaOutput);
    private Border borderForOutput;
    private JPanel panelForCollapseBtn = new JPanel();
    private int panelIndex = 0;

    MainFrame() {
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
        jcboCalcChooser.setSelectedIndex(3);
        leftInnerPanel.add(jcboCalcChooser);
        rightInnerPanel.add(collapseBtn);
        jcboCalcChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPanel(jcboCalcChooser.getSelectedIndex());
            }
        });
        collapseBtn.addMouseListener(new MouseAdapter() {
            boolean state = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (state == false) {
                    collapseBtn.setIcon(new ImageIcon("images/5.png"));
                    upperPanel.leftPanel.removeAll();
                    upperPanel.leftPanel.revalidate();
                    state = true;
                } else if (state == true) {
                    collapseBtn.setIcon(new ImageIcon("images/6.png"));
                    showPanel();
                    upperPanel.leftPanel.revalidate();
                    state = false;
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

    UpperPanel(JTextArea jtaOutput, JLabel statusBar) {
        leftPanel = new LeftPanel(jtaOutput, statusBar);
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

    LeftPanel(JTextArea jtaOutput, JLabel statusBar) {
        descriptiveCalc = new DescriptivePanel(jtaOutput, statusBar);
        zTestCalc = new ZTestPanel(jtaOutput, statusBar);
        tTestCalc = new TTestPanel(jtaOutput, statusBar);
        zScoreCalc = new ZScorePanel(jtaOutput, statusBar);
        setLayout(new FlowLayout(FlowLayout.LEADING)); // has problem with left panel keeping its size
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(zScoreCalc); // This is optional
        // setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        // setMinimumSize(new Dimension(350, 350));
    }

    // // Keep preferred dimension equal to min and max
    // // to keep it the panel at a constant size
    // @Override
    // public Dimension getPreferredSize() {
    // return new Dimension(350, 350);
    // }
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
