package duantn.backend.controller.customer;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.CommentInputDTO;
import duantn.backend.model.dto.output.CommentOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 28/04/2021
 * Time: 5:37 CH
 */

@RestController
@RequestMapping("/customer")
public class CommentManager {
    final
    CommentService commentService;

    public CommentManager(CommentService commentService) {
        this.commentService = commentService;
    }

    //thêm/sửa comment
    @PostMapping("/comment/add-update")
    CommentOutputDTO insertComment(HttpServletRequest request,
                                   @Valid @RequestBody CommentInputDTO commentInputDTO) throws CustomException{
        String email= (String) request.getAttribute("email");
        return commentService.insertComment(email, commentInputDTO);
    }
    //xem comment
    @GetMapping("/comment/show")
    CommentOutputDTO showComment(HttpServletRequest request,
                                 @RequestParam("article-id") Integer articleId) throws CustomException{
        String email= (String) request.getAttribute("email");
        return commentService.showComment(email, articleId);
    }
    //xóa comment
    @DeleteMapping("/comment/{id}")
    Message deleteComment(HttpServletRequest request,
                          @PathVariable Integer id) throws CustomException{
        String email= (String) request.getAttribute("email");
        return commentService.deleteComment(email, id);
    }
}
