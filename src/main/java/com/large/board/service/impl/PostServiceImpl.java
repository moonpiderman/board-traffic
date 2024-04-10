package com.large.board.service.impl;

import com.large.board.dto.PostDTO;
import com.large.board.dto.UserDTO;
import com.large.board.mapper.PostMapper;
import com.large.board.mapper.UserProfileMapper;
import com.large.board.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2

public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if (memberInfo != null) {
            postMapper.register(postDTO);
        } else {
            log.error("register error! {}", postDTO);
            throw new RuntimeException("게시글 등록에 실패하였습니다." + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        return postMapper.selectMyPosts(accountId);
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if (postDTO != null && postDTO.getId() != 0) {
            postMapper.updatePosts(postDTO);
        } else {
            log.error("update error!");
            throw new RuntimeException("게시글 수정에 실패하였습니다.");
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if (userId != 0 && postId != 0) {
            postMapper.deletePosts(postId);
        } else {
            log.error("delete error! {}", postId);
            throw new RuntimeException("게시글 삭제에 실패하였습니다." + postId);
        }
    }
}
