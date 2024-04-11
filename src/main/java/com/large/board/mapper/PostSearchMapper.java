package com.large.board.mapper;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSearchMapper {
    public List<PostDTO> selectPosts(PostSearchRequest postSearchRequest);

    List<PostDTO> getPostsByTagName(String tagName);
}
