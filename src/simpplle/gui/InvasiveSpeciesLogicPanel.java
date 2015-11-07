package simpplle.gui;

import simpplle.comcode.logic.BaseLogic;
import javax.swing.table.TableColumn;

import simpplle.comcode.logic.InvasiveSpeciesLogic;
import simpplle.comcode.logic.InvasiveSpeciesLogicData;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.Species;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import simpplle.comcode.logic.InvasiveSpeciesChangeLogicData;
import simpplle.comcode.logic.AbstractLogicData;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class InvasiveSpeciesLogicPanel extends VegLogicPanel {
  public InvasiveSpeciesLogicPanel(AbstractLogicDialog dialog,
                                   String kind, BaseLogic logicInst, Kinds sysKnowKind) {
    super(dialog,kind,logicInst,sysKnowKind);
    try {
      initialize();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  protected void initialize() {
    initializeBase();
  }
/**
 * Method to insert a row and therefore an invasive species logic.  
 */
  public void insertRow() {
    ArrayList<Species> invasiveSpeciesList = Species.getInvasiveSpeciesList();

    if (invasiveSpeciesList == null || invasiveSpeciesList.size() == 0) {
      JOptionPane.showMessageDialog(dialog,
                                    "No Invasive Species",
                                    "No Invasive Species",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    Species[] possibleValues = invasiveSpeciesList.toArray(new Species[invasiveSpeciesList.size()]);

    Species selectedValue = (Species)JOptionPane.showInputDialog(null,
        "Choose Invasive Species", "Choose Invasive Species",
        JOptionPane.INFORMATION_MESSAGE, null,
        possibleValues, possibleValues[0]);

    if (selectedValue == null) { return; }

    int selectedRowSave = selectedRow;
    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount() + 1;
    dataModel.addRow(position);

    position = (selectedRowSave != -1) ? selectedRowSave : dataModel.getRowCount()-1;
    AbstractLogicData data = dataModel.getValueAt(position);
    if (data instanceof InvasiveSpeciesLogicData) {
      ((InvasiveSpeciesLogicData)data).addInvasiveSpecies(selectedValue);
      ((InvasiveSpeciesLogicData)data).setRepSpecies(selectedValue);
      ((InvasiveSpeciesLogicData)data).setInvasiveSpeciesDescDefault();
    }
    else if (data instanceof InvasiveSpeciesChangeLogicData) {
      ((InvasiveSpeciesChangeLogicData)data).addInvasiveSpecies(selectedValue);
      ((InvasiveSpeciesChangeLogicData)data).setRepSpecies(selectedValue);
      ((InvasiveSpeciesChangeLogicData)data).setInvasiveSpeciesDescDefault();
    }
    dataModel.fireTableDataChanged();
  }
/**
 * Columns for Invasive species Logic
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == InvasiveSpeciesLogic.INVASIVE_SPECIES_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.INVASIVE_SPECIES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,InvasiveSpeciesLogic.INVASIVE_SPECIES_COL,"Invasive Species",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else {
      if (kind.equals(InvasiveSpeciesLogic.PROBABILITY.toString())) {
        initProbTable(column, col);
      }
      else {
        initChangeRateTable(column, col);
      }
    }
  }
  private void initProbTable(TableColumn column, int col) {
    // ** Soil Type Column **
    if (col == InvasiveSpeciesLogic.SOIL_TYPE_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.SOIL_TYPE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,InvasiveSpeciesLogic.SOIL_TYPE_COL,"Soil Type",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Veg Functional Group Column **
    else if (col == InvasiveSpeciesLogic.VEG_FUNC_GROUP_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.VEG_FUNC_GROUP_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(InvasiveSpeciesLogicData.VegFunctionalGroup.values()));
      column.setCellEditor(new MyJComboBoxEditor(InvasiveSpeciesLogicData.VegFunctionalGroup.values()));
//      logicTable.setRowHeight(logicTable.getRowHeight()+5);
//      logicTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    else if (col == InvasiveSpeciesLogic.DIST_TO_SEED_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.DIST_TO_SEED_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == InvasiveSpeciesLogic.START_VALUE_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.START_VALUE_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == InvasiveSpeciesLogic.PROB_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.PROB_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
  private void initChangeRateTable(TableColumn column, int col) {
    if (col == InvasiveSpeciesLogic.CHANGE_RATE_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.CHANGE_RATE_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == InvasiveSpeciesLogic.CHANGE_AS_PERCENT_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.CHANGE_AS_PERCENT_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == InvasiveSpeciesLogic.STATE_CHANGE_THRESHOLD_COL) {
      column.setIdentifier(InvasiveSpeciesLogic.STATE_CHANGE_THRESHOLD_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column, col);
    }
  }

}
