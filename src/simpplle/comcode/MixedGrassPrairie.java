package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> MixedGrassPrairie was developed along with the GreatPlainsSteppe and WesternGreatPlainsSteppe. The initial
 * use of this zone was to assist in developing a representation of historical conditions, so an emphasis was placed
 * on bison grazing. The use of NCRS soil ecosystems as ecological stratification is common in all three areas.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class MixedGrassPrairie extends RegionalZone {

  public MixedGrassPrairie() {

    super();

    id              = ValidZones.MIXED_GRASS_PRAIRIE;
    name            = "Mixed Grass Prairie";
    available       = true;
    hasAquatics     = true;
    zoneDir         = "knowledge/zones/mixed-grass-prairie";
    arcviewDir      = "gis/mixed-grass-prairie";
    pathwayKnowFile = "zones/mixed-grass-prairie-pathways.jar";
    sysKnowFile     = "zones/mixed-grass-prairie.jar";
    zoneDefnFile    = "zones/mixed-grass-prairie-defn.jar";
    //gisExtraFile    = "zones/mixed-grass-prairie-gis.jar";
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
    sampleAreas[0] = new Area("West Faulk","SAMPLE-AREAS/WEST-FAULK.AREA",Area.SAMPLE);

  }

  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) return false;
    return (zone instanceof MixedGrassPrairie);
  }
}

