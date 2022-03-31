package de.predic8.catalogue.repository;

import de.predic8.catalogue.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
