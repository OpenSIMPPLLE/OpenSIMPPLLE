package simpplle.gui;

import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.borland.jbcl.layout.VerticalFlowLayout;
import static simpplle.comcode.Climate.Season;
import simpplle.comcode.SystemKnowledge;
import simpplle.comcode.SimpplleError;
import simpplle.comcode.Climate;
import simpplle.comcode.Simpplle;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>Description:
 * At the time this class was designed JBuilder did not support the
 * new features of Java 1.5.  In the case of this class the variable
 * currentSeason of type simpplle.comcode.Climate.Season which is an
 * enum caused the designer problems.
 * As a result this wrapper class was created so that the class that
 * the designer manipulated will function in the designer.  That
 * classes event handlers simpler refer to this one.
 *       
 * Note: Graphics are updated by updating ClimateDialog's graphics.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class ClimateDialogWrapper {
  ClimateDialog dlg;

  private Climate climate;
  private Season currentSeason;
  private boolean userOnly=false;

  private int     cTime=1;
  private Climate.Temperature defaultTemperature;
  private Climate.Moisture    defaultMoisture;
  private boolean userAction=false;
/**
 * Constructor for ClimateDialogWrapper.  Creates a new instance of Climate dialog and sets it to this frame.  
 * Then initializes.  
 * @param frame
 * @param title
 * @param modal
 */
  public ClimateDialogWrapper(Frame frame, String title, boolean modal) {
    dlg = new ClimateDialog(this,frame,title,modal);
    initialize();
  }
/**
 * Gets the climate dialog.
 * @return the climate dialog.  
 */
  public ClimateDialog getDialog() { return dlg; }
/**
 * Sets the current season to year, climate, temperature and moisture to defaults, and time step to 1 (meaning initial).  
 * Adds the temperature to combo boxes, moistures to combo boxes, and gets the climate and season at time step 1, sets the time step
 * text to empty, if is wyoming, creates a separate set of combo boxes, then updates dialog.  
 */
  private void initialize() {
    userAction         = false;

    currentSeason = Season.YEAR;
    userOnly      = false;

    Climate.Temperature[] allTemperatures = Climate.getAllTemperatures();
    Climate.Moisture[] allMoisture        = Climate.getAllMoisture();
    int      i;

    climate            = simpplle.comcode.Simpplle.getClimate();
    defaultTemperature = Climate.getDefaultTemperature();
    defaultMoisture    = Climate.getDefaultMoisture();
    cTime              = 1;

    for(i=0;i<allTemperatures.length;i++) {
      dlg.temperatureCB.addItem(allTemperatures[i]);
    }

    for(i=0;i<allMoisture.length;i++) {
      dlg.moistureCB.addItem(allMoisture[i]);
    }

    if (climate.userScheduleClimateExists()) {
      cTime = climate.getFirstTimeStep();
      currentSeason = climate.getFirstUserSeason(cTime);
      dlg.userOnlyCB.setEnabled(true);
      dlg.userOnlyCB.setSelected(true);
    }
    else {
      cTime = 1;
      dlg.userOnlyCB.setSelected(false);
      dlg.userOnlyCB.setEnabled(false);
    }

    dlg.timeStepEdit.setText("");

    boolean isWyoming = RegionalZone.isWyoming();
    dlg.yearCB.setVisible(isWyoming);
    dlg.springRB.setVisible(isWyoming);
    dlg.summerRB.setVisible(isWyoming);
    dlg.fallRB.setVisible(isWyoming);https://www.healthcare.gov/
    dlg.winterRB.setVisible(isWyoming);

    if (isWyoming == false) { currentSeason = Season.YEAR; }

    updateDialog();
    userAction = true;
  }
