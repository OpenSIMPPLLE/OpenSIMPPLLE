package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> NorthernCentralRockies describes the Rocky Mountain region extending from southeastern British Columbia to
 * northwestern Montana.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class NorthernCentralRockies extends RegionalZone {

  public NorthernCentralRockies() {

    super();

    id              = ValidZones.NORTHERN_CENTRAL_ROCKIES;
    name            = "Northern Central Rockies";
    available       = true;
    zoneDir         = "knowledge/zones/northern-central-rockies";
    arcviewDir      = "gis/northern-central-rockies";
    pathwayKnowFile = "zones/northern-central-rockies-pathways.jar";
    sysKnowFile     = "zones/northern-central-rockies.jar";
    zoneDefnFile    = "zones/northern-central-rockies-defn.jar";
    gisExtraFile    = "zones/northern-central-rockies-gis.jar";
    gisFiles        = new String[] {};

    userProbProcesses = new ProcessType[] {
      ProcessType.LIGHT_LP_MPB,
      ProcessType.DF_BEETLE,
      ProcessType.ROOT_DISEASE,
      ProcessType.SPRUCE_BEETLE,
      ProcessType.WBP_MPB
    };

    //sampleAreas = new Area[1];
    //sampleAreas[0] = new Area("Poorman","SAMPLE-AREAS/POORMAN.AREA",Area.SAMPLE);

  }
}
