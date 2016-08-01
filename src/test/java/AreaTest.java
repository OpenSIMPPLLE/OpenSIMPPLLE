/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

import org.junit.Test;
import simpplle.comcode.*;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests pertaining to the Area class
 */
public class AreaTest {

  @Test
  public void calculatesSlope(){
    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Area area = new Area();

    // Set up to evu
    Evu evu = new Evu();
    evu.setAcres((float)22.950);  // should result in 1000 ft sides
    evu.setElevation(305);  // about 1000 ft

    // Set up adjacent data
    Evu neighborEvu = new Evu();
    neighborEvu.setElevation(274); // about 900
    AdjacentData adjacentData = new AdjacentData(neighborEvu, 'E', 'N', 270, 4, 270);

    // Check right angle adjacency
    assertEquals(-.101, area.calcSlope(evu, adjacentData), .001);

    // Check corner adjacency
    adjacentData.setSpread(315);
    assertEquals(-.0719, area.calcSlope(evu, adjacentData), .001);
  }
}
