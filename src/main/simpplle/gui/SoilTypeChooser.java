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

/**
 * This class creates the Soil Type Chooser dialog, a type of JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class SoilTypeChooser extends JDialog {
  SoilTypeDataModel dataModel = new SoilTypeDataModel();
  InvasiveSpeciesLogicData logicData;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JPanel spTablePanel = new JPanel();
  JPanel spGroupPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  Border border5 = BorderFactory.createLineBorder(SystemColor.controlText, 2);
  Border border6 = new TitledBorder(border5, "Soil Type Table");
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
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuActions = new JMenu();
  private JMenuItem menuActionsAddSoilType = new JMenuItem();
  private JMenuItem menuActionsAddSoilTypeFromFile = new JMenuItem();
  
  /**
   * Constructor for SoilType Chooser.  Sets the JDialog owner, 
   * title, and modality as well as the logic data to be used. 
   * @param owner JDialog that owners this dialog
   * @param title title of this dialog
   * @param modal specifies whether dialog blocks user input to other top-level windows when shown
   *
   */
  public SoilTypeChooser(JDialog owner, String title, boolean modal,
                         InvasiveSpeciesLogicData logicData) {
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
   * Overloaded constructor for SoilType Chooser dialog.  Sets the name to Pathway Chooser and owner to new dialog.    
   */
  public SoilTypeChooser() {
    this(new JDialog(), "SoilTypeChooser", false,null);
  }
  /**
   * Initializes the SoilType Chooser dialog with layouts, text, components, borders, and panels
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
    this.addWindowListener(new SoilTypeChooser_this_windowAdapter(this));
    menuActions.setText("Actions");
    menuActionsAddSoilType.setText("Add Soil Type");
    menuActionsAddSoilType.addActionListener(new
        SoilTypeChooser_menuActionsAddSoilType_actionAdapter(this));
    menuActionsAddSoilTypeFromFile.setText("Add Soil Types from File");
    menuActionsAddSoilTypeFromFile.addActionListener(new
        SoilTypeChooser_menuActionsAddSoilTypeFromFile_actionAdapter(this));
    this.setJMenuBar(menuBar);
    getContentPane().add(mainPanel);
    northPanel.add(spTablePanel, java.awt.BorderLayout.CENTER);
    spTablePanel.add(spScroll, java.awt.BorderLayout.NORTH);
    spScroll.getViewport().add(table);
    northPanel.add(spGroupPanel, java.awt.BorderLayout.NORTH);
    centerPanel.add(jPanel1);
    mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
    mainPanel.add(northPanel, java.awt.BorderLayout.NORTH);
    descPanel.add(descText, java.awt.BorderLayout.WEST);
    descPanel.add(descLabel, java.awt.BorderLayout.NORTH);
    jPanel1.add(currentDesc, java.awt.BorderLayout.CENTER);
    jPanel1.add(descPanel, java.awt.BorderLayout.NORTH);
    menuBar.add(menuActions);
    menuActions.add(menuActionsAddSoilType);
    menuActions.add(menuActionsAddSoilTypeFromFile);
  }
  /**
   * Sets the data model for this SoilType chooser to the Invasive Species Logic Data.  Sets the table to the soil type data model.
   * If the Soil Description for Invasive Species Logic data is the default soil description sets the description text to the soil type.  
   */
  private void initialize() {
    dataModel.setLogicData(logicData);
    table.setModel(dataModel);

    if (logicData != null) {
      descLabel.setVisible(true);
      descText.setVisible(true);
      currentDesc.setVisible(true);

      descText.setText("");
      if (logicData.isDefaultSoilTypeDescription() == false) {
        descText.setText(logicData.getSoilTypeDescription());
      }
      currentDesc.setText(logicData.getSoilTypeDescription());
    }
    else {
      descLabel.setVisible(false);
      descText.setVisible(false);
      currentDesc.setVisible(false);
    }
  }
/**
 * If window closing event occures and logic data is not null, sets the soil type description to the description text which can be the default 
 * description or one input by the user.  
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    if (logicData != null) {
      logicData.setSoilTypeDescription(descText.getText().trim());
    }
  }
/**
 * Adds a soil type to the data model from the user input description.  (This adds a row to the data model)
 * @param e
 */
  public void menuActionsAddSoilType_actionPerformed(ActionEvent e) {
    String soilTypeName = JOptionPane.showInputDialog("Soil Type");
    if (soilTypeName != null) {
      dataModel.addRow(soilTypeName);
    }
  }
/**
 * Adds a soil type to the data model from the soil types file.  
 * @param e "Add Soil Types from File" 
 */
  public void menuActionsAddSoilTypeFromFile_actionPerformed(ActionEvent e) {
    File file = Utility.getOpenFile(this,"Soil Types Import File");
    if (file != null) {
      dataModel.addRowsFromFile(file);
    }
  }
}

/**
 * Creates an action adapter to handle the clicking of a the "Add Soil Types from File" menu item.  This is a convenience method to 
 * make writing action listeners shorter.  It will add a soil type to the data model from the soil types file.  
 *
 */
class SoilTypeChooser_menuActionsAddSoilTypeFromFile_actionAdapter implements
    ActionListener {
  private SoilTypeChooser adaptee;
  SoilTypeChooser_menuActionsAddSoilTypeFromFile_actionAdapter(SoilTypeChooser
      adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Passes to menuActionsAddSoilTypeFromFile_actionPerformed(e) within the soiltype chooser.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionsAddSoilTypeFromFile_actionPerformed(e);
  }
}
/**
 * Creates an action adapter to handle the clicking of a the "Add Soil Types" menu item.  This is a convenience method to 
 * make writing action listeners shorter.  It will add a user input soil type to the data model.
 */
class SoilTypeChooser_menuActionsAddSoilType_actionAdapter implements
    ActionListener {
  private SoilTypeChooser adaptee;
  SoilTypeChooser_menuActionsAddSoilType_actionAdapter(SoilTypeChooser adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuActionsAddSoilType_actionPerformed(e);
  }
}
/**
 * Creates an window adapter to handle a window closing event.  This is a convenience method to 
 * make writing window listeners shorter.  It will add a user input soil type to the data model.
 *
 */
class SoilTypeChooser_this_windowAdapter extends WindowAdapter {
  private SoilTypeChooser adaptee;
  SoilTypeChooser_this_windowAdapter(SoilTypeChooser adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
