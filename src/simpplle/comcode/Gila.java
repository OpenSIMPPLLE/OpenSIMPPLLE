package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> Gila describes the Gila National Forest located in New Mexico.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public final class Gila extends RegionalZone {

  private static final String arcviewDir = "gis/gila";
  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  /**
   * Creates a regional zone for the Gila National Forest
   */
  public Gila () {

    super();

    name            = "Gila";
    available       = true;
    zoneDir         = "knowledge/zones/gila";
    pathwayKnowFile = "zones/gila-pathways.jar";
    sysKnowFile     = "zones/gila.jar";
    zoneDefnFile    = "zones/gila-defn.jar";
    gisExtraFile    = "zones/gila-gis.jar";

    probDataProcesses = new ProcessType[] {
      //ProcessType.LIGHT_LP_MPB,
      //ProcessType.DF_BEETLE,
      //ProcessType.ROOT_DISEASE,
      //ProcessType.SPRUCE_BEETLE,
      //ProcessType.WBP_MPB,
      ProcessType.PP_MPB
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Gila","SAMPLE-AREAS/GILA.AREA",Area.SAMPLE);

  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  /**
   * @return The unique identifier for this zone
   * @see simpplle.comcode.ValidZones
   */
  public int getId () {
    return ValidZones.GILA;
  }

}


