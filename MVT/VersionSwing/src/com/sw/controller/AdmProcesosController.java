/*
 * The MIT License
 *
 * Copyright 2020 Eusebio Ajax.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sw.controller;

import com.sw.model.Proceso;
import com.sw.view.AdmProcesos;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Eusebio Ajax
 */
public class AdmProcesosController implements Controller<ArrayList<Proceso>>
{

    private final AdmProcesos vistaFrm;
    private final TableManager tableManager;
    private ArrayList<Proceso> procesos;
    private Proceso procesoSeleccionado;

    public AdmProcesosController(AdmProcesos vistaFrm, Controller<ArrayList<Proceso>> controller)
    {
        this.vistaFrm = vistaFrm;
        tableManager = TableManager.getInstance();
        initComponents(controller);
        initTableEvents();
    }

    private void initComponents(Controller<ArrayList<Proceso>> controller)
    {
        tableManager.initTabla(vistaFrm.getTablaProcesos());
        vistaFrm.getTamanioProceso().setFormatterFactory(new MyFormatterFactory());
        vistaFrm.getTiempoLlegada().setFormatterFactory(vistaFrm.getTamanioProceso().getFormatterFactory());
        vistaFrm.getDuracion().setFormatterFactory(vistaFrm.getTamanioProceso().getFormatterFactory());

        vistaFrm.getAnadirProceso().addActionListener(this::accionBtnAnadirProceso);
        vistaFrm.getModificarProceso().addActionListener(this::accionBtnModificarProceso);
        vistaFrm.getEliminarProceso().addActionListener(this::accionBtnEliminarProceso);

        vistaFrm.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                controller.establecerDatosPorDefecto(procesos);
            }
        });

        desactivarBotonesSecundarios();
    }

    private void initTableEvents()
    {
        final JTable tablaProcesos = vistaFrm.getTablaProcesos();

        tablaProcesos.getSelectionModel().addListSelectionListener(e ->
        {
            if (tablaProcesos.getSelectedRow() >= 0 && hayProcesos())
            {
                rellenarCampos(procesos.get(tablaProcesos.getSelectedRow()));
                procesoSeleccionado = getProcesoSeleccionado();
                activarBotonesSecundarios();
            }
        });

        tableManager.initTableSelectionBehavior(tablaProcesos, new FocusAdapter()
        {
        });
    }

    @Override
    public void establecerDatosPorDefecto(ArrayList<Proceso> procesos)
    {
        this.procesos = procesos;
        actualizarTablaProcesos();
    }

    private void accionBtnAnadirProceso(ActionEvent e)
    {
        if (entradasNumericasValidas())
            if (nombreProcesoValido(getNombreProceso()) && !nombreProcesoRepetido(getNombreProceso()))
                anadirNuevoProceso(getNuevoProceso());

            else if (mostrarConfirmacion("El nombre del proceso no es válido.",
                    "El nombre del proceso se reemplazará por: P" + (procesos.size() + 1)))
            {
                vistaFrm.getNombreProceso().setText("P" + (procesos.size() + 1));
                anadirNuevoProceso(getNuevoProceso());
            }
    }

    private void accionBtnModificarProceso(ActionEvent e)
    {
        if (entradasNumericasValidas())
            if (nombreProcesoValido(getNombreProceso())
                    && !nombreProcesoRepetido(getNombreProceso(), procesoSeleccionado))
                modificarProceso(procesoSeleccionado);

            else if (mostrarConfirmacion("El nombre del proceso no es válido.",
                    "El nombre del proceso se reemplazará por: P" + (procesos.size() + 1)))
            {
                vistaFrm.getNombreProceso().setText("P" + (procesos.size() + 1));
                modificarProceso(procesoSeleccionado);
            }
    }

    private void accionBtnEliminarProceso(ActionEvent e)
    {
        eliminarProcesosSelecionados();
        actualizarTablaProcesos();
        desactivarBotonesSecundarios();
        limpiarCampos();
    }

    private void anadirNuevoProceso(Proceso proceso)
    {
        procesos.add(proceso);
        actualizarTablaProcesos();
        desactivarBotonesSecundarios();
        tableManager.selecionarUltimaFila(vistaFrm.getTablaProcesos());
    }

    private void modificarProceso(Proceso proceso)
    {
        proceso.setNombre(getNombreProceso());
        proceso.setSize(getTamanioProceso());
        proceso.setLlegada(getTiempoLlegadaProceso());
        proceso.setDuracion(getDuracionProceso());

        actualizarTablaProcesos();
    }

    private void eliminarProcesosSelecionados()
    {
        int[] filasSeleccionadas = tableManager.obtenerFilasSeleccionadas(vistaFrm.getTablaProcesos());

        for (int i = filasSeleccionadas.length - 1; i >= 0; i--)
            eliminarProceso(procesos.get(filasSeleccionadas[i]));
    }

    private Proceso getNuevoProceso()
    {
        return new Proceso(getNombreProceso(), getTamanioProceso(), getTiempoLlegadaProceso(), getDuracionProceso());
    }

    private Proceso getProcesoSeleccionado()
    {
        return procesos.get(vistaFrm.getTablaProcesos().getSelectedRow());
    }

    private void eliminarProceso(Proceso proceso)
    {
        procesos.remove(proceso);
    }

    private boolean hayProcesos()
    {
        return procesos.size() > 0;
    }

    private boolean nombreProcesoValido(String nombre)
    {
        return !nombre.trim().isEmpty();
    }

    private boolean nombreProcesoRepetido(String nombre)
    {
        return procesos.stream().anyMatch(proceso -> proceso.getNombre().equals(nombre.trim()));
    }

    private boolean nombreProcesoRepetido(String nombre, Proceso procesoAIgnorar)
    {
        return procesos.stream().anyMatch(proceso -> proceso != procesoAIgnorar && proceso.getNombre().equals(nombre.trim()));
    }

    private boolean entradasNumericasValidas()
    {
        try
        {
            getTamanioProceso();
            getTiempoLlegadaProceso();
            getDuracionProceso();

        } catch (NumberFormatException e)
        {
            mostrarError("Compos incorrectos", "Algún campo es incorrecto.");
            return false;
        }

        return true;
    }

    private String getNombreProceso()
    {
        return vistaFrm.getNombreProceso().getText();
    }

    private int getTamanioProceso() throws NumberFormatException
    {
        return Integer.parseInt(vistaFrm.getTamanioProceso().getText());
    }

    private int getTiempoLlegadaProceso() throws NumberFormatException
    {
        return Integer.parseInt(vistaFrm.getTiempoLlegada().getText());
    }

    private int getDuracionProceso() throws NumberFormatException
    {
        return Integer.parseInt(vistaFrm.getDuracion().getText());
    }

    private void rellenarCampos(Proceso proceso)
    {
        vistaFrm.getNombreProceso().setText(proceso.getNombre());
        vistaFrm.getTamanioProceso().setText(String.valueOf(proceso.getSize()));
        vistaFrm.getTiempoLlegada().setText(String.valueOf(proceso.getTiempoLlegada()));
        vistaFrm.getDuracion().setText(String.valueOf(proceso.getDuracion()));
    }

    private void limpiarCampos()
    {
        vistaFrm.getNombreProceso().setText("");
        vistaFrm.getTamanioProceso().setText("");
        vistaFrm.getTiempoLlegada().setText("");
        vistaFrm.getDuracion().setText("");
    }

    private void activarBotonesSecundarios()
    {
        vistaFrm.getModificarProceso().setEnabled(true);
        vistaFrm.getEliminarProceso().setEnabled(true);
    }

    private void desactivarBotonesSecundarios()
    {
        vistaFrm.getModificarProceso().setEnabled(false);
        vistaFrm.getEliminarProceso().setEnabled(false);
    }

    private void actualizarTablaProcesos()
    {
        tableManager.actualizarTablaProcesos(vistaFrm.getTablaProcesos(), procesos);
    }

    private boolean mostrarConfirmacion(String titulo, String text)
    {
        return JOptionPane.showConfirmDialog(vistaFrm, text, titulo, JOptionPane.OK_CANCEL_OPTION) == 0;
    }

    private void mostrarError(String titulo, String text)
    {
        JOptionPane.showMessageDialog(vistaFrm, text, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public ArrayList<Proceso> getProcesos()
    {
        return procesos;
    }

}
