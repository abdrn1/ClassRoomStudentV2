package com.abd.classroom1;

import android.util.Log;

import com.esotericsoftware.kryonet.Client;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by Abd on 3/10/2016.
 */
public class FileSenderThreadV2 extends Thread {
    RandomAccessFile aFile = null;
    Client client;
    long chunkcounter = 0L;
    int bufferSize = 2000;
    String path;
    FileChunkMessageV2 imMessage = null;

    public FileSenderThreadV2(Client client, String currFilePath, FileChunkMessageV2 imMessage) throws FileNotFoundException {
        this.client = client;
        this.imMessage = imMessage;
        this.imMessage = imMessage;
        path = currFilePath;
        //aFile = new RandomAccessFile();
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    @Override
    public void run() {
        Log.d("INFO", "Helllllloooooo");
        try {
            aFile = new RandomAccessFile(path, "rw");
            Log.d("INFO", "RANDOM FILE Created");
            chunkcounter = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[bufferSize];

            Log.d("INFO", "For Start Here");
            for (int readNum; (readNum = aFile.read(buf)) != -1; ) {
                Log.d("INFO readNum", Integer.toString(readNum));
                chunkcounter++;
                //bos = new ByteArrayOutputStream();
                bos.reset();
                bos.write(buf, 0, readNum);
                byte[] currentchunk = bos.toByteArray();
                Log.d("INFO ArrayLength", Integer.toString(currentchunk.length));
                ///bos = new ByteArrayOutputStream();
                FileChunkMessageV2 chunkFromFile = new FileChunkMessageV2();
                chunkFromFile.setSenderName(imMessage.getSenderName());
                chunkFromFile.setSenderID(imMessage.getSenderID());
                chunkFromFile.setChunkCounter(chunkcounter);
                chunkFromFile.setChunk(currentchunk);
                chunkFromFile.setFileName(imMessage.getFileName());
                chunkFromFile.setFiletype(imMessage.getFiletype());
                // send Recivers on the first packet only
                if (chunkcounter == 1L) {
                    chunkFromFile.setRecivers(imMessage.getRecivers());
                }
                Log.d("SEND", Long.toString(chunkcounter));

                // if the send buffer full wait untill some free buffer be available.
                while(client.getTcpWriteBufferSize()>8000){
                    sleep(50);
                }
                client.sendTCP(chunkFromFile);
            }
            FileChunkMessageV2 endofFile = new FileChunkMessageV2();
            endofFile.setSenderName(imMessage.getSenderName());
            Log.d("SEND", imMessage.getSenderName());
            endofFile.setSenderID(imMessage.getSenderID());
            Log.d("SEND", imMessage.getSenderID());
            endofFile.setChunkCounter((-1L));
            endofFile.setFileName(imMessage.getFileName());
            endofFile.setFiletype(imMessage.getFiletype());
            Log.d("SEND", imMessage.getFileName());
            client.sendTCP(endofFile);
            aFile.close();

            if (endofFile.getChunkCounter() == -1)
                Log.d("SEND", "END OF CHunks");

        } catch (Exception e) {
            Log.d("Error", "Helllllloooooo");
            e.printStackTrace();

        }
        Log.d("INFO", "All Done");

    }
}
