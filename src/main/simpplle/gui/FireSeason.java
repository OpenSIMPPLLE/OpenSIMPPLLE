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
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import simpplle.comcode.FireEvent;
import simpplle.comcode.Season;

/** 
 * This dialog edits the probability of the season of a fire event, which occur in grassland zones.
 */

public class FireSeason extends JDialog {

  private int spring = 0;
  private int summer = 0;
  private int fall   = 0;
  private int winter = 0;

  private JTextField springField;
  private JTextField summerField;
  private JTextField fallField;
  private JTextField winterField;

  private JLabel totalValue;

  public FireSeason(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      initialize();
      pack();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    Font font = new Font("Monospaced", 0, 12);

    JLabel springLabel = new JLabel("Spring", JLabel.RIGHT);
    springLabel.setFont(font);

    JLabel summerLabel = new JLabel("Summer", JLabel.RIGHT);
    summerLabel.setFont(font);

    JLabel fallLabel = new JLabel("Fall", JLabel.RIGHT);
    fallLabel.setFont(font);

    JLabel winterLabel = new JLabel("Winter", JLabel.RIGHT);
    winterLabel.setFont(font);

    JLabel totalLabel = new JLabel("Total", JLabel.RIGHT);
    totalLabel.setFont(font);

    springField = new JTextField("25");
    springField.setFont(font);
    springField.setHorizontalAlignment(SwingConstants.RIGHT);
    springField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        editSpring(e);
      }
    });

    summerField = new JTextField("25");
    summerField.setFont(font);
    summerField.setHorizontalAlignment(SwingConstants.RIGHT);
    summerField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        editSummer(e);
      }
    });

    fallField = new JTextField("25");
    fallField.setFont(font);
    fallField.setHorizontalAlignment(SwingConstants.RIGHT);
    fallField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        editFall(e);
      }
    });

    winterField = new JTextField("25");
    winterField.setFont(font);
    winterField.setHorizontalAlignment(SwingConstants.RIGHT);
    winterField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        editWinter(e);
      }
    });

    totalValue = new JLabel("100");
    totalValue.setFont(font);
    totalValue.setForeground(Color.blue);
    totalValue.setHorizontalAlignment(SwingConstants.RIGHT);

    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(5,2,10,5));
    gridPanel.add(springLabel);
    gridPanel.add(springField);
    gridPanel.add(summerLabel);
    gridPanel.add(summerField);
    gridPanel.add(fallLabel);
    gridPanel.add(fallField);
    gridPanel.add(winterLabel);
    gridPanel.add(winterField);
    gridPanel.add(totalLabel);
    gridPanel.add(totalValue);

    JButton cancelPB = new JButton("Cancel");
    cancelPB.addActionListener(this::pressedCancel);

    JButton okPB = new JButton("Ok");
    okPB.addActionListener(this::pressedOk);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(okPB);
    buttonPanel.add(cancelPB);

    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    mainPanel.setLayout(new BorderLayout(0,5));
    mainPanel.add(gridPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(mainPanel);

    setResizable(false);
    setMinimumSize(new Dimension(250, 200));
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        windowIsClosing(e);
      }
    });

  }

  private void initialize() {

    spring = FireEvent.getFireSeasonData(Season.SPRING);
    summer = FireEvent.getFireSeasonData(Season.SUMMER);
    fall   = FireEvent.getFireSeasonData(Season.FALL);
    winter = FireEvent.getFireSeasonData(Season.WINTER);

    springField.setText(Integer.toString(spring));
    summerField.setText(Integer.toString(summer));
    fallField.setText(Integer.toString(fall));
    winterField.setText(Integer.toString(winter));

    updateTotal();

  }

  private void updateTotal() {
    totalValue.setText(Integer.toString(spring + summer + fall + winter));
    update(getGraphics());
  }

  /**
   * Checks if a key is a digit. If it is not a digit, delete, or backspace, then the event is
   * ignored and a beep is played.
   *
   * @param e a key event
   * @return true if key event was a digit
   */
  private boolean digitTyped(KeyEvent e) {
    char key = e.getKeyChar();
    if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
      return false;
    }
    if (Character.isDigit(key)) {
      return true;
    } else {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
      return false;
    }
  }

  private void editSpring(KeyEvent e) {
    if (digitTyped(e) && springField.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
    Runnable doSpringUpdate = new Runnable() {
    	public void run() {
        String text = springField.getText();
        spring = (text.length() > 0) ? Integer.parseInt(text) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doSpringUpdate);
  }

  private void editSummer(KeyEvent e) {
    if (digitTyped(e) && summerField.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
    Runnable doSummerUpdate = new Runnable() {
    	public void run() {
        String text = summerField.getText();
        summer = (text.length() > 0) ? Integer.parseInt(text) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doSummerUpdate);
  }

  private void editFall(KeyEvent e) {
    if (digitTyped(e) && fallField.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
    Runnable doFallUpdate = new Runnable() {
    	public void run() {
        String text = fallField.getText();
        fall = (text.length() > 0) ? Integer.parseInt(text) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doFallUpdate);
  }

  private void editWinter(KeyEvent e) {
    if (digitTyped(e) && winterField.getText().length() == 3) {
      e.consume();
      java.awt.Toolkit.getDefaultToolkit().beep();
    }
    Runnable doWinterUpdate = new Runnable() {
    	public void run() {
        String text = winterField.getText();
        winter = (text.length() > 0) ? Integer.parseInt(text) : 0;
        updateTotal();
      }
    };
    SwingUtilities.invokeLater(doWinterUpdate);
  }

  private void finish() {
    int total = spring + summer + fall + winter;
    if (total != 100) {
      JOptionPane.showMessageDialog(this,
                                    "The probabilities must total 100%",
                                    "Invalid Total",
                                    JOptionPane.WARNING_MESSAGE);
      return;
    }
    FireEvent.setFireSeasonData(spring, summer, fall, winter);
    setVisible(false);
    dispose();
  }

  private void windowIsClosing(WindowEvent e) {
    finish();
  }

  private void pressedOk(ActionEvent e) {
    finish();
  }

  private void pressedCancel(ActionEvent e) {
    setVisible(false);
    dispose();
  }
}
