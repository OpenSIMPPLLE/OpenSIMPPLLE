package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for Northern Central Rockies.
 * The primary purpose is to initialize class fields with values unique to eastside region one.  As a result
 * most member functions are private.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.RegionalZone
 *
 */

public class NorthernCentralRockies extends RegionalZone {
  private static final String arcviewDir  = "gis/northern-central-rockies";
  private static final String homeDir = "knowledge/zones/northern-central-rockies";
  private static final String gisFiles[] =
    {};

  /**
   * Constructor for Northern Central Rockies.  Inherits from Regional Zone superclass and 
   * initializes name, available to true, zone directory, file pathways to jar files, and initializes process types.
   */
  public NorthernCentralRockies() {
    super();
    name      = "Northern Central Rockies";
    available = true;
    zoneDir = homeDir;
    pathwayKnowFile = "zones/northern-central-rockies-pathways.jar";
    sysKnowFile     = "zones/northern-central-rockies.jar";
    zoneDefnFile    = "zones/northern-central-rockies-defn.jar";
    gisExtraFile    = "zones/northern-central-rockies-gis.jar";

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
 * returns "gis/northern-central-rockies""gis/northern-central-rockies"
 */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }
/**
 * returns null
 */
  protected String[] getGisFiles() { return gisFiles; }

  private void createSampleAreas () {
//    sampleAreas    = new Area[1];
//    sampleAreas[0] = new Area("Poorman","SAMPLE-AREAS/POORMAN.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return the id of this zone.
   */
  public int getId () {
    return ValidZones.NORTHERN_CENTRAL_ROCKIES;
  }

}
