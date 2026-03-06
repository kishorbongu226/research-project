package com.example.demo.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CenterDetailsResponse {

    private String imgUrl;
    private String professorName;
    private String professorOccupation;
    private String centerName;
    private String centerDescription;
}