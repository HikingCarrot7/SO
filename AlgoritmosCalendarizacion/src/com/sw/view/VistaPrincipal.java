package com.sw.view;

import java.awt.Canvas;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author HikingCarrot7
 */
public class VistaPrincipal extends javax.swing.JFrame
{

    public VistaPrincipal()
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

        panelResumen = new javax.swing.JScrollPane();
        tablaResumen = new javax.swing.JTable();
        panelEspera = new javax.swing.JScrollPane();
        tablaEspera = new javax.swing.JTable();
        simulacion = new javax.swing.JButton();
        soporteTablaFinalizados = new javax.swing.JScrollPane();
        tablaProcesosFinalizados = new javax.swing.JTable();
        esquema = new java.awt.Canvas();
        regresar = new javax.swing.JButton();
        datosRecuperadosLabel = new javax.swing.JLabel();
        procesosFinalizadosLabel = new javax.swing.JLabel();
        tiemposEsperaLabel = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setResizable(false);
        getContentPane().setLayout(null);

        panelResumen.setMaximumSize(new java.awt.Dimension(220, 225));
        panelResumen.setMinimumSize(new java.awt.Dimension(220, 225));
        panelResumen.setPreferredSize(new java.awt.Dimension(220, 225));

        tablaResumen.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tablaResumen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ID", "Proceso", "Tiem. ráf.", "Tiem. lleg."
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
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
        tablaResumen.getTableHeader().setReorderingAllowed(false);
        panelResumen.setViewportView(tablaResumen);
        if (tablaResumen.getColumnModel().getColumnCount() > 0)
        {
            tablaResumen.getColumnModel().getColumn(0).setResizable(false);
            tablaResumen.getColumnModel().getColumn(0).setPreferredWidth(30);
            tablaResumen.getColumnModel().getColumn(1).setResizable(false);
            tablaResumen.getColumnModel().getColumn(2).setResizable(false);
            tablaResumen.getColumnModel().getColumn(3).setResizable(false);
        }

        getContentPane().add(panelResumen);
        panelResumen.setBounds(10, 65, 220, 225);

        panelEspera.setMaximumSize(new java.awt.Dimension(155, 495));
        panelEspera.setMinimumSize(new java.awt.Dimension(155, 495));
        panelEspera.setPreferredSize(new java.awt.Dimension(155, 495));

        tablaEspera.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tablaEspera.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Proceso", "Tiem. Esp."
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false
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
        tablaEspera.getTableHeader().setReorderingAllowed(false);
        panelEspera.setViewportView(tablaEspera);
        if (tablaEspera.getColumnModel().getColumnCount() > 0)
        {
            tablaEspera.getColumnModel().getColumn(0).setResizable(false);
            tablaEspera.getColumnModel().getColumn(1).setResizable(false);
        }

        getContentPane().add(panelEspera);
        panelEspera.setBounds(830, 65, 155, 495);

        simulacion.setBackground(new java.awt.Color(255, 255, 255));
        simulacion.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        simulacion.setText("Iniciar simulación");
        simulacion.setToolTipText("Iniciar simulación.");
        simulacion.setActionCommand("iniciarSimulacion");
        simulacion.setMaximumSize(new java.awt.Dimension(155, 25));
        simulacion.setMinimumSize(new java.awt.Dimension(155, 25));
        simulacion.setPreferredSize(new java.awt.Dimension(155, 25));
        getContentPane().add(simulacion);
        simulacion.setBounds(830, 5, 155, 25);

        soporteTablaFinalizados.setMaximumSize(new java.awt.Dimension(220, 225));
        soporteTablaFinalizados.setMinimumSize(new java.awt.Dimension(220, 225));
        soporteTablaFinalizados.setPreferredSize(new java.awt.Dimension(220, 225));

        tablaProcesosFinalizados.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tablaProcesosFinalizados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Proceso", "Tiem. fin."
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        soporteTablaFinalizados.setViewportView(tablaProcesosFinalizados);
        if (tablaProcesosFinalizados.getColumnModel().getColumnCount() > 0)
        {
            tablaProcesosFinalizados.getColumnModel().getColumn(0).setResizable(false);
            tablaProcesosFinalizados.getColumnModel().getColumn(1).setResizable(false);
        }

