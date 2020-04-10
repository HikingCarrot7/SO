/*
 * The MIT License
 *
 * Copyright 2020 SonBear.
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

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author SonBear
 */
public class RectCeldaMemoria extends Rectangle
{

    private final Label nombreLabel;
    private final Label posicionTerminaLabel;
    private final Label tamanioLabel;
    private final Line tamanioLinea;
    private final int inicioMemoria;
    private final int tamanioMemoria;

    public RectCeldaMemoria(String etiqueta, int inicioMemoria, int tamanioMemoria, double x, double y, double width, double height, Paint fill)
    {
        super(x, y, width, height);
        this.nombreLabel = new Label(etiqueta);
        this.nombreLabel.setMouseTransparent(true);
        this.posicionTerminaLabel = new Label((tamanioMemoria + inicioMemoria) + "K");
        this.tamanioLabel = new Label(tamanioMemoria + "K");
        this.tamanioLabel.setMouseTransparent(true);
        this.tamanioLinea = new Line(x + width - 5, y + 5, x + width - 5, y + height - 5);
        this.inicioMemoria = inicioMemoria;
        this.tamanioMemoria = tamanioMemoria;
        setFill(fill);
    }

    public Label getNombreLabel()
    {
        return nombreLabel;
    }

    public Label getPosicionTerminaLabel()
    {
        return posicionTerminaLabel;
    }

    public Label getTamanioLabel()
    {
        return tamanioLabel;
    }

    public Line getTamanioLinea()
    {
        return tamanioLinea;
    }

    public int getInicioMemoria()
    {
        return inicioMemoria;
    }

    public int getTamanioMemoria()
    {
        return tamanioMemoria;
    }

}
