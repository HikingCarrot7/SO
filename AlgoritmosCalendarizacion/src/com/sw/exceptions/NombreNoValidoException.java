package com.sw.exceptions;

/**
 *
 * @author HikingCarrot7
 */
public class NombreNoValidoException extends InputNoValidoException
{

    private int row;
    private int col;

    public NombreNoValidoException(int row, int col)
    {
        super("Algunos nombres no son válidos, se tomará el identificador como nombre ¿Continuar?", row, col);
    }

}
