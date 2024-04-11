package com.large.board.mapper;

import com.large.board.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    int register(CommentDTO commentDTO);
    void updateComment(CommentDTO commentDTO);
    void deleteComment(int commentId);
}
