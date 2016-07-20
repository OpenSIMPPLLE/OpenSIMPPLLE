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
 * <p> GreatPlainsSteppe was developed along with the MixedGrassPrairie and WesternGreatPlainsSteppe. The initial
 * use of this zone was to assist in developing a representation of historical conditions, so an emphasis was placed
 * on bison grazing.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class GreatPlainsSteppe extends RegionalZone {

  public GreatPlainsSteppe() {

    super();

    id              = ValidZones.GREAT_PLAINS_STEPPE;
    name            = "Great Plains Steppe";
    available       = true;
    hasAquatics     = true;
    zoneDir         = "knowledge/zones/great-plains-steppe";
    arcviewDir      = "gis/great-plains-steppe";
    pathwayKnowFile = "zones/great-plains-steppe-pathways.jar";
    sysKnowFile     = "zones/great-plains-steppe.jar";
    zoneDefnFile    = "zones/great-plains-steppe-defn.jar";
    //gisExtraFile    = "zones/great-plains-steppe-gis.jar";
    gisFiles        = new String[] { "simpplle_arcview.apr",
                                     "process_legend.avl",
                                     "species_legend.avl",
                                     "size_legend.avl",
                                     "canopy_legend.avl",
                                     "spread_legend.avl",
                                     "probability_legend.avl",
                                     "spread_legend.avl",
                                     "treatment_legend.avl" };

    userProbProcesses = null;

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("NE Haakon","SAMPLE-AREAS/NE_HAAKON4.AREA",Area.SAMPLE);

  }

  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) return false;
    return (zone instanceof GreatPlainsSteppe);
  }
}

