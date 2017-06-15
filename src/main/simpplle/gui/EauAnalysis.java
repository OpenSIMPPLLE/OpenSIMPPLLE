/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import simpplle.JSimpplle;
import simpplle.comcode.Area;
import simpplle.comcode.ExistingAquaticUnit;
import simpplle.comcode.StringTokenizerPlus;
import java.beans.*;
import java.awt.Font;

/**
 * This class deals with the Existing Aquatic Unit analyis.
 *
 * @author Documentation by Brian Losi
 * <p>Original soiurce code authorship: Kirk A. Moeller
 */

public class EauAnalysis extends JDialog {

  Area area;
  private ExistingAquaticUnit currentEau;
  static EauAnalysis instance;
  public static boolean isOpen = false;
  private static final String protoCellValue = "123456         ";

  private JTextField idEdit = new JTextField();
  private JTabbedPane historyTabbedPane = new JTabbedPane();
  private JCheckBox resultsOnlyCB = new JCheckBox();
  private JScrollPane treatmentScroll = new JScrollPane();

  private JTextArea treatmentText = new JTextArea();
  private JTextArea historyText = new JTextArea();

  JButton prevPB = new JButton();
  JButton nextPB = new JButton();
  private JButton searchPB = new JButton();

  JPanel jPanel1 = new JPanel();
  JPanel mainPanel = new JPanel();
  JPanel centerPanel = new JPanel();
  private JPanel topPanel = new JPanel();
  private JPanel bottomPanel = new JPanel();
  private JPanel prevNextPanel = new JPanel();
  private JPanel idPanel = new JPanel();
  private JPanel infoPanel = new JPanel();
  private JPanel neighborsPanel = new JPanel();
  private JPanel historyPanel = new JPanel();
  private JPanel treatmentPanel = new JPanel();
  private JPanel valuesPanel = new JPanel();
  private JPanel labelsPanel = new JPanel();
  private JPanel vegUnitPanel = new JPanel();
  private JPanel panelCol1 = new JPanel();
  private JPanel panelCol2 = new JPanel();
  private JPanel labelsPanelCol2 = new JPanel();
  private JPanel valuesPanelCol2 = new JPanel();
  private JPanel adjVegPanel = new JPanel();
  private JPanel uplandVegPanel = new JPanel();
  private JPanel aquaticUnitPanel = new JPanel();
  private JPanel predPanel = new JPanel();
  private JPanel succPanel = new JPanel();

  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  FlowLayout flowLayout6 = new FlowLayout();
  FlowLayout flowLayout7 = new FlowLayout();
  FlowLayout flowLayout8 = new FlowLayout();
  FlowLayout flowLayout9 = new FlowLayout();
  FlowLayout flowLayout10 = new FlowLayout();

  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();
  GridLayout gridLayout3 = new GridLayout();
  private GridLayout gridLayout4 = new GridLayout();
  private GridLayout gridLayout5 = new GridLayout();

  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  private BorderLayout borderLayout7 = new BorderLayout();

  private BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

  Border border1;
  Border border2;
  private Border border3;
  private Border border4;
  Border border5;
  Border border6;
  private Border border8;

  private TitledBorder attributesBorder;
  TitledBorder titledBorder1;
  private TitledBorder adjacentUnitsBorder;
  private TitledBorder predBorder;
  private TitledBorder succBorder;
  private TitledBorder uplandBorder;
  private TitledBorder adjVegBorder;
  private TitledBorder aquaticUnitBorder;
  private TitledBorder vegUnitBorder;

  private JList adjVegList = new JList();
  private JList uplandVegList = new JList();
  private JList successorsList = new JList();
  private JList predecessorList = new JList();

  private JScrollPane uplandVegScrollPane = new JScrollPane();
  private JScrollPane succScrollPane = new JScrollPane();
  private JScrollPane predScrollPane = new JScrollPane();
  private JScrollPane adjVegScrollPane = new JScrollPane();
  private JScrollPane historyScrollPane = new JScrollPane();

