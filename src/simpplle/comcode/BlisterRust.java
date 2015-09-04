
package simpplle.comcode;

import java.awt.Color;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Blister Rust, a type of process.  Blister rust is so common that it is included within the 
 * succession pathways
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @see simpplle.comcode.Process
 */


// Does not occur in Eastside.
public class BlisterRust extends Process {
  private static final String printName = "BLISTER-RUST";
  
  /**
   * BlisterRust constructor.  Inherits from Process superclass
   */
  public BlisterRust() {
    super();

    spreading   = false;
    description = "Blister Rust";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public int doProbabilityCommon(RegionalZone zone, Evu evu) {
    return doProbability(evu);
  }

  /**
   * If species is rust resistant white pine doProbability is called, 
   * else returns 0 meaning no Blister Rust is pressent.   
   */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species != null && species.contains(Species.RRWP)) {
      return doProbabilityCommon(zone,evu);
    }
    return 0;
  }

  /**
   * 
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return 0;
  }

  public int doProbability (SierraNevada zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  /**
   * 
   */
  public int doProbability (Gila               zone, Evu evu) { return 0; }
  
  /**
   * 
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  /**
   * 
   */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  /**
   * 
   */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * 
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  public String toString () {
    return printName;
  }
}
