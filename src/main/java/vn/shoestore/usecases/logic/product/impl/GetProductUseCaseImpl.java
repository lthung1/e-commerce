package vn.shoestore.usecases.logic.product.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.application.response.ProductResponse;
import vn.shoestore.application.response.SearchProductResponse;
import vn.shoestore.domain.adapter.*;
import vn.shoestore.domain.model.*;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.utils.ModelMapperUtils;
import vn.shoestore.shared.utils.ModelTransformUtils;
import vn.shoestore.usecases.logic.product.IGetProductUseCase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static vn.shoestore.shared.constants.ExceptionMessage.PRODUCT_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements IGetProductUseCase {
  private final ProductAdapter productAdapter;
  private final ProductBrandAdapter productBrandAdapter;
  private final BrandAdapter brandAdapter;
  private final ProductCategoryAdapter productCategoryAdapter;
  private final CategoryAdapter categoryAdapter;
  private final ProductAttachmentAdapter productAttachmentAdapter;

  @Override
  public SearchProductResponse searchProduct(SearchProductRequest request) {
    Page<Product> productPage = productAdapter.getProductByCondition(request);
    if (productPage.isEmpty()) return SearchProductResponse.builder().build();

    List<Product> products = productPage.getContent();
    List<ProductResponse> productResponses =
        ModelMapperUtils.mapList(products, ProductResponse.class);
    enrichInfo(productResponses);

    return SearchProductResponse.builder()
        .data(productResponses)
        .total(productPage.getTotalElements())
        .build();
  }

  @Override
  public ProductResponse findOne(Long productId) {
    Product product = productAdapter.getProductById(productId);
    if (Objects.isNull(product)) {
      throw new InputNotValidException(PRODUCT_NOT_FOUND);
    }
    ProductResponse productResponse = ModelMapperUtils.mapper(product, ProductResponse.class);
    enrichInfo(Collections.singletonList(productResponse));
    return productResponse;
  }

  private void enrichInfo(List<ProductResponse> productResponses) {
    enrichBrand(productResponses);
    enrichCategories(productResponses);
    enrichUrls(productResponses);
  }

  private void enrichCategories(List<ProductResponse> productResponses) {
    List<Long> productIds =
        ModelTransformUtils.getAttribute(productResponses, ProductResponse::getId);

    List<ProductCategory> productCategories =
        productCategoryAdapter.findAllByProductIds(productIds);

    List<Long> categoryIds =
        ModelTransformUtils.getAttribute(productCategories, ProductCategory::getId);

    List<Category> categories = categoryAdapter.findAllByIdIn(categoryIds);

    Map<Long, List<Long>> mapByProducts =
        productCategories.stream()
            .collect(
                Collectors.groupingBy(
                    ProductCategory::getProductId,
                    Collectors.mapping(ProductCategory::getCategoryId, Collectors.toList())));

    for (ProductResponse response : productResponses) {
      List<Long> categoryIdOfProducts =
          mapByProducts.getOrDefault(response.getId(), Collections.emptyList());

      List<Category> categoryOfProducts =
          categories.stream().filter(e -> categoryIdOfProducts.contains(e.getId())).toList();

      if (categoryOfProducts.isEmpty()) continue;
      response.setCategories(categoryOfProducts);
    }
  }

  private void enrichUrls(List<ProductResponse> productResponses) {
    List<Long> productIds =
        ModelTransformUtils.getAttribute(productResponses, ProductResponse::getId);
    List<ProductAttachment> productAttachments =
        productAttachmentAdapter.findAllByProductIds(productIds);

    Map<Long, List<ProductAttachment>> mapProductAttachments =
        productAttachments.stream().collect(Collectors.groupingBy(ProductAttachment::getProductId));

    for (ProductResponse response : productResponses) {
      List<ProductAttachment> attachments =
          mapProductAttachments.getOrDefault(response.getId(), Collections.emptyList());
      response.setAttachments(attachments);
    }
  }

  private void enrichBrand(List<ProductResponse> productResponses) {
    List<Long> productIds =
        ModelTransformUtils.getAttribute(productResponses, ProductResponse::getId);

    List<ProductBrand> productBrands = productBrandAdapter.findAllByProductIds(productIds);

    List<Long> brandIds = ModelTransformUtils.getAttribute(productBrands, ProductBrand::getBrandId);
    List<Brand> allBrands = brandAdapter.findAllByIds(brandIds);

    Map<Long, Long> mapProductIdWithBrandId =
        productBrands.stream()
            .collect(
                Collectors.toMap(
                    ProductBrand::getProductId, ProductBrand::getBrandId, (u1, u2) -> u2));

    Map<Long, Brand> mapBrandWithId = ModelTransformUtils.toMap(allBrands, Brand::getId);

    for (ProductResponse response : productResponses) {
      Long brandId = mapProductIdWithBrandId.getOrDefault(response.getId(), 0L);
      Brand brand = mapBrandWithId.get(brandId);
      if (Objects.isNull(brand)) continue;
      response.setBrand(brand);
    }
  }
}
