package simpplle.comcode;

import java.util.Hashtable;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for .
 * The primary purpose is to initialize class fields with
 * values unique to westside region one.  As a result
 * most member functions are private.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class WestsideRegionOne extends RegionalZone {
  private static final String arcviewDir  = "gis/westside-r1";
  private static final String homeDir = "knowledge/zones/westside-r1";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Initializes some several fields inherited from RegionalZone.
   */
  public WestsideRegionOne () {
    super();
    name         = "Westside Region One";
    available    = true;
    hasAquatics  = true;
    zoneDir      = homeDir;
    pathwayKnowFile = "zones/westside-r1-pathways.jar";
    sysKnowFile  = "zones/westside-r1.jar";
    zoneDefnFile = "zones/westside-r1-defn.jar";
    gisExtraFile = "zones/westside-r1-gis.jar";

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
      //Quack - New Processes (Also at Line 132 in ProcessType.java)
      //New Process types for WSBW
      ProcessType.LIGHT_WSBW,
      ProcessType.SEVERE_WSBW
    };

    createSampleAreas();
  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[2];
    sampleAreas[0] = new Area("Stevi West Central","SAMPLE-AREAS/STEVIWC.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Sweathouse Creek","SAMPLE-AREAS/SWEATHOUSE.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return a int representing the id of this zone.
   */
  public int getId () {
    return ValidZones.WESTSIDE_REGION_ONE;
  }

}


