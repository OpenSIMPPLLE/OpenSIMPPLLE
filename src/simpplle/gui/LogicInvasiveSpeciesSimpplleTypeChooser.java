/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simpplle.comcode.LogicData;
import simpplle.comcode.SimpplleType;
import simpplle.comcode.InvasiveSpeciesLogicDataMSU;
import javax.swing.table.*;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
* <p>This class contains methods to handle Invasive Species Simpplle Type Chooser, a type of Logic Data.  This allows users to pick the invasive species.  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */


public class LogicInvasiveSpeciesSimpplleTypeChooser extends JDialog {
  LogicInvasiveSpeciesSimpplleTypeTableDataModel dataModel;

  private InvasiveSpeciesLogicDataMSU logicData;
  private SimpplleType.Types          kind;
  private String                      description;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Process Table");
  BorderLayout borderLayout2 = new BorderLayout();
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
  /**
   * Constructor for Logic Invasive Species SImpplle Type Chooser.  Sets the dialog owner, title, modality, lotic data, and kind to either process or treatment.  
   * @param owner
   * @param title 
   * @param modal modality
   * @param logicData
   * @param kind either process or treatment 
   */
  public LogicInvasiveSpeciesSimpplleTypeChooser(JDialog owner, String title,
                             boolean modal, InvasiveSpeciesLogicDataMSU logicData,
                             SimpplleType.Types kind) {
    super(owner, title, modal);

    this.logicData = logicData;
    this.kind      = kind;
    this.dataModel = new LogicInvasiveSpeciesSimpplleTypeTableDataModel(kind);
    this.description = "";

    if (kind == SimpplleType.PROCESS) {
      if (logicData.isDefaultProcessCoeffDataDescription() == false) {
        this.description = logicData.getProcessCoeffDataDescription();
      }
    }
    else if (kind == SimpplleType.TREATMENT) {
      if (logicData.isDefaultTreatCoeffDataDescription() == false) {
        this.description = logicData.getTreatCoeffDataDescription();
      }
    }

    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      initialize();
      pack();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
/**
 * Overloaded constructor for Logic Invasive Species Simpplle type Chooser.  Creates a new dialog as owner, sets title, modality to false, and simpplle type to process
 */
  public LogicInvasiveSpeciesSimpplleTypeChooser() {
    this(new JDialog(), "LogicInvasiveSpeciesSimpplleTypeChooser", false,null,SimpplleType.PROCESS);
  }
/**
 * Init method which sets the layout, panels, componenets, text, and listeners for Logic Invasive Species Simpplle Type Chooser. 
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
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
    this.addWindowListener(new LogicInvasiveSpeciesSimpplleTypeChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(szTable);
    centerPanel.add(jPanel1);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
  }
/**
 * Initializes the Invasive Logic Species Simpplle Type Chooser data model, description, and three columns Simpplle type, coefficient, and time steps column
 * 
 */
  private void initialize() {
    dataModel.setLogicData(logicData);
    dataModel.setNonBaseCol(true);

    descText.setText(description);
    currentDesc.setText(description);

    TableColumn column = szTable.getColumnModel().getColumn(dataModel.SIMPPLLE_TYPE_COL);
    column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());

    column = szTable.getColumnModel().getColumn(dataModel.COEFF_COL);
    column.setCellRenderer(new MyJTextFieldRenderer());

    column = szTable.getColumnModel().getColumn(dataModel.TIME_STEPS_COL);
    column.setCellRenderer(new MyJTextFieldRenderer());
  }
/**
 * Method to handle window closing event.  This is called from the Window Adapter class in this class.
 * It sets the description to the description text box text, then uses this to and the kind (process or treatment) set in constructor to 
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    description = descText.getText().trim();
    if (description.length() == 0) { description = null; }

    if (kind == SimpplleType.PROCESS) {
      logicData.setProcessCoeffDataDescription(description);
    }
    else if (kind == SimpplleType.TREATMENT) {
      logicData.setTreatCoeffDataDescription(description);
    }

    setVisible(false);
  }
/**
 * Gets the description,  This is either a process or treatment.  If it is empty it is the default.  
 * @return invasive species logic description 
 */
  public String getDescription() {
    return description;
  }

}
/**
 * Window adapter class to create a window closing event that calls to window closing event in this class
 *
 */
class LogicInvasiveSpeciesSimpplleTypeChooser_this_windowAdapter extends WindowAdapter {
  private LogicInvasiveSpeciesSimpplleTypeChooser adaptee;
  LogicInvasiveSpeciesSimpplleTypeChooser_this_windowAdapter(LogicInvasiveSpeciesSimpplleTypeChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Calls to internal method for window closing event.  it will set either the process or treatment coefficient data description. 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
