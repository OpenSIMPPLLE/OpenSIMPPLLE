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
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the Logic Density Chooser dialog. 
 * 
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 *
 * 
 */

public class LogicDensityChooser extends JDialog {
  FireLogicDensityTableDataModel dataModel =
      new FireLogicDensityTableDataModel(SimpplleType.DENSITY);
  LogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Density Table");
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
   * Constructor for Logic Density Chooser.  Sets the JDialog owner (this is different from much of the GUI in which dialogs are owned by frames), 
   * title, and modality as well as the logic data to be used.  
   */
  public LogicDensityChooser(JDialog owner, String title, boolean modal,
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
   * Overloaded constructor for Logic Density Chooser.  This is specificaly for the Fire Type logic density chooser.  
   */
  public LogicDensityChooser() {
    this(new JDialog(), "FireTypeLogicDensityChooser", false,null);
  }
/**
 * Sets up the panels, layouts, and components for Logic Density Chooser.  
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
    this.addWindowListener(new FireTypeLogicDensityChooser_this_windowAdapter(this));
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
 * Initializes the Logic Density Chooser to methods and values for SimpplleType.DENSITY
 */
  private void initialize() {
    dataModel.setLogicData(logicData);

    descText.setText("");
    if (logicData.isDefaultDescription(SimpplleType.DENSITY) == false) {
      descText.setText(logicData.getDescription(SimpplleType.DENSITY));
    }
    currentDesc.setText(logicData.getDescription(SimpplleType.DENSITY));
  }
/**
 * Handles window closing event by setting the descrpition to the description text and 
 * @param e window closing  'X' 
 */
  public void this_windowClosing(WindowEvent e) {
    logicData.setDescription(descText.getText().trim(),SimpplleType.DENSITY);
  }
}
/**
 * Creates a window adapter to handle the window closing event when a user presses the 'X' window closing button.  
 *
 */
class FireTypeLogicDensityChooser_this_windowAdapter extends WindowAdapter {
  private LogicDensityChooser adaptee;
  FireTypeLogicDensityChooser_this_windowAdapter(LogicDensityChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Handles the window closing event by passing to method outside adapter class. 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
