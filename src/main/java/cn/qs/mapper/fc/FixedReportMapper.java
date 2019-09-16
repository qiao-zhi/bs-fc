package cn.qs.mapper.fc;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.fc.FixedReport;

public interface FixedReportMapper extends JpaRepository<FixedReport, Integer> {

	FixedReport findBySyncDate(String syncDate);

}
