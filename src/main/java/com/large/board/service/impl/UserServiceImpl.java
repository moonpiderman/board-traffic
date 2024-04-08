package com.large.board.service.impl;

import com.large.board.dto.UserDTO;
import com.large.board.exception.DuplicateIdException;
import com.large.board.mapper.UserProfileMapper;
import com.large.board.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.large.board.utils.SHA256Util.encryptionPassword;


@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        boolean dupleIdResult = isDuplicatedId(userDTO.getUserId());

        if (dupleIdResult) {
            throw new DuplicateIdException("중복된 아이디가 존재합니다.");
        }

        userDTO.setCreateTime(new Date());
        userDTO.setPassword(encryptionPassword(userDTO.getPassword()));
        int insertCount = userProfileMapper.register(userDTO);

        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userDTO);
            throw new RuntimeException("회원가입에 실패하였습니다.");
        }
    }

    @Override
    public UserDTO login(String id, String password) {
        String cryptoPassword = encryptionPassword(password);
        return userProfileMapper.findByIdAndPassword(id, cryptoPassword);
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
        String cryptoBeforePassword = encryptionPassword(beforePassword);
        UserDTO userDTO = userProfileMapper.findByIdAndPassword(id, cryptoBeforePassword);

        if (userDTO != null) {
            userDTO.setPassword(encryptionPassword(afterPassword));
            int insertCount = userProfileMapper.updatePassword(userDTO);
        } else {
            log.error("updatePassword ERROR! {}", (Object) null);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptoPassword = encryptionPassword(password);
        UserDTO userDTO = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (userDTO != null) {
            int deleteCount = userProfileMapper.deleteUserProfile(id);
        } else {
            log.error("deleteId ERROR! {}", (Object) null);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
