package com.sw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 *
 * @author HikingCarrot7
 */
public class Calendarizador implements Runnable, Observer
{

    private ArrayList<Proceso> procesosAEntregar;
    private ArrayList<Proceso> procesosTerminados;
    private Despachador despachador;

    public Calendarizador(ArrayList<Proceso> procesosAEntregar, Despachador despachador)
    {
        this.procesosAEntregar = procesosAEntregar;
        this.despachador = despachador;
        procesosTerminados = new ArrayList<>();
        despachador.addObserver(this);
        ordenarProcesosTiempoLlegada();
        entregarProcesosADespachador();
    }

    private void ordenarProcesosTiempoLlegada()
    {
        procesosAEntregar = procesosAEntregar.stream()
                .sorted(Comparator.comparing(Proceso::getTiempoLlegada))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void entregarProcesosADespachador()
    {
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        despachador.despacharProcesos();

        for (int i = 0; i < procesosAEntregar.size(); i++)
        {
            Proceso proceso = procesosAEntregar.get(i);
            despachador.aceptarProceso(proceso);
        }

        despachador.todosProcesosEntragados();
    }

    @Override
    public void update(Observable o, Object notif)
    {
        Notificacion notificacion = (Notificacion) notif;

        if (notificacion.getIdentificador() == Notificacion.PROCESO_HA_FINALIZADO)
            procesosTerminados.add(notificacion.getProceso());
    }

    public boolean todosProcesosTerminados()
    {
        return procesosAEntregar.size() == procesosTerminados.size();
    }

    public void reiniciarCalendarizador()
    {
        procesosAEntregar.clear();
        procesosTerminados.clear();
    }

}
