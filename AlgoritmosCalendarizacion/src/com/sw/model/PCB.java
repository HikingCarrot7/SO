package com.sw.model;

/**
 *
 * @author HikingCarrot7
 */
public class PCB implements Comparable<PCB>
{

    private Estado estadoProceso;
    private int numProceso;
    private long tiempoRafaga;
    private long tiempoEjecutado;

    public PCB(Estado estadoProceso, int numProceso, long tiempoRafaga)
    {
        this.estadoProceso = estadoProceso;
        this.numProceso = numProceso;
        this.tiempoRafaga = tiempoRafaga;
    }

    public Estado getEstadoProceso()
    {
        return estadoProceso;
    }

    public void setEstadoProceso(Estado estadoProceso)
    {
        this.estadoProceso = estadoProceso;
    }

    public int getNumProceso()
    {
        return numProceso;
    }

    public void setNumProceso(int numProceso)
    {
        this.numProceso = numProceso;
    }

    public long getTiempoRafaga()
    {
        return tiempoRafaga;
    }

    public void setTiempoRafaga(long tiempoRafaga)
    {
        this.tiempoRafaga = tiempoRafaga;
    }

    public long getTiempoEjecutado()
    {
        return tiempoEjecutado;
    }

    public void setTiempoEjecutado(long tiempoEjecutado)
    {
        this.tiempoEjecutado = tiempoEjecutado;
    }

    public void aumentarTiempoEjecutado(long tiempo)
    {
        tiempoEjecutado += tiempo;
    }

    public long tiempoRestanteParaFinalizarProceso()
    {
        return tiempoRafaga - tiempoEjecutado;
    }

    @Override
    public int compareTo(PCB o)
    {
        return (int) (tiempoRafaga - o.getTiempoRafaga());
    }

}
