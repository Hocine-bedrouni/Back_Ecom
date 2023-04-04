package fr.insy2s.commerce.shoponlineback.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fr.insy2s.commerce.shoponlineback.beans.Category;
import fr.insy2s.commerce.shoponlineback.beans.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>  {
	
    List<Product> findByCategory(Category category);

    Page<Product> findByPresentIsTrue(Pageable pageable);
    
    List<Product> findByIdProductIn(List<Long> id);
    
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Product> findAllByNameContains(String name, Pageable pageable);
    
    Page<Product> findAllByCategoryIdAndNameContains(Long categoryId, String name, Pageable pageable); 
    
}
