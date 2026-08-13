package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new ResourceNotFoundException("Category", "categoryId", categoryId)
                );
        Product product = modelMapper.map(productDTO, Product.class);

        List<Product> products = category.getProducts();
        for(int i = 0; i < products.size() ; i++){
            Product product1 = products.get(i);
            if(product1.getProductName().equals(productDTO.getProductName()))
                throw new APIException("Product with the product name "+productDTO.getProductName()+" already exists!");
        }
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01)*product.getPrice();
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponseDTO  getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty())
            throw new APIException("There is no product available in the database");

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> allProducts = productPage.getContent();
        List<ProductDTO> productDTOS = allProducts.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);

        productResponseDTO.setPageNumber(pageNumber);
        productResponseDTO.setPageSize(pageSize);
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchByCategory(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        List<Product> products = searchByCategory.findAll();
//        List<ProductDTO> productDTOS = products.stream().filter(product -> product.getCategory().getCategoryId().equals(categoryId)).
//                map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new ResourceNotFoundException("Category", "categoryId", categoryId)
        );

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> allProducts = productPage.getContent();
        if(allProducts.isEmpty())
            throw new APIException("There is no product available in the database");
        List<ProductDTO> productDTOS = allProducts.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);

        productResponseDTO.setPageNumber(pageNumber);
        productResponseDTO.setPageSize(pageSize);
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
        List<Product> products = productPage.getContent();
        if(products.isEmpty())
            throw new APIException("There is no product available in the database");
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);

        productResponseDTO.setPageNumber(pageNumber);
        productResponseDTO.setPageSize(pageSize);
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product productToBeUpdated = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("product", "productId",productId)
                );

        Product product = modelMapper.map(productDTO, Product.class);
        productToBeUpdated.setProductName(product.getProductName());
        productToBeUpdated.setDescription(product.getDescription());
        productToBeUpdated.setQuantity(product.getQuantity());
        productToBeUpdated.setDiscount(product.getDiscount());
        productToBeUpdated.setPrice(product.getPrice());
        productToBeUpdated.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(productToBeUpdated);

        List<Cart> carts = cartRepository.findByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart ->{
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream()
                    .map(p-> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .toList();
            cartDTO.setProducts(productDTOS);
            return  cartDTO;
        }).toList();

        cartDTOS.forEach(cart -> cartService.updateProductsInCart(cart.getCartId(), productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product removedProduct = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("product","productId",productId)
                );

        ProductDTO productDTO = modelMapper.map(removedProduct, ProductDTO.class);

        List<Cart> carts = cartRepository.findByProductId(productId);
        carts.forEach((cart -> cartService.deleteProductFromCart(cart.getId(), productId)));

        productRepository.delete(removedProduct);
        return productDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from database
        Product productFromDb = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("Product","productId",productId)
                );

        String fileName = fileService.uploadImage(path, image);

        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
