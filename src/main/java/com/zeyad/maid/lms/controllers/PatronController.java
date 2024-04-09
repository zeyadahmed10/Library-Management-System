package com.zeyad.maid.lms.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patrons")
@Tag(name = "Patrons Controller", description = "APIs for patrons management")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PatronController {


}
