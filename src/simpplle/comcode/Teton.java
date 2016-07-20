/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> Teton describes the Teton Range of the Rocky Mountains.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class Teton extends RegionalZone {

  public Teton() {

    super();

    id              = ValidZones.TETON;
    name            = "Teton";
    available       = true;
    zoneDir         = "knowledge/zones/teton";
    arcviewDir      = "gis/teton";
    pathwayKnowFile = "zones/teton-pathways.jar";
    sysKnowFile     = "zones/teton.jar";
    zoneDefnFile    = "zones/teton-defn.jar";
    gisExtraFile    = "zones/teton-gis.jar";
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
      ProcessType.SPRUCE_BEETLE,
      ProcessType.WBP_MPB
    };

    //sampleAreas = new Area[1];
    //sampleAreas[0] = new Area("Poorman","SAMPLE-AREAS/POORMAN.AREA",Area.SAMPLE);

  }
}
