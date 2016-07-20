/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

/**
 * This class sets up Wsbw Logic Diagrams.  This prints out a string of explaining the logic for western bud worm.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public abstract class WsbwLogicDiagrams {

  public static String getLogic() {
    String logic;

    logic =
      "In this version the probability is set equal to the susceptibility-index\n" +
      "which is a composite of:\n" +
      "\n" +
      "  percent-host-index\n" +
      "  percent-climax-host-index\n" +
      "  density-index\n" +
      "  structure-index\n" +
      "  stand-vigor-index\n" +
      "  maturity-index\n" +
      "  site-climate-index\n" +
      "  regional-climate-index\n" +
      "  character-of-adjacent-index\n" +
      "\n" +
      "These indices are documented in:\n" +
      "  Carlson and Wulf 1989. \"Silviculture Strategies to Reduce Stand\n" +
      "  and Forest Susceptibility to the Western Spruce Budworm.\"\n" +
      "  United States Department of Agriculture, Forest Service,\n" +
      "  Agriculture Handbook No. 676, 31p.\n\n\n" +
      "Access to this logic will be available in future versions.\n";

    return logic;
  }
}