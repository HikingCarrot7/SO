package com.sw.view;

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
        sjf = new javax.swing.JButton();
        srtf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seleccione un algoritmo");
        setMaximumSize(new java.awt.Dimension(305, 200));
        setMinimumSize(new java.awt.Dimension(305, 200));
        setPreferredSize(new java.awt.Dimension(305, 200));
        setResizable(false);
        getContentPane().setLayout(null);

        titile.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        titile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titile.setText("Algoritmo a simular");
        getContentPane().add(titile);
        titile.setBounds(10, 5, 280, 20);

        rr.setText("Round Robin");
        rr.setToolTipText("Simular el algoritmo RR.");
        rr.setActionCommand("rr");
        rr.setMaximumSize(new java.awt.Dimension(280, 50));
        rr.setMinimumSize(new java.awt.Dimension(280, 50));
        rr.setPreferredSize(new java.awt.Dimension(280, 50));
        getContentPane().add(rr);
        rr.setBounds(10, 130, 280, 40);

        sjf.setText("Short Job First");
        sjf.setToolTipText("Simular el algoritmo SJF.");
        sjf.setActionCommand("sjf");
        sjf.setMaximumSize(new java.awt.Dimension(280, 30));
        sjf.setMinimumSize(new java.awt.Dimension(280, 30));
        sjf.setPreferredSize(new java.awt.Dimension(280, 30));
        getContentPane().add(sjf);
        sjf.setBounds(10, 50, 280, 40);

        srtf.setText("Short Remainig Time First");
        srtf.setToolTipText("Simular el algoritmo SRTF");
        srtf.setActionCommand("srtf");
        srtf.setMaximumSize(new java.awt.Dimension(280, 50));
        srtf.setMinimumSize(new java.awt.Dimension(280, 50));
        srtf.setPreferredSize(new java.awt.Dimension(280, 50));
        getContentPane().add(srtf);
        srtf.setBounds(10, 90, 280, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JButton getRr()
    {
        return rr;
    }

    public JButton getSjf()
    {
        return sjf;
    }

    public JButton getSrtf()
    {
        return srtf;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton rr;
    private javax.swing.JButton sjf;
    private javax.swing.JButton srtf;
    private javax.swing.JLabel titile;
    // End of variables declaration//GEN-END:variables
}
