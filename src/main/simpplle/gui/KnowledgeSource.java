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
import java.awt.event.*;
import java.awt.BorderLayout;

/**
 *
 *
 * @author Documentation by Brian Losi
 * Original source code authorship: Kirk A. Moeller
 */

public class KnowledgeSource extends JDialog {
  String mainText = "";

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  private JTextArea editor = new JTextArea();
  private JScrollPane editorScrollPane = new JScrollPane(editor);
  private JPanel jPanel1 = new JPanel();
  private JPanel jPanel2 = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JButton cancelPB = new JButton();
  private JButton okPB = new JButton();
  public KnowledgeSource(Frame frame, String title, boolean modal, String text) {
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

  public KnowledgeSource() {
    this(null, "", false,"");
  }
  void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    editor.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14));
    editor.setLineWrap(true);
    editor.setWrapStyleWord(true);
    editor.setCaretPosition(0);
    editor.setText("01234567890123456789012345678901234567890123456789012345678901234567890123456789");
    editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    editorScrollPane.setPreferredSize(new Dimension(650, 350));
    jPanel1.setLayout(borderLayout2);
    jPanel2.setLayout(flowLayout1);
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new KnowledgeSource_cancelPB_actionAdapter(this));
    okPB.setText("Ok");
    okPB.addActionListener(new KnowledgeSource_okPB_actionAdapter(this));
    this.addWindowListener(new KnowledgeSource_this_windowAdapter(this));
    getContentPane().add(mainPanel);
    jPanel2.add(okPB);
    jPanel2.add(cancelPB);
    mainPanel.add(jPanel1, java.awt.BorderLayout.CENTER);
    mainPanel.add(jPanel2, java.awt.BorderLayout.SOUTH);
    jPanel1.add(editorScrollPane, java.awt.BorderLayout.CENTER);
  }
/**
 * Initializes the knowledge source editor text.
 */
  private void initialize() {
    editor.setText(mainText);
  }
/**
 * Gets the main text.  
 * @return
 */
  public String getText() {
    return mainText;
  }
/**
 * Quits the knowledge source dialog by setting it not visible and disposes.  
 */
  private void quit() {
    setVisible(false);
    dispose();
  }
  /**
   *Ok button.  Sets the main text as the editor text.  Then quits.
   * @param e 'OK'
   */
  public void okPB_actionPerformed(ActionEvent e) {
    mainText = editor.getText();
    quit();
  }
/**
 * When user cancels, sets the main text variable to null.  Then quits.
 * @param e
 */
  public void cancelPB_actionPerformed(ActionEvent e) {
    mainText = null;
    quit();
  }
/**
 * When user closes window, sets the main text as editor text so it is not lost.  Then quits.
 * @param e
 */
  public void this_windowClosing(WindowEvent e) {
    mainText = editor.getText();
    quit();
  }
}
/**
 * Class for window adaptor to handle window closing event.  
 *
 */
class KnowledgeSource_this_windowAdapter extends WindowAdapter {
  private KnowledgeSource adaptee;
  KnowledgeSource_this_windowAdapter(KnowledgeSource adaptee) {
    this.adaptee = adaptee;
  }
/**
 * If window is closed passes to method which handles the event.  
 */
  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}
/**
 * 
 *Action adaptor to handle cancel button press. 
 *
 */
class KnowledgeSource_cancelPB_actionAdapter implements ActionListener {
  private KnowledgeSource adaptee;
  KnowledgeSource_cancelPB_actionAdapter(KnowledgeSource adaptee) {
    this.adaptee = adaptee;
  }
/**
 * If cancel button is pushed passes to method which handles event.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelPB_actionPerformed(e);
  }
}
/**
 * Action adapter that handles when the ok button is pressed.  
 *
 */
class KnowledgeSource_okPB_actionAdapter implements ActionListener {
  private KnowledgeSource adaptee;
  KnowledgeSource_okPB_actionAdapter(KnowledgeSource adaptee) {
    this.adaptee = adaptee;
  }
/**
 * If ok button is pushed passes to method which handles event.  
 */
  public void actionPerformed(ActionEvent e) {
    adaptee.okPB_actionPerformed(e);
  }
}
