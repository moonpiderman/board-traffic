package com.large.board.service.impl;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.mapper.PostSearchMapper;
import com.large.board.service.PostSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class PostSearchServiceImpl implements PostSearchService {
    @Autowired
    private PostSearchMapper postSearchMapper;

    @Cacheable(value = "getPosts", key = "'getPosts' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")
    @Override
    public List<PostDTO> getPosts(PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = null;

        try {
            postDTOList = postSearchMapper.selectPosts(postSearchRequest);
        } catch (Exception e) {
            log.error("selectPosts 메소드 실패! " + e.getMessage());
        }

        return postDTOList;
    }

    @Override
    public List<PostDTO> getPotsByTagName(String tagName) {
        List<PostDTO> postDTOList = null;

        try {
            postDTOList = postSearchMapper.getPostsByTagName(tagName);
        } catch (Exception e) {
            log.error("selectPosts 메소드 실패! " + e.getMessage());
        }

        return postDTOList;
    }
}
