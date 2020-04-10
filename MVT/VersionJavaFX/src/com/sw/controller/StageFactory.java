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
package com.sw.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author SonBear
 */
public class StageFactory
{

    public static final String RUTA_ESTILOS = "/com/sw/styles/Stylesheet.css";

    /**
     * Crea un {@link Stage}.
     *
     * @param <E>
     * @param ruta La ruta del FXML del nuevo {@link Stage}
     * @param title El título para este {@link Stage}.
     * @param stylesheet La ruta del {@link StyleSheet} para esta {@link Stage}.
     * @param modality El {@link Modality} para este {@link Stage}.
     * @param defaultData
     * @param controllerPadre
     *
     * @return Una referencia al controlador de este nuevo {@link Stage}.
     */
    public static <E> Controller<E> createStage(String ruta, String title, String stylesheet, Modality modality, E defaultData, Controller<Object> controllerPadre)
    {
        return createStage(ruta, title, stylesheet, modality, null, defaultData, controllerPadre);
    }

    /**
     * Crea un {@link Stage}.
     *
     * @param <E>
     * @param ruta La ruta del FXML del nuevo {@link Stage}
     * @param title El título para este {@link Stage}.
     * @param modality El {@link Modality} para este {@link Stage}.
     * @param stylesheet La ruta del {@link StyleSheet} para esta {@link Stage}.
     * @param owner
     * @param defaultData
     * @param controllerPadre
     *
     * @return Una referencia al controlador de este nuevo {@link Stage}.
     */
    public static <E> Controller<E> createStage(String ruta, String title, String stylesheet, Modality modality, Window owner, E defaultData, Controller<Object> controllerPadre)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StageFactory.class.getResource(ruta));
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            ((Controller<E>) loader.getController()).setDefaultData(defaultData, controllerPadre);
            ((Controller) loader.getController()).initStage(stage);
            stage.initOwner(owner);
            scene.getStylesheets().add(stylesheet);
            stage.setTitle(title);
            stage.initModality(modality);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

            return loader.getController();

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

}
