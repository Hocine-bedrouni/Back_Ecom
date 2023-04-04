package fr.insy2s.commerce.shoponlineback.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    
    private Long id;
    
    private String name;

    private String url;

//    private List<ProductDTO> products;

}
