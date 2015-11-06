package simpplle.comcode.zone;

import simpplle.comcode.Area;
import simpplle.comcode.ProcessType;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.ValidZones;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods pertaining to the Colorado Front Range. This is a regional zone. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 * @since SIMPPLLE V2.4
 * @see simpplle.comcode.RegionalZone
 */

public class ColoradoPlateau extends RegionalZone {
  private static final String arcviewDir  = "gis/colorado-plateau";
  private static final String homeDir = "knowledge/zones/colorado-plateau";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor.  Initializes some of the variables inherited from the superclass RegionalZone
   */
  public ColoradoPlateau() {
    super();
    name            = "Colorado Plateau";
    available       = true;
    hasAquatics     = true;
    zoneDir         = homeDir;
    pathwayKnowFile = "zones/colorado-plateau-pathways.jar";
    sysKnowFile     = "zones/colorado-plateau.jar";
    zoneDefnFile    = "zones/colorado-plateau-defn.jar";
    gisExtraFile    = "zones/colorado-plateau-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.DF_BEETLE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.PIED_BB
    };

    createSampleAreas();
  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Mesa Verde","SAMPLE-AREAS/MV.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.COLORADO_PLATEAU;
  }
}
