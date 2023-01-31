package site.bookmore.bookmore.users.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.service.RanksService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "7-랭킹")
@RequestMapping("/api/v1/users")
public class RanksController {
    private final RanksService ranksService;

    @ApiOperation(value = "랭킹 Top100 조회")
    @GetMapping("/ranks")
    public ResultResponse<List<RanksResponse>> findTop100Ranks() {

        return ResultResponse.success(ranksService.findTop100Ranks());
    }

}