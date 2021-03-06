package duantn.backend.service.impl;

import duantn.backend.dao.*;
import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.*;
import duantn.backend.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleServicelmpl implements ArticleService {
    final
    ArticleReponsitory articleRepository;


    public ArticleServicelmpl(ArticleReponsitory articleRepository) {
        this.articleRepository = articleRepository;
    }

    private SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/mm/dd");


    @Override
    public ResponseEntity<?> insertArticle(ArticleInsertDTO articleInsertDTO) {
        try {

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Article article = modelMapper.map(articleInsertDTO, Article.class);
            Article newArticle = articleRepository.save(article);
            return ResponseEntity.ok(modelMapper.map(newArticle, ArticleOutputDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Insert failed");
        }
    }

    @Override
    public ResponseEntity<?> updateArticle(ArticleUpdateDTO articleUpdateDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Article article = modelMapper.map(articleUpdateDTO, Article.class);
            Article newArticle = articleRepository.save(article);
            return ResponseEntity.ok(modelMapper.map(newArticle, ArticleOutputDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Update failed");
        }
    }

    @Override
    public ResponseEntity<String> deleteArticle(Integer id) {
        Article article = articleRepository.findByArticleId(id);
        if (article == null) {
            return ResponseEntity.badRequest().body("id" + id + "này không tồn tại");
        } else {
            article.setDeleted(true);
            article.setStatus(true);
            articleRepository.save(article);
        }
        return ResponseEntity.ok("bài viết này đã được ẩn ");
    }

    @Override
    public ResponseEntity<String> activeArticle(Integer id) {
        Optional<Article> articles = articleRepository.findById(id);
        if (!articles.isPresent()) {
            return ResponseEntity.badRequest().body("id " + id + " không tồn tại");
        } else {
            articles.get().setDeleted(false);
            articleRepository.save(articles.get());
        }
        return ResponseEntity.ok("bài viết đã được hiển thị");
    }

    @Override
    public ResponseEntity<?> findOneArticle(Integer id) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            return ResponseEntity.ok(modelMapper.map(articleRepository.findByArticleId(id),
                    ArticleOutputDTO.class));
        } catch (Exception e) {
            return ResponseEntity.ok("id: " + id + " không tìm thấy");
        }
    }


    @Override
    public List<ArticleOutputDTO> filterArticle(Boolean status,
                                                Long start, Long end,
                                                Integer wardId, Integer districtId, Integer cityId,
                                                String sort,
                                                Integer page, Integer limit) {
        //find all articles - sort by postTime desc
        List<Article> articles = articleRepository.findAll(Sort.by("postTime").descending());

        //filter by status
        if (status != null) {
            List<Article> articlesStatus = articleRepository.findByStatus(status);
            articles = filter(articlesStatus, articles);
        }
        //filter by startDate - endDate
        if (start != null && end != null) {
            Date startDate = new Date(start);
            Date endDate = new Date(end);
            try {
                List<Article> articlesDate = articleRepository.
                        findByPostTimeGreaterThanEqualAndPostTimeIsLessThanEqual(
                                changeTime(startDate, "00:00:00"),
                                changeTime(endDate, "23:59:59"));
                articles = filter(articlesDate, articles);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //filter by ward - district -city
        if (cityId != null && districtId != null && wardId != null) {
            List<Article> articles1 = articleRepository.
                    findByWard_District_City_CityIdAndWard_District_DistrictIdAndWard_WardId
                            (cityId, districtId, wardId);
            articles = filter(articles1, articles);
        } else if (cityId != null && districtId != null) {
            List<Article> articles1 = articleRepository.
                    findByWard_District_City_CityIdAndWard_District_DistrictId
                            (cityId, districtId);
            articles = filter(articles1, articles);
        } else if (cityId != null) {
            List<Article> articles1 = articleRepository.findByWard_District_City_CityId(cityId);
            articles = filter(articles1, articles);
        }

        //sort
        if (sort != null && sort.equalsIgnoreCase("asc")) {
            Collections.reverse(articles);
        }

        //pageable
        if (page != null && limit != null) {
            articles = pageable(articles, page, limit);
        }

        return convertToDTO(articles);
    }


    public List<ArticleOutputDTO> searchArticle(String search,
                                                Integer page, Integer limit) {
        List<Article> articles = articleRepository.findByTitleLikeOrPhoneLike("%" + search + "%", "%" + search + "%");

        //pageable
        if (page != null && limit != null) {
            articles = pageable(articles, page, limit);
        }

        return convertToDTO(articles);
    }

    private List<ArticleOutputDTO> convertToDTO(List<Article> articles) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<ArticleOutputDTO> articleOutputDTOS = new ArrayList<>();
        for (Article article : articles) {
            articleOutputDTOS.add(modelMapper.map(article, ArticleOutputDTO.class));
        }
        return articleOutputDTOS;
    }

    public List<Article> filter(List<Article> sourceList, List<Article> filterList) {
        List<Article> newList = new ArrayList<>();
        for (Article article : sourceList) {
            if (filterList.contains(article)) newList.add(article);
        }
        return newList;
    }

    public Date changeTime(Date date, String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat1.parse(simpleDateFormat.format(date) + " " + time);
    }

    private List<Article> pageable(List<Article> users, Integer page, Integer limit) {
        List<Article> returnList = new ArrayList<>();
        if (page * limit > users.size() - 1) return returnList;
        int endIndex = Math.min((page + 1) * limit, users.size());
        for (int i = page * limit; i < endIndex; i++) {
            returnList.add(users.get(i));
        }
        return returnList;
    }




}
