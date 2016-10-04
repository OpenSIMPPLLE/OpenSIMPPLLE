/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/** 
 * EastsideRegionOne describes the region east of the Continental Divide.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class EastsideRegionOne extends RegionalZone {

  public EastsideRegionOne () {

    super();

    id              = ValidZones.EASTSIDE_REGION_ONE;
    name            = "Eastside Region One";
    available       = true;
    zoneDir         = "knowledge/zones/eastside-r1";
    arcviewDir      = "gis/eastside-r1";
    pathwayKnowFile = "zones/eastside-r1-pathways.jar";
    sysKnowFile     = "zones/eastside-r1.jar";
    zoneDefnFile    = "zones/eastside-r1-defn.jar";
    gisExtraFile    = "zones/eastside-r1-gis.jar";
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
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.ROOT_DISEASE,
      ProcessType.LS_ROOT_DISEASE,
      ProcessType.MS_ROOT_DISEASE,
      ProcessType.HS_ROOT_DISEASE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.WBP_MPB,
      ProcessType.LIGHT_WSBW,
      ProcessType.SEVERE_WSBW
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Poorman","SAMPLE-AREAS/POORMAN.AREA",Area.SAMPLE);

  }
}


