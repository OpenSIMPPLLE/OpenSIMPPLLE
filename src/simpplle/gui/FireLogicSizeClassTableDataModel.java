/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import simpplle.comcode.SizeClass;
import simpplle.comcode.SimpplleType;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the FireLogic Size Class Data Model. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class FireLogicSizeClassTableDataModel extends SimpplleTypeTableDataModel {
  public static final int SIZECLASS_COL = 1;
  public static final int STRUCTURE_COL = 2;

  public FireLogicSizeClassTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }
/**
 * Get columns count.  Returns 3
 */
  public int getColumnCount() {
    return 3;
  }
/**
 * Choices for columns are selected, size class, or structure column.
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:  col = c; break;
      case SIZECLASS_COL: col = SizeClass.CODE_COL; break;
      case STRUCTURE_COL: col = SizeClass.STRUCTURE_COL; break;
      default: col = c;
    }
    return col;
  }
}
