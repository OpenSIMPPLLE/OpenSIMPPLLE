/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.gui;

/**
 * This class creates an Existing Vegetative Unit analysis panel.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class EvuAnalysisPanel extends javax.swing.JPanel {
    
    /** Creates new form EvuAnalysisPanel */
    public EvuAnalysisPanel() {
        initComponents();
    }
    
    /** 
     * Auto-generated by Gui builder.  
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/simpplle/gui/images/prev.gif")));
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(jButton1);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("jLabel1");
        jPanel4.add(jLabel1);

        jTextField1.setText("jTextField1");
        jPanel4.add(jTextField1);

        jPanel1.add(jPanel4);

        jButton2.setText("jButton2");
        jPanel1.add(jButton2);

        add(jPanel1);

        add(jPanel2);

        add(jPanel3);

    }
  
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
}
