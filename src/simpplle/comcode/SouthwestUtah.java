package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> SouthwestUtah describes the region of southwest Utah.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class SouthwestUtah extends RegionalZone {

  private static final String gisFiles[] = { "simpplle_arcview.apr",
                                             "process_legend.avl",
                                             "species_legend.avl",
                                             "size_legend.avl",
                                             "canopy_legend.avl",
                                             "spread_legend.avl",
                                             "probability_legend.avl",
                                             "spread_legend.avl",
                                             "treatment_legend.avl" };

  public SouthwestUtah () {

    super();

    name            = "Southwest Utah";
    available       = true;
    zoneDir         = "knowledge/zones/southwest-utah";
    arcviewDir      = "gis/southwest-utah";
    pathwayKnowFile = "zones/southwest-utah-pathways.jar";
    sysKnowFile     = "zones/southwest-utah.jar";
    zoneDefnFile    = "zones/southwest-utah-defn.jar";
    gisExtraFile    = "zones/southwest-utah-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.PP_MPB
    };

    sampleAreas = new Area[1];
    sampleAreas[0] = new Area("Utah","SAMPLE-AREAS/UTAH.AREA",Area.SAMPLE);

  }

  public ProcessType[] getUserProbProcesses() {
    return probDataProcesses;
  }

  protected String[] getGisFiles() {
    return gisFiles;
  }

  public int getId () {
    return ValidZones.SOUTHWEST_UTAH;
  }

}
