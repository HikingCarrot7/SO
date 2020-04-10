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
package com.sw.model;

/**
 *
 * @author Equipo 1
 */
public abstract class CeldaMemoria implements Comparable<CeldaMemoria>
{

    private int inicio;
    private int size;

    public CeldaMemoria()
    {
        this(0, 0);
    }

    public CeldaMemoria(int inicio, int size)
    {
        this.inicio = inicio;
        this.size = size;
    }

    public int getInicio()
    {
        return inicio;
    }

    public void setInicio(int inicio)
    {
        this.inicio = inicio;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void ocupar(int size)
    {
        setSize(size);
    }

    public void ocupar(int inicio, int size)
    {
        setInicio(inicio);
        setSize(size);
    }

    @Override
    public String toString()
    {
        return "CeldaMemoria{" + "inicio=" + inicio + ", size=" + size + '}';
    }

    @Override
    public int compareTo(CeldaMemoria o)
    {
        return getInicio() - o.getInicio();
    }

}
