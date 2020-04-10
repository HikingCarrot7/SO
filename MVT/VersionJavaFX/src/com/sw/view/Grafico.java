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

import static com.sw.controller.Utilidades.getFontHeight;
import static com.sw.controller.Utilidades.getFontWidth;
import com.sw.model.AreaLibre;
import com.sw.model.CeldaMemoria;
import com.sw.model.Fragmento;
import com.sw.model.OS;
import com.sw.model.Particion;
import com.sw.model.RAM;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import static javafx.scene.Cursor.HAND;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.GAINSBORO;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author SonBear
 */
public class Grafico
{

    private final Color BACKGROUND_COLOR = rgb(252, 252, 252);
    private final Color STROKE_COLOR = GAINSBORO;

    private final int WIDTH_LATERAL_IZQUIERDO = 30;
    private final int WIDTH_LATERAL_DERECHO = 30;

    private final int MAX_MEMORIA_RAM;
    private final int TAMANIO_MEMORIA_OS;

    private final Pane panel;

    public Grafico(Pane panel, final int MAX_TAMANIO_RAM, final int TAMANIO_MEMORIA_OS)
    {
        this.panel = panel;
        this.MAX_MEMORIA_RAM = MAX_TAMANIO_RAM;
        this.TAMANIO_MEMORIA_OS = TAMANIO_MEMORIA_OS;
    }

    /**
     * Dibuja el gráfico para el {@link ObservableList} de las áreas libres y particiones en la {@link RAM}.
     *
     * @param areasLibres El {@link ObservableList} que representa las {@link AreaLibre}.
     * @param particiones El {@link ObservableList} que representa las {@link Particion}.
     * @param fragmentos El {@link ObservableList} que representa las {@link Fragmento}.
     */
    public void dibujarRepresentacionGrafica(ObservableList<AreaLibre> areasLibres, ObservableList<Particion> particiones, ObservableList<Fragmento> fragmentos)
    {
        createOSBlock();
        areasLibres.forEach(this::dibujarAreaLibre);
        dibujarFragmentos(fragmentos);
        particiones.forEach(this::dibujarParticion);
        createLateralIzquierdo();
        createLateralDerecho();
        traerAlFrenteTodasLasEtiquetas();
        traerAlFrenteTodasLasEtiquetas();
    }

    /**
     * Trae al frente todas las {@link Label} de las {@link CeldaMemoria}.
     */
    private void traerAlFrenteTodasLasEtiquetas()
    {
        for (int i = 0; i < panel.getChildren().size(); i++)
        {
            Node n = panel.getChildren().get(i);

            if (n instanceof Label || n instanceof Line)
                n.toFront();
        }
    }

    /**
     * Dibuja las {@link AreaLibre} contenidas en la {@link ObservableList} especificada.
     *
     * @param areaLibre La {@link ObservableList} de las {@link AreaLibre} ha dibujar.
     */
    private void dibujarAreaLibre(AreaLibre areaLibre)
    {
        RectCeldaMemoria rectAreaLibre = createRectCeldaMemoria("Área libre", areaLibre, rgb(131, 157, 165));

        rectAreaLibre.heightProperty().bind(new Escalador(rectAreaLibre).getScaleHeightProperty());
        rectAreaLibre.yProperty().bind(new Reposicionador(rectAreaLibre).getPosicionHeightProperty());

        panel.getChildren().addAll(rectAreaLibre,
                rectAreaLibre.getNombreLabel(),
                rectAreaLibre.getPosicionTerminaLabel(),
                rectAreaLibre.getTamanioLinea(),
                rectAreaLibre.getTamanioLabel());
    }

    /**
     * Dibuja las {@link Particion} contenidas en la {@link ObservableList} especificada.
     *
     * @param particion La {@link ObservableList} de las {@link Particion} ha dibujar.
     */
    private void dibujarParticion(Particion particion)
    {
        RectCeldaMemoria rectParticion = createRectCeldaMemoria(particion.getProceso().getNombre(), particion, rgb(232, 232, 232));

        rectParticion.heightProperty().bind(new Escalador(rectParticion).getScaleHeightProperty());
        rectParticion.yProperty().bind(new Reposicionador(rectParticion).getPosicionHeightProperty());

        panel.getChildren().addAll(rectParticion,
                rectParticion.getNombreLabel(),
                rectParticion.getPosicionTerminaLabel(),
                rectParticion.getTamanioLinea(),
                rectParticion.getTamanioLabel());
    }

