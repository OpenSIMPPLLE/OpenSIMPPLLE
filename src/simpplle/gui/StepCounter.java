/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import simpplle.comcode.VegetativeType;
import java.util.Vector;

/**
 * This class contains a dialog for pathways step counter.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: mdousset
 */
public class StepCounter extends JDialog {

        private static final int WIDTH = 380;
        private static final int HEIGHT = 160;
        private static final byte ORIGIN_SELECT = 0;
        private static final byte DEST_SELECT = 1;
        private static final byte COUNT = 2;

        private MyCanvas canvas;
        private HistoryTableModel history;
        private byte select = ORIGIN_SELECT;
        private JTextField[] txtFields = new JTextField[3];
        private VegetativeType[] vegTypes = new VegetativeType[2];
/**
 * Constructor for Step Counter, this sets the dialog owner to a particular Pathway and titel to Pathways Step COunter"
 * @param owner
 */
        public StepCounter(Pathway owner) {
                super(owner, "Pathways Step Counter");
                Point loc = owner.getLocation();
                Container thePane = getContentPane();
                JPanel tmpPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                // Set components
                canvas = owner.canvas;
                setLocation(loc.x+owner.getSize().width-WIDTH, loc.y);
                setSize(WIDTH, HEIGHT);
                thePane.setLayout(new GridLayout(1,2));
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4,2,0,2);
                gbc.gridwidth = 2;
                gbc.weightx = 1;
                tmpPanel.add(new JLabel("Origin"), gbc);
                gbc.insets = new Insets(0,2,0,2);
                gbc.gridy = 1;
                txtFields[ORIGIN_SELECT] = new JTextField();
                tmpPanel.add(initTextField(txtFields[ORIGIN_SELECT]), gbc);
                gbc.gridy = 2;
                tmpPanel.add(new JLabel("Destination"), gbc);
                gbc.gridy = 3;
                txtFields[DEST_SELECT] = new JTextField();
                tmpPanel.add(initTextField(txtFields[DEST_SELECT]), gbc);
                gbc.gridy = 4;
                gbc.gridwidth = 1;
                gbc.insets = new Insets(4,2,4,2);
                gbc.weightx = 0;
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.weighty = 1;
                tmpPanel.add(new JLabel("Number of time steps"), gbc);
                gbc.gridwidth = 2;
                gbc.weightx = 1;
                txtFields[COUNT] = new JTextField();
                txtFields[COUNT].setHorizontalAlignment(JTextField.RIGHT);
                tmpPanel.add(initTextField(txtFields[COUNT]), gbc);

                // Set buttons
                gbc.weightx = 0;
                gbc.weighty = 0;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                gbc.gridwidth = 1;
                gbc.insets = new Insets(0,2,0,2);
                gbc.anchor = GridBagConstraints.SOUTH;
                ImageIcon targetImg = new ImageIcon(simpplle.gui.StepCounter.class.getResource("images/target.gif"));
//		ImageIcon targetImg = new ImageIcon("simpplle/gui/images/target.gif");
                JButton selector = new JButton(targetImg);
                selector.setPreferredSize(new Dimension(targetImg.getIconWidth()+8, targetImg.getIconHeight()+8));
                selector.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                                select = ORIGIN_SELECT;
                        }
                });
                tmpPanel.add(selector, gbc);
                gbc.gridy = 2;
                selector = new JButton(targetImg);
                selector.setPreferredSize(new Dimension(targetImg.getIconWidth()+8, targetImg.getIconHeight()+8));
                selector.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                                select = DEST_SELECT;
                        }
                });
                tmpPanel.add(selector, gbc);

                // Set history grid
                thePane.add(tmpPanel);
                history = new HistoryTableModel();
                JTable historyTable = new JTable(history);
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(JLabel.RIGHT);
                historyTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
                historyTable.getColumnModel().getColumn(2).setPreferredWidth(40);
                historyTable.setFont(historyTable.getFont().deriveFont(10f));
                JScrollPane scrollPane = new JScrollPane(historyTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                thePane.add(scrollPane);

                // Set mouse adapter on canvas
                canvas.addMouseListener(new MouseAdapter(){
                        public void mouseClicked(MouseEvent e){
                                if (select>=0 && canvas.selectedState != null) {
                                        vegTypes[select] = canvas.selectedState.getState();
                                        txtFields[select].setText(vegTypes[select].toString());
                                        if(vegTypes[ORIGIN_SELECT]!=null && vegTypes[DEST_SELECT]!=null){
                                                txtFields[COUNT].setText(Integer.toString(vegTypes[ORIGIN_SELECT].calculateTimeToState(vegTypes[DEST_SELECT], canvas.getProcess())));
                                                history.addRow();
                                        }
                                }
                        }
                });
        }
/**
 * initializes the textfield used for step counter.  
 * @param aField
 * @return
 */
        private JTextField initTextField(JTextField aField) {
                aField.setEditable(false);
                aField.setFont(aField.getFont().deriveFont(Font.BOLD));
                return aField;
        }
/**
 * 
 * History table model is a type of abstract table model with three columns "Origin", "Dest.", "Steps", 
 *
 */
        private class HistoryTableModel extends AbstractTableModel{
                String[] colNames = {"Origin", "Dest.", "Steps"};
                Vector values = new Vector();

                public int getRowCount(){
                        return values.size();
                }
/**
 * Returns 3 for the number of columns.  Choices for columns are "Origin", "Dest.", "Steps". 
 */
                public int getColumnCount(){
                        return colNames.length;
                }
/**
 * Gets an array of values from a particular cell.  
 */
                public Object getValueAt(int row, int column){
                        return ((Object[])values.elementAt(row))[column];
                }
/**
 * Gets the column names for History Table Model.  Choices are "Origin", "Dest.", "Steps"
 */
                public String getColumnName(int column){
                        return colNames[column];
                }
/**
 * Adds a row to the table model.  
 */
                public void addRow()
                {
                        Object[] newRow = new Object[colNames.length];
                        for(int i=0; i<txtFields.length; i++)
                                newRow[i] = txtFields[i].getText();
                        values.insertElementAt(newRow, 0);
                        fireTableRowsInserted(0, 0);
                }
        }
}
