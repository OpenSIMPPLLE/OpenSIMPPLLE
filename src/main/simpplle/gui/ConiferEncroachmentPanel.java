/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/** 
 * This class defines Conifer Encroachment Panel, a type of JPanel.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ConiferEncroachmentPanel extends JPanel {
  private BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  Border border1;
  TitledBorder titledBorder1;
  JPanel jPanel8 = new JPanel();
  JPanel jPanel9 = new JPanel();
  JPanel jPanel10 = new JPanel();
  JPanel jPanel11 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();
  JPanel jPanel12 = new JPanel();
  JPanel jPanel13 = new JPanel();
  JLabel jLabel4 = new JLabel();
  BorderLayout borderLayout8 = new BorderLayout();
  JLabel jLabel5 = new JLabel();
  BorderLayout borderLayout9 = new BorderLayout();
  JPanel jPanel14 = new JPanel();
  JPanel jPanel15 = new JPanel();
  BorderLayout borderLayout10 = new BorderLayout();
  JLabel jLabel6 = new JLabel();
  JTextField timeText0to5 = new JTextField();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  JTextField timeText6to10 = new JTextField();
  JPanel jPanel16 = new JPanel();
  JLabel jLabel7 = new JLabel();
  JPanel jPanel17 = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  JTextField timeText11to15 = new JTextField();
  JPanel jPanel18 = new JPanel();
  FlowLayout flowLayout6 = new FlowLayout();
  JTextField timeText16to20 = new JTextField();
  JPanel jPanel19 = new JPanel();
  FlowLayout flowLayout7 = new FlowLayout();
  JTextField timeText21to25 = new JTextField();
  JPanel jPanel110 = new JPanel();
  FlowLayout flowLayout8 = new FlowLayout();
  JTextField timeText26to30 = new JTextField();
  JPanel jPanel111 = new JPanel();
  FlowLayout flowLayout9 = new FlowLayout();
  JTextField timeText31to35 = new JTextField();
  JPanel jPanel112 = new JPanel();
  FlowLayout flowLayout10 = new FlowLayout();
  JTextField timeText36to40 = new JTextField();
  JPanel jPanel113 = new JPanel();
  FlowLayout flowLayout11 = new FlowLayout();
  JTextField timeText41to45 = new JTextField();
  JPanel jPanel114 = new JPanel();
  FlowLayout flowLayout12 = new FlowLayout();
  JTextField timeText46to50 = new JTextField();
  JPanel jPanel115 = new JPanel();
  FlowLayout flowLayout13 = new FlowLayout();
  JTextField timeText50Plus = new JTextField();
  JPanel jPanel20 = new JPanel();
  BorderLayout borderLayout11 = new BorderLayout();
  JPanel jPanel21 = new JPanel();
  JLabel jLabel8 = new JLabel();
  BorderLayout borderLayout12 = new BorderLayout();
  JPanel jPanel22 = new JPanel();
  JLabel jLabel9 = new JLabel();
  BorderLayout borderLayout13 = new BorderLayout();
  JPanel jPanel23 = new JPanel();
  JLabel jLabel10 = new JLabel();
  BorderLayout borderLayout14 = new BorderLayout();
  JPanel jPanel24 = new JPanel();
  JLabel jLabel11 = new JLabel();
  BorderLayout borderLayout15 = new BorderLayout();
  JPanel jPanel25 = new JPanel();
  JLabel jLabel12 = new JLabel();
  BorderLayout borderLayout16 = new BorderLayout();
  JPanel jPanel26 = new JPanel();
  JLabel jLabel13 = new JLabel();
  BorderLayout borderLayout17 = new BorderLayout();
  JPanel jPanel27 = new JPanel();
  JLabel jLabel14 = new JLabel();
  BorderLayout borderLayout18 = new BorderLayout();
  JPanel jPanel28 = new JPanel();
  JLabel jLabel15 = new JLabel();
  BorderLayout borderLayout19 = new BorderLayout();
  JPanel jPanel29 = new JPanel();
  JLabel jLabel16 = new JLabel();
  BorderLayout borderLayout110 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JTextArea jTextArea1 = new JTextArea();
/**
 * Constructor for Conifer Encroachment Panel.  calls jbInit
 */
  public ConiferEncroachmentPanel() {
    try {
      jbInit();
      setSize(getPreferredSize());
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * Sets out a series of panels in a variety of layouts and sets the text of j textfield and labels to represent a series of time ranges
   * These are in 5 year increments between 0-50 years and then a final one for plus 50.
   * @throws Exception
   */
  void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Unit Acres Range");
    this.setLayout(borderLayout1);
    northPanel.setLayout(borderLayout2);
    jPanel9.setLayout(borderLayout7);
    jPanel11.setLayout(gridLayout1);
    gridLayout1.setHgap(0);
    gridLayout1.setRows(12);
    jPanel10.setLayout(gridLayout2);
    gridLayout2.setRows(12);
    jLabel4.setFont(new java.awt.Font("Monospaced", 1, 14));
    jLabel4.setText("Unit Acres Range ");
    jPanel12.setLayout(borderLayout8);
    jLabel5.setFont(new java.awt.Font("Monospaced", 1, 14));
    jLabel5.setText(" # Time Steps Adjacent in Succession");
    jPanel13.setLayout(borderLayout9);
    jPanel14.setLayout(borderLayout10);
    jLabel6.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel6.setText("0 --  5");
    jPanel15.setLayout(flowLayout3);
    timeText0to5.setText("5");
    timeText0to5.setColumns(4);
    timeText0to5.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText0to5.addKeyListener(new ConiferEncroachmentPanel_timeText0to5_keyAdapter(this));
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    flowLayout4.setVgap(0);
    flowLayout4.setHgap(0);
    timeText6to10.setColumns(4);
    timeText6to10.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText6to10.addKeyListener(new ConiferEncroachmentPanel_timeText6to10_keyAdapter(this));
    timeText6to10.setText("10");
    jPanel16.setLayout(flowLayout4);
    jLabel7.setText("6 -- 10");
    jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel7.setFont(new java.awt.Font("Monospaced", 0, 12));
    jPanel17.setLayout(flowLayout5);
    flowLayout5.setVgap(0);
    flowLayout5.setHgap(0);
    timeText11to15.setColumns(4);
    timeText11to15.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText11to15.addKeyListener(new ConiferEncroachmentPanel_timeText11to15_keyAdapter(this));
    timeText11to15.setText("15");
    jPanel18.setLayout(flowLayout6);
    flowLayout6.setVgap(0);
    flowLayout6.setHgap(0);
    timeText16to20.setColumns(4);
    timeText16to20.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText16to20.addKeyListener(new ConiferEncroachmentPanel_timeText16to20_keyAdapter(this));
    timeText16to20.setText("15");
    jPanel19.setLayout(flowLayout7);
    flowLayout7.setVgap(0);
    flowLayout7.setHgap(0);
    timeText21to25.setColumns(4);
    timeText21to25.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText21to25.addKeyListener(new ConiferEncroachmentPanel_timeText21to25_keyAdapter(this));
    timeText21to25.setText("15");
    jPanel110.setLayout(flowLayout8);
    flowLayout8.setVgap(0);
    flowLayout8.setHgap(0);
    timeText26to30.setColumns(4);
    timeText26to30.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText26to30.addKeyListener(new ConiferEncroachmentPanel_timeText26to30_keyAdapter(this));
    timeText26to30.setText("15");
    jPanel111.setLayout(flowLayout9);
    flowLayout9.setVgap(0);
    flowLayout9.setHgap(0);
    timeText31to35.setColumns(4);
    timeText31to35.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText31to35.addKeyListener(new ConiferEncroachmentPanel_timeText31to35_keyAdapter(this));
    timeText31to35.setText("20");
    jPanel112.setLayout(flowLayout10);
    flowLayout10.setVgap(0);
    flowLayout10.setHgap(0);
    timeText36to40.setColumns(4);
    timeText36to40.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText36to40.addKeyListener(new ConiferEncroachmentPanel_timeText36to40_keyAdapter(this));
    timeText36to40.setText("20");
    jPanel113.setLayout(flowLayout11);
    flowLayout11.setVgap(0);
    flowLayout11.setHgap(0);
    timeText41to45.setColumns(4);
    timeText41to45.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText41to45.addKeyListener(new ConiferEncroachmentPanel_timeText41to45_keyAdapter(this));
    timeText41to45.setText("20");
    jPanel114.setLayout(flowLayout12);
    flowLayout12.setVgap(0);
    flowLayout12.setHgap(0);
    timeText46to50.setColumns(4);
    timeText46to50.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText46to50.addKeyListener(new ConiferEncroachmentPanel_timeText46to50_keyAdapter(this));
    timeText46to50.setText("20");
    jPanel115.setLayout(flowLayout13);
    flowLayout13.setVgap(0);
    flowLayout13.setHgap(0);
    timeText50Plus.setText("");
    timeText50Plus.setColumns(4);
    timeText50Plus.setHorizontalAlignment(SwingConstants.RIGHT);
    timeText50Plus.addKeyListener(new ConiferEncroachmentPanel_timeText50Plus_keyAdapter(this));
    jPanel20.setLayout(borderLayout11);
    jPanel21.setLayout(borderLayout12);
    jLabel8.setText("11 -- 15");
    jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel8.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel8.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel22.setLayout(borderLayout13);
    jLabel9.setText("16 -- 20");
    jLabel9.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel9.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel9.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel23.setLayout(borderLayout14);
    jLabel10.setText("21 -- 25");
    jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel10.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel10.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel24.setLayout(borderLayout15);
    jLabel11.setText("26 -- 30");
    jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel11.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel11.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel25.setLayout(borderLayout16);
    jLabel12.setText("31 - 35");
    jLabel12.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel12.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel12.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel26.setLayout(borderLayout17);
    jLabel13.setText("36 -- 40");
    jLabel13.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel13.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel13.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel27.setLayout(borderLayout18);
    jLabel14.setText("41 -- 45");
    jLabel14.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel14.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel14.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel28.setLayout(borderLayout19);
    jLabel15.setText("46 -- 50");
    jLabel15.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel15.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel15.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel29.setLayout(borderLayout110);
    jLabel16.setText("51+");
    jLabel16.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel16.setFont(new java.awt.Font("Monospaced", 0, 12));
    jLabel16.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel14.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel20.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel15.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel16.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel17.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel18.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel19.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel110.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel111.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel112.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel113.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel114.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel115.setBorder(BorderFactory.createLoweredBevelBorder());
    borderLayout7.setHgap(0);
    jPanel8.setLayout(flowLayout1);
    jPanel12.setBorder(null);
    jPanel13.setBorder(null);
    jPanel1.setLayout(borderLayout3);
    jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setLayout(borderLayout4);
    jTextArea1.setBackground(Color.lightGray);
    jTextArea1.setEnabled(false);
    jTextArea1.setDisabledTextColor(Color.black);
    jTextArea1.setEditable(false);
    jTextArea1.setText("Acres Range is the acres of the unit being considered.\n" +
    "Blank value for time steps indicates no enroachment\n\nThe unit " +
    "must have been in Succession for N steps to be considered.");
    jTextArea1.setLineWrap(true);
    jTextArea1.setRows(6);
    jTextArea1.setWrapStyleWord(true);
    this.add(northPanel,  BorderLayout.NORTH);
    northPanel.add(jPanel8, BorderLayout.NORTH);
    jPanel8.add(jPanel9, null);
    jPanel9.add(jPanel11, BorderLayout.WEST);
    jPanel11.add(jPanel12, null);
    jPanel12.add(jLabel4, BorderLayout.CENTER);
    jPanel11.add(jPanel14, null);
    jPanel14.add(jLabel6, BorderLayout.CENTER);
    jPanel11.add(jPanel20, null);
    jPanel20.add(jLabel7, BorderLayout.CENTER);
    jPanel11.add(jPanel21, null);
    jPanel21.add(jLabel8, BorderLayout.CENTER);
    jPanel11.add(jPanel22, null);
    jPanel22.add(jLabel9, BorderLayout.CENTER);
    jPanel11.add(jPanel23, null);
    jPanel23.add(jLabel10, BorderLayout.CENTER);
    jPanel11.add(jPanel24, null);
    jPanel24.add(jLabel11, BorderLayout.CENTER);
    jPanel11.add(jPanel25, null);
    jPanel25.add(jLabel12, BorderLayout.CENTER);
    jPanel11.add(jPanel26, null);
    jPanel26.add(jLabel13, BorderLayout.CENTER);
    jPanel11.add(jPanel27, null);
    jPanel27.add(jLabel14, BorderLayout.CENTER);
    jPanel11.add(jPanel28, null);
    jPanel28.add(jLabel15, BorderLayout.CENTER);
    jPanel11.add(jPanel29, null);
    jPanel29.add(jLabel16, BorderLayout.CENTER);
    jPanel9.add(jPanel10,  BorderLayout.EAST);
    jPanel10.add(jPanel13, null);
    jPanel13.add(jLabel5, BorderLayout.CENTER);
    jPanel10.add(jPanel15, null);
    jPanel15.add(timeText0to5, null);
    jPanel10.add(jPanel16, null);
    jPanel16.add(timeText6to10, null);
    jPanel10.add(jPanel17, null);
    jPanel17.add(timeText11to15, null);
    jPanel10.add(jPanel18, null);
    jPanel18.add(timeText16to20, null);
    jPanel10.add(jPanel19, null);
    jPanel19.add(timeText21to25, null);
    jPanel10.add(jPanel110, null);
    jPanel110.add(timeText26to30, null);
    jPanel10.add(jPanel111, null);
    jPanel111.add(timeText31to35, null);
    jPanel10.add(jPanel112, null);
    jPanel112.add(timeText36to40, null);
    jPanel10.add(jPanel113, null);
    jPanel113.add(timeText41to45, null);
    jPanel10.add(jPanel114, null);
    jPanel114.add(timeText46to50, null);
    jPanel10.add(jPanel115, null);
    jPanel115.add(timeText50Plus, null);
    this.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jPanel2, BorderLayout.NORTH);
    jPanel2.add(jTextArea1, BorderLayout.NORTH);
  }
