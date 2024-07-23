package vn.shoestore.usecases.logic.import_product.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import vn.shoestore.application.request.ImportProductRequest;
import vn.shoestore.domain.adapter.ImportTicketAdapter;
import vn.shoestore.domain.adapter.ProductPropertiesAdapter;
import vn.shoestore.domain.model.ImportTicket;
import vn.shoestore.domain.model.ImportTicketProduct;
import vn.shoestore.domain.model.ProductProperties;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.dto.CustomUserDetails;
import vn.shoestore.shared.dto.ProductPropertiesAmountDTO;
import vn.shoestore.shared.enums.ImportTicketEnum;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.utils.AuthUtils;
import vn.shoestore.shared.utils.ModelMapperUtils;
import vn.shoestore.shared.utils.ModelTransformUtils;
import vn.shoestore.usecases.logic.import_product.ImportProductUseCase;

import java.time.LocalDateTime;
import java.util.*;
import static vn.shoestore.shared.constants.ExceptionMessage.TICKET_STATUS_NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class ImportProductUseCaseImpl implements ImportProductUseCase {
  private final ImportTicketAdapter importTicketAdapter;
  private final ProductPropertiesAdapter productPropertiesAdapter;

  @Override
  @Transactional
  public void saveOrUpdateImportTicket(ImportProductRequest request) {
    ImportTicket importTicket = ModelMapperUtils.mapper(request, ImportTicket.class);
    CustomUserDetails userDetails = AuthUtils.getAuthUserDetails();

    importTicket.setCreatedUserId(userDetails.getUser().getId());
    importTicket.setCreatedDate(LocalDateTime.now());
    importTicket.setStatus(ImportTicketEnum.DRAFT.getStatus());

    ImportTicket savedTicket = importTicketAdapter.saveImportTicket(importTicket);

    if (Objects.nonNull(request.getId())) {
      validateStatus(request.getId());
      deleteImportTicketProducts(savedTicket.getId());
    }

    List<ProductPropertiesAmountDTO> amountDTOS = request.getProductAmounts();
    List<Long> productIds =
        ModelTransformUtils.getAttribute(amountDTOS, ProductPropertiesAmountDTO::getProductId)
            .stream()
            .distinct()
            .toList();

    List<Integer> sizes =
        ModelTransformUtils.getAttribute(amountDTOS, ProductPropertiesAmountDTO::getSize).stream()
            .distinct()
            .toList();

    List<ProductProperties> productProperties =
        productPropertiesAdapter.findAllByProductIdInAndSizeInAndIsAble(productIds, sizes, true);
    List<ImportTicketProduct> products = new ArrayList<>();

    for (ProductPropertiesAmountDTO dto : amountDTOS) {
      Optional<ProductProperties> optionalProductProperties =
          productProperties.stream()
              .filter(
                  e ->
                      Objects.equals(e.getProductId(), dto.getProductId())
                          && Objects.equals(e.getSize(), dto.getSize()))
              .findFirst();
      if (optionalProductProperties.isEmpty()) continue;
      products.add(
          ImportTicketProduct.builder()
              .productPropertiesId(savedTicket.getId())
              .importCost(dto.getImportCost())
              .amount(dto.getAmount())
              .ticketId(savedTicket.getId())
              .build());
    }
    importTicketAdapter.saveAllImportTicketProduct(products);
  }

  private void deleteImportTicketProducts(Long ticketId) {
    List<ImportTicketProduct> existProducts =
        importTicketAdapter.findAllByTicketIdIn(Collections.singletonList(ticketId));

    if (existProducts.isEmpty()) return;

    List<Long> ids = ModelTransformUtils.getAttribute(existProducts, ImportTicketProduct::getId);
    importTicketAdapter.deleteImportTicketProductByIds(ids);
  }

  private void validateStatus(Long ticketId) {
    ImportTicket importTicket = importTicketAdapter.getTicketById(ticketId);
    if (Objects.equals(importTicket.getStatus(), ImportTicketEnum.DONE.getStatus())) {
      throw new InputNotValidException(TICKET_STATUS_NOT_FOUND);
    }
  }
}
