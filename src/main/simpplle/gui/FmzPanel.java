/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import javax.swing.*;
import simpplle.comcode.Fmz;

/**
 * This panel displays the attributes of a single fire management zone.
 */

class FmzPanel extends JPanel {

  private JTextField acreText;
  private JTextField totalFireText;
  private JTextField responseText;

  private Fmz workingZone;

  FmzPanel(Fmz workingZone){

    this.workingZone = workingZone;

    float totalFires = 0.0f;

    totalFires += workingZone.getManmadeFires(Fmz.A);
    totalFires += workingZone.getManmadeFires(Fmz.B);
    totalFires += workingZone.getManmadeFires(Fmz.C);
    totalFires += workingZone.getManmadeFires(Fmz.D);
    totalFires += workingZone.getManmadeFires(Fmz.E);
    totalFires += workingZone.getManmadeFires(Fmz.F);
    totalFires += workingZone.getNaturalFires(Fmz.A);
    totalFires += workingZone.getNaturalFires(Fmz.B);
    totalFires += workingZone.getNaturalFires(Fmz.C);
    totalFires += workingZone.getNaturalFires(Fmz.D);
    totalFires += workingZone.getNaturalFires(Fmz.E);
    totalFires += workingZone.getNaturalFires(Fmz.F);

    workingZone.setNaturalFires(Fmz.A,totalFires);
    workingZone.setNaturalFires(Fmz.B,0.0f);
    workingZone.setNaturalFires(Fmz.C,0.0f);
    workingZone.setNaturalFires(Fmz.D,0.0f);
    workingZone.setNaturalFires(Fmz.E,0.0f);
    workingZone.setNaturalFires(Fmz.F,0.0f);
    workingZone.setManmadeFires(Fmz.A,0.0f);
    workingZone.setManmadeFires(Fmz.B,0.0f);
    workingZone.setManmadeFires(Fmz.C,0.0f);
    workingZone.setManmadeFires(Fmz.D,0.0f);
    workingZone.setManmadeFires(Fmz.E,0.0f);
    workingZone.setManmadeFires(Fmz.F,0.0f);

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    JLabel zoneLabel = new JLabel(workingZone.getName());
    zoneLabel.setFont(new java.awt.Font("Monospaced", 0, 12));

    acreText = new JTextField();
    acreText.setText(nf.format(workingZone.getAcres()));
    acreText.setHorizontalAlignment(JTextField.RIGHT);
    acreText.addActionListener(e -> updateAcres());
    acreText.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateAcres();
      }
    });

    totalFireText = new JTextField();
    totalFireText.setText(nf.format(totalFires));
    totalFireText.setHorizontalAlignment(JTextField.RIGHT);
    totalFireText.setLayout(new FlowLayout(FlowLayout.CENTER));
    totalFireText.addActionListener(e -> updateFireTotal());
    totalFireText.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateFireTotal();
      }
    });

    responseText = new JTextField();
    responseText.setText(nf.format(workingZone.getResponseTime()));
    responseText.setHorizontalAlignment(JTextField.RIGHT);
    responseText.addActionListener(e -> updateResponseTime());
    responseText.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        updateResponseTime();
      }
    });

    setLayout(new GridLayout(1,4,10,0));
    setMaximumSize(new Dimension(1000,30));

    add(zoneLabel);
    add(acreText);
    add(totalFireText);
    add(responseText);

  }

  private void updateAcres() {
    float acres = Float.parseFloat(acreText.getText());
    if (acres > 0.0f) {
      workingZone.setAcres(acres);
    } else {
      JOptionPane.showMessageDialog(getParent(),
                                    "Value must be greater than zero",
                                    "Invalid Acreage",
                                    JOptionPane.ERROR_MESSAGE);
      acreText.requestFocus();
    }
  }

  private void updateFireTotal() {
    workingZone.setNaturalFires(Fmz.A, Float.parseFloat(totalFireText.getText()));
    workingZone.setNaturalFires(Fmz.B, 0.0f);
    workingZone.setNaturalFires(Fmz.C, 0.0f);
    workingZone.setNaturalFires(Fmz.D, 0.0f);
    workingZone.setNaturalFires(Fmz.E, 0.0f);
    workingZone.setNaturalFires(Fmz.F, 0.0f);
    workingZone.setManmadeFires(Fmz.A, 0.0f);
    workingZone.setManmadeFires(Fmz.B, 0.0f);
    workingZone.setManmadeFires(Fmz.C, 0.0f);
    workingZone.setManmadeFires(Fmz.D, 0.0f);
    workingZone.setManmadeFires(Fmz.E, 0.0f);
    workingZone.setManmadeFires(Fmz.F, 0.0f);
  }

  private void updateResponseTime() {
    workingZone.setResponseTime(Float.parseFloat(responseText.getText()));
  }
}
