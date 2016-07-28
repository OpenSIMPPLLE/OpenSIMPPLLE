/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.*;
import java.util.zip.*;

/**
 * This class defines Fire Type Legacy, a type of Logic Data.  This involves legacy systems fire type data .  It has variables to define or calculate
 * forest structure, fire type and ownership
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 */

public abstract class FireTypeDataLegacy {
  private static final int NUM_STRUCTURE      = 3;
  private static final int NUM_RESISTANCE     = 3;

  private static Climate.Moisture moisture;
  private static ProcessType[][][][][] fireTypeData;

  // **********************************************************************
  private static ProcessType getLowTypeOfFire(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SIERRA_NEVADA) {
      return getLowTypeOfFireSierraNevada(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else if (zoneId == ValidZones.SOUTHERN_CALIFORNIA) {
      return getLowTypeOfFireSouthernCalifornia(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else if (zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      return getLowTypeOfFireAlaska(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else {
      // East, West, Gila
      return getLowTypeOfFireCommon(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
  }

  private static ProcessType getLowTypeOfFireCommon(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    ProcessType fireProcessType;
    int        row = -1;
    String     fireType;

    switch (structure) {
      case NON_FOREST:
        if (density == Density.ONE   || density == Density.TWO ||
            density == Density.THREE || density == Density.FOUR) { row = 0; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // POLE
            if ((density == Density.ONE || density == Density.TWO) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          case 2:
            // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.ONE || density == Density.TWO) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.ONE   || density == Density.TWO ||
                      density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_WSBW)  ||
                       pastProcessType.equals(ProcessType.SEVERE_LP_MPB) ||
                       pastProcessType.equals(ProcessType.PP_MPB)))) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.ONE || density == Density.TWO) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.ONE   || density == Density.TWO ||
                  density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  pastProcessType.equals(ProcessType.ROOT_DISEASE))) {
          row = 1;
        }
        else if (density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 2;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getLowTypeOfFireSierraNevada(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int         row = -1;
    String      fireType;

    switch (structure) {
      case NON_FOREST:
        if (sizeClass == SizeClass.GRASS       || sizeClass == SizeClass.OPEN_HERB ||
            sizeClass == SizeClass.CLOSED_HERB || sizeClass == SizeClass.UNIFORM   ||
            sizeClass == SizeClass.SCATTERED   || sizeClass == SizeClass.CLUMPED) {
          row = 0;
        }
        else if (sizeClass == SizeClass.OPEN_LOW_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                 sizeClass == SizeClass.OPEN_MID_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_MID_SHRUB) {
          row = 2;
        }
        else if (sizeClass == SizeClass.OPEN_TALL_SHRUB ||
                 sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
          row = 4;
        }

        if (density == Density.ONE || density == Density.TWO) {
          row += 0;
        }
        else if (density == Density.THREE || density == Density.FOUR) {
          row += 1;
        }
        else {
          row = -1;
        }
        break;

      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // POLE
            if ((density == Density.TWO || density == Density.THREE) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          case 2: // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.TWO) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.TWO || density == Density.THREE ||
                      density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          default: row = -1;
        }
        break;

      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.TWO || density == Density.THREE) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.TWO || density == Density.THREE ||
                  density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 1;
        }
        else if (density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 2;
        }
        break;

      default:
        row = -1;
    }


    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getLowTypeOfFireSouthernCalifornia(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int         row = -1;
    String      fireType;

    switch (structure) {
      case NON_FOREST:
        if (sizeClass == SizeClass.GRASS       || sizeClass == SizeClass.OPEN_HERB ||
            sizeClass == SizeClass.CLOSED_HERB || sizeClass == SizeClass.UNIFORM   ||
            sizeClass == SizeClass.SCATTERED   || sizeClass == SizeClass.CLUMPED) {
          row = 0;
        }
        else if (sizeClass == SizeClass.OPEN_LOW_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                 sizeClass == SizeClass.OPEN_MID_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_MID_SHRUB) {
          row = 2;
        }
        else if (sizeClass == SizeClass.OPEN_TALL_SHRUB ||
                 sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
          row = 4;
        }

        if (density == Density.ONE || density == Density.TWO) {
          row += 0;
        }
        else if (density == Density.THREE || density == Density.FOUR) {
          row += 1;
        }
        else {
          row = -1;
        }
        break;

      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // POLE
            if ((density == Density.TWO || density == Density.THREE) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          case 2: // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.TWO) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.TWO || density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          default: row = -1;
        }
        break;

      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.TWO || density == Density.THREE) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.TWO || density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 1;
        }
        else if (density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 2;
        }
        break;

      default:
        row = -1;
    }


    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getLowTypeOfFireAlaska(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int     row = -1;

    switch (structure) {
      case NON_FOREST:
        if (density == Density.C) { row = 0; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0:
            if (density == Density.W || density == Density.O ||
                density == Density.C) {
              row = 0;
            }
            break;
          case 1: // POLE
            row = 0;
            break;
          case 2:
            if (density == Density.W || density == Density.O ||
                density == Density.C) {
              row = 0;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        if (density == Density.W || density == Density.O ||
            density == Density.C) {
          row = 0;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }
  // **********************************************************************
  private static ProcessType getModerateTypeOfFire(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      return getModerateTypeOfFireAlaska(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else if (zoneId == ValidZones.SOUTHERN_CALIFORNIA ||
             zoneId == ValidZones.SIERRA_NEVADA) {
      return getLowTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else {
      // East, West, Gila
      return getModerateTypeOfFireCommon(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
  }

  private static ProcessType getModerateTypeOfFireCommon(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int         row = -1;

    switch (structure) {
      case NON_FOREST:
        if (density == Density.ONE || density == Density.TWO) { row = 0; }
        else if (density == Density.THREE || density == Density.FOUR) { row = 1; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // POLE
            if ((density == Density.ONE || density == Density.TWO) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          case 2:
            // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.ONE   || density == Density.TWO) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.ONE   || density == Density.TWO ||
                      density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_WSBW)  ||
                       pastProcessType.equals(ProcessType.SEVERE_LP_MPB) ||
                       pastProcessType.equals(ProcessType.PP_MPB)))) {
              row = 1;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 2;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.ONE || density == Density.TWO) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.ONE   || density == Density.TWO ||
                  density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  pastProcessType.equals(ProcessType.ROOT_DISEASE))) {
          row = 1;
        }
        else if (density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 2;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getModerateTypeOfFireAlaska(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int     row = -1;

    switch (structure) {
      case NON_FOREST:
        if (density == Density.C) { row = 0; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0:
          case 1:
          case 2:
            if (density == Density.W)      { row = 0; }
            else if (density == Density.O) { row = 1; }
            else if (density == Density.C) { row = 2; }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        if (density == Density.W)      { row = 0; }
        else if (density == Density.O) { row = 1; }
        else if (density == Density.C) { row = 2; }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }
  // **********************************************************************
  private static ProcessType getHighTypeOfFire(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {
    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      return getHighTypeOfFireAlaska(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else if (zoneId == ValidZones.SIERRA_NEVADA) {
      return getHighTypeOfFireSierraNevada(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else if (zoneId == ValidZones.SOUTHERN_CALIFORNIA) {
      return getHighTypeOfFireSouthernCalifornia(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
    else {
      // East, West, Gila
      return getHighTypeOfFireCommon(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
  }
  private static ProcessType getHighTypeOfFireCommon(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int row = -1;

    switch (structure) {
      case NON_FOREST:
        if ((sizeClass == SizeClass.SCATTERED      || sizeClass == SizeClass.CLUMPED        ||
             sizeClass == SizeClass.OPEN_HERB      || sizeClass == SizeClass.OPEN_LOW_SHRUB ||
             sizeClass == SizeClass.OPEN_MID_SHRUB || sizeClass == SizeClass.OPEN_TALL_SHRUB) &&
            (density == Density.ONE || density == Density.TWO)) {
          row = 0;
        }
        else if ((sizeClass == SizeClass.SCATTERED || sizeClass == SizeClass.CLUMPED ||
                  sizeClass == SizeClass.OPEN_HERB ||
                  sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                  sizeClass == SizeClass.CLOSED_MID_SHRUB ||
                  sizeClass == SizeClass.CLOSED_TALL_SHRUB) &&
                 (density == Density.THREE || density == Density.FOUR)) {
          row = 1;
        }
        else if ((sizeClass == SizeClass.UNIFORM || sizeClass == SizeClass.CLOSED_HERB ||
                  sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                  sizeClass == SizeClass.CLOSED_MID_SHRUB ||
                  sizeClass == SizeClass.CLOSED_TALL_SHRUB) &&
                 (density == Density.ONE   || density == Density.TWO ||
                  density == Density.THREE || density == Density.FOUR)) {
          row = 2;
        }
        else if ((sizeClass == SizeClass.OPEN_LOW_SHRUB ||
                  sizeClass == SizeClass.OPEN_MID_SHRUB ||
                  sizeClass == SizeClass.OPEN_TALL_SHRUB) &&
                 (density == Density.ONE   || density == Density.TWO ||
                  density == Density.THREE || density == Density.FOUR)) {
          row = 3;
        }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // POLE
            if ((density == Density.ONE || density == Density.TWO) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 2;
            }
            else if (density == Density.ONE || density == Density.TWO) {
              row = 3;
            }
            else if (density == Density.THREE || density == Density.FOUR) {
              row = 4;
            }
            break;
          case 2:
            // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.ONE || density == Density.TWO) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_WSBW)  ||
                       pastProcessType.equals(ProcessType.SEVERE_LP_MPB) ||
                       pastProcessType.equals(ProcessType.PP_MPB)))) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_WSBW)  ||
                       pastProcessType.equals(ProcessType.SEVERE_LP_MPB) ||
                       pastProcessType.equals(ProcessType.PP_MPB)))) {
              row = 2;
            }
            else if (density == Density.ONE || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 3;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.ONE || density == Density.TWO) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.ONE || density == Density.TWO) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  pastProcessType.equals(ProcessType.ROOT_DISEASE))) {
          row = 1;
        }
        else if ((density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  pastProcessType.equals(ProcessType.ROOT_DISEASE))) {
          row = 2;
        }
        else if (density == Density.ONE || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 3;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.MSF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getHighTypeOfFireSierraNevada(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int         row = -1;

    switch (structure) {
      case NON_FOREST:
        if (sizeClass == SizeClass.GRASS       || sizeClass == SizeClass.OPEN_HERB ||
            sizeClass == SizeClass.CLOSED_HERB || sizeClass == SizeClass.UNIFORM   ||
            sizeClass == SizeClass.SCATTERED   || sizeClass == SizeClass.CLUMPED) {
          row = 0;
        }
        else if (sizeClass == SizeClass.OPEN_LOW_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                 sizeClass == SizeClass.OPEN_MID_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_MID_SHRUB) {
          row = 2;
        }
        else if (sizeClass == SizeClass.OPEN_TALL_SHRUB ||
                 sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
          row = 4;
        }

        if (density == Density.ONE || density == Density.TWO) {
          row += 0;
        }
        else if (density == Density.THREE || density == Density.FOUR) {
          row += 1;
        }
        else {
          row = -1;
        }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // P0LE
            if ((density == Density.TWO) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 2;
            }
            else if (density == Density.TWO) {
              row = 3;
            }
            else if (density == Density.THREE || density == Density.FOUR) {
              row = 4;
            }
            else {
              row = -1;
            }
            break;
          case 2:
            // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.TWO || density == Density.THREE || density == Density.FOUR) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.TWO) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 2;
            }
            else if (density == Density.ONE   || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 3;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.TWO) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.TWO) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 1;
        }
        else if ((density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 2;
        }
        else if (density == Density.ONE   || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 3;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.MSF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getHighTypeOfFireSouthernCalifornia(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int         row = -1;

    switch (structure) {
      case NON_FOREST:
        if (sizeClass == SizeClass.GRASS       || sizeClass == SizeClass.OPEN_HERB ||
            sizeClass == SizeClass.CLOSED_HERB || sizeClass == SizeClass.UNIFORM   ||
            sizeClass == SizeClass.SCATTERED   || sizeClass == SizeClass.CLUMPED) {
          row = 0;
        }
        else if (sizeClass == SizeClass.OPEN_LOW_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_LOW_SHRUB ||
                 sizeClass == SizeClass.OPEN_MID_SHRUB   ||
                 sizeClass == SizeClass.CLOSED_MID_SHRUB) {
          row = 2;
        }
        else if (sizeClass == SizeClass.OPEN_TALL_SHRUB ||
                 sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
          row = 4;
        }

        if (density == Density.ONE   || density == Density.TWO) {
          row += 0;
        }
        else if (density == Density.THREE || density == Density.FOUR) {
          row += 1;
        }
        else {
          row = -1;
        }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0: // SS
          case 1: // P0LE
            if ((density == Density.TWO) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 0;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
                 treatType == TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     (treatType != TreatmentType.NONE &&
                      treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
                      treatType != TreatmentType.PRECOMMERCIAL_THINNING)) {
              row = 2;
            }
            else if (density == Density.TWO) {
              row = 3;
            }
            else if (density == Density.THREE || density == Density.FOUR) {
              row = 4;
            }
            else {
              row = -1;
            }
            break;
          case 2:
            // MEDIUM, LARGE, VERY-LARGE
            if ((density == Density.TWO || density == Density.THREE || density == Density.FOUR) &&
                ((treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
                  treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                 (pastProcessType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
                  pastProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)))) {
              row = 0;
            }
            else if ((density == Density.TWO) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 1;
            }
            else if ((density == Density.THREE || density == Density.FOUR) &&
                     ((treatType != TreatmentType.NONE &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                       treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                      (pastProcessType.equals(ProcessType.ROOT_DISEASE)  ||
                       pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
              row = 2;
            }
            else if (density == Density.ONE || density == Density.TWO ||
                     density == Density.THREE || density == Density.FOUR) {
              row = 3;
            }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        // PTS, MTS, LTS, VLTS, PMU, MMU, LMU, VLMU, MU
        if ((density == Density.TWO) &&
            (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
             treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
          row = 0;
        }
        else if ((density == Density.TWO) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 1;
        }
        else if ((density == Density.THREE || density == Density.FOUR) &&
                 ((treatType != TreatmentType.NONE &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN &&
                   treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) ||
                  (pastProcessType.equals(ProcessType.ROOT_DISEASE) ||
                   pastProcessType.equals(ProcessType.SEVERE_BARK_BEETLES)))) {
          row = 2;
        }
        else if (density == Density.ONE || density == Density.TWO ||
                 density == Density.THREE || density == Density.FOUR) {
          row = 3;
        }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.MSF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }

  private static ProcessType getHighTypeOfFireAlaska(
      SizeClass.Structure structure, int page, int resistance, SizeClass sizeClass,
      Density density, ProcessType pastProcessType, TreatmentType treatType) {

    int     row = -1;

    switch (structure) {
      case NON_FOREST:
        if (density == Density.C) { row = 0; }
        break;
      case SINGLE_STORY:
        switch (page) {
          case 0:
          case 1:
          case 2:
            if (density == Density.W) { row = 0; }
            if (density == Density.O) { row = 1; }
            if (density == Density.C) { row = 2; }
            break;
          default:
            row = -1;
        }
        break;
      case MULTIPLE_STORY:
        if (density == Density.W)      { row = 0; }
        else if (density == Density.O) { row = 1; }
        else if (density == Density.C) { row = 2; }
        break;
      default:
        row = -1;
    }

    if (page == -1 || row == -1) {
      return ProcessType.SRF;
    }
    return getFireTypeData(resistance,structure,page,row);
  }
  // **********************************************************************
  public static ProcessType getTypeOfFire(int resistance,
                                          SizeClass sizeClass,
                                          Density density,
                                          ProcessType pastProcessType,
                                          TreatmentType treatType)
  {
    int         zoneId = Simpplle.getCurrentZone().getId();
    ProcessType fireProcessType;

    if (zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      return getTypeOfFireAlaska(resistance,sizeClass,density,pastProcessType,treatType);
    }
    else {
      return getTypeOfFireCommon(resistance,sizeClass,density,pastProcessType,treatType);
    }
  }

  private static ProcessType getTypeOfFireCommon(int resistance,
                                                 SizeClass sizeClass,
                                                 Density density,
                                                 ProcessType pastProcessType,
                                                 TreatmentType treatType)
  {

    int       page = -1;
    SizeClass.Structure structure = FireEvent.NON_FOREST;

    if (sizeClass == SizeClass.GRASS           || sizeClass == SizeClass.UNIFORM           ||
        sizeClass == SizeClass.SCATTERED       || sizeClass == SizeClass.CLUMPED           ||
        sizeClass == SizeClass.OPEN_HERB       || sizeClass == SizeClass.CLOSED_HERB       ||
        sizeClass == SizeClass.OPEN_LOW_SHRUB  || sizeClass == SizeClass.CLOSED_LOW_SHRUB  ||
        sizeClass == SizeClass.OPEN_MID_SHRUB  || sizeClass == SizeClass.CLOSED_MID_SHRUB  ||
        sizeClass == SizeClass.OPEN_TALL_SHRUB || sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
      structure = FireEvent.NON_FOREST;
      page = 0;
    }
    else if (sizeClass == SizeClass.SS) {
      structure = FireEvent.SINGLE_STORY;
      page = 0;
    }
    else if (sizeClass == SizeClass.POLE) {
      structure = FireEvent.SINGLE_STORY;
      page = 1;
    }
    else if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.LARGE ||
             sizeClass == SizeClass.VERY_LARGE) {
      structure = FireEvent.SINGLE_STORY;
      page = 2;
    }
    else if (sizeClass == SizeClass.PTS || sizeClass == SizeClass.PMU  ||
             sizeClass == SizeClass.MTS || sizeClass == SizeClass.MMU  ||
             sizeClass == SizeClass.LMU || sizeClass == SizeClass.MU   ||
             sizeClass == SizeClass.LTS || sizeClass == SizeClass.VLMU ||
             sizeClass == SizeClass.VLTS) {
      structure = FireEvent.MULTIPLE_STORY;
      page = 0;
    }
    else {
      page = -1;
    }

    switch (resistance) {
      case FireEvent.LOW:
        return getLowTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      case FireEvent.MODERATE:
        return getModerateTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      case FireEvent.HIGH:
        return getHighTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      default:
        return getLowTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
  }


  private static ProcessType getTypeOfFireAlaska(int resistance,
                                                 SizeClass sizeClass,
                                                 Density density,
                                                 ProcessType pastProcessType,
                                                 TreatmentType treatType)
  {

    int       page = -1;
    SizeClass.Structure structure = FireEvent.NON_FOREST;

    if (sizeClass == SizeClass.HERB        || sizeClass == SizeClass.GH        ||
        sizeClass == SizeClass.TALL_SHRUB  || sizeClass == SizeClass.LOW_SHRUB ||
        sizeClass == SizeClass.DWARF_SHRUB || sizeClass == SizeClass.AQU) {
      structure = FireEvent.NON_FOREST;
      page = 0;
    }
    else if (sizeClass == SizeClass.SS || sizeClass == SizeClass.SS_SS) {
      structure = FireEvent.SINGLE_STORY;
      page = 0;
    }
    else if (sizeClass == SizeClass.POLE || sizeClass == SizeClass.POLE_POLE) {
      structure = FireEvent.SINGLE_STORY;
      page = 1;
    }
    else if (sizeClass == SizeClass.LARGE ||
             sizeClass == SizeClass.LARGE_LARGE) {
      structure = FireEvent.SINGLE_STORY;
      page = 2;
    }
    else if (sizeClass == SizeClass.SS_POLE || sizeClass == SizeClass.SS_LARGE  ||
             sizeClass == SizeClass.POLE_SS || sizeClass == SizeClass.POLE_LARGE  ||
             sizeClass == SizeClass.LARGE_SS || sizeClass == SizeClass.LARGE_POLE) {
      structure = FireEvent.MULTIPLE_STORY;
      page = 0;
    }
    else {
      page = -1;
    }

    switch (resistance) {
      case FireEvent.LOW:
        return getLowTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      case FireEvent.MODERATE:
        return getModerateTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      case FireEvent.HIGH:
        return getHighTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
      default:
        return getLowTypeOfFire(structure,page,resistance,sizeClass,density,pastProcessType,treatType);
    }
  }

  // Fire Type data is organized into resistance, pages, rows, and moisture
  // The first number in the file is the number of resistance groups.
  // The second number in the file (on line 2) is the number of pages
  //   in the following group (i.e. the number of lines)
  // Each group is proceeded by a line specifying the number of lines
  //   that follow.
  // Lines in the input file are the pages.
  // Each line has the rows comma delimited.
  // Each row is colon delimited has three values
  // The three values are moisture: WETTER, NORMAL, DRIER
  public static void readData(BufferedReader fin) throws SimpplleError {
    String              line;
    StringTokenizerPlus strTok, values;
    int                 numPages, numRows, numCols;
    int                 resist, page, row, col;
    ProcessType         processType;

    try {
      line   = fin.readLine();
      if (line == null) {
        throw new ParseError("Fire Fire Type Data file is empty.");
      }
      fireTypeData =
        new ProcessType[NUM_RESISTANCE][NUM_STRUCTURE][][][];

      // Non Forest
      numPages = Integer.parseInt(line);
      for(resist=0; resist<NUM_RESISTANCE; resist++) {
        fireTypeData[resist][FireEvent.NON_FOREST.ordinal()] = new ProcessType[numPages][][];
      }

      // Single Story
      line     = fin.readLine();
      numPages = Integer.parseInt(line);
      for(resist=0; resist<NUM_RESISTANCE; resist++) {
        fireTypeData[resist][FireEvent.SINGLE_STORY.ordinal()] = new ProcessType[numPages][][];
      }

      // Multiple Story
      line     = fin.readLine();
      numPages = Integer.parseInt(line);
      for(resist=0; resist<NUM_RESISTANCE; resist++) {
        fireTypeData[resist][FireEvent.MULTIPLE_STORY.ordinal()] = new ProcessType[numPages][][];
      }

      for(resist=0;resist<NUM_RESISTANCE;resist++) {
        line   = fin.readLine();  // Eat Comment
        if (line == null) {
          throw new ParseError("Fire Type Data file is missing data.");
        }

        for (SizeClass.Structure structure : SizeClass.Structure.values()) {
          for(page=0;page<fireTypeData[resist][structure.ordinal()].length;page++) {
            line    = fin.readLine();
            strTok  = new StringTokenizerPlus(line,",");
            numRows = strTok.countTokens();
            fireTypeData[resist][structure.ordinal()][page] = new ProcessType[numRows][];

            for(row=0;row<numRows;row++) {
              values = new StringTokenizerPlus(strTok.getToken(),":");
              numCols = values.countTokens();
              fireTypeData[resist][structure.ordinal()][page][row] = new ProcessType[numCols];
              for(col=0;col<numCols;col++) {
                processType = ProcessType.get(values.getToken());
                setFireTypeData(resist,structure,page,row,col,processType);
              }
            }
          }
        }
      }
    }
    catch (NumberFormatException nfe) {
      String msg = "Invalid value found in fire type data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError pe) {
      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);
    }
    catch (IOException e) {
      String msg = "Problems read from fire type data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (Exception e) {
      String msg = "Invalid or missing data in Fire Type Data File.";
      System.out.println(msg);
      e.printStackTrace();
      throw new SimpplleError(msg);
    }
  }
  public static void loadData(File path) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(path));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readData(fin);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading from Fire Type data file:" + path;
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }

  private static void setFireTypeData(int resistance, SizeClass.Structure structure, int page,
                                      int row, int moisture, ProcessType processType) {
    fireTypeData[resistance][structure.ordinal()][page][row][moisture] = processType;
  }
  public static ProcessType getFireTypeData(int resistance,SizeClass.Structure structure,int page,int row) {
    return fireTypeData[resistance][structure.ordinal()][page][row][moisture.ordinal()];
  }

  public static ProcessType getData(int resistance, SizeClass sizeClass, Density density,
                                    TreatmentType treatment, ProcessType process,
                                    Climate.Moisture climateMoisture) {
    moisture = climateMoisture;
    return getTypeOfFire(resistance,sizeClass,density,process,treatment);
  }

}
