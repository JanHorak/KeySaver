/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.manager.SettingManager;

/**
 *
 * @author janhorak
 */
public class Utilities {

    public static String getHash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            LoggingManager.writeToErrorFile("Utilities - getHash(): ", ex);
        }
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void generateZip(String pathOfZip, List<String> filePathes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathOfZip);
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("Utilities: generateZip- failed:", ex);
        }
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (String filePath : filePathes) {
            File file = new File(filePath);
            ZipEntry ze = new ZipEntry(file.getName());
            try {
                zos.putNextEntry(ze);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Cant create Zip- Entry", ex);
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file.getAbsoluteFile());
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- File not found: " + file, ex);
            }
            byte[] buffer = new byte[2048];
            int len;
            try {
                while ((len = fis.read(buffer)) > 0) {
                    try {
                        zos.write(buffer, 0, len);
                    } catch (IOException ex) {
                        LoggingManager.writeToErrorFile("Utilities: generateZip- ZipOutputStream cannot write", ex);
                    }
                }
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- Problems with Buffer- Length", ex);
            }
            try {
                zos.closeEntry();
                fis.close();
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- Cannot close Streams (SubStreams):", ex);
            }

        }
        try {
            zos.close();
            fos.close();
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("Utilities: generateZip- Cannot close Streams (MainStreams):", ex);
        }
    }

    public static void decompressZip(File zipFile, String pathForUnzip) {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            File folder = new File(pathForUnzip);
            if (!folder.exists()) {
                folder.mkdir();
            }
            zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(pathForUnzip + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zis.closeEntry();
                zis.close();
            } catch (IOException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static List<String> getFilePathesFromFolder(String path2Folder) {
        List<String> fileList = new ArrayList<String>();
        File folder = new File(path2Folder);

        String[] fileArray = folder.list();

        for (String s : fileArray) {
            fileList.add(path2Folder + "/" + s);
        }
        return fileList;
    }

    public static List<File> returnFilesNotDefault(String pathOfPropertiesFile) {
        SettingManager sm = new SettingManager(pathOfPropertiesFile);
        List<String> resultTMP = new ArrayList<>();
        List<File> result = new ArrayList<>();
        try {
            resultTMP = sm.returnAllValues();
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("Utilities: returnFilesNotDefault fialed:", ex);
        }
        for (String s : resultTMP) {
            if (!s.startsWith("AppData/")) {
                result.add(new File(s));
            }
        }
        return result;
    }

    public static void copyFiles(List<String> filePathes, String targetPath, CopyOption option) {
        for (String s : filePathes) {
            try {
                File tmp = new File(s);
                System.out.println(new File(s).toPath() + "-> " + new File(targetPath + "/" + tmp.getName()).toPath());
                Files.copy(tmp.toPath(), new File(targetPath + "/" + tmp.getName()).toPath(), option);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Utilities: Failed to copy File: " + s, ex);
            }
        }
    }
    
    public static List<String> getPathesFromFileList(List<File> fileList) {
        List<String> resultList = new ArrayList<>();
        for (File f : fileList) {
            resultList.add(f.getAbsolutePath());
        }
        return resultList;
    }

    public static String getStringFromFile(String path) {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String sCurrentLine = "";

            while ((sCurrentLine = br.readLine()) != null) {
                result += sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
