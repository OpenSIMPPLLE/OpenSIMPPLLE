package simpplle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import simpplle.comcode.HabitatTypeGroup;
import simpplle.comcode.Process;
import simpplle.comcode.ProcessType;
import simpplle.comcode.Species;
import simpplle.comcode.VegetativeType;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class defines Pathways Editor, a type of JDialog. 
 * It allows users to edit a vegetative type next state.  
 * It Contains an inner class to construct a listitem object which sets the process and next state
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class PathwayEditor extends JDialog {
  private Frame theFrame;
  private HabitatTypeGroup pathwayHtGrp;
  private VegetativeType pathwayState;
/**
 * Inner class which defines a list item to be used in Pathway Editor.  
 *
 */
  private static class ListItem {
    public Process        process;
    public VegetativeType nextState;
/**
 * Constructor for 
 * @param process process of this list item
 * @param nextState the next state of the list item.  
 */
    public ListItem(Process process, VegetativeType nextState) {
      this.process   = process;
      this.nextState = nextState;
    }

/**
 * Creates a string of the process and next state.  This is displayed in a text 
 */
    public String toString() {
      return process.toString() + " ---> " + nextState.toString();
    }
  }

  private ListItem editListItem;

  private String protoCellValue =
    "STAND-REPLACING-FIRE ---> XERIC_FS_SHRUBS/CLOSED-TALL-SHRUB10/1";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel northPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel nextStatePanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel nextStateListPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  JScrollPane nextStateListScroll = new JScrollPane();
  JList nextStateList = new JList();
  JPanel editPanel = new JPanel();
  JPanel currentSelectionPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  JPanel VegetativeTypeInfoPanel = new JPanel();
  JPanel agePanel = new JPanel();
  JPanel densityPanel = new JPanel();
  JPanel sizeClassPanel = new JPanel();
  JPanel SpeciesPanel = new JPanel();
  FlowLayout flowLayout5 = new FlowLayout();
  JLabel speciesText = new JLabel();
  JLabel speciesLabel = new JLabel();
  JPanel currentStatePanel = new JPanel();
  JLabel htGrpLabel = new JLabel();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel htGrpPanel = new JPanel();
  JLabel htGrpText = new JLabel();
  FlowLayout flowLayout6 = new FlowLayout();
  JLabel currentStateText = new JLabel();
  JLabel currentStateLabel = new JLabel();
  FlowLayout flowLayout7 = new FlowLayout();
  JLabel sizeClassText = new JLabel();
  JLabel sizeClassLabel = new JLabel();
  FlowLayout flowLayout8 = new FlowLayout();
  FlowLayout flowLayout9 = new FlowLayout();
  JLabel densityText = new JLabel();
  JLabel densityLabel = new JLabel();
  JLabel ageText = new JLabel();
  JLabel ageLabel = new JLabel();
  JPopupMenu listPopupMenu = new JPopupMenu();
  JMenuItem contextMenuEdit = new JMenuItem();
  JMenuItem contextMenuDelete = new JMenuItem();
  TitledBorder titledBorder1;
  JPanel saveEditPanel = new JPanel();
  JPanel nextStateVegTypePanel = new JPanel();
  JButton showListPB = new JButton();
  JLabel nextStateVegTypeLabel = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
  JTextField nextStateVegTypeValue = new JTextField();
  JLabel processLabel = new JLabel();
  FlowLayout flowLayout10 = new FlowLayout();
  JLabel processValue = new JLabel();
  JPanel processPanel = new JPanel();
  FlowLayout flowLayout11 = new FlowLayout();
  JButton saveEditPB = new JButton();
  JPanel processPBPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JButton processAddPB = new JButton();
  JButton processDeletePB = new JButton();
  TitledBorder processPBBorder;
/**
 * Primary constructor for Pathway Editor, takes in the owner frame, title, modality and an additional variable for vegetative type.  
 * @param frame the Jframe owner of the dialog
 * @param title title of dialog
 * @param modal specifies whether dialog blocks user input to other top-level windows when shown
 * @param vegType 
 */
  public PathwayEditor(Frame frame, String title, boolean modal,
                       VegetativeType vegType) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.theFrame = frame;
    this.pathwayState = vegType;
    initialize();
  }
  /**
   * Overloaded Pathway Editor constructor.  Sets the owner to null, the title to empty string, modality to modeless, and vegetative type object to null.  
   */
  public PathwayEditor() {
    this(null, "", false, null);
  }
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Next Vegetative State");
    processPBBorder = new TitledBorder(BorderFactory.createEmptyBorder(),"Processes");
    mainPanel.setLayout(borderLayout1);
    northPanel.setLayout(borderLayout2);
    nextStatePanel.setLayout(borderLayout3);
    nextStateListPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    nextStateList.setFont(new java.awt.Font("Monospaced", 0, 12));
    nextStateList.setToolTipText("Double-Click item to change or Right-Click for Menu");
    nextStateList.setPrototypeCellValue(protoCellValue);
    nextStateList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        nextStateList_mouseClicked(e);
      }
      public void mousePressed(MouseEvent e) {
        nextStateList_mousePressed(e);
      }
      public void mouseReleased(MouseEvent e) {
        nextStateList_mouseReleased(e);
      }
    });
    editPanel.setLayout(flowLayout2);
    currentSelectionPanel.setLayout(new BoxLayout(currentSelectionPanel, BoxLayout.Y_AXIS));
    currentSelectionPanel.setBorder(titledBorder1);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    VegetativeTypeInfoPanel.setLayout(new BoxLayout(VegetativeTypeInfoPanel, BoxLayout.Y_AXIS));
    SpeciesPanel.setLayout(flowLayout5);
    speciesText.setFont(new java.awt.Font("Monospaced", 0, 12));
    speciesText.setForeground(Color.blue);
    speciesText.setText("XERIC-FS-SHRUBS");
    speciesLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    speciesLabel.setText("Species           ");
    flowLayout5.setAlignment(FlowLayout.LEFT);
    htGrpLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    htGrpLabel.setText("Ecological Grouping");
    flowLayout4.setAlignment(FlowLayout.LEFT);
    htGrpPanel.setLayout(flowLayout4);
    htGrpText.setFont(new java.awt.Font("Monospaced", 0, 12));
    htGrpText.setForeground(Color.blue);
    htGrpText.setText("A1");
    currentStatePanel.setLayout(flowLayout6);
    flowLayout6.setAlignment(FlowLayout.LEFT);
    currentStateText.setFont(new java.awt.Font("Monospaced", 0, 12));
    currentStateText.setForeground(Color.blue);
    currentStateText.setText("XERIC-FS-SHRUBS/CLOSED-TALL-SHRUB10/1");
    currentStateLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    currentStateLabel.setToolTipText("");
    currentStateLabel.setText("Vegetative Type   ");
    sizeClassPanel.setLayout(flowLayout7);
    sizeClassText.setFont(new java.awt.Font("Monospaced", 0, 12));
    sizeClassText.setForeground(Color.blue);
    sizeClassText.setText("CLOSED-TALL-SHRUB");
    sizeClassLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    sizeClassLabel.setText("Size Class        ");
    densityPanel.setLayout(flowLayout8);
    agePanel.setLayout(flowLayout9);
    densityText.setFont(new java.awt.Font("Monospaced", 0, 12));
    densityText.setForeground(Color.blue);
    densityText.setText("1");
    densityLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    densityLabel.setText("Density           ");
    ageText.setFont(new java.awt.Font("Monospaced", 0, 12));
    ageText.setForeground(Color.blue);
    ageText.setText("10");
    ageLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    ageLabel.setText("Age               ");
    flowLayout7.setAlignment(FlowLayout.LEFT);
    flowLayout8.setAlignment(FlowLayout.LEFT);
    flowLayout9.setAlignment(FlowLayout.LEFT);
    VegetativeTypeInfoPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    contextMenuEdit.setText("Edit");
    contextMenuEdit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        contextMenuEdit_actionPerformed(e);
      }
    });
    contextMenuDelete.setText("Delete");
    contextMenuDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        contextMenuDelete_actionPerformed(e);
      }
    });
    listPopupMenu.setInvoker(nextStateList);
    titledBorder1.setTitleFont(new java.awt.Font("Dialog", 2, 12));
    nextStateVegTypePanel.setLayout(flowLayout1);
    showListPB.setEnabled(false);
    showListPB.setMargin(new Insets(0, 0, 0, 0));
    showListPB.setText("Show List");
    showListPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showListPB_actionPerformed(e);
      }
    });
    nextStateVegTypeLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    nextStateVegTypeLabel.setText("Vegetative Type");
    nextStateVegTypeValue.setEnabled(false);
    nextStateVegTypeValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    nextStateVegTypeValue.setText("XERIC-FS-SHRUBS/CLOSED-TALL-SHRUB10/1");
    nextStateVegTypeValue.setColumns(40);
    nextStateVegTypeValue.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        nextStateVegTypeValue_keyTyped(e);
      }
    });
    nextStateVegTypeValue.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextStateVegTypeValue_actionPerformed(e);
      }
    });
    processLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    processLabel.setText("Process        ");
    flowLayout10.setAlignment(FlowLayout.LEFT);
    processValue.setFont(new java.awt.Font("Monospaced", 0, 12));
    processValue.setForeground(Color.blue);
    processValue.setToolTipText("");
    processValue.setText("STAND-REPLACING-FIRE");
    processPanel.setLayout(flowLayout10);
    saveEditPanel.setLayout(flowLayout11);
    saveEditPB.setEnabled(false);
    saveEditPB.setMargin(new Insets(0, 0, 0, 0));
    saveEditPB.setText("Save Edits");
    saveEditPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveEditPB_actionPerformed(e);
      }
    });
    flowLayout11.setAlignment(FlowLayout.LEFT);
    saveEditPanel.setToolTipText("");
    processPBPanel.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    gridLayout1.setVgap(5);
    processAddPB.setText("Add");
    processAddPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        processAddPB_actionPerformed(e);
      }
    });
    processDeletePB.setEnabled(false);
    processDeletePB.setText("Delete Selected");
    processDeletePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        processDeletePB_actionPerformed(e);
      }
    });
    processPBPanel.setBorder(processPBBorder);
    processPBBorder.setTitleFont(new java.awt.Font("Monospaced", 1, 12));
    getContentPane().add(mainPanel);
    mainPanel.add(northPanel, BorderLayout.NORTH);
    northPanel.add(VegetativeTypeInfoPanel, BorderLayout.WEST);
    VegetativeTypeInfoPanel.add(htGrpPanel, null);
    htGrpPanel.add(htGrpLabel, null);
    htGrpPanel.add(htGrpText, null);
    VegetativeTypeInfoPanel.add(currentStatePanel, null);
    currentStatePanel.add(currentStateLabel, null);
    currentStatePanel.add(currentStateText, null);
    VegetativeTypeInfoPanel.add(SpeciesPanel, null);
    SpeciesPanel.add(speciesLabel, null);
    SpeciesPanel.add(speciesText, null);
    VegetativeTypeInfoPanel.add(sizeClassPanel, null);
    sizeClassPanel.add(sizeClassLabel, null);
    sizeClassPanel.add(sizeClassText, null);
    VegetativeTypeInfoPanel.add(densityPanel, null);
    densityPanel.add(densityLabel, null);
    densityPanel.add(densityText, null);
    VegetativeTypeInfoPanel.add(agePanel, null);
    agePanel.add(ageLabel, null);
    agePanel.add(ageText, null);
    mainPanel.add(nextStatePanel, BorderLayout.CENTER);
    nextStatePanel.add(nextStateListPanel, BorderLayout.CENTER);
    nextStateListPanel.add(nextStateListScroll, null);
    nextStateListPanel.add(processPBPanel, null);
    processPBPanel.add(processAddPB, null);
    processPBPanel.add(processDeletePB, null);
    nextStatePanel.add(editPanel, BorderLayout.NORTH);
    editPanel.add(currentSelectionPanel, null);
    currentSelectionPanel.add(processPanel, null);
    processPanel.add(processLabel, null);
    processPanel.add(processValue, null);
    currentSelectionPanel.add(nextStateVegTypePanel, null);
    nextStateVegTypePanel.add(nextStateVegTypeLabel, null);
    nextStateVegTypePanel.add(nextStateVegTypeValue, null);
    nextStateVegTypePanel.add(showListPB, null);
    currentSelectionPanel.add(saveEditPanel, null);
    saveEditPanel.add(saveEditPB, null);
    nextStateListScroll.getViewport().add(nextStateList, null);
    listPopupMenu.add(contextMenuEdit);
    listPopupMenu.add(contextMenuDelete);
  }
