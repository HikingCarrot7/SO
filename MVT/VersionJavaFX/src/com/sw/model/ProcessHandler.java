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

import java.util.Comparator;
import java.util.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author SonBear
 */
public class ProcessHandler extends Observable implements Notificador
{

    private final RAM ram;
    private final ObservableList<Proceso> procesos;
    private final ObservableList<Proceso> procesosEnEspera;

    public ProcessHandler(RAM ram, ObservableList<Proceso> procesos)
    {
        this.ram = ram;
        this.procesos = procesos;
        this.procesosEnEspera = FXCollections.observableArrayList();
        ordenarProcesosPorTiempoLlegada(procesos);
    }

    public boolean insertarProcesoEnMemoria()
    {
        Proceso procesoAInsertar;
        boolean insertado;

        // Primero checamos la cola de espera.
        if (hayProcesosEnEspera())
        {
            procesoAInsertar = obtenerSigProcesoEnEspera(); // Obtenemos al primer proceso que esté ESPERANDO.
            insertado = insertarProceso(procesoAInsertar); // Tratamos de añadirlo a la RAM.

            if (insertado) // Si pudimos añadirlo.
            {
                notificar("Se insertó: " + procesoAInsertar.getNombre());
                eliminarSigProcesoEnEspera(); // Lo eliminamos de la cola de espera.
                return insertado;

            } else if (hayProcesosPorDespachar()) // Si se llegamos hasta aquí es porque se intentó insertar un proceso de la cola de espera pero no se logró, por lo que se intentará insertar un proceso de la cola principal.
            {
                procesoAInsertar = obtenerSigProcesoEnCola();
                insertado = insertarProceso(procesoAInsertar);

                if (insertado)
                {
                    notificar("Se insertó: " + procesoAInsertar.getNombre());
                    eliminarSigProcesoEnCola();
                    return insertado;

                } else
                {
                    notificar("No se pudo insertar: " + procesoAInsertar.getNombre());
                    anadirProcesoEnEspera(procesoAInsertar);
                    eliminarSigProcesoEnCola();
                    return insertado;
                }
            }

        } else // Si no hay nada en la cola de espera.
        {
            procesoAInsertar = obtenerSigProcesoEnCola(); // Tomamos al primer proceso de la cola de procesos.
            insertado = insertarProceso(procesoAInsertar); // Tratamos de insertarlo en la RAM.

            if (insertado) // Si se pudo insertar.
            {
                notificar("Se insertó: " + procesoAInsertar.getNombre());
                eliminarSigProcesoEnCola(); // Lo eliminamos de la cola de procesos.
                return insertado;

            } else
            {
                notificar("No se pudo insertar: " + procesoAInsertar.getNombre());
                anadirProcesoEnEspera(procesoAInsertar); // Añadimos el proceso a la cola de espera, no se pudo añadir a la RAM.
                eliminarSigProcesoEnCola(); // Lo sacamos de la cola de procesos.
                return insertado;
            }
        }

        notificar("No se pudo insertar: " + procesoAInsertar.getNombre());
        return insertado;
    }

    /**
     * Tratará de insertar un {@link Proceso} en la {@link RAM}, en caso de ser posible procederá con la operación. Cancelará la operación en caso contrario.
     *
     * @param procesoAInsertar El {@link Proceso} que se tratará de insertar.
     *
     * @return <code>true</code> si el {@link Proceso} se pudo insertar, <code>false</code> en caso contrario.
     */
    private boolean insertarProceso(Proceso procesoAInsertar)
    {
        for (int i = 0; i < ram.getAreasLibres().size(); i++)
        {
            AreaLibre areaLibre = ram.getAreaLibre(i);
            int tamCelda = areaLibre.getSize();

            if (tamCelda >= procesoAInsertar.getSize())
            {
                int tamRestante = areaLibre.getSize() - procesoAInsertar.getSize();
                ram.eliminarAreaLibre(i);
                ram.anadirParticion(new Particion(procesoAInsertar, areaLibre.getInicio(), procesoAInsertar.getSize()));

                // Crear una nueva celda si sobra espacio.
                if (tamRestante > 0)
                    ram.anadirAreaLibre(new AreaLibre(areaLibre.getInicio() + procesoAInsertar.getSize(), tamRestante));

                return true;
            }
        }

        return false;
    }

    /**
     * Elimina la {@link Particion} especificada de la memoria {@link RAM}.
     *
     * @param indiceParticion El índice de la {@link Particion} ha ser removida.
     */
    public void retirarProcesoEnMemoria(int indiceParticion)
    {
        retirarProcesoEnMemoria(ram.getParticion(indiceParticion));
    }

