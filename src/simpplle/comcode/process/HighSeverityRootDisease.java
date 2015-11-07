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
 *<p>This class defines the LightSeverityFire class, a type of Disturbance Process.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class HighSeverityRootDisease extends Process {
  private static final String printName = "HIGH-SEVERITY-ROOT-DISEASE";
  public HighSeverityRootDisease () {
    super();

    spreading   = false;
    description = "High Severity Root Disease\"";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.ECO_GROUP_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.TREATMENT_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
  public String toString () {
    return printName;
  }
  protected int doProbability (Evu evu) {
    return 0;
  }

  public int doProbability (simpplle.comcode.zone.ColoradoFrontRange zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.ColoradoPlateau zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, Evu evu) {
    return 0;
  }

  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.Teton zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, Evu evu) {
    return 0;
  }
}
