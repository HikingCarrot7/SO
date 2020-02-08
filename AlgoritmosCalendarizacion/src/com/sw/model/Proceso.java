package com.sw.model;

/**
 *
 * @author HikingC7
 */
public abstract class Proceso
{

    protected String identificador;
    protected long tiempoLlegada;
    public final PCB PCB;

    public Proceso(Estado estadoProceso, String identificador, long tiempoLlegada, int numProceso, long tiempoRafaga)
    {
        this.identificador = identificador;
        this.tiempoLlegada = tiempoLlegada;
        PCB = new PCB(estadoProceso, numProceso, tiempoRafaga);
    }

    public String getIdentificador()
    {
        return identificador;
    }

    public void setIdentificador(String identificador)
    {
        this.identificador = identificador;
    }

    public long getTiempoLlegada()
    {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(long tiempoLlegada)
    {
        this.tiempoLlegada = tiempoLlegada;
    }

    public boolean esProcesoTerminado()
    {
        return PCB.getEstadoProceso().equals(Estado.TERMINADO);
    }

    public abstract Proceso obtenerCopiaProceso();

    @Override
    public String toString()
    {
        return identificador;
    }

}
