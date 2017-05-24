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
    menuShowValCols.setText("Values Only");
    menuShowValCols.setToolTipText("Shows only columns that have values");
    menuShowValCols.addActionListener(this::menuShowValCols_actionPerformed);
    menuEcoGroup.setText("Ecological Grouping");
    menuEcoGroup.addActionListener(this::ecoGroupListener);
    menuSpecies.setText("Species");
    menuSpecies.addActionListener(this::speciesListener);
    menuSizeClass.setText("Size Class");
    menuSizeClass.addActionListener(this::sizeClassListener);
    menuDensity.setText("Density");
    menuDensity.addActionListener(this::densityListener);
    menuProcess.setText("Process");
    menuProcess.addActionListener(this::menuProcessListener);
    menuTreatment.setText("Treatment");
    menuTreatment.addActionListener(this::treatmentListener);
    menuSeason.setText("Season");
    menuSeason.addActionListener(this::seasonListener);
    menuMoisture.setText("Moisture");
    menuMoisture.addActionListener(this::moistureListener);
    menuTemp.setText("Temperature");
    menuTemp.addActionListener(this::menuTempListener);
    menuTrackingSpecies.setText("Tracking Species");
    menuTrackingSpecies.addActionListener(this::trackingSpeciesListener);
    menuOwnership.setText("Ownership");
    menuOwnership.addActionListener(this::ownershipListener);
    menuSpecialArea.setText("Special Area");
    menuSpecialArea.addActionListener(this::specialAreaListener);
    menuRoadStatus.setText("Road Status");
    menuRoadStatus.addActionListener(this::roadStatusListener);
    menuTrailStatus.setText("Trail Status");
    menuTrailStatus.addActionListener(this::trailStatusListener);
    menuLandtype.setText("Land Type");
    menuLandtype.addActionListener(this::landTypeListener);

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
    currentPanel.updateColumns();
  }
  /**
   * If show vals menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.
   */
   private void menuShowValCols_actionPerformed(ActionEvent e) {

    ArrayList<Integer> emptyCols = currentPanel.emptyColumns();

    if(menuShowValCols.isSelected()) {

      for(int i = 0; i < emptyCols.size(); i++){

        System.out.println(emptyCols.get(i));

        currentPanel.removeVisibleColumn(emptyCols.get(i));
      }
    } else{
      for(int i = 0; i < emptyCols.size(); i++){

        System.out.println(emptyCols.get(i));

        currentPanel.addVisibleColumn(emptyCols.get(i));

      }
    }
    updateMenuItems();
    currentPanel.updateColumns();
  }
  /**
   * If Eco Group menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void ecoGroupListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.ECO_GROUP_COL);
  }
  /**
   * If species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void speciesListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.SPECIES_COL);
  }
  /**
   * If Size Class menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void sizeClassListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.SIZE_CLASS_COL);
  }
  /**
   * If Density menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void densityListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.DENSITY_COL);
  }
  /**
   * If Process menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void menuProcessListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.PROCESS_COL);
  }
  /**
   * If Treatment menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void treatmentListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.TREATMENT_COL);
  }
  /**
   * If Season menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void seasonListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.SEASON_COL);
  }
  /**
   * If Moisture menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void moistureListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.MOISTURE_COL);
  }
  /**
   * If Temperature menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void menuTempListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.TEMP_COL);
  }
  /**
   * If Tracking Species menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void trackingSpeciesListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.TRACKING_SPECIES_COL);
  }
  /**
   * If Ownership menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void ownershipListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.OWNERSHIP_COL);
  }
  /**
   * If Special Area menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void specialAreaListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.SPECIAL_AREA_COL);
  }
  /**
   * If Road Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void roadStatusListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.ROAD_STATUS_COL);
  }
  /**
   * If Trail Status menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void trailStatusListener(ActionEvent e) {
    columnMenuClicked(BaseLogic.TRAIL_STATUS_COL);
  }
  /**
   * If Landtype menu item selected, calls to columnMenuClicked which adds a visible column to the current panel, hides unselected columns, and updates.  
   */
  private void landTypeListener(ActionEvent e) {
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
