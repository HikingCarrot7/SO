/*
 * The MIT License
 *
 * Copyright 2020 SonBear.
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
package com.sw.model;

/**
 *
 * @author SonBear
 */
public class Proceso
{

    private String nombre;
    private int size;
    private int tiempoLlegada;
    private int duracion;
    private int nProceso;

    public Proceso(String nombre, int size, int llegada, int duracion)
    {
        this.nombre = nombre;
        this.size = size;
        this.tiempoLlegada = llegada;
        this.duracion = duracion;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getTiempoLlegada()
    {
        return tiempoLlegada;
    }

    public void setLlegada(int llegada)
    {
        this.tiempoLlegada = llegada;
    }

    public int getDuracion()
    {
        return duracion;
    }

    public void setDuracion(int duracion)
    {
        this.duracion = duracion;
    }

    public int getNProceso()
    {
        return nProceso;
    }

    public void setNProceso(int nProceso)
    {
        this.nProceso = nProceso;
    }

    @Override
    public String toString()
    {
        return "Proceso{" + "nombre=" + nombre + ", size=" + size + ", llegada=" + tiempoLlegada + ", duracion=" + duracion + ", nProceso=" + nProceso + '}';
    }

}
