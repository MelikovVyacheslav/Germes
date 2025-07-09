
package org.slavik.service;

import com.jcraft.jsch.SftpException;
import org.slavik.dioritB2B.DioritAPIClientImpl;
import org.slavik.dioritB2B.model.DioritProduct;
import org.slavik.dioritB2B.model.ShortProduct;
import org.slavik.connector.JschSftpClient;
import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.*;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.JdbcProductAttributeRepository;
import org.slavik.repository.product.JdbcProductDescriptionRepository;
import org.slavik.repository.product.JdbcProductRepository;
import org.slavik.repository.product.JdbcProductToCategoryRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategory;
    private final JdbcManufacturerRepository jdbcManufacturerRepository;
    private final JdbcAttributeRepository jdbcAttributeRepository;
    private final JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository;
    private final JdbcProductAttributeRepository jdbcProductAttributeRepository;
    private final JschSftpClient jschSftpClient;

    public DioritProductService(DioritAPIClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository,
                                JdbcProductToCategoryRepository jdbcProductToCategory,
                                JdbcManufacturerRepository jdbcManufacturerRepository,
                                JdbcAttributeRepository jdbcAttributeRepository,
                                JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository,
                                JdbcProductAttributeRepository jdbcProductAttributeRepository, JschSftpClient jschSftpClient) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategory = jdbcProductToCategory;
        this.jdbcManufacturerRepository = jdbcManufacturerRepository;
        this.jdbcAttributeRepository = jdbcAttributeRepository;
        this.jdbcAttributeDescriptionRepository = jdbcAttributeDescriptionRepository;
        this.jdbcProductAttributeRepository = jdbcProductAttributeRepository;
        this.jschSftpClient = jschSftpClient;
    }

    private final Date CURRENT_DATE = new Date(System.currentTimeMillis());
    private final String EAN_VALUE = "dioritb2b";
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 1;
    private final int DN_ID = 0;
    private final int STOCK_STATUS = 7;
    private final int LANGUAGE_ID = 1;

    @Override
    public void sync() throws SftpException {
        List<ShortProduct> allProductAPI = apiClient.getAllProduct();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        int productId = 0;
        for (ShortProduct productAPI : allProductAPI) {
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getName().equals(productDescription.getName())) {
                    productId = productDescription.getProductId();
                    isThereProduct = true;
                    break;
                }
            }
            if (isThereProduct) {
                jdbcProductRepository.update(createProduct(productId, productAPI));
                System.out.println("Update " + productId);
            } else {
                Product product = jdbcProductRepository.create(createProduct(0, productAPI));
                jdbcProductDescriptionRepository.create(new ProductDescription(
                        product.getProductId(),
                        productAPI.getName(),
                        productAPI.getDescription()
                ));
                jdbcProductToCategory.create(new ProductToCategory(
                        product.getProductId(),
                        2012
                ));
                createAttribute(productAPI, product.getProductId());
                addingAllPhotos(productAPI);
                System.out.println("Create " + product.getProductId());
            }
        }
    }

    private Product createProduct(int productId, ShortProduct product) {
        return new Product(
                productId,
                creatorOfSkuNumbers(product.getSku()),
                creatorOfSkuNumbers(product.getSku()),
                EAN_VALUE,
                product.getStock(),
                STOCK_STATUS,
                product.extractLastPartFromUrl(product.getPhotos().get(0)),
                brandProductRatio(product.getID()),
                product.getPrice(),
                CURRENT_DATE,
                product.getWeight(),
                WEIGHT_CLASS_ID,
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                LENGTH_CLASS_ID,
                product.getStock(),
                STATUS_VALUE,
                CURRENT_DATE,
                CURRENT_DATE,
                DN_ID
        );
    }

    private void addingAllPhotos(ShortProduct product) throws SftpException {
        jschSftpClient.isConnected();
        for (String photo : product.getPhotos()) {
            jschSftpClient.downloadFile(product.extractLastPartFromUrl(photo), "sftp://u3045843@80.78.252.245/var/www/u3045843/data/www/germes.vip/image/catalog/b2b");
        }
    }

    private int brandProductRatio(UUID productId) {
        DioritProduct product = apiClient.viewProduct(productId);
        Manufacturer manufacturer = jdbcManufacturerRepository.find(product.getBrand().getName());
        if (manufacturer == null) {
            manufacturer = jdbcManufacturerRepository.create(new Manufacturer(
                    0,
                    product.getBrand().getName()
            ));
        }
        return manufacturer.getManufacturerId();
    }

    private void createAttribute(ShortProduct product, int productId) {
        Map<String, Object> attributeMap = product.extractAttributesExceptGroup(product);
        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
            List<AttributeDescription> attributeDescriptions =
                    jdbcAttributeDescriptionRepository.findByName(entry.getKey());

            if (!attributeDescriptions.isEmpty()) {
                AttributeDescription attributeDesc = attributeDescriptions.get(0);
                jdbcProductAttributeRepository.create(new ProductAttribute(
                        productId,
                        attributeDesc.getAttributeId(),
                        LANGUAGE_ID,
                        entry.getValue().toString()
                ));
            } else {
                Attribute newAttribute = jdbcAttributeRepository.create(new Attribute(
                        0,
                        1,
                        0
                ));
                AttributeDescription newAttributeDescription =
                        jdbcAttributeDescriptionRepository.create(new AttributeDescription(
                                newAttribute.getAttributeId(),
                                LANGUAGE_ID,
                                entry.getKey()
                        ));

                jdbcProductAttributeRepository.create(new ProductAttribute(
                        productId,
                        newAttributeDescription.getAttributeId(),
                        LANGUAGE_ID,
                        entry.getValue().toString()
                ));
            }
        }
    }

    private String creatorOfSkuNumbers(String sku) {
        return sku + "30";
    }
}
