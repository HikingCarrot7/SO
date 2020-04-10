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

import com.sw.model.CeldaMemoria;
import com.sw.model.Particion;
import com.sw.model.Proceso;
import com.sw.view.TableCellManager;
import com.sw.view.TableCellRenderer;
import com.sw.view.TableHeaderRenderer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Eusebio Ajax
 */
public class TableManager
{

    private static TableManager instance;

    private TableManager()
    {

    }

    public void initTabla(JTable table)
    {
        table.setDefaultRenderer(Object.class, new TableCellRenderer());
        JTableHeader jTableHeader = table.getTableHeader();
        jTableHeader.setDefaultRenderer(new TableHeaderRenderer());
        jTableHeader.setReorderingAllowed(false);
        table.setTableHeader(jTableHeader);
        table.setDefaultEditor(Object.class, new TableCellManager());
        table.setGridColor(new Color(237, 237, 237));
        table.setRowSelectionAllowed(true);
        table.setRowHeight(20);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void initTableSelectionBehavior(JTable table)
    {
        initTableSelectionBehavior(table, new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                table.clearSelection();
            }
        });
    }

    public void initTableSelectionBehavior(JTable table, FocusListener focusListener)
    {
        table.addFocusListener(focusListener);

        table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "Clear selection");
        table.getActionMap().put("Clear selection", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                table.getSelectionModel().clearSelection();
            }
        });
    }

    public void actualizarTablaProcesos(JTable table, ArrayList<? extends Proceso> procesos)
    {
        vaciarTabla(table);
        DefaultTableModel tableModel = getDefaultTableModel(table);

        procesos.forEach(proceso ->
        {
            tableModel.addRow(new Object[]
            {
                proceso.getNombre(),
                proceso.getSize() + "K",
                proceso.getTiempoLlegada(),
                proceso.getDuracion()
            });
        });
    }

    public void actualizarTablaAreasLibres(JTable table, ArrayList<? extends CeldaMemoria> areasLibres)
    {
        vaciarTabla(table);
        DefaultTableModel tableModel = getDefaultTableModel(table);

        for (int i = 0; i < areasLibres.size(); i++)
        {
            CeldaMemoria areaLibre = areasLibres.get(i);

            tableModel.addRow(new Object[]
            {
                (i + 1),
                areaLibre.getInicio() + "K",
                areaLibre.getSize() + "K",
                "Disponible"
            });
        }
    }

    public void actualizarTablaParticiones(JTable table, ArrayList<? extends CeldaMemoria> particiones)
    {
        vaciarTabla(table);
        DefaultTableModel tableModel = getDefaultTableModel(table);

        for (int i = 0; i < particiones.size(); i++)
        {
            CeldaMemoria particion = particiones.get(i);

            tableModel.addRow(new Object[]
            {
                (i + 1),
                particion.getInicio() + "K",
                particion.getSize() + "K",
                "Ocupado",
                ((Particion) particion).getProceso().getNombre()
            });
        }
    }

    public void vaciarTabla(JTable table)
    {
        DefaultTableModel tableModel = getDefaultTableModel(table);

        while (tableModel.getRowCount() > 0)
            tableModel.removeRow(0);
    }

    public void anadirFilaTabla(JTable table, Object[] fila)
    {
        DefaultTableModel tableModel = getDefaultTableModel(table);
        tableModel.addRow(fila);
    }

    public void selecionarUltimaFila(JTable table)
    {
        table.clearSelection();
        int lastIndex = table.getModel().getRowCount() - 1;
        table.getSelectionModel().setSelectionInterval(lastIndex, lastIndex);
    }

    public int[] obtenerFilasSeleccionadas(JTable table)
    {
        return table.getSelectedRows();
    }

    public DefaultTableModel getDefaultTableModel(JTable table)
    {
        return (DefaultTableModel) table.getModel();
    }

    public synchronized static TableManager getInstance()
    {
        if (instance == null)
            instance = new TableManager();

        return instance;
    }

}
