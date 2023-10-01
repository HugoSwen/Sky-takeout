package com.hugo.service;

import com.hugo.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
