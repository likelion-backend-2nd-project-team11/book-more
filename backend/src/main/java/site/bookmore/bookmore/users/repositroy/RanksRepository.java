package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.List;
import java.util.Optional;

public interface RanksRepository extends JpaRepository<Ranks, Long> {

    @Query(value = "SELECT r.id,r.point, (Rank() over(order by r.point desc)) as ranking FROM ranks r WHERE r.ranking > -1", nativeQuery = true)
    List<RanksNativeVo> findAllRanking();

    @Query("select r from Ranks r join fetch r.user where r.ranking <= 100 and r.ranking > -1 order by r.ranking asc")
    List<Ranks> findTop100ByOrderByRankingAsc();

    Optional<Ranks> findTop1ByOrderByRankingDesc();

    Optional<Ranks> findByUser(User user);
}