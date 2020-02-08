package com.sw.main;

import com.sw.controller.ControladorSeleccion;
import com.sw.view.VistaSeleccion;
import java.awt.EventQueue;

/**
 *
 * @author HikingC7
 */
public class PruebaAlgoritmo
{

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            VistaSeleccion vistaSeleccion = new VistaSeleccion();
            vistaSeleccion.setVisible(true);
            vistaSeleccion.setLocationRelativeTo(null);
            new ControladorSeleccion(vistaSeleccion);
        });
    }

}
