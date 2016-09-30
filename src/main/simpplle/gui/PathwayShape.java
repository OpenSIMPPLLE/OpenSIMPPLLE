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

  public PathwayShape() {
    this(null,DEFAULT_SIZE,DEFAULT_SIZE);
  }

  public PathwayShape(VegetativeType state) {
    this(state,DEFAULT_SIZE,DEFAULT_SIZE);
  }

  public PathwayShape(VegetativeType state, int width, int height) {
    this.state       = state;
    this.x           = 10;
    this.y           = 10;
    this.width       = width;
    this.height      = height;
    this.normalColor = SPECIES_COLOR;
    this.color       = normalColor;
    this.selected    = false;
    this.showLabel   = false;
  }

  public Point getCenterPoint() {
    return new Point(x + (width / 2), y + (height / 2));
  }

  public boolean isChangingNextState() {
    return changingNextState;
  }

  public void setChangingNextState() {
    changingNextState = true;
  }

  public VegetativeType getProcessNextState(Process p) {
    return state.getProcessNextState(p);
  }

  public void setNextState(Process p, VegetativeType nextState) {
    state.setProcessNextState(p,nextState);
    changingNextState = false;
  }

  public void setPosition(Point point) {
    x = point.x;
    y = point.y;
  }

  public void setPosition(Species species) {
    if (species == state.getSpecies()) {
      normalColor = SPECIES_COLOR;
    } else {
      normalColor = NON_SPECIES_COLOR;
    }
    setPosition(state.getSpeciesPosition(species));
  }

  public void setPosition(int newX, int newY, Species species) {
    x = newX;
    y = newY;
    state.setSpeciesPosition(species, new Point(x,y));
  }

  public void setShowLabel(boolean value) {
    showLabel = value;
  }

  public VegetativeType getState() {
    return state;
  }

  public String getStateName() {
    return state.toString();
  }

  public boolean nonSpeciesMatch() {
    return normalColor == NON_SPECIES_COLOR;
  }

  public void checkFixPosition(Species species) {
    if (x < 0 || y < 0) {
      if (x < 0) { x = 1; }
      if (y < 0) { y = 15; }
      setPosition(x,y,species);
    }
  }

  public boolean isAtNode(int tmpX, int tmpY) {
    Point p = getCenterPoint();
    return (tmpX >= (p.x-5) && tmpX < (p.x+5) &&
        tmpY >= y       && tmpY < (y+10));
  }

  public boolean isInside(int tmpX, int tmpY) {
    return (tmpX >= x && tmpX < (width + x) &&
            tmpY >= y && tmpY < (height + y));
  }

  public void select() {
    color = SELECTED_COLOR;
    selected = true;
  }
  public void unSelect() {
    if (changingNextState) { return; }

    color = normalColor;
    selected = false;
  }

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
    Point p = getCenterPoint();
    g.fillRect((p.x-5),y,10,10);

    g.setColor(Color.black);
    g.drawRect(x,y,width,height);
  }
}
