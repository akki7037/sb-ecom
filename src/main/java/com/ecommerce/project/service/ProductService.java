package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponseDTO getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchByCategory(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
