package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.service.RanksService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class RanksController {
    private final RanksService ranksService;

    @GetMapping("/ranks")
    public ResultResponse<Page<RanksResponse>> findRanks(Pageable pageable, Authentication authentication) {

        return ResultResponse.success(ranksService.findRanks(pageable, authentication.getName()));
    }

}