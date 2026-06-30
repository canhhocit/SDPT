# Architecture Diagram

Sơ đồ rút gọn cho cấu trúc package hiện tại.

```text
com.phananh.e_commerce/
├── ECommerceApplication.java
├── core/
│   ├── exception/
│   ├── infrastructure/
│   └── util/
├── productcatalog/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── order/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── usermanagement/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
├── authentication/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
└── dashboard/
    ├── application/
    └── presentation/
```

## Luồng xử lý
```text
Controller -> Service/Application -> Domain -> Infrastructure -> Database/External Service
```

## Ghi nhớ
- Package gốc hiện tại là `com.phananh.e_commerce`
- Không còn dùng cấu trúc `modules/` trong source code thực tế
- Các tài liệu lịch sử nằm trong `docs/archived/`

