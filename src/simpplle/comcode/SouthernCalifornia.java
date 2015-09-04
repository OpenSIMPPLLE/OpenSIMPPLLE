
package simpplle.comcode;

import java.util.Hashtable;


/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for Southern California.
 * The primary purpose is to initialize class fields with values unique to Southern California.  
 * <p>As a result most member functions are private.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.RegionalZone
 *
 */



public class SouthernCalifornia extends RegionalZone {
  private static final String arcviewDir  = "gis/southern-california";
  private static final String homeDir = "knowledge/zones/southern-california";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};
/**
 * Constructor for Southern California.  
 */
  public SouthernCalifornia() {
    super();
    name         = "Southern California";
    available    = true;
    zoneDir      = homeDir;
    pathwayKnowFile = "zones/southern-california-pathways.jar";
    sysKnowFile  = "zones/southern-california.jar";
    zoneDefnFile = "zones/southern-california-defn.jar";
    gisExtraFile = "zones/southern-california-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_BARK_BEETLES,
      ProcessType.BB_RD_DM_COMPLEX,
      ProcessType.ROOT_DISEASE,
    };

    createSampleAreas();
  }

  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Angeles","SAMPLE-AREAS/ANGELES.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return a int representing the id of this zone.
   */
  public int getId () {
    return ValidZones.SOUTHERN_CALIFORNIA;
  }

}
