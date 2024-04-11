package com.large.board.service.impl;

import com.large.board.dto.CommentDTO;
import com.large.board.dto.PostDTO;
import com.large.board.dto.TagDTO;
import com.large.board.dto.UserDTO;
import com.large.board.mapper.CommentMapper;
import com.large.board.mapper.PostMapper;
import com.large.board.mapper.TagMapper;
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
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if (memberInfo != null) {
            postMapper.register(postDTO);
            int postId = postDTO.getId();
            for(int i=0; i<postDTO.getTagDTOList().size(); i++) {
                TagDTO tagDTO = postDTO.getTagDTOList().get(i);
                tagMapper.register(tagDTO);
                int tagId = tagDTO.getId();
                tagMapper.createPostTag(tagId, postId);
            }
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

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if (commentDTO.getPostId() != 0) {
            commentMapper.register(commentDTO);
        } else {
            log.error("register error! {}", commentDTO);
            throw new RuntimeException("댓글 등록에 실패하였습니다." + commentDTO);
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            commentMapper.updateComment(commentDTO);
        } else {
            log.error("register error!");
            throw new RuntimeException("댓글 업데이트에 실패하였습니다.");
        }
    }

    @Override
    public void deletePostComment(int userId, int commentId) {
        if (userId != 0 && commentId != 0) {
            commentMapper.deleteComment(commentId);
        } else {
            log.error("delete error! {}", commentId);
            throw new RuntimeException("댓글 삭제에 실패하였습니다." + commentId);
        }
    }

    @Override
    public void registerTag(TagDTO tagDTO) {
        if (tagDTO != null) {
            tagMapper.register(tagDTO);
        } else {
            log.error("register error!");
            throw new RuntimeException("태그 등록에 실패하였습니다.");
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if (tagDTO != null) {
            tagMapper.register(tagDTO);
        } else {
            log.error("register error!");
            throw new RuntimeException("태그 업데이트에 실패하였습니다.");
        }
    }

    @Override
    public void deleteTag(int userId, int tagId) {
        if (userId != 0 && tagId != 0) {
            tagMapper.deleteTag(tagId);
        } else {
            log.error("delete error! {}", tagId);
            throw new RuntimeException("태그 삭제에 실패하였습니다." + tagId);
        }
    }
}
