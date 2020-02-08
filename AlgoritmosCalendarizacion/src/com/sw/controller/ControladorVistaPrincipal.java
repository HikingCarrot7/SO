package com.sw.controller;

import com.sw.model.*;
import com.sw.view.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author HikingCarrot7
 */
public class ControladorVistaPrincipal implements ActionListener, Observer
{

    private final VistaPrincipal VISTA_PRINCIPAL;
    private final DibujadorEsquema DIBUJADOR_ESQUEMA;
    private final TableManager TABLE_MANAGER;
    private Despachador despachador;
    private Calendarizador calendarizador;
    private long QUANTUMS;
    private boolean simulacionInterrumpida;

    private final String CLAVE_ALGORITMO_ACTUAL;
    private final TableCellRenderer RENDERER;

    public ControladorVistaPrincipal(final VistaPrincipal VISTA_PRINCIPAL, final String CLAVE_ALGORITMO_ACTUAL)
    {
        this.VISTA_PRINCIPAL = VISTA_PRINCIPAL;
        this.CLAVE_ALGORITMO_ACTUAL = CLAVE_ALGORITMO_ACTUAL;
        DIBUJADOR_ESQUEMA = new DibujadorEsquema(VISTA_PRINCIPAL.getEsquema());
        DIBUJADOR_ESQUEMA.setMostrarCambioContexto(CLAVE_ALGORITMO_ACTUAL.equals(ControladorSeleccion.CLAVE_ALGORITMO_SRTF));
        TABLE_MANAGER = new TableManager();
        RENDERER = new TableCellRenderer();
        initMyComponents();
    }

    private void initMyComponents()
    {
        VISTA_PRINCIPAL.getRegresar().addActionListener(this);
        VISTA_PRINCIPAL.getSimulacion().addActionListener(this);
        VISTA_PRINCIPAL.getTablaEspera().setDefaultRenderer(Object.class, RENDERER);
        DIBUJADOR_ESQUEMA.crearRenderer();
        VISTA_PRINCIPAL.revalidate();
        VISTA_PRINCIPAL.repaint();
    }

    public void establecerDatosDefecto(JTable table)
    {
        TABLE_MANAGER.copiarTablas(table, VISTA_PRINCIPAL.getTablaResumen());
        VISTA_PRINCIPAL.setTitle("Simulando el algoritmo " + CLAVE_ALGORITMO_ACTUAL);
    }

