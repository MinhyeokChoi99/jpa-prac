package kr.co.prac.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    @NotBlank
    private String name;

    @Min(0)
    private Integer price;

    @Min(0)
    private Integer stock;

    @Size(max = 1000)
    private String description;


}
