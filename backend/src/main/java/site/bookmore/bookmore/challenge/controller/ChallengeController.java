package site.bookmore.bookmore.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.challenge.dto.ChallengeDetailResponse;
import site.bookmore.bookmore.challenge.dto.ChallengeRequest;
import site.bookmore.bookmore.challenge.dto.ChallengeResponse;
import site.bookmore.bookmore.challenge.service.ChallengeService;
import site.bookmore.bookmore.common.dto.ResultResponse;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping()
    public ResultResponse<ChallengeResponse> addChallenge(Authentication authentication, @RequestBody ChallengeRequest challengeRequest) {
        String email = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.add(email, challengeRequest);
        return ResultResponse.success(challengeResponse);
    }

    @PutMapping("/{id}")
    public ResultResponse<ChallengeResponse> modifyChallenge(Authentication authentication, @RequestBody ChallengeRequest challengeRequest, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.modify(userName, id, challengeRequest);
        return ResultResponse.success(challengeResponse);
    }

    @DeleteMapping("/{id}")
    public ResultResponse<ChallengeResponse> deleteChallenge(Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.delete(userName, id);
        return ResultResponse.success(challengeResponse);
    }

    @GetMapping("/{id}")
    public ResultResponse<ChallengeDetailResponse> getChallenge(Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeDetailResponse challengeDetailResponse = challengeService.get(userName, id);
        return ResultResponse.success(challengeDetailResponse);
    }

    @GetMapping
    public ResultResponse<Page<ChallengeDetailResponse>> listChallenge(Authentication authentication,@PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = authentication.getName();
        Page<ChallengeDetailResponse> challengeListResponses = challengeService.list(pageable,userName);
        return ResultResponse.success(challengeListResponses);
    }
}
