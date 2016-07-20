/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.TreatmentType;
import simpplle.comcode.SimpplleType;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the FireLogic Treatement Table Data Model, a type of Simpplle type table data model.
 * Choices for column are selected (priority) and treatment type column
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class FireLogicTreatmentTableDataModel extends SimpplleTypeTableDataModel {
  public static final int TREATMENT_COL = 1;

  /**
   * Constructor for Fire Logic Treatment Table Data Model.  Inherits from superclass and passes simpplle type.  
   * @param kind
   */
  public FireLogicTreatmentTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Number of columns in fire logic treatment table data model is 2.  
 */
  public int getColumnCount() {
    return 2;
  }
/**
 * Choices for column are selected (priority) and treatment type column.  
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:  col = c; break;
      case TREATMENT_COL: col = TreatmentType.CODE_COL; break;
      default: col = c;
    }
    return col;
  }

}
