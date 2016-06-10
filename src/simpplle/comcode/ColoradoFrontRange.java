package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> ColoradoFrontRange describes the region of Colorado east of the foothills of the Front Range.
 * 
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class ColoradoFrontRange extends RegionalZone {

  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  public ColoradoFrontRange () {

    super();

    name            = "Colorado Front Range";
    available       = true;
    zoneDir         = "knowledge/zones/colorado-front-range";
    arcviewDir      = "gis/colorado-front-range";
    pathwayKnowFile = "zones/colorado-front-range-pathways.jar";
    sysKnowFile     = "zones/colorado-front-range.jar";
    zoneDefnFile    = "zones/colorado-front-range-defn.jar";
    gisExtraFile    = "zones/colorado-front-range-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.PIED_BB
    };

    sampleAreas = new Area[2];
    sampleAreas[0] = new Area("Wet Mtns","SAMPLE-AREAS/WETMTN.AREA",Area.SAMPLE);
    sampleAreas[1] = new Area("Trout","SAMPLE-AREAS/TROUT.AREA",Area.SAMPLE);

  }

  public ProcessType[] getUserProbProcesses() {
    return probDataProcesses;
  }

  protected String[] getGisFiles() {
    return gisFiles;
  }

  public int getId () {
    return ValidZones.COLORADO_FRONT_RANGE;
  }

}
