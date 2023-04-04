package fr.insy2s.commerce.shoponlineback.services;

import fr.insy2s.commerce.shoponlineback.beans.Category;
import fr.insy2s.commerce.shoponlineback.beans.Product;
import fr.insy2s.commerce.shoponlineback.dtos.CategoryDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.CategoryNotFoundException;
import fr.insy2s.commerce.shoponlineback.exceptions.generic_exception.WebservicesGenericServiceException;
import fr.insy2s.commerce.shoponlineback.interfaces.Webservices;
import fr.insy2s.commerce.shoponlineback.mappers.CategoryMapper;
import fr.insy2s.commerce.shoponlineback.mappers.CategoryMapperImpl;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapper;
import fr.insy2s.commerce.shoponlineback.mappers.ProductMapperImpl;
import fr.insy2s.commerce.shoponlineback.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements Webservices<CategoryDTO, WebservicesGenericServiceException> {

    private final CategoryRepository categoryRepository;
    
    private final FirebaseFileService firebaseFileService;

    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    private ProductMapper productMapper = new ProductMapperImpl();

    @Override
    public Page<CategoryDTO> all(Pageable pageable) {


        return this.categoryRepository.findAll(pageable)
                .map(this.categoryMapper::fromCategory);
    }

    @Override
    public void add(CategoryDTO e) {
//        this.categoryRepository.save(this.categoryMapper.fromCategoryDTO(e));

    }
    public CategoryDTO addNew(CategoryDTO e) {
       Category category = this.categoryRepository.save(this.categoryMapper.fromCategoryDTO(e));
       return this.categoryMapper.fromCategory(category);

    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO e) {
        return this.categoryMapper.fromCategory(this.categoryRepository.findById(id)
                .map(p-> {
                   if(p.getName() != null)
                       p.setName(e.getName());
                   if(p.getUrl() != null)
                       p.setUrl(e.getUrl());
//                   if(p.getProducts() != null){
//                       List<Product> products = e.getProducts().stream().map(this.productMapper::fromProductDTO).collect(Collectors.toList());
//                       p.setProducts(products);
//                   }
                   return this.categoryRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Sorry not found id category"))
        );
    }

    @Override
    public void remove(Long id) {
        Optional<Category>  category = this.categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new CategoryNotFoundException("Category id: " + id + "not found");
        }
            this.categoryRepository.deleteById(id);
    }

    @Override
    public Optional<CategoryDTO> getById(Long id) {
        return this.categoryRepository.findById(id)
                .map(this.categoryMapper::fromCategory)
                .map(Optional::of)
                .orElseThrow(()-> new CategoryNotFoundException("Category with Id : "+id+" not found"))
                ;
    }
    
	public void updatePicture(Long id, MultipartFile file) {
		final Optional<Category> optCategory = categoryRepository.findById(id);
		if (optCategory.isPresent()) {
			try {
				final String url = firebaseFileService.upload(file);
				Category category = optCategory.get();
				category.setUrl(url);
				categoryRepository.save(category);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to upload file");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category id: " + id + "not found");
		}
	}
}

