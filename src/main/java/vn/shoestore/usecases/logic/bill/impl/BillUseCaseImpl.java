package vn.shoestore.usecases.logic.bill.impl;

import static vn.shoestore.shared.constants.ExceptionMessage.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import vn.shoestore.application.request.CreateBillRequest;
import vn.shoestore.application.response.ProductResponse;
import vn.shoestore.domain.adapter.*;
import vn.shoestore.domain.model.*;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.dto.CustomUserDetails;
import vn.shoestore.shared.enums.BillStatusEnum;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.exceptions.NotAuthorizedException;
import vn.shoestore.shared.utils.AuthUtils;
import vn.shoestore.shared.utils.ModelTransformUtils;
import vn.shoestore.usecases.logic.bill.IBillUseCase;
import vn.shoestore.usecases.logic.product.IGetProductUseCase;

@UseCase
@RequiredArgsConstructor
public class BillUseCaseImpl implements IBillUseCase {
  private final BillAdapter billAdapter;
  private final ProductPropertiesAdapter productPropertiesAdapter;
  private final ImportTicketAdapter importTicketAdapter;
  private final CartAdapter cartAdapter;
  private final ProductAdapter productAdapter;
  private final IGetProductUseCase iGetProductUseCase;

  @Override
  @Transactional
  public void createBill(CreateBillRequest request, Boolean isOnlineTransaction) {
    CustomUserDetails customUserDetails = AuthUtils.getAuthUserDetails();
    if (Objects.isNull(customUserDetails) || Objects.isNull(customUserDetails.getUser())) {
      throw new NotAuthorizedException(NOT_VALID_USER_DETAILS);
    }
    User user = customUserDetails.getUser();
    Cart cart = cartAdapter.getCartByUserId(user.getId());

    if (Objects.isNull(cart) || cart.getProductCarts().isEmpty()) {
      throw new InputNotValidException(CART_STATUS_NOT_VALID);
    }

    List<Long> productInCarts =
        ModelTransformUtils.getAttribute(
            cart.getProductCarts(), ProductCart::getProductPropertiesId);

    List<Long> productPropertiesNotInCarts =
        request.getProductPropertiesIds().stream()
            .filter(e -> !productInCarts.contains(e))
            .toList();

    if (!productPropertiesNotInCarts.isEmpty()) {
      throw new InputNotValidException(PRODUCT_NOT_IN_CART);
    }

    List<ProductCart> productCarts =
        cart.getProductCarts().stream()
            .filter(e -> request.getProductPropertiesIds().contains(e.getProductPropertiesId()))
            .toList();

    List<ProductAmount> productAmounts =
        importTicketAdapter.getAllProductPropertiesIdsForUpdate(request.getProductPropertiesIds());

    List<ProductProperties> allProductProperties =
        productPropertiesAdapter.findAllIdIn(request.getProductPropertiesIds());

    validateAmount(productCarts, productAmounts, allProductProperties);
    // tạo hoá đơn
    extractProductInStorage(productCarts, productAmounts);
    createBill(request, productCarts, allProductProperties, isOnlineTransaction);
  }

  @Override
  public void adminConfirmBill(Long billId) {
    Bill bill = billAdapter.findBillById(billId);
    bill.setStatus(BillStatusEnum.PURCHASE.getStatus());
    billAdapter.saveBill(bill);
  }

