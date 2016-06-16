package simpplle.comcode;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Drought, a type of Process
 * 
 * @author Documentation by Brian Losi 
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 *   
 * @see simpplle.Comcode.Process
 */
public class Drought extends Process {
  private static final String printName = "DROUGHT";
  
  /**
   * Constructor to make drought process.  Inherits from Process superclass, initializes some variables.  
   */
  public Drought() {
    super();

    spreading   = false;
    description = "Drought";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  /**
   * 
   * @param zone RegionalZone 
   * @param evu Ecological Vegitative unit 
   * 
   * @return
   */
  
  public int doProbabilityCommon(RegionalZone zone, Evu evu) {
    return doProbability(evu);
  }
/**
 * outputs "DROUGHT"
 */
  public String toString () {
    return printName;
  }
}
