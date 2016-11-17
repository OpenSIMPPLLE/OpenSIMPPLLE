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
 * A pathway canvas provides users with an interface for interacting with pathway shapes. The
 * pathway is displayed as a directed graph containing shapes from the current habitat type group.
 * Each arrow in the canvas represents a process. The originating state lies at the tail of the
 * arrow, and the resulting state lies at the arrow head. Users may manipulate shapes and grid
 * lines via mouse dragging and sub-menus.
 */

public class PathwayCanvas extends JPanel implements MouseListener, MouseMotionListener {

  /**
   * Maps vegetative type names to pathway shapes.
   */
  Hashtable<String,PathwayShape> shapes;

  /**
   * Maps textual descriptions of lines to pathway grid lines.
   */
  Hashtable<String,PathwayGridline> lines;

  /**
   * The selected pathway shape.
   */
  PathwayShape selectedShape;

  /**
   * The selected pathway shape whose next state arrow is being moved by a user.
   */
  PathwayShape changingShape;

  /**
   * The current end point of the arrow being moved by a user.
   */
  Point changingLineEnd;

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
   * A flag indicating if a state is being moved by a user.
   */
  boolean movingShape;

  /**
   * A flag indicating that the previous shapes dialog is open.
   */
  boolean prevDialogOpen;

  /**
   * A flag indicating that a state was selected with a double click.
   */
  boolean selectedDoubleClicked;

  /**
   * A flag indicating if labels are visible next to shapes.
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

    shapes = new Hashtable<>();
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
      shapes.clear();
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

    if (shapes.isEmpty()) { return; }

    // Draw Shapes
    for (PathwayShape shape : shapes.values()) {
      if (species != null) {
        shape.checkFixPosition(species);
      }
      shape.setShowLabel(showAllLabels);
      shape.paint(g);
    }

    // Draw Circles and Arrows
    for (String key : shapes.keySet()) {
      PathwayShape shape = shapes.get(key);
      if (shape.nonSpeciesMatch()) continue;
      VegetativeType nextState = shape.getNextState(process);
      if (nextState == null) continue;
      PathwayShape toShape = shapes.get(nextState.getPrintName());
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
    if (selectedShape != null) {
      selectedShape.paint(g);
    }

    // Draw Changing State Line
    if (changingShape != null) {
      changingShape.paint(g);
      Point from = changingShape.getCenterPoint();
      g.setColor(process.getColor());
      g.drawLine(from.x, from.y - 10, changingLineEnd.x, changingLineEnd.y);
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

  /**
   * Clears and re-creates pathway shapes and grid lines.
   */
  public void refreshDiagram() {
    refreshStates();
    refreshGridLines();
    revalidate();
  }

  /**
   * Clears existing pathway shapes and creates one shape per vegetative type.
   */
  private void refreshStates() {
    if (htGrp == null || species == null) { return; }
    Hashtable<String,VegetativeType> vegTypes = htGrp.findMatchingSpeciesTypes(species,process);
    shapes.clear();
    for (String key : vegTypes.keySet()) {
      VegetativeType vegType = vegTypes.get(key);
      PathwayShape shape = new PathwayShape(vegType);
      shape.setPosition(species);
      shapes.put(key,shape);
    }
  }

  /**
   * Clears existing grid lines and creates grid lines around related shapes.
   */
  private void refreshGridLines() {
    lines = htGrp.getLines(species, process.getType());
    if (lines == null) {
      Hashtable<String,PathwayShape> tmpStates = new Hashtable<>(shapes);
      // Note: collapseAll should probably return a hash table of collapsed shapes
      CollapsedPathwayShape.collapseAll(tmpStates);
      Rectangle[] rectangles = new Rectangle[tmpStates.size()];

      int counter = 0;
      for (String key : tmpStates.keySet()) {
        CollapsedPathwayShape shape = (CollapsedPathwayShape) tmpStates.get(key);
        rectangles[counter] = shape.getCorners();
        counter++;
      }
      lines = PathwayGridline.getLinesAroundRectangles(rectangles, this);
      htGrp.setLines(lines, species, process.getType());
    }
  }

