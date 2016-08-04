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

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
  Hashtable        states;
  PathwayShape     selectedState, changingState;
  Point            changingStateLineEnd;
  RegionalZone     zone;
  Process          process;
  HabitatTypeGroup htGrp;
  Species          species;
  boolean          movingShape;
  boolean          isPopupVisible = false;
  boolean          prevDialogOpen = false;
  boolean          selectedDoubleClicked = false;
  boolean          showAllLabels=false;
  boolean	   showGridLines=false;
  Dimension        preferredSize = new Dimension(3000,3525);

  JPopupMenu       popupMenu = new JPopupMenu("Options");
  JMenuItem        edit = new JMenuItem("Edit");
  JMenuItem        prevStates = new JMenuItem("Previous States");
  JMenuItem        menuPopupDelete = new JMenuItem("Delete...");
  JMenuItem        menuPopupRepFiaPlots = new JMenuItem("Representative FIA Plots");
  JMenuItem        menuPopupPictures = new JMenuItem("Pictures");
        JMenuItem				 menuPopupCollapse = new JMenuItem("Collapse");
        JPopupMenu			 popupMenu3 = new JPopupMenu("Uncollapse");
        JMenuItem				 menuPopupUncollapse = new JMenuItem("Details");
  JPopupMenu			 popupMenu2 = new JPopupMenu("Line Management");
  JMenuItem				 menuAddVerticalLine = new JMenuItem("Add Vertical Line");
  JMenuItem				 menuAddHorizontalLine = new JMenuItem("Add Horizontal Line");
  Pathway          pathwayDlg;
  Hashtable				 allLines = new Hashtable();
  Hashtable				 lines = new Hashtable();
  String					 selectedLine;
  Point						 mouseClickPosition;
  JMenuItem menuPopupSpeciesChange = new JMenuItem("Species Change");
  JMenuItem menuPopupInclusionRules = new JMenuItem("Inclusion Rules");

  private static final Color CANVAS_COLOR   = new Color(212,208,200);
  private static final Color LINE_COLOR     = Color.green;
  private static final Color LINE_END_COLOR = Color.red;
/**
 * Constructor for MyCanvas.  Sets the JMenus, mouse listeners, colors, and variables.  
 */
  public MyCanvas() {
    super();
//    this.pathwayDlg = pathwayDlg;

    showAllLabels = false;
    states      = new Hashtable(10);
    movingShape = false;
    zone        = Simpplle.getCurrentZone();
    if (zone != null) {
      process = Process.findInstance(ProcessType.SUCCESSION);
    }

    addMouseListener(this);
    addMouseMotionListener(this);
    setBackground(CANVAS_COLOR);

    // Setup the right-click menu;
    edit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editShape(e);
      }
    });
    popupMenu.add(edit);

    prevStates.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevStates(e);
      }
    });
    popupMenu.add(prevStates);

    menuPopupDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPopupDelete(e);
      }
    });
    popupMenu.add(menuPopupDelete);

    menuPopupRepFiaPlots.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPopupRepFiaPlots(e);
      }
    });
    popupMenu.add(menuPopupRepFiaPlots);
    menuPopupRepFiaPlots.setEnabled(false);


    menuPopupSpeciesChange.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPopupSpeciesChange(e);
      }
    });
    popupMenu.add(menuPopupSpeciesChange);

    menuPopupInclusionRules.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPopupInclusionRules(e);
      }
    });
    popupMenu.add(menuPopupInclusionRules);

    menuPopupPictures.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuPopupPictures(e);
      }
    });
    popupMenu.add(menuPopupPictures);
    menuPopupPictures.setEnabled(false);

                menuPopupCollapse.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                                menuPopupCollapse(e);
                  }
                });
                popupMenu.add(menuPopupCollapse);

                menuPopupUncollapse.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                                menuPopupUncollapse(e);
                  }
                });
                popupMenu3.add(menuPopupUncollapse);

                menuAddVerticalLine.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                                menuAddLine(e, PathwayGridline.VERTICAL_DIR);
                  }
                });
                popupMenu2.add(menuAddVerticalLine);

                menuAddHorizontalLine.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                                menuAddLine(e, PathwayGridline.HORIZONTAL_DIR);
                  }
                });
                popupMenu2.add(menuAddHorizontalLine);
  }
/**
 * Method to switch between showing all labels and not showing all labels.  
 */
  public void toggleShowAllLabels() { showAllLabels = !showAllLabels; }
/**
 * Sets the file pathway dialog to parameter Pathway dlg
 * @param dlg
 */
  public void setPathwayDlg(Pathway dlg) { pathwayDlg = dlg; }
  public Pathway getPathwayDlg() { return pathwayDlg; }

  public void editShape(ActionEvent e) {
    PathwayEditor dlg = new PathwayEditor(JSimpplle.getSimpplleMain(),
                                          "Pathway Editor", true,
                                          selectedState.getState());
    dlg.setVisible(true);
    getPathwayDlg().updateDialog();
    refreshDiagram();
  }
