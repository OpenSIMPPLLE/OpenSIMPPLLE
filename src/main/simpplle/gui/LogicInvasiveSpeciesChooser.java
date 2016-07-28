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
import simpplle.comcode.InvasiveSpeciesLogicData;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import simpplle.comcode.SoilType;
import java.io.File;
import simpplle.comcode.InvasiveSpeciesChangeLogicData;
import javax.swing.JComboBox;
import simpplle.comcode.Species;
import java.util.ArrayList;

/** 
 * This class creates the Logic Invasive Species Chooser dialog, a type of JDialog.
 * Only two regions currently support invasive species logic.  They are Western Region 1 and Teton. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class LogicInvasiveSpeciesChooser extends JDialog {
  LogicInvasiveSpeciesDataModel dataModel = new LogicInvasiveSpeciesDataModel();
  LogicData logicData;
  String                   kind;
  boolean inInit = false;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Invasive Species Table");
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JScrollPane spScroll = new JScrollPane();
  JTable table = new JTable();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel jPanel1 = new JPanel();
  JTextField descText = new JTextField();
  JLabel descLabel = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel descPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel currentDesc = new JLabel();
  private JPanel repSpeciesPanel = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JLabel repSpeciesLabel = new JLabel();
  private JComboBox repSpeciesCB = new JComboBox();
  
  /**
   * Constructor for Invasive Species Logic Chooser.  This is a dialog to allow for user input of invasive species.  
   * 
   * @param owner
   * @param title
   * @param modal
   * @param logicData
   */
  public LogicInvasiveSpeciesChooser(JDialog owner, String title, boolean modal,
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
 * Overloaded constructor for Invasive species chooser.  Creates a new dialog as the owner, initializes the name to InvasiveSpeciesChooser. and sets the modality to false, and logic data to null.
 * This allows for the creation of logic data.   
 */
  public LogicInvasiveSpeciesChooser() {
    this(new JDialog(), "InvasiveSpeciesChooser", false,null);
  }
/**
 * Initializes the Invasive Species Chooser dialog with layouts, panels, components, and listeners.  
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    spGroupPanel.setLayout(borderLayout3);
    gridLayout1.setRows(3);
    northPanel.setLayout(borderLayout2);
    spTablePanel.setLayout(borderLayout4);
    spTablePanel.setBorder(border6);
    centerPanel.setLayout(flowLayout3);
    jPanel1.setLayout(borderLayout5);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    descText.setCaretPosition(0);
    descText.setColumns(40);
    descLabel.setText("Description (Cell Value).  Leave blank for default");
    centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    descPanel.setLayout(borderLayout6);
    this.addWindowListener(new LogicInvasiveSpeciesChooser_this_windowAdapter(this));
    repSpeciesPanel.setLayout(flowLayout1);
    repSpeciesLabel.setToolTipText("");
    repSpeciesLabel.setText("Representative Species");
    repSpeciesCB.addActionListener(new
        LogicInvasiveSpeciesChooser_repSpeciesCB_actionAdapter(this));
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(table);
    centerPanel.add(jPanel1);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.SOUTH);
    northPanel.add(repSpeciesPanel, java.awt.BorderLayout.NORTH);
    repSpeciesPanel.add(repSpeciesLabel);
    repSpeciesPanel.add(repSpeciesCB);

  }
/**
 * Initializes the dialog data model to the input logic data.  If the the logic data is invasive species logic data 
 * OR invasive species change logic, and the logic data is different from the default invasive description, the description text field and the current description label 
 * will be set to the string description of the invasive species. 
 */
  private void initialize() {
    dataModel.setLogicData(logicData);
    dataModel.setDialog(this);
    table.setModel(dataModel);

    descText.setText("");
    if (logicData instanceof InvasiveSpeciesLogicData) {
      if (((InvasiveSpeciesLogicData)logicData).isDefaultInvasiveSpeciesDescription() == false) {
        descText.setText(((InvasiveSpeciesLogicData)logicData).getInvasiveSpeciesDescription());
      }
      currentDesc.setText(((InvasiveSpeciesLogicData)logicData).getInvasiveSpeciesDescription());
    }
    else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
      if (((InvasiveSpeciesChangeLogicData)logicData).isDefaultInvasiveSpeciesDescription() == false) {
        descText.setText(((InvasiveSpeciesChangeLogicData)logicData).getInvasiveSpeciesDescription());
      }
      currentDesc.setText(((InvasiveSpeciesChangeLogicData)logicData).getInvasiveSpeciesDescription());
    }

    repSpeciesCB.setEnabled(false);
    updateDialog();
  }
