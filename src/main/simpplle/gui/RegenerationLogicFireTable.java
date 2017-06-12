/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableColumn;
import simpplle.JSimpplle;
import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.FireRegenerationData;
import simpplle.comcode.RegenerationLogic;
import simpplle.comcode.Species;
import java.awt.event.*;
import simpplle.comcode.HabitatTypeGroupType;
import java.awt.Font;
import java.util.ArrayList;
import simpplle.comcode.*;
import simpplle.comcode.SystemKnowledge.Kinds;

/** 
 * This class is for fire regeneration.   The five components used to determine how a plant community can regenerate are
 * if the species resprots in-place, resprouts from adjacent communities, seed is produced by the burned plants, seed is provided from adjacent communities
 * or seed is transported from communities within the landscape.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class RegenerationLogicFireTable extends VegLogicPanel {

  private static final int SPECIES_CODE_COL = FireRegenerationData.SPECIES_CODE_COL;
  private static final int RESPROUTING_COL = FireRegenerationData.RESPROUTING_COL;
  private static final int ADJ_RESPROUTING_COL = FireRegenerationData.ADJ_RESPROUTING_COL;
  private static final int IN_PLACE_SEED_COL = FireRegenerationData.IN_PLACE_SEED_COL;
  private static final int IN_LANDSCAPE_COL = FireRegenerationData.IN_LANDSCAPE_COL;
  private static final int ADJACENT_COL = FireRegenerationData.ADJACENT_COL;

  private HabitatTypeGroupType selectedEcoGroup;

  private JPanel aPanel = new JPanel();
  private JPanel adjacentPrefPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
  private JButton adjacentPrefPickPB = new JButton();
  private JLabel adjacentPrefValue = new JLabel();
  private GridLayout labelPanelLayout = new GridLayout();
  private JPanel adjacentPrefLabelPanel = new JPanel();
  private JLabel adjacentPrefLabel2 = new JLabel();
  private JLabel AdjacentPrefLabel1 = new JLabel();
  private JPanel ecoGroupCBPanel = new JPanel();
  private JComboBox ecoGroupCB = new JComboBox();
  private JLabel ecoGroupLabel = new JLabel();

  public RegenerationLogicFireTable(AbstractLogicDialog dialog,
                                    Kinds sysKnowKind, AbstractBaseLogic logicInst) {
    super(dialog,RegenerationLogic.FIRE_STR, logicInst,sysKnowKind);
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

  private  void initGUI() throws Exception {

    Font monospaced = new Font("Monospaced", Font.BOLD, 12);

    adjacentPrefPickPB.setText("Choose Species");
    adjacentPrefPickPB.addActionListener(this::adjacentPrefPickPB);
    adjacentPrefValue.setBackground(Color.white);
    adjacentPrefValue.setFont(monospaced);
    adjacentPrefValue.setForeground(Color.blue);
    adjacentPrefValue.setBorder(BorderFactory.createLoweredBevelBorder());
    adjacentPrefValue.setText("WS, BS");
    labelPanelLayout.setRows(2);
    adjacentPrefLabelPanel.setLayout(labelPanelLayout);
    adjacentPrefLabel2.setFont(monospaced);
    adjacentPrefLabel2.setText("First on left is most preferred");
    AdjacentPrefLabel1.setFont(monospaced);
    AdjacentPrefLabel1.setText("Preferred Adjacent Species");
    aPanel.setBorder(BorderFactory.createEtchedBorder());
    ecoGroupCB.addActionListener(this::ecoGroupCB);
    ecoGroupLabel.setFont(monospaced);
    ecoGroupLabel.setText("Ecological Grouping");
    adjacentPrefLabelPanel.add(AdjacentPrefLabel1, null);
    adjacentPrefLabelPanel.add(adjacentPrefLabel2, null);
    adjacentPrefPanel.add(adjacentPrefValue, null);
    adjacentPrefPanel.add(adjacentPrefPickPB, null);
    adjacentPrefPanel.add(adjacentPrefLabelPanel, null);

    northPanel.add(aPanel, null);
    northPanel.add(ecoGroupCBPanel);
    aPanel.add(adjacentPrefPanel, null);
  }

  protected void initColumns(TableColumn column, int col) {
	  //This is the dropdown menu
    selectedEcoGroup = HabitatTypeGroupType.ANY;
    for (HabitatTypeGroupType elem : HabitatTypeGroup.getAllLoadedTypesNew(true)) {
    	ecoGroupCB.addItem(elem);
    }
    ecoGroupCB.setSelectedItem(selectedEcoGroup);

    RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.FIRE,selectedEcoGroup);

    // ** Resprouting Column **
    if (col == RESPROUTING_COL) {
      column.setIdentifier(RESPROUTING_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable, RESPROUTING_COL,"Resprouting State", false));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Adjacent Resprouting Column **
    else if (col == ADJ_RESPROUTING_COL) {
      column.setIdentifier(ADJ_RESPROUTING_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable,ADJ_RESPROUTING_COL,"Adjacent Resprouting State", false));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** In-Place Seed Column **
    else if (col == IN_PLACE_SEED_COL) {
      column.setIdentifier(IN_PLACE_SEED_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable,IN_PLACE_SEED_COL,"In Place Seed", false));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** In-Landscape Column **
    else if (col == IN_LANDSCAPE_COL) {
      column.setIdentifier(IN_LANDSCAPE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable,IN_LANDSCAPE_COL, "In Landscape", false));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Adjacent Seed Source Priority State Column **
    else if (col == ADJACENT_COL) {
      column.setIdentifier(ADJACENT_COL);
      column.setCellEditor(new MyJButtonEditor(dialog, logicTable, ADJACENT_COL,"Adjacent State(s)", false));
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

  public void updateDialog() {
    ArrayList<Species> v = RegenerationLogic.getAdjacentPreferredSpecies();
    String str = (v != null) ? v.toString() : "";
    adjacentPrefValue.setText(str);
    super.updateDialog();
  }
  /**
   *
   */
  public void refreshTable() {
    super.refreshTable();
    RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.FIRE,selectedEcoGroup);
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
    Vector v  = HabitatTypeGroup.getValidSpecies();

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
      RegenerationLogic.setSpecies(row,Species.get(value),RegenerationLogic.FIRE);
      logicTable.clearSelection();
    }
    update(getGraphics());
  }

  private void adjacentPrefPickPB(ActionEvent e) {
    Vector validSpecies = HabitatTypeGroup.getValidSpecies();
    ArrayList<Species> value = RegenerationLogic.getAdjacentPreferredSpecies();
    String title = "Choose Preferred Adjacent Species";
    SuccessionSpeciesChooser dlg =
             new SuccessionSpeciesChooser(dialog,title,true,validSpecies,value);
    dlg.setVisible(true);
    dlg.finishUp(value);
    RegenerationLogic.markChanged(RegenerationLogic.FIRE);

    String str = (value != null) ? value.toString() : "";
    adjacentPrefValue.setText(str);
    update(getGraphics());
  }

  private  void ecoGroupCB(ActionEvent e) {
    HabitatTypeGroupType selected = (HabitatTypeGroupType)ecoGroupCB.getSelectedItem();
    if (selected != selectedEcoGroup) {
      selectedEcoGroup = selected;
      RegenerationLogic.setCurrentEcoGroup(RegenerationLogic.FIRE,selectedEcoGroup);
      dataModel.fireTableDataChanged();
      updateDialog();
    }
  }

  void setDefaultEcoGroup() {
    ecoGroupCB.setSelectedItem(HabitatTypeGroupType.ANY);
    ecoGroupCB(null);
  }
}
