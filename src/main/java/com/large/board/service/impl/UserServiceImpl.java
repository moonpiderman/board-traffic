package com.large.board.service.impl;

import com.large.board.dto.UserDTO;
import com.large.board.exception.DuplicateIdException;
import com.large.board.mapper.UserProfileMapper;
import com.large.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.large.board.utils.SHA256Util.encryptionPassword;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void registerUser(UserDTO userProfile) {
        boolean duplIdResult = isDuplicatedId(userProfile.getUserId());

        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디가 존재합니다.");
        }

        userProfile.setCreateTime(new Date());
        userProfile.setPassword(encryptionPassword(userProfile.getPassword()));
    }

    @Override
    public UserDTO login(String id, String password) {
        return null;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.checkId(id);
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        return null;
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {

    }

    @Override
    public void deleteId(String id, String password) {

    }
}
