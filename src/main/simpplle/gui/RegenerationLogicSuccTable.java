/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableColumn;
import simpplle.JSimpplle;
import simpplle.comcode.*;
import java.awt.event.*;
import java.awt.Font;
import simpplle.comcode.SystemKnowledge.Kinds;

/**
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RegenerationLogicSuccTable extends VegLogicPanel {

  private static final int SPECIES_CODE_COL       = SuccessionRegenerationData.SPECIES_CODE_COL;
  private static final int SUCCESSION_COL         = SuccessionRegenerationData.SUCCESSION_COL;
  private static final int SUCCESSION_SPECIES_COL = SuccessionRegenerationData.SUCCESSION_SPECIES_COL;

  private HabitatTypeGroupType selectedEcoGroup;

  private JPanel ecoGroupCBPanel = new JPanel();
  private JComboBox ecoGroupCB = new JComboBox();
  private JLabel ecoGroupLabel = new JLabel();

  public RegenerationLogicSuccTable(AbstractLogicDialog dialog,
                                    Kinds sysKnowKind, AbstractBaseLogic logicInst) {
    super(dialog,RegenerationLogic.SUCCESSION_STR,logicInst,sysKnowKind);
    try {
      initGUI();
      initialize();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  protected void initialize() {
    dataModel.setIsRegen(true);
    initializeBase();
  }

  private void initGUI() throws Exception {
    ecoGroupCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ecoGroupCB(e);
      }
    });
    ecoGroupLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    ecoGroupLabel.setText("Ecological Grouping");

    northPanel.add(ecoGroupCBPanel);
  }

  protected void initColumns(TableColumn column, int col) {
    selectedEcoGroup = HabitatTypeGroupType.ANY;
    for (HabitatTypeGroupType elem : HabitatTypeGroup.getAllLoadedTypesNew(true)) {
      ecoGroupCB.addItem(elem);
    }
    ecoGroupCB.setSelectedItem(selectedEcoGroup);

    RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.SUCCESSION,selectedEcoGroup);

    // ** Resprouting Column **
    if (col == SUCCESSION_COL) {
      column.setIdentifier(SUCCESSION_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == SUCCESSION_SPECIES_COL) {
      column.setIdentifier(SUCCESSION_SPECIES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,SUCCESSION_SPECIES_COL,"Succession Species",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == SPECIES_CODE_COL) {
      column.setIdentifier(SPECIES_CODE_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else {
      super.initBaseColumns(column,col);
    }
  }

  public void refreshTable() {
    super.refreshTable();
    RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.SUCCESSION,selectedEcoGroup);
  }

  void addRows(Vector speciesList) {
    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount() + 1;
    RegenerationLogic.addDataRows(position,kind,speciesList);
    refreshTable();
    logicTable.clearSelection();
  }

  public void insertRow() {
    int position = (selectedRow != -1) ? selectedRow : dataModel.getRowCount();
    addRow(position);
  }

  private void addRow(int row) {
    Vector  v          = HabitatTypeGroup.getValidSpecies();

    String[] values = new String[v.size()];
    for (int i=0; i<values.length; i++) {
      values[i] = (v.elementAt(i)).toString();
    }

    String title = "Select a Species";
    ListSelectionDialog dlg =
      new ListSelectionDialog(JSimpplle.getSimpplleMain(),title,true,values,false);
    dlg.setVisible(true);

    String value = (String)dlg.getSelection();
    if (value != null) {
      dataModel.addRow(row);
      RegenerationLogic.setSpecies(row,Species.get(value),RegenerationLogic.SUCCESSION);
      logicTable.clearSelection();
    }
    update(getGraphics());
  }

  private void ecoGroupCB(ActionEvent e) {
    HabitatTypeGroupType selected = (HabitatTypeGroupType)ecoGroupCB.getSelectedItem();
    if (selected != selectedEcoGroup) {
      selectedEcoGroup = selected;
      RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.SUCCESSION,selectedEcoGroup);
      dataModel.fireTableDataChanged();
      updateDialog();
    }
  }

  void setDefaultEcoGroup() {
    ecoGroupCB.setSelectedItem(HabitatTypeGroupType.ANY);
    ecoGroupCB(null);
  }
}
