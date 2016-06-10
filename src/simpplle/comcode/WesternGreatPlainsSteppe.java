package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> WesternGreatPlainsSteppe was developed along with the GreatPlainsSteppe and MixedGrassPrairie. The initial
 * use of this zone was to assist in developing a representation of historical conditions, so an emphasis was placed
 * on bison grazing. The use of NCRS soil ecosystems as ecological stratification is common in all three areas.
 *
 * <p> Original authorship: Kirk A. Moeller
 */

public class WesternGreatPlainsSteppe extends RegionalZone {

  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  public WesternGreatPlainsSteppe() {

    super();

    id              = ValidZones.WESTERN_GREAT_PLAINS_STEPPE;
    name            = "Western Great Plains Steppe";
    available       = true;
    hasAquatics     = true;
    zoneDir         = "knowledge/zones/western-great-plains-steppe";
    arcviewDir      = "gis/western-great-plains-steppe";
    pathwayKnowFile = "zones/western-great-plains-steppe-pathways.jar";
    sysKnowFile     = "zones/western-great-plains-steppe.jar";
    zoneDefnFile    = "zones/western-great-plains-steppe-defn.jar";
    gisExtraFile    = "zones/western-great-plains-steppe-gis.jar";

    userProbProcesses = new ProcessType[] {
      ProcessType.BISON_GRAZING
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Thunder Basin Clipped","SAMPLE-AREAS/THUNDERBASIN-CLIPPED.AREA",Area.SAMPLE);

  }

  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) return false;
    return (zone instanceof WesternGreatPlainsSteppe);
  }

  protected String[] getGisFiles() {
    return gisFiles;
  }

}
