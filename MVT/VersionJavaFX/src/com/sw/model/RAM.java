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

import java.util.RandomAccess;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author SonBear
 */
public class RAM implements RandomAccess, Volatil
{

    private final int MAX_TAM_MEMORIA;

    private ObservableList<Fragmento> fragmentos;
    private final ObservableList<AreaLibre> areasLibres;
    private final ObservableList<Particion> particiones;

    public RAM(final int MAX_TAM_MEMORIA)
    {
        areasLibres = observableArrayList();
        particiones = observableArrayList();
        fragmentos = observableArrayList();

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

    public ObservableList<AreaLibre> getAreasLibres()
    {
        return areasLibres;
    }

    public Particion getParticion(int indiceParticion)
    {
        return particiones.get(indiceParticion);
    }

    public ObservableList<Particion> getParticiones()
    {
        return particiones;
    }

    public ObservableList<Fragmento> getFragmentos()
    {
        return fragmentos;
    }

    public void setFragmentos(ObservableList<Fragmento> fragmentos)
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
