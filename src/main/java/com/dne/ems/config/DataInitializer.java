package com.dne.ems.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dne.ems.model.AqiData;
import com.dne.ems.model.AqiRecord;
import com.dne.ems.model.Grid;
import com.dne.ems.model.UserAccount;
import com.dne.ems.model.enums.Role;
import com.dne.ems.model.enums.UserStatus;
import com.dne.ems.repository.AqiDataRepository;
import com.dne.ems.repository.AqiRecordRepository;
import com.dne.ems.repository.GridRepository;
import com.dne.ems.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据初始化类，用于在应用程序启动时初始化系统数据
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final GridRepository gridRepository;
    private final UserAccountRepository userAccountRepository;
    private final AqiRecordRepository aqiRecordRepository;
    private final AqiDataRepository aqiDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化数据...");
        
        // 初始化用户
        initializeAdminUser();
        
        // 初始化网格
        initializeGridData();
        
        // 初始化 AQI 记录数据
        initializeAqiData();
        
        // 初始化 AqiData 记录
        initializeAqiDataRecords();
        
        log.info("数据初始化完成");
    }
    
    /**
     * 初始化管理员用户
     */
    private void initializeAdminUser() {
        // ... (原有的用户初始化代码保持不变)
        Optional<UserAccount> existingAdmin = userAccountRepository.findByEmail("admin@aizhangz.top");
        if (existingAdmin.isEmpty()) {
            UserAccount admin = new UserAccount();
            admin.setName("系统管理员");
            admin.setEmail("admin@aizhangz.top");
            admin.setPhone("13800138000");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setRegion("河北省");
            admin.setEnabled(true);
            userAccountRepository.save(admin);
            log.info("管理员用户创建成功");
        } else {
            log.info("管理员用户已存在，跳过创建");
        }

        Optional<UserAccount> existingWorker = userAccountRepository.findByEmail("worker@aizhangz.top");
        if (existingWorker.isEmpty()) {
            UserAccount worker = new UserAccount();
            worker.setName("默认网格员");
            worker.setEmail("worker@aizhangz.top");
            worker.setPhone("13800138001");
            worker.setPassword(passwordEncoder.encode("Worker@123"));
            worker.setRole(Role.GRID_WORKER);
            worker.setStatus(UserStatus.ACTIVE);
            worker.setRegion("河北省-石家庄市");
            worker.setEnabled(true);
            userAccountRepository.save(worker);
            log.info("网格员用户创建成功");
        } else {
            log.info("网格员用户已存在，跳过创建");
        }

        Optional<UserAccount> existingDecisionMaker = userAccountRepository.findByEmail("decision.maker@aizhangz.top");
        if (existingDecisionMaker.isEmpty()) {
            UserAccount decisionMaker = new UserAccount();
            decisionMaker.setName("默认决策者");
            decisionMaker.setEmail("decision.maker@aizhangz.top");
            decisionMaker.setPhone("13800138002");
            decisionMaker.setPassword(passwordEncoder.encode("Decision@123"));
            decisionMaker.setRole(Role.DECISION_MAKER);
            decisionMaker.setStatus(UserStatus.ACTIVE);
            decisionMaker.setRegion("全国");
            decisionMaker.setEnabled(true);
            userAccountRepository.save(decisionMaker);
            log.info("决策者用户创建成功");
        } else {
            log.info("决策者用户已存在，跳过创建");
        }

        Optional<UserAccount> existingSupervisor = userAccountRepository.findByEmail("supervisor@aizhangz.top");
        if (existingSupervisor.isEmpty()) {
            UserAccount supervisor = new UserAccount();
            supervisor.setName("默认主管");
            supervisor.setEmail("supervisor@aizhangz.top");
            supervisor.setPhone("13800138003");
            supervisor.setPassword(passwordEncoder.encode("Supervisor@123"));
            supervisor.setRole(Role.SUPERVISOR);
            supervisor.setStatus(UserStatus.ACTIVE);
            supervisor.setRegion("河北省-石家庄市");
            supervisor.setEnabled(true);
            userAccountRepository.save(supervisor);
            log.info("主管用户创建成功");
        } else {
            log.info("主管用户已存在，跳过创建");
        }
    }
    
    /**
     * 初始化网格数据
     */
    private void initializeGridData() {
        // ... (原有的网格初始化代码保持不变)
        if (gridRepository.count() > 0) {
            log.info("网格数据已存在，跳过初始化");
            return;
        }
        log.info("开始初始化网格数据...");
        List<Grid> grids = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Grid grid = new Grid();
                grid.setGridX(x);
                grid.setGridY(y);
                grid.setCityName("北京市");
                grid.setDistrictName("朝阳区");
                if ((x == 3 && y == 3) || (x == 3 && y == 4) || (x == 4 && y == 3) || (x == 4 && y == 4)) {
                    grid.setIsObstacle(true);
                } else {
                    grid.setIsObstacle(false);
                }
                grids.add(grid);
            }
        }
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Grid grid = new Grid();
                grid.setGridX(x);
                grid.setGridY(y);
                grid.setCityName("上海市");
                grid.setDistrictName("浦东新区");
                if ((x == 7 && y == 7) || (x == 7 && y == 8) || (x == 8 && y == 7) || (x == 8 && y == 8)) {
                    grid.setIsObstacle(true);
                } else {
                    grid.setIsObstacle(false);
                }
                grids.add(grid);
            }
        }
        gridRepository.saveAll(grids);
        log.info("网格数据初始化完成，共创建 {} 条记录", grids.size());
    }

    /**
     * [FIXED BY AI] Initializes mock AQI data for dashboard testing.
     */
    private void initializeAqiData() {
        if (aqiRecordRepository.count() > 0) {
            log.info("AQI数据已存在，跳过初始化");
            return;
        }
        log.info("开始初始化AQI模拟数据...");
        
        // 清空现有数据
        aqiRecordRepository.deleteAll();
        
        List<AqiRecord> records = new ArrayList<>();
        
        // 获取网格数据，用于关联
        List<Grid> grids = gridRepository.findAll();
        if (grids.isEmpty()) {
            log.warn("没有找到网格数据，无法完全初始化AQI数据");
            // 即使没有网格数据，也继续初始化基本的 AQI 数据
        }
        
        // 为不同城市选择不同的网格
        @SuppressWarnings("unused")
        Map<String, List<Grid>> gridsByCity = grids.stream()
                .collect(Collectors.groupingBy(Grid::getCityName));
        
        // 基本 AQI 数据 - 不同级别
        // Good (0-50)
        AqiRecord record1 = new AqiRecord("北京市", 40, LocalDateTime.now().minusDays(1), 10.5, 20.3, 5.1, 15.2, 0.8, 30.1);
        record1.setGridX(0);
        record1.setGridY(0);
        records.add(record1);
        
        AqiRecord record2 = new AqiRecord("上海市", 35, LocalDateTime.now().minusDays(2), 9.2, 18.5, 4.8, 14.0, 0.7, 28.5);
        record2.setGridX(0);
        record2.setGridY(1);
        records.add(record2);
        
        // Moderate (51-100)
        AqiRecord record3 = new AqiRecord("广州市", 75, LocalDateTime.now().minusDays(3), 25.3, 45.7, 10.2, 30.5, 1.5, 60.2);
        record3.setGridX(1);
        record3.setGridY(0);
        records.add(record3);
        
        AqiRecord record4 = new AqiRecord("深圳市", 85, LocalDateTime.now().minusDays(4), 28.6, 52.1, 12.3, 35.8, 1.8, 68.5);
        record4.setGridX(1);
        record4.setGridY(1);
        records.add(record4);
        
        // Unhealthy for Sensitive Groups (101-150)
        AqiRecord record5 = new AqiRecord("成都市", 130, LocalDateTime.now().minusDays(5), 45.2, 85.3, 20.5, 55.8, 3.2, 90.5);
        record5.setGridX(2);
        record5.setGridY(0);
        records.add(record5);
        
        // Unhealthy (151-200)
        AqiRecord record6 = new AqiRecord("武汉市", 175, LocalDateTime.now().minusDays(6), 60.5, 110.8, 30.2, 70.5, 4.5, 120.3);
        record6.setGridX(2);
        record6.setGridY(1);
        records.add(record6);
        
        // Very Unhealthy (201-300)
        AqiRecord record7 = new AqiRecord("西安市", 250, LocalDateTime.now().minusDays(7), 90.3, 160.5, 45.8, 95.2, 6.8, 150.5);
        record7.setGridX(3);
        record7.setGridY(0);
        records.add(record7);
        
        // Hazardous (301+)
        AqiRecord record8 = new AqiRecord("兰州市", 320, LocalDateTime.now().minusDays(8), 120.5, 200.8, 60.2, 120.5, 8.5, 180.3);
        record8.setGridX(3);
        record8.setGridY(1);
        records.add(record8);
        
        // 保存基本数据
        List<AqiRecord> savedRecords = aqiRecordRepository.saveAll(records);
        log.info("已保存基本 AQI 数据: {} 条记录", savedRecords.size());
        records.clear();
        
        // 添加带有网格位置的数据 - 用于热力图
        // 直接使用网格坐标，不关联 Grid 实体
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                int aqiValue = 50 + (x + y) * 15; // 不同 AQI 值
                LocalDateTime recordTime = LocalDateTime.now().minusDays(x + y);
                
                AqiRecord record = new AqiRecord("北京市", aqiValue, recordTime, 
                    20.0 + x, 40.0 + y * 2, 8.0 + x, 25.0 + y, 1.0 + x * 0.2, 50.0 + y * 3);
                record.setGridX(x);
                record.setGridY(y);
                records.add(record);
            }
        }
        
        // 保存热力图数据
        savedRecords = aqiRecordRepository.saveAll(records);
        log.info("已保存热力图 AQI 数据: {} 条记录", savedRecords.size());
        records.clear();
        
        // 添加不同月份的数据 - 用于趋势图
        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < 3; j++) {  // 每个月添加多条记录
                LocalDateTime recordTime = LocalDateTime.now().minusMonths(i).plusDays(j * 5);
                int aqiValue = 80 + i * 20 + j * 5; // 确保有些超过 100 的值
                
                AqiRecord record = new AqiRecord("北京市", aqiValue, recordTime, 
                    30.0 + i, 60.0 + i * 2, 12.0 + i, 35.0 + i, 1.5 + i * 0.2, 70.0 + i * 3);
                // 为趋势数据也设置网格坐标
                record.setGridX(i % 5);
                record.setGridY(j % 5);
                records.add(record);
            }
        }
        
        // 保存趋势图数据
        savedRecords = aqiRecordRepository.saveAll(records);
        log.info("已保存趋势图 AQI 数据: {} 条记录", savedRecords.size());
        
        log.info("AQI模拟数据初始化完成，共创建 {} 条记录", aqiRecordRepository.count());
    }

    /**
     * 初始化 AqiData 记录，用于仪表盘展示
     */
    private void initializeAqiDataRecords() {
        if (aqiDataRepository.count() > 0) {
            log.info("AqiData 数据已存在，跳过初始化");
            return;
        }
        log.info("开始初始化 AqiData 模拟数据...");
        
        // 获取网格数据，用于关联
        List<Grid> grids = gridRepository.findAll();
        if (grids.isEmpty()) {
            log.warn("没有找到网格数据，无法初始化 AqiData 数据");
            return;
        }
        
        // 获取网格工作人员
        List<UserAccount> gridWorkers = userAccountRepository.findByRole(Role.GRID_WORKER);
        if (gridWorkers.isEmpty()) {
            log.warn("没有找到网格工作人员，使用默认ID");
        }
        // 使用 Optional 来安全地处理可能为 null 的情况，避免潜在的 NullPointerException
        Long reporterId = gridWorkers.stream()
            .findFirst()
            .map(UserAccount::getId)
            .orElse(1L); // 如果没有找到网格员或其ID为null，则默认为1L

        List<AqiData> aqiDataList = new ArrayList<>();
        
        for (int i = 0; i < Math.min(10, grids.size()); i++) {
            Grid grid = grids.get(i);
            
            // 创建不同级别的 AQI 数据
            // Good (0-50)
            switch (i % 6) {
                case 0 ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(40);
                        data.setPm25(10.5);
                        data.setPm10(20.3);
                        data.setSo2(5.1);
                        data.setNo2(15.2);
                        data.setCo(0.8);
                        data.setO3(30.1);
                        data.setPrimaryPollutant("PM2.5");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
                case 1 ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(75);
                        data.setPm25(25.3);
                        data.setPm10(45.7);
                        data.setSo2(10.2);
                        data.setNo2(30.5);
                        data.setCo(1.5);
                        data.setO3(60.2);
                        data.setPrimaryPollutant("O3");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
                case 2 ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(130);
                        data.setPm25(45.2);
                        data.setPm10(85.3);
                        data.setSo2(20.5);
                        data.setNo2(55.8);
                        data.setCo(3.2);
                        data.setO3(90.5);
                        data.setPrimaryPollutant("PM10");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
                case 3 ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(175);
                        data.setPm25(60.5);
                        data.setPm10(110.8);
                        data.setSo2(30.2);
                        data.setNo2(70.5);
                        data.setCo(4.5);
                        data.setO3(120.3);
                        data.setPrimaryPollutant("O3");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
                case 4 ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(250);
                        data.setPm25(90.3);
                        data.setPm10(160.5);
                        data.setSo2(45.8);
                        data.setNo2(95.2);
                        data.setCo(6.8);
                        data.setO3(150.5);
                        data.setPrimaryPollutant("PM2.5");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
                default ->                     {
                        AqiData data = new AqiData();
                        data.setGrid(grid);
                        data.setReporterId(reporterId);
                        data.setAqiValue(320);
                        data.setPm25(120.5);
                        data.setPm10(200.8);
                        data.setSo2(60.2);
                        data.setNo2(120.5);
                        data.setCo(8.5);
                        data.setO3(180.3);
                        data.setPrimaryPollutant("PM2.5");
                        data.setRecordTime(LocalDateTime.now().minusDays(i));
                        aqiDataList.add(data);
                    }
            }
            
            // 添加不同月份的数据 - 用于趋势图
            for (int j = 1; j <= 6; j++) {
                AqiData data = new AqiData();
                data.setGrid(grid);
                data.setReporterId(reporterId);
                data.setAqiValue(80 + j * 20); // 确保有些超过 100 的值
                data.setPm25(30.0 + j);
                data.setPm10(60.0 + j * 2);
                data.setSo2(12.0 + j);
                data.setNo2(35.0 + j);
                data.setCo(1.5 + j * 0.2);
                data.setO3(70.0 + j * 3);
                data.setPrimaryPollutant("PM10");
                data.setRecordTime(LocalDateTime.now().minusMonths(j).plusDays(i % 5));
                aqiDataList.add(data);
            }
        }
        
        aqiDataRepository.saveAll(aqiDataList);
        log.info("AqiData 模拟数据初始化完成，共创建 {} 条记录", aqiDataList.size());
    }
}