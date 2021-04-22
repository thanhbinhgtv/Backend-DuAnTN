package duantn.backend.dao.impl;

import duantn.backend.dao.CustomArticleRepository;
import duantn.backend.model.entity.Article;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Repository
public class CustomArticleRepositoryImpl implements CustomArticleRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Article> findCustom(String sort, Long start, Long end,
                                    Integer ward, Integer district, Integer city,
                                    Boolean roommate,
                                    String status, Boolean vip, String search,
                                    Integer minAcreage, Integer maxAcreage,
                                    Integer page, Integer limit) {
        if(search==null || search.trim().equals("")) search="";

        //tạo builder
        CriteriaBuilder builder = em.getCriteriaBuilder();

        //tạo query
        CriteriaQuery<Article> query = builder.createQuery(Article.class);

        //xác định chủ thể cần truy vấn (=FROM Article)
        Root<Article> root = query.from(Article.class);

        //xác định cột trả về
        query.select(root);

        //search
        Predicate searchByName=builder.like(root.get("customer").get("name"),"%"+search+"%");
        Predicate searchByPhone=builder.like(root.get("customer").get("phone"),"%"+search+"%");
        Predicate searchByEmail=builder.like(root.get("customer").get("email"),"%"+search+"%");
        Predicate searchByTitle=builder.like(root.get("title"), "%"+search+"%");
        searchByTitle=builder.or(searchByTitle, searchByEmail, searchByPhone, searchByName);

        //tìm khoảng thời gian
        if(start !=null){
            Predicate findByGreaterTime = builder.greaterThanOrEqualTo(root.<Date>get("updateTime"), new Date(start));
            searchByTitle= builder.and(searchByTitle, findByGreaterTime);
        }
        if(end !=null){
            Predicate findByLessTime = builder.lessThanOrEqualTo(root.<Date>get("updateTime"), new Date(end));
            searchByTitle= builder.and(searchByTitle, findByLessTime);
        }

        //lọc theo diện tích
        if(minAcreage!=null){
            Predicate findByGreaterAcreage=builder.greaterThanOrEqualTo(root.get("acreage"), minAcreage);
            searchByTitle=builder.and(searchByTitle, findByGreaterAcreage);
        }
        if(maxAcreage!=null){
            Predicate findByLessAcreage=builder.lessThanOrEqualTo(root.get("acreage"), maxAcreage);
            searchByTitle=builder.and(searchByTitle, findByLessAcreage);
        }

        //tìm theo xã, huyện, tỉnh
        if(ward !=null){
            Predicate findByWard = builder.equal(root.get("ward").get("wardId"), ward);
            searchByTitle=builder.and(searchByTitle, findByWard);
        } else if(district!=null){
            Predicate findByDistrict=builder.equal(root.get("ward").get("district").get("districtId"), district);
            searchByTitle=builder.and(searchByTitle, findByDistrict);
        } else if(city !=null){
            Predicate findByCity=builder.equal(root.get("ward").get("district").get("city").get("cityId"), city);
            searchByTitle=builder.and(searchByTitle, findByCity);
        }

        //tìm theo roommate
        if(roommate!=null){
            if(roommate){
                Predicate findByRoommateNotNull=builder.isNotNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNotNull);
            }
            else{
                Predicate findByRoommateNull=builder.isNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNull);
            }
        }

        //tìm theo status
        if(status!=null && !status.trim().equals("")){
            if(status.equals("uncheck")) {
                Predicate findByStatusNull=builder.isNull(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusNull);
            }
            else if(status.equals("active")) {
                Predicate findByStatusTrue= builder.isTrue(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusTrue);
            }
            else if(status.equals("hidden")) {
                Predicate findByStatusFalse= builder.isFalse(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusFalse);
            }
        }

        //tìm theo vip
        if(vip!=null){
            Predicate findByVip=builder.equal(root.get("vip"), vip);
            searchByTitle=builder.and(searchByTitle, findByVip);
        }

        query.where(searchByTitle);

        if(sort!=null && !sort.trim().equals("")){
            if(sort.equals("desc")) query.orderBy(builder.desc(root.get("updateTime")));
            else query.orderBy(builder.asc(root.get("updateTime")));
        }

