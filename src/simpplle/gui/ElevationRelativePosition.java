package simpplle.gui;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import simpplle.comcode.Area;

/**
*
* The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class deals with the Elevation Relative Position.
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller 
*  
*/

@SuppressWarnings("serial")
public class ElevationRelativePosition extends JDialog {
  private Area area;
  private int value = 100;
  
  private final JPanel mainPanel = new JPanel();
  private final JPanel irregularPolyPanel = new JPanel();
  private final JPanel uniformPolyPanel = new JPanel();
  private final JPanel uniformLabelPanel = new JPanel();
  private final JPanel irregularLabelPanel = new JPanel();
  private final JProbabilityTextField uniformPolyValue = new JProbabilityTextField(10);
  private final JNumberTextField irregularPolyValue = new JNumberTextField();
//  private final JTextField uniformPolyValue = new JTextField(10);
//  private final JTextField irregularPolyValue = new JTextField();
  private final JLabel irregularPolyLabel = new JLabel();
  private final JLabel uniformPolyLabel = new JLabel();
  private final JPanel panel = new JPanel();
  private final JButton continueButton = new JButton();

  private final JPanel panel_1 = new JPanel();
  /**
   * Constructor for Elevation Relative Position.  Sets bounds as 100, 100, 678, 475.
   */
  public ElevationRelativePosition() {
    super();
    setBounds(100, 100, 678, 475);
    try {
      jbInit();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    //
  }
  /**
   * Overloaded constructor for Elevation Relative Position.  
   * @param frame
   * @param title
   * @param modal
   * @param area
   */
  public ElevationRelativePosition(Frame frame, String title, boolean modal, Area area) {
    super(frame, title, modal);
    this.area = area;
    
//    setBounds(100, 100, 500, 475);
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }

    setSize(getPreferredSize());
    update(getGraphics());
  }
  /**
   * Sets labels, panels, and components for Elevation Relative Position.  
   * @throws Exception
   */
  private void jbInit() throws Exception {
    
    System.currentTimeMillis();
    addWindowListener(new ThisWindowListener());
    getContentPane().add(mainPanel);
    mainPanel.setLayout(new BorderLayout());
    
    mainPanel.add(uniformPolyPanel, BorderLayout.NORTH);
    uniformPolyPanel.setLayout(new BorderLayout(10, 10));
    uniformPolyLabel.setPreferredSize(new Dimension(600, 150));
    uniformPolyPanel.add(uniformLabelPanel);
    uniformLabelPanel.add(uniformPolyLabel);
    uniformPolyLabel.setText("<html><p>Default difference in elevation (in meters) between a vegetation" +
        "unit and its adjacent vegetation units to determine relative position (above, below, or " +
        "next-to) is 10 percent. Enter a different value if the default is not desired.</p></html>");
    
    uniformLabelPanel.add(uniformPolyValue);
    uniformPolyValue.addFocusListener(new UniformPolyValueFocusListener());
    uniformPolyValue.addKeyListener(new UniformPolyValueKeyListener());
    uniformPolyValue.addActionListener(new UniformPolyValueActionListener());
    
    mainPanel.add(irregularPolyPanel);
    irregularPolyPanel.setLayout(new BorderLayout());
    
    irregularPolyPanel.add(irregularLabelPanel);
    
    irregularLabelPanel.add(irregularPolyLabel);
    irregularPolyLabel.setForeground(Color.BLACK);
    irregularPolyLabel.setBackground(Color.WHITE);
    irregularPolyLabel.setText("<html><p>Default difference in elevation  (in meters) between a " +
        "vegetation unit and its adjacent vegetation units to determine relative position (above," +
        " below, or next-to) is based on an absolute difference in mean elevation.  The default " +
        "is 100 meters.</p></html>");
    
    irregularLabelPanel.add(irregularPolyValue);
    irregularPolyValue.setColumns(10);
    irregularPolyValue.addKeyListener(new IrregularPolyValueKeyListener());
    irregularPolyValue.addFocusListener(new IrregularPolyValueFocusListener());
    irregularPolyValue.addActionListener(new IrregularPolyValueActionListener());
    
    mainPanel.add(panel, BorderLayout.SOUTH);
    
    panel.add(panel_1);
    
    panel_1.add(continueButton);
    continueButton.addActionListener(new ContinueButtonActionListener());
    continueButton.setText("Continue");
  }
  /**
   * 
   * Action listener for continue button.  
   *
   */
  private class ContinueButtonActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      continueButton_actionPerformed(e);
    }
  }
  /**
   * Action listener for Uniform Poly Value.  
   *
   */
  private class UniformPolyValueActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      uniformPolyValue_actionPerformed(e);
    }
  }
  /**
   * 
   * Key adapter for Uniform Poly value probability text field
   *
   */
  private class UniformPolyValueKeyListener extends KeyAdapter {
    public void keyTyped(KeyEvent e) {
      uniformPolyValue_keyTyped(e);
    }
  }
  /**
   * 
   * Focus listener if Uniform poly value probability text field loses focus.  
   *
   */
  private class UniformPolyValueFocusListener extends FocusAdapter {
    public void focusLost(FocusEvent e) {
      uniformPolyValue_focusLost(e);
    }
  }
  /**
   * Action listener for irregular polygon value number text field. 
   */
  private class IrregularPolyValueActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      irregularPolyValue_actionPerformed(e);
    }
  }
  /**
   * 
   * Focus listener if irregular polygon value number text field loses focus. 
   *
   */
  private class IrregularPolyValueFocusListener extends FocusAdapter {
    public void focusLost(FocusEvent e) {
      irregularPolyValue_focusLost(e);
    }
  }
  /**
   * 
   *Key listener if key is pressed in irregular polygon text field
   *
   */
  private class IrregularPolyValueKeyListener extends KeyAdapter {
    public void keyTyped(KeyEvent e) {
      irregularPolyValue_keyTyped(e);
    }
  }
  /**
   *
   *Class for window listener adaptor.  This is designed specifically for window closing event. 
   *
   */
  private class ThisWindowListener extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      this_windowClosing(e);
    }
  }
  /**
   * Finishes the dialog and calucualtes whether there are uniform size polygons or irregular size polygons.  Sets dialog as not visible and disposes.
   */
  private void finishDialog() {
    if (area.hasUniformSizePolygons()) {
      value = uniformPolyValue.getProbability();
    }
    else {
      value = irregularPolyValue.getValue();
    }
    setVisible(false);
    dispose();
  }
  /**
   * If continue button is pushed finishes the dialog.  
   * @param e 'continue'
   */
  protected void continueButton_actionPerformed(ActionEvent e) {
    finishDialog();
  }
