package com.large.board.mapper;

import com.large.board.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {
    int register(TagDTO tagDTO);
    void updateTag(TagDTO tagDTO);
    void deleteTag(int tagId);
    void createPostTag(int tagId, int postId);
}
