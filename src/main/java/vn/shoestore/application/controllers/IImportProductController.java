package vn.shoestore.application.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shoestore.application.request.ImportProductRequest;
import vn.shoestore.application.response.BaseResponse;

@RestController
@RequestMapping("/api/v1/import/")
public interface IImportProductController {
  @PostMapping("/save-or-update-ticket")
  ResponseEntity<BaseResponse> saveOrUpdateTicket(@RequestBody @Valid ImportProductRequest request);
}
