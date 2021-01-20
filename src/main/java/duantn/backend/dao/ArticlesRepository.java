package duantn.backend.dao;

import duantn.backend.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Integer> {
    @Query("FROM Articles a WHERE a.title LIKE %:title%")
    public List<Articles> findByTitle(@Param("title") String title);

    @Query("")
    public List<Articles> sortAsc();
}

