package site.bookmore.bookmore.challenge.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import site.bookmore.bookmore.common.support.annotation.Authorized;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "3-챌린지")
@RequestMapping("api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Authorized
    @ApiOperation(value = "작성")
    @PostMapping()
    public ResultResponse<ChallengeResponse> addChallenge(@ApiIgnore Authentication authentication, @RequestBody @Valid ChallengeRequest challengeRequest) {
        String email = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.add(email, challengeRequest);
        return ResultResponse.success(challengeResponse);
    }

    @Authorized
    @ApiOperation(value = "수정")
    @PatchMapping("/{id}")
    public ResultResponse<ChallengeResponse> modifyChallenge(@ApiIgnore Authentication authentication, @RequestBody @Valid ChallengeRequest challengeRequest, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.modify(userName, id, challengeRequest);
        return ResultResponse.success(challengeResponse);
    }

    @ApiOperation(value = "삭제")
    @Authorized
    @DeleteMapping("/{id}")
    public ResultResponse<ChallengeResponse> deleteChallenge(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeResponse challengeResponse = challengeService.delete(userName, id);
        return ResultResponse.success(challengeResponse);
    }

    @Authorized
    @ApiOperation(value = "상세 조회")
    @GetMapping("/{id}")
    public ResultResponse<ChallengeDetailResponse> getChallenge(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        ChallengeDetailResponse challengeDetailResponse = challengeService.get(userName, id);
        return ResultResponse.success(challengeDetailResponse);
    }

    @Authorized
    @ApiOperation(value = "리스트 조회")
    @GetMapping
    public ResultResponse<Page<ChallengeDetailResponse>> listChallenge(Authentication authentication, @PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = authentication.getName();
        Page<ChallengeDetailResponse> challengeListResponses = challengeService.list(pageable, userName);
        return ResultResponse.success(challengeListResponses);
    }
}
