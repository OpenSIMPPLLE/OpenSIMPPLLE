package simpplle.comcode.process;

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
public class LightSeverityRootDisease extends Process {
  private static final String printName = "LIGHT-SEVERITY-ROOT-DISEASE";
  public LightSeverityRootDisease () {
    super();

    spreading   = false;
    description = "Light Severity Root Disease\"";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ECO_GROUP_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.SPECIES_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.TREATMENT_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
  public String toString () {
    return printName;
  }
  protected int doProbability (simpplle.comcode.element.Evu evu) {
    return 0;
  }

  public int doProbability (simpplle.comcode.zone.ColoradoFrontRange zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.ColoradoPlateau zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }

  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }
}
