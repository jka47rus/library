package com.example.library.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookResponse {
    private UUID id;
    private String name;
    private String author;
    private String categoryName;
}
