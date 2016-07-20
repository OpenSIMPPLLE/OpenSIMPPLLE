/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */


package simpplle.gui;

import simpplle.JSimpplle;
import simpplle.comcode.SystemKnowledge;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

// Imported from simpplle.comcode
import simpplle.comcode.Fmz;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Area;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.Simpplle;
/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class FmzEditor extends JDialog {
  private RegionalZone currentZone;
  private Fmz          currentFmz;

  private boolean focusLost;
  private Vector allFmz;
  private int    allFmzPos;
  private boolean inInit = false;

  private static final int LIGHTNING = 0;
  private static final int MANMADE   = 1;
  private static final int COST      = 2;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuFileDefault = new JMenuItem();
  JPanel prevNextPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JLabel fmzLabel = new JLabel();
  JButton prevPB = new JButton();
  JButton nextPB = new JButton();
  JTextField fmzText = new JTextField();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel fmzLabelPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel southPanel = new JPanel();
  JPanel dataGridPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel classALabel = new JLabel();
  JLabel classBLabel = new JLabel();
  JLabel classCLabel = new JLabel();
  JLabel classDLabel = new JLabel();
  JLabel classELabel = new JLabel();
  JLabel classFLabel = new JLabel();
  JTextField lightningTextA = new JTextField();
  JTextField manTextA = new JTextField();
  JTextField costTextA = new JTextField();
  JTextField lightningTextB = new JTextField();
  JTextField manTextB = new JTextField();
  JTextField costTextB = new JTextField();
  JTextField lightningTextC = new JTextField();
  JTextField manTextC = new JTextField();
  JTextField costTextC = new JTextField();
  JTextField lightningTextD = new JTextField();
  JTextField manTextD = new JTextField();
  JTextField costTextD = new JTextField();
  JTextField lightningTextE = new JTextField();
  JTextField manTextE = new JTextField();
  JTextField costTextE = new JTextField();
  JTextField lightningTextF = new JTextField();
  JTextField manTextF = new JTextField();
  JTextField costTextF = new JTextField();
  JPanel dataPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JLabel Col4Label2 = new JLabel();
  JLabel Col3Label2 = new JLabel();
  JLabel Col2Label2 = new JLabel();
  JLabel Col1Label2 = new JLabel();
  JLabel Col4Label = new JLabel();
  JLabel Col3Label = new JLabel();
  JLabel Col2Label = new JLabel();
  JLabel Col1Label = new JLabel();
  JPanel acresPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  JLabel acresLabel = new JLabel();
  JTextField acresText = new JTextField();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenu jMenu1 = new JMenu();
  JMenuItem menuActionDelete = new JMenuItem();
  JMenuItem menuActionCreate = new JMenuItem();
  JMenuItem menuActionDeleteAll = new JMenuItem();
  JMenu menuKnowledgeSource = new JMenu();
  JMenuItem menuKnowledgeSourceDisplay = new JMenuItem();
  JPanel responseTimePanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JLabel responseTimeLabel = new JLabel();
  JTextField responseTimeText = new JTextField();
  JMenuItem menuImportOldFile = new JMenuItem();

  public FmzEditor(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public FmzEditor() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    System.currentTimeMillis();
    mainPanel.setLayout(borderLayout1);
    menuFile.setText("File");
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    this.setJMenuBar(jMenuBar1);
    menuFileDefault.setToolTipText("load default fmz data file for the current zone");
    menuFileDefault.setText("Load Default Data");
    menuFileDefault.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileDefault_actionPerformed(e);
      }
    });
    prevNextPanel.setLayout(flowLayout2);
    fmzLabel.setFont(new java.awt.Font("Dialog", 1, 14));
    fmzLabel.setText("Fire Management Zone");
    fmzText.setBackground(Color.white);
    fmzText.setText("dummy value xxx");
    fmzText.setToolTipText("Type an FMZ name and press enter");
    fmzText.setSelectionColor(Color.blue);
    fmzText.setColumns(15);
    fmzText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        fmzText_actionPerformed(e);
      }
    });
    prevPB.setIcon(new ImageIcon(simpplle.gui.FmzEditor.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.FmzEditor.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    nextPB.setIcon(new ImageIcon(simpplle.gui.FmzEditor.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.FmzEditor.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    northPanel.setLayout(borderLayout2);
    fmzLabelPanel.setLayout(flowLayout3);
    flowLayout2.setVgap(0);
    flowLayout3.setVgap(2);
    dataGridPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(4);
    gridLayout1.setHgap(10);
    gridLayout1.setRows(8);
    classALabel.setHorizontalAlignment(SwingConstants.CENTER);
    classALabel.setText("0.00 - 0.25");
    classBLabel.setHorizontalAlignment(SwingConstants.CENTER);
    classBLabel.setText("0.26 - 9.99");
    classCLabel.setHorizontalAlignment(SwingConstants.CENTER);
    classCLabel.setText("10.00 - 99.99");
    classDLabel.setHorizontalAlignment(SwingConstants.CENTER);
    classDLabel.setText("100.00 - 299.99");
    classELabel.setHorizontalAlignment(SwingConstants.CENTER);
    classELabel.setText("300.00 - 999.99");
    classFLabel.setText("1000.00 +");
    lightningTextA.setBackground(Color.white);
    lightningTextA.setSelectionColor(Color.blue);
    lightningTextA.setColumns(8);
    lightningTextA.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextA_focusLost(e);
      }
    });
    lightningTextA.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextA_actionPerformed(e);
      }
    });
    manTextA.setBackground(Color.white);
    manTextA.setSelectionColor(Color.blue);
    manTextA.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextA_focusLost(e);
      }
    });
    manTextA.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextA_actionPerformed(e);
      }
    });
    costTextA.setBackground(Color.white);
    costTextA.setSelectionColor(Color.blue);
    costTextA.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextA_focusLost(e);
      }
    });
    costTextA.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextA_actionPerformed(e);
      }
    });
    lightningTextB.setBackground(Color.white);
    lightningTextB.setSelectionColor(Color.blue);
    lightningTextB.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextB_focusLost(e);
      }
    });
    lightningTextB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextB_actionPerformed(e);
      }
    });
    manTextB.setBackground(Color.white);
    manTextB.setSelectionColor(Color.blue);
    manTextB.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextB_focusLost(e);
      }
    });
    manTextB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextB_actionPerformed(e);
      }
    });
    costTextB.setBackground(Color.white);
    costTextB.setSelectionColor(Color.blue);
    costTextB.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextB_focusLost(e);
      }
    });
    costTextB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextB_actionPerformed(e);
      }
    });
    lightningTextC.setBackground(Color.white);
    lightningTextC.setSelectionColor(Color.blue);
    lightningTextC.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextC_focusLost(e);
      }
    });
    lightningTextC.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextC_actionPerformed(e);
      }
    });
    manTextC.setBackground(Color.white);
    manTextC.setSelectionColor(Color.blue);
    manTextC.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextC_focusLost(e);
      }
    });
    manTextC.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextC_actionPerformed(e);
      }
    });
    costTextC.setBackground(Color.white);
    costTextC.setSelectionColor(Color.blue);
    costTextC.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextC_focusLost(e);
      }
    });
    costTextC.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextC_actionPerformed(e);
      }
    });
    lightningTextD.setBackground(Color.white);
    lightningTextD.setSelectionColor(Color.blue);
    lightningTextD.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextD_focusLost(e);
      }
    });
    lightningTextD.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextD_actionPerformed(e);
      }
    });
    manTextD.setBackground(Color.white);
    manTextD.setSelectionColor(Color.blue);
    manTextD.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextD_focusLost(e);
      }
    });
    manTextD.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextD_actionPerformed(e);
      }
    });
    costTextD.setBackground(Color.white);
    costTextD.setSelectionColor(Color.blue);
    costTextD.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextD_focusLost(e);
      }
    });
    costTextD.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextD_actionPerformed(e);
      }
    });
    lightningTextE.setBackground(Color.white);
    lightningTextE.setSelectionColor(Color.blue);
    lightningTextE.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextE_focusLost(e);
      }
    });
    lightningTextE.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextE_actionPerformed(e);
      }
    });
    manTextE.setBackground(Color.white);
    manTextE.setSelectionColor(Color.blue);
    manTextE.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextE_focusLost(e);
      }
    });
    manTextE.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextE_actionPerformed(e);
      }
    });
    costTextE.setBackground(Color.white);
    costTextE.setSelectionColor(Color.blue);
    costTextE.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextE_focusLost(e);
      }
    });
    costTextE.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextE_actionPerformed(e);
      }
    });
    lightningTextF.setBackground(Color.white);
    lightningTextF.setSelectionColor(Color.blue);
    lightningTextF.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        lightningTextF_focusLost(e);
      }
    });
    lightningTextF.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        lightningTextF_actionPerformed(e);
      }
    });
    manTextF.setBackground(Color.white);
    manTextF.setSelectionColor(Color.blue);
    manTextF.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        manTextF_focusLost(e);
      }
    });
    manTextF.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        manTextF_actionPerformed(e);
      }
    });
    costTextF.setBackground(Color.white);
    costTextF.setSelectionColor(Color.blue);
    costTextF.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        costTextF_focusLost(e);
      }
    });
    costTextF.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        costTextF_actionPerformed(e);
      }
    });
    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
    dataPanel.setLayout(flowLayout1);
    Col4Label2.setHorizontalAlignment(SwingConstants.CENTER);
    Col4Label2.setText("$/Acre");
    Col3Label2.setHorizontalAlignment(SwingConstants.CENTER);
    Col3Label2.setText("Man-Caused");
    Col2Label2.setHorizontalAlignment(SwingConstants.CENTER);
    Col2Label2.setText("Lightning");
    Col4Label.setHorizontalAlignment(SwingConstants.CENTER);
    Col4Label.setText("Suppression");
    Col3Label.setText("Year Period");
    Col2Label.setHorizontalAlignment(SwingConstants.RIGHT);
    Col2Label.setText("# Fires in 10");
    Col1Label.setHorizontalAlignment(SwingConstants.CENTER);
    Col1Label.setText("Fire Size");
    acresPanel.setLayout(flowLayout5);
    acresLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    acresLabel.setText("Acres in Analysis Area");
    acresText.setBackground(Color.white);
    acresText.setSelectionColor(Color.blue);
    acresText.setColumns(8);
    acresText.addFocusListener(new java.awt.event.FocusAdapter() {

      public void focusLost(FocusEvent e) {
        acresText_focusLost(e);
      }
    });
    acresText.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        acresText_actionPerformed(e);
      }
    });
    borderLayout1.setVgap(20);
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileClose_actionPerformed(e);
      }
    });
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    jMenu1.setText("Actions");
    menuActionDelete.setEnabled(false);
    menuActionDelete.setText("Delete Current");
    menuActionDelete.setActionCommand("Delete Current Zone");
    menuActionDelete.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuActionDelete_actionPerformed(e);
      }
    });
    menuActionCreate.setText("Create New Zone");
    menuActionCreate.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuActionCreate_actionPerformed(e);
      }
    });
    menuActionDeleteAll.setText("Delete All Zones");
    menuActionDeleteAll.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuActionDeleteAll_actionPerformed(e);
      }
    });
    menuKnowledgeSource.setText("Knowledge Source");
    menuKnowledgeSourceDisplay.setText("Display");
    menuKnowledgeSourceDisplay.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuKnowledgeSourceDisplay_actionPerformed(e);
      }
    });
    responseTimePanel.setLayout(flowLayout4);
    responseTimeLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    responseTimeLabel.setToolTipText("Used only for uniform-sized polygon areas");
    responseTimeLabel.setText("Fire Suppression Response Time (Hours)");
    responseTimeText.setToolTipText("Used only for uniform-sized polygon areas");
    responseTimeText.setText("");
    responseTimeText.setColumns(8);
    responseTimeText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        responseTimeText_focusLost(e);
      }
    });
    responseTimeText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        responseTimeText_keyTyped(e);
      }
    });
    responseTimeText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        responseTimeText_actionPerformed(e);
      }
    });
    flowLayout4.setAlignment(FlowLayout.LEFT);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    menuImportOldFile.setText("Import old format file");
    menuImportOldFile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuImportOldFile_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    jMenuBar1.add(menuFile);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(menuKnowledgeSource);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuImportOldFile);
    menuFile.addSeparator();
    menuFile.add(menuFileDefault);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(prevNextPanel, BorderLayout.SOUTH);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(fmzText, null);
    prevNextPanel.add(nextPB, null);
    northPanel.add(fmzLabelPanel, BorderLayout.NORTH);
    fmzLabelPanel.add(fmzLabel, null);
    mainPanel.add(southPanel, BorderLayout.CENTER);
    southPanel.add(acresPanel, null);
    acresPanel.add(acresLabel, null);
    acresPanel.add(acresText, null);
    southPanel.add(responseTimePanel, null);
    responseTimePanel.add(responseTimeLabel, null);
    responseTimePanel.add(responseTimeText, null);
    southPanel.add(dataPanel, null);
    dataPanel.add(dataGridPanel, null);
    dataGridPanel.add(Col1Label, null);
    dataGridPanel.add(Col2Label, null);
    dataGridPanel.add(Col3Label, null);
    dataGridPanel.add(Col4Label, null);
    dataGridPanel.add(Col1Label2, null);
    dataGridPanel.add(Col2Label2, null);
    dataGridPanel.add(Col3Label2, null);
    dataGridPanel.add(Col4Label2, null);
    dataGridPanel.add(classALabel, null);
    dataGridPanel.add(lightningTextA, null);
    dataGridPanel.add(manTextA, null);
    dataGridPanel.add(costTextA, null);
    dataGridPanel.add(classBLabel, null);
    dataGridPanel.add(lightningTextB, null);
    dataGridPanel.add(manTextB, null);
    dataGridPanel.add(costTextB, null);
    dataGridPanel.add(classCLabel, null);
    dataGridPanel.add(lightningTextC, null);
    dataGridPanel.add(manTextC, null);
    dataGridPanel.add(costTextC, null);
    dataGridPanel.add(classDLabel, null);
    dataGridPanel.add(lightningTextD, null);
    dataGridPanel.add(manTextD, null);
    dataGridPanel.add(costTextD, null);
    dataGridPanel.add(classELabel, null);
    dataGridPanel.add(lightningTextE, null);
    dataGridPanel.add(manTextE, null);
    dataGridPanel.add(costTextE, null);
    dataGridPanel.add(classFLabel, null);
    dataGridPanel.add(lightningTextF, null);
    dataGridPanel.add(manTextF, null);
    dataGridPanel.add(costTextF, null);
    jMenu1.add(menuActionCreate);
    jMenu1.add(menuActionDelete);
    jMenu1.add(menuActionDeleteAll);
    menuKnowledgeSource.add(menuKnowledgeSourceDisplay);
  }

  private void initialize() {
    inInit = true;
    currentZone = Simpplle.getCurrentZone();
    allFmzPos   = 0;
    allFmz      = currentZone.getAllFmzNames();

    if (allFmz == null || allFmz.size() == 0) {
      Fmz.makeDefault();
      allFmz = currentZone.getAllFmzNames();
    }
    currentFmz  = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));

    updateDialog();
    inInit = false;
  }

  private void updateDialog() {
    fmzText.setText(currentFmz.getName());

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setGroupingUsed(false); // Don't print commas

    acresText.setText(nf.format(currentFmz.getAcres()));

    float time = currentFmz.getResponseTime();
    String text = Float.isNaN(time) ? "" : nf.format(time);
    responseTimeText.setText(text);

    lightningTextA.setText(nf.format(currentFmz.getNaturalFires(Fmz.A)));
    lightningTextB.setText(nf.format(currentFmz.getNaturalFires(Fmz.B)));
    lightningTextC.setText(nf.format(currentFmz.getNaturalFires(Fmz.C)));
    lightningTextD.setText(nf.format(currentFmz.getNaturalFires(Fmz.D)));
    lightningTextE.setText(nf.format(currentFmz.getNaturalFires(Fmz.E)));
    lightningTextF.setText(nf.format(currentFmz.getNaturalFires(Fmz.F)));

    manTextA.setText(nf.format(currentFmz.getManmadeFires(Fmz.A)));
    manTextB.setText(nf.format(currentFmz.getManmadeFires(Fmz.B)));
    manTextC.setText(nf.format(currentFmz.getManmadeFires(Fmz.C)));
    manTextD.setText(nf.format(currentFmz.getManmadeFires(Fmz.D)));
    manTextE.setText(nf.format(currentFmz.getManmadeFires(Fmz.E)));
    manTextF.setText(nf.format(currentFmz.getManmadeFires(Fmz.F)));

    costTextA.setText(nf.format(currentFmz.getCost(Fmz.A)));
    costTextB.setText(nf.format(currentFmz.getCost(Fmz.B)));
    costTextC.setText(nf.format(currentFmz.getCost(Fmz.C)));
    costTextD.setText(nf.format(currentFmz.getCost(Fmz.D)));
    costTextE.setText(nf.format(currentFmz.getCost(Fmz.E)));
    costTextF.setText(nf.format(currentFmz.getCost(Fmz.F)));

    menuActionDelete.setEnabled((currentFmz.isDefault() != true));

    File filename = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    menuFileClose.setEnabled((filename != null));
    menuFileSave.setEnabled((filename != null));
//    menuFileSave.setEnabled(((Fmz.getFilename() != null) && Fmz.hasChanged()));

    refresh();
  }

  private void cancel() {
    setVisible(false);
    dispose();
  }

  private void refresh() {
    update(getGraphics());
  }

  // *** Menu Events ***
  // *******************

  private boolean continueDespiteLoadedArea() {
    Area area = Simpplle.getCurrentArea();

    if (area != null) {
      String msg;
      int    choice;
      msg = "Any unit assigned an FMZ that is not present in\n" +
            "the new file will be assigned the default FMZ.\n\n" +
            "Are you sure?";
      choice = JOptionPane.showConfirmDialog(this,msg,"Area Currently Loaded, Proceed?.",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
  
      if (choice != JOptionPane.YES_OPTION) {
        return false;
      }
    }
    return true;
  }

  // ** File Menu **
  void menuFileOpen_actionPerformed(ActionEvent e) {
    Area area = Simpplle.getCurrentArea();
    if (area != null && !continueDespiteLoadedArea()) {
      return;
    }
    
    SystemKnowledgeFiler.openFile(this,SystemKnowledge.FMZ,menuFileSave,menuFileClose);
    
    allFmzPos   = 0;
    allFmz      = currentZone.getAllFmzNames();
    currentFmz  = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));

    // Make sure EVU's who point to this fmz no longer present
    // are reset to the default fmz.
    if (area != null) {
      area.updateFmzData();
    }
    updateDialog();
  }

  void menuImportOldFile_actionPerformed(ActionEvent e) {
    File         outfile;
    boolean      changed = false;
    Area         area;
    MyFileFilter extFilter;
    String       title = "Select a Fire Management Zone Data File";

    extFilter = new MyFileFilter("fmz",
                                 "FMZ Data Files (*.fmz)");

    setCursor(Utility.getWaitCursor());

    outfile = Utility.getOpenFile(this,title,extFilter);
    open: try {
      if (outfile == null) { break open; }

      Fmz.loadData(outfile);

      allFmzPos   = 0;
      allFmz      = currentZone.getAllFmzNames();
      currentFmz  = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
      updateDialog();

      menuFileSave.setEnabled(true);
      menuFileClose.setEnabled(true);

      area = Simpplle.getCurrentArea();
      if (area == null) { break open; }

      changed = area.updateFmzData();
      if (changed) {
        String msg = "Fmz's in the currently loaded area that\n" +
                     "referred to fmz's not currently loaded\n" +
                     "were changed to the default fmz.\n\n" +
                     "If this is not desired load the correct\n" +
                     "fmz data file for the area and then\n" +
                     "reload the current area.";
        JOptionPane.showMessageDialog(this,msg,"Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
    finally {
      setCursor(Utility.getNormalCursor());
      refresh();
    }
  }
  void menuFileClose_actionPerformed(ActionEvent e) {
    int    choice;
    String msg;

    File filename = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    if (filename != null && Fmz.hasChanged()) {
      msg = "Changes have been made.\n" +
            "If you continue these changes will be lost.\n\n" +
            "Do you wish to continue?";
      choice = JOptionPane.showConfirmDialog(this,msg,"Close Current File.",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        update(getGraphics());
        return;
      }
    }

    Fmz.closeFile();
    loadDefaultDataFile();
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    SystemKnowledgeFiler.saveFile(this, outfile, SystemKnowledge.FMZ,
                                  menuFileSave, menuFileClose);
    refresh();
  }
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(this, SystemKnowledge.FMZ, menuFileSave,
                                  menuFileClose);
    refresh();
  }

  private void loadDefaultDataFile() {
    try {
      Area area = Simpplle.getCurrentArea();
      if (area != null && !continueDespiteLoadedArea()) {
        return;
      }
      
      SystemKnowledge.readZoneDefault(SystemKnowledge.FMZ);

      allFmzPos = 0;
      allFmz = currentZone.getAllFmzNames();
      currentFmz = currentZone.getFmz((String) allFmz.elementAt(allFmzPos));

      // Make sure EVU's who point to this fmz no longer present
      // are reset to the default fmz.
      if (area != null) {
        area.updateFmzData();
      }
      updateDialog();
    }
    catch (SimpplleError err) {
      JOptionPane.showMessageDialog(this,err.getError(),"Error loading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void menuFileDefault_actionPerformed(ActionEvent e) {
    loadDefaultDataFile();
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    cancel();
  }

  // ** Action Menu **
  void menuActionCreate_actionPerformed(ActionEvent e) {
    String msg   = "Fire Management Zone Name";
    String title = "Create new Fire Management Zone";
    String name  = JOptionPane.showInputDialog(this,msg,title,
                                               JOptionPane.PLAIN_MESSAGE);
    if (name == null) { return; }

    name = name.toLowerCase();
    Fmz newFmz = new Fmz();
    newFmz.setName(name);
    currentZone.addFmz(newFmz);
    allFmz     = currentZone.getAllFmzNames();
    allFmzPos  = allFmz.indexOf(name);
    currentFmz = currentZone.getFmz(name);
    updateDialog();

    // What about no acres, or not fires????

    Runnable doRequestFocus = new Runnable() {
      public void run() {
        acresText.requestFocus();
      }
    };
    SwingUtilities.invokeLater(doRequestFocus);
  }

  void menuActionDelete_actionPerformed(ActionEvent e) {
    String msg;
    int    choice;

    msg = "This action will delete the current FMZ.\n" +
          "If an area is loaded, any unit assigned to this\n" +
          "FMZ will be reset the the default FMZ.\n\n" +
          "Are you sure?";
    choice = JOptionPane.showConfirmDialog(this,msg,"Delete current FMZ.",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      currentZone.removeFmz(currentFmz);
      allFmzPos   = 0;
      allFmz      = currentZone.getAllFmzNames();
      currentFmz  = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
      updateDialog();
    }
  }

  void menuActionDeleteAll_actionPerformed(ActionEvent e) {
    String msg;
    int    choice;

    msg = "This action will delete all Fire Management Zones,\n" +
          "except the default zone.\n" +
          "** If an area is loaded all units will be set to\n" +
          "default FMZ.\n\n" +
          "Are you sure?";
    choice = JOptionPane.showConfirmDialog(this,msg,"Delete current FMZ.",
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      currentZone.removeAllFmz();
      allFmzPos   = 0;
      allFmz      = currentZone.getAllFmzNames();
      currentFmz  = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
      updateDialog();
    }
  }

  void prevPB_actionPerformed(ActionEvent e) {
    if (allFmzPos == 0) { allFmzPos = allFmz.size(); }
    allFmzPos--;

    currentFmz = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
    updateDialog();
  }

  void nextPB_actionPerformed(ActionEvent e) {
    allFmzPos++;
    if (allFmzPos == allFmz.size()) { allFmzPos = 0; }

    currentFmz = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
    updateDialog();
  }

  void fmzText_actionPerformed(ActionEvent e) {
    String fmz = fmzText.getText().toLowerCase();

    if (allFmz.contains(fmz)) {
      allFmzPos  = allFmz.indexOf(fmz);
    }
    else {
      JOptionPane.showMessageDialog(this,"Fire Management Zone does not exist.",
                                    "Invalid FMZ",JOptionPane.WARNING_MESSAGE);
    }
    currentFmz = currentZone.getFmz((String)allFmz.elementAt(allFmzPos));
    updateDialog();
  }

  private void acresChanged() {
    String oldValueStr;
    float value = 0.0f;
    if (focusLost) { return; }

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setGroupingUsed(false); // Don't print commas

    oldValueStr = nf.format(currentFmz.getAcres());
    if (oldValueStr.equals(acresText.getText())) { return; }

    try {
      value = Float.valueOf(acresText.getText()).floatValue();

      if ((value > 0.0f) == false) {
        throw new NumberFormatException("Value must be greater than 0.0");
      }
      currentFmz.setAcres(value);
      updateDialog();
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      JOptionPane.showMessageDialog(this,nfe.getMessage(),
                                    "Invalid Number entered",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          acresText.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
    }
  }

  void acresText_actionPerformed(ActionEvent e) {
    acresChanged();
  }

  void acresText_focusLost(FocusEvent e) {
    acresChanged();
  }

  // Check to see if the user changed the value.
  // Need to know so we know if changes have been made
  // which require saving.
  private boolean hasFmzDataChanged(JTextField textField, int classId, int kind) {
    float        value;
    String       oldValueStr;
    NumberFormat nf = NumberFormat.getInstance();

    nf.setMaximumFractionDigits(2);
    nf.setGroupingUsed(false); // Don't print commas

    switch (kind) {
      case LIGHTNING: value = currentFmz.getNaturalFires(classId); break;
      case MANMADE:   value = currentFmz.getManmadeFires(classId); break;
      case COST:      value = currentFmz.getCost(classId); break;
      default: return false;
    }
    oldValueStr = nf.format(value);

    return (! oldValueStr.equals(textField.getText())); // (i.e Not Equal)
  }
  private void fmzDataChanged(int classId, final JTextField textField,
                                    int kind) {
    float value = 0.0f;
    if (focusLost) { return; }
    if (! hasFmzDataChanged(textField, classId, kind)) { return; }

    try {
      value = Float.valueOf(textField.getText()).floatValue();

      switch (kind) {
        case LIGHTNING: currentFmz.setNaturalFires(classId,value); break;
        case MANMADE:   currentFmz.setManmadeFires(classId,value); break;
        case COST:      currentFmz.setCost(classId,value); break;
        default: return;
      }
      updateDialog();
    }
    catch (NumberFormatException nfe) {
      focusLost = true;
      JOptionPane.showMessageDialog(this,nfe.getMessage(),
                                    "Invalid Number entered",
                                    JOptionPane.ERROR_MESSAGE);
      Runnable doRequestFocus = new Runnable() {
        public void run() {
          textField.requestFocus();
          focusLost = false;
        }
      };
      SwingUtilities.invokeLater(doRequestFocus);
    }
  }

  void lightningTextA_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.A,lightningTextA,LIGHTNING);
  }

  void lightningTextA_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.A,lightningTextA,LIGHTNING);
  }

  void manTextA_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.A,manTextA,MANMADE);
  }

  void manTextA_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.A,manTextA,MANMADE);
  }

  void costTextA_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.A,costTextA,COST);
  }

  void costTextA_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.A,costTextA,COST);
  }

  void lightningTextB_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.B,lightningTextB,LIGHTNING);
  }

  void lightningTextB_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.B,lightningTextB,LIGHTNING);
  }

  void manTextB_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.B,manTextB,MANMADE);
  }

  void manTextB_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.B,manTextB,MANMADE);
  }

  void costTextB_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.B,costTextB,COST);
  }

  void costTextB_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.B,costTextB,COST);
  }

  void lightningTextC_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.C,lightningTextC,LIGHTNING);
  }

  void lightningTextC_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.C,lightningTextC,LIGHTNING);
  }

  void manTextC_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.C,manTextC,MANMADE);
  }

  void manTextC_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.C,manTextC,MANMADE);
  }

  void costTextC_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.C,costTextC,COST);
  }

  void costTextC_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.C,costTextC,COST);
  }

  void lightningTextD_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.D,lightningTextD,LIGHTNING);
  }

  void lightningTextD_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.D,lightningTextD,LIGHTNING);
  }

  void manTextD_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.D,manTextD,MANMADE);
  }

  void manTextD_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.D,manTextD,MANMADE);
  }

  void costTextD_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.D,costTextD,COST);
  }

  void costTextD_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.D,costTextD,COST);
  }

  void lightningTextE_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.E,lightningTextE,LIGHTNING);
  }

  void lightningTextE_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.E,lightningTextE,LIGHTNING);
  }

  void manTextE_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.E,manTextE,MANMADE);
  }

  void manTextE_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.E,manTextE,MANMADE);
  }

  void costTextE_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.E,costTextE,COST);
  }

  void costTextE_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.E,costTextE,COST);
  }

  void lightningTextF_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.F,lightningTextF,LIGHTNING);
  }

  void lightningTextF_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.F,lightningTextF,LIGHTNING);
  }

  void manTextF_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.F,manTextF,MANMADE);
  }

  void manTextF_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.F,manTextF,MANMADE);
  }

  void costTextF_actionPerformed(ActionEvent e) {
    fmzDataChanged(Fmz.F,costTextF,COST);
  }

  void costTextF_focusLost(FocusEvent e) {
    fmzDataChanged(Fmz.F,costTextF,COST);
  }

  void menuKnowledgeSourceDisplay_actionPerformed(ActionEvent e) {
    String str = SystemKnowledge.getSource(SystemKnowledge.FMZ);
    String title = "Fire Occurrence Input Knowledge Source";


    KnowledgeSource dlg = new KnowledgeSource(JSimpplle.getSimpplleMain(),title,true,str);
    dlg.setVisible(true);
  }

  void responseTimeText_keyTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE &&
        key != '.') {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
  }
  void responseTimeText_actionPerformed(ActionEvent e) {
    if (inInit) { return; }
    updateResponseTime();
  }

  void responseTimeText_focusLost(FocusEvent e) {
    if (inInit) { return; }
    updateResponseTime();
  }
  private void updateResponseTime() {
    String text = responseTimeText.getText();
    float num = (text.length() == 0) ? Float.NaN : Float.parseFloat(text);
    currentFmz.setResponseTime(num);
  }

}
