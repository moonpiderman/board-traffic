package com.large.board.service;

import com.large.board.dto.CommentDTO;
import com.large.board.dto.PostDTO;
import com.large.board.dto.TagDTO;

import java.util.List;

public interface PostService {
    void register(String id, PostDTO postDTO);
    List<PostDTO> getMyPosts(int accountId);
    void updatePosts(PostDTO postDTO);
    void deletePosts(int userId, int postId);

    void registerComment(CommentDTO commentDTO);
    void updateComment(CommentDTO commentDTO);
    void deletePostComment(int userId, int commentId);
    void registerTag(TagDTO tagDTO);
    void updateTag(TagDTO tagDTO);
    void deleteTag(int userId, int tagId);
}
