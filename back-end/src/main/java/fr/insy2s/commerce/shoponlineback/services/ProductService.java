package fr.insy2s.commerce.shoponlineback.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.insy2s.commerce.shoponlineback.beans.Category;
import fr.insy2s.commerce.shoponlineback.beans.Picture;
import fr.insy2s.commerce.shoponlineback.beans.Product;
import fr.insy2s.commerce.shoponlineback.dtos.ProductDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.CategoryNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.ProductNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import fr.insy2s.commerce.shoponlineback.interfaces.Webservices;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapper;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapperImpl;
import fr.insy2s.commerce.shoponlineback.mappers.PromotionMapper;
import fr.insy2s.commerce.shoponlineback.mappers.PromotionMapperImpl;
import fr.insy2s.commerce.shoponlineback.repositories.CategoryRepository;
import fr.insy2s.commerce.shoponlineback.repositories.ProductRepository;
import fr.insy2s.commerce.shoponlineback.repositories.PromotionRepository;
import fr.insy2s.commerce.shoponlineback.repositories.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static fr.insy2s.commerce.shoponlineback.repositories.specifications.ProductSpecification.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements Webservices<ProductDTO, WebservicesGenericServiceException> {

	private final ProductRepository productRepository;

	private final CategoryRepository categoryRepository;

	private final PromotionRepository promotionRepository;

	private final FirebaseFileService firebaseFileService;

	private final ProductMapper productMapper = new ProductMapperImpl();

	private final PromotionMapper promotionMapper = new PromotionMapperImpl();

	private final UuidService uuidService;
	
	

	@Override
	public Page<ProductDTO> all(Pageable pageable) {

		return this.productRepository.findByPresentIsTrue(pageable).map(this.productMapper::fromProduct);
	}

	@Override
	public void add(ProductDTO e) {
		e.setRefProduct(this.uuidService.generateUuid());

		if (e.getPresent() == null)
			e.setPresent(true);

		this.productRepository.save(this.productMapper.fromProductDTO(e));
	}

	public ProductDTO addNew(ProductDTO p) {
		p.setRefProduct(this.uuidService.generateUuid());
		Product product = this.productRepository.save(this.productMapper.fromProductDTO(p));
		return this.productMapper.fromProduct(product);
	}

	@Override
	public ProductDTO update(Long id, ProductDTO e) {
		return this.productMapper.fromProduct(this.productRepository.findById(id).map(p -> {
			p.setRefProduct(this.uuidService.generateUuid());
			if (p.getName() != null)
				p.setName(e.getName());
			if (p.getPriceTTC() != null)
				p.setPriceTTC(e.getPriceTTC());
			if (p.getProductInventory() != null)
				p.setProductInventory(e.getProductInventory());
			p.setProductDescription(e.getProductDescription());
			if (p.getPresent() == null || !p.getPresent())
				p.setPresent(true);
			if (p.getCategory() != null) {
				Product product = this.productMapper.fromProductDTO(e);

				p.setCategory(product.getCategory());
			}

			return this.productRepository.save(p);
		}).orElseThrow(() -> new RuntimeException("Sorry not this id for product")));
	}

	@Override
	public void remove(Long id) {
		Product product = this.productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("product not found"));

		product.setPresent(false);
		this.productRepository.save(product);
	}

	@Override
	public Optional<ProductDTO> getById(Long id) {
		return this.productRepository.findById(id).map(this.productMapper::fromProduct).map(Optional::of)
				.orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " was not found"));
	}

	public List<ProductDTO> getByIdIn(List<Long> idList) {
		return this.productRepository.findByIdProductIn(idList).stream().map(productMapper::fromProduct)
				.collect(Collectors.toList());
	}

	public Page<ProductDTO> getProductsByCategoryId(Long id, Pageable page) {
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " was not found"));
		List<Product> productList = this.productRepository.findByCategory(category);
		Page<Product> page1 = new PageImpl<>(productList, page, productList.size());
		return page1.map(this.productMapper::fromProduct);

	}

	public Page<ProductDTO> updatePresentAllProduct(Pageable pageable) {
		List<Product> products = this.productRepository.findAll();

		List<Product> productList = new ArrayList<>();

		for (int i = 0; i < products.size(); i++) {
			products.get(i).setPresent(true);
			productList.add(products.get(i));
		}
		
		Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

		return productPage.map(this.productMapper::fromProduct);
	}

	public void addPicture(Long id, MultipartFile file) {
		final Optional<Product> optProduct = productRepository.findById(id);
		if (optProduct.isPresent()) {
			try {
				final String url = firebaseFileService.upload(file);
				Product product = optProduct.get();
				Picture picture = new Picture();
				picture.setUrl(url);
				picture.setName(file.getOriginalFilename());
				picture.setProduct(product);
				product.getPictures().add(picture);
				productRepository.save(product);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to upload file");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id: " + id + "not found");
		}
	}

	public Page<ProductDTO> getProducts(Pageable pageable,
			Optional<Long> categoryId,
			Optional<String> name,
			Optional<Double> minPrice,
			Optional<Double> maxPrice) {				
		Specification<Product> specification = Specification.where(null);
		
		if(categoryId.isPresent()) {
			Category category = categoryRepository.findById(categoryId.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "category doesn't exist"));
			specification = specification.and(hasCategory(category));
		}
		
		if(name.isPresent()) {
			specification = specification.and(nameContains(name.get()));
		}
		
		if(minPrice.isPresent()) {
			specification = specification.and(priceTTCGreaterThanOrEqualTo(minPrice.get()));
		}
		
		if(maxPrice.isPresent()) {
			specification = specification.and(priceTTCLessThanOrEqualTo(maxPrice.get()));
		}
		
		final Page<Product> products = productRepository.findAll(specification, pageable);

		return products.map(productMapper::fromProduct);
	}

}
