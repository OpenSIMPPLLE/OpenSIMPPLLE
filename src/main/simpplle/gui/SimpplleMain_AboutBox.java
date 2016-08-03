/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/** 
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */

public class SimpplleMain_AboutBox extends JDialog {

  private static final String creditText = "";

  JPanel splashPanel = new JPanel();
  JLabel splashLabel = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JTextArea creditsText = new JTextArea();
  JPanel creditsPanel = new JPanel();
  public SimpplleMain_AboutBox(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
      creditsText.setText(creditText);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    //imageControl1.setIcon(imageIcon);
    pack();
  }

  private void jbInit() throws Exception  {
    //imageIcon = new ImageIcon(getClass().getResource("your image name goes here"));
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Credits");
    this.setTitle("OpenSIMPPLLE " + SimpplleMain.VERSION + " " + SimpplleMain.RELEASE_KIND + " (" + SimpplleMain.BUILD_DATE + ")");
    setResizable(false);
    splashPanel.setLayout(borderLayout1);
    splashLabel.setIcon(new ImageIcon(simpplle.gui.SimpplleMain_AboutBox.class.getResource("images/splash.jpg")));
    splashLabel.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        splashLabel_mouseClicked(e);
      }
    });
    creditsText.setEditable(false);
    creditsText.setText("");
    creditsPanel.setBorder(titledBorder1);
    creditsText.setRows(14);
    splashPanel.add(splashLabel, BorderLayout.CENTER);
    splashPanel.add(creditsPanel,  BorderLayout.SOUTH);
    creditsPanel.add(creditsText, null);
    splashPanel.add(creditsPanel,  BorderLayout.SOUTH);
    creditsPanel.add(creditsText, null);
    this.getContentPane().add(splashPanel, null);
  }

  protected void processWindowEvent(WindowEvent e) {
    if(e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }
/**
 * Cancels the about dialog, by setting its visibility to false, and disposing.  
 */
  void cancel() {
    setVisible(false);
    dispose();
  }
/**
 * If mouse is clicked in the information about OpenSimpplle, cancels the about dialog, by setting its visibility to false, and disposing
 * @param e
 */
  void splashLabel_mouseClicked(MouseEvent e) {
    cancel();
  }
}
