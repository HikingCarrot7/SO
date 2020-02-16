package com.sw.model;

/**
 *
 * @author HikingCarrot7
 */
public class ProcesoRR extends Proceso
{

    public ProcesoRR(Estado estadoProceso, String identificador, int numProceso, long tiempoRafaga, long tiempoLlegada)
    {
        super(estadoProceso, identificador, tiempoLlegada, numProceso, tiempoRafaga);
    }

    @Override
    public Proceso obtenerCopiaProceso()
    {
        Proceso copiaProceso = new ProcesoRR(
                PCB.getEstadoProceso(),
                identificador,
                PCB.getNumProceso(),
                PCB.getTiempoRafaga(),
                tiempoLlegada);

        copiaProceso.setTiempoLlegada(tiempoLlegada);
        copiaProceso.PCB.setTiempoEjecutado(PCB.getTiempoEjecutado());
        return copiaProceso;
    }

}
