package at.spengergasse.dp_backend.system;

import at.spengergasse.dp_backend.exceptions.ExeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Comparator;

//Classe per la gestione dei file
@Component
public class FileMangaer
{

    private final String dirBase;
    private final String dirTemp;
    private final String dirExportFile;

    public FileMangaer(@Value("${app.upload-dir}") String dirBase) {
        this.dirBase = dirBase;
        this.dirTemp = dirBase + "/TMP";
        this.dirExportFile = dirBase + "/Export";
    }

    public String saveExportFile(String nomeFile, Blob file) throws ExeException
    {
        try {
          return this.saveFile(dirTemp+"/"+nomeFile, file);
        }catch (ExeException e){
            throw e;
        }
    }


    /**
     *  Funzone per la creazione di un File
     * @param nomeFile  (Nome file mit excetion z.b. file.txt)
     * @param file (File in Blob)
     * @return   (Dir wo dss File ist)
     * @throws ExeException
     */
    public String saveFile(String nomeFile, Blob file) throws ExeException
    {
        String dirWhitNameFile = dirBase + "/" + nomeFile;
        try{
            File outputFile = new File(Path.of(dirWhitNameFile).toString());
            try (InputStream inputStream = file.getBinaryStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                throw new ExeException(e);
            } catch (SQLException e) {
                throw new ExeException(e);
            } catch (IOException e) {
                throw new ExeException(e);
            }catch (Throwable e){
                throw new ExeException(e);
            }
        }catch (Exception e){
            throw new ExeException(e);
        }

        return dirWhitNameFile;
    }

//###########################     PRIVATE   #############################

    /**
     * Create TMP dir if not existe
     * @throws ExeException
     */
    private void createDirTemp() throws ExeException
    {
        try {
            if (!Files.exists(Path.of(dirTemp))) {
                Files.createDirectories(Path.of(dirTemp));
            }
        } catch (IOException e) {
            throw new ExeException(e);
        } catch (Exception e) {
            throw new ExeException(e);
        }
    }//createDirTemo


    /**
     * Delete all Files in TMP
     * @throws ExeException
     */
    private void deleteTemp() throws ExeException
    {
        try{
            this.createDirTemp();
            Files.walk(Path.of(dirTemp))
                    .sorted(Comparator.reverseOrder()) // importante: elimina prima i file, poi le cartelle
                    .filter(p -> !p.equals(Path.of(dirTemp))) // non cancellare la cartella principale
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            System.out.println("Contenuto cancellato.");
        } catch (IOException e) {
            throw new ExeException(e);
        } catch (Exception e) {
            throw new ExeException(e);
        }
    }//deleteTemp

}//end Class
