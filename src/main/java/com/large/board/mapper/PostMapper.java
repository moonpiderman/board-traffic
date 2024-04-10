package com.large.board.mapper;

import com.large.board.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int register(PostDTO postDTO);
    List<PostDTO> selectMyPosts(int accountId);
    void updatePosts(PostDTO postDTO);
    void deletePosts(int postId);
}
