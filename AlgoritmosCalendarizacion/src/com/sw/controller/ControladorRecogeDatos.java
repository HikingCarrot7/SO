package com.sw.controller;

import com.sw.exceptions.NombreNoValidoException;
import com.sw.exceptions.ValorNoValidoException;
import com.sw.persistence.DAO;
import com.sw.view.VistaPrincipal;
import com.sw.view.VistaRecogeDatos;
import com.sw.view.VistaSeleccion;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author HikingCarrot7
 */
public class ControladorRecogeDatos implements ActionListener
{

    private final VistaRecogeDatos VISTA_RECOGE_DATOS;
    private final TableManager TABLE_MANAGER;
    private final String REGEX_ENTERO_POSITIVO_VALIDO = "^[0-9]+$";

    private final int COLS_ALGORITMO_SJF = 4;
    private final int COLS_ALGORITMO_SRTF = 4;
    private final int COLS_ALGORITMO_RR = 3;
    private String CLAVE_ALGORITMO_ACTUAL;

    public static final int COL_NOMBRE_PROCESO = 1;
    public static final int COL_TIEMPO_RAFAGA = 2;
    public static final int COL_TIEMPO_LLEGADA = 3;

    private Object[][] itemsSalvadosTabla;
    private final TableCellRenderer TABLE_RENDERER;

    public ControladorRecogeDatos(VistaRecogeDatos vistaRecogeDatos, final String CLAVE_ALGORITMO_ACTUAL)
    {
        this.VISTA_RECOGE_DATOS = vistaRecogeDatos;
        this.CLAVE_ALGORITMO_ACTUAL = CLAVE_ALGORITMO_ACTUAL;
        TABLE_MANAGER = new TableManager();
        TABLE_RENDERER = new TableCellRenderer();
        initMyComponents();
    }

    private void initMyComponents()
    {
        VISTA_RECOGE_DATOS.getAceptarNProcesos().addActionListener(this);
        VISTA_RECOGE_DATOS.getContinuar().addActionListener(this);
        VISTA_RECOGE_DATOS.getAleatorio().addActionListener(this);
        VISTA_RECOGE_DATOS.getRegresar().addActionListener(this);
        VISTA_RECOGE_DATOS.getBorrarTodo().addActionListener(this);
        VISTA_RECOGE_DATOS.getTablaRecogeDatos().setDefaultRenderer(Object.class, TABLE_RENDERER);
        VISTA_RECOGE_DATOS.getTablaRecogeDatos().setValueAt("P1", 0, 0);

        if (CLAVE_ALGORITMO_ACTUAL.equals(ControladorSeleccion.CLAVE_ALGORITMO_RR))
            TABLE_MANAGER.eliminarUltimaColumna(VISTA_RECOGE_DATOS.getTablaRecogeDatos());

        DAO dao = new DAO();

        if (dao.existSavedData())
        {
            Object[][] data = dao.getSavedData();
            TABLE_MANAGER.rellenarTabla(VISTA_RECOGE_DATOS.getTablaRecogeDatos(),
                    CLAVE_ALGORITMO_ACTUAL.equals(ControladorSeleccion.CLAVE_ALGORITMO_RR)
                    ? TABLE_MANAGER.recortarUltimaColumna(data) : data);

            VISTA_RECOGE_DATOS.getEntradaNProcesos().setValue(data.length);
        }
    }

    public void establecerDatosDefecto(JTable tabla, final String CLAVE_ALGORITMO_ACTUAL)
    {
        this.CLAVE_ALGORITMO_ACTUAL = CLAVE_ALGORITMO_ACTUAL;
        TABLE_MANAGER.copiarTablas(tabla, VISTA_RECOGE_DATOS.getTablaRecogeDatos());
        VISTA_RECOGE_DATOS.setTitle("Preparando datos para simular el algoritmo " + CLAVE_ALGORITMO_ACTUAL);
        VISTA_RECOGE_DATOS.getLabelQuantum().setVisible(false);
        VISTA_RECOGE_DATOS.getEntradaNQuantum().setVisible(false);
        VISTA_RECOGE_DATOS.getEntradaValidaLabel().setVisible(false);
        VISTA_RECOGE_DATOS.getEntradaNProcesos().setValue(tabla.getRowCount());
    }

