package org.slavik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public interface ProductService {
    void sync() throws IOException, SftpException;
}