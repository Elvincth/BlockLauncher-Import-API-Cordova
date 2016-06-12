package io.appery.project409456;

import android.content.Context;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AutoUpdateHelper {

    public static final String ZIP_BUNDLE_NAME = "bundle.zip";

    public static final String DATA_DIR = Environment.getDataDirectory() + "/data/";

    public static final void downloadBundle(InputStream is, String saveFilePath)throws IOException {
        checkDir(saveFilePath);
        File file = new File(saveFilePath, ZIP_BUNDLE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file, false);

        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        is.close();
    }


    public static final String unzip(String zipPath, String destPath) throws IOException {
        File zipFile = new File(zipPath, ZIP_BUNDLE_NAME);
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zis = new ZipInputStream(fis);

        ZipEntry zipEntry = null;

        while ((zipEntry = zis.getNextEntry()) != null) {
            String name = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                checkDir(name);
            } else {
                File fileOut = new File(destPath, name);
                checkDir(fileOut.getParent());
                FileOutputStream fout = new FileOutputStream(fileOut);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, len1);
                }

                zis.closeEntry();
                closeStream(fout);
            }
        }
        closeStream(zis);
        zipFile.delete();

        return destPath;
    }

    public static void checkDir(String dirPath) {
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static final void closeStream(Closeable stream) {
        if (stream != null)
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void copyFileOrDir(Context ctx, String path) throws IOException {
        String assets[] = ctx.getApplicationContext().getAssets().list(path);
        if (assets.length == 0) {
            copyFile(ctx, path);
        } else {
            StringBuilder fullPath = new StringBuilder();
            fullPath.append(DATA_DIR).append(ctx.getPackageName()).append("/").append(path);
            File dir = new File(fullPath.toString());
            if (!dir.exists())
                dir.mkdir();
            for (int i = 0; i < assets.length; ++i) {
                copyFileOrDir(ctx, path + "/" + assets[i]);
            }
        }
    }

    private static void copyFile(Context ctx, String filename) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        in = ctx.getApplicationContext().getAssets().open(filename);
        StringBuilder newFileName = new StringBuilder();
        newFileName.append(DATA_DIR).append(ctx.getPackageName()).append("/").append(filename);
        out = new FileOutputStream(newFileName.toString());

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    }

}
