package site.bookmore.bookmore.users.repositroy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.List;

public interface RanksRepository extends JpaRepository<Ranks, Long> {


    @Query(value = "SELECT r.id,r.point, (Rank() over(order by r.point desc)) as ranking FROM ranks r", nativeQuery = true)
    Page<Ranks> findAllRanks(Pageable pageable);

    @Query(value = "SELECT r.id,r.point, (Rank() over(order by r.point desc)) as ranking FROM ranks r", nativeQuery = true)
    List<RanksNativeVo> findAllRanking();

}