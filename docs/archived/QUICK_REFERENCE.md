# 📋 Quick Reference - Package Structure

## 🎯 Find It Quickly

### Product Catalog Context
```
📁 modules/productcatalog/
  ├── 📁 domain/model/
  │   └── Product, Category, Brand, ProductVariant, ProductAttribute, VariantImage
  ├── 📁 application/service/
  │   └── ProductService, CategoryService, BrandService
  ├── 📁 application/dto/request/
  │   └── CreateProductRequest, UpdateCategoryRequest, ...
  ├── 📁 application/dto/response/
  │   └── ProductResponse, CategoryResponse, BrandResponse, ...
  ├── 📁 application/mapper/
  │   └── ProductMapper, BrandMapper, CategoryMapper
  ├── 📁 infrastructure/persistence/repository/
  │   └── ProductRepository, CategoryRepository, BrandRepository
  └── 📁 presentation/controller/
      └── ProductController, CategoryController, BrandController
```

### Order Management Context
```
📁 modules/order/
  ├── 📁 domain/model/
  │   └── Order, OrderItem, CartItem
  ├── 📁 application/service/
  │   └── OrderService, CartItemService
  ├── 📁 application/dto/request/
  │   └── CreateOrderRequest, AddToCartRequest, ...
  ├── 📁 application/dto/response/
  │   └── OrderResponse, CartItemResponse, ...
  ├── 📁 application/mapper/
  │   └── OrderMapper, CartItemMapper
  ├── 📁 infrastructure/persistence/repository/
  │   └── OrderRepository, OrderItemRepository, CartItemRepository
  ├── 📁 infrastructure/persistence/specification/
  │   └── OrderSpecifications, ...
  └── 📁 presentation/controller/
      └── OrderController, CartController
```

### User Management Context
```
📁 modules/usermanagement/
  ├── 📁 domain/model/
  │   └── User, Role
  ├── 📁 application/service/
  │   └── UserService, RoleService
  ├── 📁 application/dto/request/
  │   └── RegisterUserRequest, UpdateProfileRequest, ...
  ├── 📁 application/dto/response/
  │   └── UserResponse, RoleResponse, ...
  ├── 📁 application/mapper/
  │   └── UserMapper, RoleMapper
  ├── 📁 infrastructure/persistence/repository/
  │   └── UserRepository, RoleRepository
  └── 📁 presentation/controller/
      └── UserController
```

### Authentication Context
```
📁 modules/authentication/
  ├── 📁 application/service/
  │   └── AuthenticationService
  ├── 📁 application/dto/request/
  │   └── LoginRequest, RefreshTokenRequest, ...
  ├── 📁 application/dto/response/
  │   └── LoginResponse, TokenResponse, ...
  ├── 📁 infrastructure/config/
  │   └── SecurityConfig, CustomJwtDecoder, JwtProvider, ...
  └── 📁 presentation/controller/
      └── AuthenticationController
```

### Dashboard Context
```
📁 modules/dashboard/
  ├── 📁 application/service/
  │   └── StatisticsService
  ├── 📁 application/dto/response/
  │   └── StatisticsResponse, Dashboarddto, ...
  └── 📁 presentation/controller/
      └── DashboardController
```

### Core Infrastructure
```
📁 modules/core/
  ├── 📁 exception/
  │   └── AppException, ErrorCode, GlobalExceptionHandler
  ├── 📁 infrastructure/
  │   └── Shared configs, base classes
  └── 📁 util/
      └── SecurityUtils, helpers, common utilities
```

---

## 🔍 Which Package Should I Look In?

| Question | Answer |
|----------|--------|
| Where are entity classes? | `modules/[context]/domain/model/` |
| Where are service interfaces? | `modules/[context]/application/service/` |
| Where are service implementations? | `modules/[context]/application/service/impl/` |
| Where are request DTOs? | `modules/[context]/application/dto/request/` |
| Where are response DTOs? | `modules/[context]/application/dto/response/` |
| Where are mappers? | `modules/[context]/application/mapper/` |
| Where are JPA repositories? | `modules/[context]/infrastructure/persistence/repository/` |
| Where are JPA specifications? | `modules/[context]/infrastructure/persistence/specification/` |
| Where are REST controllers? | `modules/[context]/presentation/controller/` |
| Where are repositories (contracts)? | `modules/[context]/domain/repository/` |
| Where is Spring Security config? | `modules/authentication/infrastructure/config/` |
| Where are enums? | `modules/[context]/domain/enums/` |
| Where is exception handling? | `modules/core/exception/` |
| Where are shared utilities? | `modules/core/util/` |

---

## 📖 Naming Conventions

### Entities
```
Product, Category, Brand, Order, User, Role, CartItem, OrderItem
```

