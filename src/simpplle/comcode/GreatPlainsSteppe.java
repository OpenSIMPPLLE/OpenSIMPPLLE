package simpplle.comcode;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for Great Plains Steppe, a Regional Zone.
 * The primary purpose is to initialize class fields with values unique Great Plains Steppe.  
 * As a result most member functions are private.
 * <p> This geographic area was developed along with Mixed Grass Prairie and Western Great Plains Steppe.  
 * Initially this was used to assist in developing a representation of historic conditions and an emphasis was placed on including bison grazing.  
 *
 * @author Documentation by Brian Losi
 * <p> Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.RegionalZone
 */
public class GreatPlainsSteppe extends RegionalZone {
  private static final String arcviewDir  = "gis/great-plains-steppe";
  private static final String homeDir = "knowledge/zones/great-plains-steppe";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};
/**
 * Constructor.  Inherits from Regional Zone superclass and intializes name to Great Plains Steppe, makes available, and initializes jar file pathways 
 * 
 */
  public GreatPlainsSteppe() {
    super();
    name         = "Great Plains Steppe";
    available    = true;
    zoneDir      = homeDir;
    hasAquatics  = true;

    pathwayKnowFile = "zones/great-plains-steppe-pathways.jar";
    sysKnowFile     = "zones/great-plains-steppe.jar";
    zoneDefnFile    = "zones/great-plains-steppe-defn.jar";
//    gisExtraFile    = "zones/great-plains-steppe-gis.jar";

    probDataProcesses = null;

    createSampleAreas();
  }
/**
 * Checks if the current zone is GreatPlainsSteppe zone is the current zone.  
 * @return true if current zone is GreatPlainsSteppe
 */
  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) { return false; }

    return (zone instanceof GreatPlainsSteppe);
  }
/**
 * gets files at "gis/great-plains-steppe"
 */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  
  /**
 * gets Gis files.  The array of which is created in GreatPlainsSteppe.  
 */
  protected String[] getGisFiles() { return gisFiles; }
/**
 * Creates sample areas.  There is only one: "NE Haakon" 
 */
  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("NE Haakon","SAMPLE-AREAS/NE_HAAKON4.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone GREAT_PLAINS_STEPPE = 11
   * @see simpplle.comcode.ValidZones
   * @return a int representing the id of this zone = 11.
   */
  public int getId () {
    return ValidZones.GREAT_PLAINS_STEPPE;
  }

}

