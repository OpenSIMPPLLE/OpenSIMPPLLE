/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.border.*;

import simpplle.*;
import simpplle.comcode.*;
import java.beans.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 * This class creates an Existing Vegetative Unit analysis dialog.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class EvuAnalysis extends JDialog {

  simpplle.comcode.Area area;
  private simpplle.comcode.Evu currentEvu;
  private ArrayList<Evu> resultUnits;
  private int resultIndex;

  public static boolean isOpen = false;
  private  static EvuAnalysis thisInstance = null;

  private JTextField idEdit = new JTextField();
  private JTabbedPane historyTabbedPane = new JTabbedPane();
  private JCheckBox resultsOnlyCB = new JCheckBox();

  private JTextArea historyText = new JTextArea();
  private JTextArea treatmentText = new JTextArea();

  JButton prevPB = new JButton();
  JButton nextPB = new JButton();
  private JButton searchPB = new JButton();

  JPanel jPanel1 = new JPanel(new FlowLayout());
  JPanel mainPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  private JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
  private JPanel bottomPanel = new JPanel(new BorderLayout());
  private JPanel prevNextPanel = new JPanel();
  private JPanel idPanel = new JPanel();
  private JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
  private JPanel historyPanel = new JPanel(new BorderLayout());
  private JPanel treatmentPanel = new JPanel(new BorderLayout());
  private JPanel valuesPanel = new JPanel();
  private JPanel labelsPanel = new JPanel();
  private JPanel aquaticUnitsPanel = new JPanel(new BorderLayout());
  private JPanel panelCol1 = new JPanel();
  private JPanel panelCol2 = new JPanel();
  private JPanel valuesPanelCol2 = new JPanel();
  private JPanel labelsPanelCol2 = new JPanel();
  private JPanel adjacentUnitsPanel = new JPanel(new BorderLayout());
  private JPanel landformUnitsPanel = new JPanel(new BorderLayout());
  private JPanel topPanelNew = new JPanel();
  private JPanel adjacentPanel = new JPanel();

  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  FlowLayout flowLayout7 = new FlowLayout();

  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();

  BorderLayout borderLayout3 = new BorderLayout();


  Border border1;
  Border border2;
  private Border border3;
  private Border border4;
  Border border5;

  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  private TitledBorder titledBorder4;
  private TitledBorder attributesBorder;
  private TitledBorder adjacentUnitsBorder;

  private JList adjacentList = new JList();
  private JList eluList = new JList();
  private JList eauList = new JList();

  private JScrollPane adjacentScrollPane = new JScrollPane();
  private JScrollPane historyScrollPane = new JScrollPane();
  private JScrollPane eluScrollPane = new JScrollPane();
  private JScrollPane eauScrollPane = new JScrollPane();
  private JScrollPane treatmentScroll = new JScrollPane();

  private JLabel idLabel = new JLabel();
  private JLabel currentStateValue = new JLabel();
  private JLabel currentStateLabel = new JLabel();
  private JLabel htGrpValue = new JLabel();
  private JLabel htGrpLabel = new JLabel();
  private JLabel landtypeValue = new JLabel();
  private JLabel acresValue = new JLabel();
  private JLabel acresLabel = new JLabel();
  private JLabel fmzLabel = new JLabel();
  private JLabel specialAreaLabel = new JLabel();
  private JLabel roadStatusLabel = new JLabel();
  private JLabel trailStatusLabel = new JLabel();
  private JLabel ownerLabel = new JLabel();
  private JLabel unitNumLabel = new JLabel();
  private JLabel landtypeLabel = new JLabel();
  private JLabel fmzValue = new JLabel();
  private JLabel specialAreaValue = new JLabel();
  private JLabel roadStatusValue = new JLabel();
  private JLabel trailStatusValue = new JLabel();
  private JLabel ownershipValue = new JLabel();
  private JLabel unitNumValue = new JLabel();

  private final JLabel elevationLabel = new JLabel();
  private final JLabel elevationValue = new JLabel();
  private final JLabel dummyLabel = new JLabel();
  private final JLabel dummyValue = new JLabel();
  private final JLabel rowValue = new JLabel();
  private final JLabel rowLabel = new JLabel();
  private final JLabel columnLabel = new JLabel();
  private final JLabel columnValue = new JLabel();
  
  /**
   * Primary constructor for Evu Analysis dialog.  Sets the frame owner, name, and modality.  
   * @param frame
   * @param title
   * @param modal
   */
  public EvuAnalysis(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    isOpen = true;
    thisInstance = this;
    if (Beans.isDesignTime() == false) {
      initialize();
    }
  }
  /**
   * Overloaded constructor of Evu Analysis.  Creates a new frame, sets the name to null and modality to false
   */
  public EvuAnalysis() {
    this(new Frame(), "", false);
  }
  /**
   * Init method for Evu Analysis dialog.  Sets the borders, lists, layouts, panels, buttons and listeners.
   * @throws Exception
   */
  void jbInit() throws Exception {

    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createEmptyBorder();
    attributesBorder = new TitledBorder(border1,"Attributes");
    border2 = BorderFactory.createEmptyBorder();
    adjacentUnitsBorder = new TitledBorder(border2,"Adjacent Units");
    border3 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(border3,"Adjacent Units");
    border4 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    border5 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(border4,"Landform Units");
    titledBorder4 = new TitledBorder(border5,"Aqua Units");
    mainPanel.setLayout(new BorderLayout());
    eauList.setToolTipText("Double click to go to a unit");
    eauList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    eauList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        eauList_mouseClicked(e);
      }
    });
    getContentPane().add(mainPanel);
    topPanelNew.setLayout(new BorderLayout());
    prevNextPanel.setLayout(flowLayout1);
    prevPB.setEnabled(false);
    prevPB.setNextFocusableComponent(nextPB);
    prevPB.setIcon(new ImageIcon(simpplle.gui.EvuAnalysis.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.EvuAnalysis.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    nextPB.setEnabled(false);
    nextPB.setNextFocusableComponent(adjacentList);
    nextPB.setIcon(new ImageIcon(simpplle.gui.EvuAnalysis.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.EvuAnalysis.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    idPanel.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    idEdit.setToolTipText("Please enter a valid Unit ID");
    idEdit.setColumns(6);
    idEdit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        idEdit_actionPerformed(e);
      }
    });
    this.setTitle("Vegetative Unit Analysis");
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    currentStateValue.setFont(new java.awt.Font("Dialog", 1, 14));
    currentStateValue.setForeground(Color.blue);
    currentStateValue.setText("DF-LP/MMU/3");
    currentStateLabel.setText("Current State");
    currentStateLabel.setFont(new Font("", Font.PLAIN, 14));
    htGrpValue.setFont(new java.awt.Font("Dialog", 1, 14));
    htGrpValue.setForeground(Color.blue);
    htGrpValue.setText("A2");
    htGrpLabel.setText("Ecological Grouping");
    htGrpLabel.setFont(new Font("", Font.PLAIN, 14));
    centerPanel.setLayout(borderLayout3);
    landtypeValue.setFont(new java.awt.Font("Dialog", 1, 14));
    landtypeValue.setForeground(Color.blue);
    landtypeValue.setText("No Idea");
    acresValue.setFont(new java.awt.Font("Dialog", 1, 14));
    acresValue.setForeground(Color.blue);
    acresValue.setText("1000000");
    acresLabel.setText("Acres");
    acresLabel.setFont(new Font("", Font.PLAIN, 14));
    adjacentPanel.setLayout(flowLayout7);
    adjacentList.setBackground(Color.white);
    adjacentList.setFont(new java.awt.Font("Monospaced", 0, 12));
    adjacentList.setToolTipText("Double click to go to a unit");
    adjacentList.setSelectionBackground(Color.blue);
    adjacentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    adjacentList.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        adjacentList_mouseClicked(e);
      }
    });
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout7.setVgap(0);
    historyText.setColumns(130);
    historyText.setRows(12);
    historyText.setBackground(Color.white);
    historyText.setSelectionColor(Color.blue);
    historyText.setEditable(false);
    adjacentScrollPane.setMinimumSize(new Dimension(100, 24));
    adjacentScrollPane.setPreferredSize(new Dimension(200, 132));
    eluScrollPane.setMinimumSize(new Dimension(100, 24));
    eluScrollPane.setPreferredSize(new Dimension(200, 132));
    historyTabbedPane.setFont(new java.awt.Font("Dialog", 1, 16));
    treatmentText.setColumns(92);
    treatmentText.setRows(12);
    treatmentText.setBackground(Color.white);
    treatmentText.setSelectionColor(Color.blue);
    treatmentText.setEditable(false);
    borderLayout3.setVgap(5);
    bottomPanel.setBorder(BorderFactory.createEtchedBorder());
    labelsPanel.setLayout(gridLayout2);
    valuesPanel.setLayout(gridLayout2);
    fmzLabel.setText("Fire Management Zone");
    fmzLabel.setFont(new Font("", Font.PLAIN, 14));
    specialAreaLabel.setText("Special Area");
    specialAreaLabel.setFont(new Font("", Font.PLAIN, 14));
    roadStatusLabel.setText("Road Status");
    roadStatusLabel.setFont(new Font("", Font.PLAIN, 14));
    trailStatusLabel.setText("Trail Status");
    trailStatusLabel.setFont(new Font("", Font.PLAIN, 14));
    ownerLabel.setText("Ownership");
    ownerLabel.setFont(new Font("", Font.PLAIN, 14));
    unitNumLabel.setText("Unit Number");
    unitNumLabel.setFont(new Font("", Font.PLAIN, 14));
    landtypeLabel.setText("Associated Landtype");
    landtypeLabel.setFont(new Font("", Font.PLAIN, 14));
    fmzValue.setFont(new java.awt.Font("Dialog", 1, 14));
    fmzValue.setForeground(Color.blue);
    fmzValue.setText("4");
    specialAreaValue.setFont(new java.awt.Font("Dialog", 1, 14));
    specialAreaValue.setForeground(Color.blue);
    specialAreaValue.setText("anything");
    roadStatusValue.setFont(new java.awt.Font("Dialog", 1, 14));
    roadStatusValue.setForeground(Color.blue);
    roadStatusValue.setText("Open-Roaded");
    trailStatusValue.setFont(new java.awt.Font("Dialog", 1, 14));
    trailStatusValue.setForeground(Color.blue);
    trailStatusValue.setText("OPEN");
    ownershipValue.setFont(new java.awt.Font("Dialog", 1, 14));
    ownershipValue.setForeground(Color.blue);
    ownershipValue.setText("NF-OTHER");
    unitNumValue.setFont(new java.awt.Font("Dialog", 1, 14));
    unitNumValue.setForeground(Color.blue);
    panelCol2.setLayout(flowLayout4);
    labelsPanelCol2.setLayout(gridLayout2);
    gridLayout2.setRows(7);
    gridLayout2.setVgap(5);
    valuesPanelCol2.setLayout(gridLayout2);
    panelCol1.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    flowLayout5.setVgap(0);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout4.setVgap(0);
    panelCol1.setBorder(BorderFactory.createEtchedBorder());
    panelCol2.setBorder(BorderFactory.createEtchedBorder());
    infoPanel.setBorder(attributesBorder);
    attributesBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 14));
    adjacentUnitsBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 14));
    flowLayout1.setVgap(1);
    resultsOnlyCB.setEnabled(false);
    resultsOnlyCB.setText("Result Units Only -->");
    resultsOnlyCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resultsOnlyCB_actionPerformed(e);
      }
    });
    searchPB.setText("Search");
    searchPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchPB_actionPerformed(e);
      }
    });
    eluList.setToolTipText("Double click to go to a unit");
    eluList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    eluList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        eluList_mouseClicked(e);
      }
    });
    adjacentUnitsPanel.setBorder(titledBorder2);
    landformUnitsPanel.setBorder(titledBorder3);
    aquaticUnitsPanel.setBorder(titledBorder4);
    mainPanel.add(topPanelNew, BorderLayout.NORTH);
    topPanelNew.add(topPanel, BorderLayout.NORTH);
    topPanel.add(jPanel1, null);
    jPanel1.add(searchPB, null);
    jPanel1.add(resultsOnlyCB, null);
    topPanel.add(prevNextPanel, null);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(idPanel, null);
    idPanel.add(idLabel, null);
    idPanel.add(idEdit, null);
    prevNextPanel.add(nextPB, null);
    topPanelNew.add(centerPanel, BorderLayout.CENTER);
    centerPanel.add(infoPanel, BorderLayout.NORTH);
    infoPanel.add(panelCol1, null);
    infoPanel.add(panelCol2, null);
    labelsPanel.add(currentStateLabel, null);
    labelsPanel.add(htGrpLabel, null);
    labelsPanel.add(acresLabel, null);
    labelsPanel.add(fmzLabel, null);
    labelsPanel.add(trailStatusLabel,null);
    labelsPanelCol2.add(roadStatusLabel, null);
    labelsPanelCol2.add(ownerLabel, null);
    labelsPanelCol2.add(specialAreaLabel, null);
    labelsPanelCol2.add(unitNumLabel, null);
    labelsPanelCol2.add(landtypeLabel, null);
    valuesPanel.add(currentStateValue, null);
    valuesPanel.add(htGrpValue, null);
    valuesPanel.add(acresValue, null);
    valuesPanel.add(fmzValue, null);
    valuesPanel.add(trailStatusValue,null);
    valuesPanelCol2.add(roadStatusValue, null);
    valuesPanelCol2.add(ownershipValue, null);
    valuesPanelCol2.add(specialAreaValue, null);
    valuesPanelCol2.add(unitNumValue, null);
    valuesPanelCol2.add(landtypeValue, null);
    centerPanel.add(adjacentPanel, BorderLayout.SOUTH);
    adjacentPanel.add(adjacentUnitsPanel, null);
    adjacentUnitsPanel.add(adjacentScrollPane, BorderLayout.CENTER);
    adjacentScrollPane.getViewport().add(adjacentList, null);
    adjacentPanel.add(landformUnitsPanel, null);
    adjacentPanel.add(aquaticUnitsPanel);
    aquaticUnitsPanel.add(eauScrollPane, java.awt.BorderLayout.CENTER);
    eauScrollPane.getViewport().add(eauList);
    landformUnitsPanel.add(eluScrollPane,  BorderLayout.CENTER);
    eluScrollPane.getViewport().add(eluList, null);
    mainPanel.add(bottomPanel, BorderLayout.CENTER);
    bottomPanel.add(historyTabbedPane, BorderLayout.CENTER);
    historyTabbedPane.add(historyPanel, "History");
    historyPanel.add(historyScrollPane, BorderLayout.CENTER);
    historyTabbedPane.add(treatmentPanel, "Treatment History");
    treatmentPanel.add(treatmentScroll, BorderLayout.CENTER);
    treatmentScroll.getViewport().add(treatmentText, null);
    historyScrollPane.getViewport().add(historyText, null);
    panelCol2.add(labelsPanelCol2, null);
    
    labelsPanelCol2.add(rowLabel);
    rowLabel.setFont(new Font("", Font.PLAIN, 14));
    rowLabel.setText("Row");
    
    labelsPanelCol2.add(columnLabel);
    columnLabel.setFont(new Font("", Font.PLAIN, 14));
    columnLabel.setText("Column");
    panelCol2.add(valuesPanelCol2, null);
    
    valuesPanelCol2.add(rowValue);
    rowValue.setForeground(Color.BLUE);
    rowValue.setFont(new Font("", Font.BOLD, 14));
    rowValue.setText("1");
    
    valuesPanelCol2.add(columnValue);
    columnValue.setForeground(Color.BLUE);
    columnValue.setFont(new Font("", Font.BOLD, 14));
    columnValue.setText("1");
    panelCol1.add(labelsPanel, null);
    
    labelsPanel.add(elevationLabel);
    elevationLabel.setFont(new Font("", Font.PLAIN, 14));
    elevationLabel.setText("Elevation");
    
    labelsPanel.add(dummyLabel);
    panelCol1.add(valuesPanel, null);
    
    valuesPanel.add(elevationValue);
    elevationValue.setForeground(Color.BLUE);
    elevationValue.setFont(new Font("", Font.BOLD, 14));
    elevationValue.setText("1000");
    
    valuesPanel.add(dummyValue);
    dummyValue.setForeground(Color.BLUE);
    dummyValue.setFont(new Font("", Font.BOLD, 14));
  }

  private void initialize() {
    eluList.setModel(new DefaultListModel());
    eauList.setModel(new DefaultListModel());

    resultUnits = null;
    resultsOnlyCB.setEnabled(false);
    area       = simpplle.comcode.Simpplle.getCurrentArea();
    currentEvu = area.getFirstEvu();

    // Make sure dialog will fit screen.
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlg = getPreferredSize();
    if (dlg.height > (screen.height-125)) {
      historyText.setRows(7);
      treatmentText.setRows(7);
    }

    historyText.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
    treatmentText.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
    setSize(getPreferredSize());
    updateDialog();
  }
  /**
   * Gets the current instance of Evu Analysis
   * @return current Evu analysis
   */
  public static EvuAnalysis getInstance() { return thisInstance; }
  /**
   * Updates the Evu Analysis dialog.  If there is a current Evu instance enables the previous and next buttons as well as adjacent Evu list.
   * Displays the id, edit id, current state, habitat group, land type, fire management zone, trail status, elevation, road status, ownership, special area
   * and current Evu ID text.  Also sets the default list model  to elu list.  If there is no current Evu analysis dialog, sets all the above text to empty string
   * and previous, next buttons to enabled.
   */
  private void updateDialog() {
    if (currentEvu != null) {
      prevPB.setEnabled(true);
      nextPB.setEnabled(true);
      adjacentList.setEnabled(true);

      String id = Integer.toString(currentEvu.getId());
      idLabel.setText(id);
      idEdit.setText(id);

      VegSimStateData state = currentEvu.getState();
      String stateStr = (state != null) ? state.getVeg().toString() : "Unknown";
      currentStateValue.setText(stateStr);
      htGrpValue.setText(currentEvu.getHabitatTypeGroup().toString());
      landtypeValue.setText(currentEvu.getLandtype());

      fmzValue.setText(currentEvu.getFmz().getName());
      trailStatusValue.setText(currentEvu.getTrailStatus().toString());
      
      elevationValue.setText(Integer.toString(currentEvu.getElevation()));
      
      roadStatusValue.setText(currentEvu.getRoadStatusNew().toString());
      ownershipValue.setText(currentEvu.getOwnership());
      specialAreaValue.setText(currentEvu.getSpecialArea());
      unitNumValue.setText(currentEvu.getUnitNumber());
      
      rowValue.setText(Integer.toString(currentEvu.getLocationY()));
      columnValue.setText(Integer.toString(currentEvu.getLocationX()));

      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(0);  // Don't show fractional part.
      acresValue.setText(nf.format(currentEvu.getFloatAcres()));

      adjacentList.setListData(currentEvu.getAdjAnalysisDisplay());

      {
        DefaultListModel model = (DefaultListModel) eluList.getModel();
        model.removeAllElements();
        if (currentEvu.getAssociatedLandUnits() != null) {
          for (ExistingLandUnit landUnit : currentEvu.getAssociatedLandUnits()) {
            model.addElement(landUnit);
          }
      //        eluList.setListData(currentEvu.getAssociatedLandUnits().toArray());
        }
      }

      {
        DefaultListModel model = (DefaultListModel) eauList.getModel();
        model.removeAllElements();
        if (currentEvu.getAssociatedAquaticUnits() != null) {
          for (ExistingAquaticUnit aquaUnit : currentEvu.getAssociatedAquaticUnits()) {
            model.addElement(aquaUnit);
          }
        }
      }

      try {
        historyText.setText(currentEvu.getHistory());
      }
      catch (SimpplleError ex) {
        historyText.setText("Problems creating history");
      }
      treatmentText.setText(currentEvu.getTreatmentHistory());

      // Scroll back to the top.
      historyText.setCaretPosition(0);
      treatmentText.setCaretPosition(0);

      // This is necessary in order to get the scroll bar to appear.
      historyText.invalidate();
      treatmentText.invalidate();
      historyText.validate();
      treatmentText.validate();
    }
    else {
      prevPB.setEnabled(false);
      nextPB.setEnabled(false);
      adjacentList.setEnabled(false);

      idLabel.setText("");
      idEdit.setText("");
      currentStateValue.setText("");
      htGrpValue.setText("");
      landtypeValue.setText("");
      acresValue.setText("");
      historyText.setText("");
      treatmentText.setText("");
      elevationValue.setText("");
      rowValue.setText("");
      columnValue.setText("");
    }
  }
  /**
   * Sets the adjacent Evu via mouse click.
   * @param e double mouse click
   */
  private void adjacentList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goAdjacent();
    }
  }
  /**
   * Sets the Elu via mouse click.
   * @param e double mouse click
   */
  private void eluList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goLandUnit();
    }
  }
  /**
   * Sets the Eau via mouse click.
   * @param e double mouse click
   */
  private void eauList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goAquaUnit();
    }
  }
  /**
   * Changes the Evu analysis dialog to the adjacent Evu.  Uses the selected adjacent Evu from the adjacent Evu list - and sets the current Evu to this adjacent Evu.
   */
  private void goAdjacent() {
    String              str = (String) adjacentList.getSelectedValue();
    int                 id = -1;
    StringTokenizerPlus strTok;

    if (str == null) { return; }

    strTok = new StringTokenizerPlus(str," ");
    try {
      id = strTok.getIntToken();
    }
    catch (simpplle.comcode.ParseError e) {}

    if (id == -1) { return; }

    currentEvu = area.getEvu(id);
    updateDialog();
  }
  /**
   * Sets the Evu Analysis dialog to a particular Unit Analysis.  IF a unit analysis dialog is open gets the instance of that dialog.  Else creates a
   * unit analysis instance and sets the land unit to the selected value in Elu list.
   */
  private void goLandUnit() {
    UnitAnalysis dlg;

    if (UnitAnalysis.isOpen()) {
      dlg = UnitAnalysis.getInstance();

    }
    else {
      dlg = new UnitAnalysis(JSimpplle.getSimpplleMain(),"Unit Analysis", false);
    }
    dlg.getLandInstance().goLandUnit((ExistingLandUnit)eluList.getSelectedValue());
    dlg.setVisible(true);
  }
  /**
   * Sets the Eau Analysis dialog to a particular Eau Analysis dialog.  If an Eau analysis dialog is open gets the instance of that dialog.  Else creates a 
   * Eau analysis instance and sets the land unit to the selected value in Elu list.  
   */
  private void goAquaUnit() {
    EauAnalysis dlg;

    if (EauAnalysis.isOpen()) {
      dlg = EauAnalysis.getInstance();

    }
    else {
      dlg = new EauAnalysis(JSimpplle.getSimpplleMain(),"Aquatic Unit Analysis", false);
    }
    dlg.goAquaticUnit((ExistingAquaticUnit)eauList.getSelectedValue());
    dlg.setVisible(true);
  }
  /**
   * Sets the Evu analysis to the Unit indicated by Evu ID parameter.
   * @param id the evu ID
   */
  void goUnit(int id) {
    goUnit(area.getEvu(id));
  }
  /**
   * Sets the Evu analysis to the Unit indicated by Evu parameter.  
   * @param evu - the Existing vegetative unit the Evu analysis will go to.  
   */
  void goUnit(Evu evu) {
    currentEvu = evu;
    updateDialog();
  }
  /**
   * If results only combo box selected, sets the current Evu to previous unit in result units.  Otherwise sets the current evu to previous Evu.  
   * @param e 'previous'
   */
  void prevPB_actionPerformed(ActionEvent e) {
    if (resultsOnlyCB.isSelected()) {
      if (resultIndex == 0) { resultIndex = resultUnits.size() - 1;  }
      else { resultIndex--; }
      currentEvu = (Evu)resultUnits.get(resultIndex);
    }
    else {
      currentEvu = area.getPrevEvu(currentEvu);
    }
    updateDialog();
  }
  /**
   * If results only combo box selected, sets the current Evu to next unit in result units.  Otherwise sets the current Evu to next Evu.  
   * @param e 'next'
   */
  void nextPB_actionPerformed(ActionEvent e) {
    if (resultsOnlyCB.isSelected()) {
      if (resultIndex == resultUnits.size() - 1) { resultIndex = 0;  }
      else { resultIndex++; }
      currentEvu = (Evu)resultUnits.get(resultIndex);
    }
    else {
      currentEvu = area.getNextEvu(currentEvu);
    }
    updateDialog();
  }
  /**
   * If edit is choosen and area Evu is valid in the area, current area is set.
   * @param e 'edit'
   */
  private void idEdit_actionPerformed(ActionEvent e) {
    int id;

    try {
      id = Integer.parseInt(idEdit.getText());
      if (area.isValidUnitId(id) == false) {
        throw new NumberFormatException();
      }

      currentEvu = area.getEvu(id);
      updateDialog();
    }
    catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(this,idEdit.getText() +
                                    " is not a value unit id.",
                                    "Invalid Unit Id",
                                    JOptionPane.ERROR_MESSAGE);
      if (currentEvu != null) {
        idEdit.setText(Integer.toString(currentEvu.getId()));
      }
      else {
        idEdit.setText("");
      }
    }
  }
  /**
   * Exits the Evu Analysis dialog.
   */
  private void quit() {
    isOpen = false;
    setVisible(false);
    dispose();
  }
  /**
   * Exits the Evu Analysis if a window closing event occurs.
   * @param e 'X' for window closing event.
   */
  void this_windowClosing(WindowEvent e) {
    quit();
  }
  /**
   * If search instance is already open, returns.  Otherwise if search button pushed starts a new instance of Evu Search dialog.
   * @param e
   */
  private void searchPB_actionPerformed(ActionEvent e) {
    if (EvuSearchLogicDlg.isOpen()) { return; }

    EvuSearchLogicDlg dlg = new EvuSearchLogicDlg(JSimpplle.getSimpplleMain(),
                                                  "Unit Attribute Search",false,this);

    dlg.setVisible(true);
  }
  /**
   * Sets the results units arraylist to the Evu arraylist.
   * @param units
   */
  void setResultUnits(ArrayList<Evu> units) {
    if (units == null || units.size() == 0) { return; }

    resultUnits = units;
    resultsOnlyCB.setEnabled(true);
    if (resultsOnlyCB.isSelected()) {
      currentEvu = resultUnits.get(0);
      resultIndex = 0;
      updateDialog();
    }
  }
  /**
   * If results only combo box is selected, current evu is set the results unit at index 0.  Otherwise current Evu is the first Evu in area.
   * @param e
   */
  private void resultsOnlyCB_actionPerformed(ActionEvent e) {
    if (resultsOnlyCB.isSelected()) {
      currentEvu = resultUnits.get(0);
      resultIndex = 0;
    }
    else {
      currentEvu = area.getFirstEvu();
    }
    updateDialog();
  }
  /**
   * Check if Evu analysis dialog is open.
   * @return true if Evu analysis dialog is open.
   */
  public static boolean isOpen() { return isOpen; }
}
