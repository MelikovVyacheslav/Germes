package org.slavik;

import org.slavik.connector.JschSftpClient;
import org.slavik.connector.SftpClientProperties;
import org.slavik.service.BreezProductService;
import org.slavik.service.DioritProductService;
import org.slavik.service.OcsProductService;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Exception {

        ConnectionManager connectionManager = new ConnectionManager(
                "jdbc:mysql://80.78.252.245:3310/u3045843_default?allowMultiQueries=true",
                "u3045843_default",
                "jAzDURqgdt3K940E"
        );

        SftpClientProperties configuration = new SftpClientProperties("u3045843", 22, "80.78.252.245", 1000000000, "3AyozD417ZU7HjwB");
        JschSftpClient jschSftpClient = new JschSftpClient(configuration);
        DataSource dataSource = connectionManager.createDataSource();
        CreateProductService productService = new CreateProductService(dataSource, jschSftpClient, configuration);

        DioritProductService dioritProductService = productService.createDioritProductService();
        dioritProductService.sync();
        System.out.println("diorit successful");

        BreezProductService breezProductService = productService.createBreezProductService();
        breezProductService.sync();
        System.out.println("breez successful");

        OcsProductService ocsProductService = productService.createOCSProductService();
        ocsProductService.sync();
        System.out.println("ocs successful");
        jschSftpClient.disconnect();
        connectionManager.disconnect();
    }
}