package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> WestsideRegionOne describes the region west of the Continental Divide.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public final class WestsideRegionOne extends RegionalZone {

  private static final String arcviewDir = "gis/westside-r1";
  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  public WestsideRegionOne () {

    super();

    name            = "Westside Region One";
    available       = true;
    hasAquatics     = true;
    zoneDir         = "knowledge/zones/westside-r1";
    pathwayKnowFile = "zones/westside-r1-pathways.jar";
    sysKnowFile     = "zones/westside-r1.jar";
    zoneDefnFile    = "zones/westside-r1-defn.jar";
    gisExtraFile    = "zones/westside-r1-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.ROOT_DISEASE,
      ProcessType.LS_ROOT_DISEASE,
      ProcessType.MS_ROOT_DISEASE,
      ProcessType.HS_ROOT_DISEASE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.WBP_MPB,
      ProcessType.WP_MPB,
      ProcessType.LIGHT_WSBW,
      ProcessType.SEVERE_WSBW
    };

    sampleAreas = new Area[2];
    sampleAreas[0] = new Area("Stevi West Central","SAMPLE-AREAS/STEVIWC.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Sweathouse Creek","SAMPLE-AREAS/SWEATHOUSE.AREA",Area.SAMPLE);

  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  public int getId () {
    return ValidZones.WESTSIDE_REGION_ONE;
  }

}