/**
 * Gets the area elevation relative position.  If the area has uniform sized polygons sets the probability value, uniform polygon label and value visible and irregular polygon label and value not visible.  
 * If has irregular polygon does the opposite.  The default value for uniform are 10 and irregular is 100
 * 
 */
  private void initialize() {
    int value = area.getElevationRelativePosition();
    
    if (area.hasUniformSizePolygons()) {
      uniformPolyValue.setProbability(value);
      irregularPolyLabel.setVisible(false);
      irregularPolyValue.setVisible(false);
      uniformPolyLabel.setVisible(true);
      uniformPolyValue.setVisible(true);
    }
    else {
      irregularPolyValue.setValue(value);
      irregularPolyLabel.setVisible(true);
      irregularPolyValue.setVisible(true);
      uniformPolyLabel.setVisible(false);
      uniformPolyValue.setVisible(false);
    }
  }
/**
 * Gets the probability value.
 * @return
 */
  public int getValue()  {
    return value;
  }
  /**
   * Gets the probability value from uniform polygon text field.  
   * @param e
   */
  protected void uniformPolyValue_actionPerformed(ActionEvent e) {
    value = uniformPolyValue.getProbability();
  }
  /**
  * Uniform polygon value field has key typed in it.
   * @param e
   */
  protected void uniformPolyValue_keyTyped(KeyEvent e) {
    uniformPolyValue.localKeyTyped(e);
  }
  /**
  * Uniform polygon value field has focus lost.
   * @param e
   */
  protected void uniformPolyValue_focusLost(FocusEvent e) {
    value = uniformPolyValue.getProbability();
  }
  /**
   * If irregular polygon field has an action in it.  
   * @param e
   */
  protected void irregularPolyValue_actionPerformed(ActionEvent e) {
    value = irregularPolyValue.getValue();
  }
  /**
   * If irregular polygon field loses focus.
   * @param e focus lost from irregular polygon field
   */
  protected void irregularPolyValue_focusLost(FocusEvent e) {
    value = irregularPolyValue.getValue();
  }
  /**
   * If irregular polygon field has a key in it.
   * @param e key typed in irregular polygon field.  
   */
  protected void irregularPolyValue_keyTyped(KeyEvent e) {
    irregularPolyValue.textField_keyTyped(e);
  }
  /**
   *Gets the probability value for either uniform or irregular polygons and disposes ElevationRelativePosition dialog if window closing event occurs.  
   * @param e
   */
  protected void this_windowClosing(WindowEvent e) {
    finishDialog();
  }
  
}
