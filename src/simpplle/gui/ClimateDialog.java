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
import java.awt.event.ActionListener;
import javax.swing.*;

/** 
 * This class defines the Climate Dialog, a type of JDialog.  It allows the user to input climate.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ClimateDialog extends JDialog {
  ClimateDialogWrapper wrap;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel topPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel prevNextPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel timeStepPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel timeStepLabel = new JLabel();
  JTextField timeStepEdit = new JTextField();
  JButton prevPB = new JButton();
  JButton nextPB = new JButton();
  JPanel temperaturePanel = new JPanel();
  JPanel moisturePanel = new JPanel();
  JPanel centerPanel = new JPanel();
  JLabel temperatureLabel = new JLabel();
  JLabel moistureLabel = new JLabel();
  JComboBox temperatureCB = new JComboBox();
  JComboBox moistureCB = new JComboBox();
  JPanel southPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JMenuBar menuBarClimate = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenu menuTimeSteps = new JMenu();
  JMenuItem menuFileOpen = new JMenuItem();
  JMenuItem menuFileSave = new JMenuItem();
  JMenuItem menuFileSaveAs = new JMenuItem();
  JMenuItem menuFileClose = new JMenuItem();
  JMenuItem menuFileQuit = new JMenuItem();
  JMenuItem menuTimeStepsDeleteCurrent = new JMenuItem();
  JMenuItem menuTimeStepsDeleteAll = new JMenuItem();
  GridLayout gridLayout4 = new GridLayout();
  GridLayout gridLayout5 = new GridLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  JButton explanationPB = new JButton();
  JButton closePB = new JButton();
  FlowLayout flowLayout3 = new FlowLayout();
  JMenuItem menuFileOldFormat = new JMenuItem();
   JPanel jPanel1 = new JPanel();
   FlowLayout flowLayout5 = new FlowLayout();
   JPanel jPanel2 = new JPanel();
   BoxLayout boxLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
   FlowLayout flowLayout4 = new FlowLayout();
   JCheckBox userCB = new JCheckBox();
   JPanel jPanel3 = new JPanel();
   FlowLayout flowLayout6 = new FlowLayout();
   ButtonGroup seasonButtonGroup = new ButtonGroup();
   JRadioButton winterRB = new JRadioButton();
   JRadioButton fallRB = new JRadioButton();
   JRadioButton summerRB = new JRadioButton();
   JRadioButton springRB = new JRadioButton();
  JCheckBox userOnlyCB = new JCheckBox();
  JMenu jMenu1 = new JMenu();
  JMenuItem menuUtilityPick = new JMenuItem();
  JMenuItem menuUtilityAllNorm = new JMenuItem();
  JCheckBox yearCB = new JCheckBox();
/**
 * Primary constructor for Climate Dialog GUI.  Sets the frame ownere, the title, and modality.
 * @param frame
 * @param title
 * @param modal
 */
  public ClimateDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded ClimateDialog constructor.  References the primary constructor and makes a new frame with Climate as teh title and modality to false.
 */
  public ClimateDialog() {
    this(new Frame(), "Climate", false);
  }
  public ClimateDialog(ClimateDialogWrapper wrap, Frame frame, String title, boolean modal) {
    this(frame,title,modal);
    this.wrap = wrap;
  }
