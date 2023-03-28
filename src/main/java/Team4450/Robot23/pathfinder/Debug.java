package Team4450.Robot23.pathfinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Team4450.Robot23.pathfinder.math.Vertex;

public class Debug {
    
    public static final File DEBUG_FILE = new File("pfdebug.txt");
    public static FileWriter writer;

    public static void open()
    {
        try
        {
            DEBUG_FILE.createNewFile();
            writer = new FileWriter(DEBUG_FILE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void close()
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static <V extends Vertex<V>> void writeVertex(String prefix, V v, String postfix)
    {
        try
        {
            writer.write(prefix + v.toString() + postfix);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void writeString(String s)
    {
        try
        {
            writer.write(s);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
