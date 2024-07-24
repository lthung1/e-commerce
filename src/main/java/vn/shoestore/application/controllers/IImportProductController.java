package vn.shoestore.application.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.shoestore.application.request.ConfirmImportTicketRequest;
import vn.shoestore.application.request.ImportProductRequest;
import vn.shoestore.application.response.BaseResponse;

@RestController
@RequestMapping("/api/v1/import/")
public interface IImportProductController {
  @PostMapping("/save-or-update-ticket")
  ResponseEntity<BaseResponse> saveOrUpdateTicket(@RequestBody @Valid ImportProductRequest request);

  @PutMapping("/confirm")
  ResponseEntity<BaseResponse> confirmImportTicket(
      @RequestBody @Valid ConfirmImportTicketRequest request);

  @DeleteMapping("{id}")
  ResponseEntity<BaseResponse> deleteTicket(@PathVariable Long id);
}
