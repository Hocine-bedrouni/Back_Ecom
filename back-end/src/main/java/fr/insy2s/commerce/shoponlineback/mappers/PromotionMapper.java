package fr.insy2s.commerce.shoponlineback.mappers;

import fr.insy2s.commerce.shoponlineback.beans.Promotion;
import fr.insy2s.commerce.shoponlineback.dtos.PromotionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "default")
public interface PromotionMapper {

    PromotionDTO fromPromotion(Promotion promotion);

    Promotion fromPromotionDTO(PromotionDTO promotionDTO);
}