### Service Interfaces
```
ProductService, CategoryService, OrderService, AuthenticationService
```

### Service Implementations
```
ProductServiceImpl, CategoryServiceImpl, OrderServiceImpl
```

### DTOs - Request
```
CreateProductRequest, UpdateCategoryRequest, AddToCartRequest, LoginRequest
```

### DTOs - Response
```
ProductResponse, CategoryResponse, OrderResponse, UserResponse
```

### Mappers
```
ProductMapper, CategoryMapper, OrderMapper, UserMapper
```

### Repositories (JPA)
```
ProductRepository, ProductJpaRepository, ProductSpringDataRepository
```

### Controllers
```
ProductController, OrderController, UserController, AuthenticationController
```

### Specifications
```
ProductSpecification, OrderSpecification, ProductSearchSpecification
```

---

## 🔗 Service Usage Examples

### Using Services Within Same Context
```java
// In ProductController
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;  // Same context
    
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }
}
```

### Using Services From Different Context
```java
// In OrderService (Order Context)
@Service
public class OrderService {
    @Autowired
    private UserService userService;  // From User Management Context
    
    @Autowired
    private OrderRepository orderRepository;  // Own context
    
    public void createOrder(CreateOrderRequest request) {
        User user = userService.getUserById(request.getUserId());
        Order order = new Order();
        orderRepository.save(order);
    }
}
```

---

## ✏️ Creating New Feature

### Step 1: Identify Context
Which bounded context does this feature belong to?
- Product-related? → `productcatalog`
- Order-related? → `order`
- User-related? → `usermanagement`
- Auth-related? → `authentication`

### Step 2: Create Domain Model
```
modules/[context]/domain/model/YourEntity.java
```

### Step 3: Create DTO Classes
```
modules/[context]/application/dto/request/YourRequest.java
modules/[context]/application/dto/response/YourResponse.java
```

### Step 4: Create Service Interface
```
modules/[context]/application/service/YourService.java
```

### Step 5: Create Service Implementation
```
modules/[context]/application/service/impl/YourServiceImpl.java
```

### Step 6: Create Mapper
```
modules/[context]/application/mapper/YourMapper.java
```

### Step 7: Create Repository (if DB access)
```
modules/[context]/infrastructure/persistence/repository/YourRepository.java
```

### Step 8: Create REST Controller
```
modules/[context]/presentation/controller/YourController.java
```

---

## 🧪 Testing Structure

Test files should mirror production structure:

```
src/test/java/com/phananh/e_commerce/modules/
├── productcatalog/
│   ├── application/service/ProductServiceTest.java
│   ├── infrastructure/persistence/ProductRepositoryTest.java
│   └── presentation/controller/ProductControllerTest.java
├── order/
│   ├── application/service/OrderServiceTest.java
│   └── presentation/controller/OrderControllerTest.java
└── ...
```

---

## 🔐 Dependency Rules

### ✅ Allowed:
- Controller → Service → Repository
- Application layer → Domain layer
- Controller → Any DTOs
- Service → Service (within same context)

### ❌ NOT Allowed:
- Domain → Application
- Infrastructure → Presentation
- Controller → Entity
- One context Domain → Another context Domain
- Repository → Service

---

## 📝 Import Patterns

### Correct:

```java

import com.phananh.e_commerce.modules.productcatalog.application.dto.request.CreateProductRequest;

```

### Incorrect:

```java
// Don't import from other context's domain directly

import com.phananh.e_commerce.modules.order.domain.model.OrderItem;

// Don't import implementations

```

---

## 📚 Additional Resources

- **BOUNDED_CONTEXT_STRUCTURE.md** - Detailed architecture documentation
- **MIGRATION_GUIDE.md** - Step-by-step migration instructions
- **[Context]/package-info.java** - Java doc comments in each context

---

## 🎓 Learning Path

1. Start with `modules/core/` - Understand shared infrastructure
2. Learn `modules/authentication/` - Security is foundational
3. Study `modules/usermanagement/` - Simpler context
4. Explore `modules/productcatalog/` - Most complex entity domain
5. Understand `modules/order/` - Cross-context dependencies
6. Review `modules/dashboard/` - Aggregation pattern

---

## 🚀 Pro Tips

1. **Use IntelliJ's Find Action** (Ctrl+Shift+A):
   - Search for class names quickly
   - Navigate to any package

2. **Use Go to Class** (Ctrl+N / Cmd+O):
   - Jump directly to entity/service/controller

3. **Use Find Usages** (Alt+F7):
   - See where a service is injected/used

4. **Use Package Diagram**:
   - Right-click package → Diagrams → Show Popup
   - Visualize structure

5. **Create Favorites**:
   - Star frequently-used packages for quick access

---

*Last Updated: 2026-05-13*

