package com.sw.exceptions;

/**
 *
 * @author HikingCarrot7
 */
public class ValorNoValidoException extends InputNoValidoException
{

    public ValorNoValidoException(String mensaje, int row, int col)
    {
        super(mensaje, row, col);
    }

}
