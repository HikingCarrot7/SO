package com.sw.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author nicol
 */
public class DAO
{

    public static String RUTA = "data.txt";
    public static String SALTO_LINEA = "\r\n";
    private File file;

    public DAO()
    {
        file = new File(RUTA);

        if (!file.exists())
            try
            {
                file.createNewFile();

            } catch (IOException ex)
            {
                System.out.println(ex.getMessage());
            }
    }

    public void saveData(Object[][] data)
    {
        String result = "";

        try (Formatter out = new Formatter(file))
        {
            for (Object[] row : data)
            {
                for (Object col : row)
                    result += col.toString() + ",";

                result += SALTO_LINEA;
            }

            out.format("%s", result);

        } catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public Object[][] getSavedData()
    {
        Object[][] data = new Object[numeroLineas()][];

        try
        {
            Scanner in = new Scanner(file);

            for (int i = 0; in.hasNext(); i++)
            {
                String[] row = in.nextLine().split(",");
                data[i] = new Object[row.length];
                System.arraycopy(row, 0, data[i], 0, row.length);
            }

        } catch (FileNotFoundException | NoSuchElementException ex)
        {
            System.out.println(ex.getMessage());
        }

        return data.length != 0 ? data : null;
    }

    public void removeAllSavedData()
    {
        try (Formatter out = new Formatter(new FileWriter(file, false)))
        {
            out.format("%s", "");

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private int numeroLineas()
    {
        try
        {
            return (int) Files.lines(Paths.get(RUTA)).count();

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        return 0;
    }

    public boolean existSavedData()
    {
        try
        {
            return new Scanner(file).hasNext();

        } catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        return false;
    }

}