  /**
   * Searches for invalid vegetative units and displays a warning dialog if any are found.
   */
  private void doInvalidAreaCheck() {
    Area area = Simpplle.getCurrentArea();
    if (area.existAnyInvalidVegUnits()) {
      String msg = "Invalid shapes were created as a result of deleting a state\n" +
                   "In addition any simulation data that may have existed has\n" +
                   "been erased from memory\n" +
                   "The area can be made valid again by either running the Unit Editor\n" +
                   "found under the Utilities menu of the main application window, or\n" +
                   "by recreating the state.\n";

      JOptionPane.showMessageDialog(this,
                                    msg,
                                    "Invalid units found",
                                    JOptionPane.INFORMATION_MESSAGE);

      JSimpplle.getSimpplleMain().markAreaInvalid();
    } else {
      JSimpplle.getSimpplleMain().markAreaValid();
    }
  }

  /**
   * An event handler that displays a pathway editor. The pathway editor allows a user to add,
   * remove, and modify the next state for a particular process. After closing the dialog, the
   * diagram is refreshed.
   *
   * @param e an action event
   */
  private void editShape(ActionEvent e) {
    PathwayEditor editor = new PathwayEditor(JSimpplle.getSimpplleMain(),
                                             "Pathway Editor",
                                             true,
                                             selectedShape.getState());
    editor.setVisible(true);
    getPathwayDlg().updateDialog();
    refreshDiagram();
  }

