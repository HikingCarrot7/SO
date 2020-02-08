package com.sw.model;

/**
 *
 * @author HikingC7
 */
public class ProcesoSRTF extends Proceso
{

    public ProcesoSRTF(Estado estadoProceso, String identificador, int numProceso, long tiempoRafaga, long tiempoLlegada)
    {
        super(estadoProceso, identificador, tiempoLlegada, numProceso, tiempoRafaga);
    }

    @Override
    public Proceso obtenerCopiaProceso()
    {
        Proceso copiaProceso = new ProcesoSRTF(
                PCB.getEstadoProceso(),
                identificador,
                PCB.getNumProceso(),
                PCB.getTiempoRafaga(),
                tiempoLlegada);

        copiaProceso.PCB.setTiempoEjecutado(PCB.getTiempoEjecutado());
        return copiaProceso;
    }

}
