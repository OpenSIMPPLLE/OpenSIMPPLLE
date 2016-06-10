package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> SouthCentralAlaska describes the shorelines and uplands of the central Gulf of Alaska.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class SouthCentralAlaska extends RegionalZone {

  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  public SouthCentralAlaska () {

    super();

    id              = ValidZones.SOUTH_CENTRAL_ALASKA;
    name            = "South Central Alaska";
    available       = true;
    zoneDir         = "knowledge/zones/south-central-alaska";
    arcviewDir      = "gis/south-central-alaska";
    pathwayKnowFile = "zones/south-central-alaska-pathways.jar";
    sysKnowFile     = "zones/south-central-alaska.jar";
    zoneDefnFile    = "zones/south-central-alaska-defn.jar";
    gisExtraFile    = "zones/south-central-alaska-gis.jar";

    userProbProcesses = new ProcessType[] {
      ProcessType.ROOT_DISEASE
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Kenai","SAMPLE-AREAS/KENAI.AREA",Area.SAMPLE);

  }

  protected String[] getGisFiles() {
    return gisFiles;
  }

}


