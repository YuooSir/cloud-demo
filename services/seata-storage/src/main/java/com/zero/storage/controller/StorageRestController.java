package com.zero.storage.controller;


import com.zero.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageRestController {

  @Autowired
  StorageService storageService;

  @PostMapping("/deduct")
  public String deduct(@RequestParam("commodityCode") String commodityCode,
      @RequestParam("count") Integer count) {
    storageService.deduct(commodityCode, count);
    return "storage deduct success";
  }
}