        getContentPane().add(soporteTablaFinalizados);
        soporteTablaFinalizados.setBounds(10, 335, 220, 225);

        esquema.setMaximumSize(new java.awt.Dimension(580, 525));
        esquema.setMinimumSize(new java.awt.Dimension(580, 525));
        esquema.setName(""); // NOI18N
        getContentPane().add(esquema);
        esquema.setBounds(240, 35, 580, 525);

        regresar.setBackground(new java.awt.Color(255, 255, 255));
        regresar.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        regresar.setText("<- Regresar");
        regresar.setToolTipText("Regresar a la ventana anterior.");
        regresar.setActionCommand("regresar");
        regresar.setMaximumSize(new java.awt.Dimension(100, 25));
        regresar.setMinimumSize(new java.awt.Dimension(100, 25));
        regresar.setPreferredSize(new java.awt.Dimension(100, 25));
        getContentPane().add(regresar);
        regresar.setBounds(10, 5, 100, 25);

        datosRecuperadosLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 14));
        datosRecuperadosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datosRecuperadosLabel.setText("Datos recuperados");
        datosRecuperadosLabel.setMaximumSize(new java.awt.Dimension(205, 25));
        datosRecuperadosLabel.setMinimumSize(new java.awt.Dimension(205, 25));
        datosRecuperadosLabel.setPreferredSize(new java.awt.Dimension(205, 25));
        getContentPane().add(datosRecuperadosLabel);
        datosRecuperadosLabel.setBounds(10, 35, 205, 25);

        procesosFinalizadosLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 14));
        procesosFinalizadosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        procesosFinalizadosLabel.setText("Procesos finalizados");
        procesosFinalizadosLabel.setMaximumSize(new java.awt.Dimension(205, 25));
        procesosFinalizadosLabel.setMinimumSize(new java.awt.Dimension(205, 25));
        procesosFinalizadosLabel.setPreferredSize(new java.awt.Dimension(205, 25));
        getContentPane().add(procesosFinalizadosLabel);
        procesosFinalizadosLabel.setBounds(10, 305, 205, 25);

        tiemposEsperaLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 14));
        tiemposEsperaLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tiemposEsperaLabel.setText("Tiempos de espera");
        tiemposEsperaLabel.setMaximumSize(new java.awt.Dimension(155, 25));
        tiemposEsperaLabel.setMinimumSize(new java.awt.Dimension(155, 25));
        tiemposEsperaLabel.setPreferredSize(new java.awt.Dimension(155, 25));
        getContentPane().add(tiemposEsperaLabel);
        tiemposEsperaLabel.setBounds(830, 35, 155, 25);

        title.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 16));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Representación gráfica de la simulación");
        title.setMaximumSize(new java.awt.Dimension(390, 25));
        title.setMinimumSize(new java.awt.Dimension(390, 25));
        title.setPreferredSize(new java.awt.Dimension(390, 25));
        getContentPane().add(title);
        title.setBounds(335, 5, 390, 25);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 5, true));
        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 994, 572);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JScrollPane getPanelResumen()
    {
        return panelResumen;
    }

    public JScrollPane getPanelLlegada()
    {
        return panelEspera;
    }

    public JButton getSimulacion()
    {
        return simulacion;
    }

    public JTable getTablaEspera()
    {
        return tablaEspera;
    }

    public JTable getTablaResumen()
    {
        return tablaResumen;
    }

    public Canvas getEsquema()
    {
        return esquema;
    }

    public JButton getRegresar()
    {
        return regresar;
    }

    public JTable getTablaProcesosFinalizados()
    {
        return tablaProcesosFinalizados;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel datosRecuperadosLabel;
    private java.awt.Canvas esquema;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane panelEspera;
    private javax.swing.JScrollPane panelResumen;
    private javax.swing.JLabel procesosFinalizadosLabel;
    private javax.swing.JButton regresar;
    private javax.swing.JButton simulacion;
    private javax.swing.JScrollPane soporteTablaFinalizados;
    private javax.swing.JTable tablaEspera;
    private javax.swing.JTable tablaProcesosFinalizados;
    private javax.swing.JTable tablaResumen;
    private javax.swing.JLabel tiemposEsperaLabel;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
