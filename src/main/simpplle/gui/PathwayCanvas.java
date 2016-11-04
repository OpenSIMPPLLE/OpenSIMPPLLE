/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import simpplle.JSimpplle;
import simpplle.comcode.*;
import simpplle.comcode.Process;
import java.awt.geom.Point2D;

/**
 * This is the My Canvas class which has methods for drawing for the pathways seen in OpenSimpplle.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public class PathwayCanvas extends JPanel implements MouseListener, MouseMotionListener {

  /**
   * Maps vegetative type names to pathway shapes.
   */
  Hashtable<String,PathwayShape> states;

  /**
   * Maps textual descriptions of lines to pathway grid lines.
   */
  Hashtable<String,PathwayGridline> lines;

  /**
   * The selected pathway shape.
   */
  PathwayShape selectedState;

  /**
   *
   */
  PathwayShape changingState;

  /**
   *
   */
  Point changingStateLineEnd;

  /**
   * The current process.
   */
  Process process;

  /**
   * The current habitat type group.
   */
  HabitatTypeGroup htGrp;

  /**
   * The current species.
   */
  Species species;

  /**
   *
   */
  boolean movingShape;

  /**
   *
   */
  boolean prevDialogOpen;

  /**
   *
   */
  boolean selectedDoubleClicked;

  /**
   * A flag indicating if labels are visible.
   */
  boolean showAllLabels;

  /**
   * A flag indicating if grid lines are visible.
   */
  boolean showGridLines;

  /**
   * The preferred size of the panel.
   */
  Dimension preferredSize = new Dimension(3000, 3525);

  /**
   * The pathway dialog that owns this panel.
   */
  Pathway pathwayDlg;

  /**
   * The name of the currently selected line.
   */
  String selectedLine;

  /**
   * The position of the last mouse click.
   */
  Point mouseClickPosition;

  /**
   * The background color of the canvas.
   */
  private static final Color CANVAS_COLOR = new Color(212, 208, 200);

  private JPopupMenu menuOptions;
  private JPopupMenu menuLine;
  private JPopupMenu menuUncollapse;

  private JMenuItem menuItemEdit;
  private JMenuItem menuItemPrevStates;
  private JMenuItem menuItemDeleteShape;
  private JMenuItem menuItemSpeciesChange;
  private JMenuItem menuItemInclusionRules;
  private JMenuItem menuItemCollapse;
  private JMenuItem menuItemAddVertical;
  private JMenuItem menuItemAddHorizontal;
  private JMenuItem menuItemDetails;

  /**
   * Creates a canvas for drawing pathways.
   */
  public PathwayCanvas() {

    super();

    states                = new Hashtable<>();
    lines                 = new Hashtable<>();
    process               = Process.findInstance(ProcessType.SUCCESSION);
    movingShape           = false;
    prevDialogOpen        = false;
    selectedDoubleClicked = false;
    showAllLabels         = false;
    showGridLines         = false;

    menuItemEdit = new JMenuItem("Edit");
    menuItemEdit.addActionListener(this::editShape);

    menuItemPrevStates = new JMenuItem("Previous States");
    menuItemPrevStates.addActionListener(this::displayPreviousStates);

    menuItemDeleteShape = new JMenuItem("Delete...");
    menuItemDeleteShape.addActionListener(this::deleteShape);

    menuItemSpeciesChange = new JMenuItem("Species Change");
    menuItemSpeciesChange.addActionListener(this::editSpeciesChange);

    menuItemInclusionRules = new JMenuItem("Inclusion Rules");
    menuItemInclusionRules.addActionListener(this::editInclusionRules);

    menuItemCollapse = new JMenuItem("Collapse");
    menuItemCollapse.addActionListener(this::collapseShape);

    menuOptions = new JPopupMenu("Options");
    menuOptions.add(menuItemEdit);
    menuOptions.add(menuItemPrevStates);
    menuOptions.add(menuItemDeleteShape);
    menuOptions.add(menuItemSpeciesChange);
    menuOptions.add(menuItemInclusionRules);
    menuOptions.add(menuItemCollapse);

    menuItemAddVertical = new JMenuItem("Add Vertical Line");
    menuItemAddVertical.addActionListener(event -> addLine(event, PathwayGridline.VERTICAL_DIR));

    menuItemAddHorizontal = new JMenuItem("Add Horizontal Line");
    menuItemAddHorizontal.addActionListener(event -> addLine(event, PathwayGridline.HORIZONTAL_DIR));

    menuLine = new JPopupMenu("Line Management");
    menuLine.add(menuItemAddVertical);
    menuLine.add(menuItemAddHorizontal);

    menuItemDetails = new JMenuItem("Details");
    menuItemDetails.addActionListener(this::uncollapseShape);

    menuUncollapse = new JPopupMenu("Uncollapse");
    menuUncollapse.add(menuItemDetails);

    addMouseListener(this);
    addMouseMotionListener(this);

    setBackground(CANVAS_COLOR);

  }

  public void setHtGrp(HabitatTypeGroup group) {
    if (group != null) {
      htGrp = group;
      species = null;
      states.clear();
    }
  }

  public void setHtGrp(String name) {
    if (name != null) {
      setHtGrp(HabitatTypeGroup.findInstance(name));
    }
  }

  public String getHtGrp() {
    return htGrp.toString();
  }

  public void setPathwayDlg(Pathway pathway) {
    pathwayDlg = pathway;
  }

  public Pathway getPathwayDlg() {
    return pathwayDlg;
  }

  public void setProcess(String name) {
    if (name != null) {
      process = Process.findInstance(name);
      refreshDiagram();
    }
  }

  public String getProcess() {
    return process.toString();
  }

  public void setSpecies(Species species) {
    if (species != null) {
      this.species = species;
      setProcess("SUCCESSION");
    }
  }

  public Species getSpecies() {
    return species;
  }

  public void setPrevDialogClosed() {
    prevDialogOpen = false;
    menuItemPrevStates.setEnabled(true);
    menuItemDeleteShape.setEnabled(true);
  }

  public void toggleShowAllLabels() {
    showAllLabels = !showAllLabels;
  }

  @Override
  public Dimension getPreferredSize() {
    return preferredSize;
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    if (states.isEmpty()) { return; }

    // Draw Shapes
    for (PathwayShape shape : states.values()) {
      if (species != null) {
        shape.checkFixPosition(species);
      }
      shape.setShowLabel(showAllLabels);
      shape.paint(g);
    }

    // Draw Circles and Arrows
    for (String key : states.keySet()) {
      PathwayShape shape = states.get(key);
      if (shape.nonSpeciesMatch()) continue;
      VegetativeType nextState = shape.getNextState(process);
      if (nextState == null) continue;
      PathwayShape toShape = states.get(nextState.getPrintName());
      if (toShape == null) continue;
      Point from = shape.getCenterPoint();
      Point to = toShape.getCenterPoint();
      g.setColor(process.getColor());
      if (key.equals(nextState.getPrintName())) {
        g.drawOval(from.x - 15, from.y - 10, 45, 45);
      } else {
        g.fillOval(from.x - 1, from.y - 11, 3, 3);
        g.drawLine(from.x, from.y - 10, to.x, to.y);
        drawArrowhead((Graphics2D)g, from.x, from.y - 10, to.x, to.y);
      }
    }

    // Draw Selection Above Lines
    if (selectedState != null) {
      selectedState.paint(g);
    }

    // Draw Changing State Line
    if (changingState != null) {
      changingState.paint(g);
      Point from = changingState.getCenterPoint();
      g.setColor(process.getColor());
      g.drawLine(from.x, from.y - 10, changingStateLineEnd.x, changingStateLineEnd.y);
    }

    // Draw Grid Lines
    if (showGridLines) {
      for (PathwayGridline line : lines.values()) {
        line.paint(g);
      }
    }
  }

  /**
   * Draws an arrow on the head of a line.
   *
   * @param g the graphics context to paint to
   * @param x1 the x-coordinate of the tail
   * @param y1 the y-coordinate of the tail
   * @param x2 the x-coordinate of the head
   * @param y2 the y-coordinate of the head
   */
  private void drawArrowhead(Graphics2D g, double x1, double y1, double x2, double y2) {

    // Length of arrow head
    double al = 7;

    // Width of arrow head
    double aw = 4;

    // Compute difference between components
    double dx = x2 - x1;
    double dy = y2 - y1;

    // Compute length of line
    double length = Math.sqrt(dx * dx + dy * dy);

    // Compute normalized line vector
    double x = dx / length;
    double y = dy / length;

    // Compute points for arrow head
    Point2D tip = new Point2D.Double(x2, y2);
    Point2D base = new Point2D.Double(x2 - x * al, y2 - y * al);
    Point2D back_top = new Point2D.Double(base.getX() - aw * y, base.getY() + aw * x);
    Point2D back_bottom = new Point2D.Double(base.getX() + aw * y, base.getY() - aw * x);

    // Create and fill polygon
    Polygon p = new Polygon();
    p.addPoint((int) tip.getX(), (int) tip.getY());
    p.addPoint((int) back_bottom.getX(), (int) back_bottom.getY());
    p.addPoint((int) back_top.getX(), (int) back_top.getY());
    g.fillPolygon(p);

  }

  public void refreshDiagram() {
    if (htGrp == null || species == null) { return; }

    Hashtable    vegTypes;
    PathwayShape shape;
    String       key;
    VegetativeType vt;

    vegTypes = htGrp.findMatchingSpeciesTypes(species,process);
    states.clear();

    Enumeration e = vegTypes.keys();
    while (e.hasMoreElements()) {
      key   = (String) e.nextElement();
      vt    = (VegetativeType) vegTypes.get(key);
      shape = new PathwayShape((VegetativeType)vegTypes.get(key));
      shape.setPosition(species);
      states.put(key,shape);
    }

    refreshGridLines();

    revalidate();
  }

  // Identifies related states and draw lines around
  private void refreshGridLines() {
    lines = htGrp.getLines(species, process.getType());
    if (lines == null) {
      Hashtable tempTable = new Hashtable(states);
      CollapsedPathwayShape.collapseAll(tempTable);
      Rectangle[] rectangles = new Rectangle[tempTable.size()];
      Enumeration e = tempTable.keys();
      String key;
      CollapsedPathwayShape shape;
      int counter = 0;

      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        shape = (CollapsedPathwayShape) tempTable.get(key);
        rectangles[counter] = shape.getCorners();
        counter++;
      }
      lines = PathwayGridline.getLinesAroundRectangles(rectangles, this);
      htGrp.setLines(lines, species, process.getType());
    }
  }

  private void doInvalidAreaCheck() {
    Area area = Simpplle.getCurrentArea();

    if (area.existAnyInvalidVegUnits()) {
      String msg =
          "Invalid states were created as a result of deleting a state\n" +
              "In addition any simulation data that may have existed has\n" +
              "been erased from memory\n" +
              "The area can be made valid again by either running the Unit Editor\n" +
              "found under the Utilities menu of the main application window, or\n" +
              "by recreating the state.\n";

      JOptionPane.showMessageDialog(this,msg,"Invalid units found",
          JOptionPane.INFORMATION_MESSAGE);
      JSimpplle.getSimpplleMain().markAreaInvalid();
    } else {
      JSimpplle.getSimpplleMain().markAreaValid();
    }
  }

  private void editShape(ActionEvent e) {
    PathwayEditor editor = new PathwayEditor(JSimpplle.getSimpplleMain(),
                                             "Pathway Editor",
                                             true,
                                             selectedState.getState());
    editor.setVisible(true);
    getPathwayDlg().updateDialog();
    refreshDiagram();
  }

  private void deleteShape(ActionEvent e) {
    if (prevDialogOpen) { return; }

    Vector v = selectedState.getState().findPreviousStates();
    VegetativeTypeNextState vegState = null;
    if (v.size() == 1) {
      vegState = (VegetativeTypeNextState) v.elementAt(0);
    }

    if (v.size() > 1 ||
        (v.size() == 1 && (selectedState.getState() != vegState.getNextState()))) {
      String msg = "The selected states has previous states.\n" +
          "It cannot be deleted until there are no previous states\n" +
          "Please use the following dialog to assist in changing the\n" +
          "previous states to point elsewhere.n" +
          "When finished try deleting again.";

      JOptionPane.showMessageDialog(this,msg,"Deletion not Allowed",
          JOptionPane.INFORMATION_MESSAGE);
      menuItemDeleteShape.setEnabled(false);
      displayPreviousStates(e);
    } else if (htGrp.getStatesCount() == 1) {
      String msg = "The selected state is the only one in this Ecological Grouping\n" +
          "Deletion is not allowed.\n" +
          "The Group must have at least one state.";
      JOptionPane.showMessageDialog(this,msg,"Previous states exist",
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      String msg = "Delete the selected state?\n\n" + "Are you sure?";
      int result = JOptionPane.showConfirmDialog(this,msg,
          "Delete Selected State",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      if (result == JOptionPane.YES_OPTION) {
        VegetativeType veg = selectedState.getState();
        veg.getHtGrp().deleteVegetativeType(veg);
        veg = null;
        getPathwayDlg().updateDialog();
        refreshDiagram();
        // Update the units and check for invalid ones.
        Area area = Simpplle.getCurrentArea();
        if (area != null) {
          area.updatePathwayData();
          doInvalidAreaCheck();
        }
      }
    }
  }

  public void displayPreviousStates(ActionEvent e) {
    if (prevDialogOpen) { return; }
    PathwayPrevStates dlg = new PathwayPrevStates(JSimpplle.getSimpplleMain(),
                                                  "Previous States",
                                                  false,
                                                  this,
                                                  selectedState.getState(),
                                                  process);
    dlg.setVisible(true);
    prevDialogOpen = true;
    menuItemPrevStates.setEnabled(false);
  }

  public void editSpeciesChange(ActionEvent e) {
    VegetativeType state = selectedState.getState();
    String title =
      "Species Change Editor -- " +
      htGrp.toString() + " " + state.getPrintName();

    PathwaySpeciesChangeEditDialog dlg =
      new PathwaySpeciesChangeEditDialog(JSimpplle.getSimpplleMain(),title,true);
     dlg.initialize(state,htGrp);
    dlg.setVisible(true);
//    refreshDiagram();

  }

  public void editInclusionRules(ActionEvent e) {
    VegetativeType state = selectedState.getState();
    String title =
      "Inclusion Rules Editor -- " +
      htGrp.toString() + " " + state.getPrintName();

    PathwayInclusionRulesEditDialog dlg =
      new PathwayInclusionRulesEditDialog(JSimpplle.getSimpplleMain(),title,true);
     dlg.initialize(selectedState.getState(),htGrp);
    dlg.setVisible(true);
//    refreshDiagram();

  }

  public void collapseShape(ActionEvent e) {
    CollapsedPathwayShape.collapse(selectedState, states);
    repaint();
  }

  public void uncollapseShape(ActionEvent e) {
    if(selectedState instanceof CollapsedPathwayShape) {
      CollapsedPathwayShape curShape = (CollapsedPathwayShape)selectedState;
      states.remove(curShape.getState().getCurrentState());
      states.putAll(curShape.getDetailedShapes());
      repaint();
    }
  }

  public void addLine(ActionEvent e, char dir) {
    PathwayGridline newLine = new PathwayGridline(dir, mouseClickPosition, this);
    lines.put(newLine.getKey(), newLine);
    repaint();
  }

  public void displayPopupMenu(MouseEvent e) {
    if (e.isPopupTrigger()) {

      mouseClickPosition = e.getPoint();

      if (selectedState != null &&
          changingState == null &&
          species == selectedState.getState().getSpecies()) {

        if(selectedState instanceof CollapsedPathwayShape)
          menuUncollapse.show(e.getComponent(),e.getX(),e.getY());
        else
          menuOptions.show(e.getComponent(),e.getX(),e.getY());
      } else {
        menuLine.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void mouseClicked(MouseEvent e) {
    if (selectedState != null && e.getClickCount() == 2 &&
        species != selectedState.getState().getSpecies()) {
      selectedDoubleClicked = true;
      VegetativeType veg = selectedState.getState();
      selectedState.deselect();
      selectedState = null;
      getPathwayDlg().setSpecies(veg);
      selectedDoubleClicked = false;
      return;
    }

    displayPopupMenu(e);
  }

  public void mouseDragged(MouseEvent e) {
    boolean repaint = false;

    if (SwingUtilities.isLeftMouseButton(e) == false) { return; }

    if (selectedState != null && selectedState.isInsideNode(e.getX(),e.getY()) &&
        species == selectedState.getState().getSpecies() &&
        movingShape == false) {
      changingState = selectedState;
      changingStateLineEnd = new Point(e.getX(),e.getY());
      selectedState.setChangingNextState();
    }
    else if (selectedState != null) {
      movingShape = true;
    }

    if (changingState == null && selectedState != null && movingShape == true &&
        (e.getX() > 0 && (e.getX() < (getWidth() - selectedState.width))) &&
        (e.getY() > 15 && (e.getY() < (getHeight() - selectedState.height)))) {
      selectedState.setPosition(e.getX(),e.getY(),species);
      repaint = true;
    }

    if (changingState != null) {
      mouseMoved(e);
      changingStateLineEnd.setLocation(e.getX(),e.getY());
    }

    if(selectedLine!=null) {
      PathwayGridline curLine = (PathwayGridline)lines.get(selectedLine);
      lines.remove(selectedLine);
      selectedLine = curLine.moveLine(e.getPoint());
      if(selectedLine!=null) {
        lines.put(selectedLine, curLine);
      }
      repaint = true;
    }

    if (repaint) {
      repaint();
    }
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mouseMoved(MouseEvent e) {

    if (states.isEmpty() || menuOptions.isVisible() || selectedDoubleClicked) return;

    selectedState = null;
    for (PathwayShape shape : states.values()) {
      if (shape.isInsideShape(e.getX(), e.getY())) {
        shape.select();
        if (selectedState != null) {
          selectedState.deselect();
        }
        selectedState = shape;
      } else {
        shape.deselect();
      }
    }

    selectedLine = null;
    PathwayGridline searchLine = PathwayGridline.grabLine(lines, PathwayGridline.HORIZONTAL_DIR, e.getPoint());
    if (searchLine == null) {
      searchLine = PathwayGridline.grabLine(lines, PathwayGridline.VERTICAL_DIR, e.getPoint());
    }
    if (searchLine != null) {
      selectedLine = searchLine.getKey();
      setCursor(searchLine.getSelectedCursor());
    } else {
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    repaint();

  }

  public void mousePressed(MouseEvent e) {
    displayPopupMenu(e);
  }

  public void mouseReleased(MouseEvent e) {

    displayPopupMenu(e);

    if (!SwingUtilities.isLeftMouseButton(e)) return;

    if (selectedState != null) {
      movingShape = false;
    }

    if (changingState != null) {
      for (PathwayShape shape : states.values()) {
        if (shape.isInsideShape(e.getX(), e.getY())) {
          pathwayDlg.saveArrowChange(changingState.getState(),
                                     process,
                                     changingState.getNextState(process));
          changingState.setNextState(process, shape.getState());
          refreshDiagram();  // get rid of unused non-species states.
          changingState.deselect();
          changingState = null;
          break;
        }
      }
      changingState = null;
      repaint();
    }
  }
}

