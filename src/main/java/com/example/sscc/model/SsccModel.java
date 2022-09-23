package com.example.sscc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SsccModel {

    private String code;
    private boolean isValidated;
    private Feed feed;
}
