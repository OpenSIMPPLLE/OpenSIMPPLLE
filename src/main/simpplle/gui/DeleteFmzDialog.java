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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import simpplle.comcode.Fmz;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;

/**
 * This dialog handles the deletion of fire management zones from the current regional zone.
 */

class DeleteFmzDialog extends JDialog {

  private JList fmzList;

  DeleteFmzDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    setLocationRelativeTo(frame);
    try  {
      jbInit();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    initialize();
  }

  private void jbInit() throws Exception {

    fmzList = new JList();
    fmzList.setBorder(BorderFactory.createRaisedBevelBorder());
    fmzList.setVisibleRowCount(20);
    fmzList.setForeground(Color.white);
    fmzList.setBackground(Color.darkGray);
    fmzList.setSelectionBackground(Color.white);
    fmzList.setSelectionForeground(Color.orange);
    fmzList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    fmzList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !fmzList.isSelectionEmpty()) {
          deleteZone();
          setVisible(false);
          dispose();
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(fmzList);

    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BorderLayout());
    northPanel.add(scrollPane, BorderLayout.CENTER);

    JButton okButton = new JButton("Ok");
    okButton.setMargin(new Insets(2, 14, 2, 14));
    okButton.setMaximumSize(new Dimension(73, 27));
    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.addActionListener(this::selectOk);

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this::selectCancel);

    JPanel southPanel = new JPanel();
    southPanel.setBorder(BorderFactory.createEtchedBorder());
    southPanel.setLayout(new FlowLayout());
    southPanel.add(okButton, null);
    southPanel.add(cancelButton, null);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    mainPanel.add(northPanel);

    add(mainPanel);
  }

  private void initialize() {

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    DefaultListModel<Fmz> listModel = new DefaultListModel();
    RegionalZone zone = Simpplle.getCurrentZone();
    Fmz defaultFmz = zone.getDefaultFmz();
    for (Fmz fmz: zone.getAllFmz()) {
      if (!fmz.equals(defaultFmz)) {
        listModel.addElement(fmz);
      }
    }
    fmzList.setModel(listModel);
  }

  private void deleteZone(){
    if (Simpplle.getCurrentArea() != null) {
      int choice = JOptionPane.showConfirmDialog(this,
                                                 "This action will delete the selected zones.\n" +
                                                 "Units assigned to these zones in the current\n" +
                                                 "area will be reassigned to the default zone.\n\n" +
                                                 "Do you wish to continue?",
                                                 "Delete Zone",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.NO_OPTION) {
        return;
      }
    }
    RegionalZone regionalZone = Simpplle.getCurrentZone();
    for (Object item : fmzList.getSelectedValuesList()) {
      regionalZone.removeFmz((Fmz)item);
    }
  }

  private void selectOk(ActionEvent e){
    if (fmzList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,
                                    "Please select a zone",
                                    "No Zone Selected",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }
    deleteZone();
    setVisible(false);
    dispose();
  }

  private void selectCancel(ActionEvent e){
    setVisible(false);
    dispose();
  }
}
