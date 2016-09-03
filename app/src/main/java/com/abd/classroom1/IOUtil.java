package com.abd.classroom1;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by Abd on 3/9/2016.
 */
public class IOUtil {


    public static byte[] getByteArrayFromImage(String filePath) throws FileNotFoundException, IOException {
        Log.d("Info", "error");
        String decodedPath = URLDecoder.decode(filePath);
        Log.d("Info File Path",decodedPath );
        File file = new File(decodedPath);
        System.out.println(file.exists() + "!!");

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset
                off to this byte array output stream.*/
                System.out.println("read " + readNum + " bytes,");
            }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }
}
