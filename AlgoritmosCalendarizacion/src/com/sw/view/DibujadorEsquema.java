package com.sw.view;

import com.sw.model.Proceso;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferStrategy;

/**
 *
 * @author HikingCarrot7
 */
public class DibujadorEsquema
{

    public static final int HEIGHT = 525;
    public static final int WIDTH = 580;
    public static final int MIDDLE = WIDTH / 2;

    private final Canvas ESQUEMA;
    private final DibujadorProcesador DIBUJADOR_PROCESADOR;
    private final DiagramaGantt DIAGRAMA_GANTT;
    private volatile boolean running;
    private boolean mostrarCambioContexto;
    private long quantums;

    public DibujadorEsquema(Canvas esquema)
    {
        this.ESQUEMA = esquema;
        DIBUJADOR_PROCESADOR = new DibujadorProcesador(this);
        DIAGRAMA_GANTT = new DiagramaGantt(this);
        quantums = -1;
    }

    public void crearRenderer()
    {
        running = true;
        new Renderer(this);
        ESQUEMA.createBufferStrategy(2);
    }

    public void destroyRenderer()
    {
        setRunning(false);
    }

    public void render()
    {
        BufferStrategy bs = ESQUEMA.getBufferStrategy();
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);

        DIBUJADOR_PROCESADOR.dibujarProcesador(g);
        DIAGRAMA_GANTT.dibujarTiemposEsperaProcesos(g);
        DIBUJADOR_PROCESADOR.dibujarProcesoActual(g);
        dibujarSimbologia(g);

        if (quantums > 0)
            dibujarQuantums(g);

