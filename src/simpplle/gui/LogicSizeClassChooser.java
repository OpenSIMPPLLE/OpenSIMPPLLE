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
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import simpplle.comcode.SizeClass;
import simpplle.comcode.SimpplleType;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class creates the dialog for the Size Class Logic Chooser, a type of JDialog.  
 * It allows the user to configure the size class and size class structure. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller</p>
 * 
 */

public class LogicSizeClassChooser extends JDialog {
  FireLogicSizeClassTableDataModel dataModel =
      new FireLogicSizeClassTableDataModel(SimpplleType.SIZE_CLASS);
  LogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  JPanel structureInnerPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JCheckBox nonForestCB = new JCheckBox();
  JCheckBox singleStoryCB = new JCheckBox();
  JCheckBox multiStoryCB = new JCheckBox();
  Border border1 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border2 = new TitledBorder(border1, "Size Class / Structure");
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Size Class Table");
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel structurePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
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
   * Constructor for LogicSizeClassChooser.  References superclass and sets the dialog owner, title, modality and type of logic data.  
   * @param owner
   * @param title
   * @param modal
   * @param logicData
   */
  public LogicSizeClassChooser(JDialog owner, String title, boolean modal,
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
 * Overloaded constructor.  Passes to primary constructor a new dialog, the title LogicSizeClassChooser, false for modality and null for logic data.
 */
  public LogicSizeClassChooser() {
    this(new JDialog(), "LogicSizeClassChooser", false,null);
  }
/**
 * Initialized the Size Class Logic chooser with panels, components, borders, layouts, listeners, fonts, and text.
 * @throws Exception
 */
  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    spGroupPanel.setLayout(borderLayout3);
    structureInnerPanel.setLayout(gridLayout1);
    gridLayout1.setRows(3);
    nonForestCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    nonForestCB.setText("Non Forest");
    nonForestCB.addActionListener(new
        FireTypeLogicSizeClassChooser_nonForestCB_actionAdapter(this));
    singleStoryCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    singleStoryCB.setText("SingleStory      ");
    singleStoryCB.addActionListener(new
        FireTypeLogicSizeClassChooser_singleStoryCB_actionAdapter(this));
    multiStoryCB.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
    multiStoryCB.setText("Multiple Story");
    multiStoryCB.addActionListener(new
        FireTypeLogicSizeClassChooser_multiStoryCB_actionAdapter(this));
    structureInnerPanel.setBorder(border2);
    northPanel.setLayout(borderLayout2);
    structurePanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
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
    this.addWindowListener(new FireTypeLogicSizeClassChooser_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(szTable);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.NORTH);
    structureInnerPanel.add(nonForestCB);
    structureInnerPanel.add(singleStoryCB);
    structureInnerPanel.add(multiStoryCB);
    structureInnerPanel.add(multiStoryCB);
    centerPanel.add(jPanel1);
    structurePanel.add(structureInnerPanel);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
    spGroupPanel.add(structurePanel, java.awt.BorderLayout.WEST);
    structurePanel.add(structureInnerPanel);
  }
/**
 * Initializes the data model and selects the checkboxes for size class structure, based on whether the logic data has the structure in it.
 * If the logic data is not the default description sets the description text to the size class, otherwise it is left empty.  
 * Also sets current description to the size class.    
 */
  private void initialize() {
    dataModel.setLogicData(logicData);

    nonForestCB.setSelected(logicData.hasStructure(SizeClass.NON_FOREST));
    singleStoryCB.setSelected(logicData.hasStructure(SizeClass.SINGLE_STORY));
    multiStoryCB.setSelected(logicData.hasStructure(SizeClass.MULTIPLE_STORY));

    descText.setText("");
    if (logicData.isDefaultDescription(SimpplleType.SIZE_CLASS) == false) {
      descText.setText(logicData.getDescription(SimpplleType.SIZE_CLASS));
    }
    currentDesc.setText(logicData.getDescription(SimpplleType.SIZE_CLASS));
  }
/**
 * Handles the selection of non forest check box.  Sets the data logic structures to NON_FOREST, otherwise removes NON_FOREST from the logic data 
 * size class structures.  
 * @param e non forest check box selected
 */
  public void nonForestCB_actionPerformed(ActionEvent e) {
    if (nonForestCB.isSelected()) {
      logicData.addStructure(SizeClass.NON_FOREST);
    }
    else {
      logicData.removeStructure(SizeClass.NON_FOREST);
    }
  }
  /**
   * Handles the selection of single story check box.  Sets the data logic structures to SINGLE_STORY, otherwise removes SINGLE_STORY from the logic data 
   * size class structures.  
   * @param e single story check box selected
   */
  public void singleStoryCB_actionPerformed(ActionEvent e) {
    if (singleStoryCB.isSelected()) {
      logicData.addStructure(SizeClass.SINGLE_STORY);
    }
    else {
      logicData.removeStructure(SizeClass.SINGLE_STORY);
    }
  }
  /**
   * Handles the selection of multiple story check box.  Sets the data logic structures to MULTIPLE_STORY, otherwise removes MULTIPLE_STORY from the logic data 
   * size class structures.  
   * @param e multiple story check box selected
   */
  public void multiStoryCB_actionPerformed(ActionEvent e) {
    if (multiStoryCB.isSelected()) {
      logicData.addStructure(SizeClass.MULTIPLE_STORY);
    }
    else {
      logicData.removeStructure(SizeClass.MULTIPLE_STORY);
    }
  }

/**
 * If window closing event occurs sets the description in the logic data of comcode to the text in description text field.  
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    logicData.setDescription(descText.getText().trim(),SimpplleType.SIZE_CLASS);
  }
}
/**
 * Creates a window adaptor to handle a window closing event.  This passes to this_windowClosing() above. 
 */
class FireTypeLogicSizeClassChooser_this_windowAdapter extends WindowAdapter {
  private LogicSizeClassChooser adaptee;
  FireTypeLogicSizeClassChooser_this_windowAdapter(LogicSizeClassChooser
                                                 adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
/**
 * Creates a window adaptor to handle the selecting of multiStory structure check box.  It will close the window if a multiStory check box is selected. 
 *  
 */
class FireTypeLogicSizeClassChooser_multiStoryCB_actionAdapter implements
    ActionListener {
  private LogicSizeClassChooser adaptee;
  FireTypeLogicSizeClassChooser_multiStoryCB_actionAdapter(
      LogicSizeClassChooser adaptee) {
    this.adaptee = adaptee;
  }
/**
 * If multiStory structure check box is selected, will pass to the method within LogicSizeClassChooser that handles the event.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.multiStoryCB_actionPerformed(e);
  }
}
/**
 * Creates a window adaptor to handle the selecting of singleStory checkbox.  It will close the window if a singleStory structure check box is selected. 
 */
class FireTypeLogicSizeClassChooser_singleStoryCB_actionAdapter implements
    ActionListener {
  private LogicSizeClassChooser adaptee;
  FireTypeLogicSizeClassChooser_singleStoryCB_actionAdapter(
      LogicSizeClassChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * If singleStory structure check box is selected, will pass to the method within LogicSizeClassChooser that handles the event.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.singleStoryCB_actionPerformed(e);
  }
}
/**
 * Creates a window adaptor to handle the selecting of nonForest checkbox.  It will close the window if a nonForest structure check box is selected. 
 */
class FireTypeLogicSizeClassChooser_nonForestCB_actionAdapter implements
    ActionListener {
  private LogicSizeClassChooser adaptee;
  FireTypeLogicSizeClassChooser_nonForestCB_actionAdapter(
      LogicSizeClassChooser adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * If nonForest structure check box is selected, will pass to the method within LogicSizeClassChooser that handles the event.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.nonForestCB_actionPerformed(e);
  }
}
