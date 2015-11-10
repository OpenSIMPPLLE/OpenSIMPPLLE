package simpplle.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import simpplle.comcode.FireEvent;
import simpplle.comcode.Climate;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines the JDialog for fire season. 
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */

public class FireSeason extends JDialog {
  private int spring=0, summer=0, fall=0, winter=0;

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel1 = new JPanel();
  GridLayout gridLayout2 = new GridLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel springLabel = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel2 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  TitledBorder titledBorder1;
  JTextField springText = new JTextField();
  JTextField fallText = new JTextField();
  JTextField summerText = new JTextField();
  JTextField winterText = new JTextField();
  JPanel buttonPanel = new JPanel();
  JButton cancelPB = new JButton();
  JButton okPB = new JButton();
  FlowLayout flowLayout2 = new FlowLayout();   
  JLabel totalLabel = new JLabel();
  JLabel totalValue = new JLabel();
/**
 * Constructor for fire season Jdialog.  
 * @param frame
 * @param title
 * @param modal
 */
  public FireSeason(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
/**
 * Overloaded constructor.  References primary constructor sets frame owner as null, title to empty string and modality ot false. 
 */
  public FireSeason() {
    this(null, "", false);
  }
  /**
   * Sets up borders, panels, layouts, and labels fro seasons 
   * @throws Exception
   */
  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Fire Season Probabilities");
    mainPanel.setLayout(borderLayout1);
    jPanel1.setLayout(borderLayout2);
    gridLayout2.setRows(5);
    gridLayout1.setRows(5);
    jLabel3.setFont(new java.awt.Font("Monospaced", 1, 12));
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel3.setText("Summer");
    jLabel4.setFont(new java.awt.Font("Monospaced", 1, 12));
    jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel4.setText("Winter");
    jLabel2.setFont(new java.awt.Font("Monospaced", 1, 12));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setText("Fall");
    springLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    springLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    springLabel.setText("Spring");
    jPanel3.setLayout(gridLayout1);
    jPanel2.setLayout(gridLayout2);
    jPanel4.setLayout(flowLayout1);
    springText.setFont(new java.awt.Font("Monospaced", 0, 12));
    springText.setPreferredSize(new Dimension(40, 21));
    springText.setText("25");
    springText.setHorizontalAlignment(SwingConstants.RIGHT);
    springText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        springText_keyTyped(e);
      }
    });
    fallText.setFont(new java.awt.Font("Monospaced", 0, 12));
    fallText.setPreferredSize(new Dimension(40, 21));
    fallText.setText("25");
    fallText.setHorizontalAlignment(SwingConstants.RIGHT);
    fallText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        fallText_keyTyped(e);
      }
    });
    summerText.setFont(new java.awt.Font("Monospaced", 0, 12));
    summerText.setPreferredSize(new Dimension(40, 21));
    summerText.setText("25");
    summerText.setHorizontalAlignment(SwingConstants.RIGHT);
    summerText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        summerText_keyTyped(e);
      }
    });
    winterText.setFont(new java.awt.Font("Monospaced", 0, 12));
    winterText.setPreferredSize(new Dimension(40, 21));
    winterText.setText("25");
    winterText.setHorizontalAlignment(SwingConstants.RIGHT);
    winterText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        winterText_keyTyped(e);
      }
    });
    borderLayout2.setHgap(5);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    okPB.setText("Ok");
    okPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    buttonPanel.setLayout(flowLayout2);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.setModal(true);
    totalLabel.setFont(new java.awt.Font("Monospaced", 1, 12));
    totalLabel.setText("Total");
    totalValue.setFont(new java.awt.Font("Monospaced", 1, 12));
    totalValue.setForeground(Color.blue);
    totalValue.setHorizontalAlignment(SwingConstants.RIGHT);
    totalValue.setText("100");
    getContentPane().add(mainPanel);
    mainPanel.add(jPanel4,  BorderLayout.CENTER);
    jPanel4.add(jPanel1, null);
    jPanel3.add(springLabel, null);
    jPanel3.add(jLabel3, null);
    jPanel3.add(jLabel2, null);
    jPanel3.add(jLabel4, null);
    jPanel3.add(totalLabel, null);
    mainPanel.add(buttonPanel,  BorderLayout.SOUTH);
    buttonPanel.add(okPB, null);
    buttonPanel.add(cancelPB, null);
    jPanel1.add(jPanel2,  BorderLayout.EAST);
    jPanel1.add(jPanel3, BorderLayout.WEST);
    jPanel2.add(springText, null);
    jPanel2.add(summerText, null);
    jPanel2.add(fallText, null);
    jPanel2.add(winterText, null);
    jPanel2.add(totalValue, null);
  }
