package org.slavik.connector;

import com.jcraft.jsch.SftpException;

import java.io.InputStream;

public interface SftpClient {

    InputStream downloadFile(String fileName) throws Exception;
    InputStream downloadFile(String fileName, String locationPath) throws SftpException;
    void uploadFile(InputStream file, String locationPath) throws SftpException;
    void uploadFile(InputStream file, String locationPath, String fileName) throws SftpException;
    boolean isConnected();
    void removeFile(String fileName, String locationPath) throws SftpException;
    void moveFile(String fileName, String locationPath, String destinationPath) throws SftpException;
    boolean isFileExist(String fileName);
    boolean isFileExist(String fileName, String locationPath);
    void disconnect();
    boolean validate();
}