  private void validateAmount(
      List<ProductCart> productCarts,
      List<ProductAmount> productAmounts,
      List<ProductProperties> allProductProperties) {
    List<Long> productIds =
        ModelTransformUtils.getAttribute(allProductProperties, ProductProperties::getProductId);

    Map<Long, ProductProperties> mapProductProperties =
        ModelTransformUtils.toMap(allProductProperties, ProductProperties::getId);

    List<Product> products = productAdapter.findAllByIds(productIds);

    Map<Long, Product> mapProducts = ModelTransformUtils.toMap(products, Product::getId);
    Map<Long, ProductAmount> mapProductAmount =
        ModelTransformUtils.toMap(productAmounts, ProductAmount::getProductPropertiesId);

    for (ProductCart productCart : productCarts) {
      ProductAmount productAmount = mapProductAmount.get(productCart.getProductPropertiesId());
      if (Objects.nonNull(productAmount) && productAmount.getAmount() >= productCart.getAmount())
        continue;
      ProductProperties properties = mapProductProperties.get(productCart.getProductPropertiesId());
      Product product =
          mapProducts.get(Objects.nonNull(properties) ? properties.getProductId() : 0L);
      throw new InputNotValidException(
          String.format(
              "Số lượng sản phẩm %s size %s không đủ . Không thể thanh tạo đơn",
              Objects.isNull(product) ? "" : product.getName(),
              Objects.nonNull(properties) ? String.valueOf(properties.getSize()) : ""));
    }
  }

  private void extractProductInStorage(
      List<ProductCart> productCarts, List<ProductAmount> productAmounts) {
    Map<Long, ProductAmount> mapProductAmount =
        ModelTransformUtils.toMap(productAmounts, ProductAmount::getProductPropertiesId);

    for (ProductCart productCart : productCarts) {
      ProductAmount productAmount = mapProductAmount.get(productCart.getProductPropertiesId());
      if (productAmount == null) continue;

      productAmount.setAmount(Math.max(productAmount.getAmount() - productCart.getAmount(), 0));
    }

    importTicketAdapter.saveProductAmount(productAmounts);
  }

  void createBill(
      CreateBillRequest request,
      List<ProductCart> productCarts,
      List<ProductProperties> allProductProperties,
      Boolean isOnlineTransaction) {
    CustomUserDetails customUserDetails = AuthUtils.getAuthUserDetails();
    List<Long> productIds =
        ModelTransformUtils.getAttribute(allProductProperties, ProductProperties::getProductId);
    List<ProductResponse> productResponses = iGetProductUseCase.findByIds(productIds);

    Map<Long, ProductProperties> mapProductProperties =
        ModelTransformUtils.toMap(allProductProperties, ProductProperties::getId);

    Map<Long, ProductResponse> mapProductResponse =
        ModelTransformUtils.toMap(productResponses, ProductResponse::getId);

    User user = customUserDetails.getUser();

    Bill bill =
        Bill.builder()
            .createdBy(user.getId())
            .userId(user.getId())
            .createdDate(LocalDateTime.now())
            .status(BillStatusEnum.CREATED.getStatus())
            .isOnlineTransaction(isOnlineTransaction)
            .address(request.getAddress())
            .phoneNumber(request.getPhoneNumber())
            .build();

    Bill savedBill = billAdapter.saveBill(bill);

    List<ProductBill> productBills = new ArrayList<>();

    for (ProductCart productCart : productCarts) {
      ProductProperties properties = mapProductProperties.get(productCart.getProductPropertiesId());
      if (properties == null) continue;
      ProductResponse productResponse = mapProductResponse.get(properties.getProductId());
      if (productResponse == null) continue;
      productBills.add(
          ProductBill.builder()
              .billId(savedBill.getId())
              .productPropertiesId(properties.getId())
              .amount(productCart.getAmount())
              .price(productResponse.getPrice())
              .promotionPrice(productResponse.getPromotionPrice())
              .promotionId(productResponse.getPromotionId())
              .build());
    }

    billAdapter.saveProductBill(productBills);
    Double total =
        productBills.stream()
            .filter(e -> Objects.nonNull(e.getTotalPrice()))
            .mapToDouble(ProductBill::getTotalPrice)
            .sum();

    savedBill.setTotal(total);
    billAdapter.saveBill(savedBill);
    cartAdapter.deleteProductCarts(
        ModelTransformUtils.getAttribute(productCarts, ProductCart::getId));
  }
}
