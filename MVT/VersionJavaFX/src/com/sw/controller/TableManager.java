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

import com.sw.model.AreaLibre;
import com.sw.model.Particion;
import com.sw.model.Proceso;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author SonBear
 */
public class TableManager
{

    private static TableManager instance;

    private TableManager()
    {

    }

    public void initGenericTableEvents(TableView<?> table)
    {
        table.addEventFilter(KeyEvent.KEY_PRESSED, e ->
        {
            if (e.getCode() == KeyCode.ESCAPE)
                table.getSelectionModel().clearSelection();
        });
    }

    public void initTablaProcesos(TableView<Proceso> tablaProcesos)
    {
        for (int i = 0; i < tablaProcesos.getColumns().size(); i++)
        {
            TableColumn c = tablaProcesos.getColumns().get(i);

            switch (i)
            {
                case 0:
                    c.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                    break;
                case 1:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((Proceso) (((TableColumn.CellDataFeatures) cellData).getValue())).getSize() + " K"));
                    break;
                case 2:
                    c.setCellValueFactory(new PropertyValueFactory<>("tiempoLlegada"));
                    break;
                case 3:
                    c.setCellValueFactory(new PropertyValueFactory<>("duracion"));
                    break;
            }
        }

        initGenericTableEvents(tablaProcesos);
    }

    public void initTablaAreasLibres(TableView<AreaLibre> tablaAreasLibres)
    {
        for (int i = 0; i < tablaAreasLibres.getColumns().size(); i++)
        {
            TableColumn c = tablaAreasLibres.getColumns().get(i);

            switch (i)
            {
                case 0:
                    c.setCellValueFactory(new PropertyValueFactory<>("posicion"));
                    break;
                case 1:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((AreaLibre) (((TableColumn.CellDataFeatures) cellData).getValue())).getInicio() + " K"));
                    break;
                case 2:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((AreaLibre) (((TableColumn.CellDataFeatures) cellData).getValue())).getSize() + " K"));
                    break;
                case 3:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty("Disponible"));
                    break;
                default:
                    throw new AssertionError();
            }
        }

        initGenericTableEvents(tablaAreasLibres);
    }

    public void initTablaParticiones(TableView<Particion> tablaParticiones)
    {
        for (int i = 0; i < tablaParticiones.getColumns().size(); i++)
        {
            TableColumn c = tablaParticiones.getColumns().get(i);

            switch (i)
            {
                case 0:
                    c.setCellValueFactory(new PropertyValueFactory<>("posicion"));
                    break;
                case 1:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((Particion) (((TableColumn.CellDataFeatures) cellData).getValue())).getInicio() + " K"));
                    break;
                case 2:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((Particion) (((TableColumn.CellDataFeatures) cellData).getValue())).getSize() + " K"));
                    break;
                case 3:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty("Asignado"));
                    break;
                case 4:
                    c.setCellValueFactory(cellData -> new SimpleStringProperty(((Particion) (((TableColumn.CellDataFeatures) cellData).getValue())).getProceso().getNombre()));
                    break;
                default:
                    throw new AssertionError();
            }
        }

        initGenericTableEvents(tablaParticiones);
    }

    public void seleccionarUltimaFila(TableView<?> tabla)
    {
        tabla.getSelectionModel().selectLast();
    }

    public void actualizarTabla(TableView<?> tabla)
    {
        Platform.runLater(tabla::refresh);
    }

    public void actualizarItemsTabla(TableView<?> table, ObservableList items)
    {
        Platform.runLater(() ->
        {
            table.setItems(items);
            table.refresh();
        });
    }

    public synchronized static TableManager getInstance()
    {
        if (instance == null)
            instance = new TableManager();

        return instance;
    }

}