/**
 * Updates the Logic Invasive Species dialog.  This is called from the LogicInvasiveSpeciesTableModel, as well as initialize method in this class.
 * Sets the species list and representative species variable to either invasive species logic or invasive
 * species change logic data list.  If there is no species present, representative species methods are not in play, if there are species present 
 * will create combo boxes for all species in list and then finally changes the inInit (meaning in initial logic data) to falsse. 
 */
  public void updateDialog() {
    inInit = true;
    ArrayList<Species> speciesList=null;
    Species repSpecies=null;
    if (logicData instanceof InvasiveSpeciesLogicData) {
      speciesList = ((InvasiveSpeciesLogicData)logicData).getInvasiveSpeciesList();
      repSpecies  = ((InvasiveSpeciesLogicData)logicData).getRepSpecies();
    }
    else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
      speciesList = ((InvasiveSpeciesChangeLogicData)logicData).getInvasiveSpeciesList();
      repSpecies  = ((InvasiveSpeciesChangeLogicData)logicData).getRepSpecies();
    }

    if (speciesList == null || speciesList.size() == 0) {
      repSpeciesCB.setEnabled(false);
      return;
    }

    repSpeciesCB.setEnabled(true);
    repSpeciesCB.removeAllItems();
    if (speciesList != null) {
      for (Species species : speciesList) {
        repSpeciesCB.addItem(species);
      }
    }

    if (repSpecies != null && speciesList.contains(repSpecies)) {
      repSpeciesCB.setSelectedItem(repSpecies);
    }
    else {
      repSpeciesCB.setSelectedIndex(0);
    }
    inInit = false;
  }
/**
 * Method which is called by window closing adapter class upon window closing 'X' button event.  
 * Sets invasive species description to either the invasive species or invasive species change logic description text field info. 
 * 
 * @param e window closing 'X'
 */
  public void this_windowClosing(WindowEvent e) {
    if (logicData instanceof InvasiveSpeciesLogicData) {
      ((InvasiveSpeciesLogicData)logicData).setInvasiveSpeciesDescription(descText.getText().trim());
    }
    else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
      ((InvasiveSpeciesChangeLogicData)logicData).setInvasiveSpeciesDescription(descText.getText().trim());
    }
  }
/**
 * Sets the invasive species representative species when a representative species is choosen from the combo box.  
 * These combo boxes are set in the updateDialog() method within this class. 
 * 
 * @param e representative species choosen from combo box.  
 */
  public void repSpeciesCB_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    Species species = (Species)repSpeciesCB.getSelectedItem();

    if (logicData instanceof InvasiveSpeciesLogicData) {
      ((InvasiveSpeciesLogicData)logicData).setRepSpecies(species);
    }
    else if (logicData instanceof InvasiveSpeciesChangeLogicData) {
      ((InvasiveSpeciesChangeLogicData)logicData).setRepSpecies(species);
    }
  }

}
/**
 * Action adapter to handle the choosing of a representation 
 *
 */
class LogicInvasiveSpeciesChooser_repSpeciesCB_actionAdapter implements
    ActionListener {
  private LogicInvasiveSpeciesChooser adaptee;
  LogicInvasiveSpeciesChooser_repSpeciesCB_actionAdapter(
      LogicInvasiveSpeciesChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Handles the representative species combo box events.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.repSpeciesCB_actionPerformed(e);
  }
}
/**
 * Adapter class to handle the window closing event.  It calls to the this_windowClosing in this class.  
 *
 */
class LogicInvasiveSpeciesChooser_this_windowAdapter extends WindowAdapter {
  private LogicInvasiveSpeciesChooser adaptee;
  LogicInvasiveSpeciesChooser_this_windowAdapter(LogicInvasiveSpeciesChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Call to this_windowClosing method which sets te representative species 
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