/**
 * Sets the time step text field with appropriate range to the toString value of its array.  This will happen if there is something already 
 * in memory.  Otherwise it will == -1, which is a flag indicating no value present and sets the text field to empty.
 * @param values array of time values.
 */
  public void initialize(int[] values) {
    timeText0to5.setText((values[0] == -1)   ? "" : Integer.toString(values[0]));
    timeText6to10.setText((values[1] == -1)  ? "" : Integer.toString(values[1]));
    timeText11to15.setText((values[2] == -1) ? "" : Integer.toString(values[2]));
    timeText16to20.setText((values[3] == -1) ? "" : Integer.toString(values[3]));
    timeText21to25.setText((values[4] == -1) ? "" : Integer.toString(values[4]));
    timeText26to30.setText((values[5] == -1) ? "" : Integer.toString(values[5]));
    timeText31to35.setText((values[6] == -1) ? "" : Integer.toString(values[6]));
    timeText36to40.setText((values[7] == -1) ? "" : Integer.toString(values[7]));
    timeText41to45.setText((values[8] == -1) ? "" : Integer.toString(values[8]));
    timeText46to50.setText((values[9] == -1) ? "" : Integer.toString(values[9]));
    timeText50Plus.setText((values[10] == -1) ? "" : Integer.toString(values[10]));
  }
