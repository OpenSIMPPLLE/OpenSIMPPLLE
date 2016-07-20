/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import simpplle.comcode.LogicData;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.SimpplleType;
import javax.swing.JCheckBox;

/**
 * This class creates the dialog for the Treatment Logic Chooser, a type of JDialog.
 * It allows the user to configure treatment logic.
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */
public class LogicTreatmentChooser extends JDialog {
  FireLogicTreatmentTableDataModel dataModel =
      new FireLogicTreatmentTableDataModel(SimpplleType.TREATMENT);
  LogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Treatment Table");
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
  private JPanel anyExceptPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private FlowLayout flowLayout2 = new FlowLayout();
  private FlowLayout flowLayout4 = new FlowLayout();
  private JCheckBox inclusiveTimeStepsCB = new JCheckBox();
  private JCheckBox anyExceptCB = new JCheckBox();
  /**
   *Constructor for Treatment Logic Chooser
   * @param owner the jdialog that owns this dialog, blank if no owner
   * @param title title of the dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows
   * @param logicData the logic data governing this dialog. 
   * @param inclusiveTS true if user wishes to use intermediate time steps
   * @param anyExcept method to allow user to select treatments not to include in analysis
   */
  public LogicTreatmentChooser(JDialog owner, String title, boolean modal,
                               LogicData logicData, boolean inclusiveTS,
                               boolean anyExcept) {
    super(owner, title, modal);
    this.logicData          = logicData;
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      initialize(inclusiveTS,anyExcept);
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded constructor for Treatment Logic Chooser.  
 * Passes a new jdialog as the owner, sets title to FireTypeLogicTreatmentChooserm, blocks user input to other top-level windows, false for inclusive time steps
 * and false for anyExcept. 
 */
  public LogicTreatmentChooser() {
    this(new JDialog(), "FireTypeLogicTreatmentChooser", false,null,false,false);
  }
/**
 * Initializes the Treatment Logic Chooser with layouts, panels, text, columns, caret position, and components.
 * @throws Exception
 */
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
    this.addWindowListener(new FireTypeLogicTreatmentChooser_this_windowAdapter(this));
    inclusiveTimeStepsPanel.setLayout(flowLayout1);
    anyExceptPanel.setLayout(flowLayout2);
    textFieldPanel.setLayout(flowLayout4);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setVgap(2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setVgap(2);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setVgap(2);
    inclusiveTimeStepsCB.setToolTipText(
      "Include all of the intermediate time steps");
    inclusiveTimeStepsCB.setText(
      "Inclusive Time Steps");
    anyExceptCB.setText("Any Treatment Except those checked");
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
 * Sets the visibility of inclusive time steps check box, which allows users to designate whether they want to include intermediate time steps.
 * @param visible true if inclusiveTimeSteps check box should be visible.
 */
  public void setInclusiveTimeStepVisibility(boolean visible) {
    inclusiveTimeStepsCB.setVisible(visible);
  }
/**
 * Initializes the Treatment Logic Chooser. 
 * Initializes the treatment logic chooser with the logic data set in the constructor.  Empties the description text field.  
 * If the logic data is not the default treatment description, sets the description text to treatment description.  
 * Then sets the current description label to the treatment description, informs user of past time steps, sets the inclusive time steps and anyExcept check box
 * to unselected (false)- (if first time through)  
 * @param inclusiveTS allows user to include intermediate time steps (true) 
 * @param anyExcept allows users to exclude treatments
 */
  private void initialize(boolean inclusiveTS, boolean anyExcept) {
    dataModel.setLogicData(logicData);

    descText.setText("");
    if (logicData.isDefaultDescription(SimpplleType.TREATMENT) == false) {
      descText.setText(logicData.getDescription(SimpplleType.TREATMENT));
    }
    currentDesc.setText(logicData.getDescription(SimpplleType.TREATMENT));
    textFieldPanel.setValue(logicData.getTreatmentPastTimeSteps());
    textFieldPanel.setLabel("Time Steps in Past");

    inclusiveTimeStepsCB.setSelected(inclusiveTS);
    inclusiveTimeStepsCB.setVisible(true);

    anyExceptCB.setSelected(anyExcept);
    update(getGraphics());
  }
  /**
   * Handles window closing event.  Sets the logic data treatment description to the description in the text field, and gets the past treatment time steps.
   * Sets the inclusive time steps and any except variables.    
   * @param e window closing event
   */
  public void this_windowClosing(WindowEvent e) {
    logicData.setDescription(descText.getText().trim(),SimpplleType.Types.TREATMENT);
    logicData.setTreatmentPastTimeSteps(textFieldPanel.getValue());

    logicData.setTreatmentInclusiveTimeSteps(inclusiveTimeStepsCB.isSelected());
    logicData.setTreatmentAnyExcept(anyExceptCB.isSelected());
  }
}
/**
 * Creates a window adapter to handle the window closing event.  This saves on work in implementing window listeners.  
 * It passes to method within LogicTreatmentChooser class.  
 *
 */
class FireTypeLogicTreatmentChooser_this_windowAdapter extends WindowAdapter {
  private LogicTreatmentChooser adaptee;
  FireTypeLogicTreatmentChooser_this_windowAdapter(LogicTreatmentChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to this_windowClosing() within LogicTreatmentChooser class when window closing event occurred.
   */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
