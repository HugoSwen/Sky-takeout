package com.hugo.controller.admin;

import com.hugo.result.Result;
import com.hugo.service.ReportService;
import com.hugo.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Api(tags = "营业额相关接口")
@Slf4j
@RestController
@RequestMapping("admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "营业额统计")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> getTurnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计，起始时间：{}，终止时间：{}", begin, end);

        TurnoverReportVO turnoverReportVO = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }
}
