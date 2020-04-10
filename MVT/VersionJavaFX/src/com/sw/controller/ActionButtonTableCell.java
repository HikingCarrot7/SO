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

import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author SonBear
 */
public class ActionButtonTableCell<S> extends TableCell<S, Button>
{

    private final Button actionButton;

    public ActionButtonTableCell(String label, Consumer<S> consumer)
    {
        actionButton = new Button(label);
        actionButton.setStyle("-fx-background-color: tomato;");
        initButtonEvents(consumer);
        actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    private void initButtonEvents(Consumer<S> consumer)
    {
        actionButton.setOnMouseEntered(e ->
        {
            actionButton.setStyle("-fx-background-color: red;");
        });

        actionButton.setOnMouseExited(e ->
        {
            actionButton.setStyle("-fx-background-color: tomato;");
        });

        actionButton.setOnAction(e ->
        {
            consumer.accept(getCurrentItem());
        });
    }

    public S getCurrentItem()
    {
        return getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String texto, Consumer<S> function)
    {
        return param -> new ActionButtonTableCell<>(texto, function);
    }

    @Override
    public void updateItem(Button item, boolean empty)
    {
        super.updateItem(item, empty);
        setGraphic(empty ? null : actionButton);
    }

}
