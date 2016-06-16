
package simpplle.gui;

import simpplle.comcode.FireEvent;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

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
 * <p>This class implements a simple dialog which
 * allows the user to change the Fire Spread Probability. It consists of a text field to enter probability that a single event will spread under extreme
 * conditions, and the size at which it will create a its own weather and spread similar to extreme conditions.
 * Hopefully in future versions of OpenSimpplle this class will contain logic for spotting downwind of a fire event.  This is not currently functional.  
  */


public class FireSpreadProb extends JDialog {
  private boolean focusLost = false;
  private int prob, extremeEventAcres;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel southPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JButton closeButton = new JButton();
  JButton okButton = new JButton();
  JPanel probPanel = new JPanel();
  JPanel weatherProbPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel probLabel = new JLabel();
  JPanel fireSizePanel = new JPanel();
  JLabel fireSizeLabel2 = new JLabel();
  TitledBorder titledBorder1;
  JPanel probTextPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JTextField probText = new JTextField();
  JPanel fireSizeTextPanel = new JPanel();
  JTextField fireSizeText = new JTextField();
  FlowLayout flowLayout5 = new FlowLayout();
  JLabel fireSizeLabel1 = new JLabel();
/**
 * Constructor for Fire Spread Probabilty.  
 * @param frame owner 
 * @param title name of dialog
 * @param modal modality
 */
  public FireSpreadProb(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }
/**
 * Overloaded Constructor for fire spread probability.  
 */
  public FireSpreadProb() {
    this(null, "", false);
  }
/**
 * Sets the panels, borders, components, and layouts for Fire Spread Probability dialog.
 * @throws Exception
 */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Extreme Fire Spread Probability");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    southPanel.setLayout(flowLayout2);
    closeButton.setText("Cancel");
    closeButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        closeButton_actionPerformed(e);
      }
    });
    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.setText("Ok");
    okButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        okButton_actionPerformed(e);
      }
    });
    southPanel.setBorder(BorderFactory.createEtchedBorder());
    probPanel.setLayout(gridLayout1);
    weatherProbPanel.setLayout(new BoxLayout(weatherProbPanel, BoxLayout.Y_AXIS));
    probText.setToolTipText("Enter a number between 0-100");
    probText.setColumns(5);
    probText.setHorizontalAlignment(SwingConstants.RIGHT);
    probText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        probText_focusLost(e);
      }
    });
    probText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        probText_actionPerformed(e);
      }
    });
    probLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    probLabel.setText("Probability that a single event will spread under extreme conditions");
    fireSizePanel.setLayout(new BoxLayout(fireSizePanel, BoxLayout.Y_AXIS));
    gridLayout1.setRows(2);
    fireSizeText.setText("1000");
    fireSizeText.setColumns(10);
    fireSizeText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        fireSizeText_focusLost(e);
      }
    });
    fireSizeText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fireSizeText_actionPerformed(e);
      }
    });
    fireSizeLabel2.setFont(new java.awt.Font("Monospaced", 1, 12));
    fireSizeLabel2.setToolTipText("");
    fireSizeLabel2.setText("and spread similar to extreme conditions");
    probPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    probTextPanel.setLayout(flowLayout3);
    probText.setToolTipText("Enter a number between 0-100");
    probText.setColumns(5);
    probText.setHorizontalAlignment(SwingConstants.RIGHT);
    probText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        probText_focusLost(e);
      }
    });
    probText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        probText_actionPerformed(e);
      }
    });
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(15);
    fireSizeTextPanel.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setHgap(15);
    fireSizeLabel1.setFont(new java.awt.Font("Monospaced", 1, 12));
    fireSizeLabel1.setText("Size at which a fire event will begin to create it's own weather");
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel, BorderLayout.CENTER);
    northPanel.add(probPanel, null);
    probPanel.add(weatherProbPanel, null);
    weatherProbPanel.add(probLabel, null);
    weatherProbPanel.add(probTextPanel, null);
    probTextPanel.add(probText, null);
    probPanel.add(fireSizePanel, null);
    fireSizePanel.add(fireSizeLabel1, null);
    fireSizePanel.add(fireSizeLabel2, null);
    fireSizePanel.add(fireSizeTextPanel, null);
    fireSizeTextPanel.add(fireSizeText, null);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(okButton, null);
    southPanel.add(closeButton, null);
  }
