/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class contains methods for Minor Vegetation Disturbance, a type of Process.  This can happen in all regions,
 * therefore it has overloaded doProbability and doSpread methods for all zones, as well as doProbabilityCommon and doSpreadCommon for the evu
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.Process
 */

public class MinorVegDisturb extends Process {
  private static final String printName = "MINOR-VEG-DISTURB";
/**
 * Constructor for Minor Vegetative Disturbance.  Inherites from Process superclass and initializes spreading to false, sets description and default visible columns.  
 */
  public MinorVegDisturb() {
    super();

    spreading   = false;
    description = "MINOR VEG DISTURB";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Returns "MINOR VEG DISTURB"
 */
  public String toString() { return printName; }
/**
 * Does the probability of a minor vegetative disturbance in an Evu.  This is the probability method common to all zones.  
 * It returns 0.  
 * @param evu the Existing vegetative unit with the minor vegetative disturbance 
 * @return
 */
  private int doProbabilityCommon(Evu evu) {
    return 0;
  }

  protected int doProbability (WestsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (EastsideRegionOne  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (Teton  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (NorthernCentralRockies  zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SierraNevada       zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (Gila               zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (SouthCentralAlaska zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
/**
 * The spreading boolean common to all regional zones for MinorVegetative Disturbance.  
 * @param fromEvu where the MinorVegetative Disturbance would spread from.  But it doesn't so returns false for all.  
 * @param evu where the Minor Vegetative Disturbance would spread to
 * @return false, because the minor vegetative disturbance does not spread for any zone. 
 */
  private boolean doSpreadCommon(Evu fromEvu, Evu evu) {
    return false;
  }

  protected boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }

}
