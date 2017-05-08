package org.anhcraft.aquawarp.Util;

import org.anhcraft.aquawarp.AquaWarp;
import org.anhcraft.aquawarp.AquaWarpsMessages;
import org.anhcraft.aquawarp.Options;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class FileManager {
    public FileManager FileManager(){
        return this;
    }

    public static void setup(){
        String[] a = Options.listGenerateFolders_018374;
        for(String f : a) {
            setFolders(Options.pluginDir + f);
        }
        String[] b = Options.listLoadFile_294008;
        for(String f : b) {
            setFileFromJar(f.split("~")[0], Options.pluginDir + f.split("~")[1]);
        }
    }

    public static Boolean setFolders(String path) {
        File a = new File(path);
        if(!a.exists()){
            a.mkdirs();
            return true;
        } else {
            return false;
        }
    }

    private static Boolean copyFileFromJar(InputStream in, File out) {
        InputStream fis = in;
        FileOutputStream fos = null;
        Boolean success = true;
        try {
            fos = new FileOutputStream(out);
            try {
                byte[] buf = new byte[1024];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Strings.sendSender(AquaWarpsMessages.get("System.Error.1001")
                        .replace("<fname>",out.getName()));
                success = false;
            } finally {
                if (fis != null) {
                    fis.close();
                } else {
                    success = false;
                }

                if (fos != null) {
                    fos.close();
                } else {
                    success = false;
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            Strings.sendSender(AquaWarpsMessages.get("System.Error.1002")
                    .replace("<fname>",out.getName()));
            success = false;
        }
        return success;
    }

    public static Boolean setFileFromJar(String jarpath, String pluginspath){
        File f = new File(pluginspath);
        Boolean b = true;
        if(!f.exists()){
            InputStream stream = AquaWarp.plugin.getClass().getResourceAsStream(jarpath);
            b = copyFileFromJar(stream, f);
        } else {
            b = false;
        }
        return b;
    }
}
