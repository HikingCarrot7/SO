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
import com.sw.model.OS;
import com.sw.model.Particion;
import com.sw.model.Proceso;
import com.sw.model.RAM;
import com.sw.view.Grafico;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SonBear
 */
public class VistaController implements Initializable, Observer, Controller<ObservableList<Proceso>>
{

    @FXML
    private TableView<Proceso> tablaProcesos;
    @FXML
    private TableView<AreaLibre> tablaAreasLibres;
    @FXML
    private TableView<Particion> tablaParticiones;
    @FXML
    private Button btnSigPaso;
    @FXML
    private Button btnAdmProcesos;
    @FXML
    private Pane panel;
    @FXML
    private Label estado;

    private OS os;
    private RAM ram;
    private TableManager tableManager;
    private Grafico grafico;
    private Stage myStage;
    private ObservableList<Proceso> procesos;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ram = new RAM(64);
        tableManager = TableManager.getInstance();
        btnSigPaso.setWrapText(true);
        btnSigPaso.setCursor(Cursor.HAND);
        btnAdmProcesos.setCursor(Cursor.HAND);
        initTablas();
    }

    private void initTablas()
    {
        tableManager.initTablaProcesos(tablaProcesos);
        tableManager.initTablaAreasLibres(tablaAreasLibres);
        tableManager.initTablaParticiones(tablaParticiones);
    }

    @Override
    public void setDefaultData(ObservableList<Proceso> procesos, Controller<Object> controllerPadre)
    {
        this.procesos = procesos;
        reiniciarSimulacion(procesos);

        if (grafico == null)
        {
            grafico = new Grafico(panel, ram.MAX_TAM_MEMORIA(), os.MEMORIA_OS());
            actualizarGrafico();
        }
    }

    @Override
    public void initStage(Stage myStage)
    {
        this.myStage = myStage;
    }

    @FXML
    private void sigPaso(ActionEvent e)
    {
        os.siguienteMomento();
        actualizarBtnMomentos();
        actualizarGrafico();
        actualizarTablas();

        if (os.getMomento() == OS.MOMENTO_FINAL)
            if (mostrarConfirmacion("Simulación terminada.", "La simulación ha terminado. ¿Desea salir?"))
                Platform.exit();
    }

    @FXML
    private void admProcesos(ActionEvent e)
    {
        if (os.getMomento() == OS.MOMENTO_INICIAL)
            crearVentanaAdmProcesos();

        else if (mostrarConfirmacion("Ya ha iniciado la simulación.", "La simulación se reiniciará. ¿Está seguro?"))
            crearVentanaAdmProcesos();
    }

    private void crearVentanaAdmProcesos()
    {
        Platform.runLater(() ->
        {
            StageFactory.createStage("/com/sw/view/AdmProcesos.fxml",
                    "Administrador de procesos",
                    StageFactory.RUTA_ESTILOS,
                    Modality.APPLICATION_MODAL,
                    myStage,
                    tablaProcesos.getItems(),
                    (Controller) this);
        });
    }

    private void reiniciarSimulacion(ObservableList<Proceso> procesos)
    {
        os = new OS(10, ram, procesos);
        os.addObserver(this);
        actualizarBtnMomentos();
        actualizarGrafico();
        actualizarTablas();
        actualizarEstado(" ");
    }

    private void actualizarTablas()
    {
        tableManager.actualizarItemsTabla(tablaProcesos, procesos);

        tableManager.actualizarItemsTabla(tablaAreasLibres,
                os.getMemoryHandler().ordenarCeldasMemoriaPorInicio(ram.getAreasLibres()));

        tableManager.actualizarItemsTabla(tablaParticiones,
                os.getMemoryHandler().ordenarCeldasMemoriaPorInicio(ram.getParticiones()));
    }

    @Override
    public void update(Observable o, Object arg)
    {
        actualizarEstado(arg.toString());
    }

    private void actualizarGrafico()
    {
        Platform.runLater(() ->
        {
            grafico.refrescarGrafico();
            grafico.dibujarRepresentacionGrafica(ram.getAreasLibres(), ram.getParticiones(), ram.getFragmentos());
        });
    }

    private void actualizarEstado(String mensaje)
    {
        Platform.runLater(() ->
        {
            estado.setText(mensaje);
        });
    }

    private void actualizarBtnMomentos()
    {
        Platform.runLater(() ->
        {
            btnSigPaso.setText(os.getMomento() != OS.MOMENTO_FINAL ? "Paso: " + os.getMomento() : "Finalizada");
        });
    }

    private boolean mostrarConfirmacion(String title, String text)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

}
