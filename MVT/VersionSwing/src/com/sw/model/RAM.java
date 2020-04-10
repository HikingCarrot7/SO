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

import java.util.ArrayList;
import java.util.RandomAccess;

/**
 *
 * @author SonBear
 */
public class RAM implements RandomAccess, Volatil
{

    private final int MAX_TAM_MEMORIA;

    private ArrayList<Fragmento> fragmentos;
    private final ArrayList<AreaLibre> areasLibres;
    private final ArrayList<Particion> particiones;

    public RAM(final int MAX_TAM_MEMORIA)
    {
        areasLibres = new ArrayList<>();
        particiones = new ArrayList<>();
        fragmentos = new ArrayList<>();

        this.MAX_TAM_MEMORIA = MAX_TAM_MEMORIA;
    }

    public void anadirAreaLibre(AreaLibre areaLibre)
    {
        areasLibres.add(areaLibre);
    }

    public void eliminarAreaLibre(AreaLibre areaLibre)
    {
        areasLibres.remove(areaLibre);
    }

    public void eliminarAreaLibre(int indiceAreaLibre)
    {
        areasLibres.remove(indiceAreaLibre);
    }

    public void anadirParticion(Particion particion)
    {
        particiones.add(particion);
    }

    public void eliminarParticion(Particion particion)
    {
        particiones.remove(particion);
    }

    public void eliminarParticion(int indiceParticion)
    {
        particiones.remove(indiceParticion);
    }

    public AreaLibre getAreaLibre(int indiceAreaLibre)
    {
        return areasLibres.get(indiceAreaLibre);
    }

    public ArrayList<AreaLibre> getAreasLibres()
    {
        return areasLibres;
    }

    public Particion getParticion(int indiceParticion)
    {
        return particiones.get(indiceParticion);
    }

    public ArrayList<Particion> getParticiones()
    {
        return particiones;
    }

    public ArrayList<Fragmento> getFragmentos()
    {
        return fragmentos;
    }

    public void setFragmentos(ArrayList<Fragmento> fragmentos)
    {
        this.fragmentos = fragmentos;
    }

    public int MAX_TAM_MEMORIA()
    {
        return MAX_TAM_MEMORIA;
    }

    @Override
    public void eliminarTodosDatos()
    {
        areasLibres.clear();
        particiones.clear();
        fragmentos.clear();
    }

}
