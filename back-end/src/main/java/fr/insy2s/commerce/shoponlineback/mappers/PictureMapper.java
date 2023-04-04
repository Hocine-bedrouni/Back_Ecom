package fr.insy2s.commerce.shoponlineback.mappers;

import org.mapstruct.Mapper;

import fr.insy2s.commerce.shoponlineback.beans.Picture;
import fr.insy2s.commerce.shoponlineback.dtos.PictureDTO;

@Mapper(componentModel = "default")
public interface PictureMapper {

    PictureDTO fromPicture(Picture picture);

    Picture fromPictureDTO(PictureDTO pictureDTO);
}
