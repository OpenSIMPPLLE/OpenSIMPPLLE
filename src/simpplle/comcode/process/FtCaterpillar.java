package simpplle.comcode.process;

import simpplle.comcode.BaseLogic;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessProbLogic;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * <p> A class that defines the Forest Tent Caterpillar, a type of Process 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 * @see simpplle.comcode.Process
 */

public class FtCaterpillar extends Process {
  private static final String printName = "FT-CATERPILLAR";
  /**
   * Constructor.  initializes spreading to false, and description to Forest Tent Caterpillar then references Process superclass
   */
  public FtCaterpillar() {
    super();

    spreading = false;
    description = "FOREST TENT CATERPILLAR";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

}
