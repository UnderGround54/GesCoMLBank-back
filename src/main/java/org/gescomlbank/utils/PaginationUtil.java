package org.gescomlbank.utils;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtil {
    private PaginationUtil() {
        // Empêche l'instanciation
    }
    public static <T> Map<String, Object> getWithPaginated(Page<T> page) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> metaData = new HashMap<>();

        metaData.put("totalElements", page.getTotalElements());
        metaData.put("totalPages", page.getTotalPages());
        metaData.put("currentPage", page.getNumber() + 1);
        metaData.put("pageSize", page.getSize());
        metaData.put("last", page.isLast());

        data.put("list", page.getContent());
        data.put("meta", metaData);

        return data;
    }
}
