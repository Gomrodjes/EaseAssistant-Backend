package com.example.demo.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApi<T> {

    private boolean success;
    private T data;
    private String message;

}