/**
 * Initializes the Pathway Editor.
 */
  private void initialize() {
    updateDialog();
    processValue.setText("");
    nextStateVegTypeValue.setText("");
  }

  private void updateDialog() {
    setPathwayStateInfo();
    fillNextStateList();
  }
/**
 * Sets the pertinent Pathway state info.  These are the Habitat group, vegetative type current state, current state, species, size class, density (canopy coverage), and age.  
 * 
 */
  private void setPathwayStateInfo() {
    pathwayHtGrp = pathwayState.getHtGrp();

    htGrpText.setText(pathwayHtGrp.toString());
    currentStateText.setText(pathwayState.getCurrentState());
    speciesText.setText(pathwayState.getSpecies().toString());
    sizeClassText.setText(pathwayState.getSizeClass().toString());
    densityText.setText(pathwayState.getDensity().toString());
    ageText.setText(Integer.toString(pathwayState.getAge()));
  }
/**
 * Gets all the processes in the vegetative type, and creates a list item array of all the processes.  Then gets the next states of the processes
 * and creates a listItem array of those
 * Pathway states are required to hava at least one process next state.  Therefore the processes list will never be empty.  
 */
  private void fillNextStateList() {

    Process[]      processes = pathwayState.getProcesses();
    ListItem[]     listItems = new ListItem[processes.length];
    VegetativeType nextState;

    for(int i=0; i<processes.length; i++) {
      nextState   = pathwayState.getProcessNextState(processes[i]);
      listItems[i] = new ListItem(processes[i],nextState);
    }
    nextStateList.setListData(listItems);
  }