/**
 * Gets the climate based on user current time step, season.  If year combo box is not selected, enables the radiobuttons for
 * seasons, if user climate exists allows user access to user only combo box, then enables previous, next, buttons, 
 * temperature, moisture combo boxes menu trop downs for save, close, delete current time steps, delete all time steps.  
 */
  private void updateDialog() {
    boolean userClimate = climate.isUserClimate(cTime,currentSeason);
    updateSeason();

    boolean isYearSelected = dlg.yearCB.isSelected();
    dlg.springRB.setEnabled(!isYearSelected);
    dlg.summerRB.setEnabled(!isYearSelected);
    dlg.fallRB.setEnabled(!isYearSelected);
    dlg.winterRB.setEnabled(!isYearSelected);

    dlg.userCB.setSelected(userClimate);

    boolean userClimateExists = climate.userScheduleClimateExists();
    dlg.userOnlyCB.setEnabled(userClimateExists);
    if (userClimateExists == false) {
      dlg.userOnlyCB.setSelected(false);
      userOnly = false;
    }

 //   if (cTime != NO_TIME) {
      dlg.timeStepEdit.setText(Integer.toString(cTime));
      // Enable controls
      dlg.prevPB.setEnabled(true);
      dlg.nextPB.setEnabled(true);
      dlg.temperatureCB.setEnabled(true);
      dlg.moistureCB.setEnabled(true);
      dlg.menuFileSaveAs.setEnabled(true);
      dlg.menuTimeStepsDeleteCurrent.setEnabled(userClimate);
      dlg.menuTimeStepsDeleteAll.setEnabled(true);

      if (SystemKnowledge.getFile(SystemKnowledge.CLIMATE) != null) {
        dlg.menuFileSave.setEnabled(true);
        dlg.menuFileClose.setEnabled(true);
      }

      dlg.menuTimeStepsDeleteCurrent.setEnabled(userClimate);

      // Necessary because ActionEvent gets triggered.
      // This in turn causes climate to say it has been changed
      // which is not correct.
      userAction = false;
      dlg.temperatureCB.setSelectedItem(climate.getTemperature(cTime,currentSeason));
      dlg.moistureCB.setSelectedItem(climate.getMoisture(cTime,currentSeason));
      userAction = true;

//    }
 //   else {
  //    prevPB.setEnabled(false);
 //     nextPB.setEnabled(false);
 //     temperatureCB.setEnabled(false);
 //     moistureCB.setEnabled(false);
 //     menuFileSave.setEnabled(false);
 //     menuFileSaveAs.setEnabled(false);
 //     menuFileClose.setEnabled(false);
 //     menuTimeStepsDeleteCurrent.setEnabled(false);
 //     menuTimeStepsDeleteAll.setEnabled(false);
 //     temperatureCB.setSelectedIndex(simpplle.comcode.Climate.NORMAL);
 //     moistureCB.setSelectedIndex(simpplle.comcode.Climate.NORMAL);
 //   }
    dlg.update(dlg.getGraphics());
  }

/**
 * Sets the selected radio button for season depending on current season  
 */
  private void updateSeason() {
    userAction = false;
    switch (currentSeason) {
      case SUMMER: dlg.summerRB.setSelected(true); break;
      case SPRING: dlg.springRB.setSelected(true); break;
      case FALL:   dlg.fallRB.setSelected(true); break;
      case WINTER: dlg.winterRB.setSelected(true); break;
      case YEAR:   dlg.yearCB.setSelected(true); break;
    }
    if (currentSeason != Season.YEAR) {
      dlg.yearCB.setSelected(false);
    }
    userAction = true;
  }
  /**
   * if user only climate is true and year combo box is selected, gets the previous user season based on the current time step and current season
   * then sets current season to the previous, and updates dialog.  
   * @param e 'previous' button pushed
   */
  void prevPB_actionPerformed(ActionEvent e) {
    if (userOnly && dlg.yearCB.isSelected() == false) {
      Season newSeason = climate.getPrevUserSeason(cTime,currentSeason);
      if (newSeason != null) {
        currentSeason = newSeason;
        updateDialog();
        return;
      }
    }


    cTime = climate.getPrevTimeStep(cTime,userOnly);
    boolean userClimate = climate.isUserClimate(cTime,currentSeason);

    if (userOnly || userClimate) {
      currentSeason = climate.getFirstUserSeason(cTime);
    }
    else {
      currentSeason = Season.YEAR;
    }
    updateDialog();
  }
