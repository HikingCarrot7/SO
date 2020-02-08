package com.sw.model;

/**
 *
 * @author HikingCarrot7
 */
public class DespachadorRR extends Despachador
{

    private final long QUANTUM;

    public DespachadorRR(final CPU CPU, final long QUANTUM)
    {
        super(CPU);
        this.QUANTUM = QUANTUM;
    }

    @Override
    public void run()
    {
        while (running)
            if (hayProcesosEsperando())
            {
                Proceso proceso = procesos.remove();
                long tiempoUsoCPU = obtenerTiempoUsoCPU(proceso);
                cambiarContexto(proceso, tiempoUsoCPU);

                esperar(); // Esperamos al proceso.

                if (!running)
                    break;

                revisarEstadoProceso(proceso);

                if (proceso.esProcesoTerminado())
                    notificar(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO,
                            proceso,
                            0, // Tiempo de uso del cpu en su última ejecución. (El proceso ha finalizado, ya no necesita usar el cpu)
                            tiempoEsperaProceso(proceso), // Tiempo que esperó el cpu para su última ejecución.
                            tiempoTotalUsoCPU + tiempoUsoCPU)); // Tiempo en el que el proceso ha terminado de ejecutarse.
                else
                {
                    notificar(new Notificacion(Notificacion.PROCESO_DEJO_CPU, proceso,
                            0, // Tiempo de uso del cpu en su última ejecución. (El proceso ha dejado el cpu)
                            tiempoEsperaProceso(proceso)));// Tiempo que esperó el proceso para su última ejecución.

                    procesos.addLast(proceso);//El proceso no ha terminado, regresa a la cola.
                }

                tiempoTotalUsoCPU += tiempoUsoCPU; // Tiempo total que se ha usado el cpu.
            }

    }

    private long obtenerTiempoUsoCPU(Proceso proceso)
    {
        return tiempoRestanteProceso(proceso) >= QUANTUM ? QUANTUM : tiempoRestanteProceso(proceso);
    }

    private void revisarEstadoProceso(Proceso proceso)
    {
        proceso.PCB.setEstadoProceso(tiempoRestanteProceso(proceso) <= 0 ? Estado.TERMINADO : Estado.ESPERA);
    }

    private long tiempoRestanteProceso(Proceso proceso)
    {
        return proceso.PCB.getTiempoRafaga() - proceso.PCB.getTiempoEjecutado();
    }

}