/**
 * Sets the prevDialogOpen boolean to false, and enables the previous states 
 */
  public void setPrevDialogClosed() {
    prevDialogOpen = false;
    prevStates.setEnabled(true);
    menuPopupDelete.setEnabled(true);
  }

  public void prevStates(ActionEvent e) {
    if (prevDialogOpen) { return; }

    PathwayPrevStates dlg = new PathwayPrevStates(JSimpplle.getSimpplleMain(),
                                                  "Previous States", false,
                                                  this,selectedState.getState(),
                                                  process);
    dlg.setVisible(true);
    prevDialogOpen = true;
    prevStates.setEnabled(false);
  }

  public void menuPopupDelete(ActionEvent e) {
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
      menuPopupDelete.setEnabled(false);
      prevStates(e);
    }
    else if (htGrp.getStatesCount() == 1) {
      String msg = "The selected state is the only one in this Ecological Grouping\n" +
                   "Deletion is not allowed.\n" +
                   "The Group must have at least one state.";
      JOptionPane.showMessageDialog(this,msg,"Previous states exist",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    else {
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
    }
    else {
      JSimpplle.getSimpplleMain().markAreaValid();
    }
  }

  public void menuPopupRepFiaPlots(ActionEvent e) {
  }

  public void menuPopupSpeciesChange(ActionEvent e) {
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
  public void menuPopupInclusionRules(ActionEvent e) {
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


  public void menuPopupPictures(ActionEvent e) {
  }

        public void menuPopupCollapse(ActionEvent e) {
                CollapsedPathwayShape.collapse(selectedState, states);
                repaint();
        }

        public void menuPopupUncollapse(ActionEvent e) {
                if(selectedState instanceof CollapsedPathwayShape)
                {
                        CollapsedPathwayShape curShape = (CollapsedPathwayShape)selectedState;
                        states.remove(curShape.getState().getCurrentState());
                        states.putAll(curShape.getDetailedShapes());
                        repaint();
                }
        }

  public void menuAddLine(ActionEvent e, char dir) {
        PathwayGridline newLine = new PathwayGridline(dir, mouseClickPosition, this);
        lines.put(newLine.getKey(), newLine);
        repaint();
  }

  public Dimension getPreferredSize() {
    return preferredSize;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (states.isEmpty()) { return; }

    PathwayShape   shape;
    String         key;
    Enumeration    e = states.keys();
    while (e.hasMoreElements()) {
      key   = (String) e.nextElement();
      shape = (PathwayShape) states.get(key);
      if (species != null) { shape.checkFixPosition(species); }
      shape.setShowLabel(showAllLabels);
      shape.paint(g);
    }

    VegetativeType nextState;
    Point          from, to;
//    Graphics2D     g2 = (Graphics2D)g;
//    Polygon triangle = new Polygon();

    e = states.keys();
    while (e.hasMoreElements()) {
      key   = (String) e.nextElement();
      shape = (PathwayShape) states.get(key);
      if (shape.nonSpeciesMatch()) { continue; }

      nextState = shape.getProcessNextState(process);
      if (nextState == null) { continue; }
      from      = shape.getCenterPosition();
      shape     = (PathwayShape) states.get(nextState.toString());
                if (shape == null) { continue; }
      to = shape.getCenterPosition();
      g.setColor(process.getColor());
      if (key.equals(nextState.toString())) {
        g.drawOval((from.x-15),from.y-10,45,45);
      }
      else {
        g.fillOval(from.x-1,from.y-11,3,3);
        g.drawLine(from.x,from.y-10,to.x,to.y);
        drawArrowhead((Graphics2D)g,from.x,from.y-10,to.x,to.y);
      }
//      g.setColor(LINE_END_COLOR);
//      g.fillOval(to.x,to.y,4,4);
    }
    // make sure selected state is on top.
    if (selectedState != null) {
      selectedState.paint(g);
    }

    if (changingState != null) {
      changingState.paint(g);
      from = changingState.getCenterPosition();
//      g.setColor(LINE_END_COLOR);
      g.drawLine(from.x,from.y-10,changingStateLineEnd.x,changingStateLineEnd.y);
//      drawArrowhead((Graphics2D)g,from.x,from.y-10,changingStateLineEnd.x,changingStateLineEnd.y);
    }

    if (showGridLines) {
      e = lines.keys();
      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        ((PathwayGridline) lines.get(key)).paint(g);
      }
    }

  }

  private void drawArrowhead(Graphics2D g, double x1, double y1, double x2,
                             double y2) {
    double al = 7;
    double aw = 4;
    double x, y, length;
    Point2D start = new Point2D.Double(x1, y1);
    Point2D end = new Point2D.Double(x2, y2);
    Point2D base = new Point2D.Double();
    Point2D back_top = new Point2D.Double();
    Point2D back_bottom = new Point2D.Double();

    //   Compute length of line
    length = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

    //   Compute normalized line vector
    x = (x2 - x1) / length;
    y = (y2 - y1) / length;

    //   Compute points for arrow head
    base.setLocation(x2 - x * al, y2 - y * al);
    back_top.setLocation(base.getX() - aw * y, base.getY() + aw * x);
    back_bottom.setLocation(base.getX() + aw * y, base.getY() - aw * x);

    Polygon p = new Polygon();
    p.addPoint((int) end.getX(), (int) end.getY());
    p.addPoint((int) back_bottom.getX(), (int) back_bottom.getY());
    p.addPoint((int) back_top.getX(), (int) back_top.getY());
    g.fillPolygon(p);
    p = null;
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
  public void refreshGridLines() {
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

  public String getProcess() { return process.toString(); }
  public void setProcess(String processName) {
    if (processName == null || processName.length() == 0) { return; }

    process = Process.findInstance(processName);
    refreshDiagram();
  }

  public Species getSpecies() { return species; }
  public void setSpecies(Species newSpecies) {
    if (newSpecies == null) { return; }

    species = newSpecies;
    setProcess("SUCCESSION");
    //refreshDiagram();
  }

  public String getHtGrp() { return htGrp.toString(); }
  public void setHtGrp(String newHtGrp) {
    if (newHtGrp == null || newHtGrp.length() == 0) { return; }

    setHtGrp(HabitatTypeGroup.findInstance(newHtGrp));
  }

  public void setHtGrp(HabitatTypeGroup newHtGrp) {
    if (newHtGrp == null) { return; }

    htGrp = newHtGrp;
    species = null;
    states.clear();
  }

  public void mouseDragged(MouseEvent e) {
        boolean repaint = false;

    if (SwingUtilities.isLeftMouseButton(e) == false) { return; }

    if (selectedState != null && selectedState.isAtNode(e.getX(),e.getY()) &&
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

                if(selectedLine!=null)
                {
                        PathwayGridline curLine = (PathwayGridline)lines.get(selectedLine);
                        lines.remove(selectedLine);
                        selectedLine = curLine.moveLine(e.getPoint());
                        if(selectedLine!=null)
                                lines.put(selectedLine, curLine);
                        repaint = true;
                }

                if(repaint)
                        repaint();
  }

  public void mouseMoved(MouseEvent e) {
    if (states.isEmpty() || popupMenu.isVisible() || selectedDoubleClicked) { return; }

    int x = e.getX();
    int y = e.getY();

    PathwayShape shape;
    String       key;
    Enumeration  keys = states.keys();
    selectedState = null;

    while (keys.hasMoreElements()) {
      key = (String) keys.nextElement();
      shape = (PathwayShape) states.get(key);
      if (shape.isInside(x,y)) {
        shape.select();
        if (selectedState != null) { selectedState.unSelect(); }
        selectedState = shape;
      }
      else {
        shape.unSelect();
      }
    }

    selectedLine = null;
    PathwayGridline searchLine = PathwayGridline.grabLine(lines,
        PathwayGridline.HORIZONTAL_DIR, e.getPoint());
    if (searchLine == null)
      searchLine = PathwayGridline.grabLine(lines, PathwayGridline.VERTICAL_DIR,
                                            e.getPoint());
    if (searchLine != null) {
      selectedLine = searchLine.getKey();
      setCursor(searchLine.getSelectedCursor());
    }
    else
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    repaint();
  }

  public void maybePopupMenu(MouseEvent e) {
    if (e.isPopupTrigger()) {
                        mouseClickPosition = e.getPoint();
      if(selectedState != null && changingState == null && species == selectedState.getState().getSpecies())
      {
                                if(selectedState instanceof CollapsedPathwayShape)
                                        popupMenu3.show(e.getComponent(),e.getX(),e.getY());
                                else
                                        popupMenu.show(e.getComponent(),e.getX(),e.getY());
      }
      else
        popupMenu2.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  public void mousePressed(MouseEvent e) {
    maybePopupMenu(e);
  }

  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {
    if (selectedState != null && e.getClickCount() == 2 &&
        species != selectedState.getState().getSpecies()) {
      selectedDoubleClicked = true;
      VegetativeType veg = selectedState.getState();
      selectedState.unSelect();
      selectedState = null;
      getPathwayDlg().setSpecies(veg);
      selectedDoubleClicked = false;
      return;
    }

    maybePopupMenu(e);
  }
  public void mouseReleased(MouseEvent e) {
    maybePopupMenu(e);

    if (SwingUtilities.isLeftMouseButton(e) == false) { return; }
    if (selectedState != null) { movingShape = false; }
    if (changingState == null) { return; }

    Enumeration  keys = states.keys();
    String       key;
    PathwayShape shape;
    int          x = e.getX(), y = e.getY();

    while (keys.hasMoreElements()) {
      key   = (String) keys.nextElement();
      shape = (PathwayShape) states.get(key);
      if (shape.isInside(x,y)) {
        pathwayDlg.saveArrowChange(changingState.getState(), process,
                                   changingState.getProcessNextState(process));
        changingState.setNextState(process,shape.getState());
        refreshDiagram();  // get rid of unused non-species states.
        changingState.unSelect();
        changingState = null;
        break;
      }
    }
    changingState = null;
    repaint();
  }
}
