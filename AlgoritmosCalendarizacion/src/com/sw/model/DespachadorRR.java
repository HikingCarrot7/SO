package com.sw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;

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
            if (todosProcesosEntregados)
            {
                RR rr = new RR();
                ArrayList<Notificacion> notificaciones = rr.rr(procesos.stream().collect(Collectors.toCollection(ArrayList::new)));

                notificaciones.forEach(System.out::println);

                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (cpu != null)
                    {
                        cpu.ejecutarProceso(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }

                }
                break;
            }

    }

    private class RR
    {

        private int[] tiemposEspera;
        private ArrayList<Proceso> listaEspera;
        private long tiempoTotal;

        public RR()
        {
            listaEspera = new ArrayList<>();
        }

        private ArrayList<Notificacion> rr(ArrayList<Proceso> procesos)
        {
            tiemposEspera = new int[procesos.size()];
            ArrayList<Notificacion> notificaciones = new ArrayList<>();
            listaEspera.addAll(obtenerProcesosEnElMomento(procesos, obtenerMenorTiempoLlegada(procesos)));
            procesos.removeAll(listaEspera);
            tiempoTotal += listaEspera.get(0).getTiempoLlegada();

            while (!listaEspera.isEmpty())
            {
                Proceso procesoActual = listaEspera.remove(0);
                ponerEnEjecucion(procesoActual);

                for (Iterator<Proceso> proceso = procesos.iterator(); proceso.hasNext();)
                {
                    Proceso sig = proceso.next();

                    if (puedeLlegar(procesoActual, sig, tiempoTotal))
                    {
                        listaEspera.add(sig);
                        proceso.remove();
                    }

                }

                long tiempoUsoDelCPU = obtenerTiempoUsoCPU(procesoActual);
                procesoActual.PCB.aumentarTiempoEjecutado(tiempoUsoDelCPU);
                revisarEstadoProceso(procesoActual);

                notificaciones.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), tiempoUsoDelCPU, tiempoTotal));

                if (procesoActual.esProcesoTerminado())
                {
                    terminarProceso(procesoActual);
                    notificaciones.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, -1, tiempoUsoDelCPU + tiempoTotal));
                    listaEspera.addAll(obtenerProcesosEnElMomento(procesos, tiempoTotal));
                    procesos.removeAll(listaEspera);

                } else
                    ponerEnEspera(procesoActual);

                /**
                 * Si la lista de espera está vacía pero aún hay más procesos que no han llegado.
                 */
                if (listaEspera.isEmpty() && !procesos.isEmpty())
                {
                    listaEspera.addAll(obtenerProcesosEnElMomento(procesos, procesos.get(0).getTiempoLlegada()));
                    procesos.removeAll(listaEspera);
                    Proceso procesoSig = listaEspera.get(0);
                    notificaciones.add(new Notificacion(Notificacion.IDLE, procesoSig, procesoSig.getTiempoLlegada() - (tiempoTotal + tiempoUsoDelCPU), tiempoTotal + tiempoUsoDelCPU));
                    tiempoTotal = procesoSig.getTiempoLlegada();

                } else
                    tiempoTotal += tiempoUsoDelCPU;

            }

            return notificaciones;
        }

        /**
         * Retorna o todos los procesos que llegan en el momento especificado.
         *
         * @param procesos Los procesos a filtrar.
         * @param momento El momento específico.
         *
         * @return La lista de procesos que llegan en el momento establecido.
         */
        private ArrayList<Proceso> obtenerProcesosEnElMomento(ArrayList<Proceso> procesos, long momento)
        {
            return procesos.stream()
                    .filter(p -> p.getTiempoLlegada() == momento)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        /**
         * Valida si cualquier proceso es capaz de llegar durante la ejecución del proceso actual.
         *
         * @param procesoActual El proceso actual.
         * @param cualquierProceso Cualquier proceso.
         * @param tiempoUsoDelCPU El tiempo de uso del CPU.
         *
         * @return Si cualquier proceso es capaz de llegar durante la ejecución del proceso actual.
         */
        private boolean puedeLlegar(Proceso procesoActual, Proceso cualquierProceso, long tiempoUsoDelCPU)
        {
            return cualquierProceso.PCB.getEstadoProceso().equals(Estado.LISTO)
                    && cualquierProceso.getTiempoLlegada() > tiempoUsoDelCPU
                    && cualquierProceso.getTiempoLlegada() < tiempoUsoDelCPU + obtenerTiempoUsoCPU(procesoActual);
        }

        /**
         * Regresa el menor tiempo de llegada de todos los procesos.
         *
         * @param procesos La lista de procesos en donde se buscará.
         *
         * @return El menor tiempo de llegada de los procesos.
         */
        private long obtenerMenorTiempoLlegada(ArrayList<Proceso> procesos)
        {
            return procesos.stream()
                    .map(Proceso::getTiempoLlegada)
                    .min(Comparator.comparing(Long::longValue))
                    .get();
        }

        /**
         * Pone en la lista de espera a los procesos especificados.
         *
         * @param procesos Los procesos a poner en la lista de espera.
         */
        private void ponerEnEspera(ArrayList<Proceso> procesos)
        {
            procesos.forEach(this::ponerEnEspera);
        }

        /**
         * Pone en espera al proceso especificado.
         *
         * @param proceso El proceso a poner en espera.
         */
        private void ponerEnEspera(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.ESPERA);
            listaEspera.add(proceso);
        }

        private void terminarProceso(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.TERMINADO);
            listaEspera.remove(proceso);
        }

        private void ponerEnEjecucion(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.EJECUCION);
        }

        private long obtenerTiempoUsoCPU(Proceso proceso)
        {
            return tiempoRestanteProceso(proceso) >= QUANTUM ? QUANTUM : tiempoRestanteProceso(proceso);
        }

        /**
         * Revisa si el proceso ya ha terminado o aún tiene que esperar.
         *
         * @param proceso El proceso para revisar.
         */
        private void revisarEstadoProceso(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(tiempoRestanteProceso(proceso) <= 0 ? Estado.TERMINADO : Estado.ESPERA);
        }

        private long tiempoRestanteProceso(Proceso proceso)
        {
            return proceso.PCB.getTiempoRafaga() - proceso.PCB.getTiempoEjecutado();
        }

    }

}
