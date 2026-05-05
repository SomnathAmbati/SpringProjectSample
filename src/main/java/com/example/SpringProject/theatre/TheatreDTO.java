package com.example.SpringProject.theatre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheatreDTO {

    private Long id;
    @NotBlank(message = "{theatre.name.invalid}")
    @Size(min = 1, max = 100, message = "{theatre.name.invalid}")
    private String name;
    
    @NotBlank(message = "{theatre.location.invalid}")
    @Size(min = 1, max = 200, message = "{theatre.location.invalid}")
    private String location;
}

