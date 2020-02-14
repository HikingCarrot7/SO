package com.sw.view;

import com.sw.model.Estado;
import com.sw.model.Proceso;
import static com.sw.view.DibujadorEsquema.WIDTH;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author HikingCarrot7
 */
public class DiagramaGantt
{

    public static final int PROCESO_RECT_WIDTH = 40;
    public static final int PROCESO_RECT_HEIGHT = 20;
    public static final int OFFSET_X = 20;
    public static final int OFFSET_Y = 230;
    public static final int SEPARACION_POR_LINEA = 30;
    public static final int TINY_TRIANGLE = 5;
    public static final int MAX_PROCESOS_COL = (WIDTH - OFFSET_X * 2) / PROCESO_RECT_WIDTH;
    public static final int MAX_PROCESOS_ROW = 6;
    public static final int MAX_PROCESOS = MAX_PROCESOS_COL * MAX_PROCESOS_ROW;

    private final DibujadorEsquema DIBUJADOR_ESQUEMA;
    private final ArrayList<Proceso> TIEMPO_ESPERA_PROCESOS;
    private final ArrayList<Point> INTERRUPCIONES;

    private double promedioTiempoEspera;
    private int nProcesos;

    public DiagramaGantt(DibujadorEsquema dibujadorEsquema)
    {
        this.DIBUJADOR_ESQUEMA = dibujadorEsquema;
        TIEMPO_ESPERA_PROCESOS = new ArrayList<>();
        INTERRUPCIONES = new ArrayList<>();
    }

    public void anadirProcesoAlDiagramaGantt(Proceso proceso, long tiempoEspera)
    {
        proceso.PCB.setTiempoEjecutado(tiempoEspera);
        TIEMPO_ESPERA_PROCESOS.add(proceso);
        promedioTiempoEspera += tiempoEspera;
        nProcesos++;
    }

    public void dibujarTiemposEsperaProcesos(Graphics2D g)
    {
        int y = OFFSET_Y;

        g.drawString("Diagrama de Gantt", OFFSET_X, OFFSET_Y - 5);
        dibujarInterrupciones(g);

        for (int i = 0, x = OFFSET_X; i < TIEMPO_ESPERA_PROCESOS.size(); i++, x += PROCESO_RECT_WIDTH)
        {
            Proceso proceso = TIEMPO_ESPERA_PROCESOS.get(i);

            dibujarRectanguloProceso(g, proceso, x, y);
            dibujarInfoProceso(g, proceso, x, y);
            dibujarTiempoEsperaPromedio(g, DibujadorProcesador.CPU_HEIGHT - 10);

            if ((i + 1) % MAX_PROCESOS_COL == 0 && TIEMPO_ESPERA_PROCESOS.size() - (i + 1) > 0)
            {
                drawLargeLine(g, OFFSET_X + MAX_PROCESOS_COL * PROCESO_RECT_WIDTH, y + PROCESO_RECT_HEIGHT / 2);
                x = OFFSET_X - PROCESO_RECT_WIDTH;
                y += PROCESO_RECT_HEIGHT + SEPARACION_POR_LINEA;
            }

        }

        if (TIEMPO_ESPERA_PROCESOS.size() >= MAX_PROCESOS)
            for (int i = 0; i < MAX_PROCESOS_COL; i++)
                TIEMPO_ESPERA_PROCESOS.remove(0);
    }

    private void dibujarTiempoEsperaPromedio(Graphics2D g, int y)
    {
        DIBUJADOR_ESQUEMA.dibujarTextoCentradoRect(g,
                "Espera promedio: " + String.format("%,.2f", ((double) (promedioTiempoEspera / nProcesos))),
                y,
                DibujadorProcesador.CPU);
    }

    private void dibujarRectanguloProceso(Graphics2D g, Proceso proceso, int x, int y)
    {
        if (proceso != null && proceso.esProcesoTerminado())
        {
            g.setColor(Color.RED);
            g.fillRect(x, y, PROCESO_RECT_WIDTH, PROCESO_RECT_HEIGHT);
            g.setColor(Color.BLACK);
        }

        g.drawRect(x, y, PROCESO_RECT_WIDTH, PROCESO_RECT_HEIGHT);
    }

