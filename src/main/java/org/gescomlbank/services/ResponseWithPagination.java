package org.gescomlbank.services;

import org.gescomlbank.utils.PaginationUtil;
import org.gescomlbank.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResponseWithPagination {
    public <T> ResponseEntity<Map<String, Object>> getResponse(
            String message,
            Pageable pageable,
            JpaRepository<T, Long> repository
    ) {
        Page<T> dataPaged = repository.findAll(pageable);
        Map<String, Object> data = PaginationUtil.getWithPaginated(dataPaged);

        return ResponseUtil.successResponse(message, data);
    }
}
