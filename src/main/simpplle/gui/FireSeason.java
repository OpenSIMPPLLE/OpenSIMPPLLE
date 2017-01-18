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
import javax.swing.border.*;
import java.awt.event.*;
import simpplle.comcode.FireEvent;
import simpplle.comcode.Climate;

/** 
 * This class defines the JDialog for fire season.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireSeason extends JDialog {
  private int spring = 0;
  private int summer = 0;
  private int fall=0;
  private int winter=0;

  private Font panelFont = new Font("Monospaced", 0, 12);

  private JPanel mainPanel = new JPanel();
  private JPanel wrapperPanel = new JPanel();
  private JPanel textBoxPanel = new JPanel();
  private JPanel labelPanel = new JPanel();
  private JPanel buttonPanel = new JPanel();

//  private JScrollPane scrollPane = new JScrollPane(wrapperPanel);
  private GridLayout gridLayout1 = new GridLayout();
  private GridLayout gridLayout2 = new GridLayout();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private FlowLayout flowLayout2 = new FlowLayout();

  private TitledBorder titledBorder1;
  private JLabel springLabel = new JLabel();
  private JLabel summerLabel = new JLabel();
  private JLabel fallLabel = new JLabel();
  private JLabel winterLabel = new JLabel();
  private JLabel totalLabel = new JLabel();
  private JLabel totalValue = new JLabel();

  private JTextField springText = new JTextField();
  private JTextField summerText = new JTextField();
  private JTextField fallText = new JTextField();
  private JTextField winterText = new JTextField();

  private JButton cancelPB = new JButton();
  private JButton okPB = new JButton();

/**
 * Constructor for fire season Jdialog.  
 * @param frame
 * @param title
 * @param modal
 */

FireSeason(Frame frame, String title, boolean modal) {
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
FireSeason() {
    this(null, "", false);
  }
  /**
   * Sets up borders, panels, layouts, and labels fro seasons 
   * @throws Exception
   */
  private void jbInit() throws Exception {

    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });

    setMinimumSize(new Dimension(220,150));

    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
                        new Color(148, 145, 140)),"Fire Season Probabilities");
    mainPanel.setLayout(borderLayout1);
    mainPanel.setMinimumSize(new Dimension(200,150));
    wrapperPanel.setLayout(borderLayout2);
    labelPanel.setLayout(gridLayout1);
    labelPanel.setMinimumSize(new Dimension(200,20));
    labelPanel.setMaximumSize(new Dimension(300,250));
    textBoxPanel.setMinimumSize(new Dimension(200,20));
    textBoxPanel.setMaximumSize(new Dimension(300,250));
    textBoxPanel.setLayout(gridLayout1);
    buttonPanel.setLayout(flowLayout2);
    gridLayout1.setRows(5);
    gridLayout2.setRows(5);
    borderLayout2.setHgap(5);

    springLabel.setFont(panelFont);
    springLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    springLabel.setText("Spring");
    summerLabel.setFont(panelFont);
    summerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    summerLabel.setText("Summer");
    fallLabel.setFont(panelFont);
    fallLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    fallLabel.setText("Fall");
    winterLabel.setFont(panelFont);
    winterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    winterLabel.setText("Winter");

    springText.setFont(panelFont);
    springText.setText("25");
    springText.setHorizontalAlignment(SwingConstants.RIGHT);
    springText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        springText_keyTyped(e);
      }
    });
    summerText.setFont(panelFont);
    summerText.setText("25");
    summerText.setHorizontalAlignment(SwingConstants.RIGHT);
    summerText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        summerText_keyTyped(e);
      }
    });
    fallText.setFont(panelFont);
    fallText.setText("25");
    fallText.setHorizontalAlignment(SwingConstants.RIGHT);
    fallText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        fallText_keyTyped(e);
      }
    });
    winterText.setFont(panelFont);
    winterText.setText("25");
    winterText.setHorizontalAlignment(SwingConstants.RIGHT);
    winterText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        winterText_keyTyped(e);
      }
    });

    cancelPB.setText("Cancel");
    cancelPB.addActionListener(this::cancelPB_actionPerformed);
    okPB.setText("Ok");
    okPB.addActionListener(this::okPB_actionPerformed);

    totalLabel.setFont(panelFont);
    totalLabel.setText("Total");
    totalValue.setFont(panelFont);
    totalValue.setForeground(Color.blue);
    totalValue.setHorizontalAlignment(SwingConstants.RIGHT);
    totalValue.setText("100");

    getContentPane().add(mainPanel);
    mainPanel.add(wrapperPanel, BorderLayout.CENTER);
    labelPanel.add(springLabel);
    labelPanel.add(summerLabel);
    labelPanel.add(fallLabel);
    labelPanel.add(winterLabel);
    labelPanel.add(totalLabel);
    mainPanel.add(buttonPanel,  BorderLayout.SOUTH);
    buttonPanel.add(okPB);
    buttonPanel.add(cancelPB);
    wrapperPanel.add(textBoxPanel,  BorderLayout.CENTER);
    wrapperPanel.add(labelPanel, BorderLayout.WEST);
    textBoxPanel.add(springText);
    textBoxPanel.add(summerText);
    textBoxPanel.add(fallText);
    textBoxPanel.add(winterText);
    textBoxPanel.add(totalValue);
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
   * Handles key event that occurs in spring text box.  Consumes if digit typed or length ==3 and beeps.
   * Else creates a new runnable for spring update.  
   * @param e
   */
  private void springText_keyTyped(KeyEvent e) {
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
  private void summerText_keyTyped(KeyEvent e) {
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
  private void fallText_keyTyped(KeyEvent e) {
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
  private void winterText_keyTyped(KeyEvent e) {
    if (digitTyped(e) && winterText.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }

    Runnable doWinterUpdate = new Runnable() {
    	/**
	     * if winter text field text length is greater than 1 parse the int and update totals.  If length not greater than 0 return 0.
    	 */
      public void run() {
        winter = (winterText.getText().length() > 0) ? Integer.parseInt(winterText.getText()) : 0;
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

  private void this_windowClosing(WindowEvent e) {
    finish();
  }
/**
 * Finishes when ok button pushed.
 * @param e 'OK'
 */
  private void okPB_actionPerformed(ActionEvent e) {
    finish();
  }
/**
 * Disposes and sets not visible the Fire Season dialog if cancel button pushed.
 * @param e 'cancel'
 */
  private void cancelPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }
}
