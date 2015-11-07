package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class contains methods for Heavy Bison Grazing, a type of Process.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * 
 */

public class HeavyBisonGrazing extends Process {
  /**
   * Constructor.  Inherits from Process superclass and initializes the description to heavy bison grazing and spreading, yearly process, and uniqueUI to true 
   */
	public HeavyBisonGrazing() {
    super();

    spreading   = false;
    description = "Heavy Bison Grazing";
    yearlyProcess = true;
    uniqueUI    = true;
  }
/**
 * probability for Wyoming region.  by default BisonGrazing heavy is set to -1 meaning this has not been calculated
 * @param evu ecological vegetative unit 
 * @return the probability of heavy bison grazing on a specified EVU in wyoming
 */
  public int doProbabilityWyoming (Evu evu) {
    int prob = simpplle.comcode.BisonGrazing.getHeavy();

    if (prob == -1) {
      simpplle.comcode.BisonGrazing.calcProbability(evu);
      prob = simpplle.comcode.BisonGrazing.getHeavy();
    }
    simpplle.comcode.BisonGrazing.resetHeavy();
    return prob;
  }
  /**
   *  WesternGreatPlainsSteppe is a wyoming zone so sends to doProbabilityWyoming
   */
  public int doProbability (simpplle.comcode.zone.WesternGreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  /**
   *  GreatPlainsSteppe is a wyoming zone so sends to doProbabilityWyoming
   */
  public int doProbability (simpplle.comcode.zone.GreatPlainsSteppe zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
  /**
   *  MixedGrassPrairie is a wyoming zone so sends to doProbabilityWyoming
   */
  public int doProbability (simpplle.comcode.zone.MixedGrassPrairie zone, Evu evu) {
    return doProbabilityWyoming(evu);
  }
}