/**
 * If next button is pushed, sets the current season to the next user season, which was calculated using current time and current season.  
 * 
 * @param e 'Next'
 */
  void nextPB_actionPerformed(ActionEvent e) {
    if (userOnly && dlg.yearCB.isSelected() == false) {
      Season newSeason = climate.getNextUserSeason(cTime,currentSeason);
      if (newSeason != null) {
        currentSeason = newSeason;
        updateDialog();
        return;
      }
    }

    cTime = climate.getNextTimeStep(cTime,userOnly);
    boolean userClimate = climate.isUserClimate(cTime,currentSeason);

    if (userOnly || userClimate) {
      currentSeason = climate.getFirstUserSeason(cTime);
    }
    else {
      currentSeason = Season.YEAR;
    }

    updateDialog();
  }
  /**
   * If user Only combo box is selected, sets user only to true, gets the first step, and sets the current time to the first time step, unless that is -1
   * sets the current season to first time step season.  
   * @param e
   */
  public void userOnlyCB_actionPerformed(ActionEvent e) {
    userOnly = dlg.userOnlyCB.isSelected();
    if (!userOnly || climate.isUserClimate(cTime, currentSeason)) {
      updateDialog();
      return;
    }

    int newTime = climate.getFirstTimeStep();
    if (newTime == -1) { return; }
    cTime = newTime;
    currentSeason = climate.getFirstTimeStepSeason();

    updateDialog();
  }
/**
 * Gets the time step input in the time step text field, if input time step is < 1 or above maximum allowed throws an exception,
 * sets the current time step to input time step, and updates Jdialog.
 * @param e time step entered in text field
 */
  void timeStepEdit_actionPerformed(ActionEvent e) {
    int tStep;
    String msg;
    int choice;

    try {
      tStep = Integer.parseInt(dlg.timeStepEdit.getText());
      if (tStep < 1 || tStep > Simulation.MAX_TIME_STEPS) {
        throw new NumberFormatException();
      }
      cTime = tStep;
      updateDialog();
    }
    catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(dlg,
                                    "Time Step must be a number (1-)" +
                                    Simulation.MAX_TIME_STEPS + ")",
                                    "Invalid Time Step",
                                    JOptionPane.ERROR_MESSAGE);
      dlg.timeStepEdit.setText(Integer.toString(cTime));
    }
  }
/**
 * If temperature combo box, sets the temperature to current time, current season.  
 * @param e temperature combo box selected
 */
  void temperatureCB_actionPerformed(ActionEvent e) {
    if (!userAction) { return; }

    Climate.Temperature item = (Climate.Temperature) dlg.temperatureCB.getSelectedItem();
    if (item == null) {
      return;
    }

    climate.setTemperature(cTime,currentSeason,item);
    if (currentSeason == Season.YEAR) {
      climate.setTemperature(cTime,Season.SPRING,item);
      climate.setTemperature(cTime,Season.SUMMER,item);
      climate.setTemperature(cTime,Season.FALL,item);
      climate.setTemperature(cTime,Season.WINTER,item);
    }
    dlg.userCB.setSelected(true);
    climate.addClimate(cTime, currentSeason);
    updateDialog();
  }
  /**
   * If moisture combo box selected, creates moisture items, then sets the current moisture according to 
   * current time step, current season and input moisture.   
   * @param e
   */

  void moistureCB_actionPerformed(ActionEvent e) {
    if (!userAction) { return; }

    Climate.Moisture item = (Climate.Moisture) dlg.moistureCB.getSelectedItem();
    if (item == null) {
      return;
    }

    climate.setMoisture(cTime,currentSeason,item);
    if (currentSeason == Season.YEAR) {
      climate.setMoisture(cTime,Season.SPRING,item);
      climate.setMoisture(cTime,Season.SUMMER,item);
      climate.setMoisture(cTime,Season.FALL,item);
      climate.setMoisture(cTime,Season.WINTER,item);
    }
    dlg.userCB.setSelected(true);
    climate.addClimate(cTime, currentSeason);
    updateDialog();
  }
