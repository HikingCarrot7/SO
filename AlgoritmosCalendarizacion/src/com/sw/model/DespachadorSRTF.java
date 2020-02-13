package com.sw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
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
                ArrayList<Notificacion> notificaciones = obtenerBloquesASimular(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada).thenComparing(p -> p.PCB.getNumProceso()))
                        .collect(Collectors.toCollection(ArrayList::new)));

                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (notif.getIdentificador() != Notificacion.INTERRUPCION)
                    {
                        cpu.ejecutarProceso(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }
                }

                break;
            }

    }

    private ArrayList<Notificacion> obtenerBloques(ArrayList<Proceso> procesos)
    {
        ArrayList<Notificacion> bloques = new ArrayList<>(); // Los bloques de información que en un futuro serán enviados a la vista para mostrarse en el diagrama Gantt.
        int[] tiemposEspera = new int[procesos.size()]; // Los tiempos de espera para cada proceso.
        Proceso procesoActual = obtenerProcesoMenorTiempoLlegada(procesos); // Obtenemos al proceso actual.
        long tiempoTotalUsoDelCPU = procesoActual.getTiempoLlegada(); // El tiempo del uso del CPU se inicializa con el tiempo ráfaga del primer proceso.
        boolean interrumpido = false;

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

                    Proceso siguienteProceso = siguienteProceso(procesos, procesoSiguienteEnCola); // Revisar

                    procesos.add(procesoActual);
                    procesoActual = siguienteProceso;
                    procesos.remove(siguienteProceso);
                    tiempoTotalUsoDelCPU += tiempoEjecutado;
                    interrumpido = true;
                }

            }

            System.out.println("");
        }

        return bloques;
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
            Optional<Proceso> procesoMenorTiempoRafaga = obtenerProcesoMenorTiempoRafaga(procesosMenorTiempoLlegada);

            if (procesoMenorTiempoRafaga != null)
            {
                procesos.remove(procesoMenorTiempoRafaga.get());
                return procesoMenorTiempoRafaga.get(); // Regresa al que tiene el menor tiempo ráfaga.

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
    private Optional<Proceso> obtenerProcesoMenorTiempoRafaga(ArrayList<Proceso> procesos)
    {
        try
        {
            return procesos.stream().min(Comparator.comparing(p -> p.PCB.getTiempoRafaga()));

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
     * Cuando ocurre una interrupción, se busca si hay más procesos que llegaron al mismo tiempo que el proceso que causó la interrupción.
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
        Optional<Proceso> proceso = obtenerProcesoMenorTiempoRafaga(obtenerProcesosQueLleganEn(procesoQueInterrumpio.getTiempoLlegada(), procesos));
        ponerEnEspera(proceso.get());
        Proceso p = obtenerProcesoEnEsperaConMenorTiempoRafaga(procesos);
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
    private Proceso obtenerProcesoEnEsperaConMenorTiempoRafaga(ArrayList<Proceso> procesos)
    {
        return procesos.stream()
                .filter(p -> p.PCB.getEstadoProceso().equals(Estado.ESPERA))
                .min(Comparator.comparing(p -> p.PCB.getTiempoRafaga()))
                .get();
    }

    /**
     * Pone todos los procesos de la lista en espera.
     *
     * @param procesos Los procesos a poner en espera.
     */
    private void ponerEnEspera(ArrayList<Proceso> procesos)
    {
        for (Proceso proceso : procesos)
            ponerEnEspera(proceso);
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

    private ArrayList<Notificacion> obtenerBloquesASimular(ArrayList<Proceso> procesos)
    {
        ArrayList<Notificacion> bloques = new ArrayList<>();
        int[] tiemposEspera = new int[procesos.size()];
        long tiempoTotalUsoDelCPU = procesos.get(0).getTiempoLlegada();
        Proceso procesoActual = procesos.remove(0);
        boolean interrumpido = false;

        while (!procesos.isEmpty())
        {
            for (int i = 0; i < procesos.size(); i++)
            {
                Proceso procesoSiguienteEnCola = procesos.get(i);

                if (interrumpe(tiempoTotalUsoDelCPU, procesoSiguienteEnCola, procesoActual))
                {
                    long tiempoEjecutado = procesoSiguienteEnCola.getTiempoLlegada() - tiempoTotalUsoDelCPU;
                    procesoActual.PCB.setEstadoProceso(Estado.ESPERA);
                    procesoActual.PCB.aumentarTiempoEjecutado(tiempoEjecutado);

                    bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), tiempoEjecutado, tiemposEspera[procesoActual.PCB.getNumProceso()]));
                    bloques.add(new Notificacion(Notificacion.INTERRUPCION));

                    Proceso siguienteProceso = siguienteProceso(procesos, procesoSiguienteEnCola);

                    procesos.add(procesoActual);
                    procesoActual = siguienteProceso;
                    procesos.remove(siguienteProceso);
                    tiempoTotalUsoDelCPU += tiempoEjecutado;
                    interrumpido = true;
                    break;

                } else if (lleganAlMismoTiempo(procesoSiguienteEnCola, procesoActual)
                        || llegaDuranteEjecucionProcesoActual(tiempoTotalUsoDelCPU, procesoSiguienteEnCola, procesoActual))
                    procesoSiguienteEnCola.PCB.setEstadoProceso(Estado.ESPERA);

            }

            if (!interrumpido)
            {
                procesoActual.PCB.setEstadoProceso(Estado.TERMINADO);
                aumentarTiemposEspera(procesos, tiemposEspera, procesoActual.PCB.tiempoRestanteParaFinalizarProceso());
                bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), procesoActual.PCB.tiempoRestanteParaFinalizarProceso(), tiemposEspera[procesoActual.PCB.getNumProceso()]));
                bloques.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, tiemposEspera[procesoActual.PCB.getNumProceso()], tiempoTotalUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso()));
                tiempoTotalUsoDelCPU += procesoActual.PCB.tiempoRestanteParaFinalizarProceso();
                procesos.remove(procesoActual);

                analizarProcesosEntrantesAlFinalizarUnProceso(procesos, tiempoTotalUsoDelCPU);
                Proceso procesoSig = procesoConMenorTiempoParaFinalizar(procesos);
                procesoActual = procesoSig;
                procesos.remove(procesoSig);
            }

            procesos = ordenarProcesosPorTiempoLlegada(procesos);
            interrumpido = false;
        }

        procesoActual.PCB.setEstadoProceso(Estado.TERMINADO);
        bloques.add(new Notificacion(Notificacion.CAMBIO_CONTEXTO, procesoActual.obtenerCopiaProceso(), procesoActual.PCB.tiempoRestanteParaFinalizarProceso(), tiemposEspera[procesoActual.PCB.getNumProceso()]));
        bloques.add(new Notificacion(Notificacion.PROCESO_HA_FINALIZADO, procesoActual.obtenerCopiaProceso(), 0, tiemposEspera[procesoActual.PCB.getNumProceso()], tiempoTotalUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso()));
        tiempoTotalUsoDelCPU += procesoActual.PCB.tiempoRestanteParaFinalizarProceso();

        return bloques;
    }

    private void aumentarTiemposEspera(ArrayList<Proceso> procesos, int[] tiemposEspera, long tiempoEspera)
    {
        for (Proceso proceso : procesos)
            if (proceso.PCB.getEstadoProceso().equals(Estado.ESPERA))
                tiemposEspera[proceso.PCB.getNumProceso()] += tiempoEspera;
    }

    private ArrayList<Proceso> ordenarProcesosPorTiempoLlegada(ArrayList<Proceso> procesos)
    {
        return procesos.stream()
                .sorted(Comparator.comparing(Proceso::getTiempoLlegada))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void analizarProcesosEntrantesAlFinalizarUnProceso(ArrayList<Proceso> procesos, long tiempoTotalUsoDelCPU)
    {
        procesos.forEach((p) ->
        {
            if (p.getTiempoLlegada() == tiempoTotalUsoDelCPU)
                p.PCB.setEstadoProceso(Estado.ESPERA);
        });
    }

    private Proceso procesoConMenorTiempoParaFinalizar(ArrayList<Proceso> procesos)
    {
        return procesos.stream()
                .filter(p -> p.PCB.getEstadoProceso().equals(Estado.ESPERA))
                .min(Comparator.comparing(p -> p.PCB.tiempoRestanteParaFinalizarProceso()))
                .get();
    }

    /**
     *
     * @param procesoLlegada
     * @param procesoActual
     *
     * @return Si el proceso proceso de llegada puede interrumpir al proceso actual.
     */
    /* private boolean interrumpe(long tiempoUsoDelCPU, Proceso procesoLlegada, Proceso procesoActual)
    {
        return procesoLlegada.PCB.getEstadoProceso().equals(Estado.LISTO)
                && procesoLlegada.PCB.tiempoRestanteParaFinalizarProceso() < (tiempoUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso() - procesoLlegada.getTiempoLlegada());

    }*/
    private Proceso siguienteProceso(ArrayList<Proceso> procesos, Proceso procesoSiguienteEnCola)
    {

        try
        {
            Proceso procesoEnEsperaMinTiempoRestante = procesos.stream()
                    .filter(p -> p.PCB.getEstadoProceso().equals(Estado.ESPERA))
                    .min(Comparator.comparing(p -> p.PCB.tiempoRestanteParaFinalizarProceso()))
                    .get();

            return procesoSiguienteEnCola.PCB.tiempoRestanteParaFinalizarProceso() <= procesoEnEsperaMinTiempoRestante.PCB.tiempoRestanteParaFinalizarProceso()
                    ? procesoSiguienteEnCola : procesoEnEsperaMinTiempoRestante;

        } catch (NoSuchElementException ex)
        {
            return procesoSiguienteEnCola;
        }

    }

    private boolean lleganAlMismoTiempo(Proceso procesoLlegada, Proceso procesoActual)
    {
        return procesoLlegada.getTiempoLlegada() == procesoActual.getTiempoLlegada();
    }

    private boolean llegaDuranteEjecucionProcesoActual(long tiempoUsoDelCPU, Proceso procesoLlegada, Proceso procesoActual)
    {
        return procesoLlegada.PCB.getEstadoProceso().equals(Estado.LISTO)
                && procesoLlegada.getTiempoLlegada() > tiempoUsoDelCPU
                && procesoLlegada.getTiempoLlegada() < tiempoUsoDelCPU + procesoActual.PCB.tiempoRestanteParaFinalizarProceso();
    }

    public void todosProcesosEntregados()
    {
        todosProcesosEntregados = true;
    }

}
