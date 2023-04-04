package fr.insy2s.commerce.shoponlineback.controllers;

import fr.insy2s.commerce.shoponlineback.dtos.ProductDTO;
import fr.insy2s.commerce.shoponlineback.dtos.PromotionDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.ProductNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.PromotionNotFoundException;
import fr.insy2s.commerce.shoponlineback.services.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shopping-online")
@Slf4j
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/no-role/all-promotion")
    public ResponseEntity<Page<PromotionDTO>> allPromotion(Pageable pageable)
    {
        return ResponseEntity.ok(this.promotionService.all(pageable));
    }
    @GetMapping("/no-role/get-by-id-promotion/{idPromotion}")
    public ResponseEntity<PromotionDTO> getByIdPromotion(@Valid @PathVariable Long idPromotion)
    {
        return this.promotionService.getById(idPromotion)
                .map(promotionDTO -> new ResponseEntity<>(promotionDTO, HttpStatus.OK))
                .orElseThrow(() -> new PromotionNotFoundException("Promotion with id " +idPromotion+ " was not found"));
    }
    @Secured("ROLE_ADMIN")
    @PostMapping("/add-promotion-dto")
    public ResponseEntity<PromotionDTO> addProductDTO (@Valid @RequestBody PromotionDTO promotionDTO) {
        try
        {
            return  ResponseEntity.status(HttpStatus.CREATED).body(this.promotionService.addNew(promotionDTO));
        }catch (ConstraintViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/remove-promotion-dto/{idPromotion}")
    public ResponseEntity<String> removeProductDTO(@Validated @PathVariable Long idPromotion){
        this.promotionService.remove(idPromotion);

        return ResponseEntity.status(200).body("Promotion with id : "+idPromotion+" successfully delete");
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update-promotion-dto/{idPromotion}")
    public ResponseEntity<PromotionDTO> updatePromotionDTO(@Valid @PathVariable Long idPromotion, @RequestBody PromotionDTO promotionDTO) {

        log.info("Updating promotion with id : {}", idPromotion);

        try {
            PromotionDTO updatePromotionDTO = this.promotionService.update(idPromotion, promotionDTO);

            log.info("Product with id : {} updated successfully " ,idPromotion);

            return new ResponseEntity<>(updatePromotionDTO, HttpStatus.OK);
        }catch (ProductNotFoundException exception)
        {
            log.error("Error occured while updating promotion with id: {}. Error: {}", idPromotion, exception.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
