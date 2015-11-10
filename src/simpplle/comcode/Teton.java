package simpplle.comcode;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the zone for Teton.
 * The primary purpose is to initialize class fields with values unique to Teton.  
 * <p>As a result most member functions are private.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *  @see simpplle.comcode.RegionalZone
 *
 */
public class Teton extends RegionalZone {
  private static final String arcviewDir  = "gis/teton";
  private static final String homeDir = "knowledge/zones/teton";
  private static final String gisFiles[] =
    {"simpplle_arcview.apr", "process_legend.avl", "species_legend.avl",
     "size_legend.avl", "canopy_legend.avl", "spread_legend.avl",
     "probability_legend.avl", "spread_legend.avl", "treatment_legend.avl"};

  /**
   * Constructor for Teton regional zone.  Inherits from regional zone and initializes some fields inherited from RegionalZone.
   */
  public Teton() {
    super();
    name      = "Teton";
    available = true;
    zoneDir = homeDir;
    pathwayKnowFile = "zones/teton-pathways.jar";
    sysKnowFile     = "zones/teton.jar";
    zoneDefnFile    = "zones/teton-defn.jar";
    gisExtraFile    = "zones/teton-gis.jar";

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
   * @return  "gis/teton"
   */
  public String getArcviewDir() { return arcviewDir; }

  public ProcessType[] getUserProbProcesses() { return probDataProcesses; }

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
    return ValidZones.TETON;
  }

}
