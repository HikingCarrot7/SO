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
package com.sw.main;

import com.sw.controller.StageFactory;
import com.sw.model.Proceso;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author SonBear
 */
public class Main extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        final ObservableList<Proceso> procesos = FXCollections.observableArrayList();

        Proceso p1 = new Proceso("A", 8, 1, 7);
        Proceso p2 = new Proceso("B", 14, 2, 7);
        Proceso p3 = new Proceso("C", 18, 3, 4);
        Proceso p4 = new Proceso("D", 6, 4, 6);
        Proceso p5 = new Proceso("E", 14, 5, 5);

        procesos.add(p1);
        procesos.add(p2);
        procesos.add(p3);
        procesos.add(p4);
        procesos.add(p5);

        StageFactory.createStage("/com/sw/view/Vista.fxml", "MVT", StageFactory.RUTA_ESTILOS, Modality.NONE, procesos, null);
    }

}
