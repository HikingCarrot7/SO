package com.sw.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
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
                ArrayList<Notificacion> notificaciones = obtenerNotificaciones(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada).thenComparing(p -> p.PCB.getNumProceso()))
                        .collect(Collectors.toCollection(ArrayList::new)));

                notificaciones.stream().filter(n -> n.getProceso() != null).forEach(System.out::println);
                for (Notificacion notif : notificaciones)
                {
                    notificar(notif);

                    if (cpu != null && notif.getIdentificador() != Notificacion.INTERRUPCION)
                    {
                        cpu.ejecutarProceso(notif.getProceso(), notif.getTiempoUsoCPU());
                        esperar();
                    }
                }

                SRTF bloques = new SRTF(procesos.stream()
                        .sorted(Comparator.comparing(Proceso::getTiempoLlegada))
                        .collect(Collectors.toCollection(ArrayList::new)));

                bloques.srtf().entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).forEach(System.out::println);

                for (Proceso proceso : procesos)
                    System.out.println("El proceso " + proceso.getIdentificador() + " esperó " + bloques.getTiemposEspera()[proceso.PCB.getNumProceso()]);

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

            notificaciones.add(new Notificacion(
                    Notificacion.CAMBIO_CONTEXTO,
                    pActual.obtenerCopiaProceso(),
                    keys.get(i + 1) - keys.get(i), keys.get(i)));

            if (pActual.PCB.getNumProceso() == pSig.PCB.getNumProceso())
                notificaciones.add(new Notificacion(Notificacion.INTERRUPCION));

            else if (keys.get(i + 1) - keys.get(i) == ((long) actual.get("Rafaga restante")))
                notificaciones.add(new Notificacion(
                        Notificacion.PROCESO_HA_FINALIZADO,
                        pActual.obtenerCopiaProceso(),
                        0,
                        srtf.getTiemposEspera()[pActual.PCB.getNumProceso()],
                        (long) solucion.get(keys.get(i)).get("Rafaga restante") + keys.get(i)));

            else
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

        private HashMap<Long, HashMap<String, Object>> srtf()
        {
            HashMap<Long, HashMap<String, Object>> gantt = new HashMap<>();

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
                        gantt.get(i).put("Proceso", procesoActual);
                        gantt.get(i).put("Rafaga restante", tiempoRestanteProceso(procesoActual, i));

                    } else if (tiempoRestanteProceso(procesoActual, i) == 0)
                    {
                        eliminarListaEspera(shortest);
                        procesoActual = shortest;
                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual);
                        gantt.get(i).put("Rafaga restante", tiempoRestanteProceso(procesoActual, i));

                    } else if (tiempoRestanteProceso(shortest, i) < tiempoRestanteProceso(procesoActual, i))
                    {
                        eliminarListaEspera(shortest);
                        listaEspera.add(procesoActual);
                        procesoActual = shortest;
                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual);
                        gantt.get(i).put("Rafaga restante", tiempoRestanteProceso(procesoActual, i));

                    } else
                    {
                        if (!listaEspera.contains(shortest))
                            listaEspera.add(shortest);

                        gantt.put(i, new HashMap<>());
                        gantt.get(i).put("Proceso", procesoActual);
                        gantt.get(i).put("Rafaga restante", tiempoRestanteProceso(procesoActual, i));
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
                    gantt.get(i).put("Proceso", shortest);
                    gantt.get(i).put("Rafaga restante", tiempoRestanteProceso(shortest, i));

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

        public long getTiempoTotal()
        {
            return tiempoTotal;
        }

    }

    /**
     * Establece que todos los procesos han sido entregados.
     */
    public void todosProcesosEntregados()
    {
        todosProcesosEntregados = true;
    }

}
