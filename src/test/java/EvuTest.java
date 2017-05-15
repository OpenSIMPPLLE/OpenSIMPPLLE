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
 * Contains tests pertaining to the Evu class
 */
public class EvuTest {

  @Test
  public void getsAzimuthDifference(){
    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Evu evu = new Evu();

    assertEquals(91.0,  evu.getAzimuthDifference(270.0, 1.0), 0);
    assertEquals(89.0,  evu.getAzimuthDifference(90.0,  1.0), 0);
    assertEquals(180.0, evu.getAzimuthDifference(181.0, 1.0), 0);
    assertEquals(179.0, evu.getAzimuthDifference(181.0, 0.0), 0);
  }

  @Test
  public void isActuallyDownwind(){
    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Evu evu = new Evu();

    assertEquals('D', evu.isDownwind(0,0));
    assertEquals('D', evu.isDownwind(0,45));
    assertEquals('N', evu.isDownwind(0,46));
  }

  @Test
  public void getsInitialVegState(){

    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Evu evu = new Evu();

    VegSimStateData fallState = new VegSimStateData();
    fallState.setLifeform(Lifeform.TREES);
    evu.setState(fallState, Climate.Season.FALL);

    VegSimStateData wrongState = new VegSimStateData();
    wrongState.setLifeform(Lifeform.TREES);
    evu.setState(wrongState, Climate.Season.YEAR);


    // getInitialState should use the proper season, rather than Season.YEAR
    assertEquals(fallState, evu.getInitialVegState(Lifeform.TREES, Climate.Season.FALL));
  }

  /**
   * Evu.getState() is specially configured to look up Season.YEAR rather than the given season when
   * the time step is zero. This was pre-existing functionality, so Evu.getInitialVegState was created
   * to avoid changing the assumption made by Evu.getState().
   */
  @Test
  public void getsYearStateAtTimeZero(){

    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Evu evu = new Evu();

    VegSimStateData yearState = new VegSimStateData();
    yearState.setLifeform(Lifeform.TREES);
    evu.setState(yearState, Climate.Season.YEAR);

    VegSimStateData fallState = new VegSimStateData();
    fallState.setLifeform(Lifeform.TREES);
    evu.setState(fallState, Climate.Season.FALL);

    // Despite sending Season.FALL, the year state should be returned
    assertEquals(yearState, evu.getState(0, Lifeform.TREES, Climate.Season.FALL));
  }

  /**
   * Evu.getNeighborIndex is used to populate and look up the indicies of adjacent units based on
   * their spread angle. Units are populated clockwise from 0 degrees azimuth.
   */
  @Test
  public void getsNeighborIndex(){
    // evu expects the current zone to be set
    Simpplle.setCurrentZone(new WestsideRegionOne());
    Evu evu = new Evu();

    assertEquals(0, evu.getNeighborIndex(0));
    assertEquals(0, evu.getNeighborIndex(1));
    assertEquals(0, evu.getNeighborIndex(44));
    assertEquals(1, evu.getNeighborIndex(45));
    assertEquals(2, evu.getNeighborIndex(90));
    assertEquals(2, evu.getNeighborIndex(91));
    assertEquals(3, evu.getNeighborIndex(135));
    assertEquals(3, evu.getNeighborIndex(179));
    assertEquals(4, evu.getNeighborIndex(180));
    assertEquals(5, evu.getNeighborIndex(225));
    assertEquals(6, evu.getNeighborIndex(270));
    assertEquals(7, evu.getNeighborIndex(315));
    assertEquals(7, evu.getNeighborIndex(359));
    assertEquals(0, evu.getNeighborIndex(360));
    assertEquals(0, evu.getNeighborIndex(361));
    assertEquals(4, evu.getNeighborIndex(540));
  }

}
