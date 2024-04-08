package com.large.board.mapper;

import com.large.board.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    public UserDTO getUserProfile(@Param("id") String id);

    int insertUserProfile(@Param("id") String id,
                          @Param(("password")) String password,
                          @Param("name") String name,
                          @Param("createTime") String createTime,
                          @Param("updateTime") String updateTime
    );

    int deleteUserProfile(@Param("id") String id);

    public int register(UserDTO userDTO);

    public UserDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    int idCheck(String id);

    public int updatePassword(UserDTO user);
}
