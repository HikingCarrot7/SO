package com.sw.model;

/**
 *
 * @author HikingCarrot7
 */
public class ProcesoSJF extends Proceso
{

    public ProcesoSJF(Estado estadoProceso, String identificador, int numProceso, long tiempoRafaga, long tiempoLlegada)
    {
        super(estadoProceso, identificador, tiempoLlegada, numProceso, tiempoRafaga);
    }

    @Override
    public Proceso obtenerCopiaProceso()
    {
        Proceso copiaProceso = new ProcesoSJF(
                PCB.getEstadoProceso(),
                identificador,
                PCB.getNumProceso(),
                PCB.getTiempoRafaga(),
                tiempoLlegada);

        copiaProceso.PCB.setTiempoEjecutado(PCB.getTiempoEjecutado());
        return copiaProceso;
    }

}