    public void establecerDatosDefecto(JTable table, final long QUANTUMS)
    {
        CLAVE_ALGORITMO_ACTUAL = ControladorSeleccion.CLAVE_ALGORITMO_RR;
        TABLE_MANAGER.copiarTablas(table, VISTA_RECOGE_DATOS.getTablaRecogeDatos());
        VISTA_RECOGE_DATOS.setTitle("Preparando datos para simular el algoritmo Round Robin");
        VISTA_RECOGE_DATOS.getEntradaNProcesos().setValue(table.getRowCount());
        VISTA_RECOGE_DATOS.getEntradaNQuantum().setValue(QUANTUMS);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "aceptar":
                prepararTabla();
                break;

            case "continuar":
                if (todosDatosValidos())
                {
                    EventQueue.invokeLater(() ->
                    {
                        VistaPrincipal vistaPrincipal = new VistaPrincipal();
                        vistaPrincipal.setVisible(true);
                        vistaPrincipal.setLocationRelativeTo(null);
                        vistaPrincipal.revalidate();
                        vistaPrincipal.repaint();
                        ControladorVistaPrincipal controladorVistaPrincipal = new ControladorVistaPrincipal(vistaPrincipal, CLAVE_ALGORITMO_ACTUAL);

                        switch (CLAVE_ALGORITMO_ACTUAL)
                        {
                            case ControladorSeleccion.CLAVE_ALGORITMO_SJF:
                            case ControladorSeleccion.CLAVE_ALGORITMO_SRTF:
                                controladorVistaPrincipal.establecerDatosDefecto(VISTA_RECOGE_DATOS.getTablaRecogeDatos());
                                break;

                            case ControladorSeleccion.CLAVE_ALGORITMO_RR:
                                controladorVistaPrincipal.establecerDatosDefecto(
                                        VISTA_RECOGE_DATOS.getTablaRecogeDatos(),
                                        Long.parseLong(VISTA_RECOGE_DATOS.getEntradaNQuantum().getValue().toString()));
                                break;

                            default:
                                throw new AssertionError();
                        }

                        VISTA_RECOGE_DATOS.dispose();
                    });

                    saveTableData();
                }
                break;

            case "regresar":

                switch (mostrarMensaje("Confirmar", "¿Desea conservar los datos actuales?"))
                {
                    case 0:
                        saveTableData();
                        break;
                    case 1:
                        new DAO().removeAllSavedData();
                        break;
                }

                EventQueue.invokeLater(() ->
                {
                    VistaSeleccion vistaSeleccion = new VistaSeleccion();
                    vistaSeleccion.setVisible(true);
                    vistaSeleccion.setLocationRelativeTo(null);
                    new ControladorSeleccion(vistaSeleccion);
                    VISTA_RECOGE_DATOS.dispose();
                });

                break;

            case "aleatorios":
                if (confirmar("Confirmar acción", "Todos los datos insertados actualmente serán eliminados, ¿Continuar?"))
                {
                    arreglarNombresProcesos();
                    generarValoresTiempoAleatorios();
                }
                break;

            case "borrarTodo":
                if (confirmar("¿Seguro?", "Se eliminarán todos los datos. ¿Seguro?"))
                    borrarInfoTabla();
                break;

            default:
                break;
        }

    }

    private boolean todosDatosValidos()
    {
        JTable tabla = VISTA_RECOGE_DATOS.getTablaRecogeDatos();

        tabla.clearSelection();

        try
        {
            if (TABLE_MANAGER.existeTabla(tabla))
            {
                nombresProcesosValidos();
                tiemposProcesosValidos();
                return true;

            } else
                mostrarError("Aún no hay datos", "Aún no existe una tabla", JOptionPane.ERROR_MESSAGE);

        } catch (NombreNoValidoException ex)
        {
            TABLE_RENDERER.anadirPunto(ex.getRow(), ex.getCol());
            repintarTabla();
            if (confirmar(
                    String.format("Error en la fila %s y columna %s", ex.getRow() + 1, ex.getCol() + 1),
                    ex.getMessage()))
            {
                arreglarNombresProcesos();
                limpiarTabla();
                return todosDatosValidos();
            }

        } catch (ValorNoValidoException ex)
        {
            TABLE_RENDERER.anadirPunto(ex.getRow(), ex.getCol());
            repintarTabla();
            mostrarError("Error en la entrada de los datos", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        limpiarTabla();
        return false;
    }

    private void arreglarNombresProcesos()
    {
        Object[][] data = recogerDatos();

        for (Object[] row : data)
        {
            String nombre = row[COL_NOMBRE_PROCESO].toString().trim();

            if (nombre.equals("") || nombre.equals(" "))
                row[COL_NOMBRE_PROCESO] = row[COL_NOMBRE_PROCESO - 1];
        }

        TABLE_MANAGER.rellenarTabla(VISTA_RECOGE_DATOS.getTablaRecogeDatos(), data);
    }

    private boolean nombresProcesosValidos()
    {
        Object[][] data = recogerDatos();

        for (int i = 0; i < data.length; i++)
        {
            String nombre = data[i][COL_NOMBRE_PROCESO].toString().trim();

            if (nombre.equals("") || nombre.equals(" "))
                throw new NombreNoValidoException(i, COL_NOMBRE_PROCESO);
        }

        return true;
    }

    private boolean tiemposProcesosValidos()
    {
        Object[][] data = recogerDatos();

        for (int i = 0; i < data.length; i++)
            for (int j = COL_TIEMPO_RAFAGA; j < data[i].length; j++)
                switch (j)
                {
                    case COL_TIEMPO_RAFAGA:
                        if (!esEntradaValida(data[i][j].toString(), REGEX_ENTERO_POSITIVO_VALIDO) || Integer.parseInt(data[i][j].toString()) < 0)
                            throw new ValorNoValidoException(
                                    String.format("El tiempo ráfaga en la fila %s y columna %s no es válido", i + 1, j + 1), i, j);
                        break;

                    case COL_TIEMPO_LLEGADA:
                        if (!esEntradaValida(data[i][j].toString(), REGEX_ENTERO_POSITIVO_VALIDO) || Integer.parseInt(data[i][j].toString()) < 0)
                            throw new ValorNoValidoException(
                                    String.format("El tiempo de llegada en la fila %s y columna %s no es válido", i + 1, j + 1), i, j);
                        break;

                    default:
                        break;
                }

        return true;
    }

    private void prepararTabla()
    {
        JTable tabla = VISTA_RECOGE_DATOS.getTablaRecogeDatos();

        if (TABLE_MANAGER.existeTabla(tabla))
            salvarTabla();

        TABLE_MANAGER.limpiarTabla(tabla);
        rellenarTabla(Integer.parseInt(
                String.valueOf(VISTA_RECOGE_DATOS.getEntradaNProcesos().getValue())));
    }

    private void rellenarTabla(int rows)
    {
        Object[] rowData;

        for (int i = 0; i < rows; i++)
        {
            if (existenItemsGuardados() && itemsSalvadosTabla.length > i)
                rowData = itemsSalvadosTabla[i];

            else
                rowData = TABLE_MANAGER.getEmptyRowData(obtenerColsTablaActual());

            rowData[0] = "P" + (i + 1);
            TABLE_MANAGER.addRow(VISTA_RECOGE_DATOS.getTablaRecogeDatos(), rowData);
        }

        itemsSalvadosTabla = null;
    }

    private void borrarInfoTabla()
    {
        JTable tabla = VISTA_RECOGE_DATOS.getTablaRecogeDatos();

        for (int i = 0; i < tabla.getRowCount(); i++)
            for (int j = 1; j < tabla.getColumnCount(); j++)
                tabla.setValueAt("", i, j);
    }

    private void generarValoresTiempoAleatorios()
    {
        final int MIN_VALUE_RAFAGA = 1;
        final int MAX_VALUE_RAFAGA = 3;

        final int MIN_VALUE_LLEGADA = 500;
        final int MAX_VALUE_LLEGADA = 900;

        SecureRandom rand = new SecureRandom();
        JTable table = VISTA_RECOGE_DATOS.getTablaRecogeDatos();
        Object[][] data = TABLE_MANAGER.obtenerDatosTabla(table);

        for (Object[] row : data)
        {
            int number = rand.nextInt(MAX_VALUE_RAFAGA - MIN_VALUE_RAFAGA) + MIN_VALUE_RAFAGA;
            row[COL_TIEMPO_RAFAGA] = number;

            if (!CLAVE_ALGORITMO_ACTUAL.equals(ControladorSeleccion.CLAVE_ALGORITMO_RR))
            {
                number = rand.nextInt(MAX_VALUE_LLEGADA - MIN_VALUE_LLEGADA) + MIN_VALUE_LLEGADA;
                row[COL_TIEMPO_LLEGADA] = number;
            }
        }

        TABLE_MANAGER.rellenarTabla(table, data);
    }

    private void salvarTabla()
    {
        itemsSalvadosTabla = TABLE_MANAGER.obtenerDatosTabla(VISTA_RECOGE_DATOS.getTablaRecogeDatos());
    }

    private int obtenerColsTablaActual()
    {
        switch (CLAVE_ALGORITMO_ACTUAL)
        {
            case ControladorSeleccion.CLAVE_ALGORITMO_SJF:
                return COLS_ALGORITMO_SJF;
            case ControladorSeleccion.CLAVE_ALGORITMO_SRTF:
                return COLS_ALGORITMO_SRTF;
            case ControladorSeleccion.CLAVE_ALGORITMO_RR:
                return COLS_ALGORITMO_RR;
            default:
                throw new AssertionError();
        }
    }

    private Object[][] recogerDatos()
    {
        return TABLE_MANAGER.obtenerDatosTabla(VISTA_RECOGE_DATOS.getTablaRecogeDatos());
    }

    private boolean existenItemsGuardados()
    {
        return itemsSalvadosTabla != null;
    }

    private void mostrarError(String titulo, String text, int tipo)
    {
        JOptionPane.showMessageDialog(VISTA_RECOGE_DATOS, text, titulo, tipo);
    }

    private boolean confirmar(String titulo, String text)
    {
        return mostrarMensaje(titulo, text) == 0;
    }

    private int mostrarMensaje(String titulo, String text)
    {
        return JOptionPane.showConfirmDialog(VISTA_RECOGE_DATOS, text, titulo, JOptionPane.YES_NO_OPTION);
    }

    private boolean esEntradaValida(String text, String regex)
    {
        return text.matches(regex);
    }

    private void limpiarTabla()
    {
        TABLE_RENDERER.borrarTodosPuntos();
        repintarTabla();
    }

    private void repintarTabla()
    {
        VISTA_RECOGE_DATOS.getTablaRecogeDatos().updateUI();
    }

    private void saveTableData()
    {
        new DAO().saveData(TABLE_MANAGER.obtenerDatosTabla(VISTA_RECOGE_DATOS.getTablaRecogeDatos()));
    }

    private class TableCellRenderer extends MyTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            setBackground(existePunto(row, column) ? Color.red : Color.white);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

    }

}
