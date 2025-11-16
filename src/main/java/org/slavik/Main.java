package org.slavik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slavik.connector.JschSftpClient;
import org.slavik.connector.SftpClientProperties;
import org.slavik.service.BreezProductService;
import org.slavik.service.DioritProductService;
import org.slavik.service.OcsProductService;

import javax.sql.DataSource;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("=== Application started ===");

        ConnectionManager connectionManager = null;
        JschSftpClient jschSftpClient = null;

        try {
            log.info("Initializing database connection...");
            connectionManager = new ConnectionManager(
                    "jdbc:mysql://80.78.252.245:3310/u3045843_default?allowMultiQueries=true",
                    "u3045843_default",
                    "jAzDURqgdt3K940E"
            );

            log.info("Configuring SFTP client...");
            SftpClientProperties configuration = new SftpClientProperties(
                    "u3045843",
                    22,
                    "80.78.252.245",
                    1000000000,
                    "3AyozD417ZU7HjwB"
            );
            jschSftpClient = new JschSftpClient(configuration);

            DataSource dataSource = connectionManager.createDataSource();
            CreateProductService productService = new CreateProductService(dataSource, jschSftpClient, configuration);

            log.info("Starting Diorit sync...");
            DioritProductService dioritProductService = productService.createDioritProductService();
            dioritProductService.sync();
            log.info("Diorit sync successful.");

            log.info("Starting Breez sync...");
            BreezProductService breezProductService = productService.createBreezProductService();
            breezProductService.sync();
            log.info("Breez sync successful.");

            log.info("Starting OCS sync...");
            OcsProductService ocsProductService = productService.createOCSProductService();
            ocsProductService.sync();
            log.info("OCS sync successful.");

        } catch (Exception e) {
            log.error("Error during execution: ", e);
        } finally {
            log.info("Cleaning up resources...");
            try {
                if (jschSftpClient != null) {
                    jschSftpClient.disconnect();
                    log.info("SFTP client disconnected.");
                }
            } catch (Exception ex) {
                log.warn("Failed to disconnect SFTP client: ", ex);
            }

            try {
                if (connectionManager != null) {
                    connectionManager.disconnect();
                    log.info("Database connection closed.");
                }
            } catch (Exception ex) {
                log.warn("Failed to disconnect database: ", ex);
            }

            log.info("=== Application finished ===");
        }
    }
}
