package simpplle.gui;

import simpplle.comcode.Fmz;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;
import simpplle.comcode.Formatting;

import java.text.NumberFormat;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class FireSuppResponseTimeLogicBuilder extends JDialog {
  private boolean inInit = false;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel headerPanel = new JPanel();
  JPanel rulesListPanel = new JPanel();
  JTextArea rulesHeaderTextArea = new JTextArea();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane rulesListScrollPane = new JScrollPane();
  JPanel rulesPanel = new JPanel();

  public FireSuppResponseTimeLogicBuilder(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public FireSuppResponseTimeLogicBuilder() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    headerPanel.setLayout(borderLayout2);
    rulesListPanel.setLayout(borderLayout3);
    rulesHeaderTextArea.setEditable(false);
    rulesHeaderTextArea.setText("");
    rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
    getContentPane().add(mainPanel);
    mainPanel.add(headerPanel,  BorderLayout.NORTH);
    mainPanel.add(rulesListPanel, BorderLayout.CENTER);
    headerPanel.add(rulesHeaderTextArea, BorderLayout.CENTER);
    rulesListPanel.add(rulesListScrollPane,  BorderLayout.CENTER);
    rulesListScrollPane.getViewport().add(rulesPanel, null);
  }

  private void initialize() {
    inInit = true;

    RegionalZone zone = Simpplle.getCurrentZone();
    Fmz[] allFmz = zone.getAllFmz();
    JPanel jPanel;
    JLabel label;
    float time;
    String timeStr;
    JDataTextField responseTimeText;

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    String headerText = "FMZ  --  Response Time (hours)    ";

    rulesHeaderTextArea.setText(headerText);

    for (int i=0; i<allFmz.length; i++) {
      jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      jPanel.setBorder(BorderFactory.createEtchedBorder());
      label  = new JLabel(Formatting.fixedField(allFmz[i].getName(),7,true));
      label.setFont(new java.awt.Font("Monospaced", 0, 12));
      jPanel.add(label);

      // Production rate ft/hr
      time   = allFmz[i].getResponseTime();
      timeStr    = Float.isNaN(time) ? "" : nf.format(time);
      responseTimeText  = new JDataTextField(5,allFmz[i]);
      responseTimeText.setText(timeStr);
      jPanel.add(responseTimeText);

      responseTimeText.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(ActionEvent e) {
          responseTimeText_actionPerformed(e);
        }
      });
      responseTimeText.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(KeyEvent e) {
          responseTimeText_keyTyped(e);
        }
      });
      responseTimeText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(FocusEvent e) {
          responseTimeText_focusLost(e);
        }
      });


      rulesPanel.add(jPanel);
    }
    inInit = false;
  }

  // *** responseTimeText Event Handlers ***
  // *******************************
  void responseTimeText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE &&
        key != '.') {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }
  void responseTimeText_focusLost(FocusEvent e) {
    if (inInit) { return; }
    updateResponseTime((JDataTextField)e.getSource());
  }
  private void responseTimeText_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    updateResponseTime((JDataTextField)e.getSource());
  }
  private void updateResponseTime(JDataTextField textField) {
    Fmz fmz = (Fmz)textField.getDataSource();
    String text = textField.getText();
    float num = (text.length() == 0) ? Float.NaN : Float.parseFloat(text);
    fmz.setResponseTime(num);
  }

}



