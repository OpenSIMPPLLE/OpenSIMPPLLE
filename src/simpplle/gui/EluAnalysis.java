package simpplle.gui;

import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.borland.jbcl.layout.*;
import simpplle.*;
import simpplle.comcode.*;
import java.awt.Font;

/**
*
* The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class creates an Existing Land Unit analysis panel.
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller 
*   
* 
*/

public class EluAnalysis extends JPanel {
  Area             area;
  ExistingLandUnit currentElu;
  UnitAnalysis     dialog;

  public static final String protoCellValue = "123456         ";

  JPanel topPanel = new JPanel();
  JPanel prevNextPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton prevPB = new JButton();
  JPanel idPanel = new JPanel();
  JButton nextPB = new JButton();
  GridLayout gridLayout1 = new GridLayout();
  JLabel idLabel = new JLabel();
  JTextField idEdit = new JTextField();
  JPanel infoPanel = new JPanel();
  JLabel soilTypeValue = new JLabel();
  JLabel soilTypeLabel = new JLabel();
  JLabel landformValue = new JLabel();
  JLabel landformLabel = new JLabel();
  JPanel centerPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel neighborsPanel = new JPanel();
  JPanel bottomPanel = new JPanel();
  JPanel historyPanel = new JPanel();
  JScrollPane historyScrollPane = new JScrollPane();
  JTextArea historyText = new JTextArea();
  TitledBorder titledBorder1;
  JTabbedPane historyTabbedPane = new JTabbedPane();
  JPanel treatmentPanel = new JPanel();
  JScrollPane treatmentScroll = new JScrollPane();
  JTextArea treatmentText = new JTextArea();
  FlowLayout flowLayout8 = new FlowLayout();
  FlowLayout flowLayout9 = new FlowLayout();
  JPanel valuesPanel = new JPanel();
  JPanel labelsPanel = new JPanel();
  GridLayout gridLayout4 = new GridLayout();
  GridLayout gridLayout5 = new GridLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel slopeLabel = new JLabel();
  JLabel parentMaterialLabel = new JLabel();
  JLabel slopeValue = new JLabel();
  JLabel parentMaterialValue = new JLabel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel panelCol2 = new JPanel();
  JPanel panelCol1 = new JPanel();
  JPanel valuesPanelCol2 = new JPanel();
  JPanel labelsPanelCol2 = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  GridLayout gridLayout2 = new GridLayout();
  GridLayout gridLayout3 = new GridLayout();
  FlowLayout flowLayout5 = new FlowLayout();
  Border border1;
  TitledBorder attributesBorder;
  Border border2;
  TitledBorder adjacentUnitsBorder;
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout10 = new FlowLayout();
  JCheckBox resultsOnlyCB = new JCheckBox();
  JButton searchPB = new JButton();
  FlowLayout flowLayout6 = new FlowLayout();
  Border border3;
  Border border4;
  TitledBorder succBorder;
  Border border5;
  Border border6;
  TitledBorder adjVegBorder;
  JPanel vegUnitPanel = new JPanel();
  JPanel vegPanel = new JPanel();
  JScrollPane vegScrollPane = new JScrollPane();
  JList assocVegList = new JList();
  JList adjacentList = new JList();
  JScrollPane adjScrollPane = new JScrollPane();
  JPanel adjUnitPanel = new JPanel();
  JPanel adjPanel = new JPanel();
  Border border7;
  Border border8;
  TitledBorder vegUnitBorder;
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  BorderLayout borderLayout7 = new BorderLayout();
  FlowLayout flowLayout7 = new FlowLayout();
  JLabel elevationLabel = new JLabel();
  JLabel acresLabel = new JLabel();
  JLabel elevationValue = new JLabel();
  JLabel acresValue = new JLabel();
  JLabel aspectLabel = new JLabel();
  JLabel aspectValue = new JLabel();
  private JLabel depthLabel = new JLabel();
  private JLabel depthValue = new JLabel();

/**
 * Constructor for EluAnalysis panel, sets the Elu Analysis to input unit analysis dialog.  
 * @param dialog
 */
  public EluAnalysis(UnitAnalysis dialog) {
    super();
    this.dialog = dialog;

    try {
      jbInit();
      initialize();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  /**
   * overloaded EluAnalysis constructor.  Creates a new instance of Unit Analysis dialog.  
   */
  public EluAnalysis() {
    this(new UnitAnalysis());
  }
  /**
   * Initializes the Elu Analysis panel.  Sets the panels buttons, border,s layouts, and margins, and images.
   * @throws Exception
   */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createEmptyBorder();
    attributesBorder = new TitledBorder(border1,"Attributes");
    border2 = BorderFactory.createEmptyBorder();
    adjacentUnitsBorder = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Adjacent Units");
    border3 = BorderFactory.createEmptyBorder();
    border4 = BorderFactory.createEmptyBorder();
    succBorder = new TitledBorder(border4,"Successors");
    border5 = BorderFactory.createEmptyBorder();
    border6 = BorderFactory.createEmptyBorder();
    adjVegBorder = new TitledBorder(border6,"Adjacent Veg");
    border7 = BorderFactory.createEmptyBorder();
    border8 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    vegUnitBorder = new TitledBorder(border8,"Vegetative Units");
    this.setLayout(verticalFlowLayout1);
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
    infoPanel.setLayout(flowLayout2);
    soilTypeValue.setFont(new java.awt.Font("Dialog", 1, 14));
    soilTypeValue.setForeground(Color.blue);
    soilTypeValue.setText("Unknown");
    soilTypeLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    soilTypeLabel.setText("Soil Type");
    landformValue.setFont(new java.awt.Font("Dialog", 1, 14));
    landformValue.setForeground(Color.blue);
    landformValue.setText("12-FMA");
    landformLabel.setFont(new java.awt.Font("Dialog", 0, 14));
    landformLabel.setText("Landform");
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
    historyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    treatmentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    borderLayout3.setVgap(5);
    bottomPanel.setBorder(BorderFactory.createEtchedBorder());
    labelsPanel.setLayout(gridLayout4);
    valuesPanel.setLayout(gridLayout5);
    gridLayout4.setRows(4);
    gridLayout4.setVgap(5);
    gridLayout5.setRows(4);
    gridLayout5.setVgap(5);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(10);
    flowLayout2.setVgap(0);
    slopeLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    slopeLabel.setText("Slope");
    parentMaterialLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    parentMaterialLabel.setText("Parent Material");
    slopeValue.setFont(new java.awt.Font("Dialog", 1, 14));
    slopeValue.setForeground(Color.blue);
    slopeValue.setText("0.5");
    parentMaterialValue.setFont(new java.awt.Font("Dialog", 1, 14));
    parentMaterialValue.setForeground(Color.blue);
    parentMaterialValue.setText("Unknown");
    flowLayout3.setAlignment(FlowLayout.LEFT);
    flowLayout3.setHgap(0);
    flowLayout3.setVgap(0);
    panelCol2.setLayout(flowLayout4);
    labelsPanelCol2.setLayout(gridLayout2);
    gridLayout2.setRows(4);
    gridLayout2.setVgap(5);
    valuesPanelCol2.setLayout(gridLayout3);
    gridLayout3.setRows(4);
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
    adjacentUnitsBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    verticalFlowLayout1.setVgap(0);
    verticalFlowLayout1.setVerticalFill(true);
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
    succBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    adjVegBorder.setTitle("Adjacent");
    adjVegBorder.setTitleFont(new java.awt.Font("Monospaced", 2, 12));
    vegUnitPanel.setLayout(borderLayout6);
    vegPanel.setLayout(borderLayout4);
    assocVegList.setFont(new java.awt.Font("Monospaced", 0, 12));
    assocVegList.setToolTipText("Double click to go to a unit");
    assocVegList.setPrototypeCellValue(protoCellValue);
    assocVegList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    assocVegList.setVisibleRowCount(7);
    assocVegList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        assocVegList_mouseClicked(e);
      }
    });
    adjacentList.setFont(new java.awt.Font("Monospaced", 0, 12));
    adjacentList.setToolTipText("Double click to go to a unit");
    adjacentList.setPrototypeCellValue(protoCellValue);
    adjacentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    adjacentList.setVisibleRowCount(7);
    adjacentList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        adjacentList_mouseClicked(e);
      }
    });
    adjUnitPanel.setLayout(borderLayout7);
    adjPanel.setLayout(borderLayout1);
    adjUnitPanel.setBorder(adjacentUnitsBorder);
    vegUnitPanel.setBorder(vegUnitBorder);
    vegUnitBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    vegUnitBorder.setTitle("Vegetative Units");
    flowLayout7.setAlignment(FlowLayout.LEFT);
    adjPanel.setBorder(null);
    vegPanel.setBorder(null);
    elevationLabel.setText("Elevation");
    acresLabel.setText("Acres");
    elevationValue.setFont(new java.awt.Font("Dialog", 1, 14));
    elevationValue.setForeground(Color.blue);
    elevationValue.setHorizontalAlignment(SwingConstants.LEFT);
    elevationValue.setText("3500");
    acresValue.setFont(new java.awt.Font("Dialog", 1, 14));
    acresValue.setForeground(Color.blue);
    acresValue.setText("5");
    aspectLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    aspectLabel.setText("Aspect");
    aspectValue.setFont(new java.awt.Font("Dialog", 1, 14));
    aspectValue.setForeground(Color.blue);
    aspectValue.setText("30 N");
    adjScrollPane.setMinimumSize(new Dimension(100, 24));
    adjScrollPane.setPreferredSize(new Dimension(200, 132));
    vegScrollPane.setMinimumSize(new Dimension(100, 24));
    vegScrollPane.setPreferredSize(new Dimension(200, 132));
    depthLabel.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    depthLabel.setText("Depth");
    depthValue.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
    depthValue.setForeground(Color.blue);
    depthValue.setText("\"\"");
    adjUnitPanel.add(adjPanel, BorderLayout.WEST);
    adjPanel.add(adjScrollPane, BorderLayout.CENTER);
    adjScrollPane.add(adjacentList, null);
    neighborsPanel.add(adjUnitPanel, null);
    neighborsPanel.add(vegUnitPanel, null);
    vegUnitPanel.add(vegPanel, BorderLayout.WEST);
    vegPanel.add(vegScrollPane, BorderLayout.CENTER);
    vegScrollPane.add(assocVegList, null);
    this.add(topPanel, null);
    topPanel.add(jPanel1, null);
    jPanel1.add(searchPB, null);
    jPanel1.add(resultsOnlyCB, null);
    topPanel.add(prevNextPanel, null);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(idPanel, null);
    idPanel.add(idLabel, null);
    idPanel.add(idEdit, null);
    prevNextPanel.add(nextPB, null);
    this.add(centerPanel, null);
    centerPanel.add(infoPanel, BorderLayout.NORTH);
    infoPanel.add(panelCol1, null);
    infoPanel.add(panelCol2, null);
    labelsPanel.add(soilTypeLabel, null);
    labelsPanel.add(landformLabel, null);
    labelsPanel.add(acresLabel, null);
    labelsPanel.add(elevationLabel, null);
    labelsPanelCol2.add(slopeLabel, null);
    labelsPanelCol2.add(aspectLabel, null);
    labelsPanelCol2.add(parentMaterialLabel, null);
    labelsPanelCol2.add(depthLabel);
    valuesPanel.add(soilTypeValue, null);
    valuesPanel.add(landformValue, null);
    valuesPanel.add(acresValue, null);
    valuesPanel.add(elevationValue, null);
    valuesPanelCol2.add(slopeValue, null);
    valuesPanelCol2.add(aspectValue, null);
    valuesPanelCol2.add(parentMaterialValue, null);
    valuesPanelCol2.add(depthValue);
    centerPanel.add(neighborsPanel, BorderLayout.SOUTH);
    this.add(bottomPanel, null);
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
    adjScrollPane.getViewport().add(this.adjacentList, null);
    vegScrollPane.getViewport().add(this.assocVegList, null);
  }
