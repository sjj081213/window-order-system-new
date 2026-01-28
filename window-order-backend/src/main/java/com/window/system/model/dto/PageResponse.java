package com.window.system.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> list;
    private Long total;

    public static <T> PageResponse<T> of(List<T> list, Long total) {
        PageResponse<T> response = new PageResponse<>();
        response.setList(list);
        response.setTotal(total);
        return response;
    }
}
