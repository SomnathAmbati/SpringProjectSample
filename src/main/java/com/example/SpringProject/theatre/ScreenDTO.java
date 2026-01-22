package com.example.SpringProject.theatre;

import lombok.Data;

@Data
public class ScreenDTO {
    private Long id;

    private String screenName;

    private TheatreDTO theatre;
}
