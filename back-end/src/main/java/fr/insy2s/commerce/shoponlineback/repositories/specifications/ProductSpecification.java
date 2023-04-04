package fr.insy2s.commerce.shoponlineback.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import fr.insy2s.commerce.shoponlineback.beans.Category;
import fr.insy2s.commerce.shoponlineback.beans.Product;

public class ProductSpecification {
	public static Specification<Product> nameContains(String name) {
	    return (product, cq, cb) -> cb.like(product.get("name"), "%" + name + "%");
	}
	
	public static Specification<Product> hasCategory(Category category) {
		return (product, cq, cb) -> cb.equal(product.get("category"), category);
	}
	
	public static Specification<Product> priceTTCGreaterThanOrEqualTo(Double priceTTC) {
		return (product, cq, cb) -> cb.greaterThanOrEqualTo(product.get("priceTTC"), priceTTC);
	}
	
	public static Specification<Product> priceTTCLessThanOrEqualTo(Double priceTTC) {
		return (product, cq, cb) -> cb.lessThanOrEqualTo(product.get("priceTTC"), priceTTC);
	}
}
