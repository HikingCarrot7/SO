package com.sw.view;

import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author HikingC7
 */
public class VistaSeleccion extends javax.swing.JFrame
{

    public VistaSeleccion()
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        titile = new javax.swing.JLabel();
        rr = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        srtf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seleccione un algoritmo");
        setMaximumSize(new java.awt.Dimension(305, 200));
        setMinimumSize(new java.awt.Dimension(305, 200));
        setPreferredSize(new java.awt.Dimension(305, 200));
        setResizable(false);
        getContentPane().setLayout(null);

        titile.setBackground(new java.awt.Color(255, 255, 255));
        titile.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 24));
        titile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titile.setText("Algoritmo a simular");
        getContentPane().add(titile);
        titile.setBounds(10, 5, 280, 40);

        rr.setBackground(new java.awt.Color(255, 255, 255));
        rr.setText("Round Robin");
        rr.setToolTipText("Simular el algoritmo RR.");
        rr.setActionCommand("rr");
        rr.setMaximumSize(new java.awt.Dimension(280, 50));
        rr.setMinimumSize(new java.awt.Dimension(280, 50));
        rr.setPreferredSize(new java.awt.Dimension(280, 50));
        getContentPane().add(rr);
        rr.setBounds(10, 110, 280, 50);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(null);

        srtf.setBackground(new java.awt.Color(255, 255, 255));
        srtf.setText("Short Remainig Time First");
        srtf.setToolTipText("Simular el algoritmo SRTF");
        srtf.setActionCommand("srtf");
        srtf.setMaximumSize(new java.awt.Dimension(280, 50));
        srtf.setMinimumSize(new java.awt.Dimension(280, 50));
        srtf.setPreferredSize(new java.awt.Dimension(280, 50));
        jPanel1.add(srtf);
        srtf.setBounds(10, 50, 280, 50);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 310, 190);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JButton getRr()
    {
        return rr;
    }

    public JButton getSrtf()
    {
        return srtf;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton rr;
    private javax.swing.JButton srtf;
    private javax.swing.JLabel titile;
    // End of variables declaration//GEN-END:variables
}
