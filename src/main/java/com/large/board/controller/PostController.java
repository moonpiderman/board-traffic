package com.large.board.controller;

import com.large.board.aop.LoginCheck;
import com.large.board.dto.PostDTO;
import com.large.board.dto.UserDTO;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.impl.PostServiceImpl;
import com.large.board.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Log4j2
public class PostController {

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    public PostController(UserServiceImpl userService, PostServiceImpl postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(
            String accountId,
            PostDTO postDTO
    ) {
        postService.register(accountId, postDTO);
        CommonResponse<PostDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPost", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/my-posts")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> getMyPosts(
            String accountId
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<PostDTO>  postDTOList = postService.getMyPosts(memberInfo.getId());
        CommonResponse<List<PostDTO>> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "getMyPosts", postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("{postId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostRequest>> updatePosts(
            String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostRequest postRequest
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContent())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(memberInfo.getId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date())
                .build();
        postService.updatePosts(postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deleteposts(
            String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostDeleteRequest postDeleteRequest
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(memberInfo.getId(), postId);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deleteposts", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    @Getter
    @AllArgsConstructor
    private static class PostResponse {
        private List<PostDTO> postDTOList;
    }

    @Setter
    @Getter
    private static class PostRequest {
        private String name;
        private String content;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    @Setter
    @Getter
    private static class PostDeleteRequest {
        private int id;
        private int accountId;
    }

}
