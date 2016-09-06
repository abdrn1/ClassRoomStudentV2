package com.abd.classroom1;

/**
 * Created by Abd on 3/10/2016.
 */
public class FileChunkMessageV2 {

    public  final static String FILE = "FILE";
    public final static String EXAM = "EXAM";
    public  final static String CAPTURE = "CAPTURE";

    byte[] chunk; // chunk of bytes from the file;
    String senderID;
    String senderName;
    String[] recivers;
    String fileName;
    String filetype;
    long chunkCounter = 0; // use it to know new file and end of file.

    public FileChunkMessageV2() {
    }

    public FileChunkMessageV2(String sename, String seid) {
        this.senderName = sename;
        this.senderID = seid;
    }

    public byte[] getChunk() {
        return chunk;
    }

    public void setChunk(byte[] chunk) {
        this.chunk = chunk;
    }

    public long getChunkCounter() {
        return chunkCounter;
    }

    public void setChunkCounter(long chunkCounter) {
        this.chunkCounter = chunkCounter;
    }

   /* public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }*/

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }





   /*  public boolean isNewfile() {
        return newfile;
    }

    public void setNewfile(boolean newfile) {
        this.newfile = newfile;
    }*/




    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String[] getRecivers() {
        return recivers;
    }

    public void setRecivers(String[] recivers) {
        this.recivers = recivers;
    }

   /* public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }*/
}
