package com.varta.util;

import java.util.List;

public class PaginationUtil {
    public static <T> List<T> page(List<T> list, int page, int pageSize) {
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(list.size(), from + pageSize);
        return from >= to ? List.of() : list.subList(from, to);
    }
}