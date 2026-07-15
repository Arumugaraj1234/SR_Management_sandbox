package com.vmfg.export.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.request.MaterialIssueRequest;
import com.vmfg.export.dao.interfaces.IDeliveryChallanReportTrackerDAO;
import com.vmfg.export.dao.interfaces.IProjectReportTrackerDAO;
import com.vmfg.export.entity.DcRequestDtlEntity;
import com.vmfg.export.entity.DcRequestHdrEntity;
import com.vmfg.export.entity.FileDownloadEntity;
import com.vmfg.export.request.DcReqDtlRequest;
import com.vmfg.export.request.DcRequestHdrRequest;
import com.vmfg.export.request.DeliveryChallanRequest;
import com.vmfg.export.request.DtlIdandTenantIdRequest;
import com.vmfg.export.response.ResponseAsList;
import com.vmfg.export.services.interfaces.IDeliveryChallanReportService;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.util.CommonBase64Class;
import com.vmfg.util.CommonMethod;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class DeliveryChallanReportService implements IDeliveryChallanReportService {
	private static final Logger logger = LoggerFactory.getLogger(DeliveryChallanReportService.class);

	@Autowired
	IDeliveryChallanReportTrackerDAO iDeliveryChallanReportTrackerDAO;

	@Autowired
	IProjectReportTrackerDAO iProjectReportTrackerDAO;

	@Override
	public ResponseAsList getDeliveryChallanReportPdf(DeliveryChallanRequest deliveryReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			logger.info("DeliveryChallanReportService Started ");
			FileDownloadEntity fdEntity = new FileDownloadEntity();
			List<FileDownloadEntity> listEnt = new ArrayList<FileDownloadEntity>();
			InputStream inputStream = null;
			String downloadPath = "";
			InputStream resource = null;

			Map<String, Object> params = new HashMap<String, Object>();
			JasperReport report = null;
			Connection connection = null;
			File downloadFoler = null;

			downloadPath = iDeliveryChallanReportTrackerDAO.getPropValueByTenant(deliveryReq.getTenantId(),
					"DOWNLOAD_PATH_FOR_DELIVERY_CHALLAN");
			String orgName = iProjectReportTrackerDAO.getOrganizationInfo(deliveryReq.getTenantId());

			downloadFoler = new File(downloadPath);
			if (!downloadFoler.exists()) {
				downloadFoler.mkdirs();
			}

			String DC_CODE = iDeliveryChallanReportTrackerDAO.getDcCodeCode(deliveryReq.getTenantId(),
					deliveryReq.getDcId());
			logger.info(DC_CODE.replace('/', '_'));

			downloadPath = downloadPath + File.separator + orgName + "_" + DC_CODE.replace('/', '_') + "_"
					+ iDeliveryChallanReportTrackerDAO.getCurrentDateTime() + ".pdf";

			String mainReportPath = iDeliveryChallanReportTrackerDAO.getProjectTrackerPath(deliveryReq.getTenantId(),
					deliveryReq.getKey(), "1");
			String subReportPath = iDeliveryChallanReportTrackerDAO.getProjectTrackerPath(deliveryReq.getTenantId(),
					deliveryReq.getKey(), "0");

			File reportFile = new File(mainReportPath);
			if (reportFile.exists()) {
				resource = new FileInputStream(reportFile);
			}

			inputStream = resource;
			report = JasperCompileManager.compileReport(inputStream);

			params.put("TENANT_ID", deliveryReq.getTenantId());
			params.put("DC_ID", deliveryReq.getDcId());
			params.put("SUBREPORT", subReportPath);

			connection = iDeliveryChallanReportTrackerDAO.getConnection();
			JasperPrint print = null;
			if (connection != null) {
				print = JasperFillManager.fillReport(report, params, connection);
			}

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(downloadPath));
			exporter.exportReport();

			connection.close();
			fdEntity.setFilePath(downloadPath);
			fdEntity.setMessageCode("M0001");
			fdEntity.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity.getFilePath())));
			listEnt.add(fdEntity);

			if (listEnt.size() > 0) {
				list.setResponseData(listEnt);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setFileName(orgName + "_" + DC_CODE.replace('/', '_') + "_"
						+ iDeliveryChallanReportTrackerDAO.getCurrentDateTime() + ".pdf");
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("DeliveryChallanReportService Exception " + ex);
			list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			list.setResponseMessage(ResponseMessageMap.noRecord);
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public ResponseAsMessage getinsertqcreq(DcRequestHdrRequest deliveryReq) {

		String result = "";
		ResponseAsMessage msg = new ResponseAsMessage();

		try {

			String reqOn = CommonMethod.getCurrentDateTime();
			String reqBy = deliveryReq.getRequestedBy();
			String pmHdrId = deliveryReq.getPmHdrId();
			String remarks = deliveryReq.getRemarks();
			String isComplested = deliveryReq.getIsCompleted();
			String tenantId = deliveryReq.getTenantId();

			String dcrHdrId = iDeliveryChallanReportTrackerDAO.insertdcReqHdr(reqOn, reqBy, pmHdrId, remarks,
					isComplested, tenantId, deliveryReq.getMrHdrId());

			List<DcReqDtlRequest> list = deliveryReq.getDcreqdtl();

			if (!dcrHdrId.equalsIgnoreCase("0")) {
				for (int i = 0; i < list.size(); i++) {
					String productId = list.get(i).getProductId();
					String descofGoods = list.get(i).getDescofGoods();
					String qty = list.get(i).getQty();
					String closedQty = list.get(i).getClosedQty();
					String dtlTenantId = list.get(i).getTenantId();

					result = iDeliveryChallanReportTrackerDAO.insertdcReqDtl(dcrHdrId, productId, descofGoods, qty,
							closedQty, dtlTenantId);
					if (result.equalsIgnoreCase("0")) {
						msg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						msg.setResponseMessage("");
						msg.setResponseDataMessage(ResponseMessageMap.failtoinsert);
					} else {
						msg.setResponseCode(ResponseMessageMap.responseCodeOk);
						msg.setResponseMessage("");
						msg.setResponseDataMessage(ResponseMessageMap.successInserted);
					}

				}

			} else {
				msg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				msg.setResponseMessage("");
				msg.setResponseDataMessage(ResponseMessageMap.failtoinsert);
			}
		} catch (Exception e) {
			logger.info("getinsertqcreq service method exception" + e);
		}

		return msg;
	}

	@Override
	public ResponseAsList getdcreqhdrdtldtl(MaterialIssueRequest deliveryReq) {

		ResponseAsList list = new ResponseAsList();
		List<DcRequestHdrEntity> hdrlist = new ArrayList<>();
		List<DcRequestDtlEntity> dtllist = new ArrayList<>();
		String pmHdrId = deliveryReq.getPmHdrId();
		String tenantId = deliveryReq.getTenantId();
		String name = deliveryReq.getFromName();

		try {
			String locTypeCode = iDeliveryChallanReportTrackerDAO.getLocTypeCode(name,tenantId);
			hdrlist = iDeliveryChallanReportTrackerDAO.getDcreqHdr(pmHdrId, tenantId, locTypeCode);

			for (int i = 0; i < hdrlist.size(); i++) {
				dtllist = iDeliveryChallanReportTrackerDAO.getDcreqDtl(hdrlist.get(i).getDcrId(), tenantId);
				hdrlist.get(i).setDereqdtllist(dtllist);
			}
			if (hdrlist.size() > 0) {
				list.setResponseData(hdrlist);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {

			logger.error("getdcreqhdrdtldtl service exception" + e);
		}
		return list;
	}

	@Override
	public ResponseAsMessage updateDcqty(List<DtlIdandTenantIdRequest> deliveryReq) {

		ResponseAsMessage msg = new ResponseAsMessage();
		String dtlId = null;
		String qty = null;
		int res = 0;
		try {
			for (int i = 0; i < deliveryReq.size(); i++) {
				dtlId = deliveryReq.get(i).getDtlId();
				qty = deliveryReq.get(i).getQty();
				res = iDeliveryChallanReportTrackerDAO.updateDcqty(dtlId, qty);

				if (res > 0) {
					// String res1 = String.valueOf(res);
					int dcrid = iDeliveryChallanReportTrackerDAO.getdcrId(dtlId);
					if (dcrid > 0) {

						int count = iDeliveryChallanReportTrackerDAO.getcheckqty(dcrid);
						if (count == 0) {
							iDeliveryChallanReportTrackerDAO.updateDcrequest(dcrid);
							iDeliveryChallanReportTrackerDAO.updateMRIscompletedStatus(
									iDeliveryChallanReportTrackerDAO.getMrHdrId(String.valueOf(dcrid)));
						}
					}
				} else {
					msg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					msg.setResponseDataMessage(ResponseMessageMap.failMsg);

				}
			}
			if (res > 0) {
				msg.setResponseCode(ResponseMessageMap.responseCodeOk);
				msg.setResponseDataMessage(ResponseMessageMap.success);
			} else {
				msg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				msg.setResponseDataMessage(ResponseMessageMap.failMsg);

			}

		} catch (Exception e) {

			logger.error("updateDcqty service method exception" + e);
		}
		return msg;

	}

}