/**
 * checks to see if the entered text is a digit, consumes any key events which are not a digit, backspace, or delete.
 * @param e the number entered representing time
 * @return true if not delete or backspace, all other entries beside digit are consumed, therefore true = a digit was entered.
 */
  private boolean digitTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
      return false;
    }
    return (key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE);
  }
/**
 * Handles any text entered in in the 0-5 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
 * @param e 
 */
  void timeText0to5_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText0to5);
  }
  /**
   * Handles any text entered inthe 6-10 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText6to10_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText6to10);
  }
  /**
  * Handles any text entered inthe 11-15 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText11to15_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText11to15);
  }
  /**
   * Handles any text entered in the 16-20 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText16to20_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText16to20);
  }
  /**
   * Handles any text entered in the 21-25 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText21to25_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText21to25);
  }
  /**
   * Handles any text entered in the 26-30 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText26to30_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText26to30);
  }
  /**
   * Handles any text entered in the 31-55 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText31to35_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText31to35);
  }
  /**
   * Handles any text entered in the 36-40 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText36to40_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText36to40);
  }
  /**
   * Handles any text entered in the 41-45 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText41to45_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText41to45);
  }
  /**
   * Handles any text entered in the 46-50 text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText46to50_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText46to50);
  }
  /**
   * Handles any text entered in the 50 plus text field.  Passes to timeTextKeyTyped(actionEvent, textfield)
   * @param e
   */
  void timeText50Plus_keyTyped(KeyEvent e) {
    timeTextKeyTyped(e, timeText50Plus);
  }
  /**
   * Reads the text from the specified text field (which will be the appropriate time) 
   * @param e the key typed representing time 
   * @param textArea passed in from above key listeners - this will come from appropriate time range text field.
   */
  private void timeTextKeyTyped(KeyEvent e, JTextField textArea) {
    String text = textArea.getText();

    if (digitTyped(e) && (textArea.getSelectedText() == null) &&
        (text != null && text.length() == 2)) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }
