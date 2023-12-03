package com.lambdatauri.ocr.dto;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PdfDto {
    String fileName;

    String text;
}
