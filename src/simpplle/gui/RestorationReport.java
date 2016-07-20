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
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import simpplle.comcode.Restoration;
import simpplle.comcode.SimpplleError;

/** 
 *
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class RestorationReport extends JDialog {
  File inputFilePrefix1, inputFilePrefix2;
  File outputFile;

  JPanel panel1 = new JPanel();
  JPanel jPanel4 = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  JPanel fileprefixPanel1 = new JPanel();
  JPanel outputFilePanel = new JPanel();
  JPanel filePrefixPanel2 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  JPanel pushButtonPanel = new JPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  JPanel aPanel = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JButton okPB = new JButton();
  JButton cancelPB = new JButton();
  JPanel buttonPanel1 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel textFieldPanel1 = new JPanel();
  JButton filePrefixPB1 = new JButton();
  JTextField filePrefixText1 = new JTextField();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel textFieldPanel2 = new JPanel();
  JPanel buttonPanel2 = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  JButton filePrefixPB2 = new JButton();
  BorderLayout borderLayout7 = new BorderLayout();
  JTextField filePrefixText2 = new JTextField();
  JPanel textFieldPanel3 = new JPanel();
  JPanel buttonPanel3 = new JPanel();
  BorderLayout borderLayout8 = new BorderLayout();
  BorderLayout borderLayout9 = new BorderLayout();
  BorderLayout borderLayout10 = new BorderLayout();
  JButton outputFilePB = new JButton();
  JTextField outputFileText = new JTextField();

  public RestorationReport(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public RestorationReport() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Prefix for Probability File Set #1");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Prefix for Probability File Set #2");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)),"Output File");
    panel1.setLayout(borderLayout1);
    jPanel4.setLayout(gridLayout2);
    gridLayout2.setRows(3);
    fileprefixPanel1.setLayout(borderLayout2);
    outputFilePanel.setLayout(borderLayout8);
    filePrefixPanel2.setLayout(borderLayout7);
    fileprefixPanel1.setBorder(titledBorder1);
    filePrefixPanel2.setBorder(titledBorder2);
    outputFilePanel.setBorder(titledBorder3);
    pushButtonPanel.setLayout(flowLayout4);
    aPanel.setLayout(gridLayout1);
    gridLayout1.setColumns(2);
    gridLayout1.setHgap(5);
    okPB.setEnabled(false);
    okPB.setNextFocusableComponent(cancelPB);
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    this.setModal(true);
    buttonPanel1.setLayout(borderLayout4);
    textFieldPanel1.setLayout(borderLayout3);
    filePrefixPB1.setIcon(new ImageIcon(RestorationReport.class.getResource("images/openFile.gif")));
    filePrefixPB1.setMargin(new Insets(0, 0, 0, 0));
    filePrefixPB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        filePrefixPB1_actionPerformed(e);
      }
    });
    borderLayout2.setHgap(2);
    filePrefixText1.setEditable(false);
    filePrefixText1.setColumns(30);
    buttonPanel2.setLayout(borderLayout5);
    textFieldPanel2.setLayout(borderLayout6);
    filePrefixPB2.setIcon(new ImageIcon(RestorationReport.class.getResource("images/openFile.gif")));
    filePrefixPB2.setMargin(new Insets(0, 0, 0, 0));
    filePrefixPB2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        filePrefixPB2_actionPerformed(e);
      }
    });
    filePrefixText2.setEditable(false);
    filePrefixText2.setColumns(30);
    borderLayout7.setHgap(2);
    buttonPanel3.setLayout(borderLayout9);
    textFieldPanel3.setLayout(borderLayout10);
    outputFilePB.setIcon(new ImageIcon(RestorationReport.class.getResource("images/save.gif")));
    outputFilePB.setMargin(new Insets(0, 0, 0, 0));
    outputFilePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        outputFilePB_actionPerformed(e);
      }
    });
    outputFileText.setToolTipText("");
    outputFileText.setEditable(false);
    outputFileText.setColumns(30);
    borderLayout8.setHgap(2);
    getContentPane().add(panel1);
    panel1.add(jPanel4, BorderLayout.NORTH);
    jPanel4.add(fileprefixPanel1, null);
    fileprefixPanel1.add(buttonPanel1, BorderLayout.WEST);
    buttonPanel1.add(filePrefixPB1, BorderLayout.NORTH);
    fileprefixPanel1.add(textFieldPanel1, BorderLayout.CENTER);
    textFieldPanel1.add(filePrefixText1, BorderLayout.NORTH);
    jPanel4.add(filePrefixPanel2, null);
    filePrefixPanel2.add(buttonPanel2, BorderLayout.WEST);
    buttonPanel2.add(filePrefixPB2, BorderLayout.NORTH);
    filePrefixPanel2.add(textFieldPanel2, BorderLayout.CENTER);
    textFieldPanel2.add(filePrefixText2, BorderLayout.NORTH);
    jPanel4.add(outputFilePanel, null);
    outputFilePanel.add(buttonPanel3, BorderLayout.WEST);
    buttonPanel3.add(outputFilePB, BorderLayout.NORTH);
    outputFilePanel.add(textFieldPanel3, BorderLayout.CENTER);
    textFieldPanel3.add(outputFileText, BorderLayout.SOUTH);
    panel1.add(pushButtonPanel, BorderLayout.SOUTH);
    pushButtonPanel.add(aPanel, null);
    aPanel.add(okPB, null);
    aPanel.add(cancelPB, null);
  }

  private void enableOkPBIfReady() {
    if (inputFilePrefix1 != null && inputFilePrefix2 != null &&
        outputFile != null) {
      okPB.setEnabled(true);
    }
  }
/**
 * In order to run restoration must have species, size class, and density files.  
 * @param prefix
 * @return
 */
  private boolean checkInputFile(File prefix) {
    if (Restoration.doFilesExist(prefix) == false) {
      String msg = "Could not open one or more of the input files:\n" +
                   prefix.toString() + "-species.txt\n" +
                   prefix.toString() + "-size.txt\n" +
                   prefix.toString() + "-canopy.txt\n\n" +
                   "Please Try Again";
      JOptionPane.showMessageDialog(this,msg,"Invalid Input File Prefix",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  void filePrefixPB1_actionPerformed(ActionEvent e) {
    String title = "Please Select <Any> Probability File For Set #1";

    File prefix = getFilePrefix(title);
    if (prefix == null) { return; }

    inputFilePrefix1 = prefix;
    filePrefixText1.setText(inputFilePrefix1.getPath());
    enableOkPBIfReady();
  }

  void filePrefixPB2_actionPerformed(ActionEvent e) {
    String title = "Please Select <Any> Probability File For Set #2";

    File prefix = getFilePrefix(title);
    if (prefix == null) { return; }

    inputFilePrefix2 = prefix;
    filePrefixText2.setText(inputFilePrefix2.getPath());
    enableOkPBIfReady();
  }

  private File getFilePrefix(String title) {
    File         prefix, outfile;
    String       dir, name;
    int          returnVal, index;
    String[]     suffixes = new String[] {"-species","-canopy","-size"};
    MyFileFilter extFilter = new MyFileFilter(suffixes,"txt",
                                               "Simpplle Probability Files (*.txt)");


    do {
      outfile = Utility.getOpenFile(this,title,extFilter);
      if (outfile == null) { return null; }

      dir  = outfile.getParent();
      name = outfile.getName();

      index = name.lastIndexOf("-");
      if (index == -1) {
        String msg = "Invalid Probability File";
        JOptionPane.showMessageDialog(this,msg,msg,JOptionPane.ERROR_MESSAGE);
        return null;
      }

      name = name.substring(0,index);
      prefix = new File(dir,name);
    }
    while (checkInputFile(prefix) == false);

    return prefix;
  }

  void outputFilePB_actionPerformed(ActionEvent e) {
    File filename = Utility.getSaveFile(this,"Output File");
    if (filename != null) {
      outputFile = filename;
      outputFileText.setText(outputFile.getPath());
      enableOkPBIfReady();
    }
  }

  void okPB_actionPerformed(ActionEvent e) {
    try {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      Restoration.doReport(inputFilePrefix1, inputFilePrefix2, outputFile);
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      setVisible(false);
      dispose();
    }
    catch (SimpplleError err) {
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      JOptionPane.showMessageDialog(this,err.getMessage(),
                                    "Problems generating file",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }
}