    /**
     * Elimina la {@link Particion} especificada de la memoria {@link RAM}.
     *
     * @param particion La {@link Particion} ha ser removida.
     */
    public void retirarProcesoEnMemoria(Particion particion)
    {
        ram.anadirAreaLibre(new AreaLibre(particion));
        ram.eliminarParticion(particion);
        notificar("Se retiró: " + particion.getProceso().getNombre());
    }

    /**
     * Retorna el índice del {@link Proceso} que terminará a continuación.
     *
     * @return El índice del {@link Proceso} que terminará a continuación o -1 si no hay.
     */
    public int siguienteProcesoATerminar()
    {
        ObservableList<Particion> particiones = ram.getParticiones();

        for (int i = 0; i < particiones.size(); i++)
        {
            Proceso procesoEnMemoria = particiones.get(i).getProceso();
            Proceso procesoEnCola = procesos.get(0);

            int finalizacion = procesoEnMemoria.getTiempoLlegada() + procesoEnMemoria.getDuracion();

            if (finalizacion <= procesoEnCola.getTiempoLlegada())
                return i;
        }

        return -1;
    }

    /**
     * Retira al siguiente {@link Proceso} de la {@link RAM}. El proceso a retirar es determinado por la llegada + duración más corta.
     */
    public void retirarSiguienteProceso()
    {
        ObservableList<Particion> particiones = ram.getParticiones();

        // Encontrar el proceso con la llegada + duración más corta.
        Particion particionARetirar = particiones.get(0);
        Proceso proceso = particiones.get(0).getProceso();
        int menorTiempoFinalizacion = proceso.getTiempoLlegada() + proceso.getDuracion();

        for (int i = 1; i < particiones.size(); i++)
        {
            proceso = particiones.get(i).getProceso();
            int tiempoFinalizacion = proceso.getTiempoLlegada() + proceso.getDuracion();

            if (tiempoFinalizacion < menorTiempoFinalizacion)
            {
                menorTiempoFinalizacion = tiempoFinalizacion;
                particionARetirar = particiones.get(i);
            }
        }

        retirarProcesoEnMemoria(particionARetirar);
        notificar("Se retiró: " + particionARetirar.getProceso().getNombre());
    }

    private void ordenarProcesosPorTiempoLlegada(ObservableList<Proceso> procesos)
    {
        procesos.sort(Comparator.comparing(Proceso::getTiempoLlegada));
    }

    @Override
    public void notificar(String mensaje)
    {
        setChanged();
        notifyObservers(mensaje);
        clearChanged();
    }

    private void anadirProcesoEnEspera(Proceso procesoEnEspera)
    {
        procesosEnEspera.add(procesoEnEspera);
    }

    private void anadirProcesoEnCola(Proceso procesoEnCola)
    {
        procesos.add(procesoEnCola);
    }

    private Proceso obtenerSigProcesoEnEspera()
    {
        return obtenerProcesoEnEspera(0);
    }

    private Proceso obtenerSigProcesoEnCola()
    {
        return obtenerProcesoEnCola(0);
    }

    private Proceso obtenerProcesoEnEspera(int indexProcesoEspera)
    {
        return procesosEnEspera.get(indexProcesoEspera);
    }

    private Proceso obtenerProcesoEnCola(int indexProcesoCola)
    {
        return procesos.get(indexProcesoCola);
    }

    private void eliminarSigProcesoEnEspera()
    {
        eliminarProcesoEnEspera(0);
    }

    private void eliminarSigProcesoEnCola()
    {
        eliminarProcesoEnCola(0);
    }

    private void eliminarProcesoEnEspera(int indexProcesoEspera)
    {
        procesosEnEspera.remove(indexProcesoEspera);
    }

    private void eliminarProcesoEnCola(int indexProcesoCola)
    {
        procesos.remove(indexProcesoCola);
    }

    public boolean hayProcesosEnEspera()
    {
        return !getProcesosEspera().isEmpty();
    }

    public boolean hayProcesosPorDespachar()
    {
        return !getColaProcesos().isEmpty();
    }

    public boolean hayProcesosEnMemoria()
    {
        return !ram.getParticiones().isEmpty();
    }

    public ObservableList<Proceso> getColaProcesos()
    {
        return procesos;
    }

    public ObservableList<Proceso> getProcesosEspera()
    {
        return procesosEnEspera;
    }

}
