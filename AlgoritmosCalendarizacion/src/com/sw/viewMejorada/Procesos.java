package com.sw.viewMejorada;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nicolás
 */
public class Procesos extends javax.swing.JFrame
{

    public Procesos()
    {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        GridBagConstraints gridBagConstraints;

        jPanel2 = new JPanel();
        jPanel7 = new JPanel();
        jPanel8 = new JPanel();
        jLabel5 = new JLabel();
        filler2 = new Box.Filler(new Dimension(0, 10), new Dimension(0, 10), new Dimension(32767, 10));
        filler3 = new Box.Filler(new Dimension(0, 10), new Dimension(0, 10), new Dimension(32767, 10));
        jPanel3 = new JPanel();
        jLabel1 = new JLabel();
        jSpinner1 = new JSpinner();
        jButton1 = new JButton();
        filler1 = new Box.Filler(new Dimension(30, 0), new Dimension(30, 0), new Dimension(30, 32767));
        jLabel2 = new JLabel();
        jSpinner2 = new JSpinner();
        jLabel3 = new JLabel();
        jPanel4 = new JPanel();
        jPanel6 = new JPanel();
        jLabel4 = new JLabel();
        jPanel5 = new JPanel();
        jButton2 = new JButton();
        jButton5 = new JButton();
        jButton4 = new JButton();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 360));
        setPreferredSize(new Dimension(560, 360));

        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.LINE_AXIS));
        getContentPane().add(jPanel2, BorderLayout.NORTH);

        jPanel7.setLayout(new BorderLayout());

        jPanel8.setLayout(new BorderLayout());

        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel5.setText("Preparando datos para la simulación");
        jPanel8.add(jLabel5, BorderLayout.CENTER);
        jPanel8.add(filler2, BorderLayout.PAGE_END);
        jPanel8.add(filler3, BorderLayout.PAGE_START);

        jPanel7.add(jPanel8, BorderLayout.NORTH);

        jPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel1.setText("Número de procesos:");
        jPanel3.add(jLabel1);

        jSpinner1.setMaximumSize(new Dimension(60, 20));
        jSpinner1.setMinimumSize(new Dimension(60, 20));
        jSpinner1.setPreferredSize(new Dimension(60, 20));
        jPanel3.add(jSpinner1);

        jButton1.setText("Aceptar");
        jPanel3.add(jButton1);
        jPanel3.add(filler1);

        jLabel2.setText("QUANTUM:");
        jPanel3.add(jLabel2);

        jSpinner2.setMaximumSize(new Dimension(60, 20));
        jSpinner2.setMinimumSize(new Dimension(60, 20));
        jSpinner2.setPreferredSize(new Dimension(60, 20));
        jPanel3.add(jSpinner2);

        jLabel3.setText("Milisegundos");
        jPanel3.add(jLabel3);

        jPanel7.add(jPanel3, BorderLayout.CENTER);

        getContentPane().add(jPanel7, BorderLayout.NORTH);

        jPanel4.setLayout(new GridBagLayout());

        jPanel6.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabel4.setText("NOTA: Las unidades de tiempo se toman en milisegundos.");
        jPanel6.add(jLabel4);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        jPanel4.add(jPanel6, gridBagConstraints);

        jPanel5.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jButton2.setText("Borrar datos");
        jPanel5.add(jButton2);

        jButton5.setText("Regresar");
        jPanel5.add(jButton5);

        jButton4.setText("Continuar");
        jPanel5.add(jButton4);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        jPanel4.add(jPanel5, gridBagConstraints);

        getContentPane().add(jPanel4, BorderLayout.SOUTH);

        jPanel1.setBorder(BorderFactory.createTitledBorder("Datos para la simulación"));
        jPanel1.setLayout(new BorderLayout());

        jTable1.setModel(new DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, BorderLayout.CENTER);

        getContentPane().add(jPanel1, BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Windows".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            System.out.println(ex.getMessage());
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() ->
        {
            Procesos vista = new Procesos();
            vista.setVisible(true);
            vista.setLocationRelativeTo(null);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Box.Filler filler1;
    private Box.Filler filler2;
    private Box.Filler filler3;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton4;
    private JButton jButton5;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JScrollPane jScrollPane1;
    private JSpinner jSpinner1;
    private JSpinner jSpinner2;
    private JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
