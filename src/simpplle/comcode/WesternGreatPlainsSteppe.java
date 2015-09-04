package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>Western Great Plains Steppe is a regional zone in Wyoming.  It is a grassland zone.  Bison grazing is particular important in this zone.  
 *<p> This geographic area was developed along with Mixed Grass Prairie and  Great Plains Steppe.  
 * Initially this was used to assist in developing a representation of historic conditions and an emphasis was placed on including bison grazing.
 * <p>Disturbance Process- succession, light, moderate, and heavy bison grazing; stand replacing fire, wet and dry succession, prairie dog inactive and active.     
 * @author Documentation by Brian Losi
 * <p>Original authorship: Kirk A. Moeller
 *
 */

public class WesternGreatPlainsSteppe extends RegionalZone {
  private static final String arcviewDir  = "gis/western-great-plains-steppe";
  private static final String homeDir = "knowledge/zones/western-great-plains-steppe";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor for Western Great Plain Steppe.   
   */
  public WesternGreatPlainsSteppe() {
    super();
    name         = "Western Great Plains Steppe";
    available    = true;
    zoneDir      = homeDir;
    hasAquatics  = true;

    pathwayKnowFile = "zones/western-great-plains-steppe-pathways.jar";
    sysKnowFile     = "zones/western-great-plains-steppe.jar";
    zoneDefnFile    = "zones/western-great-plains-steppe-defn.jar";
    gisExtraFile    = "zones/western-great-plains-steppe-gis.jar";

    probDataProcesses = new ProcessType[] {
      ProcessType.BISON_GRAZING
    };

    createSampleAreas();
  }
/**
 * Checks if current zone is instance of Western Great Plains Steppe.  
 * @return
 */
  public static boolean isCurrent() {
    RegionalZone zone = Simpplle.getCurrentZone();
    if (zone == null) { return false; }

    return (zone instanceof WesternGreatPlainsSteppe);
  }
/**
 * Returns "gis/western-great-plains-steppe"
 */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

  protected String[] getGisFiles() { return gisFiles; }
/**
 * The sample area for Western Great Plains Steppe is Tunder Basin-Clipped
 */
  private void createSampleAreas () {
    sampleAreas    = new Area[1];
    sampleAreas[0] = new Area("Thunder Basin Clipped","SAMPLE-AREAS/THUNDERBASIN-CLIPPED.AREA",Area.SAMPLE);
  }

  /**
   * Gets the id of this zone.
   * @see simpplle.comcode.ValidZones
   * @return a int representing the id of this zone.
   */
  public int getId () {
    return ValidZones.WESTERN_GREAT_PLAINS_STEPPE;
  }



}
