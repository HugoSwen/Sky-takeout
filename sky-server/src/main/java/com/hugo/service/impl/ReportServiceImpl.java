package com.hugo.service.impl;

import com.hugo.constant.OrdersConstant;
import com.hugo.dto.GoodsSalesDTO;
import com.hugo.mapper.OrderMapper;
import com.hugo.mapper.UserMapper;
import com.hugo.service.ReportService;
import com.hugo.service.WorkspaceService;
import com.hugo.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 日期序列
        List<LocalDate> dateList = getDateList(begin, end);

        // 营业额序列
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Double turnover = orderMapper.sumByMap(beginTime, endTime, OrdersConstant.COMPLETED);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Long> totalUserList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Long totalUser = userMapper.countByMap(null, endTime);
            totalUserList.add(totalUser);

            Long newUser = userMapper.countByMap(beginTime, endTime);
            newUserList.add(newUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Long> orderCountList = new ArrayList<>();
        List<Long> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Long orderCount = orderMapper.countByMap(beginTime, endTime, null);
            orderCountList.add(orderCount);

            Long validOrderCount = orderMapper.countByMap(beginTime, endTime, OrdersConstant.COMPLETED);
            validOrderCountList.add(validOrderCount);
        }

        // TODO 待学习
        Long totalOrderCount = orderCountList.stream().reduce(Long::sum).get();
        Long validOrderCount = validOrderCountList.stream().reduce(Long::sum).get();

        Double orderCompletionRate = 0.0;

        if (totalOrderCount != 0)
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> GoodsSalesList = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> nameList = GoodsSalesList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = GoodsSalesList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    // TODO 待学习
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        // 日期区间
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

        // 运营数据概览
        BusinessDataVO TotalBusinessData = workspaceService.getBusinessData(LocalDateTime.of(beginDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));

        // 获取输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            assert inputStream != null;
            // 获取poi对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            // 获取Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // 设置时间段
            sheet.getRow(1).getCell(1).setCellValue("时间：" + beginDate + "至" + endDate);

            // 概览数据
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(TotalBusinessData.getTurnover());
            row.getCell(4).setCellValue(TotalBusinessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(TotalBusinessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(TotalBusinessData.getValidOrderCount());
            row.getCell(4).setCellValue(TotalBusinessData.getUnitPrice());

            // 明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate today = beginDate.plusDays(i);
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(today, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MAX));

                row= sheet.getRow(7 + i);
                row.getCell(1).setCellValue(today.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            // 获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);

            // 关闭资源
            inputStream.close();
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取日期序列
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        // 日期序列
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
