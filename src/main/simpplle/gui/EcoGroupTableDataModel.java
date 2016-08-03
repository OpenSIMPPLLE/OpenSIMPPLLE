/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.SimpplleType;
import simpplle.comcode.HabitatTypeGroupType;

/** 
 * This class defines model for Eco Group Table Data Model, a OpenSimpplle table data model.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class EcoGroupTableDataModel extends SimpplleTypeTableDataModel {
  public static final int ECOGROUP_COL = 1;

  public EcoGroupTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Returns column count as 2.
 */
  public int getColumnCount() {
    return 2;
  }
/**
 * Gets the column based on integer of column.  Choices are selected_col and ecogroup col.  Defaults to passed integer parameter.  
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:  col = c; break;
      case ECOGROUP_COL: col = HabitatTypeGroupType.CODE_COL; break;
      default: col = c;
    }
    return col;
  }

}
