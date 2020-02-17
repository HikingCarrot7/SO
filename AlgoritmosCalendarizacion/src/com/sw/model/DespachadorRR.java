package com.sw.model;

import java.util.ArrayList;
import java.util.Arrays;
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

                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (cpu != null)
                    {
                        cpu.ejecutarProceso(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }
                }

                System.out.println(Arrays.toString(rr.getTiemposEspera()));
                todosProcesosEntregados = false;
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
            ponerEnEspera(obtenerProcesosEnElMomento(procesos, obtenerMenorTiempoLlegada(procesos)));
            procesos.removeAll(listaEspera);
            System.out.println(listaEspera);
            tiempoTotal += listaEspera.get(0).getTiempoLlegada();

            while (!listaEspera.isEmpty())
            {
                Proceso procesoActual = listaEspera.remove(0);
                ponerEnEjecucion(procesoActual);

                /**
                 * Buscamos a todos los procesos que puedan llegar durante la ejecución del proceso actual para ponerlos a la espera.
                 */
                for (Iterator<Proceso> proceso = procesos.iterator(); proceso.hasNext();)
                {
                    Proceso sig = proceso.next();

                    if (puedeLlegar(procesoActual, sig, tiempoTotal)) // Si el proceso puede llegar durante la ejecución del proceso actual.
                    {
                        ponerEnEspera(sig); // Lo añadimos a la lista de espera.
                        proceso.remove(); // Lo sacamos de la lista de procesos.
                    }

                }

                long tiempoUsoDelCPU = obtenerTiempoUsoCPU(procesoActual); // Tiempo que el proceso actual uso el CPU.
                procesoActual.PCB.aumentarTiempoEjecutado(tiempoUsoDelCPU); // Aumentamos el tiempo de ejecución del proceso actual.
                aumentarTiemposEspera(listaEspera, tiemposEspera, tiempoUsoDelCPU); // Aumentamos el tiempo de espera a todos los procesos que estén en espera.
                revisarEstadoProceso(procesoActual); // Si el proceso actual ya terminó o se va a esperar.

                notificaciones.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), tiempoUsoDelCPU, tiempoTotal)); // Notificamos a la vista del cambio en el diagrama.

                if (procesoActual.esProcesoTerminado())
                {
                    terminarProceso(procesoActual); // Terminamos el proceso.
                    notificaciones.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, -1, tiempoUsoDelCPU + tiempoTotal)); // Notificamos el proceso ya terminó.

                    /**
                     * Obtemos a todos los procesos que hayan llegado justo cuando el proceso actual terminó.
                     */
                    ponerEnEspera(obtenerProcesosEnElMomento(procesos, tiempoTotal + tiempoUsoDelCPU));
                    procesos.removeAll(listaEspera);

                } else
                    ponerEnEspera(procesoActual); // Ponemos en espera al proceso actual.

                /**
                 * Si la lista de espera está vacía pero aún hay más procesos que no han llegado.
                 */
                if (listaEspera.isEmpty() && !procesos.isEmpty())
                {
                    ponerEnEspera(obtenerProcesosEnElMomento(procesos, procesos.get(0).getTiempoLlegada()));
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
         * Aumenta el tiempo de espera a todos los procesos que estén en espera.
         *
         * @param procesos La lista de todos los procesos que queden por ejecutar.
         * @param tiemposEspera Los tiempos de espera de todos los procesos.
         * @param tiempoAumentar El tiempo para aumentar en los procesos.
         */
        private void aumentarTiemposEspera(ArrayList<Proceso> procesos, int[] tiemposEspera, long tiempoAumentar)
        {
            procesos.stream()
                    .filter(p -> p.PCB.getEstadoProceso().equals(Estado.ESPERA))
                    .forEach(p -> tiemposEspera[p.PCB.getNumProceso()] += tiempoAumentar);
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
                    .filter(p -> !p.PCB.getEstadoProceso().equals(Estado.ESPERA) && p.getTiempoLlegada() == momento)
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
                    && cualquierProceso.getTiempoLlegada() >= tiempoUsoDelCPU
                    && cualquierProceso.getTiempoLlegada() <= tiempoUsoDelCPU + obtenerTiempoUsoCPU(procesoActual);
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
            for (int i = 0; i < procesos.size(); i++)
            {
                Proceso proceso = procesos.get(i);
                ponerEnEspera(proceso);
            }
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

        public int[] getTiemposEspera()
        {
            return tiemposEspera;
        }

    }

}
