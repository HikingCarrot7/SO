package com.sw.model;

import java.util.ArrayDeque;
import java.util.Observable;

/**
 *
 * @author HikingC7
 */
public abstract class Despachador extends Observable implements Runnable
{

    protected CPU cpu;
    protected volatile ArrayDeque<Proceso> procesos;
    protected long tiempoTotalUsoCPU; // Es el tiempo (en ms) que ha pasado desde que se ejecutó el primer proceso en el cpu.
    protected volatile boolean running;
    protected volatile boolean todosProcesosEntregados;

    public Despachador(final CPU CPU)
    {
        this.cpu = CPU;
        this.procesos = new ArrayDeque<>();
    }

    protected void despacharProcesos()
    {
        running = true;
        new Thread(this).start();
    }

    public void aceptarProceso(Proceso proceso)
    {
        procesos.add(proceso);
        proceso.PCB.setEstadoProceso(Estado.LISTO);
    }

    public void cambiarContexto(Proceso proceso)
    {
        cambiarContexto(proceso, proceso.PCB.getTiempoRafaga());
    }

    public void cambiarContexto(Proceso proceso, long tiempoUsoCPU)
    {
        proceso.PCB.setEstadoProceso(Estado.EJECUCION);
        cpu.ejecutarProceso(proceso, tiempoUsoCPU);
    }

    @Override
    public abstract void run();

    public void notificar(Notificacion notificacion)
    {
        setChanged();
        notifyObservers(notificacion);
    }

    protected void esperar()
    {
        while (cpu != null && cpu.isOcupado())
        {
        }
    }

    protected boolean hayProcesosEsperando()
    {
        return !procesos.isEmpty();
    }

    protected long tiempoEsperaProceso(Proceso proceso)
    {
        return tiempoTotalUsoCPU - proceso.getTiempoLlegada() <= 0 ? 0 : tiempoTotalUsoCPU - proceso.getTiempoLlegada();
    }

    public void detenerDespachador()
    {
        running = false;
    }

    public void reiniciarDespachador()
    {
        running = false;
        cpu.interrumpirProceso();
        cpu = null;
        tiempoTotalUsoCPU = 0;
        procesos.clear();
    }

    protected void todosProcesosEntragados()
    {
        todosProcesosEntregados = true;
    }

}
