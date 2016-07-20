/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import simpplle.comcode.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
  * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 */

public class LogicProcessChooser extends JDialog {
  LogicProcessTableDataModel dataModel =
      new LogicProcessTableDataModel(SimpplleType.PROCESS);
  LogicData logicData;
  private String description;
  private int    timeSteps;
  private boolean isAdjProcess;
  private boolean inclusiveTimeSteps;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Process Table");
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JScrollPane spScroll = new JScrollPane();
  JTable szTable = new JTable();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel jPanel1 = new JPanel();
  JTextField descText = new JTextField();
  JLabel descLabel = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel descPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel currentDesc = new JLabel();
  JLabeledNumberTextField textFieldPanel = new
      JLabeledNumberTextField();
  private JPanel inclusiveTimeStepsPanel = new JPanel();
  private JCheckBox inclusiveTimeStepsCB = new JCheckBox();
  private FlowLayout flowLayout1 = new FlowLayout();
  private FlowLayout flowLayout2 = new FlowLayout();
  private FlowLayout flowLayout4 = new FlowLayout();
  private JPanel anyExceptPanel = new JPanel();
  private JCheckBox anyExceptCB = new JCheckBox();
  
  /**
   * Constructor for Process Logic Chooser.  
   * @param owner owner of the dialog
   * @param title dialog title
   * @param modal modality 
   * @param logicData 
   * @param description  description from the logic data
   * @param timeSteps time steps 
   * @param inclusiveTS inclusive time steps
   * @param isAdjProcess true if is an adjacent evu process
   * @param anyExcept
   */
  public LogicProcessChooser(JDialog owner, String title, boolean modal,
                             LogicData logicData,
                             String description, int timeSteps,
                             boolean inclusiveTS,
                             boolean isAdjProcess,
                             boolean anyExcept) {
    super(owner, title, modal);
    this.logicData    = logicData;
    this.description  = description;
    this.timeSteps    = timeSteps;
    this.isAdjProcess = isAdjProcess;
    this.inclusiveTimeSteps = inclusiveTS;
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      initialize(anyExcept);
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public LogicProcessChooser() {
    this(new JDialog(), "LogicProcessChooser", false,null,"",1,false,false,false);
  }

  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    spGroupPanel.setLayout(borderLayout3);
    gridLayout1.setRows(3);
    northPanel.setLayout(borderLayout2);
    spTablePanel.setLayout(borderLayout4);
    spTablePanel.setBorder(border6);
    szTable.setModel(dataModel);
    centerPanel.setLayout(flowLayout3);
    jPanel1.setLayout(borderLayout5);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    descText.setCaretPosition(0);
    descText.setColumns(40);
    descLabel.setText("Description (Cell Value).  Leave blank for default");
    centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    descPanel.setLayout(borderLayout6);
    this.addWindowListener(new FireTypeLogicProcessChooser_this_windowAdapter(this));
    inclusiveTimeStepsCB.setToolTipText(
      "Include all of the intermediate time steps");
    inclusiveTimeStepsCB.setText("Inclusive Time Steps");
    anyExceptCB.setText("Any Process Except those checked");
    anyExceptPanel.setLayout(flowLayout4);

    textFieldPanel.setLayout(flowLayout1);
    inclusiveTimeStepsPanel.setLayout(flowLayout2);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setVgap(2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(2);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setVgap(2);
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(szTable);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.NORTH);
    centerPanel.add(jPanel1);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
    spGroupPanel.add(inclusiveTimeStepsPanel, java.awt.BorderLayout.CENTER);
    inclusiveTimeStepsPanel.add(inclusiveTimeStepsCB);
    spGroupPanel.add(anyExceptPanel, java.awt.BorderLayout.SOUTH);
    anyExceptPanel.add(anyExceptCB);
    spGroupPanel.add(textFieldPanel, java.awt.BorderLayout.NORTH);
  }
/**
 * Sets the inclusive time steps combo box visibility.  This allows users to include intermediate time steps.    
 * @param visible true if combo box should be visible.  
 */
  public void setInclusiveTimeStepVisibility(boolean visible) {
    inclusiveTimeStepsCB.setVisible(visible);
  }
  /**
   * Sets the visibility for the any except combo box which will allow users to eliminate process from consideration.  
   * @param visible
   */
  public void setAnyExceptVisibility(boolean visible) {
    anyExceptCB.setVisible(visible);
  }
  /**
   * Initializes the Process Logic chooser with data model logic data, and the combo boxes for intermediate time step inclusion and process elimination. 
   * @param anyExcept
   */
  private void initialize(boolean anyExcept) {
    dataModel.setLogicData(logicData);
    dataModel.setNonBaseCol(isAdjProcess);

    descText.setText(description);
    currentDesc.setText(description);
    textFieldPanel.setValue(timeSteps);
    textFieldPanel.setLabel("Time Steps in Past");
    inclusiveTimeStepsCB.setSelected(inclusiveTimeSteps);
    inclusiveTimeStepsCB.setVisible(true);
    anyExceptCB.setSelected(anyExcept);
  }
/**
 * Handles window changed status.  This method is called from the window adaptor and sets the variables for time steps, inclusive time steps, and processes to be eliminated
 * from consideration.  
 * @param e window changed status
 */
  public void this_windowClosing(WindowEvent e) {
    description = descText.getText().trim();
    if (description.length() == 0) { description = null; }

    timeSteps = textFieldPanel.getValue();
    inclusiveTimeSteps = inclusiveTimeStepsCB.isSelected();

    logicData.setProcessAnyExcept(anyExceptCB.isSelected());

    setVisible(false);
  }
/**
 * Get time steps.  Time steps are entered by user and set in the window closing event or in the constructor.   
 * @return time steps
 */
  public int getTimeSteps() {
    return timeSteps;
  }
/**
 * Gets the logic type description.  This is set in window closing event or in the constructor. 
 * @return
 */
  public String getDescription() {
    return description;
  }
/**
 * Method to determine whether to include intermediate time steps.  
 * @return
 */
  public boolean isInclusiveTimeSteps() {
    return inclusiveTimeSteps;
  }

}

class FireTypeLogicProcessChooser_this_windowAdapter extends WindowAdapter {
  private LogicProcessChooser adaptee;
  FireTypeLogicProcessChooser_this_windowAdapter(LogicProcessChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