    private void dibujarInterrupciones(Graphics2D g)
    {
        for (int i = 0; i < INTERRUPCIONES.size(); i++)
        {
            Point interrupcion = INTERRUPCIONES.get(i);
            DIBUJADOR_ESQUEMA.drawTurnedTriangle(g, interrupcion.x, interrupcion.y, 5, Color.BLUE);
        }

    }

    private void dibujarInfoProceso(Graphics2D g, Proceso proceso, int x, int y)
    {
        final int LINE_LENGTH = 5;
        final Font CURRENT_FONT = g.getFont();
        final Font TIME_FONT = new Font(CURRENT_FONT.getFontName(), CURRENT_FONT.getStyle(), CURRENT_FONT.getSize() - 2);

        g.drawLine(x, y + PROCESO_RECT_HEIGHT, x, y + PROCESO_RECT_HEIGHT + LINE_LENGTH);
        DIBUJADOR_ESQUEMA.drawInvertedTriangle(g, x, y + PROCESO_RECT_HEIGHT, TINY_TRIANGLE);

        if (proceso != null)
        {
            g.setFont(TIME_FONT);
            DIBUJADOR_ESQUEMA.dibujarStringPunto(g, String.valueOf(proceso.PCB.getTiempoEjecutado()), x, y + PROCESO_RECT_HEIGHT + LINE_LENGTH);
            g.setFont(CURRENT_FONT);
            DIBUJADOR_ESQUEMA.dibujarStringPunto(g, proceso.getIdentificador(), x + PROCESO_RECT_WIDTH / 2, y + 3);
        }

    }

    public void drawLargeLine(Graphics2D g, int x, int y)
    {
        g.drawLine(x, y, WIDTH - OFFSET_X, y);
        g.drawLine(WIDTH - OFFSET_X, y, WIDTH - OFFSET_X, y + PROCESO_RECT_HEIGHT + 10);
        g.drawLine(WIDTH - OFFSET_X, y + PROCESO_RECT_HEIGHT + 10, OFFSET_X - OFFSET_X / 2, y + PROCESO_RECT_HEIGHT + 10);
        g.drawLine(OFFSET_X - OFFSET_X / 2, y + PROCESO_RECT_HEIGHT + 10, OFFSET_X - OFFSET_X / 2, y + SEPARACION_POR_LINEA + PROCESO_RECT_HEIGHT);
        g.drawLine(OFFSET_X - OFFSET_X / 2, y + SEPARACION_POR_LINEA + PROCESO_RECT_HEIGHT, OFFSET_X, y + SEPARACION_POR_LINEA + PROCESO_RECT_HEIGHT);
    }

    public void marcarUltimoProceso()
    {
        int size = TIEMPO_ESPERA_PROCESOS.size();
        Proceso p = TIEMPO_ESPERA_PROCESOS.get(size - 1);
        p.PCB.setEstadoProceso(Estado.TERMINADO);
    }

    public void marcarCambioDeContexto()
    {
        int x = OFFSET_X + (TIEMPO_ESPERA_PROCESOS.size() % MAX_PROCESOS_COL) * PROCESO_RECT_WIDTH;
        int y = OFFSET_Y + (TIEMPO_ESPERA_PROCESOS.size() / MAX_PROCESOS_COL) * (PROCESO_RECT_HEIGHT + SEPARACION_POR_LINEA);
        INTERRUPCIONES.add(new Point(x, y));
    }

    private void subirNivelMarcasInterrupciones()
    {

    }

    private int obtenerNivelMarcarInterrupcion()
    {
        return 0;
    }

    public ArrayList<Proceso> getProcesosDibujados()
    {
        return TIEMPO_ESPERA_PROCESOS;
    }

    public void setPromedioTiempoEspera(double promedioTiempoEspera)
    {
        this.promedioTiempoEspera = promedioTiempoEspera;
    }

    public void setnProcesos(int nProcesos)
    {
        this.nProcesos = nProcesos;
    }

}
