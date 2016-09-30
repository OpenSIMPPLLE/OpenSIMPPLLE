/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import simpplle.comcode.Process;
import simpplle.comcode.Species;
import simpplle.comcode.VegetativeType;

/**
 * A pathway shape is a visual representation of a vegetative type in a pathway. Each pathway shape
 * is drawn as a rectangle which can be selected to display the name of the state. At the top of
 * each rectangle is a small 'node' box. Clicking and dragging from the node box allows a user to
 * connect the shape to the next state in the pathway.
 */

public class PathwayShape {

  private static final int DEFAULT_SIZE = 30;

  private static final Color NODE_COLOR        = Color.blue;
  private static final Color TEXT_COLOR        = Color.black;
  private static final Color SELECTED_COLOR    = Color.orange;
  private static final Color SPECIES_COLOR     = Color.yellow;
  private static final Color NON_SPECIES_COLOR = new Color(160,170,160);

  /**
   * The vegetative type that this shape represents
   */
  VegetativeType state;

  /**
   * The width of the rectangle
   */
  public int width;

  /**
   * The height of the rectangle
   */
  public int height;

  /**
   * The horizontal position of the rectangle
   */
  protected int x;

  /**
   * The vertical position of the rectangle
   */
  protected int y;

  /**
   * The current color of the shape
   */
  protected Color color;

  /**
   * The color of the shape when it is not selected
   */
  protected Color normalColor;

  /**
   * A flag indicating if the shape is selected
   */
  boolean selected;

  /**
   * A flag indicating that the user is connecting the node to a new next state
   */
  private boolean changingNextState;

  /**
   * A flag indicating if the label is drawn even when deselected
   */
  private boolean showLabel;

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
    showLabel       = false;
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