/**
 * Checks to make sure value entered is valid by whether the string exists and has length >0
 * @param str string being evaluated
 * @return true if value is valid.  
 */
  private boolean valueIsOk(String str) { return (str != null && str.length() > 0); }
/**
 * Makes an array of time values which will either contain the time value entered as string in text field and parsed to an Integer or -1 as a flag that no value exists 
 * 
 * @return array of values representing time
 */
  public int[] getValues() {
    int[] values = new int[11];
    String str;

    str = timeText0to5.getText();
    values[0] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText6to10.getText();
    values[1] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText11to15.getText();
    values[2] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText16to20.getText();
    values[3] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText21to25.getText();
    values[4] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText26to30.getText();
    values[5] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText31to35.getText();
    values[6] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText36to40.getText();
    values[7] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText41to45.getText();
    values[8] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText46to50.getText();
    values[9] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    str = timeText50Plus.getText();
    values[10] = (valueIsOk(str) ? Integer.parseInt(str) : -1);

    return values;
  }

}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 0-5 so that only the keyTyped method need be call.  
 *
 *
 */

class ConiferEncroachmentPanel_timeText0to5_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText0to5_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 0-5.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText0to5_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 6-10 so that only the keyTyped method need be call.  
 *
 */

