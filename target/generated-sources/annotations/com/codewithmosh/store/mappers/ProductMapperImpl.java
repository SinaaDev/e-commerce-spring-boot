package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CreateProductRequest;
import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Category;
import com.codewithmosh.store.entities.Product;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-16T15:55:36+0430",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Homebrew)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        Byte categoryId = null;
        Long id = null;
        String name = null;
        BigDecimal price = null;
        String description = null;

        categoryId = productCategoryId( product );
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        description = product.getDescription();

        ProductDto productDto = new ProductDto( id, name, price, description, categoryId );

        return productDto;
    }

    @Override
    public Product toEntity(CreateProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( request.getName() );
        product.description( request.getDescription() );
        product.price( request.getPrice() );

        return product.build();
    }

    @Override
    public void update(ProductDto productDto, Product product) {
        if ( productDto == null ) {
            return;
        }

        product.setName( productDto.getName() );
        product.setDescription( productDto.getDescription() );
        product.setPrice( productDto.getPrice() );
    }

    private Byte productCategoryId(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
