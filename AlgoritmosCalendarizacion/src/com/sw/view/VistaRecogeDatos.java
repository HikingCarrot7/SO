package com.sw.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;

/**
 *
 * @author HikingCarrot7
 */
public class VistaRecogeDatos extends javax.swing.JFrame
{

    public VistaRecogeDatos()
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

        title = new javax.swing.JLabel();
        labelNProcesos = new javax.swing.JLabel();
        aceptarNProcesos = new javax.swing.JButton();
        labelQuantum = new javax.swing.JLabel();
        milisegundosLabel = new javax.swing.JLabel();
        soporteTabla = new javax.swing.JTabbedPane();
        soporteScrollTabla = new javax.swing.JScrollPane();
        tablaRecogeDatos = new javax.swing.JTable();
        continuar = new javax.swing.JButton();
        nota = new javax.swing.JLabel();
        regresar = new javax.swing.JButton();
        entradaNProcesos = new javax.swing.JSpinner();
        entradaNQuantum = new javax.swing.JSpinner();
        aleatorio = new javax.swing.JButton();
        borrarTodo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(790, 595));
        setMinimumSize(new java.awt.Dimension(790, 595));
        setPreferredSize(new java.awt.Dimension(790, 595));
        setResizable(false);
        getContentPane().setLayout(null);

        title.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Preparando datos para la simulación");
        title.setMaximumSize(new java.awt.Dimension(680, 50));
        title.setMinimumSize(new java.awt.Dimension(680, 50));
        title.setPreferredSize(new java.awt.Dimension(680, 50));
        getContentPane().add(title);
        title.setBounds(52, 10, 680, 50);

        labelNProcesos.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        labelNProcesos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelNProcesos.setText("Número de procesos:");
        labelNProcesos.setMaximumSize(new java.awt.Dimension(160, 30));
        labelNProcesos.setMinimumSize(new java.awt.Dimension(160, 30));
        labelNProcesos.setPreferredSize(new java.awt.Dimension(160, 30));
        getContentPane().add(labelNProcesos);
        labelNProcesos.setBounds(10, 70, 160, 30);

        aceptarNProcesos.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        aceptarNProcesos.setText("Aceptar");
        aceptarNProcesos.setToolTipText("Establecer el número de procesos.");
        aceptarNProcesos.setActionCommand("aceptar");
        aceptarNProcesos.setMaximumSize(new java.awt.Dimension(80, 30));
        aceptarNProcesos.setMinimumSize(new java.awt.Dimension(80, 30));
        aceptarNProcesos.setPreferredSize(new java.awt.Dimension(80, 30));
        getContentPane().add(aceptarNProcesos);
        aceptarNProcesos.setBounds(280, 70, 80, 30);

        labelQuantum.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        labelQuantum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelQuantum.setText("QUANTUM:");
        labelQuantum.setMaximumSize(new java.awt.Dimension(160, 30));
        labelQuantum.setMinimumSize(new java.awt.Dimension(160, 30));
        labelQuantum.setPreferredSize(new java.awt.Dimension(160, 30));
        getContentPane().add(labelQuantum);
        labelQuantum.setBounds(360, 70, 160, 30);

        milisegundosLabel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        milisegundosLabel.setText("Milisegundos");
        milisegundosLabel.setMaximumSize(new java.awt.Dimension(145, 30));
        milisegundosLabel.setMinimumSize(new java.awt.Dimension(145, 30));
        milisegundosLabel.setPreferredSize(new java.awt.Dimension(145, 30));
        getContentPane().add(milisegundosLabel);
        milisegundosLabel.setBounds(625, 70, 145, 30);

        soporteTabla.setMaximumSize(new java.awt.Dimension(750, 380));
        soporteTabla.setMinimumSize(new java.awt.Dimension(750, 380));
        soporteTabla.setPreferredSize(new java.awt.Dimension(750, 380));

        soporteScrollTabla.setMaximumSize(new java.awt.Dimension(450, 400));
        soporteScrollTabla.setMinimumSize(new java.awt.Dimension(450, 400));
        soporteScrollTabla.setPreferredSize(new java.awt.Dimension(450, 400));

        tablaRecogeDatos.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tablaRecogeDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {"", "", "", ""}
            },
            new String []
            {
                "ID", "Nombre proceso", "Tiempo ráfaga", "Tiempo de llegada"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tablaRecogeDatos.setToolTipText("");
        tablaRecogeDatos.getTableHeader().setReorderingAllowed(false);
        soporteScrollTabla.setViewportView(tablaRecogeDatos);

        soporteTabla.addTab("Datos para la simulación.", soporteScrollTabla);

        getContentPane().add(soporteTabla);
        soporteTabla.setBounds(10, 130, 750, 380);

        continuar.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        continuar.setText("Continuar");
        continuar.setToolTipText("Continuar con la simulación.");
        continuar.setActionCommand("continuar");
        continuar.setMaximumSize(new java.awt.Dimension(100, 30));
        continuar.setMinimumSize(new java.awt.Dimension(100, 30));
        continuar.setPreferredSize(new java.awt.Dimension(100, 30));
        getContentPane().add(continuar);
        continuar.setBounds(660, 520, 100, 30);

        nota.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        nota.setText("NOTA: Las unidades de tiempo se toman en milisegundos.");
        nota.setMaximumSize(new java.awt.Dimension(160, 30));
        nota.setMinimumSize(new java.awt.Dimension(160, 30));
        nota.setPreferredSize(new java.awt.Dimension(160, 30));
        getContentPane().add(nota);
        nota.setBounds(10, 520, 330, 30);

        regresar.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        regresar.setText("<- Regresar");
        regresar.setToolTipText("Regresar a la ventana anterior.");
        regresar.setActionCommand("regresar");
        regresar.setMaximumSize(new java.awt.Dimension(100, 30));
        regresar.setMinimumSize(new java.awt.Dimension(100, 30));
        regresar.setPreferredSize(new java.awt.Dimension(100, 30));
        getContentPane().add(regresar);
        regresar.setBounds(10, 10, 100, 30);

        entradaNProcesos.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        entradaNProcesos.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        entradaNProcesos.setToolTipText("Insertar el número de procesos para esta simulación.");
        entradaNProcesos.setMaximumSize(new java.awt.Dimension(100, 30));
        entradaNProcesos.setMinimumSize(new java.awt.Dimension(100, 30));
        entradaNProcesos.setPreferredSize(new java.awt.Dimension(100, 30));
        getContentPane().add(entradaNProcesos);
        entradaNProcesos.setBounds(170, 70, 100, 30);

        entradaNQuantum.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        entradaNQuantum.setToolTipText("Insertar el valor del Quantum para esta simulación.");
        entradaNQuantum.setMaximumSize(new java.awt.Dimension(100, 30));
        entradaNQuantum.setMinimumSize(new java.awt.Dimension(100, 30));
        entradaNQuantum.setPreferredSize(new java.awt.Dimension(100, 30));
        getContentPane().add(entradaNQuantum);
        entradaNQuantum.setBounds(520, 70, 100, 30);

        aleatorio.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        aleatorio.setText("Datos aleatorios");
        aleatorio.setToolTipText("Generar datos aleatorios.");
        aleatorio.setActionCommand("aleatorios");
        aleatorio.setMaximumSize(new java.awt.Dimension(130, 30));
        aleatorio.setMinimumSize(new java.awt.Dimension(130, 30));
        aleatorio.setPreferredSize(new java.awt.Dimension(130, 30));
        getContentPane().add(aleatorio);
        aleatorio.setBounds(520, 520, 130, 30);

        borrarTodo.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        borrarTodo.setText("Borrar todo");
        borrarTodo.setToolTipText("Borra todo el contenido de la tabla.");
        borrarTodo.setActionCommand("borrarTodo");
        borrarTodo.setMaximumSize(new java.awt.Dimension(90, 30));
        borrarTodo.setMinimumSize(new java.awt.Dimension(90, 30));
        borrarTodo.setPreferredSize(new java.awt.Dimension(90, 30));
        getContentPane().add(borrarTodo);
        borrarTodo.setBounds(410, 520, 100, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JButton getAceptarNProcesos()
    {
        return aceptarNProcesos;
    }

    public JButton getContinuar()
    {
        return continuar;
    }

    public JButton getRegresar()
    {
        return regresar;
    }

    public JButton getAleatorio()
    {
        return aleatorio;
    }

    public JSpinner getEntradaNQuantum()
    {
        return entradaNQuantum;
    }

    public JLabel getEntradaValidaLabel()
    {
        return milisegundosLabel;
    }

    public JLabel getLabelQuantum()
    {
        return labelQuantum;
    }

    public JTable getTablaRecogeDatos()
    {
        return tablaRecogeDatos;
    }

    public JSpinner getEntradaNProcesos()
    {
        return entradaNProcesos;
    }

    public JButton getBorrarTodo()
    {
        return borrarTodo;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarNProcesos;
    private javax.swing.JButton aleatorio;
    private javax.swing.JButton borrarTodo;
    private javax.swing.JButton continuar;
    private javax.swing.JSpinner entradaNProcesos;
    private javax.swing.JSpinner entradaNQuantum;
    private javax.swing.JLabel labelNProcesos;
    private javax.swing.JLabel labelQuantum;
    private javax.swing.JLabel milisegundosLabel;
    private javax.swing.JLabel nota;
    private javax.swing.JButton regresar;
    private javax.swing.JScrollPane soporteScrollTabla;
    private javax.swing.JTabbedPane soporteTabla;
    private javax.swing.JTable tablaRecogeDatos;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}