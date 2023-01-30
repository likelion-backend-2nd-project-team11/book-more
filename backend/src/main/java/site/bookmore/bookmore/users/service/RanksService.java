package site.bookmore.bookmore.users.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.books.repository.LikesRepository;
import site.bookmore.bookmore.books.repository.ReviewRepository;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.entity.PointSave;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class RanksService {
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final ReviewRepository reviewRepository;
    private final RanksRepository ranksRepository;


    public Page<RanksResponse> findRanks(Pageable pageable, String email) {
        userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        List<User> users = userRepository.findAll();
        List<PointSave> pointSaves = new ArrayList<>();


        for (User user : users) {
            Integer likeSum = reviewRepository.findSum(user.getId());
            if (likeSum == null) {
                likeSum = 0;
            }
            pointSaves.add(new PointSave(user.getId(), likeSum));
            log.info("likeSum : {}", likeSum);
        }
        for (PointSave pointSave : pointSaves) {
            ranksRepository.save(new Ranks(pointSave.getId(), pointSave.getPoint()));
        }

        // ranking 가져오기
        List<RanksNativeVo> rankList = ranksRepository.findAllRanking();

        for (int i = 0; i < rankList.size(); i++) {
            Long ranking = rankList.get(i).getRanking();
            log.info("ranking : {}", ranking);
            ranksRepository.save(new Ranks(rankList.get(i).getId(), rankList.get(i).getPoint(), ranking));
        }

        return ranksRepository.findAllRanks(pageable).map(ranks -> new RanksResponse(ranks));

    }
}