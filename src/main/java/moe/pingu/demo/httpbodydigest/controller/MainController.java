package moe.pingu.demo.httpbodydigest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import moe.pingu.demo.httpbodydigest.pojo.RegForm;
import moe.pingu.demo.httpbodydigest.pojo.RegResult;

@RestController
public class MainController {

  @PostMapping("/")
  public RegResult handleReg(@RequestBody RegForm form) {
    return new RegResult("DONE");
  }

}
