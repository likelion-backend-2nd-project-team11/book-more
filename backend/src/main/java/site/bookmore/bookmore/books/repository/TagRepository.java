package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByLabel(String label);
}