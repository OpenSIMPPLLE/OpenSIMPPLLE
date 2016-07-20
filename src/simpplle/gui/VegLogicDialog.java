/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Frame;
import simpplle.comcode.*;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Vegetative Logic Dialog, a type of Abstract Logic Dialog, which itself inherits from JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class VegLogicDialog extends AbstractLogicDialog {
  protected boolean              inColumnInit=false;
  protected ArrayList<JMenuItem> colMenuItems = new ArrayList<JMenuItem>();

  protected JMenu menuColumns = new JMenu();
  protected JCheckBoxMenuItem menuEcoGroup = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuSpecies = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuSizeClass = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuDensity = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuProcess = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuTreatment = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuSeason = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuMoisture = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuTemp = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuTrackingSpecies = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuOwnership = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuSpecialArea = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuRoadStatus = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuTrailStatus = new JCheckBoxMenuItem();
  protected JCheckBoxMenuItem menuLandtype = new JCheckBoxMenuItem();
/**
 * Constructor for Vegetative Logic Dialog, a type of abstract logic dialog.  
 * @param owner
 * @param title
 * @param modal
 */
  protected VegLogicDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * Overloaded constructor for Vegetative Logic Dialog.  Calls to the abstract logic dialog superclass, which makes a dialog in the usual 
   * OpenSimpplle way by designating the frame owner, title, and modality.  
   */
  public VegLogicDialog() {
    super();
  }
/**
 * Initializes the VegLogicDialog with a Jmenu to select columns by a series of checkboxes added to it.     
 * @throws Exception
 */
  private void jbInit() throws Exception {
    menuColumns.setText("Columns");
    menuEcoGroup.setText("Ecological Grouping");
    menuEcoGroup.addActionListener(new
        VegLogicDialog_menuEcoGroup_actionAdapter(this));
    menuSpecies.setText("Species");
    menuSpecies.addActionListener(new
        VegLogicDialog_menuSpecies_actionAdapter(this));
    menuSizeClass.setText("Size Class");
    menuSizeClass.addActionListener(new
        VegLogicDialog_menuSizeClass_actionAdapter(this));
    menuDensity.setText("Density");
    menuDensity.addActionListener(new
        VegLogicDialog_menuDensity_actionAdapter(this));
    menuProcess.setText("Process");
    menuProcess.addActionListener(new
        VegLogicDialog_menuProcess_actionAdapter(this));
    menuTreatment.setText("Treatment");
    menuTreatment.addActionListener(new
        VegLogicDialog_menuTreatment_actionAdapter(this));
    menuSeason.setText("Season");
    menuSeason.addActionListener(new
        VegLogicDialog_menuSeason_actionAdapter(this));
    menuMoisture.setText("Moisture");
    menuMoisture.addActionListener(new
        VegLogicDialog_menuMoisture_actionAdapter(this));
    menuTemp.setText("Temperature");
    menuTemp.addActionListener(new
        VegLogicDialog_menuTemp_actionAdapter(this));
    menuTrackingSpecies.setText("Tracking Species");
    menuTrackingSpecies.addActionListener(new
      VegLogicDialog_menuTrackingSpecies_actionAdapter(this));
    menuOwnership.setText("Ownership");
    menuOwnership.addActionListener(new
      VegLogicDialog_menuOwnership_actionAdapter(this));
    menuSpecialArea.setText("Special Area");
    menuSpecialArea.addActionListener(new
      VegLogicDialog_menuSpecialArea_actionAdapter(this));
    menuRoadStatus.setText("Road Status");
    menuRoadStatus.addActionListener(new
      VegLogicDialog_menuRoadStatus_actionAdapter(this));
    menuTrailStatus.setText("Trail Status");
    menuTrailStatus.addActionListener(new
      VegLogicDialog_menuTrailStatus_actionAdapter(this));
    menuLandtype.setText("Land Type");
    menuLandtype.addActionListener(new
      VegLogicDialog_menuLandtype_actionAdapter(this));
    menuColumns.add(menuEcoGroup);
    menuColumns.add(menuSpecies);
    menuColumns.add(menuSizeClass);
    menuColumns.add(menuDensity);
    menuColumns.add(menuProcess);
    menuColumns.add(menuTreatment);
    menuColumns.add(menuSeason);
    menuColumns.add(menuMoisture);
    menuColumns.add(menuTemp);
    menuColumns.add(menuTrackingSpecies);
    menuColumns.add(menuOwnership);
    menuColumns.add(menuSpecialArea);
    menuColumns.add(menuRoadStatus);
    menuColumns.add(menuTrailStatus);
    menuColumns.add(menuLandtype);
    menuBar.add(menuColumns);
  }
/**
 * Initializes the Vegetative Logic Dialog with column menu items representing rows for for ROW or 0 column (Priority), Ecological group, species, size class, process, treatment, season, moisture, temperature, 
 * tracking species, ownership, special area, road status, trail status, and landtype.  
 */
  protected void initialize(String[] kinds) {
    super.initialize(kinds);

    colMenuItems.clear();
    colMenuItems.add(BaseLogic.ROW_COL,null);
    colMenuItems.add(BaseLogic.ECO_GROUP_COL,menuEcoGroup);
    colMenuItems.add(BaseLogic.SPECIES_COL,menuSpecies);
    colMenuItems.add(BaseLogic.SIZE_CLASS_COL,menuSizeClass);
    colMenuItems.add(BaseLogic.DENSITY_COL,menuDensity);
    colMenuItems.add(BaseLogic.PROCESS_COL,menuProcess);
    colMenuItems.add(BaseLogic.TREATMENT_COL,menuTreatment);
    colMenuItems.add(BaseLogic.SEASON_COL,menuSeason);
    colMenuItems.add(BaseLogic.MOISTURE_COL,menuMoisture);
    colMenuItems.add(BaseLogic.TEMP_COL,menuTemp);
    colMenuItems.add(BaseLogic.TRACKING_SPECIES_COL,menuTrackingSpecies);
    colMenuItems.add(BaseLogic.OWNERSHIP_COL,menuOwnership);
    colMenuItems.add(BaseLogic.SPECIAL_AREA_COL,menuSpecialArea);
    colMenuItems.add(BaseLogic.ROAD_STATUS_COL,menuRoadStatus);
    colMenuItems.add(BaseLogic.TRAIL_STATUS_COL,menuTrailStatus);
    colMenuItems.add(BaseLogic.LANDTYPE_COL,menuLandtype);
  }
  /**
   * Creates an arraylist of selected columns menu items..  
   * @param selected true if selected
   * @param col column number 
   */
  public void setColumnMenuItemSelected(boolean selected, int col) {
    inColumnInit = true;
    colMenuItems.get(col).setSelected(selected);
    inColumnInit = false;
  }
  /**
   * Takes in the int representation of a column.  If the column was in the initial columns set in the init method, does nothing
   * @param column
   */
  protected void columnMenuClicked(int column) {
    if (inColumnInit) { return; }

    if (colMenuItems.get(column).isSelected()) {
      currentPanel.addVisibleColumn(column);
//      currentPanel.showColumn(column);
    }
    else {
      currentPanel.removeVisibleColumn(column);
//      currentPanel.hideColumn(column);
    }
    currentPanel.updateColumns();
  }
  /**
   * If Eco Group menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuEcoGroup_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.ECO_GROUP_COL);
  }
  /**
   * If species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuSpecies_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.SPECIES_COL);
  }
  /**
   * If Size Class menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuSizeClass_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.SIZE_CLASS_COL);
  }
  /**
   * If Density menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuDensity_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.DENSITY_COL);
  }
  /**
   * If Process menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuProcess_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.PROCESS_COL);
  }
  /**
   * If Treatment menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuTreatment_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.TREATMENT_COL);
  }
  /**
   * If Season menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuSeason_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.SEASON_COL);
  }
  /**
   * If Moisture menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuMoisture_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.MOISTURE_COL);
  }
  /**
   * If Temperature menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuTemp_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.TEMP_COL);
  }
  /**
   * If Tracking Species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuTrackingSpecies_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.TRACKING_SPECIES_COL);
  }
  /**
   * If Ownership menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuOwnership_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.OWNERSHIP_COL);
  }
  /**
   * If Special Area menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuSpecialArea_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.SPECIAL_AREA_COL);
  }
  /**
   * If Road Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuRoadStatus_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.ROAD_STATUS_COL);
  }
  /**
   * If Trail Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuTrailStatus_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.TRAIL_STATUS_COL);
  }
  /**
   * If Landtype menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  public void menuLandtype_actionPerformed(ActionEvent e) {
    columnMenuClicked(BaseLogic.LANDTYPE_COL);
  }
/**
 * Updates the menu items.  Gets the selected menu items, calls to the super update menu items class and sets the current panels visible columns.  
 */
  protected void updateMenuItems() {
    super.updateMenuItems();
    for (int i=0; i<colMenuItems.size(); i++) {
      if (colMenuItems.get(i) == null) { continue; }
      colMenuItems.get(i).setSelected(
        currentPanel.isVisibleColumn(i));
    }
  }

}
/**
 * Ownership action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuOwnership_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuOwnership_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
/**
 * Ownership JMenu item selected.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuOwnership_actionPerformed(e);
  }
}
/**
 * Special area action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuSpecialArea_actionAdapter implements ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuSpecialArea_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Special area JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuSpecialArea_actionPerformed(e);
  }
}
/**
 * Road status action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuRoadStatus_actionAdapter implements ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuRoadStatus_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Road status JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuRoadStatus_actionPerformed(e);
  }
}
/**
 * Trail status action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuTrailStatus_actionAdapter implements ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuTrailStatus_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Trail status JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuTrailStatus_actionPerformed(e);
  }
}
/**
 * Landtype action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuLandtype_actionAdapter implements ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuLandtype_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Landtype JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuLandtype_actionPerformed(e);
  }
}
/**
 * Tracking species action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuTrackingSpecies_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuTrackingSpecies_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Tracking Species JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuTrackingSpecies_actionPerformed(e);
  }
}
/**
 * Temperature action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuTemp_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuTemp_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Temperature JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuTemp_actionPerformed(e);
  }
}
/**
 * Moisture action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuMoisture_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuMoisture_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Moisture JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuMoisture_actionPerformed(e);
  }
}
/**
 * Season action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuSeason_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuSeason_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Season JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuSeason_actionPerformed(e);
  }
}
/**
 * Treatment action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuTreatment_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuTreatment_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Treatment JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuTreatment_actionPerformed(e);
  }
}
/**
 * Process action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuProcess_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuProcess_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Process JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuProcess_actionPerformed(e);
  }
}
/**
 * Density action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuDensity_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuDensity_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Density JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuDensity_actionPerformed(e);
  }
}
/**
 * Size Class action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuSizeClass_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuSizeClass_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Size Class JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuSizeClass_actionPerformed(e);
  }
}
/**
 * Species action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 *
 */
class VegLogicDialog_menuSpecies_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuSpecies_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * Species JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuSpecies_actionPerformed(e);
  }
}
/**
 * EcoGroup action adapter class.  Creates an adapter on action listener that allows only the action of selecting menu item to be needed.  
 */
class VegLogicDialog_menuEcoGroup_actionAdapter implements
    ActionListener {
  private VegLogicDialog adaptee;
  VegLogicDialog_menuEcoGroup_actionAdapter(
      VegLogicDialog adaptee) {
    this.adaptee = adaptee;
  }
  /**
   * EcoGroup JMenu item selected.  
   */
  public void actionPerformed(ActionEvent e) {
    adaptee.menuEcoGroup_actionPerformed(e);
  }
}

