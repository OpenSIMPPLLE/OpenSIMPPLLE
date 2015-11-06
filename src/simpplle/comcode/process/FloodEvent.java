package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> This class contains methods for Flood Event, a type of Process.  
 * Unlike Flood process all regions can have a Flood Event.  Although they all return 0 for probability  and false for spread.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 * @see simpplle.comcode.Process
 */
public class FloodEvent extends Process {
  private static final String printName = "FLOOD-EVENT";

  public FloodEvent() {
    super();

    spreading   = false;
    description = "FLOOD EVENT";
  }

  public String toString() { return printName; }

  private int doProbabilityCommon(Evu evu) {
    return 0;
  }

  protected int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.Teton zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.Gila zone, Evu evu) {
    return doProbabilityCommon(evu);
  }
  protected int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, Evu evu) {
    return doProbabilityCommon(evu);
  }

  private boolean doSpreadCommon(Evu fromEvu, Evu evu) {
    return false;
  }

  protected boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.Gila zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }
  protected boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(fromEvu,evu);
  }

}