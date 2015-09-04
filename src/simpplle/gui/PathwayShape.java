
package simpplle.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import simpplle.comcode.Process;
import simpplle.comcode.Species;
import simpplle.comcode.VegetativeType;
/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class creates the vegetative pathway diagrams.  
 * 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class PathwayShape {
  VegetativeType state;
  public int     width, height;
  int     x, y;
  Color   color, normalColor;
  boolean selected, changingNextState;
  boolean showLabel=false;

  private static final Color SPECIES_COLOR     = Color.yellow;
  private static final Color NON_SPECIES_COLOR = new Color(160,170,160);
  private static final Color SELECTED_COLOR    = Color.orange;
  private static final Color TEXT_COLOR        = Color.black;
  private static final Color NODE_COLOR        = Color.blue;
  private static final int   DEFAULT_SIZE			 = 30;
/**
 * Constructor for pathway shape.  Starts the pathway at (10,10).  
 */
  public PathwayShape() {
    x               = 10;
    y               = 10;
    width           = DEFAULT_SIZE;
    height          = DEFAULT_SIZE;
    normalColor     = SPECIES_COLOR;
    color           = normalColor;
    selected        = false;
  }
/**
 * Overloaded constructor which sets the vegetative state 
 * @param state
 */
  public PathwayShape(VegetativeType state) {
    this();
    this.state    = state;
  }
/**
 * Overloaded constructor for pathway shape.  sets the state, width, and height to parameter arguments.  
 * @param state
 * @param width
 * @param height
 */
  public PathwayShape(VegetativeType state, int width, int height) {
    this();
    this.state  = state;
    this.width  = width;
    this.height = height;
  }
/**
 * Gets the vegetative state
 * @return vegetative type.  
 */
  public VegetativeType getState() { return state; }
/**
 * Sets the next state from a process and vegetative type next state.  
 * @param p
 * @param nextState
 */
  public void setNextState(Process p, VegetativeType nextState) {
    state.setProcessNextState(p,nextState);
    changingNextState = false;
  }

  public boolean nonSpeciesMatch() { return (normalColor == NON_SPECIES_COLOR); }
/**
 * Calculates the center position (x,y).  
 * @return
 */
  public Point getCenterPosition() {
    return new Point((x + (width/2)), (y + (height/2)));
  }
/**
 * Sets the x,y position of the pathway shape from a parameter point.  
 * @param pt
 */
  public void setPosition(Point pt) {
    x = pt.x;
    y = pt.y;
  }
/**
 * Sets the x,y position of this pathway and the species position for this vegetative type.  This is stored in a hashtable keyed by species with 
 * value of position.
 * @param newX x coordinate of species
 * @param newY y coordinate of species
 * @param species the species to be set at a particular position.  
 */
  public void setPosition(int newX, int newY, Species species) {
    x = newX;
    y = newY;

    state.setSpeciesPosition(species, new Point(x,y));
  }
  /**
   * Checks if show label should be set.  
   * @param value
   */
  public void setShowLabel(boolean value) { showLabel = value; }
/**
 * Checks the fix position of a species if x or y coordinate is below 0.  
 * @param species
 */
  public void checkFixPosition(Species species) {
    if (x < 0 || y < 0) {
      if (x < 0) { x = 1; }
      if (y < 0) { y = 15; }
      setPosition(x,y,species);
    }
  }
/**
 * Sets the position for a particular species.  
 * @param species
 */
  public void setPosition(Species species) {
    if (species == state.getSpecies()) {
      normalColor = SPECIES_COLOR;
    }
    else {
      normalColor = NON_SPECIES_COLOR;
    }

    setPosition(state.getSpeciesPosition(species));
  }
/**
 * Method to calculate if parameter x,y coordinates are inside the x square representing a vegetative type.  
 * @param tmpX
 * @param tmpY
 * @return
 */
  public boolean isInside(int tmpX, int tmpY) {
    return (tmpX >= x && tmpX < (width + x) &&
            tmpY >= y && tmpY < (height + y));
  }

  public boolean isAtNode(int tmpX, int tmpY) {
    Point p = getCenterPosition();
    return (tmpX >= (p.x-5) && tmpX < (p.x+5) &&
            tmpY >= y       && tmpY < (y+10));
  }

  public void setChangingNextState() { changingNextState = true; }
  public boolean isChangingNextState() { return changingNextState; }

  public void select() {
    color = SELECTED_COLOR;
    selected = true;
  }
  public void unSelect() {
    if (changingNextState) { return; }

    color = normalColor;
    selected = false;
  }
/**
 * Paint method.  
 * @param g
 */
  public void paint(Graphics g) {
    Font old, f;

    if (selected) {
      g.setColor(TEXT_COLOR);
      old = g.getFont();
      f   = new Font("Dialog",Font.BOLD,16);
      g.setFont(f);
      g.drawString(getStateName(),x,y);
      g.setFont(old);
    }

    if (!selected && showLabel) {
      g.setColor(TEXT_COLOR);
      old = g.getFont();
      f   = new Font("Dialog",Font.BOLD,10);
      g.setFont(f);
      g.drawString(getStateName(),x+(int)(width*0.75),y);
      g.setFont(old);
    }

    g.setColor(color);
    g.fillRect(x,y,width,height);

    g.setColor(NODE_COLOR);
    Point p = getCenterPosition();
    g.fillRect((p.x-5),y,10,10);

    g.setColor(Color.black);
    g.drawRect(x,y,width,height);
  }
/**
 * Vegetative type toString containing species, size class, age, and density.
 * @return
 */
  public String getStateName() {
    return state.toString();
  }
  /**
   * Gets the next vegetative type state.  
   * @param p
   * @return
   */
  public VegetativeType getProcessNextState(Process p) {
  	return state.getProcessNextState(p);
  }

}
