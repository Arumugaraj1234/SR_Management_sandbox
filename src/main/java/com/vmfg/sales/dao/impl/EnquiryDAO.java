package com.vmfg.sales.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.general.rowmapper.DocTypeMstRowMapper;
import com.vmfg.general.rowmapper.DocumentStatusMstRowMapper;
import com.vmfg.general.rowmapper.GetstageprocessDtlRowMapper;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.entity.BudgetExcessUploadResponse;
import com.vmfg.sales.entity.BudgetSheetFileEntity;
import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.FinancialYearTransactionMstEntity;
import com.vmfg.sales.entity.SalesBudgetFullEntity;
import com.vmfg.sales.request.EnqEnablementRequest;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;
import com.vmfg.sales.rowmapper.CustomerMstRowMapper;
import com.vmfg.sales.rowmapper.FinancialYearTransactionMstRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetFullRowMapper;
import com.vmfg.sales.rowmapper.SalesBudgetSheetExntDtlRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class EnquiryDAO implements IEnquiryDAO {
	private static final Logger logger = LoggerFactory.getLogger(LandingPageDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int updatecontactDtl(String contactName, String ContactNo, String ContactEmail, String slaveId,
			String primary,String department) {
		int updateStatus = 0;
		try {
			if (primary == null || primary.equalsIgnoreCase("")) {
				primary = "0";
			}
			String updateStatusStr = "UPDATE `sales_enq_contact` SET `CONTACT_NAME`=?, `CONTACT_EMAIL`=?, `CONTACT_NO`=? ,`IS_PRIMARY` = ?,DEPARTMENT=? WHERE `SEC_ID`=?  ";
			updateStatus = this.jdbcTemplate.update(updateStatusStr, contactName, ContactEmail, ContactNo, primary,department,
					slaveId);
		} catch (Exception ex) {
			logger.error("updatecontactDtl Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int insertcontactDtl(String contactName, String ContactNo, String ContactEmail, String slaveId,
			String masterId, String primary,String department) {
		int insertContactDt = 0;
		try {
			if (primary == null || primary.equalsIgnoreCase("")) {
				primary = "0";
			}
			String insertContactDtStr = "INSERT INTO `sales_enq_contact` (`MASTER_ID`, `CONTACT_NAME`, `CONTACT_EMAIL`, `CONTACT_NO`,`IS_PRIMARY`,DEPARTMENT) VALUES (?,?,?,?,?,?)";
			insertContactDt = this.jdbcTemplate.update(insertContactDtStr, masterId, contactName, ContactEmail,
					ContactNo, primary,department);
		} catch (Exception ex) {
			logger.error("insertcontactDtl Error" + ex);
		}

		return insertContactDt;
	}

	@Override
	public List<DocumentTypeMstEntity> getStgDocDtl(String pmId, String tenantId) {
		List<DocumentTypeMstEntity> docTypeMst = new ArrayList<DocumentTypeMstEntity>();
		try {
			String docTypeMstStr = "SELECT   \r\n" + "					    dtm.*,pm.PROCESS_NAME AS PM_DESC  \r\n"
					+ "					FROM  \r\n"
					+ "					    document_type_mst dtm inner join process_mst pm on pm.PM_ID=dtm.PM_ID   \r\n"
					+ "					WHERE  \r\n"
					+ "					    dtm.PM_ID = ?  AND dtm.IS_REFERNCE='1' ";
			docTypeMst = this.jdbcTemplate.query(docTypeMstStr, new DocTypeMstRowMapper(), pmId);
		} catch (Exception ex) {
			logger.error("updateStgstatusDtl Error" + ex);
		}
		return docTypeMst;
	}

	@Override
	public int insertStgDtl(String masterId, String status, String seq, String tenantId, String tableName) {
		int updateStatus = 0;
		try {
			String insertStgDtlStr = "INSERT INTO " + tableName
					+ " (`MASTER_ID`, `TRANSACTION_STATUS`, `TRANSACTION_STATUS_SEQ`, `TENANT_ID`) VALUES (?, ?,?,?) ";
			updateStatus = this.jdbcTemplate.update(insertStgDtlStr, masterId, status, seq, tenantId);
		} catch (Exception ex) {
			logger.error("updateStgstatusDtl Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<DocumentStatusMstEntity> getfirstSeqBypmIdDocType(String referenceDoc, String processCode,
			String tenantId) {
		List<DocumentStatusMstEntity> documentCurrSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String currSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "WHERE\r\n" + "    dsm.DOC_TYPE = ? \r\n" + "        AND dsm.PROCESS_CODE = ? \r\n"
					+ "        AND dsm.TENANT_ID = ? \r\n" + "ORDER BY dsm.CURR_SEQUENCE\r\n" + "LIMIT 1 ";
			documentCurrSeqList = this.jdbcTemplate.query(currSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, processCode, tenantId);
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return documentCurrSeqList;
	}
	public List<DocumentStatusMstEntity> getfirstSeqByDocType(String referenceDoc,
			String tenantId) {
		List<DocumentStatusMstEntity> documentCurrSeqList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String currSeqbatchDtlStr = "SELECT \r\n" + "    dsm.*,\r\n" + "    dtm.DOCUMENT_TYPE_DESCRIPTION,\r\n"
					+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n" + "    document_type_mst dtm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_lifecycle_mst dsm ON dtm.DOCUMENT_TYPE_CODE = dsm.DOC_TYPE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = dsm.DOC_STATUS\r\n"
					+ "WHERE\r\n" + "    dsm.DOC_TYPE = ? \r\n" + "  \r\n"
					+ "        AND dsm.TENANT_ID = ? \r\n" + "ORDER BY dsm.CURR_SEQUENCE\r\n" + "LIMIT 1 ";
			documentCurrSeqList = this.jdbcTemplate.query(currSeqbatchDtlStr, new DocumentStatusMstRowMapper(),
					referenceDoc, tenantId);
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return documentCurrSeqList;
	}

	@Override
	public int getCRLastNo() {
		int lastCRNo = 0;
		try {
			String lastEnqcountStr = "SELECT \r\n" +

					"COUNT(*) as COUNT \r\n" + "FROM\r\n" + "    change_request_hdr";
			
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(lastEnqcountStr);
			int lastEnqcount = Integer.parseInt(resultMap.get("COUNT").toString());
			
			if (lastEnqcount > 0) {
				String lastEnqNoStr = "SELECT \r\n" + "   CR_NO\r\n" + "FROM\r\n" + "    change_request_hdr\r\n"
						+ "ORDER BY CR_ID DESC\r\n" + "LIMIT 1";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(lastEnqNoStr);
				lastCRNo = Integer.parseInt(resultData.get("CR_NO").toString());
			}
		} catch (Exception ex) {
			logger.error("getsalEnqLastNo Error" + ex);
		}
		return lastCRNo;
	}

	@Override
	public List<FinancialYearTransactionMstEntity> getfinacicalTransDtlByDocType(String pmId, String tenantId) {
		List<FinancialYearTransactionMstEntity> getDtl = new ArrayList<FinancialYearTransactionMstEntity>();
		try {
			String getDtlStr = "SELECT \r\n" + "    *\r\n" + "FROM\r\n" + "    financial_year_transaction_mst\r\n"
					+ "WHERE\r\n" + "    PM_ID = ? \r\n" + "        AND IS_ACTIVE = 1 \r\n"
					+ "        AND TENANT_ID = ? ";
			getDtl = this.jdbcTemplate.query(getDtlStr, new FinancialYearTransactionMstRowMapper(), pmId, tenantId);

		} catch (Exception e) {
			logger.error("getfinacicalTransDtlByDocType Error" + e);
		}
		return getDtl;
	}

	@Override
	public List<GetstageprocessDtlEntity> getFirstStageByPmId(String proccessCode, String tenantId) {
		List<GetstageprocessDtlEntity> list = new ArrayList<GetstageprocessDtlEntity>();
		try {
			String processDtlBySeqStr = "SELECT \r\n"
					+ "    pc.*, pm.PROCESS_NAME,sm.STG_DESC,dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    process_config pc\r\n" + "        INNER JOIN\r\n"
					+ "    process_mst pm ON pc.PM_ID = pm.PM_ID INNER JOIN\r\n"
					+ "	stg_master sm ON sm.STG_CODE = pc.STG_CODE LEFT JOIN document_status_type_code dstc on dstc.DOCUMENT_STATUS_TYPE_CODE =pc.MASTER_DOC_STATUS\r\n "
					+ "WHERE\r\n" + "    pc.PM_ID = ?  \r\n" + "        AND pc.TENANT_ID = ? order by SEQ+1 limit 1 ";
			list = this.jdbcTemplate.query(processDtlBySeqStr, new GetstageprocessDtlRowMapper(), proccessCode,
					tenantId);
		} catch (Exception ex) {
			logger.error("getprocessDtlBySeq Error" + ex);
		}
		return list;
	}

	@Override
	public int insertEnqHdr(String projectName, String customerName, String industrialType, String scopeofWork,
			String projectDescription, String productDtl, String enquiryType, String enqCustomerSts, String enqDate,
			String reason, String leadDtl, String tentativePoValue, String tenantId, String stageCode, String stageSeq,
			String statusCode, String statusSeq, String location, String poDate,String isInternal) {
		int insertRes = 0;
		try {

			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(enqDate, tenantId, "sales_enq_hdr", "1",
					jdbcTemplate, 0,0,null,1);

			String insertsalesDtlStr = "INSERT INTO `sales_enq_hdr` (`ENQUIRY_CODE`, `TRANSACTION_NO`, `PROJECT_NAME`, `CUSTOMER_NAME`, `INDUSTRIAL_TYPE`, `SCOPE_OF_WORK`, `PROJECT_DESCRIPTION`, `PRODUCT_DETAILS`, `ENQUIRY_DATE`, `LEAD_DTL`, `TENTATIVE_PO_VALUE`,  `EXPECTED_PO_DATE`,`TRANSACTION_STATUS`, `TRANSACTION_STATUS_SEQ`, `TRANSACTION_STAGE`, `TRANSACTION_STAGE_SEQ`, `CREATED_DATETIME`, `LAST_UPDATED_DATETIME`, `TENANT_ID`,`LOCATION`,`REASON`,`ENQUIRY_TYPE`,`FINANCIAL_YEAR_MST_ID`,`IS_INTERNAL`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertsalesDtlStr, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, gen.getEnquiryCode());
					ps.setInt(2, gen.getSeq());
					ps.setString(3, projectName);
					ps.setString(4, customerName);
					ps.setString(5, industrialType);
					ps.setString(6, scopeofWork);
					ps.setString(7, projectDescription);
					ps.setString(8, productDtl);
					ps.setString(9, enqDate);
					ps.setString(10, leadDtl);
					ps.setString(11, tentativePoValue);
					ps.setString(12, poDate);
					ps.setString(13, statusCode);
					ps.setString(14, statusSeq);
					ps.setString(15, stageCode);
					ps.setString(16, stageSeq);
					ps.setString(17, CommonMethod.getCurrentDateTime());
					ps.setString(18, CommonMethod.getCurrentDateTime());
					ps.setString(19, tenantId);
					ps.setString(20, location);
					ps.setString(21, reason);
					ps.setString(22, enquiryType);
					ps.setString(23, gen.getFinainceId());
					ps.setString(24, isInternal);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertEnqHdr Error" + ex);
		}
		return insertRes;
	}

	@Override
	public int updateEnqHdr(String projectName, String customerName, String industrialType, String scopeofWork,
			String projectDescription, String productDtl, String enquiryType, String enqCustomerSts, String enqDate,
			String reason, String leadDtl, String tentativePoValue, String tenantId, String mstId, String location , String poDate) {
		int updateEnqHdr = 0;
		try {
			String updateEnqHdrStr = "UPDATE `sales_enq_hdr` SET `PROJECT_NAME`=?, `CUSTOMER_NAME`=?, `INDUSTRIAL_TYPE`=?, `SCOPE_OF_WORK`=?, `PROJECT_DESCRIPTION`=?, `PRODUCT_DETAILS`=?, `ENQUIRY_TYPE`=?, `ENQUIRY_CUSTOMER_STATUS`=?, `ENQUIRY_DATE`=?, `REASON`=?, `LEAD_DTL`=?, `TENTATIVE_PO_VALUE`=?, `LAST_UPDATED_DATETIME`=? ,`LOCATION` = ? ,`EXPECTED_PO_DATE` = ? WHERE `SE_ID`=? ";
			updateEnqHdr = this.jdbcTemplate.update(updateEnqHdrStr, projectName, customerName, industrialType,
					scopeofWork, projectDescription, productDtl, enquiryType, enqCustomerSts, enqDate, reason, leadDtl,
					tentativePoValue, CommonMethod.getCurrentDateTime(), location, poDate , mstId );
		} catch (Exception ex) {
			logger.error("updateEnqHdr Error" + ex);
		}
		return updateEnqHdr;
	}

	@Override
	public List<DocumentTypeMstEntity> getEnqDocTypeMstDtlByStage(String stgCode, String pmId, String tenantId) {
		List<DocumentTypeMstEntity> documentTypeMstDtl = new ArrayList<DocumentTypeMstEntity>();
		try {
			String documentTypeMstDtlStr = "SELECT \r\n" + "    dtm.*,pm.PROCESS_NAME AS PM_DESC\r\n" + "FROM\r\n"
					+ "    document_type_mst dtm inner join process_mst pm on pm.PM_ID=dtm.PM_ID \r\n" + "WHERE\r\n"
					+ "    dtm.PM_ID = ?  \r\n" + "        AND dtm.STG_CODE = ? ";
			documentTypeMstDtl = this.jdbcTemplate.query(documentTypeMstDtlStr, new DocTypeMstRowMapper(),
					Integer.parseInt(pmId), stgCode);
		} catch (Exception ex) {
			logger.error("getDocTypeMstDtlByStage Error" + ex);
		}

		return documentTypeMstDtl;
	}

	@Override
	public String getEnqProcessLifeCycleCurrSeq(String processCode, String status, String tenantId) {
		String getProcessLifeCycleCurrSeq = "";
		try {
			String getProcessLifeCycleCurrSeqStr = "SELECT \r\n"
					+ "  case when Count(*) >0 then CURRENT_SEQUENCE else  '' end  AS CURRENT_SEQUENCE  \r\n"
					+ "FROM\r\n" + "    process_lifecycle_mst\r\n" + "WHERE\r\n" + "    PM_ID = ? \r\n" 
					+ " AND PL_STATUS =?\r\n" + " ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getProcessLifeCycleCurrSeqStr,processCode,status);
			getProcessLifeCycleCurrSeq = resultMap.get("CURRENT_SEQUENCE").toString();
		} catch (Exception ex) {
			logger.error("getDocstsBydocseqAtype Error " + ex);
		}
		return getProcessLifeCycleCurrSeq;
	}

	@Override
	public int insertProcessAssignDtl(String masterId, String empId, String tenantId, String pmId) {
		int updateStatus = 0;
		try {
			
			String gtQ = "select case when count(PA_ID)>0 then PA_ID else 0 end as PA_ID from process_assigned_team where PM_ID=? AND ASSIGNED_EMP_ID=? AND MASTER_ID=? AND IS_ACTIVE=1 AND TENANT_ID=? ";
			 Map<String, Object> resultMap = jdbcTemplate.queryForMap(gtQ, pmId, empId,masterId,tenantId);
			 int cnt = Integer.valueOf(resultMap.get("PA_ID").toString());
			 
			if(cnt <= 0) {
			    String insertProcessAssignStr = "INSERT INTO `process_assigned_team` (`MASTER_ID`, `PM_ID`, `ASSIGNED_EMP_ID`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?,?,?,?,?) ";
			    updateStatus = this.jdbcTemplate.update(insertProcessAssignStr, masterId, pmId, empId, "1", tenantId);
			}

		} catch (Exception ex) {
			logger.error("insertProcessAssignDtl Error " + ex);
		}
		return updateStatus;
	}

	@Override
	public String setDefaultUser(String pmId,String tenantID) {
//		String empIdDef = "";
//		try {
//
////			String gtQ = "SELECT PRIMARY_POC FROM project_wbs_initiation_mst where PM_ID= ? and TENANT_ID = ? ";
//			String gtQ = "SELECT GROUP_CONCAT(PRIMARY_POC) AS PRIMARY_POC \r\n" + 
//					"FROM project_wbs_initiation_mst \r\n" + 
//					"WHERE PM_ID = ? AND TENANT_ID = ? ";
//			Map<String,Object> resultMap = jdbcTemplate.queryForMap(gtQ,pmId,tenantID);
//			empIdDef = resultMap.get("PRIMARY_POC").toString();
//
//		} catch (Exception ex) {
//			logger.error("setDefaultUser Error " + ex);
//		}
//		return empIdDef;
		    String empIdDef = "";
		    try {
		        String gtQ = "SELECT GROUP_CONCAT(PRIMARY_POC) AS PRIMARY_POC " +
		                     "FROM project_wbs_initiation_mst " +
		                     "WHERE PM_ID = ? AND TENANT_ID = ?";
		        Map<String, Object> resultMap = jdbcTemplate.queryForMap(gtQ, pmId, tenantID);
		        String rawPocs = resultMap.get("PRIMARY_POC") != null ? resultMap.get("PRIMARY_POC").toString() : "";

		        
		        Set<String> uniquePocs = new LinkedHashSet<>(Arrays.asList(rawPocs.split(",")));
		        empIdDef = String.join(",", uniquePocs);

		    } catch (Exception ex) {
		        logger.error("setDefaultUser Error " + ex);
		    }

		    return empIdDef;
		}
	
	@Override
	public String getEmpNameDesingCode(String approveDesig, String tenantId) {
		String empIdDef = "";
		try {

			String[] parts = approveDesig.split(",");
			String formatted = Arrays.stream(parts)
			                         .map(s -> "'" + s + "'")
			                         .collect(Collectors.joining(","));

			String gtQ = "SELECT GROUP_CONCAT(distinct EMPLOYEE_ID) AS EMPLOYEE_ID\r\n" + 
					"FROM employee_mst\r\n" + 
					"WHERE DESIGNATION_CODE in ("+formatted+") and TENANT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(gtQ,tenantId);
			empIdDef = resultMap.get("EMPLOYEE_ID").toString();

		} catch (Exception ex) {
			logger.error("setDefaultUser Error " + ex);
		}
		return empIdDef;
	}


	@Override
	public List<BudgetExcessUploadResponse> uploadBudgetSheetfile(String tenantId, MultipartFile file) {
		List<BudgetSheetFileEntity> returnList = new ArrayList<>();
		List<BudgetExcessUploadResponse> mainList = new ArrayList<>();
		BudgetExcessUploadResponse mainObj = new BudgetExcessUploadResponse();
		List<String> errorMsgs = new ArrayList<>();

		try {
			Workbook wb;
			String csPath = GetPropertyValue.getPropValue("DOC_SAVED_PATH", tenantId, this.jdbcTemplate);

			File UPLOADED_FOLDER = new File(csPath + File.separator);
			if (!UPLOADED_FOLDER.exists()) {
				UPLOADED_FOLDER.mkdirs();
			}

			String newFilePath = csPath + File.separator + "_" + CommonMethod.getCurrentDateTimeForReport() + ".xlsx";
			int emptyRowIndex = validateEmptyRows(file);
			logger.info("Empty row detected at index: " + emptyRowIndex);

			File convFile = new File(newFilePath);
			boolean fileCreate = convFile.createNewFile();
			if (fileCreate) {
				try (FileOutputStream fos = new FileOutputStream(convFile)) {
					fos.write(file.getBytes());
				}

				wb = new XSSFWorkbook(convFile);
				Sheet sheet = wb.getSheetAt(0);
				int rowCount = sheet.getLastRowNum();
				logger.info("Total rows in sheet (including header): " + (rowCount + 1));

				if (rowCount > 0) {
					for (int i = 1; i <= rowCount; i++) {
						if (i == emptyRowIndex) {
							logger.info("Skipping empty row at index: " + i);
							continue; // skip this row but continue reading others
						}

						Row row = sheet.getRow(i);
						if (row == null) {
							logger.info("Skipping null row at index: " + i);
							continue;
						}

						int colCount = 16;
						ArrayList<String> cellVal = new ArrayList<>(colCount);
						BudgetSheetFileEntity list = new BudgetSheetFileEntity();

						for (int k = 0; k < colCount; k++) {
							String cellValue = "";
							try {
								Cell rowCell = row.getCell(k);
								if (rowCell != null) {
									switch (rowCell.getCellType()) {
										case STRING:
											cellValue = rowCell.getStringCellValue();
											break;
										case NUMERIC:
											if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(rowCell)) {
												DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
												cellValue = df.format(rowCell.getDateCellValue());
											} else {
												Double doubleValue = rowCell.getNumericCellValue();
												int intValue = doubleValue.intValue();
												cellValue = String.valueOf(intValue);
											}
											break;
										case BLANK:
											cellValue = "";
											break;
										default:
											logger.info("Unsupported cell type at row " + i + ", col " + k);
									}
								}
							} catch (Exception e) {
								logger.error("Error reading cell at row " + i + ", col " + k, e);
							}

							cellValue = (cellValue != null) ? cellValue.trim() : "";
							cellVal.add(cellValue);

							// Assign based on column index
							switch (k) {
								case 0:
									list.setSalesDescription(cellValue);
									break;
								case 1:
									if (cellValue.isEmpty()) {
										errorMsgs.add("Element Header is empty at row " + (i + 1));
									}
									list.setElementHeader(cellValue);
									break;
								case 2:
									list.setStnNo(cellValue);
									break;
								case 3:
									list.setLeg(cellValue);
									break;
								case 4:
									list.setElementDescription(cellValue);
									break;
								case 5:
									list.setSpecification(cellValue);
									break;
								case 6:
									list.setMake(cellValue);
									break;
								case 7:
									if (cellValue.isEmpty()) {
										errorMsgs.add("Qty is empty at row " + (i + 1));
									}
									list.setQty(cellValue);
									break;
								case 8:
									list.setUom(cellValue);
									break;
								case 9:
									if (cellValue.isEmpty()) {
										errorMsgs.add("Value is empty at row " + (i + 1));
									}
									list.setValue(cellValue);
									break;
								case 10:
									list.setSubTotalvalue(cellValue);
									break;
								case 11:
									list.setValueSubAssy(cellValue);
									break;
								case 12:
									if (cellValue.isEmpty()) {
										errorMsgs.add("Contingency is empty at row " + (i + 1));
									}
									list.setContingency(cellValue);
									break;
								case 13:
									list.setTotalValue(cellValue);
									break;
//								case 14:
//									list.setCritical(cellValue);
//									break;

								case 14:
									if (cellValue != null) {
										cellValue = cellValue.trim();
										if (cellValue.equalsIgnoreCase("Y") || cellValue.equalsIgnoreCase("Yes")) {
											list.setCritical("1");
										} else if (cellValue.equalsIgnoreCase("N") || cellValue.equalsIgnoreCase("No") || cellValue.isEmpty()) {
											list.setCritical("0");
										} else {
											list.setCritical("0");
										}
									} else {
										list.setCritical("0");
									}
									break;

								case 15:
									list.setTimelineInWeeks(cellValue);
									returnList.add(list);
									break;
							}
						}
					}
				}
			}

			logger.info("Parsed rows count: " + returnList.size());

			mainObj.setErrorMsgs(errorMsgs);
			mainObj.setList(returnList);
			mainList.add(mainObj);

		} catch (Exception e) {
			logger.error("uploadBudgetSheetfile DAO Method Exception", e);
		}

		return mainList;
	}


//		@Override
//		public List<BudgetExcessUploadResponse> uploadBudgetSheetfile(String tenantId, MultipartFile file) {
//			List<BudgetSheetFileEntity> returnList = new ArrayList<BudgetSheetFileEntity>();
//			List<BudgetExcessUploadResponse> mainList = new ArrayList<BudgetExcessUploadResponse>();
//			BudgetExcessUploadResponse mainobj=new BudgetExcessUploadResponse();
//			List<String> errorMsgs = new ArrayList<>();
//			try {
//				Workbook wb;
//				String csPath;
//	//			String  salesDescription,elementHeader,stnNo,leg,elementDescription,qty, specification, make, uom ,contingency,value,totalValue= "";
//
//				csPath = GetPropertyValue.getPropValue("DOC_SAVED_PATH", tenantId, this.jdbcTemplate);
//
//				File UPLOADED_FOLDER = new File(csPath + File.separator);
//				if (!UPLOADED_FOLDER.exists()) {
//					UPLOADED_FOLDER.mkdirs();
//				}
//				String newFilePath = csPath + File.separator + File.separator + "_"
//						+ CommonMethod.getCurrentDateTimeForReport() + ".xlsx";
//				int emptyRows = validateEmptyRows(file);
//
//				File convFile = new File(newFilePath);
//				boolean fileCreate = convFile.createNewFile();
//				if (fileCreate == true) {
//					FileOutputStream fos = new FileOutputStream(convFile);
//					fos.write(file.getBytes());
//					fos.close();
//					wb = new XSSFWorkbook(convFile);
//					Sheet sheet = wb.getSheetAt(0);
//					int rowCount = sheet.getLastRowNum();
//
//					if (rowCount > 0) {
//
//						for (int i = 1; i <= rowCount; i++) {
//							if(i!= emptyRows) {
//								int colCount = 16;
//								ArrayList<String> cellVal = new ArrayList<String>();
//								BudgetSheetFileEntity list = new BudgetSheetFileEntity();
//
//								for (int k = 0; k < colCount; k++) {
//								    try {
//								        String cellValue = "";
//								        Row row = sheet.getRow(i);
//								        Cell rowCell = row.getCell(k);
//
//								        if (rowCell != null) {
//								            if (rowCell.getCellType() == CellType.STRING) {
//								                cellValue = rowCell.getStringCellValue();
//								                cellVal.add(!cellValue.equalsIgnoreCase("") ? cellValue : null);
//								            } else if (rowCell.getCellType() == CellType.NUMERIC) {
//								                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(rowCell)) {
//								                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//								                    Date date = rowCell.getDateCellValue();
//								                    cellValue = df.format(date);
//								                } else {
//								                    Double doubleValue = rowCell.getNumericCellValue();
//								                    int getValue = doubleValue.intValue();
//								                    cellValue = String.valueOf(getValue);
//								                }
//								                cellVal.add(!cellValue.equalsIgnoreCase("") ? cellValue : null);
//								            } else if (rowCell.getCellType() == CellType.BLANK) {
//								                cellVal.add("");
//								            } else {
//								                logger.info("Cell Type" + rowCell.getCellType());
//								            }
//								        } else {
//								            cellVal.add("");
//								        }
//
//								        String cellDtl = cellVal.get(k).trim();
//
//								        switch (k) {
//								          case 0:
//								              list.setSalesDescription(cellDtl);
//								              break;
//								          case 1:
//								        	  if (cellDtl.isEmpty()) {
//								                    errorMsgs.add("Element Header is empty at row " + (i+1));
//								                }
//								              list.setElementHeader(cellDtl);
//								              break;
//										case 2:
//											list.setStnNo(cellDtl);
//											break;
//										case 3:
//											list.setLeg(cellDtl);
//											break;
//										case 4:
//											list.setElementDescription(cellDtl);
//											break;
//										case 5:
//											list.setSpecification(cellDtl);
//											break;
//										case 6:
//											list.setMake(cellDtl);
//											break;
//										case 7:
//							                if (cellDtl.isEmpty()) {
//							                    errorMsgs.add("Qty is empty at row " + (i+1));
//							                }
//							                list.setQty(cellDtl);
//							                break;
//										case 8:
//											list.setUom(cellDtl);
//											break;
//										case 9:
//											 if (cellDtl.isEmpty()) {
//							                    errorMsgs.add("Value is empty at row " + (i+1));
//							                }
//							                list.setValue(cellDtl);
//							                break;
//										case 10:
//											list.setSubTotalvalue(cellDtl);
//											break;
//										case 11:
//											list.setValueSubAssy(cellDtl);
//											break;
//										 case 12:
//								              if (cellDtl.isEmpty()) {
//								            	  errorMsgs.add("Contingency is empty at row " + (i+1));
//								              }
//								              list.setContingency(cellDtl);
//								              break;
//										case 13:
//											list.setTotalValue(cellDtl);
//											break;
//										case 14:
//											list.setCritical(cellDtl);
//											break;
//										case 15:
//											list.setTimelineInWeeks(cellDtl);
//											returnList.add(list);
//											break;
//										}
//
//
//									} catch (Exception e) {
//										logger.error("Exception in reading cells" + e);
//
//									}
//
//								}
//							}else {
//								logger.info("III");
//								break;
//							}
//						}
//					}
//				}
//				mainobj.setErrorMsgs(errorMsgs);
//				mainobj.setList(returnList);
//				mainList.add(mainobj);
//				} catch (Exception e) {
//				logger.error("uploadBudgetSheetfile DAO  Method Exception" + e);
//			}
//
//			return mainList;
//		}

	// Checking white spaces in excel
	public int validateEmptyRows(MultipartFile file) throws IOException, InvalidFormatException {
		int emptyRowVal = 0;
		InputStream inputStream = file.getInputStream();
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		   for (int i = 1; i <= sheet.getLastRowNum(); i++) {
		        Row row = sheet.getRow(i);
		        if (row == null) continue; // Skip if row is null
		        boolean isEmptyRow = true;
		        for (Cell cell : row) {
		            if (cell.getCellType() != CellType.BLANK) {
		                isEmptyRow = false;
		                break;
		            }
		        }
			if (isEmptyRow) {
				// The row is empty, do something
				emptyRowVal = row.getRowNum();
				break;
			}
		}

		workbook.close();
		return emptyRowVal;
	}

	@Override
	public String getprojectCodeDtl(String enqId, String tenantId) {
		String resp = "";
		String saleDate=null;
		try {
			String respStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN 1 \r\n"
					+ "        ELSE 0\r\n" + "    END AS ProjectCode\r\n" + "FROM\r\n" + "    project_hdr\r\n"
					+ "WHERE\r\n" + "    ENQUIRY_ID = ? AND TENANT_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(respStr,enqId,tenantId);
			resp = resultMap.get("ProjectCode").toString();
			if(!resp.equalsIgnoreCase("0")) {
				String saleHandOverDate="select case when Count(*)>0 then PROJECT_HANDOVER_DATE else '' end as HANDOVER_DATE from  sales_enq_hdr where SE_ID=? ";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(saleHandOverDate,enqId);
				saleDate = resultData.get("HANDOVER_DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("getprojectCodeDtl Error" + ex);
		}
		return saleDate;
	}

	@Override
	public List<SalesBudgetFullEntity> getsalesHdrDtl(String masterId, String tenantId) {
		List<SalesBudgetFullEntity> list = new ArrayList<SalesBudgetFullEntity>();
		try {
			String currSeqbatchDtlStr = "SELECT \r\n" + "    *\r\n" + "FROM\r\n" + "    sales_budget_sheet_hdr\r\n"
					+ "WHERE\r\n" + "    MASTER_ID = ? AND TENANT_ID =? ";
			list = this.jdbcTemplate.query(currSeqbatchDtlStr, new SalesBudgetFullRowMapper(), masterId, tenantId);
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int updateSaleBudgethdr(String sbHdrId, String paymentTerm, String salePercent, String finalSaleVal,
			String tenantId) {
		int resp = 0;
		try {
			String respStr = "UPDATE `sales_budget_sheet_hdr` SET `PAYMENT_TERMS`=?, `SALE_PERCENT`=?, `FINAL_SALE_VALUE`=?  WHERE `SB_HDR_ID`=? and `TENANT_ID` = ? ";
			resp = this.jdbcTemplate.update(respStr, paymentTerm, salePercent, finalSaleVal, sbHdrId, tenantId);
		} catch (Exception ex) {
			logger.error("getprojectCodeDtl Error" + ex);
		}
		return resp;
	}

	@Override
	public List<SalesBudgetSheetExntDtlEntity> getsaleExtDtlList(String sbExtId, String tenantId) {
		List<SalesBudgetSheetExntDtlEntity> list = new ArrayList<SalesBudgetSheetExntDtlEntity>();

		try {
			String currSeqbatchDtlStr = "SELECT \r\n" + "    *\r\n" + "FROM\r\n" + "    sales_budget_sheet_extn\r\n"
					+ "WHERE\r\n" + "    SB_DTL_ID = ? AND TENANT_ID = ? ";
			list = this.jdbcTemplate.query(currSeqbatchDtlStr, new SalesBudgetSheetExntDtlRowMapper(), sbExtId,
					tenantId);
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int getEnqEnablement(EnqEnablementRequest enqEnablementRequest, String deptCode) {
		int resp = 0;
		try {
			String getQ = "select count(*) as count from project_wbs_initiation_mst where DEPARTMENT_ASSIGNED like '%"+deptCode+"%' "  
					+ " and PM_ID=? and TENANT_ID= ? ;";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(getQ, enqEnablementRequest.getPmId(), enqEnablementRequest.getTenantId());
			resp = Integer.parseInt(resultData.get("count").toString());
		} catch (Exception ex) {
			logger.error("getDocCurrentSeqDtl Error" + ex);
		}
		return resp;
	}

	@Override
	public int insertCustomerDtl(String customerName, String city, String state, String country, String pincode,
			String address, String tenantId,String contactNo,String pan, String gst) {
		int resp = 0;
		try {
			String custCode ="";
			String checkCust ="";
			String mstCodeQryCheck = "select count(*) as count from customer_mst ";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(mstCodeQryCheck);
			int mstCodevalcheck = Integer.parseInt(resultData.get("count").toString());
			
			if(mstCodevalcheck>0) {
				 Map<String,Object> resultCust = jdbcTemplate.queryForMap("Select case when count(*)>0 then CUST_CODE else '' end AS CODE from customer_mst where CUST_NAME = ? AND TENANT_ID = ? ", customerName,tenantId);
				 checkCust = resultCust.get("CODE").toString();
				if(checkCust.equalsIgnoreCase("")) {
				String mstCodeQry = "select CUST_CODE from customer_mst order by CUST_CODE desc limit 1";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(mstCodeQry);
				String mstCodeval = resultMap.get("CUST_CODE").toString();
		     custCode = CommonMethod.getUomNewCode("CT", mstCodeval, "CT0001");
				}else {
					custCode = 	checkCust;
				}
			}else {
			custCode  = "CT0001";
			}
			if(checkCust.equalsIgnoreCase("")) {
			String respStr = "INSERT INTO `customer_mst` (`CUST_CODE`, `CUST_NAME`, `ADDRESS`, `CITY`, `STATE`, `COUNTRY`, `PINCODE`, `TENANT_ID`,`CONTACT_NO`,`PAN`,`GST`) VALUES (?, ?,?,?,?,?,?,?, ?,?,?) ";
			resp = this.jdbcTemplate.update(respStr, custCode, customerName, address, city, state,country,pincode,tenantId,contactNo,pan,gst);
			}else {
			String updatecustMst="UPDATE `customer_mst` SET  `ADDRESS`=?, `CITY`=? , `STATE`=? , `COUNTRY`=? , `PINCODE`= ? ,`CONTACT_NO` = ? ,GST=?,PAN=? WHERE CUST_CODE = ? ";	
			resp = this.jdbcTemplate.update(updatecustMst,address,city,state,country,pincode,contactNo,gst,pan,custCode);
			}
		} catch (Exception ex) {
			logger.error("insertCustomerDtl Error" + ex);
		}
		return resp;
	}
	
	@Override
	public String getsaleEnquiryCode(String seId) {
		String resp = "";
		try {
			String getQ = "select ENQUIRY_CODE from sales_enq_hdr where SE_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, seId);
			resp = resultMap.get("ENQUIRY_CODE").toString();
		} catch (Exception ex) {
			logger.error("getsaleEnquiryCode Error" + ex);
		}
		return resp;
	}
	
	@Override
	public String getsaleEnquiryCurSeq(String seId) {
		String resp = "";
		try {
			String getQ = "select TRANSACTION_STATUS_SEQ from sales_enq_hdr where SE_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, seId);
			resp = resultMap.get("TRANSACTION_STATUS_SEQ").toString();
		} catch (Exception ex) {
			logger.error("getsaleEnquiryCode Error" + ex);
		}
		return resp;
	}
	
	@Override
	public String getCusCodeByCusName(String customerName,String tenantId) {
		String customerCode = "";
		try {
			String qry = " select CUST_CODE from customer_mst where CUST_NAME=? and TENANT_ID=?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, customerName, tenantId);
			customerCode = resultMap.get("CUST_CODE").toString();
			
		} catch (Exception ex) {
			logger.error("getCusCodeByCusName Error" + ex);
		}
		return customerCode;
	}
	
	@Override
	public List<CustomerMstEntity> getCustomerMst(String tenantid) {
		List<CustomerMstEntity> list = new ArrayList<CustomerMstEntity>();
		try {

			String qry = "select * from customer_mst where TENANT_ID ='"+tenantid+"' ";
			list = this.jdbcTemplate.query(qry, new CustomerMstRowMapper());

		} catch (Exception ex) {
			logger.error("getCustomerMst Error" + ex);	
		}
		return list;
	}

	@Override
	public int deleteSaleEnqContact(String hdrId) {
		int resp = 0;
		try {
			String getQ = "DELETE FROM sales_enq_contact WHERE SEC_ID=?";
			resp = this.jdbcTemplate.update(getQ,hdrId);

		} catch (Exception ex) {
			logger.error("deleteSaleEnqContact Error" + ex);
		}
		return resp;
	}
}