    private void dibujarFragmentos(ObservableList<Fragmento> fragmentos)
    {
        for (Fragmento fragmento : fragmentos)
        {
            RectCeldaMemoria rectParticion = createRectCeldaMemoria("", fragmento, rgb(221, 79, 67));

            rectParticion.heightProperty().bind(new Escalador(rectParticion).getScaleHeightProperty());
            rectParticion.yProperty().bind(new Reposicionador(rectParticion).getPosicionHeightProperty());
            panel.getChildren().addAll(rectParticion,
                    rectParticion.getNombreLabel(),
                    rectParticion.getPosicionTerminaLabel(),
                    rectParticion.getTamanioLinea(),
                    rectParticion.getTamanioLabel());
        }

        if (fragmentos.size() > 1)
        {
            Label label = new Label("Fragmentación");
            label.setRotate(90);
            label.setId("fragmentacionLabel");
            panel.getChildren().add(label);
        }

    }

    /**
     * Crea el {@link Rectangle} que representa a nuestro {@link OS}.
     */
    private void createOSBlock()
    {
        RectCeldaMemoria os = createRectCeldaMemoria("SO", 0, TAMANIO_MEMORIA_OS, 0, 0, panel.getWidth(), obtenerTamanioEnGrafica(TAMANIO_MEMORIA_OS), rgb(0, 112, 192));
        os.heightProperty().bind(new Escalador(os).getScaleHeightProperty());
        os.yProperty().bind(new Reposicionador(os).getPosicionHeightProperty());
        os.getNombreLabel().setStyle("-fx-font-family: 'Helvetica Neue';-fx-font-size: 1.5em;-fx-font-weight: bold;-fx-text-fill: white;");
        panel.getChildren().addAll(os,
                os.getNombreLabel(),
                os.getPosicionTerminaLabel(),
                os.getTamanioLinea(),
                os.getTamanioLabel());
    }

    /**
     * Crea un {@link Rectangle} que representa una {@link CeldaMemoria} en la {@link RAM}.
     *
     * @param label El texto representativo para esta {@link CeldaMemoria}.
     * @param inicio El la posición inicial de esta {@link CeldaMemoria} en la {@link RAM}.
     * @param tamanio El tamaño de esta {@link CeldaMemoria}.
     * @param x La posición en x para esta {@link CeldaMemoria}.
     * @param y La posición en y para esta {@link CeldaMemoria}.
     * @param width El ancho (en píxeles) para esta {@link CeldaMemoria}.
     * @param height El alto (en píxeles) para esta {@link CeldaMemoria}.
     * @param fill El {@link Paint} para rellenar esta {@link CeldaMemoria}.
     *
     * @return El {@link Rectangle} que representa esta {@link CeldaMemoria}.
     */
    private RectCeldaMemoria createRectCeldaMemoria(String label, int inicio, int tamanio, double x, double y, double width, double height, Paint fill)
    {
        RectCeldaMemoria rect = new RectCeldaMemoria(label, inicio, tamanio, x, y, width, height, fill);
        rect.widthProperty().bind(panel.widthProperty());
        rect.setStroke(STROKE_COLOR);
        rect.setStrokeWidth(2);

        rect.setOnMouseEntered(e ->
        {
            rect.setFill(((Color) rect.getFill()).brighter());
            rect.setCursor(HAND);
        });

        rect.setOnMouseExited(e ->
        {
            rect.setFill(fill);
        });

        rect.toFront();
        return rect;
    }

    /**
     * Crea un {@link Rectangle} que representa una {@link CeldaMemoria} en la {@link RAM}.
     *
     * @param celdaMemoria La {@link CeldaMemoria} de la cual se quiere crear su representación.
     * @param fill El {@link Paint} para rellenar esta {@link CeldaMemoria}.
     *
     * @return El {@link Rectangle} que representa esta {@link CeldaMemoria}.
     */
    private RectCeldaMemoria createRectCeldaMemoria(String label, CeldaMemoria celdaMemoria, Paint fill)
    {
        return createRectCeldaMemoria(label,
                celdaMemoria.getInicio(),
                celdaMemoria.getSize(), 0,
                obtenerPosicionEnGrafica(celdaMemoria.getInicio()),
                panel.getWidth(),
                obtenerTamanioEnGrafica(celdaMemoria.getSize()), fill);
    }

