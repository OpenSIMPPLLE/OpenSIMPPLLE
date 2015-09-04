package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines methods for Prairie Dog, a type of Disturbance Process.
 * This process must be locked in.  There is no probability logic.  It is used in the Great Plains Steppe, Western Great Plains Steppe, and Mixed Grass Prairie.
 * If locked in - considered active, if not considered inactive.   
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class PrairieDog extends Process {
  /**
   * Constructor for prairie dog.  
   * Inherits from Process superclass and initializes basic logic default visible columns ROW_COL and  PROB_COL.
   */
	public PrairieDog() {
    super();
    description = "Prairie Dog";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

}
