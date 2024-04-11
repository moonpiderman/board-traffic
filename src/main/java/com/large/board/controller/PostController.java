package com.large.board.controller;

import com.large.board.aop.LoginCheck;
import com.large.board.dto.CommentDTO;
import com.large.board.dto.PostDTO;
import com.large.board.dto.TagDTO;
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

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> registerPostComment(
            String accountId,
            @RequestBody CommentDTO commentDTO
    ) {
        postService.registerComment(commentDTO);
        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("/comments/{commentId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updateComment(
            String accountId,
            @PathVariable(name = "commentId") int commentId,
            @RequestBody CommentDTO commentDTO
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if (memberInfo != null) {
            postService.updateComment(commentDTO);
        }

        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updateComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/comments/{commentId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostComment(
            String accountId,
            @PathVariable(name = "commentId") int commentId,
            @RequestBody CommentDTO commentDTO
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if (memberInfo != null) {
            postService.deletePostComment(memberInfo.getId(), commentId);
        }

        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> registerTag(
            String accountId,
            @RequestBody TagDTO tagDTO
    ) {
        postService.registerTag(tagDTO);
        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("/tags/{tagId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> updatePostTag(
            String accountId,
            @PathVariable(name = "tagId") int tagId,
            @RequestBody TagDTO tagDTO
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if (memberInfo != null) {
            postService.updateTag(tagDTO);
        }

        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/tags/{tagId}")
    @LoginCheck(userType = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> deleteTag(
            String accountId,
            @PathVariable(name = "tagId") int tagId,
            @RequestBody TagDTO tagDTO
    ) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if (memberInfo != null) {
            postService.deleteTag(memberInfo.getId(), tagId);
        }

        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deleteTag", tagDTO);
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