/**
 * Edits the next step by creating a temporary listitem to hold a selected list item, enables the Show List button and next state veg type value text field which allows 
 * users to edit the next state.   This is how the user can select a process --> next state to edit.  
 */
  private void editNextState() {
    editListItem = (ListItem) nextStateList.getSelectedValue();
    if (editListItem == null) { return; }

    showListPB.setEnabled(true);
    nextStateVegTypeValue.setEnabled(true);

    processValue.setText(editListItem.process.toString());
    nextStateVegTypeValue.setText(editListItem.nextState.toString());
  }
/**
 * Saves the edits by creating a new vegetative type from the user entered text (made uppercase), edits the list items array with the new vegetative type
 * then updates the dialog.   
 */
  private void saveEdits() {
    VegetativeType newNextState =
       pathwayHtGrp.getVegetativeType(nextStateVegTypeValue.getText().toUpperCase());

    if (newNextState == null) {
      JOptionPane.showMessageDialog(this,"Invalid Next State",
                                    "Invalid Next State",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    editListItem.nextState = newNextState;
    pathwayState.setProcessNextState(editListItem.process,newNextState);

    processValue.setText("");
    nextStateVegTypeValue.setText("");
    showListPB.setEnabled(false);
    saveEditPB.setEnabled(false);
    nextStateVegTypeValue.setEnabled(false);
    editListItem = null;

    update(getGraphics());
  }
/**
 * Handles the double mouse click in the next state list.  Allows users to choose a next state to edit.  
 * @param e
 */
  void nextStateList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2 && nextStateList.isSelectionEmpty() == false) {
      editNextState();
    }
  }