//        CriteriaBuilder qb = em.getCriteriaBuilder();
//        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//        cq.select(qb.count(cq.from(Article.class)));
//        System.out.println("asddfjj: "+em.createQuery(cq).getSingleResult());

        return em.createQuery(query).setFirstResult(page*limit).setMaxResults(limit).getResultList();
    }

    @Override
    public List<Article> findCustomAndEmail(String email, String sort, Long start, Long end, Integer ward, Integer district, Integer city, Boolean roommate, String status, Boolean vip, String search, Integer minAcreage, Integer maxAcreage,Integer page, Integer limit) {
        if(search==null || search.trim().equals("")) search="";

        //tạo builder
        CriteriaBuilder builder = em.getCriteriaBuilder();

        //tạo query
        CriteriaQuery<Article> query = builder.createQuery(Article.class);

        //xác định chủ thể cần truy vấn (=FROM Article)
        Root<Article> root = query.from(Article.class);

        //xác định cột trả về
        query.select(root);

        //search
        Predicate searchByTitle=builder.like(root.get("title"), "%"+search+"%");

        //phải đúng email
        Predicate findByEmail=builder.like(root.get("customer").get("email"), email);
        searchByTitle=builder.and(searchByTitle, findByEmail);


        //tìm khoảng thời gian
        if(start !=null){
            Predicate findByGreaterTime = builder.greaterThanOrEqualTo(root.<Date>get("updateTime"), new Date(start));
            searchByTitle= builder.and(searchByTitle, findByGreaterTime);
        }
        if(end !=null){
            Predicate findByLessTime = builder.lessThanOrEqualTo(root.<Date>get("updateTime"), new Date(end));
            searchByTitle= builder.and(searchByTitle, findByLessTime);
        }

        //lọc theo diện tích
        if(minAcreage!=null){
            Predicate findByGreaterAcreage=builder.greaterThanOrEqualTo(root.get("acreage"), minAcreage);
            searchByTitle=builder.and(searchByTitle, findByGreaterAcreage);
        }
        if(maxAcreage!=null){
            Predicate findByLessAcreage=builder.lessThanOrEqualTo(root.get("acreage"), maxAcreage);
            searchByTitle=builder.and(searchByTitle, findByLessAcreage);
        }

        //tìm theo xã, huyện, tỉnh
        if(ward !=null){
            Predicate findByWard = builder.equal(root.get("ward").get("wardId"), ward);
            searchByTitle=builder.and(searchByTitle, findByWard);
        } else if(district!=null){
            Predicate findByDistrict=builder.equal(root.get("ward").get("district").get("districtId"), district);
            searchByTitle=builder.and(searchByTitle, findByDistrict);
        } else if(city !=null){
            Predicate findByCity=builder.equal(root.get("ward").get("district").get("city").get("cityId"), city);
            searchByTitle=builder.and(searchByTitle, findByCity);
        }

        //tìm theo roommate
        if(roommate!=null){
            if(roommate){
                Predicate findByRoommateNotNull=builder.isNotNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNotNull);
            }
            else{
                Predicate findByRoommateNull=builder.isNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNull);
            }
        }

        //tìm theo status
        if(status!=null && !status.trim().equals("")){
            if(status.equals("uncheck")) {
                Predicate findByStatusNull=builder.isNull(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusNull);
            }
            else if(status.equals("active")) {
                Predicate findByStatusTrue= builder.isTrue(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusTrue);
            }
            else if(status.equals("hidden")) {
                Predicate findByStatusFalse= builder.isFalse(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusFalse);
            }
        }

        //tìm theo vip
        if(vip!=null){
            Predicate findByVip=builder.equal(root.get("vip"), vip);
            searchByTitle=builder.and(searchByTitle, findByVip);
        }

        query.where(searchByTitle);

        if(sort!=null && !sort.trim().equals("")){
            if(sort.equals("desc")) query.orderBy(builder.desc(root.get("updateTime")));
            else query.orderBy(builder.asc(root.get("updateTime")));
        }

        return em.createQuery(query).setFirstResult(page*limit).setMaxResults(limit).getResultList();
    }

    @Override
    public List<Article> findCustomShow(Long start, Long end,
                                    Integer ward, Integer district, Integer city,
                                    Boolean roommate,
                                    String status, String search,
                                    Integer minAcreage, Integer maxAcreage,
                                    Integer page, Integer limit) {
        if(search==null || search.trim().equals("")) search="";

        //tạo builder
        CriteriaBuilder builder = em.getCriteriaBuilder();

        //tạo query
        CriteriaQuery<Article> query = builder.createQuery(Article.class);

        //xác định chủ thể cần truy vấn (=FROM Article)
        Root<Article> root = query.from(Article.class);

        //xác định cột trả về
        query.select(root);

        //search
        Predicate searchByName=builder.like(root.get("customer").get("name"),"%"+search+"%");
        Predicate searchByPhone=builder.like(root.get("customer").get("phone"),"%"+search+"%");
        Predicate searchByEmail=builder.like(root.get("customer").get("email"),"%"+search+"%");
        Predicate searchByTitle=builder.like(root.get("title"), "%"+search+"%");
        searchByTitle=builder.or(searchByTitle, searchByEmail, searchByPhone, searchByName);

        //tìm khoảng thời gian
        if(start !=null){
            Predicate findByGreaterTime = builder.greaterThanOrEqualTo(root.<Date>get("updateTime"), new Date(start));
            searchByTitle= builder.and(searchByTitle, findByGreaterTime);
        }
        if(end !=null){
            Predicate findByLessTime = builder.lessThanOrEqualTo(root.<Date>get("updateTime"), new Date(end));
            searchByTitle= builder.and(searchByTitle, findByLessTime);
        }

        //lọc theo diện tích
        if(minAcreage!=null){
            Predicate findByGreaterAcreage=builder.greaterThanOrEqualTo(root.get("acreage"), minAcreage);
            searchByTitle=builder.and(searchByTitle, findByGreaterAcreage);
        }
        if(maxAcreage!=null){
            Predicate findByLessAcreage=builder.lessThanOrEqualTo(root.get("acreage"), maxAcreage);
            searchByTitle=builder.and(searchByTitle, findByLessAcreage);
        }

        //tìm theo xã, huyện, tỉnh
        if(ward !=null){
            Predicate findByWard = builder.equal(root.get("ward").get("wardId"), ward);
            searchByTitle=builder.and(searchByTitle, findByWard);
        } else if(district!=null){
            Predicate findByDistrict=builder.equal(root.get("ward").get("district").get("districtId"), district);
            searchByTitle=builder.and(searchByTitle, findByDistrict);
        } else if(city !=null){
            Predicate findByCity=builder.equal(root.get("ward").get("district").get("city").get("cityId"), city);
            searchByTitle=builder.and(searchByTitle, findByCity);
        }

        //tìm theo roommate
        if(roommate!=null){
            if(roommate){
                Predicate findByRoommateNotNull=builder.isNotNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNotNull);
            }
            else{
                Predicate findByRoommateNull=builder.isNull(root.get("roommate"));
                searchByTitle=builder.and(searchByTitle, findByRoommateNull);
            }
        }

        //tìm theo status
        if(status!=null && !status.trim().equals("")){
            if(status.equals("uncheck")) {
                Predicate findByStatusNull=builder.isNull(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusNull);
            }
            else if(status.equals("active")) {
                Predicate findByStatusTrue= builder.isTrue(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusTrue);
            }
            else if(status.equals("hidden")) {
                Predicate findByStatusFalse= builder.isFalse(root.get("deleted"));
                searchByTitle=builder.and(searchByTitle, findByStatusFalse);
            }
        }

        query.where(searchByTitle);

        Order vipDesc=builder.desc(root.get("vip"));
        Order timeDesc=builder.desc(root.get("updateTime"));
        query.orderBy(vipDesc, timeDesc);

        return em.createQuery(query).setFirstResult(page*limit).setMaxResults(limit).getResultList();
    }
}
