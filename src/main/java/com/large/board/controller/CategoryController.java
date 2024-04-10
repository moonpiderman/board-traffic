package com.large.board.controller;

import com.large.board.aop.LoginCheck;
import com.large.board.dto.CategoryDTO;
import com.large.board.service.impl.CategoryServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Log4j2
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(userType = LoginCheck.UserType.ADMIN)
    public void registerCategory(
            String accountId,
            @RequestBody CategoryDTO categoryDTO
    ) {
        categoryService.register(accountId, categoryDTO);
    }

    @PatchMapping("/{category-id}")
    @LoginCheck(userType = LoginCheck.UserType.ADMIN)
    public void updateCategory(
            @PathVariable("category-id") int categoryId,
            @RequestBody CategoryRequest categoryRequest
    ) {
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), CategoryDTO.SortStatus.NEWEST, 10, 10);
        categoryService.update(categoryDTO);
    }

    @DeleteMapping("/{category-id}")
    @LoginCheck(userType = LoginCheck.UserType.ADMIN)
    public void deleteCategory(
            @PathVariable("category-id") int categoryId
    ) {
        categoryService.delete(categoryId);
    }

    @Getter
    @Setter
    private static class CategoryRequest {
        private int id;
        private String name;
    }
}
