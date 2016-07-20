/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import javax.swing.*;

/**
 *
 *
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class JTextAreaDialog extends JDialog {
  String mainText = "";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane mainScrollPane = new JScrollPane();
  JTextArea mainTextArea = new JTextArea();
/**
 * Constructor for JTextAreaDialog 
 * @param frame - frame owner of JDialog.  
 * @param title the title of Dialog
 * @param modal
 * @param text 
 */
  public JTextAreaDialog(Frame frame, String title, boolean modal, String text) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    mainText = text;
    initialize();
  }
/**
 * Overloaded constructor for JTextAreaDialog.  Sets the owner to null, the title to empty, modality to false, and main text to empty string.
 */
  public JTextAreaDialog() {
    this(null, "", false,"");
  }
  /**
   * Sets the main text area size columns, rows, and editability.  Also creates a main panel and scroll pain. where the main text will be displayed
   * @throws Exception
   */
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    mainTextArea.setEditable(false);
    mainTextArea.setColumns(90);
    mainTextArea.setRows(25);
    this.setModal(true);
    getContentPane().add(mainPanel);
    mainPanel.add(mainScrollPane,  BorderLayout.CENTER);
    mainScrollPane.getViewport().add(mainTextArea, null);
  }
/**
 * Initializes the JTextAreaDialog by setting the main text area with main text which is either empty or passed in constructor.  
 * Sets the main panel size and the caret position to 0
 */
  private void initialize() {
    mainTextArea.setText(mainText);
    mainPanel.setSize(this.getPreferredSize());
    mainTextArea.setCaretPosition(0);
  }
}

