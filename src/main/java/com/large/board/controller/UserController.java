package com.large.board.controller;

import com.large.board.aop.LoginCheck;
import com.large.board.dto.UserDTO;
import com.large.board.dto.request.UserDeleteId;
import com.large.board.dto.request.UserLoginRequest;
import com.large.board.dto.request.UserUpdatePasswordRequest;
import com.large.board.dto.response.LoginResponse;
import com.large.board.dto.response.UserInfoResponse;
import com.large.board.service.impl.UserServiceImpl;
import com.large.board.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static LoginResponse loginResponse;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(
            @RequestBody UserDTO userDTO
    ) {
        if (UserDTO.hasNullDataBeforeSignUp(userDTO)) {
            throw new IllegalArgumentException("회원가입에 필요한 정보가 부족합니다.");
        }
        userService.registerUser(userDTO);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus login(
            @RequestBody UserLoginRequest userLoginRequest,
            HttpSession session
    ) {
        ResponseEntity<LoginResponse> response = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == UserDTO.Status.ADMIN) {
                SessionUtil.setLoginAdminId(session, userInfo.getUserId());
            } else {
                SessionUtil.setLoginMemberId(session, userInfo.getUserId());
            }

            response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("로그인에 실패하였습니다.");
        }

        return HttpStatus.OK;
    }

    @GetMapping("/my-info")
    public UserInfoResponse memberInfo(
            HttpSession session
    ) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) id = SessionUtil.getLoginAdminId(session);
        UserDTO userInfo = userService.getUserInfo(id);

        return new UserInfoResponse(userInfo);
    }

    @PutMapping("/logout")
    public void logout(
            HttpSession session
    ) {
        SessionUtil.clear(session);
    }

    @PatchMapping("/password")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
            HttpSession session
    ) {
        ResponseEntity<LoginResponse> response = null;
        String id = SessionUtil.getLoginMemberId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            response = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("비밀번호 변경 실패! {}", e.getMessage());
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @DeleteMapping("")
    public ResponseEntity<LoginResponse> deleteUser(
            @RequestBody UserDeleteId userDeleteId,
            HttpSession session
    ) {
        ResponseEntity<LoginResponse> response = null;
        String id = userDeleteId.getId();
        String password = userDeleteId.getPassword();

        try {
            userService.deleteId(id, password);
            SessionUtil.clear(session);
            response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("회원탈퇴 실패! {}", e.getMessage());
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

}