/**
 * Makes sure user wants to edit the next state, then saves the edits if user is positive of changes.  
 * @param e
 */
  void nextStateVegTypeValue_actionPerformed(ActionEvent e) {
    String msg = "Are you sure you want to save edits?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Save Edits",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      saveEdits();
    }
  }

/**
 * Saves the edited next vegetative state when 'Save Edits' button pressed.
 * @param e 'Save Edits'
 */
  void saveEditPB_actionPerformed(ActionEvent e) {
    saveEdits();
  }
/**
 * Edits the context menu to   
 * @param e
 */
  void contextMenuEdit_actionPerformed(ActionEvent e) {
    editNextState();
  }
/**
 * 
 * @param e
 */
  void contextMenuDelete_actionPerformed(ActionEvent e) {
    String msg;

    ListItem item = (ListItem) nextStateList.getSelectedValue();
    if (item.process.toString().equals("SUCCESSION")) {
      msg = "Deleting the process SUCCESSION is not allowed";
      JOptionPane.showMessageDialog(this,"Illegal selection",msg,
                                    JOptionPane.ERROR_MESSAGE);
      nextStateList.clearSelection();
      return;
    }

    msg = "Are you sure you want to delete the selected " +
                 "Process Next State?";
    int choice = JOptionPane.showConfirmDialog(this,msg,"Delete Selected Item",
                                               JOptionPane.YES_NO_OPTION,
                                               JOptionPane.QUESTION_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      pathwayState.removeProcessNextState(item.process);
      updateDialog();
    }
    nextStateList.clearSelection();
  }
