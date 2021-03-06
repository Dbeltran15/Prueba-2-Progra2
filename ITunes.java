
package PruebaS9;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class ITunes 
{
    File direccion = new File("C:\\Usuarios\\Hp\\Documentos\\Universidad\\2021\\Lab Programacion 2\\LabProgra2\\src\\PruebaS9\\JTunes");
    File codigos = new File(direccion.getPath() + "\\Codigos.itn");
    File songs = new File(direccion.getPath() + "\\Songs.itn");
    File downloads = new File(direccion.getPath() + "\\Downloads.itn");
    RandomAccessFile code, song, download;

    public ITunes() throws FileNotFoundException, IOException 
    {
        direccion.mkdir();
        code = new RandomAccessFile(codigos, "rw");
        song = new RandomAccessFile(songs, "rw");
        download = new RandomAccessFile(downloads, "rw");
        if (code.length() == 0) 
        {
            code.writeInt(1);
            code.writeInt(1);
        }
    }    
    
    public int getCodigo(long offset) throws IOException
    {
        code.seek(0);
        int cancion = code.readInt();
        int descarga = code.readInt();
        if (songs.length() != 0) 
        {
            if (offset == 0) 
            {
                return ++cancion;
            }
            else if(offset == 4)
            {
                return ++descarga;
            }
        }
        else if (songs.length() != 0 && download.length() == 0) 
        {
            if (offset == 0) 
            {
                return ++cancion;
            }
            else if(offset == 4)
            {
                return descarga;
            }
        }
        else
        {
            if (offset == 0) 
            {
                return cancion;
            }
            else if(offset == 4)
            {
                return descarga;
            }
        }
        return -1;
    }
    
    public void addSong(String nombre, String cantante, double precio) throws IOException
    {
        code.seek(0);
        int cancion = code.readInt();
        int descarga = code.readInt();
        if (song.length() != 0) 
        {
            song.seek(song.length());
            song.writeInt(++cancion);
            song.writeUTF(nombre);
            song.writeUTF(cantante);
            song.writeDouble(precio);
            song.writeInt(0);
            song.writeInt(0);
        }
        else
        {
            song.seek(song.length());
            song.writeInt(cancion);
            song.writeUTF(nombre);
            song.writeUTF(cantante);
            song.writeDouble(precio);
            song.writeInt(0);
            song.writeInt(0);
        }
        code.seek(0);
        code.writeInt(++cancion);
        code.writeInt(descarga);
    }
    
    public void reviewSong(int code, int stars) throws IOException
    {
        song.seek(0);
        while(song.getFilePointer() < song.length())
        {
            int c = song.readInt();
            String n = song.readUTF();
            String ca = song.readUTF();
            double p = song.readDouble();
            long posicion = song.getFilePointer();
            int s = song.readInt();
            int r = song.readInt();
            if (code == c) 
            {
                if (stars >= 0 && stars <= 5) 
                {
                    s += stars;
                    r = ++r;
                    song.seek(posicion);
                    song.writeInt(s);
                    song.writeInt(r);
                }
                else
                {
                    throw new InvalidRateException(r);
                }
            }
            else
            {
                System.out.println("C??digo de canci??n inv??lido.");
            }
        }
    }
    
    public void downloadSong(int codeSong, String cliente) throws IOException
    {
        code.seek(0);
        int cancion = code.readInt();
        int descarga = code.readInt();
        
        song.seek(0);
        while(song.getFilePointer() < song.length())
        {
            int c = song.readInt();
            String n = song.readUTF();
            String ca = song.readUTF();
            double p = song.readDouble();
            int s = song.readInt();
            int r = song.readInt();
            if (codeSong == c) 
            {
                download.seek(download.length());
                if (download.length() != 0) 
                {
                    download.writeInt(++descarga);
                    download.writeLong(Calendar.getInstance().getTimeInMillis());
                    download.writeInt(c);
                    download.writeUTF(cliente);
                    download.writeDouble(p);
                }
                else
                {
                    download.writeInt(descarga);
                    download.writeLong(Calendar.getInstance().getTimeInMillis());
                    download.writeInt(c);
                    download.writeUTF(cliente);
                    download.writeDouble(p);
                }
                code.seek(0);
                code.writeInt(cancion);
                code.writeInt(++descarga);
                return;
            }
            else
            {
                System.out.println("C??digo de canci??n inv??lido.");
            }
        }
    }
    
    public void songs(String txtFile) throws FileNotFoundException, IOException 
    {

        PrintWriter escritura = new PrintWriter(txtFile);
        escritura.print("");
        escritura.close();

        FileReader canciones = new FileReader(txtFile);
        
        while(songs.getFilePointer()<songs.length()) 
        {
            int c = song.readInt();
            String n = song.readUTF();
            String ca = song.readUTF();
            double p = song.readDouble();
            double s = song.readInt();
            int r = song.readInt();
            
            Double ratingfinal = s/r;

            System.out.println("");
            System.out.println("Codigo: " + c);
            System.out.println("Cantante: " + ca);
            System.out.println("Cantidad de reviews hechas: " + r);
            System.out.println("Precio: " + p);
            System.out.println("Rating: " + ratingfinal);
        }
    }
    public void infoSong(int codeSong) throws IOException
    {
        int des = 0;
        song.seek(0);
        download.seek(0);
        
        while(song.getFilePointer() < song.length())
        {
            int c = song.readInt();
            String n = song.readUTF();
            String ca = song.readUTF();
            double p = song.readDouble();
            int s = song.readInt();
            int r = song.readInt();
            if (codeSong == c) 
            {
                download.seek(0);
                while(download.getFilePointer() < download.length())
                {
                    int c2 = download.readInt();
                    long f = download.readLong();
                    int cS = download.readInt();
                    String cl = download.readUTF();
                    double pr = download.readDouble();
                    if (cS == codeSong) 
                    {
                        des++;
                    }
                }
                System.out.println("C??digo de la canci??n: " + c + " - Nombre:" + n + " - Cantante: " + ca  
                        + " - Precio: " + p + " - Estrellas recibidas: " + s + " - Cantidad de reviews recibidos: " + r + " - Rating: " + (s/r));
            }
        }
    }
}
