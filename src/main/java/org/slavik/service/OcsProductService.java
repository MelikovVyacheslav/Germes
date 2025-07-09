package org.slavik.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.slavik.ocs.OCSAPIClientImpl;
import org.slavik.ocs.model.*;
import org.slavik.entity.category.Category;
import org.slavik.entity.category.CategoryDescription;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.entity.product.ProductToCategory;
import org.slavik.repository.category.JdbcCategoryDescriptionRepository;
import org.slavik.repository.category.JdbcCategoryRepository;
import org.slavik.repository.product.JdbcProductDescriptionRepository;
import org.slavik.repository.product.JdbcProductRepository;
import org.slavik.repository.product.JdbcProductToCategoryRepository;

import java.sql.Date;
import java.util.List;

public class OcsProductService implements ProductService {
    private final OCSAPIClientImpl apiClient;
    private final JdbcCategoryRepository jdbcCategoryRepository;
    private final JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategoryRepository;

    private final int MANUFACTURER_ID = 1;
    private final Date CURRENT_DATE = new Date(System.currentTimeMillis());
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 6;
    private final int DN_ID = 0;

    public OcsProductService(OCSAPIClientImpl apiClient, JdbcProductDescriptionRepository descriptionRepo, JdbcProductRepository productRepo, JdbcProductToCategoryRepository productToCategory, JdbcCategoryRepository jdbcCategoryRepository, JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository1) {
        this.apiClient = apiClient;
        this.jdbcCategoryRepository = jdbcCategoryRepository;
        this.jdbcProductDescriptionRepository = descriptionRepo;
        this.jdbcProductRepository = productRepo;
        this.jdbcProductToCategoryRepository = productToCategory;
        this.jdbcCategoryDescriptionRepository = jdbcCategoryDescriptionRepository1;
    }


    public void sync() throws JsonProcessingException {
        List<Result> allProductAPI = apiClient.getAll();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        int productId = 0;
        for (Result productAPI : allProductAPI) {
            if (productAPI.getLocations() == null || productAPI.getLocations().isEmpty()) {
                continue;
            }
            Location location = productAPI.getLocations().getFirst();
            String description = location.getDescription();
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getProduct().getProductName().equals(productDescription.getName())) {
                    productId = productDescription.getProductId();
                    isThereProduct = true;
                    break;
                }
            }
            if (productAPI.getPrice() == null) {
                continue;
            }
            int price = (int) productAPI.getPrice().getPriceList().getValue();

            if (isThereProduct) {
                System.out.println("Update");
                jdbcProductRepository.update(new Product(
                        productId,
                        productAPI.getProduct().getProductKey(),
                        productAPI.getProduct().getProductKey(),
                        "OCS",
                        location.getQuantity().getValue(),
                        productAPI.getProduct().getStockStatus(description),
                        null,
                        MANUFACTURER_ID,
                        price,
                        CURRENT_DATE,
                        productAPI.getPackageInformation().getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getPackageInformation().getDepth(),
                        productAPI.getPackageInformation().getWidth(),
                        productAPI.getPackageInformation().getHeight(),
                        LENGTH_CLASS_ID,
                        0,
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductDescriptionRepository.update(new ProductDescription(
                        productId,
                        productAPI.getProduct().getProductName(),
                        productAPI.getProduct().getProductDescription()
                ));
                syncTableProductToCategory(productId, productAPI);
            } else {
                Product newProduct = jdbcProductRepository.create(new Product(
                        0,
                        productAPI.getProduct().getProductKey(),
                        productAPI.getProduct().getProductKey(),
                        "OCS",
                        location.getQuantity().getValue(),
                        productAPI.getProduct().getStockStatus(description),
                        null,
                        MANUFACTURER_ID,
                        price,
                        CURRENT_DATE,
                        productAPI.getPackageInformation().getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getPackageInformation().getDepth(),
                        productAPI.getPackageInformation().getWidth(),
                        productAPI.getPackageInformation().getHeight(),
                        LENGTH_CLASS_ID,
                        0,
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductDescriptionRepository.create(new ProductDescription(
                        newProduct.getProductId(),
                        productAPI.getProduct().getProductName(),
                        productAPI.getProduct().getProductDescription()
                ));
                syncTableProductToCategory(newProduct.getProductId(), productAPI);
            }
        }

    }

    public void syncTableProductToCategory(int productId, Result result) {
        String nameCategory = result.getProduct().getCatalogPath().get(0).getName();

        // 1. Проверяем, есть ли такая категория
        boolean isThereCategory = checkThereCategory(result.getProduct().getCatalogPath().get(0));

        // 2. Если категории нет — создаём
        if (!isThereCategory) {
            // создаём Category с временным id = 0 и родительским id = 1182
            Category newCategory = new Category(0, 1182, CURRENT_DATE, CURRENT_DATE);

            // создаём Category и получаем сгенерированный categoryId
            Category createdCategory = jdbcCategoryRepository.create(newCategory);

            // создаём описание для категории
            // создаём описание для категории
            CategoryDescription description = new CategoryDescription(
                    createdCategory.getCategoryId(),
                    nameCategory,  // name
                    nameCategory,  // description
                    nameCategory   // meta
            );
            jdbcCategoryDescriptionRepository.create(description);

        }

        // 3. Получаем все категории (уже включая новую)
        List<Category> categoryList = jdbcCategoryRepository.findAll();

        // 4. Ищем по имени и связываем с продуктом
        for (Category category : categoryList) {
            List<CategoryDescription> descriptions = jdbcCategoryDescriptionRepository.find(category.getCategoryId());

            for (CategoryDescription categoryDescription : descriptions) {
                if (categoryDescription.getDescription().equals(nameCategory)) {
                    List<ProductToCategory> productToCategories = jdbcProductToCategoryRepository.find(productId, category.getCategoryId());

                    if (productToCategories.isEmpty()) {
                        jdbcProductToCategoryRepository.create(
                                new ProductToCategory(productId, category.getCategoryId())
                        );
                    }


                    // Связь уже найдена, дальше идти не надо
                    break;
                }
            }
        }

    }

    private boolean checkThereCategory(CatalogPath catalogPath) {
        CategoryDescription categoryDescription = jdbcCategoryDescriptionRepository.findByName(catalogPath.getName());
        if (categoryDescription == null) {
            return false;
        } else {
            return true;
        }
    }
}
