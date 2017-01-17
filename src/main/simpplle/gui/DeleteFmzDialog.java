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
import java.awt.event.MouseListener;
import javax.swing.*;
import simpplle.comcode.Fmz;
import simpplle.comcode.RegionalZone;
import simpplle.comcode.Simpplle;

/**
 *
 * This class handles the deletion of fmz(s) within a particular zone.
 *
 */

class DeleteFmzDialog extends JDialog {

  private JList fmzList = new JList();
  private JButton okButton = new JButton("Ok");
  private JButton cancelButton = new JButton("Cancel");
  private JScrollPane scrollPane = new JScrollPane();
  private JPanel mainPanel = new JPanel();
  private JPanel northPanel = new JPanel();
  private JPanel southPanel = new JPanel();
  private FlowLayout flowLayout2 = new FlowLayout();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  RegionalZone zone = Simpplle.getCurrentZone();

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
    fmzList.setBorder(BorderFactory.createRaisedBevelBorder());
    fmzList.setVisibleRowCount(20);
    fmzList.setForeground(Color.white);
    fmzList.setBackground(Color.darkGray);
    fmzList.setSelectionBackground(Color.white);
    fmzList.setSelectionForeground(Color.orange);
    fmzList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    fmzList.addMouseListener(new MouseClicked());

    scrollPane.setViewportView(fmzList);

    northPanel.setLayout(borderLayout2);
    northPanel.add(scrollPane, BorderLayout.CENTER);

    okButton.setMaximumSize(new Dimension(73, 27));
    okButton.setMinimumSize(new Dimension(73, 27));
    okButton.setPreferredSize(new Dimension(73, 27));
    okButton.setMargin(new Insets(2, 14, 2, 14));
    okButton.addActionListener(this::selectOk);
    cancelButton.addActionListener(this::selectCancel);

    southPanel.setLayout(flowLayout2);
    southPanel.setBorder(BorderFactory.createEtchedBorder());

    southPanel.add(okButton, null);
    southPanel.add(cancelButton, null);

    mainPanel.setLayout(borderLayout1);
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    mainPanel.add(northPanel);

    add(mainPanel);
  }

  private void initialize() {
    DefaultListModel<Fmz> fmzData = new DefaultListModel();
    Fmz[] availFmz = zone.getAllFmz();
    Fmz defaultFmz = zone.getDefaultFmz();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    for(Fmz item: availFmz){

      if(!item.equals(defaultFmz)){

        fmzData.addElement(item);
      }
    }
    fmzList.setModel(fmzData);
  }

  private void deleteZone(){
    String optionMsg;
    int choice;

    optionMsg = "This action will delete any selected FMZ(s).\n" +
        "If an area is loaded, any unit assigned to these\n" +
        "FMZ(s) will be reset to the default FMZ.\n\n" +
        "Are you sure?";

    choice = JOptionPane.showConfirmDialog(this,optionMsg,"Delete an FMZ.",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if(choice == JOptionPane.YES_OPTION) {
      java.util.List removeList= fmzList.getSelectedValuesList();

      for(Object item: removeList){
        zone.removeFmz((Fmz)item);
      }
    }
  }

  // Action handlers
  private void selectOk(ActionEvent e){
    if (fmzList.isSelectionEmpty()) {
      JOptionPane.showMessageDialog(this,"Please select an Fmz","No Fmz Selected",JOptionPane.WARNING_MESSAGE);
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

  private class MouseClicked implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2 && fmzList.isSelectionEmpty() == false) {
        deleteZone();
        setVisible(false);
        dispose();
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
  }
}
