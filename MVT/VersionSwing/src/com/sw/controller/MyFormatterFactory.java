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
package com.sw.controller;

import java.text.ParseException;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Eusebio Ajax
 */
public class MyFormatterFactory extends JFormattedTextField.AbstractFormatterFactory
{

    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf)
    {
        return new JFormattedTextField.AbstractFormatter()
        {
            @Override
            public Object stringToValue(String text) throws ParseException
            {
                try
                {
                    int number = Integer.parseInt(text);

                    if (number <= 0)
                        throw new ParseException(text, 0);

                    return number;

                } catch (NumberFormatException e)
                {
                    throw new ParseException(text, 0);
                }
            }

            @Override
            public String valueToString(Object value) throws ParseException
            {
                return String.valueOf(value == null ? "" : value);
            }
        };
    }

}
