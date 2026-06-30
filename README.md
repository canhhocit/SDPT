# E-Commerce Backend

Spring Boot backend cho hệ thống thương mại điện tử, tổ chức theo bounded context:
- `productcatalog`
- `order`
- `usermanagement`
- `authentication`
- `dashboard`
- `core`

## Tài liệu chính
- Kiến trúc tổng quan: `BOUNDED_CONTEXT_STRUCTURE.md`
- Sơ đồ package / luồng layer: `ARCHITECTURE_DIAGRAM.md`
- Tích hợp Cloudinary: `CLOUDINARY_IMPLEMENTATION.md`
- Hỗ trợ nhanh: `HELP.md`
- Danh mục tài liệu đang dùng: `docs/README.md`

## Chạy ứng dụng
```powershell
.\mvnw.cmd spring-boot:run
```

## Build nhanh
```powershell
.\mvnw.cmd -DskipTests=true compile
```

## Ghi chú
- Tài liệu cũ, báo cáo session và bản nháp đã được gom vào `docs/archived/`.
- Nếu cần tra cứu nhanh theo chủ đề, mở `docs/README.md` trước.
# e_commere_nghich
