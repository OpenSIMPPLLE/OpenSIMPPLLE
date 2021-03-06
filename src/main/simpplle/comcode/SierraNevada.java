/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * SierraNevada describes a region between the Central Valley of California and the Basin and Range Province.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class SierraNevada extends RegionalZone {

  public SierraNevada() {

    super();

    id              = ValidZones.SIERRA_NEVADA;
    name            = "Sierra Nevada";
    available       = true;
    zoneDir         = "knowledge/zones/sierra-nevada";
    arcviewDir      = "gis/sierra-nevada";
    pathwayKnowFile = "zones/sierra-nevada-pathways.jar";
    sysKnowFile     = "zones/sierra-nevada.jar";
    zoneDefnFile    = "zones/sierra-nevada-defn.jar";
    gisExtraFile    = "zones/sierra-nevada-gis.jar";
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

    sampleAreas = new Area[2];
    sampleAreas[0] = new Area("Yosemite NW","SAMPLE-AREAS/YOSEMITE-NW.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Yosemite","SAMPLE-AREAS/YOSEMITE.AREA",Area.SAMPLE);

  }
}