/**
 * initializes the layout of main panel, and sets ComboBoxes for User Only, Year, JMenu items, next and previous buttons
 * and a JTextField for time step
 * @throws Exception
 */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    userOnlyCB.setText("Show User Only");
    userOnlyCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        userOnlyCB_actionPerformed(e);
      }
    });
    jMenu1.setText("Utility");
    menuUtilityPick.setText("Pick Non-User Using Probabilities");
    menuUtilityPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityPick_actionPerformed(e);
      }
    });
    menuUtilityAllNorm.setText("Set Non-User to Normal");
    menuUtilityAllNorm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuUtilityAllNorm_actionPerformed(e);
      }
    });
    userCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        userCB_actionPerformed(e);
      }
    });
    yearCB.setText("Year");
    yearCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        yearCB_actionPerformed(e);
      }
    });
    getContentPane().add(mainPanel);
    topPanel.setLayout(borderLayout2);
    prevNextPanel.setLayout(flowLayout1);
    timeStepPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(2);
    nextPB.setIcon(new ImageIcon(simpplle.gui.ClimateDialog.class.getResource("images/next.gif")));
    nextPB.setMargin(new Insets(0, 0, 0, 0));
    nextPB.setPressedIcon(new ImageIcon(simpplle.gui.ClimateDialog.class.getResource("images/nextg.gif")));
    nextPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nextPB_actionPerformed(e);
      }
    });
    prevPB.setIcon(new ImageIcon(simpplle.gui.ClimateDialog.class.getResource("images/prev.gif")));
    prevPB.setMargin(new Insets(0, 0, 0, 0));
    prevPB.setPressedIcon(new ImageIcon(simpplle.gui.ClimateDialog.class.getResource("images/prevg.gif")));
    prevPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prevPB_actionPerformed(e);
      }
    });
    timeStepEdit.setBackground(Color.white);
    timeStepEdit.setToolTipText("Type a time step number and press enter");
    timeStepEdit.setSelectionColor(Color.blue);
    timeStepEdit.setColumns(5);
    timeStepEdit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        timeStepEdit_actionPerformed(e);
      }
    });
    temperaturePanel.setLayout(gridLayout4);
    moisturePanel.setLayout(gridLayout5);
    centerPanel.setLayout(boxLayout);
    temperatureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    temperatureLabel.setText("Temperature");
    moistureLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    moistureLabel.setText("Moisture");
    timeStepLabel.setText("Time Step");
    southPanel.setLayout(flowLayout3);
    buttonPanel.setLayout(flowLayout2);
    this.setJMenuBar(menuBarClimate);
    menuFile.setText("File");
    menuTimeSteps.setText("Time Steps");
    menuFileOpen.setEnabled(true);
    menuFileOpen.setText("Open");
    menuFileOpen.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileOpen_actionPerformed(e);
      }
    });
    menuFileSave.setEnabled(false);
    menuFileSave.setText("Save");
    menuFileSave.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSave_actionPerformed(e);
      }
    });
    menuFileSaveAs.setEnabled(false);
    menuFileSaveAs.setText("Save As");
    menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileSaveAs_actionPerformed(e);
      }
    });
    menuFileClose.setEnabled(false);
    menuFileClose.setText("Close");
    menuFileClose.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileClose_actionPerformed(e);
      }
    });
    menuFileQuit.setEnabled(true);
    menuFileQuit.setText("Close Dialog");
    menuFileQuit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuFileQuit_actionPerformed(e);
      }
    });
    menuTimeStepsDeleteCurrent.setEnabled(false);
    menuTimeStepsDeleteCurrent.setText("Delete Current");
    menuTimeStepsDeleteCurrent.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuTimeStepsDeleteCurrent_actionPerformed(e);
      }
    });
    menuTimeStepsDeleteAll.setText("Delete All");
    menuTimeStepsDeleteAll.setActionCommand("Delete All ...");
    menuTimeStepsDeleteAll.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        menuTimeStepsDeleteAll_actionPerformed(e);
      }
    });
    menuFileOldFormat.setText("Import Old Format File ");
    menuFileOldFormat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuFileOldFormat_actionPerformed(e);
      }
    });
    menuBarClimate.add(menuFile);
    menuBarClimate.add(menuTimeSteps);
    menuBarClimate.add(jMenu1);
    menuFile.add(menuFileOpen);
    menuFile.add(menuFileClose);
    menuFile.addSeparator();
    menuFile.add(menuFileOldFormat);
    menuFile.addSeparator();
    menuFile.add(menuFileSave);
    menuFile.add(menuFileSaveAs);
    menuFile.addSeparator();
    menuFile.add(menuFileQuit);
    menuTimeSteps.add(menuTimeStepsDeleteCurrent);
    menuTimeSteps.add(menuTimeStepsDeleteAll);
    temperatureCB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        temperatureCB_actionPerformed(e);
      }
    });
    moistureCB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        moistureCB_actionPerformed(e);
      }
    });
    gridLayout4.setRows(2);
    gridLayout4.setVgap(10);
    gridLayout5.setRows(2);
    gridLayout5.setVgap(5);
    flowLayout2.setVgap(0);
    explanationPB.setText("Explanation");
    explanationPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        explanationPB_actionPerformed(e);
      }
    });
    closePB.setText("Close");
    closePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        closePB_actionPerformed(e);
      }
    });
    southPanel.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setLayout(flowLayout5);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    jPanel2.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    userCB.setText("User Time Step");
    userCB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        userCB_actionPerformed(e);
      }
    });
    winterRB.setText("Winter");
    winterRB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        winterRB_actionPerformed(e);
      }
    });
    fallRB.setText("Fall");
    fallRB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fallRB_actionPerformed(e);
      }
    });
    summerRB.setText("Summer");
    summerRB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        summerRB_actionPerformed(e);
      }
    });
    springRB.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    springRB.setText("Spring");
    springRB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        springRB_actionPerformed(e);
      }
    });
    mainPanel.add(topPanel, BorderLayout.NORTH);
    topPanel.add(prevNextPanel, BorderLayout.NORTH);
    prevNextPanel.add(prevPB, null);
    prevNextPanel.add(timeStepPanel, null);
    timeStepPanel.add(timeStepLabel, null);
    timeStepPanel.add(timeStepEdit, null);
    prevNextPanel.add(nextPB, null);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    jPanel3.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.CENTER);
    jPanel3.setBorder(BorderFactory.createLoweredBevelBorder());
    centerPanel.add(jPanel3);
    jPanel3.add(yearCB);
    jPanel3.add(springRB, null);
    jPanel3.add(summerRB, null);
    jPanel3.add(fallRB, null);
    jPanel3.add(winterRB, null);
    centerPanel.add(jPanel2);
    jPanel2.add(userOnlyCB);
    jPanel2.add(userCB);
    centerPanel.add(jPanel1);
    jPanel1.add(temperaturePanel);
    jPanel1.add(moisturePanel);
    temperaturePanel.add(temperatureLabel, null);
    temperaturePanel.add(moistureLabel);
    moisturePanel.add(temperatureCB, null);
    moisturePanel.add(moistureCB, null);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    southPanel.add(buttonPanel, null);
    buttonPanel.add(closePB, null);
    buttonPanel.add(explanationPB, null);
    seasonButtonGroup.add(springRB);
    seasonButtonGroup.add(summerRB);
    seasonButtonGroup.add(fallRB);
    seasonButtonGroup.add(winterRB);
    jMenu1.add(menuUtilityPick);
    jMenu1.add(menuUtilityAllNorm);
  }


  void prevPB_actionPerformed(ActionEvent e) {
    wrap.prevPB_actionPerformed(e);
  }

  void nextPB_actionPerformed(ActionEvent e) {
    wrap.nextPB_actionPerformed(e);
  }

  void timeStepEdit_actionPerformed(ActionEvent e) {
    wrap.timeStepEdit_actionPerformed(e);
  }

  void temperatureCB_actionPerformed(ActionEvent e) {
    wrap.temperatureCB_actionPerformed(e);
  }

  void moistureCB_actionPerformed(ActionEvent e) {
    wrap.moistureCB_actionPerformed(e);
  }

  public void yearCB_actionPerformed(ActionEvent e) {
    wrap.yearCB_actionPerformed(e);
  }
  public void springRB_actionPerformed(ActionEvent e) {
    wrap.seasonChanged(e,simpplle.comcode.Climate.Season.SPRING);
  }
  public void summerRB_actionPerformed(ActionEvent e) {
    wrap.seasonChanged(e,simpplle.comcode.Climate.Season.SUMMER);
  }
  public void fallRB_actionPerformed(ActionEvent e) {
    wrap.seasonChanged(e,simpplle.comcode.Climate.Season.FALL);
  }
  public void winterRB_actionPerformed(ActionEvent e) {
    wrap.seasonChanged(e,simpplle.comcode.Climate.Season.WINTER);
  }

  public void userCB_actionPerformed(ActionEvent e) {
    wrap.userCB_actionPerformed(e);
  }
  public void userOnlyCB_actionPerformed(ActionEvent e) {
    wrap.userOnlyCB_actionPerformed(e);
  }

  void closePB_actionPerformed(ActionEvent e) {
    wrap.closePB_actionPerformed(e);
  }


  void menuFileOpen_actionPerformed(ActionEvent e) {
    wrap.menuFileOpen_actionPerformed(e);
  }

  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    wrap.menuFileOldFormat_actionPerformed(e);
  }

  void menuFileSave_actionPerformed(ActionEvent e) {
    wrap.menuFileSave_actionPerformed(e);
  }

  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    wrap.menuFileSaveAs_actionPerformed(e);
  }

  void menuFileClose_actionPerformed(ActionEvent e) {
    wrap.menuFileClose_actionPerformed(e);
  }

  void menuFileQuit_actionPerformed(ActionEvent e) {
    wrap.menuFileQuit_actionPerformed(e);
  }

  void menuTimeStepsDeleteCurrent_actionPerformed(ActionEvent e) {
    wrap.menuTimeStepsDeleteCurrent_actionPerformed(e);
  }

  void menuTimeStepsDeleteAll_actionPerformed(ActionEvent e) {
    wrap.menuTimeStepsDeleteAll_actionPerformed(e);
  }

  void explanationPB_actionPerformed(ActionEvent e) {
    wrap.explanationPB_actionPerformed(e);
  }

  public void menuUtilityPick_actionPerformed(ActionEvent e) {
    wrap.menuUtilityPick_actionPerformed(e);
  }

  public void menuUtilityAllNorm_actionPerformed(ActionEvent e) {
    wrap.menuUtilityAllNorm_actionPerformed(e);
  }


}
