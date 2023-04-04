package fr.insy2s.commerce.shoponlineback.controllers;

import java.io.IOException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.insy2s.commerce.shoponlineback.dtos.CategoryDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.CategoryNotFoundException;
import fr.insy2s.commerce.shoponlineback.services.CategoryService;
import fr.insy2s.commerce.shoponlineback.services.FirebaseFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    
    @GetMapping("/no-role/all-category-dto")
    public ResponseEntity <Page<CategoryDTO>> allCategoryDTO(Pageable pageable){
        return ResponseEntity.ok(this.categoryService.all(pageable));}

    @PostMapping("/no-role/add-category-dto")
    public ResponseEntity<CategoryDTO> addCategoryDTO(@Valid @RequestBody CategoryDTO categoryDTO) {

        try
        {
            return ResponseEntity.status(HttpStatus.CREATED).body( this.categoryService.addNew(categoryDTO));
        }catch (ConstraintViolationException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/no-role/update-category-dto/{idCategory}")
    public ResponseEntity<CategoryDTO> updateCategoryDTO(@Valid @PathVariable Long idCategory, @RequestBody CategoryDTO categoryDTO) {
        this.categoryService.update(idCategory, categoryDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryDTO);
    }

    @DeleteMapping("/no-role/remove-category-dto/{idCategory}")
    public ResponseEntity<String> removeCategoryDTO(@Valid @PathVariable Long idCategory) {
        this.categoryService.remove(idCategory);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Category successfully deleted");
    }

    @GetMapping("/no-role/get-by-id-category-dto/{idCategory}")
    public ResponseEntity<CategoryDTO> getByIdCategoryDTO(@Valid @PathVariable Long idCategory) {
        return this.categoryService.getById(idCategory)
                .map(category->{
                    log.info("category with id :{}" , idCategory);
                    return new ResponseEntity<>(category, HttpStatus.OK);
                }).orElseThrow(()->{
                    log.error("category with id : {} was not found", idCategory);
                    return new CategoryNotFoundException("not found");
                });
    }
    

    @PostMapping("/no-role/categories/{id}/picture")
    public ResponseEntity<String> uploadCategoryPicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    		categoryService.updatePicture(id, file);
    		return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

}
