/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */



package simpplle.gui;

import simpplle.comcode.Density;
import simpplle.comcode.SimpplleType;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireLogicDensityTableDataModel extends SimpplleTypeTableDataModel {
  public static final int DENSITY_COL = 1;
/**
 * Constructor for Fire logic density table Data Model.  
 * @param kind
 */
  public FireLogicDensityTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Gets the column count.  Set to 2.  
 */
  public int getColumnCount() {
    return 2;
  }
/**
 * Gets the column.  Either this will be the selected column or density column. 
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:  col = c; break;
      case DENSITY_COL: col = Density.CODE_COL; break;
      default: col = c;
    }
    return col;
  }

}
