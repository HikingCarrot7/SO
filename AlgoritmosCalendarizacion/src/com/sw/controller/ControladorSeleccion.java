package com.sw.controller;

import com.sw.view.VistaRecogeDatos;
import com.sw.view.VistaSeleccion;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author HikingC7
 */
public class ControladorSeleccion implements ActionListener
{

    public static final String CLAVE_ALGORITMO_SJF = "SJF";
    public static final String CLAVE_ALGORITMO_SRTF = "SRTF";
    public static final String CLAVE_ALGORITMO_RR = "RR";

    private final VistaSeleccion VISTA_SELECCION;

    public ControladorSeleccion(VistaSeleccion vistaSeleccion)
    {
        this.VISTA_SELECCION = vistaSeleccion;
        initMyComponents();
    }

    private void initMyComponents()
    {
        VISTA_SELECCION.getSrtf().addActionListener(this);
        VISTA_SELECCION.getRr().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        switch (command)
        {
            case "srtf":
            case "sjf":

                EventQueue.invokeLater(() ->
                {
                    VistaRecogeDatos vistaRecogeDatos = new VistaRecogeDatos();
                    vistaRecogeDatos.setVisible(true);
                    vistaRecogeDatos.setLocationRelativeTo(null);
                    vistaRecogeDatos.setTitle("Preparando datos para simular el algoritmo " + command.toUpperCase());
                    vistaRecogeDatos.getEntradaNQuantum().setVisible(false);
                    vistaRecogeDatos.getEntradaValidaLabel().setVisible(false);
                    vistaRecogeDatos.getLabelQuantum().setVisible(false);
                    new ControladorRecogeDatos(vistaRecogeDatos, obtenerClaveAlgoritmoActual(command));
                });
                break;

            case "rr":

                EventQueue.invokeLater(() ->
                {
                    VistaRecogeDatos vistaRecogeDatos = new VistaRecogeDatos();
                    vistaRecogeDatos.setVisible(true);
                    vistaRecogeDatos.setLocationRelativeTo(null);
                    vistaRecogeDatos.setTitle("Preparando datos para simular el algoritmo Round Robin");
                    new ControladorRecogeDatos(vistaRecogeDatos, obtenerClaveAlgoritmoActual(command));
                });
                break;

            default:
                throw new AssertionError();
        }

        VISTA_SELECCION.dispose();
    }

    private String obtenerClaveAlgoritmoActual(String command)
    {
        switch (command)
        {
            case "srtf":
                return CLAVE_ALGORITMO_SRTF;
            case "sjf":
                return CLAVE_ALGORITMO_SJF;
            case "rr":
                return CLAVE_ALGORITMO_RR;
            default:
                throw new AssertionError();
        }
    }

}
