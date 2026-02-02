package com.example.demo.io;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class FileResponse {
    private String fileUrl;
}
