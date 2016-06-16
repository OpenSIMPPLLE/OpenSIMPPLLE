package simpplle.gui;

import simpplle.comcode.Species;
import simpplle.comcode.SimpplleType;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the FireLogic Species Table Data Model, a type of Simpplle type table data model. 
 * Choices for column are selected(priority), species, lifeform, and resistance. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class FireLogicSpeciesTableDataModel extends SimpplleTypeTableDataModel {
  public static final int SPECIES_COL    = 1;
  public static final int LIFEFORM_COL   = 2;
  public static final int RESISTANCE_COL = 3;

  public FireLogicSpeciesTableDataModel(SimpplleType.Types kind) {
    super(kind);
  }

/**
 * The column count for fire logic species table data model is 4.
 */
  public int getColumnCount() {
    return 4;
  }
/**
 * Choices for column are selected(priority), species, lifeform, and resistance.  
 */
  protected int getCol(int c) {
    int col;
    switch(c) {
      case SELECTED_COL:   col = c; break;
      case SPECIES_COL:    col = Species.CODE_COL; break;
      case LIFEFORM_COL:   col = Species.LIFEFORM_COL; break;
      case RESISTANCE_COL: col = Species.FIRE_RESISTANCE_COL; break;
      default: col = c;
    }
    return col;
  }

}