/**
 * Updates the current season to a specified season.  
 * @param e 
 * @param season the season to be updated.  
 */
  public void seasonChanged(ActionEvent e, Season season) {
    if (!userAction) { return; }

    if (currentSeason != season) {
      currentSeason = season;
      updateDialog();
    }
  }
  /**
   * If year Combo box is choosen, sets the moisture, temperature to current, the current season to year, if not selected... current season is set to spring. 
   * @param e combo box year item selected
   */
  public void yearCB_actionPerformed(ActionEvent e) {
    if (!userAction) { return; }

    if (dlg.yearCB.isSelected()) {
      Climate.Moisture    moisture = climate.getMoisture(cTime,currentSeason);
      Climate.Temperature temp     = climate.getTemperature(cTime,currentSeason);
      currentSeason = Season.YEAR;
      climate.addClimate(cTime, currentSeason);
      climate.setMoisture(cTime,currentSeason,moisture);
      climate.setTemperature(cTime,currentSeason,temp);
    }
    else {
      climate.removeClimate(cTime,Season.YEAR);
      currentSeason = Season.SPRING;
    }
    updateDialog();
  }
  /**
   * If user combo box is selected, sets the climate to current time and current season, else if current season is year 
   * removes all climates for current time, else removes seasons for both current time and current season.  
   * @param e
   */
  public void userCB_actionPerformed(ActionEvent e) {
    if (dlg.userCB.isSelected()) {
      climate.addClimate(cTime, currentSeason);
    }
    else {
      if (currentSeason == Season.YEAR) {
        climate.removeAllClimate(cTime);
      }
      else {
        climate.removeClimate(cTime, currentSeason);
      }
    }
    updateDialog();
  }
  /**
   * Randomizes the climate.
   * @param e
   */
  public void menuUtilityPick_actionPerformed(ActionEvent e) {
    climate.randomizeClimate();
    updateDialog();
  }
/**
 * Sets normal climate to the system normal climate - as in not input by user.  
 * @param e
 */
  public void menuUtilityAllNorm_actionPerformed(ActionEvent e) {
    climate.allNonUserNormal();
    updateDialog();
  }

  /**
   * If close button is pushed
   * @param e "close"
   */
  void closePB_actionPerformed(ActionEvent e) {
    quit();
  }

/**
 * Opens climate file for system.  Sets the current time to first time step, and current season to year
 * @param e 'open file'
 */
  void menuFileOpen_actionPerformed(ActionEvent e) {
    if (SystemKnowledgeFiler.openFile(dlg, SystemKnowledge.CLIMATE,dlg.menuFileSave,dlg.menuFileClose)) {
      cTime = climate.getFirstTimeStep();
      currentSeason = Season.YEAR;
      dlg.yearCB.setSelected(true);
      updateDialog();
    }
    dlg.update(dlg.getGraphics());
  }
/**
 * Uses the old file format.  This will no longer be supported in OpenSimpplle.  
 * @param e 'Old File  Format'
 */
  void menuFileOldFormat_actionPerformed(ActionEvent e) {
    File         inputFile;
    MyFileFilter extFilter;
    String       title = "Select a Climate Data File";

    extFilter = new MyFileFilter("climate",
                                 "Climate Data Files (*.climate)");

    dlg.setCursor(Utility.getWaitCursor());
    inputFile = Utility.getOpenFile(dlg,title,extFilter);

    try {
      if (inputFile != null) {
        climate.loadData(inputFile);
        cTime = climate.getFirstTimeStep();
        currentSeason = Season.YEAR;
        dlg.yearCB.setSelected(true);
        updateDialog();
        dlg.menuFileSave.setEnabled(true);
        dlg.menuFileSaveAs.setEnabled(true);
        dlg.menuFileClose.setEnabled(true);
      }
    }
    catch (SimpplleError ex) {
      JOptionPane.showMessageDialog(dlg,ex.getError(),"Error loading file",
                                    JOptionPane.ERROR_MESSAGE);
    }
    dlg.setCursor(Utility.getNormalCursor());
    dlg.update(dlg.getGraphics());
  }
