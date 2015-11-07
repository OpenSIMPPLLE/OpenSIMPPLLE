package simpplle.gui;

import javax.swing.table.TableColumn;

import simpplle.comcode.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class sets up Vegetative Logic Panel, a type of Base Logic Panel, which inherits directly from JPanel. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class VegLogicPanel extends BaseLogicPanel {
/**
 * Constructor for Vegetative Logic Panel.  References Vegetative Logic Panel.  
 * @param dialog kind of vegetative type 
 * @param kind
 * @param logicInst
 * @param sysKnowKind the system knowledge kind
 */
  public VegLogicPanel(AbstractLogicDialog dialog,
                    String kind, simpplle.comcode.logic.AbstractBaseLogic logicInst,
                    SystemKnowledge.Kinds sysKnowKind) {
    super(dialog,kind,logicInst,sysKnowKind);
  }

/**
 * Initializes the columns.  
 */
  protected void initColumns(TableColumn column, int col) {}
/**
 * Initializes the base columns within the Vegetative Logic Panel.  These are for any or all of the following: 
 * <li> Row Col (from Base Logic superclass)
 * <li>Ecological Grouping
 * <li>Species
 * <li>Size Class
 * <li>Density
 * <li>Process
 * <li>Treatment
 * <li>Season
 * <li>Moisture
 * <li>Temperature
 * <li>Tracking Species
 * <li>Ownership
 * <li>Special Area
 * <li>Road Status
 * <li>Trail Status
 * <li>Landtype
 * 
 */
  protected void initBaseColumns(TableColumn column, int col) {
    // ** Ecological Grouping Column **
    if (col == simpplle.comcode.logic.BaseLogic.ECO_GROUP_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.ECO_GROUP_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.ECO_GROUP_COL,"Ecological Grouping",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Species Column **
    else if (col == simpplle.comcode.logic.BaseLogic.SPECIES_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.SPECIES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.SPECIES_COL,"Species",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Size Class Column **
    else if (col == simpplle.comcode.logic.BaseLogic.SIZE_CLASS_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.SIZE_CLASS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.SIZE_CLASS_COL,"Size Class",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Density Column **
    else if (col == simpplle.comcode.logic.BaseLogic.DENSITY_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.DENSITY_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.DENSITY_COL,"Density",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Process Column **
    else if (col == simpplle.comcode.logic.BaseLogic.PROCESS_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.PROCESS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.PROCESS_COL,"Process",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Treatment Column **
    else if (col == simpplle.comcode.logic.BaseLogic.TREATMENT_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.TREATMENT_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.TREATMENT_COL,"Treatment",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Season Column **
    else if (col == simpplle.comcode.logic.BaseLogic.SEASON_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.SEASON_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(Climate.allSeasons));
      column.setCellEditor(new MyJComboBoxEditor(Climate.allSeasons));
//      logicTable.setRowHeight(logicTable.getRowHeight()+5);
//      logicTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    // ** Moisture Column **
    else if (col == simpplle.comcode.logic.BaseLogic.MOISTURE_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.MOISTURE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.MOISTURE_COL,"Moisture",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Temperature Column **
    else if (col == simpplle.comcode.logic.BaseLogic.TEMP_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.TEMP_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.TEMP_COL,"Temperature",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    // ** Tracking Species Column **
    else if (col == simpplle.comcode.logic.BaseLogic.TRACKING_SPECIES_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.TRACKING_SPECIES_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.TRACKING_SPECIES_COL,"Tracking Species",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.BaseLogic.OWNERSHIP_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.OWNERSHIP_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.OWNERSHIP_COL,"Ownership",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.BaseLogic.SPECIAL_AREA_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.SPECIAL_AREA_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.SPECIAL_AREA_COL,"Special Area",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.BaseLogic.ROAD_STATUS_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.ROAD_STATUS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.ROAD_STATUS_COL,"Road Status",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.BaseLogic.TRAIL_STATUS_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.TRAIL_STATUS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.TRAIL_STATUS_COL,"Trail Status",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == simpplle.comcode.logic.BaseLogic.LANDTYPE_COL) {
      column.setIdentifier(simpplle.comcode.logic.BaseLogic.LANDTYPE_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel, simpplle.comcode.logic.BaseLogic.LANDTYPE_COL,"Land Type",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else {
      super.initBaseColumns(column,col);
    }

  }


}


