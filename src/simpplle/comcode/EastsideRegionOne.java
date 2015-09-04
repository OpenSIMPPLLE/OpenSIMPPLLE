package simpplle.comcode;


/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for Eastside Region 1.  This is a Regional Zone
 * The primary purpose is to initialize class fields with
 * values unique to Eastside Region 1.  As a result most member functions are private.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 * @see simpplle.comcode.RegionalZone
 *
 */

public final class EastsideRegionOne extends RegionalZone {
  private static final String arcviewDir  = "gis/eastside-r1";
  private static final String homeDir = "knowledge/zones/eastside-r1";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Eastside Region 1 constructor.  This initializes several fields inherited from RegionalZone superclass.
   */
  public EastsideRegionOne () {
    super();
    name      = "Eastside Region One";
    available = true;
    zoneDir = homeDir;
    pathwayKnowFile = "zones/eastside-r1-pathways.jar";
    sysKnowFile     = "zones/eastside-r1.jar";
    zoneDefnFile    = "zones/eastside-r1-defn.jar";
    gisExtraFile    = "zones/eastside-r1-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.ROOT_DISEASE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.WBP_MPB
    };

    createSampleAreas();
  }
/**
 * Gets the arc view directory.  This is "gis/eastside-r1".
 */
  public String getArcviewDir() { return arcviewDir; }
/**
 * Gets all the user designated process types for Eastside Region 1.
 */
  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }
/**
 * Creates sample areas.  
 */
  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Poorman","SAMPLE-AREAS/POORMAN.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return id of this zone.
   */
  public int getId () {
    return ValidZones.EASTSIDE_REGION_ONE;
  }

}


