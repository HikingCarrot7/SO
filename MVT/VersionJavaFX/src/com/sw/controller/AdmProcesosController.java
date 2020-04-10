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

import com.sw.model.Proceso;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SonBear
 */
public class AdmProcesosController implements Initializable, Controller<ObservableList<Proceso>>
{

    @FXML
    private TableView<Proceso> tablaProcesos;
    @FXML
    private TableColumn<Proceso, Button> colEliminar;
    @FXML
    private TextField inputNombreProceso;
    @FXML
    private TextField inputTamProceso;
    @FXML
    private TextField inputLlegadaProceso;
    @FXML
    private TextField inputDuracion;
    @FXML
    private Button btnAnadirProceso;
    @FXML
    private Button btnModificarProceso;

    private TableManager tableManager;
    private Proceso procesoSeleccionado;
    private Controller<Object> controllerPadre;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initMyComponents();
        initTableEvents();
    }

    @Override
    public void setDefaultData(ObservableList<Proceso> data, Controller<Object> controllerPadre)
    {
        this.controllerPadre = controllerPadre;
        tablaProcesos.setItems(data);
    }

    @Override
    public void initStage(Stage myStage)
    {
        myStage.setOnHiding(e ->
        {
            controllerPadre.setDefaultData(getProcesos(), null);
        });
    }

    private void initMyComponents()
    {
        tableManager = TableManager.getInstance();
        tableManager.initTablaProcesos(tablaProcesos);

        colEliminar.setCellFactory(ActionButtonTableCell.<Proceso>forTableColumn("Eliminar", this::eliminarProceso));

        inputTamProceso.setTextFormatter(new TextFormatter<>(new MyNumberStringConverter()));
        inputLlegadaProceso.setTextFormatter(new TextFormatter<>(inputTamProceso.getTextFormatter().getValueConverter()));
        inputDuracion.setTextFormatter(new TextFormatter<>(inputTamProceso.getTextFormatter().getValueConverter()));
    }

    private void initTableEvents()
    {
        tablaProcesos.setOnMouseClicked(e ->
        {
            Proceso proceso = tablaProcesos.getSelectionModel().getSelectedItem();

            if (proceso != null)
                rellenarCampos(proceso);
        });

        tablaProcesos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, proceso) ->
        {
            if (proceso != null)
                rellenarCampos(proceso);

            procesoSeleccionado = tablaProcesos.getSelectionModel().getSelectedItem();
            activarBtnModificarProceso();
        });

        tablaProcesos.addEventFilter(KeyEvent.KEY_PRESSED, e ->
        {
            if (e.getCode() == KeyCode.ESCAPE)
            {
                tablaProcesos.getSelectionModel().clearSelection();
                limpiarCampos();
                desactivarBtnModificarProceso();
            }
        });
    }

    @FXML
    private void accionBtnAnadirProceso(ActionEvent e)
    {
        if (entradasNumericasValidas())
            if (nombreProcesoValido(getNombreProceso()) && !nombreProcesoRepetido(getNombreProceso()))
                anadirNuevoProceso(getNuevoProceso());

            else if (mostrarConfirmacion("El nombre del proceso no es válido.",
                    "El nombre del proceso se reemplazará por: P" + (getProcesos().size() + 1)))
            {
                inputNombreProceso.setText("P" + (getProcesos().size() + 1));
                anadirNuevoProceso(getNuevoProceso());
            }
    }

    @FXML
    private void accionBtnModificarProceso(ActionEvent e)
    {
        if (entradasNumericasValidas())
            if (nombreProcesoValido(getNombreProceso())
                    && !nombreProcesoRepetido(getNombreProceso(), procesoSeleccionado))
                modificarProceso(procesoSeleccionado);

            else if (mostrarConfirmacion("El nombre del proceso no es válido.",
                    "El nombre del proceso se reemplazará por: P" + (getProcesos().size() + 1)))
            {
                inputNombreProceso.setText("P" + (getProcesos().size() + 1));
                modificarProceso(procesoSeleccionado);
            }
    }

    private void anadirNuevoProceso(Proceso proceso)
    {
        getProcesos().add(proceso);
        tableManager.seleccionarUltimaFila(tablaProcesos);
    }

    private void modificarProceso(Proceso proceso)
    {
        proceso.setNombre(getNombreProceso());
        proceso.setSize(getTamanioProceso());
        proceso.setLlegada(getTiempoLlegadaProceso());
        proceso.setDuracion(getDuracionProceso());

        tableManager.actualizarTabla(tablaProcesos);
    }

    private Proceso getNuevoProceso()
    {
        return new Proceso(getNombreProceso(), getTamanioProceso(), getTiempoLlegadaProceso(), getDuracionProceso());
    }

    private boolean nombreProcesoValido(String nombre)
    {
        return !nombre.trim().equals("");
    }

    private boolean nombreProcesoRepetido(String nombre)
    {
        return getProcesos().stream().anyMatch(proceso -> proceso.getNombre().equals(nombre.trim()));
    }

    private boolean nombreProcesoRepetido(String nombre, Proceso procesoAIgnorar)
    {
        return getProcesos().stream().anyMatch(proceso -> proceso != procesoAIgnorar && proceso.getNombre().equals(nombre.trim()));
    }

    private boolean entradasNumericasValidas()
    {
        try
        {
            getTamanioProceso();
            getTiempoLlegadaProceso();
            getDuracionProceso();

        } catch (NumberFormatException e)
        {
            mostrarError("Compos incorrectos", "Algún campo es incorrecto.");
            return false;
        }

        return true;
    }

    private String getNombreProceso()
    {
        return inputNombreProceso.getText();
    }

    private int getTamanioProceso() throws NumberFormatException
    {
        return Integer.parseInt(inputTamProceso.getText());
    }

    private int getTiempoLlegadaProceso() throws NumberFormatException
    {
        return Integer.parseInt(inputLlegadaProceso.getText());
    }

    private int getDuracionProceso() throws NumberFormatException
    {
        return Integer.parseInt(inputDuracion.getText());
    }

    private void rellenarCampos(Proceso proceso)
    {
        inputNombreProceso.setText(proceso.getNombre());
        inputTamProceso.setText(String.valueOf(proceso.getSize()));
        inputLlegadaProceso.setText(String.valueOf(proceso.getTiempoLlegada()));
        inputDuracion.setText(String.valueOf(proceso.getDuracion()));
    }

    private void limpiarCampos()
    {
        inputNombreProceso.setText("");
        inputTamProceso.setText("");
        inputLlegadaProceso.setText("");
        inputDuracion.setText("");
    }

    private void eliminarProceso(Proceso p)
    {
        tablaProcesos.getItems().remove(p);

        if (tablaProcesos.getItems().isEmpty())
        {
            limpiarCampos();
            desactivarBtnModificarProceso();
        }
    }

    private void mostrarError(String title, String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText(null);
        alert.showAndWait();
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

    private void activarBtnModificarProceso()
    {
        btnModificarProceso.disableProperty().set(false);
    }

    private void desactivarBtnModificarProceso()
    {
        btnModificarProceso.disableProperty().set(true);
    }

    private ObservableList<Proceso> getProcesos()
    {
        return tablaProcesos.getItems();
    }

}