/**
 * If drop down menu save file choosen, calls save file to the system knowledge climate file.  
 * @param e "save" 
 */
  void menuFileSave_actionPerformed(ActionEvent e) {
    File outfile = SystemKnowledge.getFile(SystemKnowledge.CLIMATE);
    SystemKnowledgeFiler.saveFile(dlg, outfile, SystemKnowledge.CLIMATE,
                                  dlg.menuFileSave, dlg.menuFileClose);
    dlg.update(dlg.getGraphics());
  }
/**
 * Does not use the current System knowledge climate file.  Instead calls the saveFile() with file not specified.  
 * @param e
 */
  void menuFileSaveAs_actionPerformed(ActionEvent e) {
    SystemKnowledgeFiler.saveFile(dlg, SystemKnowledge.CLIMATE, dlg.menuFileSave,
                                  dlg.menuFileClose);
    dlg.update(dlg.getGraphics());
  }
/**
 * If menu item close choosen, prompts user to save changes if there is already a climate file, and the climate has changed.
 * Resets the current time to 1 (initial), closes climate file, and changes current season to YEAR.  
 * @param e
 */
  void menuFileClose_actionPerformed(ActionEvent e) {
    int    choice;
    String msg;

    if (SystemKnowledge.getFile(SystemKnowledge.CLIMATE) != null && climate.hasChanged()) {
      msg = "Changes have been made in the schedule.\n" +
                       "If you continue these changes will be lost.\n\n" +
                       "Do you wish to continue?";
      choice = JOptionPane.showConfirmDialog(dlg,msg,"Close Climate Schedule",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);

      if (choice == JOptionPane.NO_OPTION) {
        dlg.update(dlg.getGraphics());
        return;
      }
    }

    cTime = 1;
    climate.closeFile();
    currentSeason = Season.YEAR;
    dlg.yearCB.setSelected(true);
    updateDialog();
  }
/**
 * If menu item Quit is choosen closes the dialog.
 * @param e 'Quit'
 */
  void menuFileQuit_actionPerformed(ActionEvent e) {
    quit();
  }
/**
 * Quits by setting dialog not visible and disposes. 
 */
  private void quit() {
    dlg.setVisible(false);
    dlg.dispose();
  }
/**
 * 
 * Deletes the current climate according to current time step and current season.
 * @param e
 */
  void menuTimeStepsDeleteCurrent_actionPerformed(ActionEvent e) {
    climate.removeClimate(cTime,currentSeason);
    cTime = climate.getPrevTimeStep(cTime,userOnly);
    updateDialog();
  }
/**
 * Deletes all time steps by removing all climates.  
 * @param e
 */
  void menuTimeStepsDeleteAll_actionPerformed(ActionEvent e) {
    cTime = cTime;
    climate.removeAll();
    updateDialog();
  }
/**
 * Regional climate explanation which will be shown if explanation button is pushed.  
 * @param e
 */
  void explanationPB_actionPerformed(ActionEvent e) {
    String msg =
      "Regional Climate will be linked to actual weather records.\n" +
      "A library of weather data, or access to a web site will be provided.\n" +
      "User can choose from or create a pattern.\n" +
      "The abstraction of \"normal, drier, wetter, warmer, and cooler\n" +
      "can be linked to actual weather data\".";
    String title = "Explanation of Regional Climate";

    JOptionPane.showMessageDialog(simpplle.JSimpplle.getSimpplleMain(),msg,title,
                                  JOptionPane.INFORMATION_MESSAGE);
  }

}
