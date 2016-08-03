/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.table.TableColumn;
import simpplle.comcode.SystemKnowledge.Kinds;
import simpplle.comcode.*;

/**
 * Class that defines Do Competition Logic Panel a type of vegetative logic panel.
 * Choices are selected column, lifeform, minimum canopy, maximum canopy, density, change lifeforms, and action.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class DoCompetitionLogicPanel extends VegLogicPanel {
	/**
	 * Constructor for Do Competion Logic panel. Creates the doCompetition panel based on passed dialog, kind, logic instance, and system knowledge kind. 
	 * @param dialog abstract logic dialog 
	 * @param kind the system kind in this case do competition
	 * @param logicInst 
	 * @param sysKnowKind 
	 */
  public DoCompetitionLogicPanel(AbstractLogicDialog dialog,
                                 String kind, AbstractBaseLogic logicInst, Kinds sysKnowKind) {
    super(dialog,kind,logicInst,sysKnowKind);
    try {
      initialize();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  /**
   * Initializes the Do Competion Logic panel by calling superclass initialize base method.
   */
  protected void initialize() {
    initializeBase();
  }
/**
 * Initializes the Do Competition logic columns.  
 * Choices are selected column, lifeform, minimum canopy, maximum canopy, density, change lifeforms, and action.
 */
  protected void initColumns(TableColumn column, int col) {
    if (col == DoCompetitionLogic.SELECTED_COL) {
      column.setIdentifier(DoCompetitionLogic.SELECTED_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == DoCompetitionLogic.LIFEFORM_COL) {
      column.setIdentifier(DoCompetitionLogic.LIFEFORM_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(DoCompetitionData.getLifeformValues()));
      column.setCellEditor(new MyJComboBoxEditor(DoCompetitionData.getLifeformValues()));
    }
    else if (col == DoCompetitionLogic.MIN_CANOPY_COL) {
      column.setIdentifier(DoCompetitionLogic.MIN_CANOPY_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == DoCompetitionLogic.MAX_CANOPY_COL) {
      column.setIdentifier(DoCompetitionLogic.MAX_CANOPY_COL);
      column.setCellRenderer(new AlternateRowColorDefaultTableCellRenderer());
    }
    else if (col == DoCompetitionLogic.DENSITY_CHANGE_COL) {
      column.setIdentifier(DoCompetitionLogic.DENSITY_CHANGE_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(DoCompetitionData.DensityChange.values()));
      column.setCellEditor(new MyJComboBoxEditor(DoCompetitionData.DensityChange.values()));
    }
    else if (col == DoCompetitionLogic.CHANGE_LIFEFORMS_COL) {
      column.setIdentifier(DoCompetitionLogic.CHANGE_LIFEFORMS_COL);
      column.setCellEditor(new MyJButtonEditor(dialog,logicTable,dataModel,DoCompetitionLogic.CHANGE_LIFEFORMS_COL,"Soil Type",true));
      column.setCellRenderer(new MyJTextAreaRenderer());
    }
    else if (col == DoCompetitionLogic.ACTION_COL) {
      column.setIdentifier(DoCompetitionLogic.ACTION_COL);
      column.setCellRenderer(new MyJComboBoxRenderer(DoCompetitionData.Actions.values()));
      column.setCellEditor(new MyJComboBoxEditor(DoCompetitionData.Actions.values()));
    }
    else {
      super.initBaseColumns(column,col);
    }
  }
}