        bs.show();
        g.dispose();
    }

    private void dibujarSimbologia(Graphics2D g)
    {
        final int INICIAL_Y = 15;
        final int OFFSET_X = 5;
        final int TRIANGLE_LENGTH = 15;
        final int SEPARACION = 15;
        final Font CURRENT_FONT = g.getFont();

        g.drawString("Simbología", OFFSET_X, INICIAL_Y);

        g.setFont(new Font(CURRENT_FONT.getName(), CURRENT_FONT.getStyle(), CURRENT_FONT.getSize() - 1));
        g.setColor(Color.red);
        g.fillRect(OFFSET_X, INICIAL_Y + 4, 25, 12);
        g.setColor(Color.black);
        g.drawString("Representa que un proceso", OFFSET_X + 30, INICIAL_Y + SEPARACION);
        g.drawString("ha concluido su ejecución", OFFSET_X + 30, INICIAL_Y + SEPARACION * 2);
        g.drawString("en ese intervalo de tiempo", OFFSET_X + 30, INICIAL_Y + SEPARACION * 3);

        if (mostrarCambioContexto)
        {
            drawTurnedTriangle(g, OFFSET_X + TRIANGLE_LENGTH / 2 + 5, INICIAL_Y + SEPARACION * 5 + 3, TRIANGLE_LENGTH, Color.BLUE);
            g.drawString("Representa un cambio de", OFFSET_X + 30, INICIAL_Y + SEPARACION * 5);
            g.drawString("contexto en los procesos", OFFSET_X + 30, INICIAL_Y + SEPARACION * 6);
        }

        g.setFont(CURRENT_FONT);
    }

    private void dibujarQuantums(Graphics2D g)
    {
        dibujarTextoCentradoRect(g, "Quantums : " + quantums, DibujadorProcesador.CPU_HEIGHT - 30, DibujadorProcesador.CPU);
    }

    public void mostrarEnProcesadorProcesoActual(Proceso proceso, long tiempoUsoCpu)
    {
        DIBUJADOR_PROCESADOR.setProcesoActual(proceso);
        DIBUJADOR_PROCESADOR.setTiempoUsoCPUActual(tiempoUsoCpu);
    }

    public void mostrarNoHayProcesoActual()
    {
        DIBUJADOR_PROCESADOR.setProcesoActual(null);
    }

    public void actualizarDiagramaGantt(Proceso proceso, long tiempoEspera)
    {
        DIAGRAMA_GANTT.anadirProcesoAlDiagramaGantt(proceso, tiempoEspera);
    }

    public void marcarUltimoProceso()
    {
        DIAGRAMA_GANTT.marcarUltimoProceso();
    }

    public void dibujarInterrupcion()
    {
        DIAGRAMA_GANTT.marcarCambioDeContexto();
    }

    public void reiniciarEsquema()
    {
        DIAGRAMA_GANTT.getProcesosDibujados().clear();
        DIBUJADOR_PROCESADOR.setProcesoActual(null);
        DIBUJADOR_PROCESADOR.setTiempoUsoCPUActual(-1);
        DIAGRAMA_GANTT.setPromedioTiempoEspera(0);
        DIAGRAMA_GANTT.setnProcesos(0);
    }

    public void dibujarRectanguloCentrado(Graphics2D g, int y, int width, int height)
    {
        g.drawRect(MIDDLE - width / 2, y, width, height);
    }

    public void dibujarRectanguloCentrado(Graphics2D g, Rectangle rect)
    {
        g.drawRect(MIDDLE - rect.width / 2, rect.y, rect.width, rect.height);
    }

    public void dibujarTextoCentradoRect(Graphics2D g, String text, int y, Rectangle rect)
    {
        FontMetrics fm = g.getFontMetrics();
        Rectangle bounds = fm.getStringBounds(text, g).getBounds();
        g.drawString(text, (float) (rect.x + rect.width / 2 - bounds.getWidth() / 2), rect.y + y);
    }

    public void dibujarStringPunto(Graphics2D g, String text, int x, int y)
    {
        Rectangle rect = g.getFontMetrics().getStringBounds(text, g).getBounds();
        g.drawString(text, x - rect.width / 2, y + rect.height / 2 + 4);
    }

    public void drawTriangle(Graphics2D g, int x, int y, int lenght)
    {
        int offset = (int) Math.sqrt(Math.pow(lenght, 2) - Math.pow((lenght / 2), 2));

        int[] puntosX =
        {
            x - offset, x, x - offset
        };

        int[] puntosY =
        {
            y - offset, y, y + offset
        };

        GeneralPath triangle = new GeneralPath();

        triangle.moveTo(puntosX[0], puntosY[0]);

        for (int i = 0; i < puntosX.length; i++)
            triangle.lineTo(puntosX[i], puntosY[i]);

        triangle.closePath();
        g.fill(triangle);
    }

    public void drawInvertedTriangle(Graphics2D g, int x, int y, int lenght)
    {
        int offset = (int) Math.sqrt(Math.pow(lenght, 2) - Math.pow((lenght / 2), 2));

        int[] puntosX =
        {
            x + offset, x, x + offset
        };

        int[] puntosY =
        {
            y + offset, y, y - offset
        };

        GeneralPath triangle = new GeneralPath();

        triangle.moveTo(puntosX[0], puntosY[0]);

        for (int i = 0; i < puntosX.length; i++)
            triangle.lineTo(puntosX[i], puntosY[i]);

        triangle.closePath();
        g.fill(triangle);
    }

    public void drawTurnedTriangle(Graphics2D g, int x, int y, int lenght, Color color)
    {
        int offset = (int) Math.sqrt(Math.pow(lenght, 2) - Math.pow((lenght / 2), 2));

        int[] puntosX =
        {
            x - offset, x, x + offset
        };

        int[] puntosY =
        {
            y - offset, y, y - offset
        };

        GeneralPath triangle = new GeneralPath();

        triangle.moveTo(puntosX[0], puntosY[0]);

        for (int i = 0; i < puntosX.length; i++)
            triangle.lineTo(puntosX[i], puntosY[i]);

        triangle.closePath();

        final Color CURRENT_COLOR = g.getColor();

        g.setColor(color);
        g.fill(triangle);
        g.setColor(CURRENT_COLOR);
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public void setMostrarCambioContexto(boolean mostrarCambioContexto)
    {
        this.mostrarCambioContexto = mostrarCambioContexto;
    }

    public void setQuantums(long quantums)
    {
        this.quantums = quantums;
    }

    private class Renderer implements Runnable
    {

        private final DibujadorEsquema DIBUJADOR_ESQUEMA;

        public Renderer(final DibujadorEsquema DIBUJADOR_ESQUEMA)
        {
            this.DIBUJADOR_ESQUEMA = DIBUJADOR_ESQUEMA;
            new Thread(this).start();
        }

        @Override
        public void run()
        {
            long lastTime = System.nanoTime();
            final double amountOfThicks = 60.0;
            double ns = 1_000_000_000 / amountOfThicks;
            double delta = 0;

            while (DIBUJADOR_ESQUEMA.isRunning())
            {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                if (delta >= 1)
                {
                    DIBUJADOR_ESQUEMA.render();
                    delta--;
                }

            }

        }

    }

}