  /**
   * An event handler that deletes a shape. Deletion is prevented if the state is referenced by
   * other shapes or if the state is the last state in a habitat type group. When deletion is
   * allowed, the state is deleted, the diagram is refreshed, and invalid areas are checked.
   *
   * @param e an action event
   */
  private void deleteShape(ActionEvent e) {
    if (prevDialogOpen) { return; }

    Vector<VegetativeTypeNextState> previousStates = selectedShape.getState().findPreviousStates();
    VegetativeTypeNextState vegState = null;
    if (previousStates.size() == 1) {
      vegState = previousStates.elementAt(0);
    }

    if (previousStates.size() > 1 ||
        (previousStates.size() == 1 &&
         (selectedShape.getState() != vegState.getNextState()))) {
      String msg = "The selected shapes has previous shapes.\n" +
                   "It cannot be deleted until there are no previous shapes\n" +
                   "Please use the following dialog to assist in changing the\n" +
                   "previous shapes to point elsewhere.\n" +
                   "When finished try deleting again.";
      JOptionPane.showMessageDialog(this,
                                    msg,
                                    "Deletion not Allowed",
                                    JOptionPane.INFORMATION_MESSAGE);
      menuItemDeleteShape.setEnabled(false);
      displayPreviousStates(e);
    } else if (htGrp.getStatesCount() == 1) {
      String msg = "The selected state is the only one in this Ecological Grouping.\n" +
                   "Deletion is not allowed.\n" +
                   "The group must have at least one state.";
      JOptionPane.showMessageDialog(this,
                                    msg,
                                    "Previous shapes exist",
                                    JOptionPane.INFORMATION_MESSAGE);
    } else {
      String msg = "Delete the selected state?\n\n" + "Are you sure?";
      int result = JOptionPane.showConfirmDialog(this,msg,
                                                 "Delete Selected State",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (result == JOptionPane.YES_OPTION) {
        VegetativeType veg = selectedShape.getState();
        veg.getHtGrp().deleteVegetativeType(veg);
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

  /**
   * An event handler that displays a dialog for editing previous shapes.
   *
   * @param e an action event
   */
  public void displayPreviousStates(ActionEvent e) {
    if (prevDialogOpen) { return; }
    PathwayPrevStates dlg = new PathwayPrevStates(JSimpplle.getSimpplleMain(),
                                                  "Previous States",
                                                  false,
                                                  this,
                                                  selectedShape.getState(),
                                                  process);
    dlg.setVisible(true);
    prevDialogOpen = true;
    menuItemPrevStates.setEnabled(false);
  }

  public void editSpeciesChange(ActionEvent e) {
    VegetativeType state = selectedShape.getState();
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
    VegetativeType state = selectedShape.getState();
    String title =
      "Inclusion Rules Editor -- " +
      htGrp.toString() + " " + state.getPrintName();

    PathwayInclusionRulesEditDialog dlg =
      new PathwayInclusionRulesEditDialog(JSimpplle.getSimpplleMain(),title,true);
     dlg.initialize(selectedShape.getState(),htGrp);
    dlg.setVisible(true);
//    refreshDiagram();

  }

  public void collapseShape(ActionEvent e) {
    CollapsedPathwayShape.collapse(selectedShape, shapes);
    repaint();
  }

  public void uncollapseShape(ActionEvent e) {
    if(selectedShape instanceof CollapsedPathwayShape) {
      CollapsedPathwayShape curShape = (CollapsedPathwayShape) selectedShape;
      shapes.remove(curShape.getState().getCurrentState());
      shapes.putAll(curShape.getDetailedShapes());
      repaint();
    }
  }

  public void addLine(ActionEvent e, char dir) {
    PathwayGridline newLine = new PathwayGridline(dir, mouseClickPosition, this);
    lines.put(newLine.getKey(), newLine);
    repaint();
  }

  public void mouseClicked(MouseEvent e) {
    if (selectedShape != null && e.getClickCount() == 2 &&
        species != selectedShape.getState().getSpecies()) {
      selectedDoubleClicked = true;
      VegetativeType veg = selectedShape.getState();
      selectedShape.deselect();
      selectedShape = null;
      getPathwayDlg().setSpecies(veg);
      selectedDoubleClicked = false;
      return;
    }
  }

  public void mouseDragged(MouseEvent e) {
    boolean repaint = false;

    if (SwingUtilities.isLeftMouseButton(e) == false) { return; }

    if (selectedShape != null && selectedShape.isInsideNode(e.getX(),e.getY()) &&
        species == selectedShape.getState().getSpecies() &&
        movingShape == false) {
      changingShape = selectedShape;
      changingLineEnd = new Point(e.getX(),e.getY());
      selectedShape.setChangingNextState();
    }
    else if (selectedShape != null) {
      movingShape = true;
    }

    if (changingShape == null && selectedShape != null && movingShape == true &&
        (e.getX() > 0 && (e.getX() < (getWidth() - selectedShape.width))) &&
        (e.getY() > 15 && (e.getY() < (getHeight() - selectedShape.height)))) {
      selectedShape.setPosition(e.getX(),e.getY(),species);
      repaint = true;
    }

    if (changingShape != null) {
      mouseMoved(e);
      changingLineEnd.setLocation(e.getX(),e.getY());
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

    if (shapes.isEmpty() || menuOptions.isVisible() || selectedDoubleClicked) return;

    selectedShape = null;
    for (PathwayShape shape : shapes.values()) {
      if (shape.isInsideShape(e.getX(), e.getY())) {
        shape.select();
        if (selectedShape != null) {
          selectedShape.deselect();
        }
        selectedShape = shape;
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
    if (e.isPopupTrigger()) {
      mouseClickPosition = e.getPoint();
      if (selectedShape != null &&
          changingShape == null &&
          species == selectedShape.getState().getSpecies()) {
        if(selectedShape instanceof CollapsedPathwayShape) {
          menuUncollapse.show(e.getComponent(), e.getX(), e.getY());
        } else {
          menuOptions.show(e.getComponent(), e.getX(), e.getY());
        }
      } else {
        menuLine.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  public void mouseReleased(MouseEvent e) {

    if (!SwingUtilities.isLeftMouseButton(e)) return;

    if (selectedShape != null) {
      movingShape = false;
    }

    if (changingShape != null) {
      for (PathwayShape shape : shapes.values()) {
        if (shape.isInsideShape(e.getX(), e.getY())) {
          pathwayDlg.saveArrowChange(changingShape.getState(),
                                     process,
                                     changingShape.getNextState(process));
          changingShape.setNextState(process, shape.getState());
          refreshDiagram();  // get rid of unused non-species shapes.
          changingShape.deselect();
          changingShape = null;
          break;
        }
      }
      changingShape = null;
      repaint();
    }
  }
}

