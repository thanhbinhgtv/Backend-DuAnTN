package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.CommentInputDTO;
import duantn.backend.model.dto.output.CommentOutputDTO;
import duantn.backend.model.dto.output.Message;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 28/04/2021
 * Time: 4:50 CH
 */

public interface CommentService {
    //thêm/sửa comment
    CommentOutputDTO insertComment(String email, CommentInputDTO commentInputDTO) throws CustomException;
    //xem comment
    CommentOutputDTO showComment(String email, Integer articleId) throws CustomException;
    //xóa comment
    Message deleteComment(String email, Integer id) throws CustomException;
}
