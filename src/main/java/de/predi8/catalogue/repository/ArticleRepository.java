package de.predi8.catalogue.repository;

import de.predi8.catalogue.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
