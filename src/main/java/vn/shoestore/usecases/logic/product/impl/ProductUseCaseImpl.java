package vn.shoestore.usecases.logic.product.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import vn.shoestore.application.request.ProductRequest;
import vn.shoestore.domain.adapter.*;
import vn.shoestore.domain.model.Product;
import vn.shoestore.domain.model.ProductAttachment;
import vn.shoestore.domain.model.ProductBrand;
import vn.shoestore.domain.model.ProductCategory;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.utils.ModelTransformUtils;
import vn.shoestore.usecases.logic.product.IProductUseCase;

import java.util.*;

import static vn.shoestore.shared.constants.ExceptionMessage.PRODUCT_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class ProductUseCaseImpl implements IProductUseCase {
  private final ProductAdapter productAdapter;
  private final ProductAttachmentAdapter productAttachmentAdapter;
  private final ProductCategoryAdapter productCategoryAdapter;
  private final ProductBrandAdapter productBrandAdapter;

  @Override
  @Transactional
  public void createOrUpdateProduct(ProductRequest request) {
    if (Objects.nonNull(request.getId())) {
      update(request);
    } else create(request);
  }

  @Override
  public void deleteProduct(Long id) {

  }

  private void create(ProductRequest request) {
    Product product =
        Product.builder()
            .code(request.getCode())
            .description(request.getDescription())
            .price(request.getPrice())
            .name(request.getName())
            .build();

    Product savedProduct = productAdapter.save(product);

    processSave(request, savedProduct);
  }

  private void processSave(ProductRequest request, Product savedProduct) {
    if (Objects.nonNull(request.getBrandId())) {
      productBrandAdapter.saveAll(
          Collections.singletonList(
              ProductBrand.builder()
                  .brandId(request.getBrandId())
                  .productId(savedProduct.getId())
                  .build()));
    }

    if (Objects.nonNull(request.getCategories()) && !request.getCategories().isEmpty()) {
      List<ProductCategory> productCategories = new ArrayList<>();
      for (Long categoryId : request.getCategories()) {
        productCategories.add(
            ProductCategory.builder()
                .categoryId(categoryId)
                .productId(savedProduct.getId())
                .build());
      }
      productCategoryAdapter.saveAll(productCategories);
    }

    if (Objects.nonNull(request.getAttachments()) && !request.getAttachments().isEmpty()) {
      List<ProductAttachment> productAttachments = new ArrayList<>();
      for (String attachment : request.getAttachments()) {
        productAttachments.add(
            ProductAttachment.builder()
                .productId(savedProduct.getId())
                .attachment(attachment)
                .build());
      }
      productAttachmentAdapter.saveAll(productAttachments);
    }
  }

  private void processDelete(Long productId) {
    List<ProductCategory> productCategories =
        productCategoryAdapter.findAllByProductIds(Collections.singletonList(productId));

    List<ProductAttachment> productAttachments =
        productAttachmentAdapter.findAllByProductIds(Collections.singletonList(productId));

    List<ProductBrand> productBrands =
        productBrandAdapter.findAllByProductIds(Collections.singletonList(productId));

    productCategoryAdapter.deleteByIds(
        ModelTransformUtils.getAttribute(productCategories, ProductCategory::getId));

    productAttachmentAdapter.deleteByIds(
        ModelTransformUtils.getAttribute(productAttachments, ProductAttachment::getId));

    productBrandAdapter.deleteByIds(
        ModelTransformUtils.getAttribute(productBrands, ProductBrand::getId));
  }

  private void update(ProductRequest request) {
    Product product = productAdapter.getProductById(request.getId());
    if (Objects.isNull(product)) {
      throw new InputNotValidException(PRODUCT_NOT_FOUND);
    }

    product.setCode(Objects.nonNull(request.getCode()) ? request.getCode() : product.getCode());
    product.setName(Objects.nonNull(request.getName()) ? request.getName() : product.getName());
    product.setPrice(Objects.nonNull(request.getPrice()) ? request.getPrice() : product.getPrice());
    product.setDescription(
        Objects.nonNull(request.getDescription())
            ? request.getDescription()
            : product.getDescription());

    productAdapter.save(product);
    processDelete(product.getId());
    processSave(request, product);
  }
}
