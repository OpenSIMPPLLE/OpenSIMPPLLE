/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import simpplle.comcode.LogicData;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import simpplle.comcode.Lifeform;
import simpplle.comcode.FireResistance;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.SimpplleType;

/**
 * This class creates the dialog for the Species Logic Chooser, a type of JDialog.
 * It allows the user to choose species to use. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 */

public class LogicSpeciesChooser extends JDialog {
  FireLogicSpeciesTableDataModel dataModel =
      new FireLogicSpeciesTableDataModel(SimpplleType.SPECIES);
  LogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  JPanel lifeformPanel = new JPanel();
  JPanel resistanceInnerPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JCheckBox lowResistCB = new JCheckBox();
  JCheckBox modResistCB = new JCheckBox();
  JCheckBox highResistCB = new JCheckBox();
  Border border1 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border2 = new TitledBorder(border1, "Fire Resistance");
  Border border3 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border4 = new TitledBorder(border3, "Lifeforms");
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Species Table");
  BorderLayout borderLayout2 = new BorderLayout();
  JCheckBox treesCB = new JCheckBox();
  JCheckBox herbCB = new JCheckBox();
  JCheckBox shrubCB = new JCheckBox();
  JCheckBox agrCB = new JCheckBox();
  JCheckBox naCB = new JCheckBox();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel lifeformInnerPanel = new JPanel();
  GridLayout gridLayout3 = new GridLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel resistancePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JScrollPane spScroll = new JScrollPane();
  JTable spTable = new JTable();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel jPanel1 = new JPanel();
  JTextField descText = new JTextField();
  JLabel descLabel = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel descPanel = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  JLabel currentDesc = new JLabel();
  /**
   * Constructor for Species Logic Chooser.  Passes to superclass the dialog owner, title, modality.  Also sets the logic data for this class.  
   * @param owner dialog owner
   * @param title string title of this dialog
   * @param modal
   * @param logicData
   */
  public LogicSpeciesChooser(JDialog owner, String title, boolean modal,
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
 * Overloaded constructor for Species Logic Chooser.  Passes to primary constructor a new dialog as owner, makes title"FireTypeLogicSpeciesChooser'
 * sets modality to false, and logic data to null.   
 */
  public LogicSpeciesChooser() {
    this(new JDialog(), "FireTypeLogicSpeciesChooser", false,null);
  }
/**
 * Initializes the dialog with layouts, components, listeners, data models, and panels
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    spGroupPanel.setLayout(borderLayout3);
    resistanceInnerPanel.setLayout(gridLayout1);
    lifeformPanel.setLayout(flowLayout1);
    gridLayout1.setRows(3);
    lowResistCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    lowResistCB.setText("Low");
    lowResistCB.addActionListener(new
        FireTypeLogicSpeciesChooser_lowResistCB_actionAdapter(this));
    modResistCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    modResistCB.setText("Moderate      ");
    modResistCB.addActionListener(new
        FireTypeLogicSpeciesChooser_modResistCB_actionAdapter(this));
    highResistCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    highResistCB.setText("High");
    highResistCB.addActionListener(new
        FireTypeLogicSpeciesChooser_highResistCB_actionAdapter(this));
    resistanceInnerPanel.setBorder(border2);
    northPanel.setLayout(borderLayout2);
    treesCB.setText("Trees");
    treesCB.addActionListener(new
                              FireTypeLogicSpeciesChooser_treesCB_actionAdapter(this));
    herbCB.setText("Herbacious");
    herbCB.addActionListener(new
                             FireTypeLogicSpeciesChooser_herbCB_actionAdapter(this));
    shrubCB.setText("Shrubs");
    shrubCB.addActionListener(new
                              FireTypeLogicSpeciesChooser_shrubCB_actionAdapter(this));
    agrCB.setText("Agriculture   ");
    agrCB.addActionListener(new FireTypeLogicSpeciesChooser_agrCB_actionAdapter(this));
    naCB.setText("no classification");
    naCB.addActionListener(new FireTypeLogicSpeciesChooser_naCB_actionAdapter(this));
    lifeformInnerPanel.setLayout(gridLayout3);
    gridLayout3.setRows(5);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    lifeformInnerPanel.setBorder(border4);
    lifeformPanel.setBorder(null);
    resistancePanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    spTablePanel.setLayout(borderLayout4);
    spTablePanel.setBorder(border6);
    spTable.setModel(dataModel);
    centerPanel.setLayout(flowLayout3);
    jPanel1.setLayout(borderLayout5);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    descText.setCaretPosition(0);
    descText.setColumns(40);
    descLabel.setText("Description (Cell Value).  Leave blank for default");
    centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    descPanel.setLayout(borderLayout6);
    this.addWindowListener(new FireTypeLogicSpeciesChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(spTable);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.NORTH);
    resistanceInnerPanel.add(lowResistCB);
    resistanceInnerPanel.add(modResistCB);
    resistanceInnerPanel.add(highResistCB);
    resistanceInnerPanel.add(highResistCB);
    lifeformPanel.add(lifeformInnerPanel);
    lifeformInnerPanel.add(treesCB);
    lifeformInnerPanel.add(shrubCB);
    lifeformInnerPanel.add(herbCB);
    lifeformInnerPanel.add(agrCB);
    lifeformInnerPanel.add(naCB);
    centerPanel.add(jPanel1);
    lifeformInnerPanel.add(herbCB);
    resistancePanel.add(resistanceInnerPanel);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
    spGroupPanel.add(lifeformPanel, java.awt.BorderLayout.CENTER);
    spGroupPanel.add(resistancePanel, java.awt.BorderLayout.WEST);
    resistancePanel.add(resistanceInnerPanel);
  }
/**
 * Initializes the Logic Species chooser with checkboxes for fire resistance and lifeforms.  
 */
  private void initialize() {
    dataModel.setLogicData(logicData);

    lowResistCB.setSelected(logicData.hasFireResistance(FireResistance.LOW));
    modResistCB.setSelected(logicData.hasFireResistance(FireResistance.MODERATE));
    highResistCB.setSelected(logicData.hasFireResistance(FireResistance.HIGH));

    treesCB.setSelected(logicData.hasLifeform(Lifeform.TREES));
    shrubCB.setSelected(logicData.hasLifeform(Lifeform.SHRUBS));
    herbCB.setSelected(logicData.hasLifeform(Lifeform.HERBACIOUS));
    agrCB.setSelected(logicData.hasLifeform(Lifeform.AGRICULTURE));
    naCB.setSelected(logicData.hasLifeform(Lifeform.NA));

    descText.setText("");
    if (logicData.isDefaultDescription(SimpplleType.SPECIES) == false) {
      descText.setText(logicData.getDescription(SimpplleType.SPECIES));
    }
    currentDesc.setText(logicData.getDescription(SimpplleType.SPECIES));
  }
  /**
   * Handles the event when low fire resistance check box is selected.  
   * If selected, adds a fire resistance object set to LOW fire resistance to logic data.  
   * Otherwise removes any fire resistance = LOW object from logic data
   * @param e lowResistCB selected
   */
  public void lowResistCB_actionPerformed(ActionEvent e) {
    if (lowResistCB.isSelected()) {
      logicData.addFireResistance(FireResistance.LOW);
    }
    else {
      logicData.removeFireResistance(FireResistance.LOW);
    }
  }
  /**
   * Handles the event when high fire resistance check box is selected.  
   * If selected, adds a fire resistance object set to MODERATE fire resistance to logic data.  
   * Otherwise removes any fire resistance = MODERATE object from logic data
   * @param e modResistCB selected
   */
  public void modResistCB_actionPerformed(ActionEvent e) {
    if (modResistCB.isSelected()) {
      logicData.addFireResistance(FireResistance.MODERATE);
    }
    else {
      logicData.removeFireResistance(FireResistance.MODERATE);
    }
  }
  /**
   * Handles the event when high fire resistance check box is selected.  
   * If selected, adds a fire resistance object set to HIGH fire resistance to logic data.  
   * Otherwise removes any fire resistance = HIGH object from logic data
   * @param e highResistCB selected
   */
  public void highResistCB_actionPerformed(ActionEvent e) {
    if (highResistCB.isSelected()) {
      logicData.addFireResistance(FireResistance.HIGH);
    }
    else {
      logicData.removeFireResistance(FireResistance.HIGH);
    }
  }
  
  /**
   * Handles the event when trees resistance check box is selected.  
   * If selected, adds a trees Lifeform object to logic data.  
   * Otherwise removes any Lifeform = TREES object from logic data
   * @param e treesCB selected
   */
  public void treesCB_actionPerformed(ActionEvent e) {
    if (treesCB.isSelected()) {
      logicData.addLifeform(Lifeform.TREES);
    }
    else {
      logicData.removeLifeform(Lifeform.TREES);
    }
  }
  /**
   * Handles the event when shrubs resistance check box is selected.  
   * If selected, adds a shrub Lifeform object to logic data.  
   * Otherwise removes any Lifeform = SHRUBS object from logic data
   * @param e shrubCB selected
   */
  public void shrubCB_actionPerformed(ActionEvent e) {
    if (shrubCB.isSelected()) {
      logicData.addLifeform(Lifeform.SHRUBS);
    }
    else {
      logicData.removeLifeform(Lifeform.SHRUBS);
    }
  }
  /**
   * Handles the event when herbacious resistance check box is selected.  
   * If selected, adds a herbacious Lifeform object to logic data.  
   * Otherwise removes any Lifeform = HERBACIOUS object from logic data
   * @param e herbCB selected
   */
  public void herbCB_actionPerformed(ActionEvent e) {
    if (herbCB.isSelected()) {
      logicData.addLifeform(Lifeform.HERBACIOUS);
    }
    else {
      logicData.removeLifeform(Lifeform.HERBACIOUS);
    }
  }
  /**
   * Handles the event when agriculture resistance check box is selected.  
   * If selected, adds a agriculture Lifeform object to logic data.  
   * Otherwise removes any Lifeform = AGRICULTURE object from logic data
   * @param e agrCB selected
   */
  public void agrCB_actionPerformed(ActionEvent e) {
    if (agrCB.isSelected()) {
      logicData.addLifeform(Lifeform.AGRICULTURE);
    }
    else {
      logicData.removeLifeform(Lifeform.AGRICULTURE);
    }
  }
  /**
   * Handles the event when no classification resistance check box is selected.  
   * If selected, adds a no classification Lifeform object to logic data.  
   * Otherwise removes any Lifeform = NA object from logic data
   * @param e naCB selected
   */
  public void naCB_actionPerformed(ActionEvent e) {
    if (naCB.isSelected()) {
      logicData.addLifeform(Lifeform.NA);
    }
    else {
      logicData.removeLifeform(Lifeform.NA);
    }
  }
/**
 * Handles window closing event.  Sets the logic data description to the description in the text field.  
 * @param e window closing event
 */
  public void this_windowClosing(WindowEvent e) {
    logicData.setDescription(descText.getText().trim(),SimpplleType.SPECIES);
  }
}
/**
 * Creates a window adapter to handle the window closing event.  This saves on work in implementing window listeners.  
 * It passes to method within LogicSpeciesChooser class.  
 *
 */
class FireTypeLogicSpeciesChooser_this_windowAdapter extends WindowAdapter {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_this_windowAdapter(LogicSpeciesChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when naCB selected.  
 *
 */
class FireTypeLogicSpeciesChooser_naCB_actionAdapter implements ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_naCB_actionAdapter(LogicSpeciesChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Passes to method within LogicSpeciesChooser class when naCB selected.
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.naCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when agrCB selected.
 */
class FireTypeLogicSpeciesChooser_agrCB_actionAdapter implements ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_agrCB_actionAdapter(LogicSpeciesChooser
                                                  adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when agrCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.agrCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when herbCB selected.
 */
class FireTypeLogicSpeciesChooser_herbCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_herbCB_actionAdapter(LogicSpeciesChooser
      adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when herbCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.herbCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when shrubCB selected.
 */
class FireTypeLogicSpeciesChooser_shrubCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_shrubCB_actionAdapter(LogicSpeciesChooser
      adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when shrubCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.shrubCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when treesCB selected.
 */
class FireTypeLogicSpeciesChooser_treesCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_treesCB_actionAdapter(LogicSpeciesChooser
      adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when treesCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.treesCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when highResistCB selected.
 */
class FireTypeLogicSpeciesChooser_highResistCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_highResistCB_actionAdapter(
      LogicSpeciesChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when highResistCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.highResistCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when modResistCB selected.
 */
class FireTypeLogicSpeciesChooser_modResistCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_modResistCB_actionAdapter(
      LogicSpeciesChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when modResistCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.modResistCB_actionPerformed(e);
  }
}
/**
 * Creates a window adapter to handle the check box action listener.  This saves on work in implementing action listeners.  
 * It passes to method within LogicSpeciesChooser class when lowResistCB selected.
 */
class FireTypeLogicSpeciesChooser_lowResistCB_actionAdapter implements
    ActionListener {
  private LogicSpeciesChooser adaptee;
  FireTypeLogicSpeciesChooser_lowResistCB_actionAdapter(
      LogicSpeciesChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Passes to method within LogicSpeciesChooser class when lowResistCB selected.
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.lowResistCB_actionPerformed(e);
  }
}
