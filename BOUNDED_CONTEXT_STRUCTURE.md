# Bounded Context Structure

Tài liệu này mô tả cấu trúc bounded context hiện tại của dự án theo package thật trong `src/main/java/com/phananh/e_commerce`.

## Các context chính
- `core` — exception, util, cấu hình dùng chung
- `productcatalog` — product, category, brand, variant, attribute
- `order` — order, order item, cart item
- `usermanagement` — user, role, quản lý tài khoản
- `authentication` — login, token, security config
- `dashboard` — thống kê và báo cáo

## Quy ước lớp
- `domain` — entity, repository contract, enum nghiệp vụ
- `application` — service, DTO, mapper, orchestrator
- `infrastructure` — Spring Data, config, persistence, kỹ thuật triển khai
- `presentation` — REST controller

## Nguyên tắc giao tiếp
- Không gọi trực tiếp entity của context khác
- Trao đổi qua DTO/service interface
- Thống kê nặng dùng projection hoặc SQL ở tầng persistence
- Mapping DTO dùng MapStruct hoặc mapper trong `application/mapper`

## Cấu trúc thực tế
```text
com.phananh.e_commerce/
├── core/
├── productcatalog/
├── order/
├── usermanagement/
├── authentication/
└── dashboard/
```

## Ghi chú hiện tại
- Ứng dụng đang dùng package gốc `com.phananh.e_commerce`
- Các tài liệu session cũ đã chuyển vào `docs/archived/`
- Đọc thêm `ARCHITECTURE_DIAGRAM.md` nếu cần sơ đồ package chi tiết hơn

