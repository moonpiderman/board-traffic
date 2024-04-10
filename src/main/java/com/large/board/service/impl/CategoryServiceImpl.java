package com.large.board.service.impl;

import com.large.board.dto.CategoryDTO;
import com.large.board.mapper.CategoryMapper;
import com.large.board.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if (accountId != null) {
            categoryMapper.register(categoryDTO);
        } else {
            log.error("register error! {}", categoryDTO);
            throw new RuntimeException("카테고리 등록에 실패하였습니다." + categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) {
            categoryMapper.updateCategory(categoryDTO);
        } else {
            log.error("update error!");
            throw new RuntimeException("카테고리 수정에 실패하였습니다.");
        }
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) {
            categoryMapper.deleteCategory(categoryId);
        } else {
            log.error("delete error! {}", categoryId);
            throw new RuntimeException("카테고리 삭제에 실패하였습니다." + categoryId);
        }
    }
}
