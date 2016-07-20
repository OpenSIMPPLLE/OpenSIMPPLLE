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
 * <p> Colorado Plateau describes a region of the Intermontane Plateaus, lying within Utah, Colorado, Arizona,
 * and New Mexico.
 *
 * <p> Original authorship: Kirk A. Moeller
 */

public class ColoradoPlateau extends RegionalZone {

  public ColoradoPlateau() {

    super();

    id              = ValidZones.COLORADO_PLATEAU;
    name            = "Colorado Plateau";
    available       = true;
    hasAquatics     = true;
    zoneDir         = "knowledge/zones/colorado-plateau";
    arcviewDir      = "gis/colorado-plateau";
    pathwayKnowFile = "zones/colorado-plateau-pathways.jar";
    sysKnowFile     = "zones/colorado-plateau.jar";
    zoneDefnFile    = "zones/colorado-plateau-defn.jar";
    gisExtraFile    = "zones/colorado-plateau-gis.jar";
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
      ProcessType.DF_BEETLE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.PIED_BB
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Mesa Verde","SAMPLE-AREAS/MV.AREA",Area.SAMPLE);

  }
}
