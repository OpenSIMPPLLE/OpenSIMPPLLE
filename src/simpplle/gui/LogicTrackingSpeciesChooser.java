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

/**
 * This class creates the dialog for the Tracking Species Logic Chooser, a type of JDialog.
 * It allows the user to configure tracking species. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */
public class LogicTrackingSpeciesChooser extends JDialog {
  LogicTrackingSpeciesTableDataModel dataModel =
      new LogicTrackingSpeciesTableDataModel();
  LogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Species Table");
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
  
  /**
   * Constructor for Tracking Species Logic Chooser.  Passes to superclass the dialog owner, title, modality.  Also sets the logic data for this class.  
   * @param owner dialog owner
   * @param title string title of this dialog
   * @param modal
   * @param logicData
   */
  public LogicTrackingSpeciesChooser(JDialog owner, String title, boolean modal,
                                     LogicData logicData) {
    super(owner, title, modal);
    this.logicData = logicData;
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
   * Overloaded constructor for Species Logic Chooser.  Passes to primary constructor a new dialog as owner, makes title"LogicTrackingSpeciesChooser'
   * sets modality to false, and logic data to null.   
   */
  public LogicTrackingSpeciesChooser() {
    this(new JDialog(), "LogicTrackingSpeciesChooser", false,null);
  }
  /**
   * Initializes the dialog with layouts, components, listeners, data models, and panels
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
    this.addWindowListener(new LogicTrackingSpeciesChooser_this_windowAdapter(this));
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
  }
/**
 * Initializes the tracking species logic chooser with the logic data set in the constructor.  Empties the description text field.  
 * If the logic data is not the default tracking species description, sets the description text to tracking species description.  
 * Then sets the current description label to the tracking species description.  
 */
  private void initialize() {
    dataModel.setLogicData(logicData);

    descText.setText("");
    if (logicData.isDefaultTrackingSpeciesDesc() == false) {
      descText.setText(logicData.getTrackingSpeciesDesc());
    }
    currentDesc.setText(logicData.getTrackingSpeciesDesc());
  }
  /**
   * Handles window closing event.  Sets the logic data tracking species description to the description in the text field.  
   * @param e window closing event
   */
  public void this_windowClosing(WindowEvent e) {
    logicData.setTrackingSpeciesDesc(descText.getText().trim());
  }
}
/**
 * Creates a window adapter to handle the window closing event.  This saves on work in implementing window listeners.  
 * It passes to method within LogicTrackingSpeciesChooser class.  
 *
 */
class LogicTrackingSpeciesChooser_this_windowAdapter extends WindowAdapter {
  private LogicTrackingSpeciesChooser adaptee;
  LogicTrackingSpeciesChooser_this_windowAdapter(LogicTrackingSpeciesChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to this_windowClosing() within LogicTrackingSpeciesChooser class when window closing event occurred.
   */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
