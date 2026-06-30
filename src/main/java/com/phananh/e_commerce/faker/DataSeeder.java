package com.phananh.e_commerce.faker;

import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductAttribute;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class DataSeeder implements ApplicationRunner {

    private final FakerService fakerService;
    private final FakerConfigService props;

    @Override
    public void run(ApplicationArguments args) {
        if (!props.isEnabled()) {
            log.info("[DataSeeder] Faker seeding is disabled by properties");
            return;
        }

        log.info("=== [DataSeeder] Bắt đầu seed fake data ===");


        List<User> users = fakerService.seedUsers(props.getUsers());
        log.info("[DataSeeder] Users: {}", users.size());

        List<Brand> brands = fakerService.seedBrands(props.getBrands());
        log.info("[DataSeeder] Brands: {}", brands.size());

        List<Category> categories = fakerService.seedCategories(props.getCategories());
        log.info("[DataSeeder] Categories: {}", categories.size());

        Map<ProductAttribute, List<AttributeValue>> attrMap = fakerService.seedAttributes();
        log.info("[DataSeeder] Attributes: {}", attrMap.size());

        List<Product> products = fakerService.seedProducts(props.getProducts(), categories, brands, attrMap);
        log.info("[DataSeeder] Products: {}", products.size());

        int orderCount = fakerService.seedOrders(props.getOrders(), users);
        log.info("[DataSeeder] Orders: {}", orderCount);

        log.info("=== [DataSeeder] Seed hoàn tất ===");
    }
}
