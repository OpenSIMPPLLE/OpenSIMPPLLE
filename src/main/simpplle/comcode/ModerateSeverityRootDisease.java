package simpplle.comcode;

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
public class ModerateSeverityRootDisease extends Process {
  private static final String printName = "MODERATE-SEVERITY-ROOT-DISEASE";
  public ModerateSeverityRootDisease () {
    super();

    spreading   = false;
    description = "Moderate Severity Root Disease\"";

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

  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    return 0;
  }
  public int doProbability (ColoradoPlateau zone, Evu evu) {
    return 0;
  }
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return 0;
  }

  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return 0;
  }
  public int doProbability (Teton zone, Evu evu) {
    return 0;
  }
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    return 0;
  }
  public int doProbability (SierraNevada zone, Evu evu) {
    return 0;
  }
  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return 0;
  }
  public int doProbability (SouthCentralAlaska zone, Evu evu) {
    return 0;
  }
}