    public void establecerDatosDefecto(JTable table, final long QUANTUMS)
    {
        TABLE_MANAGER.eliminarUltimaColumna(VISTA_PRINCIPAL.getTablaResumen());
        TABLE_MANAGER.copiarTablas(table, VISTA_PRINCIPAL.getTablaResumen());
        VISTA_PRINCIPAL.setTitle("Simulando el algoritmo Round Robin");
        DIBUJADOR_ESQUEMA.setQuantums(QUANTUMS);
        this.QUANTUMS = QUANTUMS;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "regresar":
                EventQueue.invokeLater(() ->
                {
                    VistaRecogeDatos vistaRecogeDatos = new VistaRecogeDatos();
                    vistaRecogeDatos.setVisible(true);
                    vistaRecogeDatos.setLocationRelativeTo(null);
                    ControladorRecogeDatos controladorRecogeDatos = new ControladorRecogeDatos(vistaRecogeDatos, CLAVE_ALGORITMO_ACTUAL);

                    switch (CLAVE_ALGORITMO_ACTUAL)
                    {
                        case ControladorSeleccion.CLAVE_ALGORITMO_SJF:
                        case ControladorSeleccion.CLAVE_ALGORITMO_SRTF:
                            controladorRecogeDatos.establecerDatosDefecto(VISTA_PRINCIPAL.getTablaResumen(), CLAVE_ALGORITMO_ACTUAL);
                            break;

                        case ControladorSeleccion.CLAVE_ALGORITMO_RR:
                            controladorRecogeDatos.establecerDatosDefecto(VISTA_PRINCIPAL.getTablaResumen(), QUANTUMS);
                            break;

                        default:
                            throw new AssertionError();
                    }

                    if (despachador != null)
                        despachador.detenerDespachador();

                    DIBUJADOR_ESQUEMA.destroyRenderer();
                    VISTA_PRINCIPAL.dispose();
                });
                break;

            case "iniciarSimulacion":

                if (calendarizador != null && !calendarizador.todosProcesosTerminados())
                {
                    if (confirmar("Confirmar", "¿Seguro que desea reiniciar la simulación?"))
                    {
                        simulacionInterrumpida = true;
                        reiniciarSimulacion();
                    }

                } else
                {
                    VISTA_PRINCIPAL.getSimulacion().setText("Reiniciar simulación");
                    crearSimulacion();
                }

                break;

            default:
                break;
        }
    }

    private void crearSimulacion()
    {
        final CPU CPU = new CPU();
        ArrayList<Proceso> procesos = null;
        DIBUJADOR_ESQUEMA.reiniciarEsquema();
        limpiarTablas();

        switch (CLAVE_ALGORITMO_ACTUAL)
        {
            case ControladorSeleccion.CLAVE_ALGORITMO_SJF:
                procesos = obtenerProcesosSJF();
                despachador = new DespachadorSJF(CPU);
                break;
            case ControladorSeleccion.CLAVE_ALGORITMO_SRTF:
                procesos = obtenerProcesosSRTF();
                despachador = new DespachadorSRTF(CPU);
                break;
            case ControladorSeleccion.CLAVE_ALGORITMO_RR:
                procesos = obtenerProcesosRR();
                despachador = new DespachadorRR(CPU, QUANTUMS);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        despachador.addObserver(this);
        calendarizador = new Calendarizador(procesos, despachador, CLAVE_ALGORITMO_ACTUAL.equals(ControladorSeleccion.CLAVE_ALGORITMO_SRTF));
        simulacionInterrumpida = false;
    }

    private void reiniciarSimulacion()
    {
        despachador.reiniciarDespachador();
        calendarizador.reiniciarCalendarizador();
        DIBUJADOR_ESQUEMA.reiniciarEsquema();
        RENDERER.borrarTodosPuntos();
        calendarizador = null;
        despachador = null;

        limpiarTablas();
        VISTA_PRINCIPAL.getSimulacion().setText("Iniciar simulación");
    }

    private void limpiarTablas()
    {
        TABLE_MANAGER.limpiarTabla(VISTA_PRINCIPAL.getTablaEspera());
        TABLE_MANAGER.limpiarTabla(VISTA_PRINCIPAL.getTablaProcesosFinalizados());
    }

    //--------------------------------------------------- REVISAR
    private ArrayList<Proceso> obtenerProcesosSJF()
    {
        ArrayList<Proceso> procesos = new ArrayList<>();
        JTable table = VISTA_PRINCIPAL.getTablaResumen();
        Object[][] data = TABLE_MANAGER.obtenerDatosTabla(table);

        for (int i = 0; i < table.getRowCount(); i++)
            procesos.add(new ProcesoSJF(
                    Estado.NUEVO,
                    data[i][ControladorRecogeDatos.COL_NOMBRE_PROCESO].toString(),
                    i,
                    Long.parseLong(data[i][ControladorRecogeDatos.COL_TIEMPO_RAFAGA].toString()),
                    Long.parseLong(data[i][ControladorRecogeDatos.COL_TIEMPO_LLEGADA].toString())));

        return procesos;
    }

    private ArrayList<Proceso> obtenerProcesosSRTF()
    {
        ArrayList<Proceso> procesos = new ArrayList<>();
        JTable table = VISTA_PRINCIPAL.getTablaResumen();
        Object[][] data = TABLE_MANAGER.obtenerDatosTabla(table);

        for (int i = 0; i < table.getRowCount(); i++)
            procesos.add(new ProcesoSRTF(
                    Estado.NUEVO,
                    data[i][ControladorRecogeDatos.COL_NOMBRE_PROCESO].toString(),
                    i,
                    Long.parseLong(data[i][ControladorRecogeDatos.COL_TIEMPO_RAFAGA].toString()),
                    Long.parseLong(data[i][ControladorRecogeDatos.COL_TIEMPO_LLEGADA].toString())));

        return procesos;
    }

    private ArrayList<Proceso> obtenerProcesosRR()
    {
        ArrayList<Proceso> procesos = new ArrayList<>();
        JTable table = VISTA_PRINCIPAL.getTablaResumen();
        Object[][] data = TABLE_MANAGER.obtenerDatosTabla(table);

        for (int i = 0; i < table.getRowCount(); i++)
            procesos.add(new ProcesoRR(
                    Estado.NUEVO,
                    data[i][ControladorRecogeDatos.COL_NOMBRE_PROCESO].toString(),
                    i,
                    Long.parseLong(data[i][ControladorRecogeDatos.COL_TIEMPO_RAFAGA].toString())));

        return procesos;
    }

    //-----------------------------------------------------------------------
    private void anadirProcesoTablaTiempoEspera(Proceso proceso, long tiempoTranscurrido)
    {
        TABLE_MANAGER.addRow(VISTA_PRINCIPAL.getTablaEspera(), new Object[]
        {
            proceso.getIdentificador(), tiempoTranscurrido
        });
    }

    private void anadirProcesoTablaFinalizados(Proceso proceso, long tiempoEnQueProcesoFinalizo)
    {
        TABLE_MANAGER.addRow(VISTA_PRINCIPAL.getTablaProcesosFinalizados(), new Object[]
        {
            proceso.getIdentificador(), tiempoEnQueProcesoFinalizo
        });
    }

    @Override
    public void update(Observable o, Object arg)
    {
        Notificacion notificacion = (Notificacion) arg;
        Proceso proceso = notificacion.getProceso();

        if (!simulacionInterrumpida)
            switch (notificacion.getIdentificador())
            {
                case Notificacion.PROCESO_HA_FINALIZADO:
                    DIBUJADOR_ESQUEMA.marcarUltimoProceso();
                    anadirProcesoTablaFinalizados(proceso, notificacion.getTiempoEnQueFinalizoProceso());
                    anadirProcesoTablaTiempoEspera(proceso, notificacion.getTiempoEsperaProceso());
                    RENDERER.anadirFila(VISTA_PRINCIPAL.getTablaEspera().getRowCount() - 1);

                    if (calendarizador.todosProcesosTerminados())
                    {
                        VISTA_PRINCIPAL.getSimulacion().setText("Iniciar simulación");
                        DIBUJADOR_ESQUEMA.mostrarNoHayProcesoActual();
                        despachador.detenerDespachador();
                    }
                    break;

                case Notificacion.CAMBIO_CONTEXTO:
                    DIBUJADOR_ESQUEMA.mostrarEnProcesadorProcesoActual(proceso, notificacion.getTiempoUsoCPU());
                    DIBUJADOR_ESQUEMA.actualizarDiagramaGantt(proceso, notificacion.getTiempoEsperaProceso());
                    break;

                case Notificacion.PROCESO_DEJO_CPU:
                    anadirProcesoTablaTiempoEspera(proceso, notificacion.getTiempoEsperaProceso());
                    break;

                case Notificacion.INTERRUPCION:
                    DIBUJADOR_ESQUEMA.dibujarInterrupcion();
                    break;

                default:
                    break;
            }

    }

    private boolean confirmar(String titulo, String text)
    {
        return JOptionPane.showConfirmDialog(VISTA_PRINCIPAL, text, titulo, JOptionPane.YES_NO_OPTION) == 0;
    }

    private class TableCellRenderer extends MyTableCellRenderer
    {

        @Override
        public synchronized Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            boolean existeFila = existeFila(row);
            setBackground(existeFila ? Color.red : Color.white);
            setForeground(existeFila ? Color.white : Color.black);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }

}
