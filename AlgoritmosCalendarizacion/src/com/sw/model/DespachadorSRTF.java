package com.sw.model;

import java.util.ArrayList;
import java.util.Collection;
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
                arreglarProcesos(procesos.stream().collect(Collectors.toCollection(ArrayList::new)));

                ArrayList<Notificacion> notificaciones = obtenerNotificaciones(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada).thenComparing(p -> p.PCB.getNumProceso()))
                        .collect(Collectors.toCollection(ArrayList::new)));

                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (cpu != null && notif.getIdentificador() != Notificacion.INTERRUPCION)
                    {
                        cambiarContexto(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }
                }

                todosProcesosEntregados = false;
                break;
            }

    }

    private ArrayList<Notificacion> obtenerNotificaciones(ArrayList<Proceso> procesos)
    {
        ArrayList<Notificacion> notificaciones = new ArrayList<>();
        SRTF srtf = new SRTF(procesos);

        HashMap<Long, HashMap<String, Object>> solucion = (HashMap<Long, HashMap<String, Object>>) srtf.srtf()
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Entry::getKey))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        ArrayList<Long> keys = solucion
                .keySet()
                .stream()
                .sorted(Comparator.comparing(Long::longValue))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < keys.size() - 1; i++)
        {
            HashMap<String, Object> actual = solucion.get(keys.get(i));
            HashMap<String, Object> sig = solucion.get(keys.get(i + 1));

            Proceso pActual = (Proceso) actual.get("Proceso");
            Proceso pSig = (Proceso) sig.get("Proceso");
            boolean idle = false;

            if (((Long) actual.get("Rafaga restante")) < 0)
            {
                notificaciones.add(new Notificacion(
                        Notificacion.IDLE,
                        pActual.obtenerCopiaProceso(),
                        keys.get(i + 1) - keys.get(i), keys.get(i)));
                idle = true;
                actual.put("Rafaga restante", ((Long) actual.get("Rafaga restante")) * -1);

            } else
                notificaciones.add(new Notificacion(
                        Notificacion.CAMBIO_CONTEXTO,
                        pActual.obtenerCopiaProceso(),
                        keys.get(i + 1) - keys.get(i), keys.get(i)));

            if (!idle && pActual.PCB.getNumProceso() == pSig.PCB.getNumProceso())
                notificaciones.add(new Notificacion(Notificacion.INTERRUPCION));

            else if (!idle && keys.get(i + 1) - keys.get(i) == ((long) actual.get("Rafaga restante")))
                notificaciones.add(new Notificacion(
                        Notificacion.PROCESO_HA_FINALIZADO,
                        pActual.obtenerCopiaProceso(),
                        0,
                        srtf.getTiemposEspera()[pActual.PCB.getNumProceso()],
                        (long) solucion.get(keys.get(i)).get("Rafaga restante") + keys.get(i)));

            else if (!idle)
                notificaciones.add(new Notificacion(Notificacion.INTERRUPCION));

        }

        Proceso last = (Proceso) solucion.get(keys.get(keys.size() - 1)).get("Proceso");

        notificaciones.add(new Notificacion(
                Notificacion.CAMBIO_CONTEXTO,
                last.obtenerCopiaProceso(),
                (long) solucion.get(keys.get(keys.size() - 1)).get("Rafaga restante"),
                keys.get(keys.size() - 1)));

        notificaciones.add(new Notificacion(
                Notificacion.PROCESO_HA_FINALIZADO,
                last.obtenerCopiaProceso(),
                0,
                srtf.getTiemposEspera()[last.PCB.getNumProceso()],
                (long) solucion.get(keys.get(keys.size() - 1)).get("Rafaga restante") + keys.get(keys.size() - 1)));

        return notificaciones;
    }

    private class SRTF
    {

        private ArrayList<Proceso> procesos;
        private ArrayList<Proceso> listaEspera;
        private HashMap<Long, ArrayList<Proceso>> mapOrderedByEntry;
        private Proceso procesoActual;
        private int[] tiemposEspera;
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
                tiempoTotal += proceso.getTiempoLlegada() + proceso.PCB.getTiempoRafaga();
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

        private HashMap<Long, HashMap<String, Object>> srtf()
        {
            HashMap<Long, HashMap<String, Object>> gantt = new HashMap<>();

            for (long tiempoTranscurrido = 0; tiempoTranscurrido < tiempoTotal; tiempoTranscurrido++)
            {
                if (mapOrderedByEntry.containsKey(tiempoTranscurrido))
                {
                    anadirListaEspera(mapOrderedByEntry.get(tiempoTranscurrido)); // Se añaden todos los procesos que hayan llegado ahora a la lista de espera.
                    Proceso shortest = listaEspera.get(0); // Suponemos que el primer proceso de la lista tiene el menor tiempo ráfaga.

                    // Buscamos al que tenga el menor tiempo ráfaga en la lista.
                    for (int j = 1; j < listaEspera.size(); j++)
                        if (tiempoRestanteProceso(shortest, tiempoTranscurrido) > tiempoRestanteProceso(listaEspera.get(j), tiempoTranscurrido))
                            shortest = listaEspera.get(j);

                    if (tiempoTranscurrido == 0) //Si es el primero que llega.
                    {
                        eliminarListaEspera(shortest); // Eliminamos de la lista de espera al proceso que tiene menos tiempo rágaga.
                        procesoActual = shortest; // Ahora es el actual.
                        ponerEnEjecucion(procesoActual);
                        gantt.put(tiempoTranscurrido, new HashMap<>());
                        gantt.get(tiempoTranscurrido).put("Proceso", procesoActual);
                        gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoRestanteProceso(procesoActual, tiempoTranscurrido));

                    } else if (tiempoRestanteProceso(procesoActual, tiempoTranscurrido) == 0) // Si el proceso actual ha finalizado.
                    {
                        eliminarListaEspera(shortest);
                        finalizarProceso(procesoActual);
                        procesoActual = shortest;
                        gantt.put(tiempoTranscurrido, new HashMap<>());
                        gantt.get(tiempoTranscurrido).put("Proceso", procesoActual);
                        gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoRestanteProceso(procesoActual, tiempoTranscurrido));

                    } else if (tiempoRestanteProceso(shortest, tiempoTranscurrido) < tiempoRestanteProceso(procesoActual, tiempoTranscurrido)) // Si hay una interrupción.
                    {
                        eliminarListaEspera(shortest);
                        anadirListaEspera(procesoActual);
                        procesoActual = shortest;
                        gantt.put(tiempoTranscurrido, new HashMap<>());
                        gantt.get(tiempoTranscurrido).put("Proceso", procesoActual);
                        gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoRestanteProceso(procesoActual, tiempoTranscurrido));

                    } else
                    {
                        if (!listaEspera.contains(shortest))
                            anadirListaEspera(shortest);

                        gantt.put(tiempoTranscurrido, new HashMap<>());
                        gantt.get(tiempoTranscurrido).put("Proceso", procesoActual);
                        gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoRestanteProceso(procesoActual, tiempoTranscurrido));
                    }

                } else if (tiempoRestanteProceso(procesoActual, tiempoTranscurrido) == 0)
                {
                    finalizarProceso(procesoActual);

                    if (listaEspera.isEmpty())
                    {
                        Proceso sig = siguienteProceso(mapOrderedByEntry);

                        if (sig != null)
                        {
                            anadirListaEspera(sig);
                            gantt.put(tiempoTranscurrido, new HashMap<>());
                            gantt.get(tiempoTranscurrido).put("Proceso", sig);
                            gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoTranscurrido - sig.getTiempoLlegada());
                            tiempoTranscurrido = sig.getTiempoLlegada();
                        }
                    }

                    if (!listaEspera.isEmpty())
                    {
                        Proceso shortest = listaEspera.get(0);

                        for (int j = 1; j < listaEspera.size(); j++)
                        {
                            Proceso proceso = listaEspera.get(j);

                            if (tiempoRestanteProceso(shortest, tiempoTranscurrido) > tiempoRestanteProceso(proceso, tiempoTranscurrido))
                                shortest = proceso;

                            else if (tiempoRestanteProceso(shortest, tiempoTranscurrido) == tiempoRestanteProceso(proceso, tiempoTranscurrido))
                                if (shortest.getTiempoLlegada() > proceso.getTiempoLlegada())
                                    shortest = proceso;
                        }

                        gantt.put(tiempoTranscurrido, new HashMap<>());
                        gantt.get(tiempoTranscurrido).put("Proceso", shortest);
                        gantt.get(tiempoTranscurrido).put("Rafaga restante", tiempoRestanteProceso(shortest, tiempoTranscurrido));

                        eliminarListaEspera(shortest);
                        procesoActual = shortest;
                        ponerEnEjecucion(procesoActual);
                    }
                }

                for (Proceso proceso : listaEspera)
                    tiemposEspera[proceso.PCB.getNumProceso()]++;
            }

            return gantt;
        }

        private Proceso siguienteProceso(HashMap<Long, ArrayList<Proceso>> procesos)
        {
            Optional<Proceso> proceso = procesos.entrySet()
                    .stream()
                    .map(Entry::getValue)
                    .flatMap(Collection::stream)
                    .filter(p -> p.PCB.getEstadoProceso().equals(Estado.LISTO))
                    .min(Comparator.comparing(Proceso::getTiempoLlegada));

            if (proceso.isPresent())
                procesos.entrySet()
                        .stream()
                        .map(Entry::getValue)
                        .flatMap(Collection::stream)
                        .filter(p -> p != proceso.get() && p.PCB.getEstadoProceso().equals(Estado.LISTO) && p.getTiempoLlegada() == proceso.get().getTiempoLlegada())
                        .forEach(this::anadirListaEspera);

            return proceso.isPresent() ? procesos.entrySet()
                    .stream()
                    .map(Entry::getValue)
                    .flatMap(Collection::stream)
                    .filter(p -> p.PCB.getEstadoProceso().equals(Estado.LISTO) && p.getTiempoLlegada() == proceso.get().getTiempoLlegada())
                    .min(Comparator.comparing(pr -> pr.PCB.getTiempoRafaga())).get() : null;
        }

        private void eliminarListaEspera(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.LISTO);
            listaEspera.remove(proceso);
        }

        private void anadirListaEspera(ArrayList<Proceso> procesos)
        {
            procesos.forEach(this::anadirListaEspera);
        }

        private void anadirListaEspera(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.ESPERA);
            listaEspera.add(proceso);
        }

        private void ponerEnEjecucion(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.EJECUCION);
        }

        private void finalizarProceso(Proceso proceso)
        {
            proceso.PCB.setEstadoProceso(Estado.TERMINADO);
        }

        private long tiempoRestanteProceso(Proceso proceso, long tiempoUsoDelCPU)
        {
            return proceso.PCB.getTiempoRafaga() - (tiempoUsoDelCPU - proceso.getTiempoLlegada() - tiempoEsperaProceso(proceso));
        }

        private Proceso procesoMayorTiempoLlegada(ArrayList<Proceso> procesos)
        {
            return procesos.stream().max(Comparator.comparing(Proceso::getTiempoLlegada)).get();
        }

        private long tiempoEsperaProceso(Proceso proceso)
        {
            return tiemposEspera[proceso.PCB.getNumProceso()];
        }

        public int[] getTiemposEspera()
        {
            return tiemposEspera;
        }

        public long getTiempoTotal()
        {
            return tiempoTotal;
        }

    }

    private void arreglarProcesos(ArrayList<Proceso> procesos)
    {
        Proceso procesoMinTiempoLlegada = procesos
                .stream()
                .min(Comparator.comparing(Proceso::getTiempoLlegada))
                .get();

        procesos.stream()
                .filter(p -> p.getTiempoLlegada() == procesoMinTiempoLlegada.getTiempoLlegada())
                .forEach(p ->
                {
                    p.PCB.setEstadoProceso(Estado.LISTO);
                    p.setTiempoLlegada(0);
                });
    }

}
