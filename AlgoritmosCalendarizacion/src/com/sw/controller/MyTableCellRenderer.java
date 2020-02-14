package com.sw.controller;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author HikingCarrot7
 */
public class MyTableCellRenderer extends DefaultTableCellRenderer
{

    private ArrayList<Point> puntos;

    public MyTableCellRenderer()
    {
        puntos = new ArrayList<>();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    protected boolean existePunto(int row, int col)
    {
        return puntos.stream().anyMatch(p -> p.y == row && p.x == col);
    }

    protected boolean existeFila(int row)
    {
        for (int i = 0; i < puntos.size(); i++)
        {
            Point p = puntos.get(i);

            if (p.y == row)
                return true;
        }

        return false;
    }

    public void anadirFila(int row)
    {
        puntos.add(new Point(-1, row));
    }

    public void anadirPunto(int row, int col)
    {
        puntos.add(new Point(col, row));
    }

    public void borrarTodosPuntos()
    {
        puntos.clear();
    }

}
