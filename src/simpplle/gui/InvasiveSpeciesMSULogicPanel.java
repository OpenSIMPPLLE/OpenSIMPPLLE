/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.BaseLogic;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import simpplle.comcode.InvasiveSpeciesLogicMSU;
import simpplle.comcode.InvasiveSpeciesLogicDataMSU;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.Species;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import simpplle.comcode.InvasiveSpeciesChangeLogicData;
import simpplle.comcode.AbstractLogicData;

/** 
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class InvasiveSpeciesMSULogicPanel extends VegLogicPanel {
  public InvasiveSpeciesMSULogicPanel(AbstractLogicDialog dialog,
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
    if (data instanceof InvasiveSpeciesLogicDataMSU) {
      ((InvasiveSpeciesLogicDataMSU)data).addInvasiveSpecies(selectedValue);
      ((InvasiveSpeciesLogicDataMSU)data).setRepSpecies(selectedValue);
      ((InvasiveSpeciesLogicDataMSU)data).setInvasiveSpeciesDescDefault();
    }
    else if (data instanceof InvasiveSpeciesChangeLogicData) {
      ((InvasiveSpeciesChangeLogicData)data).addInvasiveSpecies(selectedValue);
      ((InvasiveSpeciesChangeLogicData)data).setRepSpecies(selectedValue);
      ((InvasiveSpeciesChangeLogicData)data).setInvasiveSpeciesDescDefault();
    }
    dataModel.fireTableDataChanged();
  }

  protected void initColumns(TableColumn column, int col) {
    if (col == InvasiveSpeciesLogicMSU.INVASIVE_SPECIES_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.INVASIVE_SPECIES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,InvasiveSpeciesLogicMSU.INVASIVE_SPECIES_COL,"Invasive Species",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else {
      if (kind.equals(InvasiveSpeciesLogicMSU.PROBABILITY.toString())) {
        initProbTable(column, col);
      }
      else {
        initChangeRateTable(column, col);
      }
    }
  }
  private void initProbTable(TableColumn column, int col) {
    if (col == InvasiveSpeciesLogicMSU.INTERCEPT_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.INTERCEPT_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.ELEV_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.ELEV_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.SLOPE_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.SLOPE_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.C0SASP_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.C0SASP_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.SINASP_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.SINASP_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.ANNRAD_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.ANNRAD_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.DISTROAD_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.DISTROAD_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.DISTTRAIL_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.DISTTRAIL_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL,"Process Coefficients",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL,"Treatment Coefficients",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.SHRUB_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.SHRUB_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.GRASS_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.GRASS_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.TREE_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.TREE_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else if (col == InvasiveSpeciesLogicMSU.START_VALUE_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.START_VALUE_COL);
      column.setCellRenderer(new MyJTextFieldRenderer());
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
  private void initChangeRateTable(TableColumn column, int col) {
    if (col == InvasiveSpeciesLogicMSU.CHANGE_RATE_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.CHANGE_RATE_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == InvasiveSpeciesLogicMSU.CHANGE_AS_PERCENT_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.CHANGE_AS_PERCENT_COL);
      Utility.setColumnCellColor(column);
    }
    else if (col == InvasiveSpeciesLogicMSU.STATE_CHANGE_THRESHOLD_COL) {
      column.setIdentifier(InvasiveSpeciesLogicMSU.STATE_CHANGE_THRESHOLD_COL);
      Utility.setColumnCellColor(column);
    }
    else {
      super.initBaseColumns(column, col);
    }
  }

}
