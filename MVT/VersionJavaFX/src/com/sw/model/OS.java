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

import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author SonBear
 */
@OSVersion(version = "Windows 10")
public class OS extends Observable implements Observer, Notificador
{

    public static final int MOMENTO_INICIAL = 0;
    public static final int MOMENTO_FINAL = -1;

    private final MemoryHandler memoryHandler;
    private final ProcessHandler processHandler;
    private final int MEMORIA_OS;
    private int momento;

    public OS(final int MEMORIA_OS, RAM ram, ObservableList<Proceso> procesos)
    {
        this.MEMORIA_OS = MEMORIA_OS;
        this.momento = MOMENTO_INICIAL;
        memoryHandler = new MemoryHandler(ram);
        processHandler = new ProcessHandler(ram, FXCollections.observableArrayList(procesos));

        memoryHandler.addObserver(this);
        processHandler.addObserver(this);

        iniciarOS(ram);
    }

    private void iniciarOS(RAM ram)
    {
        AreaLibre areaLibre = new AreaLibre(MEMORIA_OS, ram.MAX_TAM_MEMORIA() - MEMORIA_OS);
        ram.eliminarTodosDatos();
        ram.anadirAreaLibre(areaLibre);
    }

    /**
     * Siguiente momento para este {@link OS}.
     */
    public void siguienteMomento()
    {
        if (processHandler.hayProcesosPorDespachar())
        {
            int celdaATerminar = processHandler.siguienteProcesoATerminar();

            if (celdaATerminar >= 0)
                processHandler.retirarProcesoEnMemoria(celdaATerminar);
            else
                processHandler.insertarProcesoEnMemoria();

        } else if (processHandler.hayProcesosEnMemoria())
            if (processHandler.hayProcesosEnEspera())
            {
                if (!processHandler.insertarProcesoEnMemoria())
                    processHandler.retirarSiguienteProceso();

            } else
                processHandler.retirarSiguienteProceso();

        else if (processHandler.hayProcesosEnEspera())
            processHandler.insertarProcesoEnMemoria();

        else
        {
            notificar("La simulación ha terminado");
            momento = MOMENTO_FINAL;
        }

        memoryHandler.compactarMemoria();
        memoryHandler.revisarFragmentacion();

        if (momento != MOMENTO_FINAL)
            momento++;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        notificar(arg.toString());
    }

    @Override
    public void notificar(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
        clearChanged();
    }

    public int MEMORIA_OS()
    {
        return MEMORIA_OS;
    }

    public int getMomento()
    {
        return momento;
    }

    public MemoryHandler getMemoryHandler()
    {
        return memoryHandler;
    }

    public ProcessHandler getProcessHandler()
    {
        return processHandler;
    }

}
