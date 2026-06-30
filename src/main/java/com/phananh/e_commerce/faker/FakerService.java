package com.phananh.e_commerce.faker;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.order.domain.model.enums.PaymentMethod;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataOrderItemRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataOrderRepository;
import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductAttribute;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataAttributeValueRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductAttributeRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductVariantRepository;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataBrandRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataCategoryRepository;
import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.UserInfo;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FakerService {

    private final SpringDataUserRepository userRepository;
    private final SpringDataRoleRepository roleRepository;
    private final SpringDataBrandRepository brandRepository;
    private final SpringDataCategoryRepository categoryRepository;
    private final SpringDataProductRepository productRepository;
    private final SpringDataProductVariantRepository variantRepository;
    private final SpringDataProductAttributeRepository attributeRepository;
    private final SpringDataAttributeValueRepository attributeValueRepository;
    private final SpringDataOrderRepository orderRepository;
    private final SpringDataOrderItemRepository orderItemRepository;

    private final Faker faker = new Faker(Locale.forLanguageTag("vi"));


    // ── USERS ──────────────────────────────────────────────────────────────────

    @Transactional
    public List<User> seedUsers(int count) {
        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Seed roles first"));

        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String username = "user_" + i;
            String phone = "09" + faker.number().digits(8);
            String email = faker.internet().emailAddress();

            if (userRepository.existsByCredentialsUsername(username)) continue;
            if (userRepository.existsByInfoEmail(email)) continue;

            User user = new User();
            setField(user, "credentials", new UserCredentials(username, "123456", true));
            setField(user, "info", UserInfo.builder()
                    .fullName(faker.name().fullName())
                    .email(email)
                    .address(faker.address().fullAddress())
                    .phoneNumber(phone)
                    .build());
            setField(user, "roles", new HashSet<>(Set.of(customerRole)));
            users.add(userRepository.save(user));
        }
        return users;
    }

    // ── BRANDS ─────────────────────────────────────────────────────────────────

    @Transactional
    public List<Brand> seedBrands(int count) {
        List<Brand> brands = new ArrayList<>();
        Set<String> names = new HashSet<>();
        for (int i = 0; i < count; i++) {
            String name = faker.company().name();
            if (names.contains(name) || brandRepository.existsByNameIgnoreCase(name)) continue;
            names.add(name);
            brands.add(brandRepository.save(Brand.create(name, faker.lorem().sentence(), null)));
        }
        return brands;
    }

    // ── CATEGORIES ─────────────────────────────────────────────────────────────

    @Transactional
    public List<Category> seedCategories(int count) {
        List<Category> categories = new ArrayList<>();
        Set<String> names = new HashSet<>();
        String[] predefined = {
                "Điện thoại", "Laptop", "Máy tính bảng", "Phụ kiện", "Đồng hồ",
                "Thời trang nam", "Thời trang nữ", "Giày dép", "Túi xách", "Mỹ phẩm",
                "Thực phẩm", "Đồ gia dụng", "Thể thao", "Sách", "Đồ chơi"
        };
        for (int i = 0; i < Math.min(count, predefined.length); i++) {
            String name = predefined[i];
            if (names.contains(name) || categoryRepository.existsByNameIgnoreCase(name)) continue;
            names.add(name);
            categories.add(categoryRepository.save(Category.create(name, faker.lorem().sentence(), null)));
        }
        return categories;
    }

    // ── ATTRIBUTES ─────────────────────────────────────────────────────────────

    @Transactional
    public Map<ProductAttribute, List<AttributeValue>> seedAttributes() {
        Map<ProductAttribute, List<AttributeValue>> result = new LinkedHashMap<>();

        Map<String, String[]> attrMap = Map.of(
                "Màu sắc", new String[]{"Đỏ", "Xanh", "Đen", "Trắng", "Vàng", "Hồng"},
                "Kích thước", new String[]{"S", "M", "L", "XL", "XXL"},
                "Chất liệu", new String[]{"Cotton", "Polyester", "Linen", "Denim"}
        );

        for (Map.Entry<String, String[]> entry : attrMap.entrySet()) {
            ProductAttribute attr = attributeRepository.findByName(entry.getKey())
                    .orElseGet(() -> attributeRepository.save(
                            ProductAttribute.builder().name(entry.getKey()).build()));

            List<AttributeValue> values = new ArrayList<>();
            for (String v : entry.getValue()) {
                values.add(attributeValueRepository.save(AttributeValue.create(v, attr)));
            }
            result.put(attr, values);
        }
        return result;
    }

    // ── PRODUCTS ───────────────────────────────────────────────────────────────

    @Transactional
    public List<Product> seedProducts(int count, List<Category> categories, List<Brand> brands,
                                      Map<ProductAttribute, List<AttributeValue>> attrMap) {
        List<AttributeValue> colorValues = attrMap.values().stream().findFirst().orElse(List.of());
        List<AttributeValue> sizeValues = attrMap.values().stream().skip(1).findFirst().orElse(List.of());

        List<Product> products = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Long categoryId = categories.isEmpty() ? null : categories.get(random.nextInt(categories.size())).getId();
            Long brandId = brands.isEmpty() ? null : brands.get(random.nextInt(brands.size())).getId();

            Product product = Product.builder()
                    .name(faker.commerce().productName())
                    .description(faker.lorem().paragraph())
                    .avatarUrl(null)
                    .status(ProductStatus.ACTIVE)
                    .categoryId(categoryId)
                    .brandId(brandId)
                    .variants(new HashSet<>())
                    .build();
            product = productRepository.save(product);

            // 2 variants per product
            for (int v = 0; v < 2; v++) {
                String sku = "SKU-" + System.nanoTime() + "-" + v;
                BigDecimal price = BigDecimal.valueOf(faker.number().numberBetween(50000, 5000000))
                        .setScale(2, RoundingMode.HALF_UP);
                int stock = faker.number().numberBetween(0, 200);

                Set<AttributeValue> attrValues = new HashSet<>();
                if (!colorValues.isEmpty()) attrValues.add(colorValues.get(random.nextInt(colorValues.size())));
                if (!sizeValues.isEmpty()) attrValues.add(sizeValues.get(random.nextInt(sizeValues.size())));

                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .skuCode(sku)
                        .price(price)
                        .stockQuantity(stock)
                        .attributeValues(attrValues)
                        .images(new HashSet<>())
                        .build();
                variantRepository.save(variant);
            }
            products.add(product);
        }
        return products;
    }

    // ── ORDERS ─────────────────────────────────────────────────────────────────

    @Transactional
    public int seedOrders(int count, @NonNull List<User> users) {
        List<ProductVariant> variants = variantRepository.findAll();
        if (users.isEmpty() || variants.isEmpty()) return 0;

        Random random = new Random();
        OrderStatus[] statuses = OrderStatus.values();
        PaymentMethod[] methods = PaymentMethod.values();
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = users.get(random.nextInt(users.size()));
            boolean isPaid = random.nextBoolean();

            Order order = Order.builder()
                    .userId(user.getId())
                    .fullName(user.getInfo().fullName())
                    .phoneNumber(user.getInfo().phoneNumber())
                    .shippingAddress(faker.address().fullAddress())
                    .paymentMethod(methods[random.nextInt(methods.length)])
                    .status(statuses[random.nextInt(statuses.length)])
                    .isPaid(isPaid)
                    .paymentDate(null)
                    .shippingFee(BigDecimal.valueOf(faker.number().numberBetween(0, 50000)))
                    .totalPrice(BigDecimal.ZERO)
                    .orderItems(new HashSet<>())
                    .build();
            order = orderRepository.save(order);

            // 1-3 items per order
            int itemCount = faker.number().numberBetween(1, 4);
            BigDecimal total = BigDecimal.ZERO;
            for (int j = 0; j < itemCount; j++) {
                ProductVariant variant = variants.get(random.nextInt(variants.size()));
                int qty = faker.number().numberBetween(1, 5);
                BigDecimal price = variant.getPrice();

                OrderItem item = OrderItem.builder()
                        .order(order)
                        .variantId(variant.getId())
                        .quantity(qty)
                        .price(price)
                        .build();
                orderItemRepository.save(item);
                total = total.add(price.multiply(BigDecimal.valueOf(qty)));
            }
            order.updateTotalPrice(total.add(order.getShippingFee()));
            orderRepository.save(order);
            orders.add(order);
        }
        return orders.size();
    }

    // ── HELPER ─────────────────────────────────────────────────────────────────

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set field: " + fieldName, e);
        }
    }
}
