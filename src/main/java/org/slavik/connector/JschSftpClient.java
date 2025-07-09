package org.slavik.connector;

import java.io.InputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.slavik.connector.exception.SftpLayerException;

public class JschSftpClient implements SftpClient {

    private final JSch jsch = new JSch();
    private final Session session;
    private final ChannelSftp channel;

    public JschSftpClient(SftpClientProperties configuration, String ppk) throws Exception {
        this.session = jsch.getSession(
                configuration.getUserName(),
                configuration.getHost(),
                configuration.getPort()
        );

        jsch.addIdentity("sftp", ppk.getBytes(), null, null);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.setTimeout(configuration.getTimeout());
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        this.channel = channel;
        this.channel.connect();
    }

    @Override
    public InputStream downloadFile(String fileName) throws Exception {
        return downloadFile(fileName, "");
    }

    @Override
    public InputStream downloadFile(String fileName, String locationPath) throws SftpException {
        if (fileName.isBlank())
            throw new SftpLayerException("Filename is empty");
        return channel.get(locationPath + "/" + fileName);
    }

    @Override
    public void uploadFile(InputStream file, String locationPath, String fileName) throws SftpException {
        channel.put(file, locationPath + "/" + fileName);
    }

    @Override
    public void uploadFile(InputStream file, String locationPath) throws SftpException {
        channel.put(file, locationPath);
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public void removeFile(String fileName, String locationPath) throws SftpException {
        channel.rm(locationPath + "/" + fileName);
    }

    @Override
    public void moveFile(String fileName, String locationPath, String destinationPath) throws SftpException {
        channel.rename(locationPath + "/" + fileName, destinationPath + "/" + fileName);
    }

    @Override
    public boolean isFileExist(String fileName) {
        return isFileExist(fileName, "/");
    }

    @Override
    public boolean isFileExist(String fileName, String locationPath) {
        final LsEntry[] file = new LsEntry[1];
        try {
            channel.ls(locationPath, new ChannelSftp.LsEntrySelector() {
                @Override
                public int select(ChannelSftp.LsEntry entry) {
                    if (entry.getFilename().equals(fileName) && entry.getAttrs().isReg()) {
                        file[0] = entry;
                        return BREAK;
                    }
                    return CONTINUE;
                }
            });
            return file[0] != null;
        } catch (SftpException e) {
            return false;
        }
    }

    @Override
    public void disconnect() {
        channel.exit();
        session.disconnect();
    }

    @Override
    public boolean validate() {
        try {
            channel.cd(".");
            return true;
        } catch (SftpException e) {
            return false;
        }
    }
}
