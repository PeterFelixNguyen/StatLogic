package pfnguyen.statlogic.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class AboutPanel extends JPanel {
    private JEditorPane JepAbout = new JEditorPane();
    private UrlListener urlListener = new UrlListener();

    public AboutPanel() {
        JepAbout.setEditable(false);
        JepAbout.addHyperlinkListener(urlListener);
        File aboutPage = new File("html/AboutPage.html");

        if (aboutPage != null) {
            try {
                JepAbout.setPage(aboutPage.toURI().toURL());
            }
            catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + aboutPage);
            }
        }

        add(JepAbout);
    }
}

class UrlListener implements HyperlinkListener {

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(e.getURL().toURI());
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
}