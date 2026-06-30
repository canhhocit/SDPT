# Cloudinary Implementation

Tài liệu này mô tả phần tích hợp Cloudinary đang dùng trong dự án.

## Vị trí cấu hình / service
- `core.infrastructure` hoặc package config tương ứng: nơi tạo bean Cloudinary
- `application.service` / `application.service.impl`: nơi xử lý upload file

## Properties cần có
```properties
cloudinary.cloud_name=...
cloudinary.api_key=...
cloudinary.api_secret=...
```

## Ghi chú an toàn
- Không commit credentials thật lên source
- Dùng biến môi trường khi deploy production

## Cách dùng
- Gọi service upload từ controller hoặc service tầng application
- Trả về secure URL của file sau khi upload

## Thư mục đề xuất
- `products/` — ảnh sản phẩm
- `users/` — avatar người dùng
- `categories/` — ảnh danh mục

## Troubleshooting nhanh
- Sai credentials: kiểm tra `application.properties`
- Upload lỗi mạng: kiểm tra kết nối và quota Cloudinary
- File không thấy sau upload: kiểm tra `folder` và log ứng dụng
