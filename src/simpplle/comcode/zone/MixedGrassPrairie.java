package simpplle.comcode.zone;

import simpplle.comcode.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Mixed Grass Prairie, a Regional Zone.
 * <p> This geographic area was developed along with Great Plains Steppe and Western Great Plains Steppe.  
 * Initially this was used to assist in developing a representation of historic conditions and an emphasis was placed on including bison grazing. 
 *<p> The use of NCRS soil ecosystems as ecological stratification is common in all three areas.   
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 * @see simpplle.comcode.RegionalZone
 */
public class MixedGrassPrairie extends RegionalZone {
  private static final String arcviewDir  = "gis/mixed-grass-prairie";
  private static final String homeDir = "knowledge/zones/mixed-grass-prairie";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor for Mixed Grass Prairie.  Inherits from Regional Zone superclass 
   * and initializes name, available to true, hasAquatics to true, the directory and file locations
   */
  public MixedGrassPrairie() {
    super();
    name         = "Mixed Grass Prairie";
    available    = true;
    zoneDir      = homeDir;
    hasAquatics  = true;

    pathwayKnowFile = "zones/mixed-grass-prairie-pathways.jar";
    sysKnowFile     = "zones/mixed-grass-prairie.jar";
    zoneDefnFile    = "zones/mixed-grass-prairie-defn.jar";
//    gisExtraFile    = "zones/mixed-grass-prairie-gis.jar";

    probDataProcesses = null;

    createSampleAreas();
  }

  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) { return false; }

    return (zone instanceof MixedGrassPrairie);
  }
/**
 * returns gis/mixed-grass-prairie"
 */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  /**
   * returns "simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"
   */
  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("West Faulk","SAMPLE-AREAS/WEST-FAULK.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.MIXED_GRASS_PRAIRIE;
  }

}

