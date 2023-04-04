package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Category;
import fr.insy2s.commerce.shoponlineback.dtos.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "default")
public interface CategoryMapper {

    CategoryDTO fromCategory(Category category);

    Category fromCategoryDTO(CategoryDTO categoryDTO);



}
