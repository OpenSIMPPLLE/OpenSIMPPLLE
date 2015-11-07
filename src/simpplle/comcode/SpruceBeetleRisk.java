package simpplle.comcode;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Defines the SpruceBeetle Risk.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public abstract class SpruceBeetleRisk {
//  private static final int LOW_HAZARD      = 0;
//  private static final int MODERATE_HAZARD = 1;
//  private static final int HIGH_HAZARD     = 2;

  private static class Hazard {
    public double low      = 0;
    public double moderate = 0;
    public double high     = 0;
  }

  public static int lowProb    = 0;
  public static int moderateProb = 0;
  public static int highProb     = 0;

  private static Hashtable adjUnitsHt;

  public static int getLowProbability()      { return lowProb; }
  public static int getModerateProbability() { return moderateProb; }
  public static int getHighProbability()     { return highProb; }

  public static void compute(simpplle.comcode.element.Evu evu) {
    int    iterations = (evu.getAcres() <= 510) ? 3 : 2;
    Hazard hazard = new Hazard();

    lowProb      = 0;
    moderateProb = 0;
    highProb     = 0;

    adjUnitsHt = new Hashtable();
    addAdjacentUnits(evu,iterations);

    doHazard(hazard,evu);
    double mortality = doAdjacentMortality();
    double breeding  = doAdjacentBreedingMaterial();
    double temp      = doRegionalClimateTemperature();
    double moisture  = doRegionalClimateMoisture();
    double doFire    = doFire(evu);
    double harvest   = doAdjacentHarvest();

    double total = mortality + breeding + temp + moisture + doFire + harvest;

    boolean needNewLine=false;
    int     tStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    if (simpplle.JSimpplle.debug()) {
      if (hazard.low > 0 || hazard.moderate > 0 || hazard.high > 0) {
        System.out.print(evu.getId() + ", ");
        System.out.print(tStep + "(Time Step), ");
        System.out.print(hazard.low + "(hazard.low), ");
        System.out.print(hazard.moderate + "(hazard.moderate), ");
        System.out.print(hazard.high + "(hazard.high), ");
        System.out.print(mortality + "(mortality), ");
        System.out.print(breeding + "(breeding), ");
        System.out.print(temp + "(temp), ");
        System.out.print(moisture + "(moisture), ");
        System.out.print(doFire + "(fire), ");
        System.out.print(total + "(total), ");
        System.out.print(harvest + "(harvest), Post Weibull= ");
        needNewLine = true;
      }
    }

    final double lambda = 0.008;
    final double beta   = 3.0;

    if (hazard.low > 0) {
      hazard.low += total;
      hazard.low = simpplle.comcode.utility.CDF_Weibull2.w2cdf(lambda,beta,hazard.low);
    }
    if (hazard.moderate > 0) {
      hazard.moderate += total;
      hazard.moderate = simpplle.comcode.utility.CDF_Weibull2.w2cdf(lambda,beta,hazard.moderate);
    }
    if (hazard.high > 0) {
      hazard.high += total;
      hazard.high = simpplle.comcode.utility.CDF_Weibull2.w2cdf(lambda,beta,hazard.high);
    }

    if (simpplle.JSimpplle.debug()) {
      if ( (hazard.low > 0 || hazard.moderate > 0 || hazard.high > 0) &&
          total > 0) {
        System.out.print(hazard.low + "(hazard.low), ");
        System.out.print(hazard.moderate + "(hazard.moderate), ");
        System.out.println(hazard.high + "(hazard.high)");
      }
      else if (needNewLine) {
        System.out.println();
      }
    }
    lowProb      = (int)Math.round(hazard.low      * 100.0);
    moderateProb = (int)Math.round(hazard.moderate * 100.0);
    highProb     = (int)Math.round(hazard.high     * 100.0);
  }

  private static void addAdjacentUnits(simpplle.comcode.element.Evu evu, int iterations) {
    if (iterations == 0) { return; }

    AdjacentData[] adjUnits = evu.getAdjacentData();
    simpplle.comcode.element.Evu unit;
    for (int i=0; i<adjUnits.length; i++) {
      unit = adjUnits[i].evu;
      adjUnitsHt.put(unit,unit);
      addAdjacentUnits(unit,(iterations-1));
    }
  }

  private static Hazard doHazard(Hazard hazard, simpplle.comcode.element.Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return hazard; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    Species dominantSpecies = species.getDominantSpecies();
    Species secondSpecies   = species.getSecondSpecies();

    if (species.equals(Species.WS) == false &&
        species.equals(Species.LS) == false &&
        species.equals(Species.SS) == false &&
        dominantSpecies.equals(Species.WS) == false &&
        dominantSpecies.equals(Species.LS) == false &&
        dominantSpecies.equals(Species.SS) == false &&
        secondSpecies.equals(Species.WS) == false &&
        secondSpecies.equals(Species.LS) == false &&
        secondSpecies.equals(Species.SS)) {
      return hazard;
    }

    if (simpplle.comcode.element.Evu.haveHighSpruceBeetle() &&
        sizeClass.equals(SizeClass.LARGE) == false &&
        sizeClass.equals(SizeClass.POLE) == false) {
      return hazard;
    }
    else if (simpplle.comcode.element.Evu.haveHighSpruceBeetle() == false &&
             sizeClass.equals(SizeClass.LARGE) == false) {
      return hazard;
    }

    if (density.equals(Density.W) || density.equals(Density.O)) {
      if (dominantSpecies.equals(Species.WS) ||
          dominantSpecies.equals(Species.LS) ||
          dominantSpecies.equals(Species.SS) ||
          species.equals(Species.WS) ||
          species.equals(Species.LS) ||
          species.equals(Species.SS)) {
        if (false /** @todo Aspect N,NE,NW = Low(1) */) {
        }
        else {
          hazard.low = 1;
          hazard.moderate = 5;
        }
      }
      else {
        hazard.low = 1;
      }
    }
    else if (density.equals(Density.C)) {
      if (true /** @todo elevation < 294.5m */) {
        if (true /** @todo aspect = east */) {
          if (species.equals(Species.WS) ||
              species.equals(Species.LS) ||
              species.equals(Species.SS)) {
            hazard.moderate = 5;
            hazard.high     = 9;
          }
          else if (dominantSpecies.equals(Species.WS) ||
                   dominantSpecies.equals(Species.LS) ||
                   dominantSpecies.equals(Species.SS)) {
            hazard.moderate = 5;
          }
          else if (secondSpecies.equals(Species.WS) ||
                   secondSpecies.equals(Species.LS) ||
                   secondSpecies.equals(Species.SS)) {
            hazard.low      = 1;
            hazard.moderate = 5;
          }
        }
        else {
          if (density.equals(Density.C)) {
            hazard.moderate = 5;
            hazard.high     = 9;
          }
          else if (density.equals(Density.W) || density.equals(Density.O)) {
            hazard.low      = 1;
            hazard.moderate = 5;
          }
        }
      }
      else if (false /** @todo elevation > 294.5m */) {
        if (false/** @todo elevation <= 442m) */) {
          hazard.moderate = 5;
        }
        else if (false /** @todo elevaiont > 442m */) {
          hazard.high = 9;
        }
      }
    }

    hazard.low      *= 0.32;
    hazard.moderate *= 0.32;
    hazard.high     *= 0.32;

    return hazard;
  }

  private static double doAdjacentMortality () {
    int         iterations;
    simpplle.comcode.element.Evu unit;
    int         tStep = Simulation.getCurrentTimeStep();
    int         lightSbCount = 0, mediumSbCount = 0, highSbCount = 0;
    int         otherCount = 0;
    double      result;

    Enumeration units = adjUnitsHt.keys();
    while (units.hasMoreElements()) {
      unit = (simpplle.comcode.element.Evu)units.nextElement();

      VegSimStateData state = unit.getState(tStep);
      if (state == null) { continue; }

      ProcessType process = unit.getState(tStep).getProcess();

      if (process.equals(ProcessType.LIGHT_SB)) {
        lightSbCount++;
      }
      else if (process.equals(ProcessType.MEDIUM_SB)) {
        mediumSbCount++;
      }
      else if (process.equals(ProcessType.HIGH_SB)) {
        highSbCount++;
      }
      else {
        otherCount++;
      }
    }

    if (lightSbCount == 0 && mediumSbCount == 0 && highSbCount == 0) {
      result = 0;
    }
    else if (mediumSbCount == 0 && highSbCount == 0 && otherCount == 0) {
      result = 1;
    }
    else if (mediumSbCount >= 1 && highSbCount == 0 && otherCount == 0 && lightSbCount >= 1) {
      result = 3;
    }
    else if (lightSbCount == 0 && highSbCount == 0 && otherCount == 0) {
      result = 5;
    }
    else if (highSbCount >= 1) {
      result = 7;
    }
    else if (lightSbCount == 0 && mediumSbCount == 0 && otherCount == 0) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.18;
    return result;
  }

  private static double doAdjacentBreedingMaterial() {
    simpplle.comcode.element.Evu unit;
    Enumeration units = adjUnitsHt.keys();
    int         lowWindCount = 0, modWindCount = 0, highWindCount = 0;
    int         otherCount = 0;
    int         tStep = Simulation.getCurrentTimeStep();
    ProcessType process;
    double      result = 0;

    while (units.hasMoreElements()) {
      unit = (simpplle.comcode.element.Evu)units.nextElement();

      VegSimStateData state = unit.getState(tStep);
      if (state == null) { continue; }

      process = unit.getState(tStep).getProcess();
      if (process.equals(ProcessType.LOW_WINDTHROW)) {
        lowWindCount++;
      }
      else if (process.equals(ProcessType.MEDIUM_WINDTHROW)) {
        modWindCount++;
      }
      else if (process.equals(ProcessType.HIGH_WINDTHROW)) {
        highWindCount++;
      }
      else {
        otherCount++;
      }
    }

    if (lowWindCount == 0 && modWindCount == 0 && highWindCount == 0) {
      result = 0;
    }
    else if (modWindCount == 0 && highWindCount == 0 && otherCount == 0) {
      result = 1;
    }
    else if (modWindCount >= 1 && highWindCount == 0 && otherCount == 0 && lowWindCount >= 1) {
      result = 3;
    }
    else if (lowWindCount == 0 && otherCount == 0 && highWindCount == 0) {
      result = 5;
    }
    else if (highWindCount >= 1) {
      result = 7;
    }
    else if (lowWindCount == 0 && modWindCount == 0 && otherCount == 0) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.33;
    return result;
  }

  private static double doRegionalClimateTemperature() {
    Climate climate = Simpplle.getClimate();
    double result;
    Climate.Temperature temp     = climate.getTemperature();
    Climate.Moisture    moisture = climate.getMoisture();

    if (temp == Climate.Temperature.NORMAL || temp == Climate.COOLER) {
      result = 0;
    }
    else if (temp == Climate.WARMER) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.07;
    return result;
  }
  private static double doRegionalClimateMoisture() {
    Climate climate = Simpplle.getClimate();
    double result;
    Climate.Moisture moisture = climate.getMoisture();

    if (moisture == Climate.Moisture.NORMAL || moisture == Climate.WETTER) {
      result = 0;
    }
    else if (moisture == Climate.DRIER) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.06;
    return result;
  }

  private static double doFire(simpplle.comcode.element.Evu evu) {
    simpplle.comcode.element.Evu unit;
    Enumeration units = adjUnitsHt.keys();
    int         lsf = 0, msf = 0, srf = 0;
    int         otherCount = 0;
    int         tStep = Simulation.getCurrentTimeStep();
    ProcessType process;
    double      result = 0;

    while (units.hasMoreElements()) {
      unit = (simpplle.comcode.element.Evu)units.nextElement();

      VegSimStateData state = unit.getState(tStep);
      if (state == null) { continue; }

      process = unit.getState(tStep).getProcess();
      if (process.equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
        lsf++;
      }
      else if (process.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
        msf++;
      }
      else if (process.equals(ProcessType.STAND_REPLACING_FIRE)) {
        srf++;
      }
      else {
        otherCount++;
      }
    }

    // Need to check the current unit as well.
    VegSimStateData state = evu.getState(tStep);
    if (state == null) { return 0; }

    process = evu.getState(tStep).getProcess();
    if (process.equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
      lsf++;
    }
    else if (process.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
      msf++;
    }
    else if (process.equals(ProcessType.STAND_REPLACING_FIRE)) {
      srf++;
    }
    else {
      otherCount++;
    }

    if (lsf == 0 && msf == 0 && srf == 0) {
      result = 0;
    }
    else if (msf == 0 && srf == 0 && otherCount == 0) {
      result = 1;
    }
    else if (msf >= 1 && srf == 0 && otherCount == 0 && lsf >= 1) {
      result = 3;
    }
    else if (lsf == 0 && otherCount == 0 && srf == 0) {
      result = 5;
    }
    else if (srf >= 1) {
      result = 7;
    }
    else if (lsf == 0 && msf == 0 && otherCount == 0) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.03;
    return result;
  }

  private static double doAdjacentHarvest() {
    simpplle.comcode.element.Evu unit;
    Enumeration   units = adjUnitsHt.keys();
    int           harvestCount = 0;
    int           tStep = Simulation.getCurrentTimeStep();
    Treatment     treatment;
    TreatmentType treatType;
    double        result = 0;

    while (units.hasMoreElements()) {
      unit = (simpplle.comcode.element.Evu)units.nextElement();
      treatment = unit.getTreatment(tStep,true);
      if (treatment == null) { continue; }

      treatType = treatment.getType();
      if ((treatType.equals(TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN)     == false) &&
          (treatType.equals(TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) == false) &&
          (treatType.equals(TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN)          == false) &&
          (treatType.equals(TreatmentType.PRECOMMERCIAL_THINNING)                  == false) &&
          (treatType.equals(TreatmentType.PRECOMMERCIAL_THINNING_DIVERSITY)        == false) &&
          (treatType.equals(TreatmentType.AGRICULTURE)                             == false)) {
        harvestCount++;
      }
    }

    if (harvestCount == 0) {
      result = 0;
    }
    else if (harvestCount == 1) {
      result = 3;
    }
    else if (harvestCount > 1) {
      result = 9;
    }
    else { result = 0; }

    result *= 0.01;
    return result;
  }
}