    /**
     * Crea el lateral donde se muestra el tamaño de cada {@link CeldaMemoria}.
     */
    private void createLateralIzquierdo()
    {
        Rectangle lateral = new Rectangle(0, 0, WIDTH_LATERAL_IZQUIERDO, panel.getHeight());
        lateral.setFill(BACKGROUND_COLOR);
        lateral.heightProperty().bind(panel.heightProperty());
        Label inicio = new Label("0K");
        inicio.setTranslateX(WIDTH_LATERAL_IZQUIERDO - getFontWidth(inicio.getText(), inicio.getFont()) - 4);
        inicio.setTranslateY(-(getFontHeight(inicio.getFont()) / 2) - 2);
        panel.getChildren().addAll(lateral, inicio);
    }

    /**
     * Crea el lateral donde se muestra el tamaño de cada {@link CeldaMemoria}.
     */
    private void createLateralDerecho()
    {
        Rectangle lateral = new Rectangle(panel.getWidth() - WIDTH_LATERAL_DERECHO, 0, WIDTH_LATERAL_DERECHO, panel.getHeight());
        lateral.setFill(BACKGROUND_COLOR);
        lateral.heightProperty().bind(panel.heightProperty());
        panel.getChildren().add(lateral);
    }

    /**
     * Regresa la altura (en píxeles) de una {@link CeldaMemoria} de acuerda a su tamaño en memoria.
     *
     * @param tamanioMemoria El tamaño en memoria para una {@link CeldaMemoria}.
     *
     * @return Regresa el tamaño (en píxeles) de la {@link CeldaMemoria} para ser graficada.
     */
    private double obtenerTamanioEnGrafica(int tamanioMemoria)
    {
        return tamanioMemoria * panel.getHeight() / MAX_MEMORIA_RAM;
    }

    /**
     * Regresa la posición (en píxeles) de una {@link CeldaMemoria} de acuerda a su posición inicial en la {@link RAM}.
     *
     * @param inicioMemoria El inicio en memoria para una {@link CeldaMemoria}.
     *
     * @return Regresa la posición (en píxeles) de la {@link CeldaMemoria} para ser graficada.
     */
    private double obtenerPosicionEnGrafica(int inicioMemoria)
    {
        return inicioMemoria * panel.getHeight() / MAX_MEMORIA_RAM;
    }

    /**
     * Elimina todos los nodos correspodientes a este {@link Pane}.
     */
    public void refrescarGrafico()
    {
        panel.getChildren().clear();
    }

    /**
     * Cuando se cambia de tamaño al {@link Stage}, esta clase se encarga de actualizar el alto de los {@link Rectangle} que representan a las {@link CeldaMemoria} en el gráfico.
     *
     * Podemos decir que los objetos de esta clase están a la escucha de los cambios de tamaño del panel donde se dibuja el gráfico (dichos cambios se dan cuando se cambia el tamaño de la ventana).
     */
    private class Escalador implements ChangeListener<Number>
    {

        private final DoubleProperty scaleHeightProperty;
        private final RectCeldaMemoria rectCeldaMemoriaAEscalar;

        public Escalador(RectCeldaMemoria rectCeldaMemoriaAEscalar)
        {
            this.rectCeldaMemoriaAEscalar = rectCeldaMemoriaAEscalar;
            scaleHeightProperty = new SimpleDoubleProperty(obtenerTamanioEnGrafica(rectCeldaMemoriaAEscalar.getTamanioMemoria()));
            panel.heightProperty().addListener(this);
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
        {
            scaleHeightProperty.setValue(obtenerTamanioEnGrafica(rectCeldaMemoriaAEscalar.getTamanioMemoria()));
        }

        /**
         * Retorna el {@link DoubleProperty} de este {@link Escalador}.
         *
         * @return El {@link DoubleProperty} de este {@link Escalador}.
         */
        public DoubleProperty getScaleHeightProperty()
        {
            return scaleHeightProperty;
        }

    }

