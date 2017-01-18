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
import simpplle.comcode.Formatting;

/**
 * This panel displays the attributes of a single fire management zone.
 */

class FmzPanel extends JPanel {

  private JLabel zoneLabel;
  private JTextField acreText;
  private JTextField totalFireText;
  private JTextField responseText;

  private Dimension JPanelDim = new Dimension(595,40);

  private Fmz workingZone;

  FmzPanel(Fmz workingZone){
    this.workingZone = workingZone;

    // Number format
    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    // Formatting individual panels
    setLayout(new FlowLayout(FlowLayout.TRAILING,65,0));
    setMaximumSize(JPanelDim);

    setBorder(BorderFactory.createEtchedBorder());

    // Zone label
    zoneLabel = new JLabel(Formatting.fixedField(workingZone.getName(), 4));
    zoneLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
    zoneLabel.setHorizontalTextPosition(SwingConstants.TRAILING);

    // Acres
    float acres = workingZone.getAcres();
    String acreStr = Float.isNaN(acres) ? "" : nf.format(acres);

    acreText = new JTextField(5);
    acreText.setText(acreStr);
    acreText.addActionListener(this::acreText_ActionPerformed);
    acreText.addFocusListener(new acre_Focus());

    // Total starts/10 years (fmz man made + fmz natural)
    float fmzAManFireTotal = workingZone.getManmadeFires(Fmz.A);
    float fmzBManFireTotal = workingZone.getManmadeFires(Fmz.B);
    float fmzCManFireTotal = workingZone.getManmadeFires(Fmz.C);
    float fmzDManFireTotal = workingZone.getManmadeFires(Fmz.D);
    float fmzEManFireTotal = workingZone.getManmadeFires(Fmz.E);
    float fmzFManFireTotal = workingZone.getManmadeFires(Fmz.F);

    float fmzANatFireTotal = workingZone.getNaturalFires(Fmz.A);
    float fmzBNatFireTotal = workingZone.getNaturalFires(Fmz.B);
    float fmzCNatFireTotal = workingZone.getNaturalFires(Fmz.C);
    float fmzDNatFireTotal = workingZone.getNaturalFires(Fmz.D);
    float fmzENatFireTotal = workingZone.getNaturalFires(Fmz.E);
    float fmzFNatFireTotal = workingZone.getNaturalFires(Fmz.F);

    float manFireTotal = fmzAManFireTotal + fmzBManFireTotal + fmzCManFireTotal + fmzDManFireTotal
        + fmzEManFireTotal + fmzFManFireTotal;

    float natFireTotal = fmzANatFireTotal + fmzBNatFireTotal + fmzCNatFireTotal + fmzDNatFireTotal
        + fmzENatFireTotal + fmzFNatFireTotal;

    float finalFireTotal = manFireTotal + natFireTotal;
    String fireTotalStr = Float.isNaN(finalFireTotal) ? "" : nf.format(finalFireTotal);

    // Changing values due to starts/10 years
    workingZone.setNaturalFires(Fmz.A,finalFireTotal);
    workingZone.setNaturalFires(Fmz.B,0);
    workingZone.setNaturalFires(Fmz.C,0);
    workingZone.setNaturalFires(Fmz.D,0);
    workingZone.setNaturalFires(Fmz.E,0);
    workingZone.setNaturalFires(Fmz.F,0);

    workingZone.setManmadeFires(Fmz.A,0);
    workingZone.setManmadeFires(Fmz.B,0);
    workingZone.setManmadeFires(Fmz.C,0);
    workingZone.setManmadeFires(Fmz.D,0);
    workingZone.setManmadeFires(Fmz.E,0);
    workingZone.setManmadeFires(Fmz.F,0);

    totalFireText = new JTextField(7);
    totalFireText.setText(fireTotalStr);
    totalFireText.setLayout(new FlowLayout(FlowLayout.CENTER));
    totalFireText.addActionListener(this::fireText_ActionPerformed);
    totalFireText.addFocusListener(new fire_Focus());

    // Response times
    float time = workingZone.getResponseTime();
    String timeStr = Float.isNaN(time) ? "" : nf.format(time);

    responseText = new JTextField(5);
    responseText.setText(timeStr);
    responseText.addActionListener(this::responseText_ActionPerformed);
    responseText.addFocusListener(new response_Focus());

    // Add components
    add(zoneLabel);
    add(acreText);
    add(totalFireText);
    add(responseText);
  }

  // Action Listeners
  private void acreText_ActionPerformed(ActionEvent e){
    try{
      updateAcres();
    } catch (NumberFormatException nfe){

      JOptionPane.showMessageDialog(getParent(),nfe.getMessage(),"Invalid Number Entered",JOptionPane.ERROR_MESSAGE);
      acreText.requestFocus();
    }
  }

  private void fireText_ActionPerformed(ActionEvent e){
      updateFire();
  }
  private void responseText_ActionPerformed(ActionEvent e){
    updateResponseTime();
  }

  // Focus listeners
  private class acre_Focus implements FocusListener{

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {

      try{
        updateAcres();
      } catch (NumberFormatException nfe){

        JOptionPane.showMessageDialog(getParent(),nfe.getMessage(),"Invalid Number Entered",JOptionPane.ERROR_MESSAGE);
        acreText.requestFocus();
      }
    }
  }

  private class fire_Focus implements FocusListener{

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {
      updateFire();
    }
  }

  private class response_Focus implements FocusListener{

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {

      updateResponseTime();
    }
  }

  private void updateAcres(){
    String newText;
    float newAcres;

    newText = acreText.getText();
    newAcres = Float.parseFloat(newText);

    // Checks for zero acres
    if(!(newAcres > 0.0f)){
      throw new NumberFormatException("Value must be greater than 0.0");
    }
    workingZone.setAcres(newAcres);
  }

  private void updateFire(){
    String newText;
    float newFireTotal;

    newText = totalFireText.getText();
    newFireTotal = Float.parseFloat(newText);

    // Use first natural fire class to hold total
    workingZone.setNaturalFires(Fmz.A,newFireTotal);
  }

  private void updateResponseTime(){
    String newText;
    float newResponse;

    newText = responseText.getText();
    newResponse = Float.parseFloat(newText);

    workingZone.setResponseTime(newResponse);
  }
}
