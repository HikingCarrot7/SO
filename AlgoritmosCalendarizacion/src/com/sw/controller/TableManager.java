package com.sw.controller;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author HikingCarrot7
 */
public class TableManager
{

    public void limpiarTabla(JTable tabla)
    {
        DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();

        while (tableModel.getRowCount() != 0)
            tableModel.removeRow(0);

        tabla.getParent().revalidate();
    }

    public void addRows(JTable tabla, Object[] rowData, int rows)
    {
        DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();

        for (int i = 0; i < rows; i++)
            tableModel.addRow(rowData);

        tabla.getParent().revalidate();
    }

    public void addRow(JTable tabla, Object[] rowData)
    {
        DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
        tableModel.addRow(rowData);
        tabla.getParent().revalidate();
    }

    public void addEmptyRow(JTable tabla)
    {
        DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
        tableModel.addRow(getEmptyRowData(tabla.getColumnCount()));
        tabla.getParent().revalidate();
    }

    public void eliminarUltimaColumna(JTable tabla)
    {
        TableColumnModel tableColumnModel = tabla.getColumnModel();
        tableColumnModel.removeColumn(tableColumnModel.getColumn(tableColumnModel.getColumnCount() - 1));
        tabla.getParent().revalidate();
    }

    public void eliminarUltimaFila(JTable tabla)
    {
        DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
        tableModel.removeRow(tableModel.getRowCount() - 1);
        tabla.getParent().revalidate();
    }

    public void copiarTablas(JTable src, JTable dest)
    {
        rellenarTabla(dest, obtenerDatosTabla(src));
    }

    public Object[][] obtenerDatosTabla(JTable table)
    {
        Object[][] data = new Object[obtenerNFilasTabla(table)][obtenernColsTabla(table)];

        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++)
                data[i][j] = table.getValueAt(i, j);

        return data;
    }

    public void rellenarTabla(JTable table, Object[][] data)
    {
        for (int i = 0; i < data.length; i++)
        {
            if (table.getRowCount() < data.length)
                addEmptyRow(table);

            for (int j = 0; j < data[i].length; j++)
                table.setValueAt(data[i][j], i, j);
        }

    }

    public Object[] getLastColumn(JTable table)
    {
        Object[] lastColumn = new Object[table.getRowCount()];

        for (int i = 0; i < lastColumn.length; i++)
            lastColumn[i] = table.getModel().getValueAt(i, table.getColumnCount() - 1);

        return lastColumn;
    }

    public Object[][] recortarUltimaColumna(Object[][] data)
    {
        Object[][] newData = new Object[data.length][data[0].length - 1];

        for (int i = 0; i < newData.length; i++)
            System.arraycopy(data[i], 0, newData[i], 0, newData[i].length);

        return newData;
    }

    public int obtenerNFilasTabla(JTable table)
    {
        return table.getRowCount();
    }

    public int obtenernColsTabla(JTable table)
    {
        return table.getColumnCount();
    }

    public Object[] getEmptyRowData(int cols)
    {
        Object[] data = new Object[cols];

        for (int i = 0; i < data.length; i++)
            data[i] = "";

        return data;
    }

    public boolean existeTabla(JTable table)
    {
        return table.getRowCount() > 0;
    }

}
