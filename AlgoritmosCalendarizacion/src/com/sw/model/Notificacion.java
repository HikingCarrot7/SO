package com.sw.model;

/**
 *
 * @author HikingCarrot7
 */
public class Notificacion
{

    public static final int CAMBIO_CONTEXTO = 0;
    public static final int PROCESO_DEJO_CPU = 1;
    public static final int PROCESO_HA_FINALIZADO = 2;
    public static final int NO_QUEDAN_PROCESOS = 3;
    public static final int INTERRUPCION = 4;
    public static final int IDLE = 5;

    private int identificador;
    private Proceso proceso;
    private long tiempoEnQueFinalizoProceso;
    private long momento;
    private long tiempoUsoCPU;

    public Notificacion(int identificador)
    {
        this.identificador = identificador;
    }

    public Notificacion(int identificador, Proceso proceso, long tiempoUsoCPU, long momento)
    {
        this.identificador = identificador;
        this.proceso = proceso;
        this.momento = momento;
        this.tiempoUsoCPU = tiempoUsoCPU;
    }

    public Notificacion(int identificador, Proceso proceso, long tiempoUsoCPU, long momento, long tiempoEnQueFinalizoProceso)
    {
        this(identificador, proceso, tiempoUsoCPU, momento);
        this.tiempoEnQueFinalizoProceso = tiempoEnQueFinalizoProceso;
    }

    public Notificacion(int identificador, Proceso proceso, long tiempoUsoCPU)
    {
        this(identificador, proceso, tiempoUsoCPU, 0);
    }

    public int getIdentificador()
    {
        return identificador;
    }

    public void setIdentificador(int identificador)
    {
        this.identificador = identificador;
    }

    public Proceso getProceso()
    {
        return proceso == null ? null : proceso.obtenerCopiaProceso();
    }

    public void setProceso(Proceso proceso)
    {
        this.proceso = proceso;
    }

    public long getTiempoEnQueFinalizoProceso()
    {
        return tiempoEnQueFinalizoProceso;
    }

    public void setTiempoEnQueFinalizoProceso(long tiempoEnQueFinalizoProceso)
    {
        this.tiempoEnQueFinalizoProceso = tiempoEnQueFinalizoProceso;
    }

    public long getMomento()
    {
        return momento;
    }

    public void setMomento(long momento)
    {
        this.momento = momento;
    }

    public long getTiempoUsoCPU()
    {
        return tiempoUsoCPU;
    }

    public void setTiempoUsoCPU(long tiempoUsoCPU)
    {
        this.tiempoUsoCPU = tiempoUsoCPU;
    }

    @Override
    public String toString()
    {
        return "Notificacion{" + "identificador=" + identificador + ", proceso=" + proceso.getIdentificador() + ", tiempoUsoCPU=" + tiempoUsoCPU + ", tiempoEspera=" + momento + '}';
    }

}
