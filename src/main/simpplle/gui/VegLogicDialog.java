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
import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.awt.Frame;
import simpplle.comcode.*;

/**
 * This class sets up Vegetative Logic Dialog, a type of Abstract Logic Dialog, which itself inherits from JDialog.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class VegLogicDialog extends AbstractLogicDialog {

  protected boolean inColumnInit = false;
  protected ArrayList<JMenuItem> colMenuItems = new ArrayList<>();
  protected JMenu menuColumns = new JMenu();
  private JCheckBoxMenuItem menuShowValCols = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuEcoGroup = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuSpecies = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuSizeClass = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuDensity = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuProcess = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuTreatment = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuSeason = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuMoisture = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuTemp = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuTrackingSpecies = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuOwnership = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuSpecialArea = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuRoadStatus = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuTrailStatus = new JCheckBoxMenuItem();
  private JCheckBoxMenuItem menuLandtype = new JCheckBoxMenuItem();

  /**
   * Constructor for Vegetative Logic Dialog, a type of abstract logic dialog.
   * @param owner Parent frame of the dialogue
   * @param title Title of the dialog
   * @param modal Specifies whether dialog blocks user input to other top-level windows when shown
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
   * @throws Exception Any exception thrown
   */
  private void jbInit() throws Exception {

    menuColumns.setText("Columns");
    menuShowValCols.setText("Values Only");
    menuShowValCols.setSelected(true);
    menuShowValCols.setToolTipText("Shows only columns that have values");
    menuShowValCols.addActionListener(e -> menuShowValCols_actionPerformed());
    menuEcoGroup.setText("Ecological Grouping");
    menuEcoGroup.addActionListener(e -> ecoGroupListener());
    menuSpecies.setText("Species");
    menuSpecies.addActionListener(e -> speciesListener());
    menuSizeClass.setText("Size Class");
    menuSizeClass.addActionListener(e -> sizeClassListener());
    menuDensity.setText("Density");
    menuDensity.addActionListener(e -> densityListener());
    menuProcess.setText("Process");
    menuProcess.addActionListener(e -> menuProcessListener());
    menuTreatment.setText("Treatment");
    menuTreatment.addActionListener(e -> treatmentListener());
    menuSeason.setText("Season");
    menuSeason.addActionListener(e -> seasonListener());
    menuMoisture.setText("Moisture");
    menuMoisture.addActionListener(e -> moistureListener());
    menuTemp.setText("Temperature");
    menuTemp.addActionListener(e -> menuTempListener());
    menuTrackingSpecies.setText("Tracking Species");
    menuTrackingSpecies.addActionListener(e -> trackingSpeciesListener());
    menuOwnership.setText("Ownership");
    menuOwnership.addActionListener(e -> ownershipListener());
    menuSpecialArea.setText("Special Area");
    menuSpecialArea.addActionListener(e -> specialAreaListener());
    menuRoadStatus.setText("Road Status");
    menuRoadStatus.addActionListener(e -> roadStatusListener());
    menuTrailStatus.setText("Trail Status");
    menuTrailStatus.addActionListener(e -> trailStatusListener());
    menuLandtype.setText("Land Type");
    menuLandtype.addActionListener(e -> landTypeListener());

    menuColumns.add(menuShowValCols);
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
   * @param column Index of column clicked
   */
  public void columnMenuClicked(int column) {
    if (inColumnInit) { return; }

    if (colMenuItems.get(column).isSelected()) {
      currentPanel.addVisibleColumn(column);
    }
    else {
      currentPanel.removeVisibleColumn(column);
    }
    if(menuShowValCols.isSelected()){
      menuShowValCols.setSelected(false);
    }
    currentPanel.updateColumns();
  }
  /**
   * If Show Vals menu item selected, calls to hideEmpty which removes all empty columns from the current panel and updates.
   * Otherwise, all of the hidden columns are shown.
   */
  private void menuShowValCols_actionPerformed() {

    if(menuShowValCols.isSelected()) {
      currentPanel.hideEmpty();
    } else{
      currentPanel.showEmpty();
    }
    updateMenuItems();
    currentPanel.updateColumns();
  }
  /**
   * If Eco Group menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void ecoGroupListener() {
    columnMenuClicked(BaseLogic.ECO_GROUP_COL);
  }
  /**
   * If species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void speciesListener() {
    columnMenuClicked(BaseLogic.SPECIES_COL);
  }
  /**
   * If Size Class menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void sizeClassListener() {
    columnMenuClicked(BaseLogic.SIZE_CLASS_COL);
  }
  /**
   * If Density menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void densityListener() {
    columnMenuClicked(BaseLogic.DENSITY_COL);
  }
  /**
   * If Process menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void menuProcessListener() {
    columnMenuClicked(BaseLogic.PROCESS_COL);
  }
  /**
   * If Treatment menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void treatmentListener() {
    columnMenuClicked(BaseLogic.TREATMENT_COL);
  }
  /**
   * If Season menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void seasonListener() {
    columnMenuClicked(BaseLogic.SEASON_COL);
  }
  /**
   * If Moisture menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void moistureListener() {
    columnMenuClicked(BaseLogic.MOISTURE_COL);
  }
  /**
   * If Temperature menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void menuTempListener() {
    columnMenuClicked(BaseLogic.TEMP_COL);
  }
  /**
   * If Tracking Species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void trackingSpeciesListener() {
    columnMenuClicked(BaseLogic.TRACKING_SPECIES_COL);
  }
  /**
   * If Ownership menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void ownershipListener() {
    columnMenuClicked(BaseLogic.OWNERSHIP_COL);
  }
  /**
   * If Special Area menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void specialAreaListener() {
    columnMenuClicked(BaseLogic.SPECIAL_AREA_COL);
  }
  /**
   * If Road Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void roadStatusListener() {
    columnMenuClicked(BaseLogic.ROAD_STATUS_COL);
  }
  /**
   * If Trail Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void trailStatusListener() {
    columnMenuClicked(BaseLogic.TRAIL_STATUS_COL);
  }
  /**
   * If Landtype menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void landTypeListener() {
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

  @Override
  public void tabbedPane_stateChanged(ChangeEvent e) {
    super.tabbedPane_stateChanged(e);
    // Update current tab appropriately
    menuShowValCols_actionPerformed();
  }
}
