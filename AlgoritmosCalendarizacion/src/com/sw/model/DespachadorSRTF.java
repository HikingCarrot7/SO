package com.sw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author HikingC7
 */
public class DespachadorSRTF extends Despachador
{

    private volatile boolean todosProcesosEntregados;

    public DespachadorSRTF(final CPU CPU)
    {
        super(CPU);
    }

    @Override
    public void run()
    {
        while (running)
            if (todosProcesosEntregados)
            {
                /*ArrayList<Notificacion> notificaciones = obtenerBloquesASimular(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada).thenComparing(p -> p.PCB.getNumProceso()))
                        .collect(Collectors.toCollection(ArrayList::new)));

                // notificaciones.stream().filter(n -> n.getProceso() != null).forEach(System.out::println);
                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (cpu != null && notif.getIdentificador() != Notificacion.INTERRUPCION)
                    {
                        cpu.ejecutarProceso(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }
                }*/

                SRTF bloques = new SRTF(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada))
                        .collect(Collectors.toCollection(ArrayList::new)));

                bloques.srtf().entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).forEach(System.out::println);

                for (Proceso proceso : procesos)
                    System.out.println("El proceso " + proceso.getIdentificador() + " espero " + bloques.getTiemposEspera()[proceso.PCB.getNumProceso()]);

                break;
            }

    }

    private ArrayList<Notificacion> obtenerBloques(ArrayList<Proceso> procesos)
    {
        ArrayList<Proceso> p = procesos;
        ArrayList<Proceso> listaEspera = new ArrayList<>();
        HashMap<Integer, ArrayList<Proceso>> entradas = new HashMap<>();
        Proceso procesoActual;
        int[] tiemposEspera = new int[procesos.size()];
        int nProcesos = 0;
        long tiempoTotal = 0;

        for (Proceso proceso : p)
        {
            tiempoTotal += proceso.PCB.getTiempoRafaga();
            nProcesos++;

        }

        return null;
    }

    private class SRTF
    {

        private ArrayList<Proceso> procesos;
        private ArrayList<Proceso> listaEspera;
        private HashMap<Long, ArrayList<Proceso>> mapOrderedByEntry;
        private Proceso procesoActual;
        private int[] tiemposEspera;
        private int nProcesos;
        private long tiempoTotal;

        public SRTF(ArrayList<Proceso> procesos)
        {
            this.procesos = procesos;
            listaEspera = new ArrayList<>();
            mapOrderedByEntry = new HashMap<>();
            tiemposEspera = new int[procesos.size()];
            initMapOrderedByEntry();
        }

        private void initMapOrderedByEntry()
        {
            for (Proceso proceso : procesos)
            {
                tiempoTotal += proceso.PCB.getTiempoRafaga();
                nProcesos++;

                if (mapOrderedByEntry.containsKey(proceso.getTiempoLlegada()))
                {
                    if (!mapOrderedByEntry.get(proceso.getTiempoLlegada()).contains(proceso))
                        mapOrderedByEntry.get(proceso.getTiempoLlegada()).add(proceso);

                } else
                {
                    mapOrderedByEntry.put(proceso.getTiempoLlegada(), new ArrayList<>());
                    mapOrderedByEntry.get(proceso.getTiempoLlegada()).add(proceso);
                }

            }

        }

        private HashMap<Long, HashMap<String, String>> srtf()
        {
            HashMap<Long, HashMap<String, String>> gantt = new HashMap<>();

            for (long i = 0; i < tiempoTotal; i++)
            {

                if (mapOrderedByEntry.containsKey(i))
                {
                    listaEspera.addAll(mapOrderedByEntry.get(i));
                    Proceso shortest = listaEspera.get(0);

                    for (int j = 1; j < listaEspera.size(); j++)
                        if (tiempoRestanteProceso(shortest, i) > tiempoRestanteProceso(listaEspera.get(j), i))
                            shortest = listaEspera.get(j);

                    if (i == 0)
                    {
                        eliminarListaEspera(shortest);
                        procesoActual = shortest;
                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual.getIdentificador());
                        gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(procesoActual, i)));

                    } else if (tiempoRestanteProceso(procesoActual, i) == 0)
                    {
                        eliminarListaEspera(shortest);
                        procesoActual = shortest;
                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual.getIdentificador());
                        gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(procesoActual, i)));

                    } else if (tiempoRestanteProceso(shortest, i) < tiempoRestanteProceso(procesoActual, i))
                    {
                        eliminarListaEspera(shortest);
                        listaEspera.add(procesoActual);
                        procesoActual = shortest;
                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual.getIdentificador());
                        gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(procesoActual, i)));

                    } else
                    {
                        if (!listaEspera.contains(shortest))
                            listaEspera.add(shortest);

                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual.getIdentificador());
                        gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(procesoActual, i)));
                    }

                } else if (tiempoRestanteProceso(procesoActual, i) != 0)
                {
                    /*gantt.put(i, new HashMap<>());
                    gantt.get(i).put("Proceso", procesoActual.getIdentificador());
                    gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(procesoActual, i)));*/

                } else
                {
                    Proceso shortest = listaEspera.get(0);

                    for (int j = 1; j < listaEspera.size(); j++)
                    {
                        Proceso proceso = listaEspera.get(j);

                        if (tiempoRestanteProceso(shortest, i) > tiempoRestanteProceso(proceso, i))
                            shortest = proceso;

                        else if (tiempoRestanteProceso(shortest, i) == tiempoRestanteProceso(proceso, i))
                            if (shortest.getTiempoLlegada() > proceso.getTiempoLlegada())
                                shortest = proceso;

                    }

                    gantt.put(i, new HashMap<>());
                    gantt.get(i).put("Proceso", shortest.getIdentificador());
                    gantt.get(i).put("Rafaga restante", String.valueOf(tiempoRestanteProceso(shortest, i)));

                    eliminarListaEspera(shortest);
                    procesoActual = shortest;
                }

                for (Proceso proceso : listaEspera)
                    tiemposEspera[proceso.PCB.getNumProceso()]++;
            }

            return gantt;
        }

        private void eliminarListaEspera(Proceso proceso)
        {
            listaEspera.remove(proceso);
        }

        private long tiempoRestanteProceso(Proceso proceso, long tiempoUsoDelCPU)
        {
            return proceso.PCB.getTiempoRafaga() - (tiempoUsoDelCPU - proceso.getTiempoLlegada() - tiempoEsperaProceso(proceso));
        }

        private long tiempoEsperaProceso(Proceso proceso)
        {
            return tiemposEspera[proceso.PCB.getNumProceso()];
        }

        public int[] getTiemposEspera()
        {
            return tiemposEspera;
        }

    }

    private ArrayList<Notificacion> obtenerBloquesASimular(ArrayList<Proceso> procesos)
    {
        ArrayList<Notificacion> bloques = new ArrayList<>(); // Los bloques de información que en un futuro serán enviados a la vista para mostrarse en el diagrama Gantt.
        int[] tiemposEspera = new int[procesos.size()]; // Los tiempos de espera para cada proceso.
        Proceso procesoActual = obtenerProcesoMenorTiempoLlegada(procesos); // Obtenemos al proceso actual.
        long tiempoTotalUsoDelCPU = procesoActual.getTiempoLlegada(); // El tiempo del uso del CPU se inicializa con el tiempo ráfaga del primer proceso.
        boolean interrumpido = false;

        procesoActual.PCB.setEstadoProceso(Estado.EJECUCION);

        while (!procesos.isEmpty()) // Mientras existan procesos por despachar en la lista.
        {
            for (int i = 0; i < procesos.size() && !interrumpido; i++)
            {
                Proceso procesoSiguienteEnCola = procesos.get(i);

                if (interrumpe(tiempoTotalUsoDelCPU, procesoSiguienteEnCola, procesoActual))
                {
                    long tiempoEjecutado = procesoSiguienteEnCola.getTiempoLlegada() - tiempoTotalUsoDelCPU;
                    ponerEnEspera(procesoActual);
                    procesoActual.PCB.aumentarTiempoEjecutado(tiempoEjecutado);

                    bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), tiempoEjecutado, tiemposEspera[procesoActual.PCB.getNumProceso()]));
                    bloques.add(new Notificacion(Notificacion.INTERRUPCION));

                    Proceso siguienteProceso = siguienteProceso(procesos, procesoSiguienteEnCola);

                    procesos.add(procesoActual);
                    procesoActual = siguienteProceso;
                    procesos.remove(siguienteProceso);
                    tiempoTotalUsoDelCPU += tiempoEjecutado;
                    interrumpido = true;

                } else if (llegaDuranteEjecucion(tiempoTotalUsoDelCPU, procesoActual, procesoSiguienteEnCola))
                    ponerEnEspera(procesoSiguienteEnCola);
            }

            if (!interrumpido)
            {
                procesoActual.PCB.setEstadoProceso(Estado.TERMINADO); // El proceso termina.
                aumentarTiemposEspera(procesos, tiemposEspera, procesoActual.PCB.tiempoRestanteParaFinalizarProceso()); // Se aumentan los tiempos de espera.

                //Se crean los bloques de información a notificar en la vista.
                bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), procesoActual.PCB.tiempoRestanteParaFinalizarProceso(), tiemposEspera[procesoActual.PCB.getNumProceso()]));
                bloques.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, tiemposEspera[procesoActual.PCB.getNumProceso()], tiempoTotalUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso()));

                tiempoTotalUsoDelCPU += procesoActual.PCB.tiempoRestanteParaFinalizarProceso(); // Aumentamos el tiempo del CPU.
                procesos.remove(procesoActual); // Sacamos al proceso que acaba de terminar de la lista.

                analizarProcesosEntrantesAlFinalizarUnProceso(procesos, tiempoTotalUsoDelCPU); // Se ponen en espera a todos los procesos que llegan justo cuando acaba de terminar el proceso actual.
                Proceso procesoSig = procesoSiguienteAlFinalizarElActual(procesos);
                procesoActual = procesoSig;
                procesos.remove(procesoSig);
            }

            interrumpido = false;
        }

        procesoActual.PCB.setEstadoProceso(Estado.TERMINADO); // El último proceso termina.
        aumentarTiemposEspera(procesos, tiemposEspera, procesoActual.PCB.tiempoRestanteParaFinalizarProceso()); // Aumentamos los tiempos de espera.

        // Se crean los bloques a notificar a la vista.
        bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), procesoActual.PCB.tiempoRestanteParaFinalizarProceso(), tiemposEspera[procesoActual.PCB.getNumProceso()]));
        bloques.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, tiemposEspera[procesoActual.PCB.getNumProceso()], tiempoTotalUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso()));

        return bloques; // Regresamos los bloques.
    }

    /**
     * Regresa el proceso con el menor tiempo de llegada en la lista de procesos. Este método elimina al proceso de la lista.
     *
     * Si hay dos o más procesos que tengan el mismo tiempo de llegada, regresará el que tiene el menor tiempo ráfaga.
     *
     * y los demás se pondrán en espera.
     */
    private Proceso obtenerProcesoMenorTiempoLlegada(ArrayList<Proceso> procesos) throws NullPointerException
    {
        ArrayList<Proceso> procesosMenorTiempoLlegada;
        //Buscamos el proceso con el menor tiempo llegada.
        Optional<Proceso> procesoMenorTiempoLlegada = procesos.stream().min(Comparator.comparing(Proceso::getTiempoLlegada));

        if (procesoMenorTiempoLlegada.isPresent())
        {
            procesosMenorTiempoLlegada = procesos.stream()
                    .filter(p -> p != procesoMenorTiempoLlegada.get() && p.getTiempoLlegada() == procesoMenorTiempoLlegada.get().getTiempoLlegada())
                    .collect(Collectors.toCollection(ArrayList::new)); // Procedemos a buscar más ocurrencias (si hay más procesos con el mismo tiempo de llegada)

            ponerEnEspera(procesosMenorTiempoLlegada); // Ponemos a los demás procesos que hayan llegado al mismo tiempo en espera.

            //Obtiene el proceso que tiene el menor tiempo ráfaga.
            Optional<Proceso> procesoMenorTiempoFinalizar = obtenerProcesoMenorTiempoParaFinalizar(procesosMenorTiempoLlegada);

            if (procesoMenorTiempoFinalizar.isPresent())
            {
                procesos.remove(procesoMenorTiempoFinalizar.get());
                return procesoMenorTiempoFinalizar.get(); // Regresa al que tiene el menor tiempo ráfaga.

            } else
            {
                procesos.remove(procesoMenorTiempoLlegada.get());
                return procesoMenorTiempoLlegada.get(); // Regresa al que tiene el menor tiempo de llegada.
            }

        } else
            throw new NullPointerException("No hay procesos esperando.");
    }

    /**
     * Valida si el proceso que acaba de llegar es capaz de interrumpir al proceso que está actualmente en ejecución.
     *
     * @param procesoLlegada El proceso que acaba de llegar.
     * @param procesoActual El proceso que está actualmente en ejecución.
     *
     * @return Si el proceso proceso de llegada puede interrumpir al proceso actual.
     */
    private boolean interrumpe(long tiempoUsoDelCPU, Proceso procesoLlegada, Proceso procesoActual)
    {
        return procesoLlegada.PCB.getEstadoProceso().equals(Estado.LISTO)
                && procesoLlegada.PCB.tiempoRestanteParaFinalizarProceso() < (tiempoUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso() - procesoLlegada.getTiempoLlegada());
    }

    /**
     * Valida si un proceso cualquiera es capaz de llegar durante la ejecución del proceso actual.
     *
     * @param tiempoUsoDelCPU El tiempo del uso del CPU.
     * @param procesoActual El proceso actual que se está ejecutando en el CPU.
     * @param cualquierProceso Un proceso cualquiera.
     *
     * @return Si un proceso cualquiera es capaz de llegar durante la ejecución del programa actual.
     */
    private boolean llegaDuranteEjecucion(long tiempoUsoDelCPU, Proceso procesoActual, Proceso cualquierProceso)
    {
        return cualquierProceso.PCB.getEstadoProceso().equals(Estado.LISTO)
                && cualquierProceso.getTiempoLlegada() < tiempoUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso()
                && cualquierProceso.getTiempoLlegada() > tiempoUsoDelCPU;
    }

    /**
     * Regresa al proceso con el menor tiempo ráfaga de la lista.
     *
     * @param procesos La lista a buscar el proceso con el menor tiempo ráfaga.
     *
     * @return El proceso con el menor tiempo ráfaga. Si la lista está vacía, se retornará null.
     */
    private Optional<Proceso> obtenerProcesoMenorTiempoParaFinalizar(ArrayList<Proceso> procesos)
    {
        try
        {
            return procesos.stream().min(Comparator.comparing(p -> p.PCB.tiempoRestanteParaFinalizarProceso()));

        } catch (NullPointerException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene a los procesos que tienen un tiempo de llegada especificado.
     *
     * @param tiempoLlegada El tiempo de llegada de los procesos que se regresarán en una lista.
     * @param procesos Los procesos a filtrar.
     *
     * @return La lista de procesos que cumplen con el tiempo de llegada especificado.
     */
    private ArrayList<Proceso> obtenerProcesosQueLleganEn(long tiempoLlegada, ArrayList<Proceso> procesos)
    {
        return procesos.stream().filter(p -> p.getTiempoLlegada() == tiempoLlegada).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Cuando ocurre una interrupción, se busca si hay más procesos que llegaron al mismo tiempo que el proceso que causó la interrupción y se ponen a la espera.
     *
     * En caso de que no hayan otros, se busca al proceso que tiene el menor tiempo ráfaga de entre los que están en espera.
     *
     * @param procesos Los procesos en donde se buscará la siguiente proceso a ser ejecutado por el CPU.
     * @param procesoQueInterrumpio El proceso que ha interrumpido al proceso que se estaba ejecutando en el CPU.
     *
     * @return El proceso que sigue después de la interrupción.
     */
    private Proceso siguienteProceso(ArrayList<Proceso> procesos, Proceso procesoQueInterrumpio)
    {
        Optional<Proceso> proceso = obtenerProcesoMenorTiempoParaFinalizar(obtenerProcesosQueLleganEn(procesoQueInterrumpio.getTiempoLlegada(), procesos));
        ponerEnEspera(proceso.get());
        Proceso p = obtenerProcesoEnEsperaConMenorTiempoFinalizar(procesos).get();
        p.PCB.setEstadoProceso(Estado.EJECUCION);
        return p;
    }

    /**
     * Regresa al proceso que está en espera con el menor tiempo ráfaga. En caso de que hayan dos o más procesos con el mismo tiempo ráfaga, se regresará a la primera ocurrencia.
     *
     * @param procesos La lista de procesos a filtrar.
     *
     * @return El proceso en espera con el menor tiempo ráfaga.
     */
    private Optional<Proceso> obtenerProcesoEnEsperaConMenorTiempoFinalizar(ArrayList<Proceso> procesos)
    {
        return procesos.stream()
                .filter(p -> p.PCB.getEstadoProceso().equals(Estado.ESPERA))
                .min(Comparator.comparing(p -> p.PCB.tiempoRestanteParaFinalizarProceso()));
    }

    /**
     * Regresa el proceso siguiente después de la terminación del proceso actual.
     *
     * @param procesos La lista de procesos restantes a ser despachados.
     * @return El proceso siguiente a ser despachado.
     */
    private Proceso procesoSiguienteAlFinalizarElActual(ArrayList<Proceso> procesos)
    {
        Optional<Proceso> procesoSiguiente = obtenerProcesoEnEsperaConMenorTiempoFinalizar(procesos);

        if (procesoSiguiente.isPresent())
            return procesoSiguiente.get();

        else
            return obtenerProcesoMenorTiempoLlegada(procesos);
    }

    /**
     * Aumenta los tiempos de espera de los procesos.
     *
     * @param procesos Los procesos que aún no se han despachado.
     * @param tiemposEspera La lista de los tiempos de espera de cada proceso.
     * @param tiempoEspera El tiempo de espera que se añadirá a cada proceso.
     *
     */
    private void aumentarTiemposEspera(ArrayList<Proceso> procesos, int[] tiemposEspera, long tiempoEspera)
    {
        for (Proceso proceso : procesos)
            if (proceso.PCB.getEstadoProceso().equals(Estado.ESPERA))
                tiemposEspera[proceso.PCB.getNumProceso()] += tiempoEspera;
    }

    /**
     * Pone en espera a todos los procesos (si es que hay) que lleguen justo cuando un proceso ha terminado.
     *
     * @param procesos La lista de procesos que faltan por despachar.
     * @param tiempoTotalUsoDelCPU El tiempo total de uso del CPU.
     */
    private void analizarProcesosEntrantesAlFinalizarUnProceso(ArrayList<Proceso> procesos, long tiempoTotalUsoDelCPU)
    {
        procesos.stream().filter(p -> p.getTiempoLlegada() == tiempoTotalUsoDelCPU).forEach(this::ponerEnEspera);
    }

    /**
     * Pone todos los procesos de la lista en espera.
     *
     * @param procesos Los procesos a poner en espera.
     */
    private void ponerEnEspera(ArrayList<Proceso> procesos)
    {
        procesos.forEach(this::ponerEnEspera);
    }

    /**
     * Pone el proceso en espera.
     *
     * @param proceso El proceso a poner en espera.
     */
    private void ponerEnEspera(Proceso proceso)
    {
        proceso.PCB.setEstadoProceso(Estado.ESPERA);
    }

    /**
     * Establece que todos los procesos han sido entregados.
     */
    public void todosProcesosEntregados()
    {
        todosProcesosEntregados = true;
    }

}