/**
 * Initializes the fire season data for each season.  
 */
  private void initialize() {
    spring = FireEvent.getFireSeasonData(Climate.Season.SPRING);
    summer = FireEvent.getFireSeasonData(Climate.Season.SUMMER);
    fall   = FireEvent.getFireSeasonData(Climate.Season.FALL);
    winter   = FireEvent.getFireSeasonData(Climate.Season.WINTER);

    springText.setText(Integer.toString(spring));
    summerText.setText(Integer.toString(summer));
    fallText.setText(Integer.toString(fall));
    winterText.setText(Integer.toString(winter));
    updateTotal();
  }
/**
 * Updates the total value by adding the integers for spring summer fall and winter and then updating graphics.  
 */
  private void updateTotal() {
    totalValue.setText(Integer.toString((spring+summer+fall+winter)));
    update(getGraphics());
  }
/**
 * Checks if key event is a typed digit.  If the character is not a digit nor delete, nor backspace consumes it, returns false, and beeps.
 * Else it returns true if it is not delete nor backspace.     
 * @param e key pressed
 * @return true if key event was a digit
 */
  private boolean digitTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (Character.isDigit(key) == false &&
        key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
      return false;
    }
    return (key != KeyEvent.VK_DELETE && key != KeyEvent.VK_BACK_SPACE);
  }
  /**
   * Handles key event that occurs in spring text box.  Sonsumes if digit typed or length ==3 and beeps.
   * Else creates a new runnable for spring update.  
   * @param e
   */
  void springText_keyTyped(KeyEvent e) {
    if (digitTyped(e) && springText.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doSpringUpdate = new Runnable() {
    	/**
    	 * if spring text field text length is greater than 1 parse the int and update totals.  If length not greater than 0 return 0.
    	 */
      public void run() {
        spring = (springText.getText().length() > 0) ?
                      Integer.parseInt(springText.getText()) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doSpringUpdate);
  }
  /**
   * Handles key event that occurs in summer text box.  Consumes if digit typed or length ==3 and beeps.
   * @param e
   */
  void summerText_keyTyped(KeyEvent e) {
    if (digitTyped(e) && summerText.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doSummerUpdate = new Runnable() {
    	/**
   	     * if summer text field text length is greater than 1 parse the int and update totals.  If length not greater than 0 return 0.
    	 */
      public void run() {
        summer = (summerText.getText().length() > 0) ?
                      Integer.parseInt(summerText.getText()) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doSummerUpdate);
  }
  /**
   * Handles key event that occurs in Fall text box.  Consumes if digit typed or length ==3 and beeps.
   * @param e
   */
  void fallText_keyTyped(KeyEvent e) {
    if (digitTyped(e) && fallText.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doFallUpdate = new Runnable() {
    	/**
  	     * if Fall text field text length is greater than 1 parse the int and update totals.  If length not greater than 0 return 0.
    	 */
      public void run() {
        fall = (fallText.getText().length() > 0) ?
                      Integer.parseInt(fallText.getText()) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doFallUpdate);
  }
  /**
   * Handles key event that occurs in Winter text box.  Consumes if digit typed or length ==3 and beeps.
   * @param e
   */
  void winterText_keyTyped(KeyEvent e) {
    if (digitTyped(e) && winterText.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doWinterUpdate = new Runnable() {
    	/**
	     * if winter text field text length is greater than 1 parse the int and update totals.  If length not greater than 0 return 0.
    	 */
      public void run() {
        winter = (winterText.getText().length() > 0) ?
                      Integer.parseInt(winterText.getText()) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doWinterUpdate);
  }
/**
 * First checks to make sure spring, summer, fall, and winter probabilities must total to 100 and returns.  Otherwise sets the fire season data to 
 */
  private void finish() {
    if ((spring + summer + fall + winter) != 100) {
      String msg = "Probabilities must total to 100";
      JOptionPane.showMessageDialog(this,msg,"Total NOT 100",
                                    JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    FireEvent.setFireSeasonData(spring,summer,fall,winter);
    setVisible(false);
    dispose();
  }
  void this_windowClosing(WindowEvent e) {
    finish();
  }
/**
 * Finishes when ok button pushed.
 * @param e 'OK'
 */
  void okPB_actionPerformed(ActionEvent e) {
    finish();
  }
/**
 * Disposes and sets not visible the Fire Season dialog if cancel button pushed.
 * @param e 'cancel'
 */
  void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }


}




