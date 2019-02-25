package de.predi8.catalogue.repository;

import de.predi8.catalogue.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
