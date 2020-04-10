/*
 * The MIT License
 *
 * Copyright 2020 Eusebio Ajax.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sw.view;

import com.sw.model.AreaLibre;
import com.sw.model.CeldaMemoria;
import com.sw.model.Fragmento;
import com.sw.model.Particion;
import com.sw.model.RAM;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Eusebio Ajax
 */
public class Grafico extends JPanel
{

    private final Color BACKGROUND_COLOR = new Color(252, 252, 252);
    private final Color COLOR_AREA_LIBRE = new Color(131, 157, 165);
    private final Color COLOR_PARTICION = new Color(232, 232, 232);
    private final Color COLOR_FRAGMENTO = new Color(221, 79, 67);
    private final Color COLOR_OS_BLOCK = new Color(0, 112, 192);
    private final Color COLOR_TEXTO = Color.BLACK;

    private final Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 13);
    private final Font OS_BLOCK_FONT = new Font("Tahoma", Font.BOLD, 24);
    private final Font CELDA_MEMORIA_FONT = new Font("Tahoma", Font.PLAIN, 14);

    private final int OFFSET_X = 40;

    private ArrayList<AreaLibre> areasLibres;
    private ArrayList<Particion> particiones;
    private ArrayList<Fragmento> fragmentos;

    private final int MAX_MEMORIA_RAM;
    private final int TAMANIO_MEMORIA_OS;

    public Grafico(int MAX_MEMORIA_RAM, int TAMANIO_MEMORIA_OS)
    {
        this.MAX_MEMORIA_RAM = MAX_MEMORIA_RAM;
        this.TAMANIO_MEMORIA_OS = TAMANIO_MEMORIA_OS;

        areasLibres = new ArrayList<>();
        particiones = new ArrayList<>();
        fragmentos = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        setBackground(BACKGROUND_COLOR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        createOSBlock(g2d);
        dibujarAreasLibres(g2d);
        dibujarParticiones(g2d);
        dibujarFragmentacion(g2d);

        g.dispose();
        g2d.dispose();
    }

    public void actualizarGrafico(ArrayList<AreaLibre> areasLibres, ArrayList<Particion> particiones, ArrayList<Fragmento> fragmentos)
    {
        this.areasLibres = areasLibres;
        this.particiones = particiones;
        this.fragmentos = fragmentos;
        repaint();
    }

    private void createOSBlock(Graphics2D g)
    {
        dibujarCeldaMemoria(g, OS_BLOCK_FONT, "SO", COLOR_OS_BLOCK, Color.WHITE, TAMANIO_MEMORIA_OS, 0);

        g.setColor(COLOR_TEXTO);
        FontMetrics fm = g.getFontMetrics();
        Rectangle bounds = getTextBounds(g, "0K");
        g.drawString("0K", (int) (OFFSET_X - bounds.getWidth()), fm.getAscent());
    }

    private void dibujarAreasLibres(Graphics2D g)
    {
        areasLibres.forEach(areaLibre ->
        {
            dibujarCeldaMemoria(g, CELDA_MEMORIA_FONT, "Área libre", COLOR_AREA_LIBRE, Color.BLACK, areaLibre);
        });
    }

    private void dibujarParticiones(Graphics2D g)
    {
        particiones.forEach(particion ->
        {
            dibujarCeldaMemoria(g, CELDA_MEMORIA_FONT, particion.getProceso().getNombre(), COLOR_PARTICION, Color.BLACK, particion);
        });
    }

    private void dibujarFragmentacion(Graphics2D g)
    {
        fragmentos.forEach(fragmento ->
        {
            dibujarCeldaMemoria(g, CELDA_MEMORIA_FONT, "Área libre", COLOR_FRAGMENTO, Color.BLACK, fragmento);

            g.rotate(Math.toRadians(90));
            g.setColor(COLOR_TEXTO);
            Rectangle bounds = getTextBounds(g, "Fragmentación");
            g.drawString("Fragmentación", (int) (getHeight() - bounds.getWidth()) / 2, -getWidth() + OFFSET_X / 2);
            g.rotate(Math.toRadians(-90));
        });

    }

    private void dibujarCeldaMemoria(Graphics2D g, Font font, String label, Color colorCeldaMemoria, Color colorTexto, int tamanioMemoria, int posicionMemoria)
    {
        Stroke strokeActual = g.getStroke();
        g.setColor(colorCeldaMemoria);
        Rectangle rect = new Rectangle(OFFSET_X, obtenerPosicionEnGrafica(posicionMemoria), getWidth() - OFFSET_X * 2, obtenerTamanioEnGrafica(tamanioMemoria));
        g.fill(rect);
        g.setStroke(new BasicStroke(2));
        g.setColor(BACKGROUND_COLOR);
        g.draw(rect);
        centrarTextoEnRect(g, font, colorTexto, label, rect);

        g.setFont(DEFAULT_FONT);
        g.setColor(COLOR_TEXTO);
        dibujarInfoCeldaMemoria(g, tamanioMemoria, posicionMemoria);
        g.setStroke(strokeActual);
    }

    private void dibujarCeldaMemoria(Graphics2D g, Font font, String label, Color colorCeldaMemoria, Color colorTexto, CeldaMemoria celdaMemoria)
    {
        dibujarCeldaMemoria(g, font, label, colorCeldaMemoria, colorTexto, celdaMemoria.getSize(), celdaMemoria.getInicio());
    }

    private void dibujarInfoCeldaMemoria(Graphics2D g, int tamanioMemoria, int posicionMemoria)
    {
        final int posicionEnGrafica = obtenerPosicionEnGrafica(posicionMemoria);
        final int tamanioEnGrafica = obtenerTamanioEnGrafica(tamanioMemoria);

        FontMetrics fm = g.getFontMetrics();
        String finMemoria = tamanioMemoria + posicionMemoria + "K";
        Rectangle bounds = getTextBounds(g, finMemoria);
        g.drawString(finMemoria, (int) (OFFSET_X - bounds.getWidth()), posicionEnGrafica + tamanioEnGrafica);

        g.drawLine(getWidth() - OFFSET_X - 5, posicionEnGrafica + 5, getWidth() - OFFSET_X - 5, posicionEnGrafica + tamanioEnGrafica - 5);

        Rectangle boundsStringTamanioMemoria = getTextBounds(g, tamanioMemoria + "K");
        g.drawString(tamanioMemoria + "K", (float) (getWidth() - OFFSET_X - boundsStringTamanioMemoria.getWidth() - 5), posicionEnGrafica + (tamanioEnGrafica + fm.getAscent()) / 2);
    }

    private void centrarTextoEnRect(Graphics2D g, Font font, Color colorTexto, String text, Rectangle rect)
    {
        g.setFont(font);
        g.setColor(colorTexto);
        FontMetrics fm = g.getFontMetrics();
        Rectangle bounds = getTextBounds(g, text);
        g.drawString(text, (int) (OFFSET_X + (rect.getWidth() - bounds.getWidth()) / 2), (int) (rect.y + (rect.getHeight() + fm.getAscent()) / 2));
        g.setFont(DEFAULT_FONT);
    }

    /**
     * Regresa la altura (en píxeles) de una {@link CeldaMemoria} de acuerda a su tamaño en memoria.
     *
     * @param tamanioMemoria El tamaño en memoria para una {@link CeldaMemoria}.
     *
     * @return Regresa el tamaño (en píxeles) de la {@link CeldaMemoria} para ser graficada.
     */
    private int obtenerTamanioEnGrafica(int tamanioMemoria)
    {
        return tamanioMemoria * getHeight() / MAX_MEMORIA_RAM;
    }

    /**
     * Regresa la posición (en píxeles) de una {@link CeldaMemoria} de acuerda a su posición inicial en la {@link RAM}.
     *
     * @param inicioMemoria El inicio en memoria para una {@link CeldaMemoria}.
     *
     * @return Regresa la posición (en píxeles) de la {@link CeldaMemoria} para ser graficada.
     */
    private int obtenerPosicionEnGrafica(int inicioMemoria)
    {
        return inicioMemoria * getHeight() / MAX_MEMORIA_RAM;
    }

    private Rectangle getTextBounds(Graphics2D g, String text)
    {
        return g.getFontMetrics().getStringBounds(text, g).getBounds();
    }

}
