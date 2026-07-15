package com.vmfg.mis.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.services.impl.VendorService;
import com.vmfg.mis.dao.interfaces.iReportSchedulerDAO;
import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;
import com.vmfg.mis.services.interfaces.iReportSchedulerService;
import com.vmfg.util.CommonMethod;

@Service
public class ReportSchedulerService implements iReportSchedulerService {
	private static final Logger logger = LoggerFactory.getLogger(VendorService.class);
	@Autowired
	iReportSchedulerDAO ireportSchedulerDAO;

	@Override
	public ResponseAsMessage insertReportDtls() {
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			String FrmDate = ireportSchedulerDAO.getWeekStartedDate(CommonMethod.getCurrentDate());
			String ToDate = ireportSchedulerDAO.getPrevDate(CommonMethod.getCurrentDate());
			String tenantAllId = ireportSchedulerDAO.getOrgTenant();
			String[] tenantArrId = tenantAllId.split(",");
			String tenantId;
			for(int q=0;q<tenantArrId.length;q++) {
				tenantId = tenantArrId[q];
			String tenatValue = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_STATUS");
			if (tenatValue.equalsIgnoreCase("OFF")) {

				String from_to_date[] = ireportSchedulerDAO.getTenantValue(tenantId, "SCHEDULER_DATE_RANGE")
						.split(Pattern.quote("|"));

				String from_date = from_to_date[0];
				String to_date = from_to_date[1];
				List<String> dateList = new ArrayList<String>();
				String start_date = from_to_date[0];
				dateList.add(start_date);
				String sourceDate = "";
				do {
					sourceDate = from_date;

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(sourceDate));

					calendar.add(Calendar.DATE, 1);
					sourceDate = sdf.format(calendar.getTime());

					dateList.add(sourceDate);
					from_date = sourceDate;
				} while (!sourceDate.equals(to_date));
				String[] dateRange = new String[dateList.size()];
				dateRange = dateList.toArray(dateRange);

				for (int i = 0; i < dateRange.length; i++) {
					FrmDate = ireportSchedulerDAO.getWeekStartedDate(dateRange[i]);
					ToDate = ireportSchedulerDAO.getPrevDate(dateRange[i]);
					this.reportTaskSch(FrmDate, ToDate, returnList,tenantId);
					this.updateOldTask(ToDate,tenantId);
				}

			} else {
				this.reportTaskSch(FrmDate, ToDate, returnList,tenantId);
			}
			}
		} catch (Exception ex) {
			logger.error("insertReportDtls service error " + ex);
		}
		return returnList;
	}

	public ResponseAsMessage reportTaskSch(String FrmDate, String ToDate, ResponseAsMessage returnList,String tenantId) {

		List<ReportSchedulerEntity> list = new ArrayList<ReportSchedulerEntity>();
		list = ireportSchedulerDAO.getTaskDtl(FrmDate, ToDate,tenantId);
		if (list.size() > 0) {
			for (ReportSchedulerEntity entity : list) {
				int Cnt = ireportSchedulerDAO.checkReportDtl(ToDate, entity.getEmpId(), entity.getProjId(),
						entity.getTenantID(), entity.getDeptCode());
				if (Cnt == 0) {
					int InsertRec = ireportSchedulerDAO.InsertRecordDtl(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), entity.getYear(), entity.getMonth(), entity.getDayStart(),
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),
							entity.getTenantID(), ToDate);
					if (InsertRec > 0) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				} else {
					int UpdateRec = ireportSchedulerDAO.UpdateRecordDtl(entity.getEmpId(), entity.getProjId(),
							entity.getDeptCode(), entity.getYear(), entity.getMonth(), entity.getDayStart(),
							entity.getNoPlanned(), entity.getNoCompleted(), entity.getDelay(), entity.getPerCentage(),
							entity.getTenantID(), ToDate);
					if (UpdateRec == 1) {
						returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.alreadyExist);
					}
				}
			}
		}
		return returnList;

	}

	public void updateOldTask(String refDate,String tenantId) {
		List<TaskDtlEntity> getPlannedLi = ireportSchedulerDAO.getOldTaskPlanned(refDate,tenantId);
		List<TaskDtlEntity> getCompLi = ireportSchedulerDAO.getOldTaskCompleted(refDate,tenantId);

		for (int h = 0; h < getPlannedLi.size(); h++) {
			ireportSchedulerDAO.updatePlannedTask(getPlannedLi.get(h), refDate,tenantId);
		}

		for (int h = 0; h < getCompLi.size(); h++) {
			ireportSchedulerDAO.updateCompTask(getCompLi.get(h), refDate,tenantId);
		}

		ireportSchedulerDAO.taskPercentUpdate(refDate,tenantId);

	}

}
