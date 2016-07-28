/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * SouthernCalifornia describes the Southern California mountains and valleys.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class SouthernCalifornia extends RegionalZone {

  public SouthernCalifornia() {

    super();

    id              = ValidZones.SOUTHERN_CALIFORNIA;
    name            = "Southern California";
    available       = true;
    zoneDir         = "knowledge/zones/southern-california";
    arcviewDir      = "gis/southern-california";
    pathwayKnowFile = "zones/southern-california-pathways.jar";
    sysKnowFile     = "zones/southern-california.jar";
    zoneDefnFile    = "zones/southern-california-defn.jar";
    gisExtraFile    = "zones/southern-california-gis.jar";
    gisFiles        = new String[] { "simpplle_arcview.apr",
                                     "process_legend.avl",
                                     "species_legend.avl",
                                     "size_legend.avl",
                                     "canopy_legend.avl",
                                     "spread_legend.avl",
                                     "probability_legend.avl",
                                     "spread_legend.avl",
                                     "treatment_legend.avl" };

    userProbProcesses = new ProcessType[] {
      ProcessType.LIGHT_BARK_BEETLES,
      ProcessType.BB_RD_DM_COMPLEX,
      ProcessType.ROOT_DISEASE
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Angeles","SAMPLE-AREAS/ANGELES.AREA",Area.SAMPLE);

  }
}