  private JLabel idLabel = new JLabel();
  private JLabel currentStateValue = new JLabel();
  private JLabel currentStateLabel = new JLabel();
  private JLabel groupValue = new JLabel();
  private JLabel groupLabel = new JLabel();
  private JLabel lengthLabel = new JLabel();
  private JLabel segmentNumLabel = new JLabel();
  private JLabel lengthValue = new JLabel();
  private JLabel statusText = new JLabel();
  private JLabel statusLabel = new JLabel();
  private JLabel segmentNumValue = new JLabel();

  /**
   * Constructor for EauAnalysis.  Sets the frame owner name and modality.
   * @param frame
   * @param title
   * @param modal
   */
  public EauAnalysis(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    isOpen = true;
    instance = this;
    if (Beans.isDesignTime() == false) {
      initialize();
    }
  }

  public EauAnalysis() {
    this(new Frame(), "", false);
  }
  /**
   * Sets the borders buttons, text of buttons, and layouts and panels for Eau Analysis
   * @throws Exception
   */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createEmptyBorder();
    attributesBorder = new TitledBorder(border1,"Attributes");
    border2 = BorderFactory.createEmptyBorder();
    adjacentUnitsBorder = new TitledBorder(border2,"Adjacent Units");
    border3 = BorderFactory.createEmptyBorder();
    predBorder = new TitledBorder(border3,"Predecessors");
    border4 = BorderFactory.createEmptyBorder();
    succBorder = new TitledBorder(border4,"Successors");
    border5 = BorderFactory.createEmptyBorder();
    uplandBorder = new TitledBorder(border5,"Upland Veg");
    border6 = BorderFactory.createEmptyBorder();
    adjVegBorder = new TitledBorder(border6,"Adjacent Veg");
    aquaticUnitBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Aquatic Units");
    border8 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    vegUnitBorder = new TitledBorder(border8,"Vegetative Units");
    mainPanel.setLayout(boxLayout);
    topPanel.setLayout(flowLayout6);
    prevNextPanel.setLayout(flowLayout1);
    prevPB.setEnabled(false);
    prevPB.setNextFocusableComponent(nextPB);
    prevPB.setIcon(new ImageIcon(EauAnalysis.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(EauAnalysis.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    nextPB.setEnabled(false);
    nextPB.setIcon(new ImageIcon(EauAnalysis.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(EauAnalysis.class.getResource("images/nextg.gif")));
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
    this.setTitle("Aquatic Unit Analysis");
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    infoPanel.setLayout(flowLayout2);
    currentStateValue.setFont(new java.awt.Font("Dialog", 1, 14));
    currentStateValue.setForeground(Color.blue);
    currentStateValue.setText("A3-REF");
    currentStateLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    currentStateLabel.setText("Current State");
    groupValue.setFont(new java.awt.Font("Dialog", 1, 14));
    groupValue.setForeground(Color.blue);
    groupValue.setText("12-FMA");
    groupLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    groupLabel.setText("LTA Valley Segment Group");
    centerPanel.setLayout(borderLayout3);
    neighborsPanel.setLayout(flowLayout7);
    bottomPanel.setLayout(flowLayout3);
    historyPanel.setLayout(flowLayout8);
    historyText.setColumns(92);
    historyText.setRows(12);
    historyText.setBackground(Color.white);
    historyText.setSelectionColor(Color.blue);
    historyText.setEditable(false);
    historyTabbedPane.setFont(new java.awt.Font("Dialog", 1, 16));
    treatmentPanel.setLayout(flowLayout9);
    treatmentText.setColumns(92);
    treatmentText.setRows(12);
    treatmentText.setBackground(Color.white);
    treatmentText.setSelectionColor(Color.blue);
    treatmentText.setEditable(false);
    flowLayout9.setHgap(0);
    flowLayout9.setVgap(0);
    flowLayout8.setHgap(0);
    flowLayout8.setVgap(0);
    borderLayout3.setVgap(5);
    bottomPanel.setBorder(BorderFactory.createEtchedBorder());
    labelsPanel.setLayout(gridLayout4);
    valuesPanel.setLayout(gridLayout5);
    gridLayout4.setRows(2);
    gridLayout4.setVgap(5);
    gridLayout5.setRows(2);
    gridLayout5.setVgap(5);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(10);
    flowLayout2.setVgap(0);
    lengthLabel.setText("Length");
    segmentNumLabel.setText("Segment Number");
    lengthValue.setFont(new java.awt.Font("Dialog", 1, 14));
    lengthValue.setForeground(Color.blue);
    lengthValue.setText("350");
    segmentNumValue.setFont(new java.awt.Font("Dialog", 1, 14));
    segmentNumValue.setForeground(Color.blue);
    segmentNumValue.setText("2");
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    panelCol2.setLayout(flowLayout4);
    labelsPanelCol2.setLayout(gridLayout2);
    gridLayout2.setRows(3);
    gridLayout2.setVgap(5);
    valuesPanelCol2.setLayout(gridLayout3);
    gridLayout3.setRows(3);
    gridLayout3.setVgap(5);
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
    jPanel1.setLayout(flowLayout10);
    resultsOnlyCB.setEnabled(false);
    resultsOnlyCB.setText("Result Units Only -->");
    resultsOnlyCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resultsOnlyCB_actionPerformed(e);
      }
    });
    searchPB.setEnabled(false);
    searchPB.setText("Search");
    searchPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchPB_actionPerformed(e);
      }
    });
    flowLayout6.setAlignment(FlowLayout.LEFT);
    predBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    succBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    uplandBorder.setTitle("Upland");
    uplandBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    adjVegBorder.setTitle("Adjacent");
    adjVegBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    vegUnitPanel.setLayout(borderLayout6);
    adjVegPanel.setLayout(borderLayout5);
    adjVegPanel.setBorder(adjVegBorder);
    adjVegList.setFont(new java.awt.Font("Monospaced", 0, 12));
    adjVegList.setToolTipText("Double click to go to a unit");
    adjVegList.setPrototypeCellValue(protoCellValue);
    adjVegList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    adjVegList.setVisibleRowCount(7);
    adjVegList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        adjVegList_mouseClicked(e);
      }
    });
    uplandVegPanel.setLayout(borderLayout4);
    uplandVegPanel.setBorder(uplandBorder);
    uplandVegList.setFont(new java.awt.Font("Monospaced", 0, 12));
    uplandVegList.setToolTipText("Double click to go to a unit");
    uplandVegList.setPrototypeCellValue(protoCellValue);
    uplandVegList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    uplandVegList.setVisibleRowCount(7);
    uplandVegList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        uplandVegList_mouseClicked(e);
      }
    });
    predecessorList.setFont(new java.awt.Font("Monospaced", 0, 12));
    predecessorList.setToolTipText("Double click to go to a unit");
    predecessorList.setPrototypeCellValue(protoCellValue);
    predecessorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    predecessorList.setVisibleRowCount(7);
    predecessorList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        predecessorList_mouseClicked(e);
      }
    });
    aquaticUnitPanel.setLayout(borderLayout7);
    predPanel.setLayout(borderLayout1);
    predPanel.setBorder(predBorder);
    succPanel.setLayout(borderLayout2);
    succPanel.setBorder(succBorder);
    successorsList.setFont(new java.awt.Font("Monospaced", 0, 12));
    successorsList.setToolTipText("Double click to go to a unit");
    successorsList.setPrototypeCellValue(protoCellValue);
    successorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    successorsList.setVisibleRowCount(7);
    successorsList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        successorsList_mouseClicked(e);
      }
    });
    aquaticUnitPanel.setBorder(aquaticUnitBorder);
    vegUnitPanel.setBorder(vegUnitBorder);
    aquaticUnitBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    vegUnitBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    adjVegScrollPane.setFont(new java.awt.Font("Monospaced", 0, 12));
    flowLayout7.setAlignment(FlowLayout.LEFT);
    statusText.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
    statusText.setForeground(Color.blue);
    statusText.setText("PERENNIAL");
    statusLabel.setText("Status");
    aquaticUnitPanel.add(predPanel, BorderLayout.WEST);
    predPanel.add(predScrollPane, BorderLayout.CENTER);
    aquaticUnitPanel.add(succPanel, BorderLayout.CENTER);
    succPanel.add(succScrollPane, BorderLayout.CENTER);
    neighborsPanel.add(aquaticUnitPanel, null);
    neighborsPanel.add(vegUnitPanel, null);
    vegUnitPanel.add(uplandVegPanel, BorderLayout.WEST);
    uplandVegPanel.add(uplandVegScrollPane, BorderLayout.CENTER);
    vegUnitPanel.add(adjVegPanel, BorderLayout.CENTER);
    adjVegPanel.add(adjVegScrollPane, BorderLayout.CENTER);
    getContentPane().add(mainPanel);
    mainPanel.add(topPanel, null);
    topPanel.add(jPanel1, null);
    jPanel1.add(searchPB, null);
    jPanel1.add(resultsOnlyCB, null);
    topPanel.add(prevNextPanel, null);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(idPanel, null);
    idPanel.add(idLabel, null);
    idPanel.add(idEdit, null);
    prevNextPanel.add(nextPB, null);
    mainPanel.add(centerPanel, null);
    centerPanel.add(infoPanel, BorderLayout.NORTH);
    infoPanel.add(panelCol1, null);
    infoPanel.add(panelCol2, null);
    labelsPanel.add(currentStateLabel, null);
    labelsPanel.add(groupLabel, null);
    labelsPanelCol2.add(lengthLabel, null);
    labelsPanelCol2.add(segmentNumLabel, null);
    labelsPanelCol2.add(statusLabel);
    valuesPanel.add(currentStateValue, null);
    valuesPanel.add(groupValue, null);
    valuesPanelCol2.add(lengthValue, null);
    valuesPanelCol2.add(segmentNumValue, null);
    valuesPanelCol2.add(statusText);
    centerPanel.add(neighborsPanel, BorderLayout.SOUTH);
    mainPanel.add(bottomPanel, null);
    bottomPanel.add(historyTabbedPane, null);
    historyTabbedPane.add(historyPanel, "History");
    historyPanel.add(historyScrollPane, null);
    historyTabbedPane.add(treatmentPanel, "Treatment History");
    treatmentPanel.add(treatmentScroll, null);
    treatmentScroll.getViewport().add(treatmentText, null);
    historyScrollPane.getViewport().add(historyText, null);
    panelCol2.add(labelsPanelCol2, null);
    panelCol2.add(valuesPanelCol2, null);
    panelCol1.add(labelsPanel, null);
    panelCol1.add(valuesPanel, null);
    predScrollPane.getViewport().add(predecessorList, null);
    succScrollPane.getViewport().add(successorsList, null);
    uplandVegScrollPane.getViewport().add(uplandVegList, null);
    adjVegScrollPane.getViewport().add(adjVegList, null);
  }
  /**
   * Initializes the Eau Analysis dialog by setting the area to the current area and eau to the first Eau in the current area.  
   * Contains checks to makes sure the dialog does not exceed screen height.  
   */
  private void initialize() {
    area       = simpplle.comcode.Simpplle.getCurrentArea();
    currentEau = area.getFirstEau();

    // Make sure dialog will fit screen.
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dlg = getPreferredSize();
    if (dlg.height > screen.height) {
      historyText.setRows(6);
      treatmentText.setRows(6);
    }

    historyText.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
    treatmentText.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
    setSize(getPreferredSize());
    updateDialog();
  }
  /**
   * Updates the Eau Analysis dialog to information pertinent to the current Eau.
   * The method enables previous and next buttons, predecessor, successor, upland Evu and adjacent Evu vegetative lists.
   * If there is no information in the system for these Eau elements, the dummy lists will be used.
   */
  private void updateDialog() {
    if (currentEau != null) {
      prevPB.setEnabled(true);
      nextPB.setEnabled(true);
      predecessorList.setEnabled(true);
      successorsList.setEnabled(true);
      uplandVegList.setEnabled(true);
      adjVegList.setEnabled(true);

      String id = Integer.toString(currentEau.getId());
      idLabel.setText(id);
      idEdit.setText(id);

      currentStateValue.setText(currentEau.getCurrentState().toString());

      groupValue.setText(currentEau.getLtaValleySegmentGroup().toString());

      segmentNumValue.setText(Integer.toString(currentEau.getSegmentNumber()));

      statusText.setText(currentEau.getStatus().toString());

      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(0);  // Don't show fractional part.
      lengthValue.setText(nf.format(currentEau.getFloatLength()));

      String[] dummyList = new String[] {""};
      String[] theList;

      theList = currentEau.getPredecessorDisplay();
      predecessorList.setEnabled((theList != null));
      if (theList == null) {  theList = dummyList; }
      predecessorList.setListData(theList);

      theList = currentEau.getSuccessorDisplay();
      successorsList.setEnabled((theList != null));
      if (theList == null) { theList = dummyList; }
      successorsList.setListData(theList);

      theList = currentEau.getUplandVegDisplay();
      uplandVegList.setEnabled((theList != null));
      if (theList == null) {  theList = dummyList; }
      uplandVegList.setListData(theList);

      theList = currentEau.getAdjVegDisplay();
      adjVegList.setEnabled((theList != null));
      if (theList == null) {  theList = dummyList; }
      adjVegList.setListData(theList);

//      historyText.setText(currentEvu.getHistory());
//      treatmentText.setText(currentEvu.getTreatmentHistory());

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
      predecessorList.setEnabled(false);
      successorsList.setEnabled(false);
      uplandVegList.setEnabled(false);
      adjVegList.setEnabled(false);

      idLabel.setText("");
      idEdit.setText("");
      currentStateValue.setText("");
      groupValue.setText("");
      segmentNumValue.setText("");
      lengthValue.setText("");
      historyText.setText("");
      treatmentText.setText("");
      statusText.setText("");
    }
    update(getGraphics());
  }
  /**
   * Sets the current Eau's predecessor to the user selected predecessor in predecessor JList
   */
  private void goPredecessor() {
    goAquaticUnit((String) predecessorList.getSelectedValue());
  }
  /**
   * Sets the current Eau's successor to the user selected successor in successor JList.  
   */
  private void goSuccessor() {
    goAquaticUnit((String) successorsList.getSelectedValue());
  }
  /**
   * Sets the current Eau for analysis to the parsed input string.  This changes the current Eau.  If nothing is input, the current Eau does not change.  
   * @param str name of Eau to be analyzed
   */
  private void goAquaticUnit(String str) {
    int                 id = -1;
    StringTokenizerPlus strTok;

    if (str == null) { return; }

    strTok = new StringTokenizerPlus(str," ");
    try {
      id = strTok.getIntToken();
    }
    catch (simpplle.comcode.ParseError e) {}

    if (id == -1) { return; }
    goAquaticUnit(area.getEau(id));
  }
  /**
   * Sets the Adjacent Evu for the current Eau being analyzed to the minimum index number choosen in the adjacent veg list.  
   */
  private void goAdjVeg() {
    goVegUnit((String) adjVegList.getSelectedValue());
  }
  /**
   * Sets the Upland Evu for the current Eau being analyzed to the minimum index number choosen in the upland veg list.  This will be an Evu ID.    
   */
  private void goUplandVeg() {
    goVegUnit((String) uplandVegList.getSelectedValue());
  }
  /**
   * Sets the current upland Evu for analysis to the parsed input string of the selected EVU id.  
   * This changes the current upland Evu.  If nothing is selected, the current upland Evu does not change.  
   * @param str name of Upland Evu to be analyzed
   */
  private void goVegUnit(String str) {
    int                 id = -1;
    StringTokenizerPlus strTok;

    if (str == null) { return; }

    strTok = new StringTokenizerPlus(str," ");
    try {
      id = strTok.getIntToken();
    }
    catch (simpplle.comcode.ParseError e) {}

    if (id == -1) { return; }
    goVegUnit(id);
  }
  /**
   * Sets the Evu to either the Evu instance being analyzed, or a new EvuAnalysis instance.
   * @param id the Evu id.
   */
  private void goVegUnit(int id) {
    EvuAnalysis dlg;

    if (EvuAnalysis.isOpen()) {
      dlg = EvuAnalysis.getInstance();

    }
    else {
      dlg = new EvuAnalysis(JSimpplle.getSimpplleMain(),"Vegetative Unit Analysis",false);
    }
    dlg.goUnit(id);
    dlg.setVisible(true);
  }
  /**
   * Sets the current Eau to parameter Eau.
   * @param eau the Eau to be set as the current Eau.
   */
  void goAquaticUnit(ExistingAquaticUnit eau) {
    currentEau = eau;
    updateDialog();
  }
  /**
   * Gets the previous Eau if previous button selected.
   * @param e 'previous'
   */
  void prevPB_actionPerformed(ActionEvent e) {
    goAquaticUnit(area.getPrevEau(currentEau));
  }
  /**
   * Gets the previous Eau if previous button selected.
   * @param e 'previous'
   */
  void nextPB_actionPerformed(ActionEvent e) {
    goAquaticUnit(area.getNextEau(currentEau));
  }
  /**
   * Allows for editing of selected Eau.
   * @param e
   */
  private void idEdit_actionPerformed(ActionEvent e) {
    int id;

    try {
      id = Integer.parseInt(idEdit.getText());
      if (area.isValidAquaticUnitId(id) == false) {
        throw new NumberFormatException();
      }

      goAquaticUnit(area.getEau(id));
    }
    catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(this,idEdit.getText() +
                                    " is not a value unit id.",
                                    "Invalid Unit Id",
                                    JOptionPane.ERROR_MESSAGE);
      if (currentEau != null) {
        idEdit.setText(Integer.toString(currentEau.getId()));
      }
      else {
        idEdit.setText("");
      }
    }
  }
  /**
   * Exist the Eau Analysis Dialog.
   */
  private void quit() {
    isOpen = false;
    setVisible(false);
    dispose();
  }
  /**
   * If window closing event occurs, quits Eau Analysis Dialog.
   * @param e window closing event
   */
  void this_windowClosing(WindowEvent e) {
    quit();
  }
  /**
   * Deprecated search button method - does nothing.
   * @param e
   */
  private void searchPB_actionPerformed(ActionEvent e) {

  }
  // TODO: Look into removing this...
  /**
   * This method does nothing.
   * @param e
   */
  private void resultsOnlyCB_actionPerformed(ActionEvent e) {}
  /**
   * Checks if Eau Analysis Dialog is open.
   * @return true if Eau Analysis Dialog is open
   */
  public static boolean isOpen() { return isOpen; }
  /**
   * Used to choose from predecessor list via mouse click
   * @param e - double mouse click
   */
  private void predecessorList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goPredecessor();
    }
  }
  /**
   * Used to choose from successor list via mouse click.
   * @param e - double mouse click
   */
  private void successorsList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goSuccessor();
    }
  }
  /**
   * Used to choose from upland Evu list via mouse click.
   * @param e - double mouse click
   */
  private void uplandVegList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goUplandVeg();
    }
  }
  /**
   * Used to choose from upland adjacent Evu list via mouse click.
   * @param e - double mouse click
   */
  private void adjVegList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goAdjVeg();
    }
  }
  /**
   * Gets the current Eau Analysis instance
   * @return the current Eau Analysis
   */
  public static EauAnalysis getInstance() {
    return instance;
  }
}
