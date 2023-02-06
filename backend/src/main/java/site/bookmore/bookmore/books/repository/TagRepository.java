package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.bookmore.bookmore.books.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select distinct t from Tag t join fetch t.reviewTags rt join fetch rt.review where t.id = :id")
    Optional<Tag> findByIdWithReview(Integer id);
    Optional<Tag> findByLabel(String label);
}