package com.example.myapplication.ui.notifications;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    public void createZipArchive(String[] files, String zipFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

            byte[] buffer = new byte[1024];

            for (String filePath : files) {
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);

                zos.putNextEntry(new ZipEntry(filePath));

                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }

                bis.close();
                fis.close();
                zos.closeEntry();
            }

            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
