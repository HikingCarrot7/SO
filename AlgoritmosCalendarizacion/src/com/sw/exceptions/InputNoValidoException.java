package com.sw.exceptions;

/**
 *
 * @author HikingCarrot7
 */
public abstract class InputNoValidoException extends RuntimeException
{

    private int row;
    private int col;

    public InputNoValidoException(String mensaje, int row, int col)
    {
        super(mensaje);
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

}
