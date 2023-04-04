package fr.insy2s.commerce.shoponlineback.controllers;

import fr.insy2s.commerce.shoponlineback.dtos.ProductDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.ProductNotFoundException;
import fr.insy2s.commerce.shoponlineback.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

@RestController
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/no-role/products")
	public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable, 
			@RequestParam Optional<Long> categoryId, 
			@RequestParam Optional<String> name,
			@RequestParam Optional<Double> minPrice,
			@RequestParam Optional<Double> maxPrice) {
		return ResponseEntity.ok(this.productService.getProducts(pageable, categoryId, name, minPrice, maxPrice));
	}
	
	@GetMapping("/no-role/all-product-dto")
	public ResponseEntity<Page> allProductDTO(Pageable pageable) {

		log.debug("Finding all users"); 

		return ResponseEntity.ok(this.productService.all(pageable));

	}

    @Secured("ROLE_ADMIN")
    @PostMapping("/add-product-dto")
    public ResponseEntity<ProductDTO> addProductDTO(@Valid @RequestBody ProductDTO productDTO) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.addNew(productDTO));
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/update-product-dto/{idProduct}")
	public ResponseEntity<ProductDTO> updateProductDTO(@Valid @PathVariable Long idProduct,
			@RequestBody ProductDTO productDTO) {

		log.info("Updating ordered with id : {}", idProduct);

		try {
			ProductDTO updateProductDTO = this.productService.update(idProduct, productDTO);

			log.info("Product with id : {} updated successfully ", idProduct);

			return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);
		} catch (ProductNotFoundException exception) {
			log.error("Error occured while updating product with id: {}. Error: {}", idProduct, exception.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/remove-product-dto/{idProduct}")
	public ResponseEntity<String> removeProductDTO(@Valid @PathVariable Long idProduct) {
		this.productService.remove(idProduct);

		return ResponseEntity.status(200).body("Product with id : " + idProduct + " successfully delete");
	}

//    @Secured("ROLE_ADMIN")
	@GetMapping("/no-role/get-by-id-product/{idProduct}")
	public ResponseEntity<ProductDTO> getByIdProductDTO(@Valid @PathVariable Long idProduct) {

		return this.productService.getById(idProduct).map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK))
				.orElseThrow(() -> new ProductNotFoundException("Product with id " + idProduct + " was not found"));
	}

	@GetMapping("/no-role/get-by-category-id/{id}")
	public ResponseEntity<Page<ProductDTO>> findProductsByCategoryId(@Valid @PathVariable Long id, Pageable page) {
		return ResponseEntity.ok(this.productService.getProductsByCategoryId(id, page));
	}

	@GetMapping("/no-role/get-all-update")
	public ResponseEntity<Page<ProductDTO>> getUpdatePresentAllProduct(Pageable pageable) {
		return ResponseEntity.ok(this.productService.updatePresentAllProduct(pageable));
	}

	@GetMapping("/no-role/all-product-by-id-in")
	public ResponseEntity<List<ProductDTO>> getAllProductByIdIn(@RequestParam List<Long> idList) {
		return ResponseEntity.ok(this.productService.getByIdIn(idList));
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/products/{id}/picture")
	public ResponseEntity<String> uploadCategoryPicture(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {
		productService.addPicture(id, file);
		return ResponseEntity.status(HttpStatus.CREATED).body("created");
	}

}
