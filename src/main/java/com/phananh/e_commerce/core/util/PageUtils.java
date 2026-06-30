package com.phananh.e_commerce.core.util;

public class PageUtils {

    public static Integer getPageNumber(Integer page) {
        return page == null || page < 0 ? 0 : page;
    }

    public static Integer getPageSize(Integer size) {
        return size == null || size <= 0 ? 50 : size;
    }

    public static String getSortBy(String sortBy) {
        return StringUtils.isBlank(sortBy) ? "createdAt" : sortBy;
    }

    public static String getSortType(String sortType) {
        return StringUtils.isBlank(sortType) ? "desc" : sortType;
    }
}
