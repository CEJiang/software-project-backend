package software.project.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import software.project.project.component.comment.Comment;
import software.project.project.component.comment.CommentService;

@RestController
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @PostMapping("/auth/comment")
    public void addComment(@RequestBody Comment comment){
        commentService.save(comment);
    }

    @GetMapping("/auth/getUserComment/{userID}")
    public ResponseEntity<List<Comment>> getUserComment(@PathVariable("userID") String userID){
        List<Comment> commentList = commentService.getUserComment(userID);

        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/auth/getTypeComment/{type}/{userID}/{createTime}")
    public ResponseEntity<List<Comment>> getTypeComment(@PathVariable("type") String type, @PathVariable("userID") String userID, @PathVariable("createTime") String createTime){
        List<Comment> commentList = commentService.getTypeComment(type, userID, createTime);

        return ResponseEntity.ok(commentList);
    }
}
