/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import java.util.ArrayList;
import simpplle.comcode.Process;
import simpplle.comcode.*;
import java.util.*;

/**
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class MyJButtonEditor extends AbstractCellEditor implements TableCellEditor  {
  protected JButton button = null;
  protected boolean multipleValues;
  protected String  title;
  protected Object  value;
  protected int     column;
  protected JDialog dialog;
  protected JTable  theTable;
  protected LogicDataModel dataModel;

  public MyJButtonEditor() {
    this(null,null,null,0,"",false);
  }
  public MyJButtonEditor(JDialog dialog, JTable theTable,
                         int column,String title, boolean multipleValues) {
    this(dialog,theTable,null,column,title,multipleValues);
  }
  public MyJButtonEditor(JDialog dialog, JTable theTable,
                         LogicDataModel dataModel, int column,
                         String title, boolean multipleValues) {
    this.dialog         = dialog;
    this.theTable       = theTable;
    this.dataModel      = dataModel;
    this.column         = column;
    this.title          = title;
    this.multipleValues = multipleValues;

    button = new JButton("");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_actionPeformed(e);
      }
    });
  }
  public Object getCellEditorValue() {
    return value;
  }
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    this.value = value;
    button.setText(this.value.toString());
//    table.setRowHeight(row,button.getPreferredSize().height);
    return button;
  }

  private void doVegetativeTypeChooser() {
    int row = theTable.getSelectedRow();
    int col = RegenerationData.SPECIES_CODE_COL;
    Species species = (Species)RegenerationLogic.getValueAt(row,col,RegenerationLogic.FIRE);

    final RegenVegTypeChooser dlg =
                new RegenVegTypeChooser(dialog,title,true,species,(ArrayList<VegetativeType>)value,false);
    dlg.setVisible(true);
    dlg.finishUp((ArrayList<VegetativeType>)value);
  }
  private void doSpeciesChooser() {
    int row = theTable.getSelectedRow();
    int col = RegenerationData.SPECIES_CODE_COL;
    Species species = (Species)RegenerationLogic.getValueAt(row,col,RegenerationLogic.SUCCESSION);

    final RegenSuccessionChooser dlg =
       new RegenSuccessionChooser(dialog,title,true,species,(ArrayList<RegenerationSuccessionInfo>)value,true);
    dlg.setVisible(true);
    dlg.finishUp((ArrayList<RegenerationSuccessionInfo>)value);
  }

  private void doLogicSpeciesChooser(LogicData logicData) {
    LogicSpeciesChooser dlg =
      new LogicSpeciesChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doLogicSizeClassChooser(LogicData logicData) {
    LogicSizeClassChooser dlg =
      new LogicSizeClassChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doLogicDensityChooser(LogicData logicData) {
    LogicDensityChooser dlg =
      new LogicDensityChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doLogicProcessChooser(LogicData logicData) {
    String desc = "";
    if (logicData.isDefaultDescription(SimpplleType.PROCESS) == false) {
      desc = logicData.getDescription(SimpplleType.PROCESS);
    }
    int ts = logicData.getProcessPastTimeSteps();
    boolean inclusiveTS = logicData.getProcessInclusiveTimeSteps();
    boolean anyExcept   = logicData.getProcessAnyExcept();

    LogicProcessChooser dlg =
      new LogicProcessChooser(dialog,title,true,logicData,desc,ts,inclusiveTS,false,anyExcept);


    dlg.setVisible(true);

    desc = dlg.getDescription();
    if (desc != null) {
      logicData.setDescription(desc,SimpplleType.PROCESS);
    }
    else {
      logicData.setDescriptionDefault(SimpplleType.Types.PROCESS);
    }

    logicData.setProcessPastTimeSteps(dlg.getTimeSteps());
    logicData.setProcessInclusiveTimeSteps(dlg.isInclusiveTimeSteps());
  }
  private void doLogicAdjProcessChooser(ProcessProbLogicData logicData) {
    String desc = "";
    if (logicData.isDefaultAdjProcessDescription() == false) {
      desc = logicData.getAdjProcessDescription();
    }
    int ts = logicData.getAdjProcessPastTimeSteps();

    LogicProcessChooser dlg =
      new LogicProcessChooser(dialog,title,true,logicData,desc,ts,false,true,false);
    dlg.setInclusiveTimeStepVisibility(false);
    dlg.setAnyExceptVisibility(false);

    dlg.setVisible(true);

    logicData.setAdjProcessDescription(dlg.getDescription());
    logicData.setAdjProcessPastTimeSteps(dlg.getTimeSteps());
  }

  private void doLogicTreatmentChooser(LogicData logicData) {
    boolean inclusiveTS = logicData.getTreatmentInclusiveTimeSteps();
    boolean anyExcept   = logicData.getTreatmentAnyExcept();


    LogicTreatmentChooser dlg =
      new LogicTreatmentChooser(dialog,title,true,logicData,inclusiveTS,anyExcept);
    dlg.setVisible(true);
  }
  private void doLogicTrackingSpeciesChooser(LogicData logicData) {
    LogicTrackingSpeciesChooser dlg =
      new LogicTrackingSpeciesChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doSoilTypeChooser(InvasiveSpeciesLogicData logicData) {
    SoilTypeChooser dlg =
      new SoilTypeChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doInvasiveSpeciesChooser(LogicData logicData) {
    LogicInvasiveSpeciesChooser dlg =
      new LogicInvasiveSpeciesChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doLogicEcoGroupChooser(LogicData logicData) {
    LogicEcoGroupChooser dlg =
      new LogicEcoGroupChooser(dialog,title,true,logicData);
    dlg.setVisible(true);
  }
  private void doFireLogicPositionChooser(LogicData logicData) {
    LogicPositionChooser dlg =
      new LogicPositionChooser(dialog,title,true,(FireSpreadLogicData)logicData);
    dlg.setVisible(true);
  }
  private void doFireLogicOriginProcessChooser(LogicData logicData) {
    LogicOriginFireProcessChooser dlg =
      new LogicOriginFireProcessChooser(dialog,title,true,(FireSpreadLogicData)logicData);
    dlg.setVisible(true);
  }
  private void doMoistureChooser(LogicData logicData) {
    Moisture[] allValues = Moisture.values();
    int countTS = logicData.getMoistureCountTimeStep();
    int numTS   = logicData.getMoistureNumTimeStep();
    ClimateLogicChooser dlg =
      new ClimateLogicChooser(dialog,title,true, Moisture.values(),
                              logicData.getMoistureList(),countTS,numTS);
    dlg.setVisible(true);

    ArrayList chosenItems = dlg.getChosenItems();
    for (int i=0; i<allValues.length; i++) {
      if (chosenItems.contains(allValues[i])) {
        logicData.addMoisture(allValues[i]);
      }
      else {
        logicData.removeMoisture(allValues[i]);
      }
    }
    logicData.setMoistureCountTimeStep(dlg.getCountTimeStep());
    logicData.setMoistureNumTimeStep(dlg.getNumTimeStep());
  }
  private void doTemperatureChooser(LogicData logicData) {
    Temperature[] allValues = Temperature.values();
    int countTS = logicData.getTempCountTimeStep();
    int numTS   = logicData.getTempNumTimeStep();
    ClimateLogicChooser dlg =
      new ClimateLogicChooser(dialog,title,true, Temperature.values(),
                              logicData.getTemperatureList(),countTS,numTS);
    dlg.setVisible(true);

    ArrayList chosenItems = dlg.getChosenItems();
    for (int i=0; i<allValues.length; i++) {
      if (chosenItems.contains(allValues[i])) {
        logicData.addTemperature(allValues[i]);
      }
      else {
        logicData.removeTemperature(allValues[i]);
      }
    }
    logicData.setTempCountTimeStep(dlg.getCountTimeStep());
    logicData.setTempNumTimeStep(dlg.getNumTimeStep());
  }
  private void doMpbHazardChooser(ProcessProbLogicData logicData) {
    ArrayList<MtnPineBeetleHazard.Hazard> allValues =
      new ArrayList<MtnPineBeetleHazard.Hazard>(Arrays.asList(MtnPineBeetleHazard.Hazard.values()));

    CheckBoxChooser dlg =
      new CheckBoxChooser(dialog,title,true,allValues,logicData.getMpbHazardList());
    dlg.setVisible(true);

    ArrayList chosenItems = dlg.getChosenItems();
    for (int i=0; i<allValues.size(); i++) {
      if (chosenItems.contains(allValues.get(i))) {
        logicData.addMpbHazard(allValues.get(i));
      }
      else {
        logicData.removeMpbHazard(allValues.get(i));
      }
    }
  }
//  private void doRoadStatusChooser(FireSuppClassALogicData logicData) {
//    ArrayList allValues = new ArrayList(Arrays.asList(Roads.Status.values()));
//
//    CheckBoxChooser dlg =
//      new CheckBoxChooser(dialog,title,true,allValues,logicData.getRoadStatusList());
//
//    dlg.setVisible(true);
//
//    ArrayList chosenItems = dlg.getChosenItems();
//    for (int i=0; i<allValues.size(); i++) {
//      if (chosenItems.contains(allValues.get(i))) {
//        logicData.addRoadStatus((Roads.Status)allValues.get(i));
//      }
//      else {
//        logicData.removeRoadStatus((Roads.Status)allValues.get(i));
//      }
//    }
//  }
//  private void doRoadStatusChooserBeyond(FireSuppBeyondClassALogicData logicData) {
//    ArrayList allValues = new ArrayList(Arrays.asList(Roads.Status.values()));
//
//    CheckBoxChooser dlg =
//      new CheckBoxChooser(dialog,title,true,allValues,logicData.getRoadStatusList());
//
//    dlg.setVisible(true);
//
//    ArrayList chosenItems = dlg.getChosenItems();
//    for (int i=0; i<allValues.size(); i++) {
//      if (chosenItems.contains(allValues.get(i))) {
//        logicData.addRoadStatus((Roads.Status)allValues.get(i));
//      }
//      else {
//        logicData.removeRoadStatus((Roads.Status)allValues.get(i));
//      }
//    }
//  }


  private void doCompetitionLifeformChooser(DoCompetitionData logicData) {
    Lifeform lifeform = logicData.getLifeform();
    ArrayList<Lifeform> changeLifeforms = logicData.getChangeLifeforms();
    ArrayList<Lifeform> lives = new ArrayList<Lifeform>();

    if (lifeform == Lifeform.TREES) {
      lives.add(Lifeform.SHRUBS);
      lives.add(Lifeform.HERBACIOUS);
    }
    else if (lifeform == Lifeform.SHRUBS) {
      lives.add(Lifeform.HERBACIOUS);
    }
    else { return; }

    CheckBoxChooser dlg =
      new CheckBoxChooser(dialog,title,true,lives,logicData.getChangeLifeforms());
    dlg.setVisible(true);

    ArrayList chosenItems = dlg.getChosenItems();
    for (int i=0; i<changeLifeforms.size(); i++) {
      if (chosenItems.contains(changeLifeforms.get(i))) {
        logicData.addChangeLifeform(changeLifeforms.get(i));
      }
      else {
        logicData.removeChangeLifeform(changeLifeforms.get(i));
      }
    }
  }
  private void doListSelectionChooser(AbstractLogicData logicData, int column) {
    JTableListSelectionDialog dlg =
        new JTableListSelectionDialog(dialog,title,true,logicData,column);
    dlg.setVisible(true);
  }

  private void doRegenTypeChooser(ProducingSeedLogicData logicData) {
    ArrayList<Evu.RegenTypes> allValues =
      new ArrayList<Evu.RegenTypes>(Arrays.asList(Evu.RegenTypes.values()));

    CheckBoxChooser dlg =
      new CheckBoxChooser(dialog,title,true,allValues,
                          (ArrayList)logicData.getValueAt(ProducingSeedLogic.REGEN_TYPE_COL));
    dlg.setVisible(true);


    logicData.setRegenTypes(dlg.getChosenItems());
  }

  private void doFireTypeChooser(AbstractLogicData logicData, int col) {
    ArrayList<String> fireProcesses = Process.getFireSpreadUIProcesses(true);

    ArrayList<String> values = (ArrayList<String>)logicData.getValueAt(col);

    CheckBoxChooser dlg =
      new CheckBoxChooser(dialog,title,true,fireProcesses,values);
    dlg.setVisible(true);

    ArrayList chosenItems = dlg.getChosenItems();
    values.clear();
    for (int i=0; i<chosenItems.size(); i++) {
      values.add((String)chosenItems.get(i));
    }
  }

  private void doInvasiveSpeciesSimpplleTypeChooser(InvasiveSpeciesLogicDataMSU logicData,
                                                    SimpplleType.Types kind)
  {
    LogicInvasiveSpeciesSimpplleTypeChooser dlg =
      new LogicInvasiveSpeciesSimpplleTypeChooser(dialog,title,true,logicData,kind);

    dlg.setVisible(true);
  }

  private void doLogicOwnershipChooser(LogicData logicData, int column)
  {
    ArrayList dataList = Ownership.getAllInstances();
    ListTableChooser dlg =
      new ListTableChooser(dialog,title,true,logicData,dataList,column);

    dlg.setVisible(true);
  }
  private void doLogicSpecialAreaChooser(LogicData logicData, int column)
  {
    ArrayList dataList = SpecialArea.getAllInstances();
    ListTableChooser dlg =
      new ListTableChooser(dialog,title,true,logicData,dataList,column);

    dlg.setVisible(true);
  }
  private void doLogicRoadStatusChooser(LogicData logicData, int column)
  {
    ArrayList dataList = new ArrayList<Roads.Status>(Arrays.asList(Roads.Status.values()));
    ListTableChooser dlg =
      new ListTableChooser(dialog,title,true,logicData,dataList,column);

    dlg.setVisible(true);
  }
  private void doLogicTrailStatusChooser(LogicData logicData, int column)
  {
    ArrayList dataList = new ArrayList<Trails.Status>(Arrays.asList(Trails.Status.values()));
    ListTableChooser dlg =
      new ListTableChooser(dialog,title,true,logicData,dataList,column);

    dlg.setVisible(true);
  }
  private void doLogicLandtypeChooser(LogicData logicData, int column)
  {
    ArrayList dataList = Landtype.getAllInstances();
    ListTableChooser dlg =
      new ListTableChooser(dialog,title,true,logicData,dataList,column);

    dlg.setVisible(true);
  }

  private void button_actionPeformed(ActionEvent e) {
    if (dialog instanceof FireEventLogicDialog ||
        dialog instanceof ProcessProbabilityLogicDialog ||
        dialog instanceof InvasiveSpeciesLogicDialog ||
        dialog instanceof InvasiveSpeciesLogicDialog||
        dialog instanceof RegenerationLogicDialog ||
        dialog instanceof DoCompetitionDlg ||
        dialog instanceof RegenDelayLogicDlg ||
        dialog instanceof GapProcessLogicDlg ||
        dialog instanceof EvuSearchLogicDlg ||
        dialog instanceof ProducingSeedLogicDlg ||
        dialog instanceof VegUnitFireTypeLogicDlg ||
        dialog instanceof InvasiveSpeciesMSULogicDialog ||
        dialog instanceof FireSuppClassALogicDlg ||
        dialog instanceof FireSuppBeyondClassALogicDlg ||
        dialog instanceof FireSuppProductionRateLogicDlg ||
        dialog instanceof FireSuppSpreadRateLogicDlg ||
        dialog instanceof FireSuppWeatherClassALogicDlg ||
        dialog instanceof FireSuppEventLogicDlg
       )
    {
      AbstractLogicData logicData;
      if ((dialog instanceof RegenerationLogicDialog)) {
        logicData = (AbstractLogicData)((LogicDataModel)theTable.getModel()).getValueAt(theTable.getSelectedRow());
      }
      else {
        logicData = (AbstractLogicData)dataModel.getValueAt(theTable.getSelectedRow());
      }

      if (dialog instanceof VegUnitFireTypeLogicDlg) {
        doFireTypeChooser(logicData, column);
      }
      else {
        if (column == BaseLogic.SPECIES_COL) {
          doLogicSpeciesChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.SIZE_CLASS_COL) {
          doLogicSizeClassChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.DENSITY_COL) {
          doLogicDensityChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.PROCESS_COL) {
          doLogicProcessChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.TREATMENT_COL) {
          doLogicTreatmentChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.ECO_GROUP_COL) {
          doLogicEcoGroupChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.MOISTURE_COL) {
          doMoistureChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.TEMP_COL) {
          doTemperatureChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.TRACKING_SPECIES_COL) {
          doLogicTrackingSpeciesChooser((LogicData) logicData);
        }
        else if (column == BaseLogic.OWNERSHIP_COL) {
          doLogicOwnershipChooser((LogicData) logicData, column);
        }
        else if (column == BaseLogic.SPECIAL_AREA_COL) {
          doLogicSpecialAreaChooser((LogicData) logicData, column);
        }
        else if (column == BaseLogic.ROAD_STATUS_COL) {
          doLogicRoadStatusChooser((LogicData) logicData, column);
        }
        else if (column == BaseLogic.TRAIL_STATUS_COL) {
          doLogicTrailStatusChooser((LogicData) logicData, column);
        }
        else if (column == BaseLogic.LANDTYPE_COL) {
          doLogicLandtypeChooser((LogicData) logicData, column);
        }
        else if (dialog instanceof ProcessProbabilityLogicDialog) {
          if (column == ProcessProbLogic.MPB_HAZARD_COL) {
            doMpbHazardChooser((ProcessProbLogicData) logicData);
          }
          else if (column == ProcessProbLogic.ADJ_PROCESS_COL) {
            doLogicAdjProcessChooser((ProcessProbLogicData) logicData);
          }
        }
        else if (dialog instanceof FireEventLogicDialog) {
          if (column == FireEventLogic.POSITION_COL) {
            doFireLogicPositionChooser((LogicData) logicData);
          }
          else if (column == FireEventLogic.ORIGIN_PROCESS_COL) {
            doFireLogicOriginProcessChooser((LogicData) logicData);
          }
        }
        else if (dialog instanceof InvasiveSpeciesLogicDialog) {
          if (column == InvasiveSpeciesLogic.SOIL_TYPE_COL) {
            doSoilTypeChooser((InvasiveSpeciesLogicData) logicData);
          }
          else if (column == InvasiveSpeciesLogic.INVASIVE_SPECIES_COL) {
            doInvasiveSpeciesChooser((LogicData) logicData);
          }
        }
        else if (dialog instanceof InvasiveSpeciesMSULogicDialog) {
          if (column == InvasiveSpeciesLogicMSU.PROCESS_COEFF_COL) {
            doInvasiveSpeciesSimpplleTypeChooser((InvasiveSpeciesLogicDataMSU)logicData,SimpplleType.PROCESS);
          }
          else if (column == InvasiveSpeciesLogicMSU.TREATMENT_COEFF_COL) {
            doInvasiveSpeciesSimpplleTypeChooser((InvasiveSpeciesLogicDataMSU)logicData,SimpplleType.TREATMENT);
          }
        }
        else if (dialog instanceof RegenerationLogicDialog) {
          if (column == SuccessionRegenerationData.SUCCESSION_SPECIES_COL &&
              ((RegenerationLogicDialog) dialog).getLogicKind() ==
              RegenerationLogic.SUCCESSION) {
            doSpeciesChooser();
          }
          else {
            doVegetativeTypeChooser();
          }
        }
        else if (dialog instanceof DoCompetitionDlg) {
          if (column == DoCompetitionLogic.CHANGE_LIFEFORMS_COL) {
            doCompetitionLifeformChooser((DoCompetitionData) logicData);
          }
        }
        else if (dialog instanceof EvuSearchLogicDlg) {
          doListSelectionChooser((EvuSearchData) logicData, column);
        }
        else if (dialog instanceof ProducingSeedLogicDlg) {
          if (column == ProducingSeedLogic.REGEN_TYPE_COL) {
            doRegenTypeChooser((ProducingSeedLogicData) logicData);
          }
        }
//        else if (dialog instanceof FireSuppClassALogicDlg) {
//          if (column == FireSuppClassALogic.ROAD_STATUS_COL) {
//            doRoadStatusChooser((FireSuppClassALogicData) logicData);
//          }
//        }
//        else if (dialog instanceof FireSuppBeyondClassALogicDlg) {
//          if (column == FireSuppBeyondClassALogic.ROAD_STATUS_COL) {
//            doRoadStatusChooserBeyond((FireSuppBeyondClassALogicData) logicData);
//          }
//        }


      }
    }
    super.fireEditingStopped();
    dialog.update(dialog.getGraphics());
  }



}



