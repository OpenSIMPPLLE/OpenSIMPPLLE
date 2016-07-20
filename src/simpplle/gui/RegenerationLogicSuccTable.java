/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import simpplle.JSimpplle;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.SuccessionRegenerationData;
import simpplle.comcode.RegenerationLogic;
import simpplle.comcode.Simpplle;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.Species;
import simpplle.comcode.SystemKnowledge;
import java.awt.event.*;
import simpplle.comcode.HabitatTypeGroupType;
import java.awt.Font;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.FireRegenerationData;

/** 
 *
 * 
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
                                    Kinds sysKnowKind) {
    super(dialog,RegenerationLogic.SUCCESSION_STR,null,sysKnowKind);
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

  void initGUI() throws Exception {
    ecoGroupCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ecoGroupCB_actionPerformed(e);
      }
    });
    ecoGroupLabel.setFont(new java.awt.Font("Monospaced", Font.BOLD, 12));
    ecoGroupLabel.setText("Ecological Grouping");
    //"Quack - EcoGroup Dropdown Succession"
    //ecoGroupCBPanel.add(ecoGroupLabel);
    //ecoGroupCBPanel.add(ecoGroupCB);

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

  public void addRows(Vector speciesList) {
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
      values[i] = ((Species)v.elementAt(i)).toString();
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

  public void ecoGroupCB_actionPerformed(ActionEvent e) {
    HabitatTypeGroupType selected = (HabitatTypeGroupType)ecoGroupCB.getSelectedItem();
    if (selected != selectedEcoGroup) {
      selectedEcoGroup = selected;
      RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.SUCCESSION,selectedEcoGroup);
      dataModel.fireTableDataChanged();
      updateDialog();
    }

  }
  public void setDefaultEcoGroup() {
    ecoGroupCB.setSelectedItem(HabitatTypeGroupType.ANY);
    ecoGroupCB_actionPerformed(null);
  }


}