/**
 * Initializes the Elu analysis panel to the current area and first ELU.  If dialog height exceeds screen height adjusts the dialog size, otherwise sets the size as preferred size.
 * 
 */
  private void initialize() {
    area       = simpplle.comcode.Simpplle.getCurrentArea();
    currentElu = area.getFirstElu();

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
 * Updates the Elu Analysis dialog.  If there is a current Elu instance going on, sets the adjacent Evu, associated Evu lists are enabled.  Current Elu id, Soil type, landform, elevation, slope, aspect, parent material, 
 * depth and acreage.  If there is not a current elu, sets all the above to null values and sets enabled on previous and next buttons to false.     
 */
  private void updateDialog() {
    if (currentElu != null) {
      prevPB.setEnabled(true);
      nextPB.setEnabled(true);
      adjacentList.setEnabled(true);
      assocVegList.setEnabled(true);

      String id = Integer.toString(currentElu.getId());
      idLabel.setText(id);
      idEdit.setText(id);

      soilTypeValue.setText(currentElu.getSoilType().toString());
      landformValue.setText(currentElu.getLandform().toString());
      elevationValue.setText(Integer.toString(currentElu.getElevation()));
      slopeValue.setText(Float.toString(currentElu.getSlope()));
      if (currentElu.hasNumericAspect()) {
        aspectValue.setText(Double.toString(currentElu.getAspect()));
      }
      else {
        aspectValue.setText(currentElu.getAspectName());
      }
      parentMaterialValue.setText(currentElu.getParentMaterial());

      depthValue.setText(currentElu.getDepth());

      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(0);  // Don't show fractional part.
      acresValue.setText(nf.format(currentElu.getFloatAcres()));

      if (currentElu.getNeighbors() != null) {
        adjacentList.setListData(currentElu.getNeighbors().toArray());
      }
      else { adjacentList.setEnabled(false); }

      if (currentElu.getAssociatedVegUnits() != null) {
        assocVegList.setListData(currentElu.getAssociatedVegUnits().toArray());
      }
      else  { assocVegList.setEnabled(false); }

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
      adjacentList.setEnabled(false);
      assocVegList.setEnabled(false);

      idLabel.setText("");
      idEdit.setText("");

      soilTypeValue.setText("");
      landformValue.setText("");
      elevationValue.setText("");
      slopeValue.setText("");
      aspectValue.setText("");
      parentMaterialValue.setText("");
      acresValue.setText("");
      historyText.setText("");
      treatmentText.setText("");
    }
    dialog.refresh();
  }

 /**
  * Sets the Elu to the adjacent list value selected via mouse click.  
  */

  private void goLandUnit() {
    goLandUnit((ExistingLandUnit)adjacentList.getSelectedValue());
  }
  /**
   * Sets the current Elu to the passed Elu
   * @param elu
   */
  public void goLandUnit(ExistingLandUnit elu) {
    currentElu = elu;
    updateDialog();
  }
/**
 * If there is an Evu analysis instance open, the Veg Unit is set to that, else a new instance of the Evu Analysis dialog is started and Evu is set to the selected Evu ID.  
 */
  public void goVegUnit() {
    EvuAnalysis dlg;

    if (EvuAnalysis.isOpen()) {
      dlg = EvuAnalysis.getInstance();

    }
    else {
      dlg = new EvuAnalysis(JSimpplle.getSimpplleMain(),"Vegetative Unit Analysis",false);
    }
    dlg.goUnit((Evu)assocVegList.getSelectedValue());
    dlg.setVisible(true);
  }
/**
 * Selects the previous Elu from the current area.
 * @param e 'previous'.  
 */
  void prevPB_actionPerformed(ActionEvent e) {
    goLandUnit(area.getPrevElu(currentElu));
  }
  /**
   * Selects the previous Elu from the current area.
   * @param e 'next'.  
   */
  void nextPB_actionPerformed(ActionEvent e) {
    goLandUnit(area.getNextElu(currentElu));
  }
/**
 * If edit is selected and the Elu ID is valid for current area, goes to the ELU selected. 
 * @param e
 */
  void idEdit_actionPerformed(ActionEvent e) {
    int id;

    try {
      id = Integer.parseInt(idEdit.getText());
      if (area.isValidLandUnitId(id) == false) {
        throw new NumberFormatException();
      }

      goLandUnit(area.getElu(id));
    }
    catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(this,idEdit.getText() +
                                    " is not a value unit id.",
                                    "Invalid Unit Id",
                                    JOptionPane.ERROR_MESSAGE);
      if (currentElu != null) {
        idEdit.setText(Integer.toString(currentElu.getId()));
      }
      else {
        idEdit.setText("");
      }
    }
  }

  /**
   * This method deprecated for OpenSimpplle 1.0
   * @param e
   */
  void searchPB_actionPerformed(ActionEvent e) {

  }
/**
 * This method does nothing snf will be deprecated for OpenSimpplle 1.0.
 * @param e
 */
  void resultsOnlyCB_actionPerformed(ActionEvent e) {

  }
/**
 * Selects adjacent Elu via mouse click.  
 * @param e double mouse click
 */
  void adjacentList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goLandUnit();
    }
  }

  /**
   * Selects associated Evu via mouse click.  
   * @param e double mouse click
   */
  void assocVegList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      goVegUnit();
    }
  }

}