/**
 * Displays a list popup menu if next state is selected.  Menu will popup at the x and y coordinates of component that invoked it.  
 * @param e
 */
  public void maybePopupMenu(MouseEvent e) {
    if (e.isPopupTrigger() && nextStateList.isSelectionEmpty() == false) {
      listPopupMenu.show(e.getComponent(),e.getX(),e.getY());
    }
  }
  /**
   * Handles mouse press in in next state list.  This will give the user the ability to delete a process if a next state is selected.  
   * @param e
   */
  void nextStateList_mousePressed(MouseEvent e) {
    processDeletePB.setEnabled((nextStateList.isSelectionEmpty() == false));
    maybePopupMenu(e);
  }

  void nextStateList_mouseReleased(MouseEvent e) {
    processDeletePB.setEnabled((nextStateList.isSelectionEmpty() == false));
    maybePopupMenu(e);
  }
/**
 * If a next state vegetative type is typed into the next state vegetative type text field, enables the saveEdit buton.  
 * @param e
 */
  void nextStateVegTypeValue_keyTyped(KeyEvent e) {
    saveEditPB.setEnabled(true);
  }
/**
 * Once the process --> next state is selected, if the user pushes the 'Show List' button makes the vegetative type chooser.  This creates a new instance of vegetative type chooserwhich allows user to 
 * see a list of vegetative types (species/size class/density) to choose from. 
 * @param e
 */
  void showListPB_actionPerformed(ActionEvent e) {
    String  title   = "Vegetative Type Chooser";
    Species species = editListItem.nextState.getSpecies();
    VegetativeTypeChooser dlg =
      new VegetativeTypeChooser(this,title,true,pathwayHtGrp,species);

    dlg.setVisible(true);

    VegetativeType selection = dlg.getSelection();
    if (selection != null) {
      nextStateVegTypeValue.setText(selection.toString());
      saveEditPB.setEnabled(true);
    }
  }
/**
 * Method to add a process to a pathway.  Creates a new list selection dialog of all the summary porcesses, gets the user selected object if one exists, 
 * gets the next state of the vegetative type and adds it and the process to the pathway state.  
 * @param e
 */
  void processAddPB_actionPerformed(ActionEvent e) {
    String              title = "Add a Process";
    ListSelectionDialog dlg;

    dlg = new ListSelectionDialog(theFrame,title,true,Process.getSummaryProcesses());
    dlg.setLocation(getLocation());
    dlg.setVisible(true);

    ProcessType result = (ProcessType)dlg.getSelection();
    if (result != null) {
      Process p = Process.findInstance(result);
      Process succession = Process.findInstance(ProcessType.SUCCESSION);
      VegetativeType nextState = pathwayState.getProcessNextState(succession);
      pathwayState.addProcessNextState(p,nextState);
    }
    updateDialog();
  }
/**
 * Delete process button.  Calls the context menu which deletes a process within the list item.  
 * @param e
 */
  void processDeletePB_actionPerformed(ActionEvent e) {
    contextMenuDelete_actionPerformed(e);
  }





}