class ConiferEncroachmentPanel_timeText6to10_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText6to10_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 6-10.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText6to10_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 11-15 so that only the keyTyped method need be call.  
 *
 */
class ConiferEncroachmentPanel_timeText11to15_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText11to15_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 11-15.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText11to15_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 16-20 so that only the keyTyped method need be call.  
 *
 */

class ConiferEncroachmentPanel_timeText16to20_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText16to20_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 16-20.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText16to20_keyTyped(e);
  }
}

/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 21-25 so that only the keyTyped method need be call.  
 *
 */

class ConiferEncroachmentPanel_timeText21to25_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText21to25_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 21-25.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText21to25_keyTyped(e);
  }
}

class ConiferEncroachmentPanel_timeText26to30_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText26to30_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 31-35.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText26to30_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field31-35 so that only the keyTyped method need be call.  
 *
 */
class ConiferEncroachmentPanel_timeText31to35_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText31to35_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 31-35.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText31to35_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 36-40 so that only the keyTyped method need be call.  
 *
 */
class ConiferEncroachmentPanel_timeText36to40_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText36to40_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 41-45.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText36to40_keyTyped(e);
  }
}

/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 41-45 so that only the keyTyped method need be call.  
 *
 */
class ConiferEncroachmentPanel_timeText41to45_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText41to45_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 41-50.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText41to45_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 46-50 so that only the keyTyped method need be call.  
 *
 */

class ConiferEncroachmentPanel_timeText46to50_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText46to50_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 46-50.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText46to50_keyTyped(e);
  }
}
/**
 * 
 *Uses abstract key adapter to implement a key listener for the time text field 50 plus so that only the keyTyped method need be call.  
 *
 */
class ConiferEncroachmentPanel_timeText50Plus_keyAdapter extends java.awt.event.KeyAdapter {
  ConiferEncroachmentPanel adaptee;

  ConiferEncroachmentPanel_timeText50Plus_keyAdapter(ConiferEncroachmentPanel adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Key event in text field for time range 50 plus.
   */
  public void keyTyped(KeyEvent e) {
    adaptee.timeText50Plus_keyTyped(e);
  }
}