/**
 * This initializes the Fire Spread Probability class by getting the extreme probability from fire event class.  
 */
  private void initialize() {
    prob = simpplle.comcode.FireEvent.getExtremeProb();
    probText.setText(Integer.toString(prob));

    extremeEventAcres = FireEvent.getExtremeEventAcres();
    fireSizeText.setText(Integer.toString(extremeEventAcres));
  }
/**
 * Cancels fire spread probability dialog.
 */
  private void cancel() {
    setVisible(false);
    dispose();
  }
/**
 * If ok button is pressed, sets the extreme probability in fire event class to probability value entered in probability text field.  
 */
  private  void okPressed() {
    FireEvent.setExtremeProb(prob);
    FireEvent.setExtremeEventAcres(extremeEventAcres);
    dispose();
  }
/**
 * Allows user to input probability that a single event will spread under extreme conditions.  
 * Event listener for probability text field.  If focus is lost parses the text.  If it is an integer between 0 and 100 sets the probability to entered value.  If not an integer between
 * 0-100 notifies user of invalid number.   If focus is lost from text field a new thread is run holding the text from the text field in a temporary text field variable. 
 */
  private void probValueEntered() {
    int value;
    if (focusLost) { return; }

    try {
      value = Integer.parseInt(probText.getText());
      if (value < 0 || value > 100) {
       throw new NumberFormatException("Value should be a number 0 - 100");
      }
      prob = value;
    }
    catch (NumberFormatException ex) {
      focusLost = true;
      String msg = ex.getMessage();
      JOptionPane.showMessageDialog(this,msg,"Invalid number entered",
                                    JOptionPane.ERROR_MESSAGE);
      final JTextField tmpText = probText;
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          tmpText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
  }
/**
 * Allows user to set the size at which fire event will begin to create its own weather and spread similar to extreme conditions.  
 * Sets the fires size from user input in fire size text field.  If focus is not lost parses the text from fire size text field.  
 * If is an integer greater than 0 sets the extreme fire event acreage to input text value.
 * If focus is lost from text field a new thread is run holding the text from the text field in a temporary text field variable.
 */
  private void fireSizeValueEntered() {
    int value;
    if (focusLost) { return; }

    try {
      value = Integer.parseInt(fireSizeText.getText());
      if (value < 0) {
       throw new NumberFormatException("Value should be a positive number");
      }
      extremeEventAcres = value;
    }
    catch (NumberFormatException ex) {
      focusLost = true;
      String msg = ex.getMessage();
      JOptionPane.showMessageDialog(this,msg,"Invalid number entered",
                                    JOptionPane.ERROR_MESSAGE);
      final JTextField tmpText = fireSizeText;
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          tmpText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
      return;
    }
  }
/**
 * If probability value is entered in probability text field calls probValueEntered()
 * @param e 'extreme fire probability entered'
 */
  void probText_actionPerformed(ActionEvent e) {
    probValueEntered();
  }
/**
 * If focus lost in probability text field probability calls probValueEntered()
 * @param e focus lost in probability text field
 */
  void probText_focusLost(FocusEvent e) {
    ;
  }
/**
 * If Ok button pressed calls okPressed().
 * @param e 'ok'
 */
  void okButton_actionPerformed(ActionEvent e) {
    okPressed();
  }
/**
 * If close button pressed, cancels Fire Spread Probability dialog.
 * @param e 'cancel'
 */
  void closeButton_actionPerformed(ActionEvent e) {
    cancel();
  }
  /**
   * If fire size acreage entered in probability text field calls fireSizeValueEntered()
   *
  * @param e 'extreme fire probability entered'
  */
  void fireSizeText_actionPerformed(ActionEvent e) {
	  fireSizeValueEntered() ;
  }
  /**
   * If focus lost in fire size text field  calls fireSizeValueEntered()
   * @param e focus lost in probability text field
   */
  void fireSizeText_focusLost(FocusEvent e) {
    fireSizeValueEntered();
  }
  /**
   * Method empty
   * @param e
   */
  void probText1_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText1_focusLost(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText1_actionPerformed(ActionEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText2_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText2_focusLost(FocusEvent e) {

  }
  void probText2_actionPerformed(ActionEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText3_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText3_focusLost(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText3_actionPerformed(ActionEvent e) {

  }
  void probText4_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText4_focusLost(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText4_actionPerformed(ActionEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText5_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText5_focusLost(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void probText5_actionPerformed(ActionEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void fireSizeText1_focusGained(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void fireSizeText1_focusLost(FocusEvent e) {

  }
  /**
   * Method empty
   * @param e
   */
  void fireSizeText1_actionPerformed(ActionEvent e) {

  }

}
