package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.CommentRepository;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.model.dto.input.CommentInputDTO;
import duantn.backend.model.dto.output.CommentOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Article;
import duantn.backend.model.entity.Comment;
import duantn.backend.model.entity.Customer;
import duantn.backend.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import java.util.*;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 28/04/2021
 * Time: 5:00 CH
 */

@Service
public class CommentServiceImpl implements CommentService {
    final
    ArticleRepository articleRepository;
    final
    CustomerRepository customerRepository;
    final
    CommentRepository commentRepository;

    public CommentServiceImpl(ArticleRepository articleRepository, CustomerRepository customerRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.customerRepository = customerRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentOutputDTO insertComment(String email, CommentInputDTO commentInputDTO) throws CustomException {
        Article article = articleRepository.findByArticleIdAndDeletedFalse(commentInputDTO.getArticleId());
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new CustomException("Khách hàng không hợp lệ");
        if (article == null) throw new CustomException("Bài đăng bạn comment không tồn tại");
        if (article.getCustomer().getEmail().equals(email))
            throw new CustomException("Bạn không được tự nhận xét bài đăng của chính mình");

        Comment comment = commentRepository.findByArticleAndCustomer(article, customer);
        if (comment == null) {
            comment = new Comment();
            comment.setArticle(article);
            comment.setCustomer(customer);
        }
        comment.setComment(commentInputDTO.getComment());
        comment.setStart(commentInputDTO.getStart());

        Comment newComment = commentRepository.save(comment);
        return convertToOutputDTO(newComment);
    }

    @Override
    public CommentOutputDTO showComment(String email, Integer articleId) throws CustomException {
        Article article = articleRepository.findByArticleIdAndDeletedFalse(articleId);
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new CustomException("Khách hàng không hợp lệ");
        if (article == null) throw new CustomException("Bài đăng bạn comment không tồn tại");
        Comment comment = commentRepository.findByArticleAndCustomer(article, customer);
        if (comment == null) return new CommentOutputDTO();
        else return convertToOutputDTO(comment);
    }

    @Override
    public Message deleteComment(String email, Integer id) throws CustomException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) throw new CustomException("Khách hàng không hợp lệ");
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isPresent()){
            Comment comment= optionalComment.get();
            if(!email.equals(comment.getCustomer().getEmail()))
                throw new CustomException("Bạn không được xóa comment của người khác");
            commentRepository.delete(comment);
            return new Message("Xóa comment thành công");
        } else throw new CustomException("Không tìm thấy comment");
    }

    @Override
    public Map<String, Object> listComment(Integer articleId, Integer page, Integer limit) throws CustomException {
        Page<Comment> commentPage=commentRepository.findByArticle_ArticleId(articleId,
                PageRequest.of(page, limit, Sort.by("timeCreated").descending()));
        List<CommentOutputDTO> commentOutputDTOS=new ArrayList<>();
        Integer sum=0;
        Integer index=0;
        for (Comment comment: commentPage.toList()){
            commentOutputDTOS.add(convertToOutputDTO(comment));
            sum+=comment.getStart();
            index++;
        }
        Double avgStar=((double) sum)/((double) index);
        avgStar=Math.round(avgStar*10)/10.0;

        Map<String, Object> map=new HashMap<>();
        map.put("avgStar", avgStar);
        map.put("elements", commentPage.getTotalElements());
        map.put("pages", commentPage.getTotalPages());
        map.put("data", commentOutputDTOS);

        return map;
    }

    private CommentOutputDTO convertToOutputDTO(Comment comment) {
        CommentOutputDTO commentOutputDTO = new CommentOutputDTO();
        commentOutputDTO.setCommentId(comment.getCommentId());
        commentOutputDTO.setComment(comment.getComment());
        commentOutputDTO.setStart(comment.getStart());
        commentOutputDTO.setTime(comment.getTimeCreated().getTime());

        Map<String, Object> customer = new HashMap<>();
        customer.put("customerId", comment.getCustomer().getCustomerId());
        customer.put("name", comment.getCustomer().getName());
        customer.put("email", comment.getCustomer().getEmail());
        customer.put("image", comment.getCustomer().getImage());
        commentOutputDTO.setCustomer(customer);

        Map<String, Object> article = new HashMap<>();
        article.put("articleId", comment.getArticle().getArticleId());
        article.put("title", comment.getArticle().getTitle());
        article.put("image", comment.getArticle().getImage());
        commentOutputDTO.setArticle(article);

        return commentOutputDTO;
    }
}