    /**
     * "Arregla" la posición en la que los {@link Rectangle} que representan las {@link CeldaMemoria} deberían de dibujarse. También se encarga de reposicionar las {@link Label} de las {@link CeldaMemoria}.
     */
    private class Reposicionador implements ChangeListener<Number>
    {

        private final DoubleProperty posicionHeightProperty;
        private final RectCeldaMemoria rectCeldaMemoriaAReposicionar;

        public Reposicionador(RectCeldaMemoria rectCeldaMemoriaAReposicionar)
        {
            this.rectCeldaMemoriaAReposicionar = rectCeldaMemoriaAReposicionar;
            posicionHeightProperty = new SimpleDoubleProperty(obtenerPosicionEnGrafica(rectCeldaMemoriaAReposicionar.getInicioMemoria()));
            panel.heightProperty().addListener(this);
            reposicionarEtiqueta();
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
        {
            reposicionarEtiqueta();
            posicionHeightProperty.setValue(obtenerPosicionEnGrafica(rectCeldaMemoriaAReposicionar.getInicioMemoria()));
        }

        /**
         * Reposiciona a la {@link Label} contenida en esta {@link CeldaMemoria}.
         */
        private void reposicionarEtiqueta()
        {
            double posicionEnGrafica = obtenerPosicionEnGrafica(rectCeldaMemoriaAReposicionar.getInicioMemoria());
            double tamanioEnGrafica = obtenerTamanioEnGrafica(rectCeldaMemoriaAReposicionar.getTamanioMemoria());
            Label nombreLabel = rectCeldaMemoriaAReposicionar.getNombreLabel();
            Label posicionTerminaLabel = rectCeldaMemoriaAReposicionar.getPosicionTerminaLabel();
            Label tamanioLabel = rectCeldaMemoriaAReposicionar.getTamanioLabel();
            Line tamanioLinea = rectCeldaMemoriaAReposicionar.getTamanioLinea();

            Label label = (Label) panel.lookup("#fragmentacionLabel");

            if (label != null)
            {
                label.setTranslateX(panel.getWidth() - getFontWidth(label.getText(), label.getFont()) / 2 - getFontHeight(label.getFont()) / 2);
                label.setTranslateY((panel.getHeight() - getFontHeight(label.getFont())) / 2);
            }

            /**
             * La línea estética para representar el tamaño de cada celda de memoria.
             */
            tamanioLinea.setStartX(panel.getWidth() - 5 - WIDTH_LATERAL_DERECHO);
            tamanioLinea.setStartY(posicionEnGrafica + 5);
            tamanioLinea.setEndX(panel.getWidth() - 5 - WIDTH_LATERAL_DERECHO);
            tamanioLinea.setEndY(posicionEnGrafica + rectCeldaMemoriaAReposicionar.getHeight() - 5);

            tamanioLabel.setTranslateX(tamanioLinea.getStartX() - getFontWidth(tamanioLabel.getText(), tamanioLabel.getFont()) - 3);
            tamanioLabel.setTranslateY(posicionEnGrafica + tamanioEnGrafica / 2 - getFontHeight(tamanioLabel.getFont()) / 2);

            posicionTerminaLabel.setTranslateX(WIDTH_LATERAL_IZQUIERDO - getFontWidth(posicionTerminaLabel.getText(), posicionTerminaLabel.getFont()));
            posicionTerminaLabel.setTranslateY(posicionEnGrafica + tamanioEnGrafica - getFontHeight(posicionTerminaLabel.getFont()) / 2);

            nombreLabel.setTranslateX((panel.getWidth() - getFontWidth(nombreLabel.getText(), nombreLabel.getFont()) + WIDTH_LATERAL_IZQUIERDO - WIDTH_LATERAL_DERECHO) / 2);
            nombreLabel.setTranslateY(posicionEnGrafica + tamanioEnGrafica / 2 - getFontHeight(nombreLabel.getFont()) / 2);
        }

        /**
         * Retorna el {@link DoubleProperty} de este {@link Reposicionador}.
         *
         * @return El {@link DoubleProperty} de este {@link Reposicionador}.
         */
        public DoubleProperty getPosicionHeightProperty()
        {
            return posicionHeightProperty;
        }

    }

}
