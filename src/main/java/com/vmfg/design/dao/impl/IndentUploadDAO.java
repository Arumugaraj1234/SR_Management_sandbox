package com.vmfg.design.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.design.entity.GetBudgetDtlByIndentEntity;
import com.vmfg.design.entity.GetIndentDtlProductCostEntity;
import com.vmfg.design.entity.GetIndentLifecycDtlEntity;
import com.vmfg.design.entity.GetindentDtlcycEntity;
import com.vmfg.design.entity.IndentBudgetDtlByIndentIdEntity;
import com.vmfg.design.entity.IndentCostDtlEntity;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.design.entity.IndentProjectEntity;
import com.vmfg.design.entity.IndentRemarksEntity;
import com.vmfg.design.entity.IndentRequestEntity;
import com.vmfg.design.entity.IndentTypeMstTblEntity;
import com.vmfg.design.entity.SalesIndentBudgetDtlEntity;
import com.vmfg.design.entity.UomEntity;
import com.vmfg.design.entity.UploadIndentTemplateEntity;
import com.vmfg.design.rowmapper.GetBudgetDtlByIndentRowMapper;
import com.vmfg.design.rowmapper.GetIndentDtlProductCostRowMapper;
import com.vmfg.design.rowmapper.GetIndentLifecycDtlRowMapper;
import com.vmfg.design.rowmapper.GetindentDtlcycRowMapper;
import com.vmfg.design.rowmapper.IndentBudgetDtlByIndentIdRowMapper;
import com.vmfg.design.rowmapper.IndentCostRowMapper;
import com.vmfg.design.rowmapper.IndentDtlTblRowMapper;
import com.vmfg.design.rowmapper.IndentHdrDtlsRowMapper;
import com.vmfg.design.rowmapper.IndentProjectRowMapper;
import com.vmfg.design.rowmapper.IndentRemarksRowMapper;
import com.vmfg.design.rowmapper.IndentTypeMstTblRowMapper;
import com.vmfg.design.rowmapper.UomRowMapper;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.inventory.rowmapper.InventoryMaterialTransferRowMapper;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.entity.BudgetKeyCategory;
import com.vmfg.sales.rowmapper.SalesBudgetRowMapper;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.PodtlsForProductEntity;
import com.vmfg.scm.rowmapper.PodtlsForProductRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;
import com.vmfg.util.entity.InventrytolocRowMapper;
import com.vmfg.util.entity.Inventrytolocentity;

@Transactional
@Repository
public class IndentUploadDAO implements IIndentUploadDAO {
	private static final Logger logger = LoggerFactory.getLogger(IndentUploadDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	ProjectDAO projectDAO;

	@Autowired
	UploadManagementDAO uploadManagementDAO;
	
	@Override
	public List<UploadIndentTemplateEntity> uploadIndentTemplate(JSONArray getArray, MultipartFile file) {
		List<UploadIndentTemplateEntity> returnList = new ArrayList<UploadIndentTemplateEntity>();
		try {
			Workbook wb;
			String csPath;
			String tenantId = "", duplicateRecord = "";
			int sNo = 1;
			for (int h = 0; h < getArray.length(); h++) {
				JSONObject objects = getArray.getJSONObject(h);
				JSONArray keys = objects.names();

				for (int j = 0; j < keys.length(); ++j) {

					String key = keys.getString(j);
					String value = objects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					}
				}

				csPath = GetPropertyValue.getPropValue("DOC_SAVED_PATH", tenantId, this.jdbcTemplate);

				File UPLOADED_FOLDER = new File(csPath + File.separator);
				if (!UPLOADED_FOLDER.exists()) {
					UPLOADED_FOLDER.mkdirs();
				}
				String newFilePath = csPath + File.separator + File.separator + "_"
						+ CommonMethod.getCurrentDateTimeForReport() + ".xlsx";
				int emptyRows = validateEmptyRows(file);

				File convFile = new File(newFilePath);
				boolean fileCreate = convFile.createNewFile();
				if (fileCreate == true) {
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(file.getBytes());
					fos.close();
					wb = new XSSFWorkbook(convFile);
					Sheet sheet = wb.getSheetAt(0);
					int rowCount = sheet.getLastRowNum();

					if (rowCount > 0 && emptyRows == 0) {

						HashSet<String> partNumberSet = new HashSet<>();
						for (int i = 1; i <= rowCount; i++) {
							int colCount = 10;
							ArrayList<String> cellVal = new ArrayList<String>();
							UploadIndentTemplateEntity list = new UploadIndentTemplateEntity();
							list.setSNo(sNo++);
							for (int k = 0; k < colCount; k++) {
								try {
									String cellValue = "";
									Row row = sheet.getRow(i);
									Cell rowCell = row.getCell(k);

									if (rowCell != null) {
										if (rowCell.getCellType() == CellType.STRING) {

											cellValue = rowCell.getStringCellValue();
											cellVal.add(!cellValue.equalsIgnoreCase("") ? cellValue : null);

										} else if (rowCell.getCellType() == CellType.NUMERIC) {
											if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(rowCell)) {
												DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
												Date date = rowCell.getDateCellValue();
												cellValue = df.format(date);
											} else {
											//	Double doubleValue = rowCell.getNumericCellValue();
												if(k==1) {
												cellValue = new BigDecimal(rowCell.getNumericCellValue()).setScale(0, RoundingMode.HALF_DOWN).toString();
												}else {
												//	cellValue = new BigDecimal(rowCell.getNumericCellValue()).setScale(2, RoundingMode.HALF_DOWN).toString();
													Double doubleValue = rowCell.getNumericCellValue();
													cellValue = doubleValue.toString();
												}
								
											}
											cellVal.add(!cellValue.equalsIgnoreCase("") ? cellValue : null);

										} else if (rowCell.getCellType() == CellType.BLANK) {

											cellVal.add("");

										} else {
											logger.info("Cell Type" + rowCell.getCellType());
										}
									} else {
										cellVal.add("");
									}

									String cellDtl = cellVal.get(k);

									switch (k) {
									case 0:
										list.setItemNo(cellDtl);
										break;
									case 1:
									//	cellDtl = cellDtl.replaceAll("[^a-zA-Z0-9]", "");
										cellDtl = cellDtl.replaceAll("\\ / : * ? \" < > | ,", "");
										list.setPartNumber(cellDtl);
										if (partNumberSet.contains(cellDtl)) {
											duplicateRecord = "1";
										} else {
											partNumberSet.add(cellDtl);
											duplicateRecord = "0";
										}
										list.setDuplicateRecord(duplicateRecord);
										break;
									case 2:
										list.setDesc(cellDtl);
										break;
									case 3:
										list.setSpecification(cellDtl);
										break;
									case 4:
									//	cellDtl = cellDtl.replaceAll("[^a-zA-Z0-9]", "");
										list.setWeight(cellDtl);
										break;
									case 5:
										list.setMaterial(cellDtl);
										break;
									case 6:
										list.setMake(cellDtl);
										break;
									case 7:
										 if (cellDtl != null && !cellDtl.matches("-?\\d+(\\.\\d+)?")) {
                                        cellDtl = cellDtl.replaceAll("[^a-zA-Z0-9]", "");
										 }
										 list.setQty(cellDtl);
										 break;
									case 8:
										// Replace special characters in unit
                                        if (cellDtl != null) {
                                            if (cellDtl.equalsIgnoreCase("No's")) {
                                                cellDtl = "Nos";
                                            } else {
                                                cellDtl = cellDtl.replaceAll("[^a-zA-Z0-9]", "");
                                            }
                                        }
										list.setUnit(cellDtl);
										break;
									case 9:
										list.setRemarks(cellDtl);
										returnList.add(list);
										break;
									}
									

								} catch (Exception e) {
									logger.error("Exception in reading cells" + e);

								}
							}
						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("uploadIndentTemplate DAO  Method Exception" + e);
		}

		return returnList;
	}

	// Checking white spaces in excel
	public int validateEmptyRows(MultipartFile file) throws IOException, InvalidFormatException {
		int emptyRowVal = 0;
		InputStream inputStream = file.getInputStream();
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		for (Row row : sheet) {
			boolean isEmptyRow = true;
			for (Cell cell : row) {
				if (cell.getCellType() != CellType.BLANK) {
					isEmptyRow = false;
					break;
				}
			}
			if (isEmptyRow) {
				// The row is empty, do something
				emptyRowVal = row.getRowNum() + 1;
				break;
			}
		}

		workbook.close();
		return emptyRowVal;
	}

	@Override
	public String getUomCodeByUnit(String unit, String tenantId) {
		String uomCode = "";
		try {
			String getCode = "select * from uom_mst where UOM_SHORT_DESCRIPTION= ? ";
			List<UomEntity> uomList  = this.jdbcTemplate.query(getCode,new UomRowMapper(), unit);
		if(uomList.size()>0) {
			uomCode = uomList.get(0).getUomCode();
		}else {
			uomCode ="0";
		}
		} catch (Exception ex) {
			logger.error("getUomCodeByUnit method Error" + ex);
		}
		return uomCode;

	}

	@Override
	public String getProjectCodeByProjId(String projectId, String tenantId) {
		String projectCode = "";
		try {
			String getCode = "select PROJECT_CODE from project_hdr where PM_HDR_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCode,projectId,tenantId);
			projectCode= resultMap.get("PROJECT_CODE").toString();

		} catch (Exception ex) {
			logger.error("getProjectCodeByProjId method Error" + ex);
		}
		return projectCode;

	}

	@Override
	public String getSbcShortDescBySbcCode(String sbcCode, String tenantId) {
		String shortDesc = "";
		try {
			String getCode = "select SHORT_DESC from sales_budget_category where SBC_CODE=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCode,sbcCode,tenantId);
			shortDesc= resultMap.get("SHORT_DESC").toString();

		} catch (Exception ex) {
			logger.error("getSbcShortDescBySbcCode method Error" + ex);
		}
		return shortDesc;

	}

	@Override
	public String getKeyAreaCodeByPkId(String pkaId, String tenantId) {
		String keyAreaCode = "";
		try {
			String getCode = "SELECT \r\n" + "    CODE\r\n" + "FROM\r\n" + "    project_key_area_mst pka\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area pk ON pka.PK_ID = pk.PK_ID\r\n" + "WHERE\r\n"
					+ "    pk.PKA_ID =? and pka.TENANT_ID=?; ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCode,pkaId,tenantId);
			keyAreaCode= resultMap.get("CODE").toString();

		} catch (Exception ex) {
			logger.error("getKeyAreaCodeByPkId method Error" + ex);
		}
		return keyAreaCode;

	}

	@Override
	public String getSubKeyAreaCodeByPskId(String pksaId, String tenantId) {
		String keySubAreaCode = "";
		try {
			String getCode = "SELECT \r\n" + "    CODE\r\n" + "FROM\r\n" + "    project_key_sub_area pksa\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n"
					+ "WHERE\r\n" + "    pksa.PKSA_ID = ? and psk.TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCode,pksaId,tenantId);
			keySubAreaCode= resultMap.get("CODE").toString();

		} catch (Exception ex) {
			logger.error("getSubKeyAreaCodeByPskId method Error" + ex);
		}
		return keySubAreaCode;

	}

	@Override
	public int insertIndentHdrDtls(String indentCode, String expectedDeliveryDate, String runningNo, String sbcCode,
			String pkId, String pskId, String indentTypeCode, String indentDate, String projectId, String masterId,
			String empId, String seqStatus, String tenantId, String design, String deptCode) {
		int hdrId = 0;
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String insrtQry = "INSERT INTO indent_hdr (RUNNING_NO, INDENT_CODE, SBC_CODE, PKA_ID, PKSA_ID, INDENT_TYPE_CODE, CREATED_DATE, PROJECT_ID, MASTER_ID, CREATED_DATETIME, CREATED_BY, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, SEQUENCE_N0, SEQUENCE_STATUS, BUDGET_VALUE, IS_COMPLETED, TENANT_ID,EXPECTED_DELIVERY_DATE,NEXT_APPROVING_DESIG,DEPARTMENT_CODE,REVISION_DATE) VALUES (?,?,?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insrtQry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, runningNo);
					ps.setString(2, indentCode);
					ps.setString(3, sbcCode);
					ps.setString(4, pkId);
					ps.setString(5, pskId);
					ps.setString(6, indentTypeCode);
					ps.setString(7, indentDate);
					ps.setString(8, projectId);
					ps.setString(9, masterId);
					ps.setString(10, CommonMethod.getCurrentDateTime());
					ps.setString(11, empId);
					ps.setString(12, CommonMethod.getCurrentDateTime());
					ps.setString(13, empId);
					ps.setString(14, "1");
					ps.setString(15, seqStatus);
					ps.setString(16, "0");
					ps.setString(17, "0");
					ps.setString(18, tenantId);
					ps.setString(19, expectedDeliveryDate);
					ps.setString(20, design);
					ps.setString(21, deptCode);
					ps.setString(22, CommonMethod.getCurrentDate());
					return ps;
				}
			}, holder);

			hdrId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertIndentHdrDtls method Error" + ex);
		}
		return hdrId;

	}

	@Override
	public int insertIndentDtl(int indentId, String prodCode, String desc, String specification, String make,
			String qty, String unit, String weight, String material, String tenantId, String remarks) {
		int indentDtlId = 0;
		try {

			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String qry = "INSERT INTO indent_dtl (INDENT_ID, PRODUCT_CODE, DESCRIPTION, SPECIFICATION, MAKE, QTY, UNIT, WEIGHT, MATERIAL, REMARKS,TENANT_ID) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, String.valueOf(indentId));
					ps.setString(2, prodCode);
					ps.setString(3, desc);
					ps.setString(4, specification);
					ps.setString(5, make);
					ps.setString(6, qty);
					ps.setString(7, unit);
					ps.setString(8, weight);
					ps.setString(9, material);
					ps.setString(10, remarks);
					ps.setString(11, tenantId);
					return ps;
				}
			}, holder);

			indentDtlId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertIndentDtl method Error" + ex);
		}
		return indentDtlId;

	}

	@Override
	public int createNewUomAndInsert(String unit, String tenantId) {
		int insertStatus = 0;
		try {

			String qry = "select UOM_CODE from uom_mst where TENANT_ID =? order by UOM_CODE desc limit 1";

			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,tenantId);
			String lastUomCode= resultMap.get("UOM_CODE").toString();
			
			String newUomCode = CommonMethod.getUomNewCode("U", lastUomCode, "U0001");
			

			String insert = "INSERT INTO uom_mst (UOM_CODE, UOM_LONG_DESCRIPTION, UOM_SHORT_DESCRIPTION, TENANT_ID) VALUES (?, ?, ?, ?)";
			insertStatus = this.jdbcTemplate.update(insert, newUomCode, unit, unit, tenantId);

		} catch (Exception ex) {
			logger.error("createNewUomAndInsert method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int checkProductCodeInProdMst(String ProductCode,String desc,String specification, String tenantId, String projectId, String prodCategory) {
		int count = 0;
		try {
			String qry = "select case when count(*)>0 then product_id else 0 end as COUNT from product_mst where PRODUCT_CODE=?"
					+   "AND PRODUCT_DESCRIPTION = ? " +
					"AND SPECIFICATION = ? " +" and TENANT_ID=? AND PM_HDR_ID=? AND PRODUCT_CATEGORY=?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,ProductCode,desc,specification,tenantId,projectId,prodCategory);
			count = Integer.parseInt(result.get("COUNT").toString());


		} catch (Exception ex) {
			logger.error("checkProductCodeInProdMst method Error" + ex);
		}
		return count;

	}

	@Override
	public int insertnewProductInProdMst(String productCode, String pmHdrId, String prodDesc, String uomCode,
			String tenantId, String empId, String specification, String make, String qty, String unit, String weight,
			String material, String pkaId, String pskaId, String prodCategory, String unitRate) {
		int indentDtlId = 0;
		try {

			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				String qry = "INSERT INTO product_mst (PRODUCT_CODE, PRODUCT_DESCRIPTION, PM_HDR_ID, PRODUCT_UOM_CODE, PKA_ID, "
						+ "PSKA_ID, CREATED_USER_ID, CREATED_DATETIME, SPECIFICATION, MAKE, QTY, UNIT, WEIGHT, MATERIAL, IS_ACTIVE, "
						+ "LAST_UPDATED_USER_ID, LAST_UPDATED_DATETIME, TENANT_ID,PRODUCT_CATEGORY, PRODUCT_COST_PER_UNIT) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, productCode);
					ps.setString(2, prodDesc);
					ps.setString(3, pmHdrId);
					ps.setString(4, uomCode);
					ps.setString(5, pkaId);
					ps.setString(6, pskaId);
					ps.setString(7, empId);
					ps.setString(8, CommonMethod.getCurrentDateTime());
					ps.setString(9, specification);
					ps.setString(10, make);
					ps.setString(11, qty);
					ps.setString(12, unit);
					ps.setString(13, weight);
					ps.setString(14, material);
					ps.setString(15, "1");
					ps.setString(16, empId);
					ps.setString(17, CommonMethod.getCurrentDateTime());
					ps.setString(18, tenantId);
					ps.setString(19, prodCategory);
					ps.setString(20, unitRate);
					return ps;
				}
			}, holder);

			indentDtlId = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertnewProductInProdMst method Error" + ex);
		}
		return indentDtlId;

	}

	@Override
	public void updateProductCostPerUnit(int productId, String costPerUnit) {
		String sql = "UPDATE product_mst SET PRODUCT_COST_PER_UNIT = ? WHERE PRODUCT_ID = ?";
		jdbcTemplate.update(sql, costPerUnit, productId);
	}


	@Override
	public int insertIndentAssignTeam(String indentDtlId, String empId, String primary, String tenantId) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO indent_assign_team (INDENT_DTL_ID, EMPLOYEE_ID, IS_PRIMARY, TENANT_ID) VALUES (?, ?, ?, ?);";
			insertStatus = this.jdbcTemplate.update(qry, indentDtlId, empId, primary, tenantId);

		} catch (Exception ex) {
			logger.error("insertIndentAssignTeam method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int updateIndentAssignTeam(String empId, String iaId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_assign_team SET EMPLOYEE_ID=? WHERE IA_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, empId, iaId);

		} catch (Exception ex) {
			logger.error("updateIndentAssignTeam method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String getIaId(String indentDtlId) {
		String iaId = "";
		try {
			String qry = "select IA_ID from indent_assign_team where INDENT_DTL_ID=? and IS_PRIMARY='0'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId);
			iaId= resultMap.get("IA_ID").toString();
		} catch (Exception ex) {
			logger.error("getIaId method Error" + ex);
		}
		return iaId;

	}

	@Override
	public int getLastestRunningNo(String tenantId,String pmHdrId) {
		int runningNo = 0;
		try {
			String checkCount = "select Count(*) AS COUNT from indent_hdr where TENANT_ID=? and PROJECT_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(checkCount,tenantId,pmHdrId);
			int count = Integer.parseInt(result.get("COUNT").toString());

			if (count > 0) {
				String getCode = "select RUNNING_NO from indent_hdr where TENANT_ID=? and PROJECT_ID = ? order by INDENT_ID desc limit 1;";
				Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(getCode,tenantId,pmHdrId);
				runningNo = Integer.parseInt(resultMap.get("RUNNING_NO").toString());
				runningNo = runningNo + 1;
			} else {
				runningNo = 1;
			}

		} catch (Exception ex) {
			logger.error("getLastestRunningNo method Error" + ex);
		}
		return runningNo;

	}

	@Override
	public List<IndentDtlTblEntity> getIndentDtlsByIndentId(String projId, String indentId, String tenantId) {
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
		try {
			String getDtlList = "SELECT \r\n" + 
					"    @a := @a + 1 AS S_NO,\r\n" + 
					"    iteam.IS_PRIMARY,\r\n" + 
					"    CREATED_BY,\r\n" + 
					"\r\n" + 
					"    iteam.EMPLOYEE_ID AS EMP_ID,\r\n" + 
					"\r\n" + 
					"    (\r\n" + 
					"        SELECT EMPLOYEE_FIRSTNAME\r\n" + 
					"        FROM employee_mst\r\n" + 
					"        WHERE EMPLOYEE_ID = iteam.EMPLOYEE_ID\r\n" + 
					"    ) AS EMPLOYEE_FIRSTNAME,\r\n" + 
					"\r\n" + 
					"    dtl.*,\r\n" + 
					"    uom.UOM_SHORT_DESCRIPTION,\r\n" + 
					"    itm.INDENT_TYPE_DESC,\r\n" + 
					"    hdr.INDENT_TYPE_CODE,\r\n" + 
					"    (SELECT \r\n" + 
					"		  case when DM_ID > 0 then DM_ID ELSE 0 END AS DM_ID  \r\n" + 
					"	  FROM \r\n" + 
					"		  document_management \r\n" + 
					"	  WHERE \r\n" + 
					"		  REFERENCE_ID = dtl.INDENT_DTL_ID \r\n" + 
					"	       AND UPLOAD_DOC_TYPE = 'FC015' \r\n" + 
					"	       AND TENANT_ID = dtl.TENANT_ID \r\n" + 
					"	  ORDER BY VERSION DESC \r\n" + 
					"	  LIMIT 1) AS DM_ID,\r\n" +
					"(SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN FILE_NAME_EXTN = 'pdf' THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS IS_PDF\r\n" + 
					"FROM\r\n" + 
					"    document_management dm\r\n" + 
					"        INNER JOIN\r\n" + 
					"    file_manager fm ON dm.DM_ID = fm.REFERNCE_ID\r\n" + 
					"WHERE\r\n" + 
					"    dm.REFERENCE_ID = dtl.INDENT_DTL_ID\r\n" + 
					"        AND UPLOAD_DOC_TYPE = 'FC015'\r\n" + 
					"        AND dm.TENANT_ID = dtl.TENANT_ID\r\n" + 
					"ORDER BY dm.VERSION DESC\r\n" + 
					"LIMIT 1) AS IS_PDF \r\n"  +
//					"        (SELECT COUNT(DISTINCT pd.po_id) AS PO_COUNT \r\n" + 
//					"FROM po_dtl pd\r\n" + 
//					"INNER JOIN po_hdr ph ON pd.PO_ID = pd.PO_ID\r\n" + 
//					"WHERE INDENT_DTL_ID IN (\r\n" + 
//					"    SELECT INDENT_DTL_ID\r\n" + 
//					"    FROM indent_dtl id\r\n" + 
//					"    INNER JOIN indent_hdr ih ON id.INDENT_ID = ih.INDENT_ID\r\n" + 
//					"    WHERE PRODUCT_CODE = dtl.PRODUCT_CODE\r\n" + 
//					"    AND ih.PROJECT_ID = ?\r\n" + 
//					"    AND id.TENANT_ID = ?\r\n" + 
//					"    ORDER BY INDENT_DTL_ID DESC\r\n" + 
//					")) AS PO_COUNT\r\n" + 
					"FROM\r\n" + 
					"    (SELECT @a:=0) AS a,\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    uom_mst uom ON dtl.UNIT = uom.UOM_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_type_mst itm ON itm.INDENT_TYPE_CODE = hdr.INDENT_TYPE_CODE\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    indent_assign_team iteam ON dtl.INDENT_DTL_ID = iteam.INDENT_DTL_ID\r\n" + 
					"        AND iteam.IS_PRIMARY = 0\r\n" + 
					"WHERE\r\n" + 
					"    dtl.INDENT_ID = ? \r\n" + 
					"        AND dtl.TENANT_ID = ?;";
			list = this.jdbcTemplate.query(getDtlList, new IndentDtlTblRowMapper(), indentId, tenantId);
		} catch (Exception ex) {
			logger.error("getIndentDtlsByIndentId method Error" + ex);
		}
		return list;
	}

	@Override
	public List<IndentDtlTblEntity> getIndentDtlsByIndentDtlId(String indentDtlId, String tenantId) {
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
		try {
			String getDtlList = "SELECT \r\n" + "    @a:=@a + 1 AS S_NO,\r\n" + "    dtl.*,\r\n"
					+ "    itm.INDENT_TYPE_DESC,\r\n" + "    uom.UOM_SHORT_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    (SELECT @a:=0) AS a,\r\n" + "    indent_dtl dtl\r\n" + "        INNER JOIN\r\n"
					+ "    uom_mst uom ON dtl.UNIT = uom.UOM_CODE\r\n" + "        INNER JOIN\r\n"
					+ "    indent_hdr hdr ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + "        INNER JOIN\r\n"
					+ "    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + "WHERE\r\n"
					+ "    dtl.INDENT_DTL_ID = '" + indentDtlId + "'\r\n" + "        AND dtl.TENANT_ID = '" + tenantId
					+ "'";
			list = this.jdbcTemplate.query(getDtlList, new IndentDtlTblRowMapper());
		} catch (Exception ex) {
			logger.error("getIndentDtlsByIndentDtlId method Error" + ex);
		}
		return list;
	}

	@Override
	public List<IndentHdrDtlsEntity> getIndentHdrDtlsByProjectId(String projectId, String tenantId, String dept) {
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();
		try {
			String qry = "SELECT \r\n" + "    INDENT_CODE,\r\n" + "    INDENT_ID,\r\n" + "    hdr.CREATED_DATE,\r\n"
					+ "    EMPLOYEE_FIRSTNAME AS CREATED_BY,EMPLOYEE_ID,\r\n" + "    proj.PROJECT_NAME,\r\n"
					+ "    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    sbc.SBC_DESC,\r\n" + "    pk.PK_DESC,\r\n"
					+ "    pksa.PKSA_ID,\r\n" + "    hdr.EXPECTED_DELIVERY_DATE,\r\n" + "    psk.PSK_DESC,\r\n"
					+ "    itm.INDENT_TYPE_DESC,hdr.CLOSED_DATE AS CLOSED_DATE ,hdr.TARGET_VALUE,hdr.REVISION_NO,hdr.REVISION_DATE\r\n" + "FROM\r\n"
					+ "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + "WHERE\r\n"
					+ "    PROJECT_ID = ? AND hdr.TENANT_ID = ?  AND itm.DEPARTMENT_CODE like '%" + dept + "%' \r\n"
					+ "ORDER BY hdr.EXPECTED_DELIVERY_DATE ASC";
			list = this.jdbcTemplate.query(qry, new IndentHdrDtlsRowMapper(), projectId, tenantId);

		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByProjectId Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public List<IndentTypeMstTblEntity> getIndentTypeMstDropDown(String tenantID, String deptCode, String isInternal) {
		List<IndentTypeMstTblEntity> list = new ArrayList<IndentTypeMstTblEntity>();
		try {
			if (isInternal == null || isInternal.equalsIgnoreCase("")) {
				isInternal = "0";
			}
			if (deptCode != null) {
				String qry = "select * from indent_type_mst where TENANT_ID= ? and  find_in_set( ? ,TRIM(DEPARTMENT_CODE)) and IS_INTERNAL  = ? ;";
				list = this.jdbcTemplate.query(
						qry,
						new IndentTypeMstTblRowMapper(),
						tenantID.trim(),
						deptCode.trim(),    // ensures no spaces
						Integer.parseInt(isInternal.toString())
				);

			} else {
				String qry = "select * from indent_type_mst where TENANT_ID= ? and IS_INTERNAL  = ? ";
				list = this.jdbcTemplate.query(qry, new IndentTypeMstTblRowMapper(), tenantID, isInternal);

			}
		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public boolean getAssignedStatus(String indentId, String tenantId){
		boolean assignedStatus = false;
		try{
			String qry = "    SELECT \n" +
					"    (\n" +
					"        SELECT \n" +
					"            emp.EMPLOYEE_FIRSTNAME\n" +
					"        FROM\n" +
					"            employee_mst emp\n" +
					"        WHERE\n" +
					"            emp.EMPLOYEE_ID = iteam.EMPLOYEE_ID\n" +
					"    ) AS EMPLOYEE_FIRSTNAME\n" +
					"FROM\n" +
					"    indent_hdr hdr\n" +
					"INNER JOIN\n" +
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\n" +
					"LEFT JOIN\n" +
					"    indent_assign_team iteam ON dtl.INDENT_DTL_ID = iteam.INDENT_DTL_ID\n" +
					"    AND iteam.IS_PRIMARY = 0\n" +
					"WHERE\n" +
					"    dtl.INDENT_ID = ?\n" +
					"    AND dtl.TENANT_ID = ?;";
			List<String> result = this.jdbcTemplate.queryForList(qry, String.class, indentId, tenantId);
			assignedStatus = result.stream()
					.anyMatch(val -> val == null || "null".equalsIgnoreCase(val));
		} catch (Exception ex) {
			logger.error("getAssignedStatus Method Exception --->" + ex);
		}
		return assignedStatus;
	}

	@Override
	public int getNoOfProductsByIndentID(String indentId, String tenantId) {
		int count = 0;
		try {
			String getCount = "select count(*) AS COUNT from indent_dtl where INDENT_ID=? and TENANT_ID=?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,indentId,tenantId);
			count = Integer.parseInt(result.get("COUNT").toString());


		} catch (Exception ex) {
			logger.error("getNoOfProductsByIndentID Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public String getSbcCodeByIndentId(String indentId, String tenantId) {
		String sbcCode = "";
		try {
			String getCount = "select SBC_CODE from indent_hdr where INDENT_ID=? and TENANT_ID=? ;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,indentId,tenantId);
			sbcCode= resultMap.get("SBC_CODE").toString();

		} catch (Exception ex) {
			logger.error("getSbcCodeByIndentId Method Exception --->" + ex);

		}
		return sbcCode;
	}

	@Override
	public String getIndentTypeCodeByIndentId(String indentId, String tenantId) {
		String indentTypeCode = "";
		try {
			String getCount = "select INDENT_TYPE_CODE from indent_hdr where INDENT_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,indentId);
			indentTypeCode= resultMap.get("INDENT_TYPE_CODE").toString();

		} catch (Exception ex) {
			logger.error("getIndentTypeCodeByIndentId Method Exception --->" + ex);

		}
		return indentTypeCode;
	}

	@Override
	public int getCurrentSeqByIndentId(String indentId, String tenantId) {
		int seq = 0;
		try {
			int cnt = 0;
			String getCount = "select count(SEQUENCE_N0) as SEQUENCE_N0 from indent_hdr where INDENT_ID=? and TENANT_ID=? ;";

			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,indentId,tenantId);
			cnt = Integer.parseInt(result.get("SEQUENCE_N0").toString());
			
			if(cnt>0) {
				
			    String getSeq = "select SEQUENCE_N0 from indent_hdr where INDENT_ID=? and TENANT_ID=? ;";
			    Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(getSeq,indentId,tenantId);
			    seq = Integer.parseInt(resultMap.get("SEQUENCE_N0").toString());
			
			}

		} catch (Exception ex) {
			logger.error("getCurrentSeqByIndentId Method Exception --->" + ex);

		}
		return seq;
	}

	@Override
	public int getApprovebtnEnable(String designCode, String docGroup, String tenantId, String docTypeCode,
			String seq) {
		int status = 0;
		try {
			String getStatus = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN 1\r\n"
					+ "        ELSE 0\r\n" + "    END AS result\r\n" + "FROM\r\n" + "    document_lifecycle_mst\r\n"
					+ "WHERE\r\n" + "    FIND_IN_SET(?, APPR_DESI)\r\n" + "        AND DOC_TYPE = ?"
					+ "\r\n" + "        AND CURR_SEQUENCE = ?\r\n"
					+ "        AND TENANT_ID = ?\r\n" + "        AND DOC_GROUP = ?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getStatus,designCode,docTypeCode,seq,tenantId,docGroup);
			status = Integer.parseInt(result.get("result").toString());

		} catch (Exception ex) {
			logger.error("getApprovebtnEnable Error" + ex);
		}
		return status;

	}

	@Override
	public String getIndentDtlIdByProductCode(String productCode, String tenantId, String indentId) {
		String id = "";
		try {
			String getCount = "select case when count(*)>0 then INDENT_DTL_ID else 0 end AS INDENT_DTL_ID from indent_dtl dtl INNER JOIN indent_hdr hdr ON hdr.INDENT_ID = dtl.INDENT_ID where dtl.PRODUCT_CODE=?"
					+ " and dtl.TENANT_ID=? AND hdr.INDENT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,productCode,tenantId,indentId);
			id= resultMap.get("INDENT_DTL_ID").toString();

		} catch (Exception ex) {
			logger.error("getIndentDtlIdByProductCode Method Exception --->" + ex);

		}
		return id;
	}

	@Override
	public int getDmIdByLatestVerion(String indentDtlId, String tenantId) {
		int dmId = 0;
		try {
			String checkCountStr = "select count(*) DM_ID_COUNT from document_management where REFERENCE_ID=?"
					+ " and UPLOAD_DOC_TYPE='FC015' and TENANT_ID=? order by VERSION desc limit 1;";
					Map<String, Object> results = this.jdbcTemplate.queryForMap(checkCountStr,indentDtlId,tenantId);
					int checkCount =  Integer.parseInt(results.get("DM_ID_COUNT").toString());
					if(checkCount>0) {
			String getCount = "select  DM_ID AS DM_ID from document_management where REFERENCE_ID=?"
					+ " and UPLOAD_DOC_TYPE='FC015' and TENANT_ID=? order by VERSION desc limit 1;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,indentDtlId,tenantId);
			dmId = Integer.parseInt(result.get("DM_ID").toString());
					}
		} catch (Exception ex) {
			logger.error("getDmIdByLatestVerion Method Exception --->" + ex);

		}
		return dmId;
	}

	@Override
	public int insertIndentStatusDtl(String indentId, String seq, String seqStatus, String remarks, String tenantId,
			String empId,String docType) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO indent_status_dtl (REFERENCE_ID, REFERENCE_DOC, SEQUENCE_NO, SEQUENCE_STATUS, REMARKS, UPDATED_BY, UPDATED_ON, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			insertStatus = this.jdbcTemplate.update(qry, indentId, docType, seq, seqStatus, remarks, empId,
					CommonMethod.getCurrentDateTime(), tenantId);

		} catch (Exception ex) {
			logger.error("insertIndentStatusDtl method Error" + ex);
		}
		return insertStatus;

	}

	@Override
	public int getScsCountByIndentDtlId(String indentDtlId) {
		int count = 0;
		try {
			String getCount = "select count(*) AS COUNT from indent_grp_scs_dtl where INDENT_DTL_ID=?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getCount,indentDtlId);
			count = Integer.parseInt(result.get("COUNT").toString());


		} catch (Exception ex) {
			logger.error("getScsCountByIndentDtlId Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public int getUngroupedIndentDtlCount(String indentId, String tenantId) {
		int count = 0;
		try {
			String qry = "SELECT COUNT(*) AS COUNT FROM indent_dtl dtl " +
						 "WHERE dtl.INDENT_ID = ? AND dtl.TENANT_ID = ? " +
						 "AND dtl.INDENT_DTL_ID NOT IN " +
						 "(SELECT INDENT_DTL_ID FROM indent_grp_dtl WHERE TENANT_ID = ?)";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry, indentId, tenantId, tenantId);
			count = Integer.parseInt(result.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getUngroupedIndentDtlCount Method Exception --->" + ex);
		}
		return count;
	}

	@Override
	public String getStatusCodebySeqAndDocType(String seq, String tenantId, String docType) {
		String seqStatus = "";
		try {
			String getCount = "select DOC_STATUS from document_lifecycle_mst where CURR_SEQUENCE=?"
					+ " and DOC_TYPE=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,seq,docType,tenantId);
			seqStatus= resultMap.get("DOC_STATUS").toString();

		} catch (Exception ex) {
			logger.error("getStatusCodebySeqAndDocType Method Exception --->" + ex);

		}
		return seqStatus;
	}

	@Override
	public String getIndentCodeByIndentId(String indentId) {
		String indentCode = "";
		try {
			String getCount = "select INDENT_CODE from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,indentId);
			indentCode= resultMap.get("INDENT_CODE").toString();
		} catch (Exception ex) {
			logger.error("getIndentCodeByIndentId Method Exception --->" + ex);

		}
		return indentCode;
	}

	@Override
	public String getProjectIdByIndentId(String indentId) {
		String projectId = "";
		try {
			String getCount = "select PROJECT_ID from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,indentId);
			projectId= resultMap.get("PROJECT_ID").toString();

		} catch (Exception ex) {
			logger.error("getProjectIdByIndentId Method Exception --->" + ex);

		}
		return projectId;
	}

	@Override
	public String getEnqIdByProjectId(String projectId) {
		String enqId = "";
		try {
			String getCount = "select ENQUIRY_ID from project_hdr where PM_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,projectId);
			enqId= resultMap.get("ENQUIRY_ID").toString();

		} catch (Exception ex) {
			logger.error("getEnqIdByProjectId Method Exception --->" + ex);

		}
		return enqId;
	}

	public String getprojIdByEnqId(String enqId) {
		String enquiryId = "";
		try {
			String getCount = "select PM_HDR_ID from project_hdr where ENQUIRY_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,enqId);
			enquiryId= resultMap.get("PM_HDR_ID").toString();

		} catch (Exception ex) {
			logger.error("getEnqIdByProjectId Method Exception --->" + ex);

		}
		return enquiryId;
	}

	@Override
	public String getStatusCodebySeqAndDocTypeByDocGrp(String seq, String tenantId, String docType, String docGrp) {
		String seqStatus = "";
		try {
			String getCount = "select DOC_STATUS from document_lifecycle_mst where CURR_SEQUENCE=?"
					+ " and DOC_TYPE=? and TENANT_ID=? and DOC_GROUP = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,seq,docType,tenantId,docGrp);
			seqStatus= resultMap.get("DOC_STATUS").toString();
		} catch (Exception ex) {
			logger.error("getStatusCodebySeqAndDocTypeByDocGrp Method Exception --->" + ex);

		}
		return seqStatus;
	}

	@Override
	public int updateIndentHdrStatusAndDesig(String indentId, String seq, String seqStatus, String nextApprdesig,
			String empId, String tenantId) {
		int updateStatus = 0;
		String updateDate = CommonMethod.getCurrentDateTime();
		try {
			String qry = "UPDATE indent_hdr SET SEQUENCE_N0=?, SEQUENCE_STATUS=?, NEXT_APPROVING_DESIG=?, LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=?, TENANT_ID=? WHERE INDENT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,seq,seqStatus,nextApprdesig,updateDate,empId,tenantId,indentId);

		} catch (Exception ex) {
			logger.error("updateIndentHdrStatusAndDesig method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String getStartIndentReqStatus(String hdrId, String tenantId) {
		String StartIndentReqStatus = "";
		try {
			String qry = "select case when START_INDENT_REQUEST is null then 0  else CAST(START_INDENT_REQUEST AS UNSIGNED) end as Result from design_hdr where PM_HDR_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,hdrId,tenantId);
			StartIndentReqStatus= resultMap.get("Result").toString();
		} catch (Exception ex) {
			logger.error("getStartIndentReqStatus Method Exception --->" + ex);

		}
		return StartIndentReqStatus;
	}

	@Override
	public String getDocTypeDescByDocType(String docType, String tenantId) {
		String desc = "";
		try {
			String qry = "select DOCUMENT_TYPE_DESCRIPTION from document_type_mst where DOCUMENT_TYPE_CODE=? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,docType);
			desc= resultMap.get("DOCUMENT_TYPE_DESCRIPTION").toString();

		} catch (Exception ex) {
			logger.error("getDocTypeDescByDocType Method Exception --->" + ex);

		}
		return desc;
	}

	@Override
	public String getPmHdrIdByDesignId(String hdrId, String tenantId) {
		String pmHdrId = "";
		try {
			String qry = "select PM_HDR_ID from design_hdr where DE_HDR_ID=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,hdrId,tenantId);
			pmHdrId= resultMap.get("PM_HDR_ID").toString();

		} catch (Exception ex) {
			logger.error("getPmHdrIdByDesignId Method Exception --->" + ex);

		}
		return pmHdrId;
	}

	@Override
	public int getPmSeqByPmHdrId(String pmHdrId, String tenantId) {
		int seq = 0;
		try {
			String qry = "select TRANSACTION_STATUS_SEQ from project_hdr where PM_HDR_ID=? and TENANT_ID=?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,pmHdrId,tenantId);
			seq = Integer.parseInt(result.get("TRANSACTION_STATUS_SEQ").toString());

		} catch (Exception ex) {
			logger.error("getPmSeqByPmHdrId Method Exception --->" + ex);

		}
		return seq;
	}

	@Override
	public int checkLastSeqByPmId(String pmId, int pmSeq, String tenantId) {
		int count = 0;
		try {
			String qry = "select count(*) AS COUNT from process_lifecycle_mst where PM_ID=? and CURRENT_SEQUENCE=? and LAST_SEQ='1'";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,pmId,pmSeq);
			count = Integer.parseInt(result.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("checkLastSeqByPmId Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public int updateStartIndentReq(String designId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE design_hdr SET START_INDENT_REQUEST='0' WHERE PM_HDR_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,designId);

		} catch (Exception ex) {
			logger.error("updateStartIndentReq method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public int getApprovebtnEnableByCurr(String designCode, String tenantId, String docTypeCode, String seq) {
		int status = 0;
		try {
			String getStatus = "SELECT\r\n" + "    CASE  \r\n" + "        WHEN COUNT(*) > 0 THEN 1  \r\n"
					+ "        ELSE 0  \r\n" + "    END AS result  \r\n" + "FROM  \r\n"
					+ "    document_lifecycle_mst  \r\n" + "WHERE  \r\n" + "    FIND_IN_SET(?, APPR_DESI)  \r\n" + "    AND DOC_TYPE = ?  \r\n"
					+ "    AND CURR_SEQUENCE = ?  \r\n" + "    AND TENANT_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getStatus,designCode,docTypeCode,seq,tenantId);
			status = Integer.parseInt(result.get("result").toString());


		} catch (Exception ex) {
			logger.error("getApprovebtnEnableByCurr Error" + ex);
		}
		return status;
	}

	@Override
	public List<IndentRemarksEntity> getIndentRemarks(String indentHdrId, String tenantID) {
		List<IndentRemarksEntity> list = new ArrayList<IndentRemarksEntity>();
		try {
			String qry = "SELECT \r\n" + "    dtl.REMARKS,\r\n" + "    dtl.UPDATED_ON,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    indent_status_dtl dtl,\r\n" + "    employee_mst em,\r\n"
					+ "    document_status_type_code ds\r\n" + "WHERE\r\n" + "    dtl.UPDATED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND dtl.SEQUENCE_STATUS = ds.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        AND REFERENCE_ID = ?\r\n" + "        AND dtl.TENANT_ID = ?";
			list = this.jdbcTemplate.query(qry, new IndentRemarksRowMapper(), indentHdrId, tenantID);

		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int updateBudgetValInPKA(String budgetValue, String pkaId, String operation) {
		int updateStatus = 0;
		String qry ="";
		try {
			if(operation.equalsIgnoreCase("+")) {
				 qry = "UPDATE project_key_area SET BUDGET_VALUE=BUDGET_VALUE + ? WHERE PKA_ID=?";
			}else {
				 qry = "UPDATE project_key_area SET BUDGET_VALUE=BUDGET_VALUE - ? WHERE PKA_ID=?";
			}
			updateStatus = this.jdbcTemplate.update(qry,budgetValue, pkaId);

		} catch (Exception ex) {
			logger.error("updateBudgetValInPKA Method Exception --->" + ex);

		}
		return updateStatus;
	}

	@Override
	public String getPkaIdByIndentId(String indentId) {
		String pkaId = "";
		try {

			String qry = "select PKA_ID from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			pkaId= resultMap.get("PKA_ID").toString();

		} catch (Exception ex) {
			logger.error("getPkaIdByIndentId Method Exception --->" + ex);

		}
		return pkaId;
	}

	public String getBudgetValue(String budgetValue, String indentId) {
		String subBudgetValue = "";
		try {
			String qry = "SELECT \r\n" + "   pka.BUDGET_VALUE AS BUDGET_VALUE\r\n" + "FROM\r\n" + "    indent_hdr AS ihr\r\n"
					+ "        INNER JOIN \r\n" + "    project_key_area AS pka ON ihr.PKA_ID =pka.PKA_ID\r\n"
					+ "    \r\n" + "    where ihr.INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			subBudgetValue= resultMap.get("BUDGET_VALUE").toString();

		} catch (Exception ex) {
			logger.error("addBudgetValue method Error" + ex);
		}
		return subBudgetValue;

	}

	@Override
	public int updateScmBudget(String scmBudgetValue, String indentId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_hdr SET SCM_BUDGET_ALLOCATED=? WHERE INDENT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, scmBudgetValue, indentId);

		} catch (Exception ex) {
			logger.error("updateScmBudget Method Exception --->" + ex);

		}
		return updateStatus;
	}

	public int updateScmBudgetvalByoperation(String scmBudgetValue, String indentId, String operation) {
		int updateStatus = 0;
		String qry="";
		try {
			if (operation.equalsIgnoreCase("+")) {
				qry = "UPDATE indent_hdr SET SCM_BUDGET_ALLOCATED=SCM_BUDGET_ALLOCATED + ? WHERE INDENT_ID=?";
			}else {
				qry = "UPDATE indent_hdr SET SCM_BUDGET_ALLOCATED=SCM_BUDGET_ALLOCATED - ? WHERE INDENT_ID=?";
			}
			updateStatus = this.jdbcTemplate.update(qry, scmBudgetValue, indentId);

		} catch (Exception ex) {
			logger.error("updateScmBudget Method Exception --->" + ex);

		}
		return updateStatus;
	}

	@Override
	public String getEmpIdByProjectCode(String tenantId) {
		String empId = "";
		try {
			String qry = "select MASTER_POC from project_wbs_initiation_mst where PM_ID = '5' and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,tenantId);
			empId= resultMap.get("MASTER_POC").toString();

		} catch (Exception ex) {
			logger.error("updateBudgetDtl Method Exception --->" + ex);

		}
		return empId;
	}

	@Override
	public List<IndentProjectEntity> getIndentByIndentID(IndentRequestEntity indentReq) {
		List<IndentProjectEntity> list = new ArrayList<IndentProjectEntity>();
		try {
			String qry = "SELECT \r\n" + "    pm.PROJECT_CODE,\r\n" + "    pm.PROJECT_NAME,\r\n"
					+ "    hdr.INDENT_CODE,\r\n" + "    hdr.CREATED_DATE,\r\n"
					+ "    hdr.EXPECTED_DELIVERY_DATE,hdr.SCM_BUDGET_ALLOCATED\r\n" + "FROM\r\n"
					+ "    indent_hdr hdr,\r\n" + "    project_hdr pm\r\n" + "WHERE\r\n"
					+ "    hdr.PROJECT_ID = pm.PM_HDR_ID\r\n" + "        AND hdr.INDENT_ID = ?\r\n"
					+ "        AND hdr.TENANT_ID = ?";
			list = this.jdbcTemplate.query(qry, new IndentProjectRowMapper(), indentReq.getIndentId(),
					indentReq.getTenantId());

		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<IndentCostDtlEntity> getCostAnlysDtlslist(String poCheck, String hdrId, String tenantId, String sbcCode) {
		List<IndentCostDtlEntity> list = new ArrayList<IndentCostDtlEntity>();
		try {

			   String qry = 
				    "SELECT \n" +
				    "    hd.INDENT_ID,\n" +
				    "    hd.INDENT_CODE AS INDENT_TYPE_CODE,\n" +
				    "    hd.PKA_ID,\n" +
				    "    hd.PKSA_ID,\n" +
				    "    hd.CREATED_DATE,\n" +
				    "    hd.BUDGET_VALUE,\n" +
				    "    hd.TARGET_VALUE,\n" +
				    "    CASE\n" +
				    "        WHEN (SELECT COUNT(*) FROM po_hdr WHERE po_hdr.INDENT_ID = hd.INDENT_ID) > 0\n" +
				    "            THEN (hd.BUDGET_VALUE - hd.SCM_BUDGET_ALLOCATED)\n" +
				    "        ELSE 0\n" +
				    "    END AS Budget_Status,\n" +
				    "    hd.SCM_BUDGET_ALLOCATED,\n" +
				    "    itm.INDENT_TYPE_DESC,\n" +
				    "    pkm.PK_DESC,\n" +
				    "    pksm.PSK_DESC,\n" +
				    "    hd.INDENT_CODE,\n" +
				    "    hd.SBC_CODE,\n" +
				    "    sbc.SBC_DESC,\n" +
				    "    hd.CLOSED_DATE, (select case when sum(dn_value) > 0 then sum(dn_value) else 0 end from debit_note dn inner join project_hdr hdr \r\n" + 
				    "        on hdr.PM_HDR_ID = dn.PM_HDR_ID where hdr.PM_HDR_ID = ?) as DN_VALUE\n" +
				    "FROM\n" +
				    "    indent_hdr AS hd\n" +
				    "    INNER JOIN indent_type_mst itm ON hd.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\n" +
				    "    INNER JOIN project_key_area pk ON hd.PKA_ID = pk.PKA_ID\n" +
				    "    INNER JOIN project_key_area_mst pkm ON pk.PK_ID = pkm.PK_ID\n" +
				    "    INNER JOIN project_key_sub_area pks ON hd.PKSA_ID = pks.PKSA_ID\n" +
				    "    INNER JOIN project_key_sub_area_mst pksm ON pks.PSK_ID = pksm.PSK_ID\n" +
				    "    INNER JOIN sales_budget_category sbc ON sbc.SBC_CODE = hd.SBC_CODE\n" +
				    "WHERE\n" +
				    "    hd.PROJECT_ID = ?\n" +
				    "    AND hd.TENANT_ID = ?\n" +
				    "    AND hd.SBC_CODE = ?;";
			list = this.jdbcTemplate.query(qry, new IndentCostRowMapper(),hdrId, hdrId, tenantId, sbcCode);

		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public BigDecimal getTotalQty(String indentDtlId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN SUM(ALLOCATED_QTY)\r\n"
					+ "        ELSE 0\r\n" + "    END AS SUM\r\n" + "FROM\r\n" + "    indent_budget_dtl mst\r\n"
					+ "WHERE\r\n" + "    mst.INDENT_DTL_ID =?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(valSumStr,indentDtlId);
			qResp = new BigDecimal(result.get("SUM").toString());

		} catch (Exception ex) {
			logger.error("getTotalQty error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public BigDecimal getTotalValue(String indentDtlId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN SUM(ALLOCATED_VALUE)\r\n" + "        ELSE 0\r\n"
					+ "    END AS SUM\r\n" + "FROM\r\n" + "    indent_budget_dtl mst\r\n" + "WHERE\r\n"
					+ "    mst.INDENT_DTL_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(valSumStr,indentDtlId);
			qResp = new BigDecimal(result.get("SUM").toString());
		} catch (Exception ex) {
			logger.error("getTotalValue error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public String getAvailableValue(String indentId) {
		String availableValue = "";
		try {
			String qry = "SELECT \r\n" + "   ABS ((pka.ALLOCATED_VALUE) - (pka.BUDGET_VALUE)) AS availableValue\r\n"
					+ "FROM\r\n" + "    indent_hdr AS ihr\r\n" + "        INNER JOIN\r\n"
					+ "   project_key_area AS pka ON ihr.PKA_ID=pka.PKA_ID\r\n" + "WHERE\r\n" + "    ihr.INDENT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			availableValue= resultMap.get("availableValue").toString();

		} catch (Exception ex) {
			logger.error("getAvailableValue method Error" + ex);
		}
		return availableValue;

	}

	@Override
	public String getTargetValue(String indentId) {
		String targetValue = "";
		try {
			String qry = "SELECT \r\n" + "    TARGET_VALUE \r\n" + "FROM\r\n" + "    indent_hdr\r\n"
					+ "WHERE\r\n" + "    INDENT_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			targetValue= resultMap.get("TARGET_VALUE").toString();

		} catch (Exception ex) {
			logger.error("getTargetValue method Error" + ex);
		}
		return targetValue;

	}

	@Override
	public String getBudgetValues(String indentId,String pkaId) {
		String budgetValue = "";
		try {
			String qry = "select case when  sum(BUDGET_VALUE) is null then 0 else sum(BUDGET_VALUE) end AS BUDGET_VALUE from indent_budget_dtl where INDENT_ID = ? and PKA_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId,pkaId);
			budgetValue= resultMap.get("BUDGET_VALUE").toString();

		} catch (Exception ex) {
			logger.error("getBudgetValue method Error" + ex);
		}
		return budgetValue;

	}

	@Override
	public BigDecimal getindentBudgetval(String indentId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN SUM(BUDGET_VALUE)\r\n"
					+ "        ELSE 0\r\n" + "    END sum\r\n" + "FROM\r\n" + "    indent_hdr mst\r\n" + "WHERE\r\n"
					+ "    mst.INDENT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(valSumStr,indentId);
			qResp= new BigDecimal(resultMap.get("sum").toString());
		} catch (Exception ex) {
			logger.error("getindentBudgetval error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int updateIndentCostDtl(String budgetValue, String targetValue, String expectedDate, String indentId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_hdr SET TARGET_VALUE=?,EXPECTED_DELIVERY_DATE=?,BUDGET_VALUE=? WHERE INDENT_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, targetValue, expectedDate, budgetValue, indentId);

		} catch (Exception ex) {
			logger.error("updateIndentCostDtl method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public List<BudgetKeyCategory> getbudgetKeyCategoryList(String tenantId) {
		List<BudgetKeyCategory> catLi = null;
		String getQ = "select * from sales_budget_category where TENANT_ID=?";
		catLi = this.jdbcTemplate.query(getQ, new SalesBudgetRowMapper(), tenantId);
		return catLi;
	}

	@Override
	public BigDecimal totalsalesBudget(String pmId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "SELECT \r\n"
					+ "    case when count(*) >0 then (buf.TOTAL_BUDGET_COST + CR_COST) else 0 end AS cost\r\n" + "FROM\r\n"
					+ "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr prj ON hdr.PROJECT_ID = prj.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    sales_enq_hdr sal ON sal.SE_ID = prj.ENQUIRY_ID\r\n" + "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_hdr buf ON buf.MASTER_ID = sal.SE_ID\r\n"
					+ "     where hdr.PROJECT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(valSumStr,pmId);
			qResp= new BigDecimal(resultMap.get("cost").toString());
			
		} catch (Exception ex) {
			logger.error("totalsalesBudget error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int CheckEmpInIndentAssignTeam(String indentDtlId, String empId) {
		int count = 0;
		try {
			String qry = "select count(*) AS COUNT from indent_assign_team where INDENT_DTL_ID=? and EMPLOYEE_ID=?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId,empId);
			count = Integer.parseInt(result.get("COUNT").toString());
		} catch (Exception e) {
			logger.error("CheckEmpInIndentAssignTeam error " + e);
		}
		return count;
	}

	@Override
	public int CheckIsPrimaryZero(String indentDtlId) {
		int count = 0;
		try {
			String qry = "select count(*) AS COUNT from indent_assign_team where INDENT_DTL_ID=? and IS_PRIMARY='0'";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId);
			count = Integer.parseInt(result.get("COUNT").toString());
		} catch (Exception e) {
			logger.error("CheckIsPrimaryZero error " + e);
		}
		return count;
	}

	@Override
	public String getEmpIdByIndentDtlId(String indentDtlId) {
		String empId = "";
		try {
			String qry = "select case when count(*)>0 then EMPLOYEE_ID else 0 end as EMPLOYEE_ID from indent_assign_team where INDENT_DTL_ID=?"
					+ " and IS_PRIMARY='0'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId);
			empId= resultMap.get("EMPLOYEE_ID").toString();
		} catch (Exception e) {
			logger.error("getEmpIdByIndentDtlId error " + e);
		}
		return empId;
	}

	@Override
	public String getEmpNameByEmpId(String assignTeamEmpId) {
		String empName = "";
		try {
			String qry = "select EMPLOYEE_FIRSTNAME from employee_mst where EMPLOYEE_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,assignTeamEmpId);
			empName= resultMap.get("EMPLOYEE_FIRSTNAME").toString();
		} catch (Exception e) {
			logger.error("getEmpNameByEmpId error " + e);
		}
		return empName;
	}

	@Override
	public List<Inventrytolocentity> getProdLcnList(String tenantId) {
		// TODO Auto-generated method stub
		List<Inventrytolocentity> lcnlist = null;
		try {
			String lcnQry = "select * from inventory_location_mst where TENANT_ID='" + tenantId + "';\r\n";
			RowMapper<Inventrytolocentity> rowmapper = new InventrytolocRowMapper();
			lcnlist = this.jdbcTemplate.query(lcnQry, rowmapper);
		} catch (Exception ex) {
			logger.error("getProdLcnList error " + ex);
		}
		return lcnlist;
	}

	@Override
	public String insertInvProdDtl(int prodId, String tolocationcode, String tenantId) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			String insQry = "INSERT INTO `inventory_product_dtl`\r\n" + "(\r\n" + "`PRODUCT_ID`,\r\n"
					+ "`INVENTORY_LOCATION_CODE`,\r\n" + "`PRODUCT_QUANTITY_ON_HAND`,\r\n" + "`TENANT_ID`)\r\n"
					+ "VALUES\r\n" + "(?,?,?,?);";
			this.jdbcTemplate.update(insQry, prodId, tolocationcode, "0.00", tenantId);
		} catch (Exception ex) {
			logger.error("insertInvProdDtl error " + ex);
		}
		return response;
	}

	@Override
	public String totalbudgetConsumed(String indentId) {
		String response = "";
		try {
			String insQry = "SELECT \r\n"
					+ "  case when  SUM(L1_EXTN_FINAL_BASIC_TOTAL) is null then 0 else SUM(L1_EXTN_FINAL_BASIC_TOTAL) end  AS toatlBudgetConsumed\r\n"
					+ "FROM\r\n" + "    indent_grp_scs scs\r\n" + "        INNER JOIN\r\n"
					+ "    indent_grp_scs_ven_dtl dtl ON dtl.IG_SCS_ID = scs.IG_SCS_ID\r\n" + "WHERE\r\n"
					+ "    INDENT_ID =?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(insQry,indentId);
			response= resultMap.get("toatlBudgetConsumed").toString();
		} catch (Exception ex) {
			logger.error("totalbudgetConsumed error " + ex);
		}
		return response;
	}

	@Override
	public List<GetBudgetDtlByIndentEntity> getBudgetDtlByIndent(IndentRequestEntity indentReq) {
		List<GetBudgetDtlByIndentEntity> lcnlist = null;
		try {
			/*
			 * String lcnQry = "SELECT \r\n" + "    pkae.PKSE_ID,\r\n" +
			 * "    pkae.PKA_ID,\r\n" + "    pkae.SB_EXTN_ID,\r\n" +
			 * "    pkae.ALLOCATED_QTY,\r\n" + "    pkae.ALLOCATED_VALUE,\r\n" +
			 * "    pkae.ALLOCATED_QTY - pkae.BUDGET_QTY AS BALANCE_QTY,\r\n" +
			 * "    pkae.ALLOCATED_VALUE - pkae.BUDGET_VALUE AS BALANCE_VALUE,\r\n" +
			 * "    extn.ELEMENT_HDR,\r\n" +
			 * "    extn.ELEMENT_DTL,extn.SPECIFICATION AS SPECIFICATION,extn.MAKE AS MAKE,\r\n"
			 * + "    extn.VALUE AS UNIT_PRICE,\r\n" + "    CASE\r\n" +
			 * "        WHEN bdtl.BUDGET_QTY IS NULL THEN '0'\r\n" +
			 * "        ELSE bdtl.BUDGET_QTY\r\n" + "    END AS REQUIRED_QTY,\r\n" +
			 * "    CASE\r\n" + "        WHEN bdtl.BUDGET_VALUE IS NULL THEN '0'\r\n" +
			 * "        ELSE bdtl.BUDGET_VALUE\r\n" + "    END AS REQUIRED_VALUE\r\n" +
			 * "FROM\r\n" + "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n" +
			 * "    project_key_area_extn pkae ON pkae.PKA_ID = hdr.PKA_ID\r\n" +
			 * "        INNER JOIN\r\n" +
			 * "    sales_budget_sheet_extn extn ON extn.SB_EXTN_ID = pkae.SB_EXTN_ID\r\n" +
			 * "        LEFT JOIN\r\n" +
			 * "    indent_budget_dtl bdtl ON bdtl.PKA_ID = pkae.PKA_ID\r\n" +
			 * "        AND bdtl.SB_EXTN_ID = pkae.SB_EXTN_ID AND bdtl.INDENT_ID = hdr.INDENT_ID \r\n"
			 * + "WHERE\r\n" + "    hdr.INDENT_ID = ? \r\n" +
			 * "        AND pkae.ALLOCATED_QTY - pkae.BUDGET_QTY > 0 and hdr.TENANT_ID=? ";
			 */

			String lcnQry = "SELECT \r\n" + 
					"    pkae.PKSE_ID,\r\n" + 
					"    pkae.PKA_ID,\r\n" + 
					"    pkae.SB_EXTN_ID,\r\n" + 
					"    pkae.ALLOCATED_QTY,\r\n" + 
					"    pkae.ALLOCATED_VALUE,\r\n" + 
					"    case when pkae.ALLOCATED_QTY - pkae.BUDGET_QTY <=0 then 0 else pkae.ALLOCATED_QTY - pkae.BUDGET_QTY end AS BALANCE_QTY,\r\n" + 
					"    case when pkae.ALLOCATED_VALUE - pkae.BUDGET_VALUE <=0 then 0 else pkae.ALLOCATED_VALUE - pkae.BUDGET_VALUE end  AS BALANCE_VALUE,\r\n" + 
					"    extn.ELEMENT_HDR,\r\n" + 
					"    extn.ELEMENT_DTL,\r\n" + 
					"    extn.SPECIFICATION AS SPECIFICATION,\r\n" + 
					"    extn.MAKE AS MAKE,\r\n" + 
					"    extn.VALUE AS UNIT_PRICE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN bdtl.BUDGET_QTY IS NULL THEN '0'\r\n" + 
					"        ELSE bdtl.BUDGET_QTY\r\n" + 
					"    END AS REQUIRED_QTY,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN bdtl.BUDGET_VALUE IS NULL THEN '0'\r\n" + 
					"        ELSE bdtl.BUDGET_VALUE\r\n" + 
					"    END AS REQUIRED_VALUE\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_extn pkae ON pkae.PKA_ID = hdr.PKA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    sales_budget_sheet_extn extn ON extn.SB_EXTN_ID = pkae.SB_EXTN_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    indent_budget_dtl bdtl ON bdtl.PKA_ID = pkae.PKA_ID\r\n" + 
					"        AND bdtl.SB_EXTN_ID = pkae.SB_EXTN_ID\r\n" + 
					"        AND bdtl.INDENT_ID = hdr.INDENT_ID\r\n" + 
					"WHERE\r\n" + 
					"	hdr.INDENT_ID = ? AND\r\n" + 
					"         hdr.TENANT_ID = ? ";
			
			lcnlist = this.jdbcTemplate.query(lcnQry, new GetBudgetDtlByIndentRowMapper(), indentReq.getIndentId(),
					indentReq.getTenantId());
		} catch (Exception ex) {
			logger.error("getBudgetDtlByIndent error " + ex);
		}
		return lcnlist;
	}

	@Override
	public int updateIndentBudgetDtl(List<SalesIndentBudgetDtlEntity> salesIndentBudgetDtlReq) {
		int updateStatus = 0;
		try {
			for (int i = 0; i < salesIndentBudgetDtlReq.size(); i++) {
				String checkCountStr = "select  case when count(*)>0 then INDENT_BUD_ID else '0' end As INDENT_BUD_ID from indent_budget_dtl where INDENT_ID =?"
						+ " and PKA_ID =? and SB_EXTN_ID = ? and TENANT_ID = ?";
				Map<String, Object> result = this.jdbcTemplate.queryForMap(checkCountStr,salesIndentBudgetDtlReq.get(i).getIndentId(),
						salesIndentBudgetDtlReq.get(i).getPkaId(),salesIndentBudgetDtlReq.get(i).getSbExtnId(),salesIndentBudgetDtlReq.get(i).getTenantId());
				int checkCount = Integer.parseInt(result.get("INDENT_BUD_ID").toString());


				if (checkCount > 0) {
					String updateIndBudDtl = "UPDATE `indent_budget_dtl` SET `BUDGET_QTY`= ?,`BUDGET_VALUE`= ? WHERE `INDENT_BUD_ID`=? ";
					updateStatus = this.jdbcTemplate.update(updateIndBudDtl,
							salesIndentBudgetDtlReq.get(i).getRequiredQty(),
							salesIndentBudgetDtlReq.get(i).getRequiredValue(), checkCount);
				} else {
					String insertindBudDtl = "INSERT INTO `indent_budget_dtl` ( `INDENT_ID`, `PKA_ID`, `SB_EXTN_ID`, `BUDGET_QTY`, `BUDGET_VALUE`, `TENANT_ID`) VALUES (?,?, ?,?,?,?)";
					updateStatus = this.jdbcTemplate.update(insertindBudDtl,
							salesIndentBudgetDtlReq.get(i).getIndentId(), salesIndentBudgetDtlReq.get(i).getPkaId(),
							salesIndentBudgetDtlReq.get(i).getSbExtnId(),
							salesIndentBudgetDtlReq.get(i).getRequiredQty(),
							salesIndentBudgetDtlReq.get(i).getRequiredValue(),
							salesIndentBudgetDtlReq.get(i).getTenantId());
				}
				if (updateStatus > 0) {
//					BigDecimal finalBudgetQty =new BigDecimal(salesIndentBudgetDtlReq.get(i).getRequiredQty()).subtract(new BigDecimal(salesIndentBudgetDtlReq.get(i).getOldRequiredQty())).abs();
//					BigDecimal finalBudgetval = new BigDecimal(salesIndentBudgetDtlReq.get(i).getRequiredValue()).subtract(new BigDecimal(salesIndentBudgetDtlReq.get(i).getOldRequiredValue())).abs();
					
					String finalBudgetQtyQry = "select case when sum(BUDGET_QTY)<=0 || BUDGET_QTY is null then 0 else sum(BUDGET_QTY) end as BUDGET_QTY from indent_budget_dtl where PKA_ID= ?  and SB_EXTN_ID= ?;";
					String finalBudgetValueQry = "select case when sum(BUDGET_VALUE)<=0 || BUDGET_VALUE is null then 0 else sum(BUDGET_VALUE) end as BUDGET_VALUE from indent_budget_dtl where PKA_ID= ? and SB_EXTN_ID= ?;";
					
					Map<String, Object> finalBudgetQtyMap = this.jdbcTemplate.queryForMap(finalBudgetQtyQry,salesIndentBudgetDtlReq.get(i).getPkaId(),salesIndentBudgetDtlReq.get(i).getSbExtnId());
					String budgetQty = finalBudgetQtyMap.get("BUDGET_QTY").toString();
					
					
					Map<String, Object> finalBudgetValueMap = this.jdbcTemplate.queryForMap(finalBudgetValueQry,salesIndentBudgetDtlReq.get(i).getPkaId(),salesIndentBudgetDtlReq.get(i).getSbExtnId());
					String budgetValue = finalBudgetValueMap.get("BUDGET_VALUE").toString();
					
					BigDecimal finalBudgetQty= new BigDecimal(budgetQty);
					BigDecimal finalBudgetval= new BigDecimal(budgetValue);
					
					String udpateProjectKeyAreaExtnStr = "UPDATE `project_key_area_extn` SET `BUDGET_QTY`= ?, `BUDGET_VALUE`= ? WHERE PKA_ID = ? and SB_EXTN_ID = ? ";
					this.jdbcTemplate.update(udpateProjectKeyAreaExtnStr, finalBudgetQty,finalBudgetval,salesIndentBudgetDtlReq.get(i).getPkaId(),
							salesIndentBudgetDtlReq.get(i).getSbExtnId());
					String budgetvalStr = projectDAO.getBudgetValSum(salesIndentBudgetDtlReq.get(i).getPkaId());
					projectDAO.updateBudgetVal(budgetvalStr,salesIndentBudgetDtlReq.get(i).getPkaId());
				}
			}
		} catch (Exception ex) {
			logger.error("updateTargetValInIndent method Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<IndentHdrDtlsEntity> getIndentHdrDtlsByProjectIdAndType(String projectId, String tenantId,
			String indentType) {
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();
		try {
			String qry = "SELECT \r\n" + "    INDENT_CODE,\r\n" + "    INDENT_ID,\r\n" + "    hdr.CREATED_DATE,\r\n"
					+ "    EMPLOYEE_FIRSTNAME AS CREATED_BY,\r\n" + "    proj.PROJECT_NAME,\r\n"
					+ "    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    sbc.SBC_DESC,\r\n" + "    pk.PK_DESC,\r\n"
					+ "    pksa.PKSA_ID,\r\n" + "    hdr.EXPECTED_DELIVERY_DATE,\r\n" + "    psk.PSK_DESC,\r\n"
					+ "    itm.INDENT_TYPE_DESC,hdr.SEQUENCE_N0,hdr.CLOSED_DATE  \r\n" + "FROM\r\n"
					+ "    indent_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + "WHERE\r\n"
					+ "    PROJECT_ID = ? AND hdr.TENANT_ID = ? AND hdr.INDENT_TYPE_CODE = ? and hdr.SEQUENCE_N0 !='4' \r\n"
					+ "ORDER BY hdr.EXPECTED_DELIVERY_DATE ASC";
			list = this.jdbcTemplate.query(qry, new IndentHdrDtlsRowMapper(), projectId, tenantId, indentType);

		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByProjectIdAndType Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int deleteIndentByIndentDtlId(String indentDtlId) {
		int del = 0;
		try {
			String qry = "delete from indent_dtl where INDENT_DTL_ID=?";
			del = this.jdbcTemplate.update(qry,indentDtlId);

		} catch (Exception e) {
			logger.error("deleteIndentByIndentDtlId method Error" + e);
		}
		return del;
	}

	@Override
	public void deleteFileManager(String dmId) {
		try {
			String qry = "DELETE FROM file_manager WHERE REFERNCE_ID=?";
			this.jdbcTemplate.update(qry,dmId);

		} catch (Exception e) {
			logger.error("deleteFileManager method Error" + e);
		}
	}

	@Override
	public void deleteDocumentManagement(String dmId) {
		try {
			String qry = "DELETE FROM document_management WHERE DM_ID=?";
			this.jdbcTemplate.update(qry,dmId);

		} catch (Exception e) {
			logger.error("deleteDocumentManagement method Error" + e);
		}
	}

	@Override
	public String getIndentNxtAppDesc(String docTypeCode, String currSeq, String docGroup,String tenantId) {
		String desc = "";
		try {
			String ApprovingDescQry = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n"
					+ "        ELSE '' \r\n" + "    END AS APPR_DESI\r\n" + "FROM\r\n" + "    document_lifecycle_mst\r\n"
					+ "WHERE\r\n" + "    DOC_TYPE = ?\r\n" + "        AND DOC_GROUP = ? AND TENANT_ID=?"
					+ "\r\n" + "        AND CURR_SEQUENCE = (SELECT \r\n" + "            NEXT_SEQ\r\n"
					+ "        FROM\r\n" + "            document_lifecycle_mst\r\n" + "        WHERE\r\n"
					+ "            DOC_TYPE = ?\r\n" + "                AND CURR_SEQUENCE = ?"
							+ "\r\n" + "                AND DOC_GROUP = ? AND TENANT_ID=? )";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ApprovingDescQry,docTypeCode,docGroup,tenantId,docTypeCode,currSeq,docGroup,tenantId);
			desc= resultMap.get("APPR_DESI").toString();

		} catch (Exception ex) {
			logger.error("getIndentNxtAppDesc error " + ex);
		}
		return desc;
	}

	@Override
	public int updateIndentHdrStatusAndSeq(String indentId, String seq, String seqStatus, String empId, String tenantId) {
		int updateStatus = 0;
		String updateDate = CommonMethod.getCurrentDateTime();
		try {
			String qry = "UPDATE indent_hdr SET LAST_UPDATED_DATETIME=?, LAST_UPDATED_BY=?, SEQUENCE_N0=?, SEQUENCE_STATUS=?, TENANT_ID=? WHERE INDENT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry,updateDate,empId,seq,seqStatus,tenantId,indentId);

		} catch (Exception ex) {
			logger.error("updateIndentHdrStatusAndSeq method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public List<PodtlsForProductEntity> getLatestPodtls(String productCode, String projectId, String tenantId) {
		List<PodtlsForProductEntity> list = new ArrayList<PodtlsForProductEntity>();
		try {
			String qry = "SELECT \r\n" + "    ph.PO_ID,\r\n" + "    ph.PO_CODE,\r\n" + "    ph.VENDOR_NAME,\r\n"
					+ "    pd.PO_DTL_ID,\r\n" + "    pd.QTY,\r\n" + "    pd.UNITE_RATE,\r\n" + "    pd.TOTAL_VALUE,"
					+ "dtl.DESCRIPTION, dtl.SPECIFICATION, dtl.PRODUCT_CODE, \r\n" 
					+ "                        dtl.MATERIAL, dtl.QTY AS DTL_QTY, dtl.UNIT\r\n"
					+ "FROM\r\n" + "    po_dtl pd\r\n" + "        INNER JOIN\r\n"
					+ "    po_hdr ph ON pd.PO_ID = pd.PO_ID\r\n"
					+ "  INNER JOIN indent_dtl dtl on dtl.INDENT_DTL_ID= pd.INDENT_DTL_ID\r\n"
					+ "WHERE\r\n" + "    pd.INDENT_DTL_ID IN (SELECT \r\n"
					+ "            INDENT_DTL_ID\r\n" + "        FROM\r\n" + "            indent_dtl id\r\n"
					+ "                INNER JOIN\r\n" + "            indent_hdr ih ON id.INDENT_ID = ih.INDENT_ID \r\n"
					+ "        WHERE\r\n" + "            PRODUCT_CODE = '" + productCode + "'\r\n"
					+ "                AND ih.PROJECT_ID = '" + projectId + "'\r\n"
					+ "                AND id.TENANT_ID = '" + tenantId + "' AND ph.TENANT_ID = '" + tenantId + "'\r\n"
					+ "        ORDER BY INDENT_DTL_ID DESC)\r\n" + "GROUP BY pd.PO_ID ORDER BY pd.INDENT_DTL_ID DESC\r\n"
					+ "LIMIT 3;";
			list = this.jdbcTemplate.query(qry, new PodtlsForProductRowMapper());
		} catch (Exception ex) {
			logger.error("getLatestPodtls method Error" + ex);
		}
		return list;
	}

	public List<IndentDtlTblEntity> getProductDtlsByProductId(String productId, String tenantId, String projectId) {
		// TODO Auto-generated method stub
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();

		try {
			String pmHdrQry = "select PM_HDR_ID from project_hdr where PROJECT_CODE=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(pmHdrQry,projectId);
			String pmhdrId= resultMap.get("PM_HDR_ID").toString();

			String listQry = "SELECT \r\n" + "    @a:=@a + 1 AS S_NO,\r\n" + "    PRODUCT_CODE,\r\n"
					+ "    PRODUCT_DESCRIPTION AS DESCRIPTION,\r\n" + "    SPECIFICATION,\r\n" + "    MAKE,\r\n"
					+ "    QTY,\r\n" + "    UNIT,MATERIAL,\r\n" + "    WEIGHT,\r\n" + "    WEIGHT,\r\n" + "    pm.TENANT_ID,\r\n"
					+ "    um.UOM_SHORT_DESCRIPTION\r\n" + "FROM\r\n" + "    (SELECT @a:=0) AS a,\r\n"
					+ "    product_mst pm\r\n" + "        INNER JOIN\r\n"
					+ "    uom_mst um ON um.UOM_CODE = pm.PRODUCT_UOM_CODE\r\n" + "WHERE\r\n" + "    pm.PRODUCT_ID = '"
					+ productId + "'\r\n" + "        AND pm.PM_HDR_ID ='" + pmhdrId + "' and pm.TENANT_ID='" + tenantId
					+ "';";
			list = this.jdbcTemplate.query(listQry, new IndentDtlTblRowMapper());

		} catch (Exception ex) {
			logger.error("getProductDtlsByProductId error " + ex);
		}
		return list;
	}

	@Override
	public List<IndentRemarksEntity> indentRemarks(String indentId, String tenantId) {
		List<IndentRemarksEntity> list = new ArrayList<IndentRemarksEntity>();
		try {
			String qry = "SELECT \n" + "    dtl1.REMARKS,\n" + "    dtl1.UPDATED_ON,\n" + "    em.EMPLOYEE_FIRSTNAME,\n"
					+ "    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION\n" + "FROM\n" + "    indent_status_dtl dtl1\n"
					+ "        LEFT JOIN\n" + "    indent_status_dtl dtl2 ON (dtl1.REFERENCE_ID = dtl2.REFERENCE_ID\n"
					+ "        AND dtl1.SEQUENCE_NO = dtl2.SEQUENCE_NO\n"
					+ "        AND dtl1.UPDATED_on < dtl2.UPDATED_ON),\n" + "    employee_mst em,\n"
					+ "    document_status_type_code ds\n" + "WHERE\n"
					+ "    dtl1.REFERENCE_ID = ? AND  dtl1.TENANT_ID = ?\n" + "        AND dtl2.ISD_ID IS NULL\n"
					+ "        AND dtl1.UPDATED_BY = em.EMPLOYEE_ID\n"
					+ "        AND dtl1.SEQUENCE_STATUS = ds.DOCUMENT_STATUS_TYPE_CODE AND dtl1.SEQUENCE_STATUS !='DS022'\n"
					+ "GROUP BY dtl1.SEQUENCE_NO  ORDER BY dtl1.SEQUENCE_NO , dtl1.UPDATED_ON DESC";
			list = this.jdbcTemplate.query(qry, new IndentRemarksRowMapper(), indentId, tenantId);

		} catch (Exception ex) {
			logger.error("indentRemarks Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public int indentisFlagReApproval(String indentId, String tenantId) {
		int value = 0;
		try {
			String qry = "SELECT \n" + "    count(dtl2.ISD_ID) as COUNT\n" + "FROM\n" + "    indent_status_dtl dtl1\n"
					+ "        LEFT JOIN\n" + "    indent_status_dtl dtl2 ON (dtl1.REFERENCE_ID = dtl2.REFERENCE_ID\n"
					+ "        AND dtl1.SEQUENCE_NO = dtl2.SEQUENCE_NO\n"
					+ "        AND dtl1.UPDATED_on < dtl2.UPDATED_ON)\n" + "WHERE\n" + "    dtl1.REFERENCE_ID = ?"
				    + " AND dtl1.TENANT_ID =?\n"
					+ "        AND dtl2.ISD_ID IS not NULL AND dtl2.SEQUENCE_NO !='4'\n"
					+ "ORDER BY dtl1.SEQUENCE_NO , dtl1.UPDATED_ON DESC";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentId,tenantId);
			value = Integer.parseInt(result.get("COUNT").toString());


		} catch (Exception ex) {
			logger.error("indentisFlagReApproval Method Exception --->" + ex);

		}
		return value;

	}

	@Override
	public List<GetindentDtlcycEntity> getindentDtlcyclist(String indentId, String tenantId) {
		List<GetindentDtlcycEntity> list = new ArrayList<GetindentDtlcycEntity>();
		try {
			String qry = "SELECT \n" + "     @a:=@a+1 SERIAL_NO,dtl.INDENT_DTL_ID,\n" + "    PRODUCT_CODE,\n"
					+ "    DESCRIPTION,\n" + "    CASE\n" + "        WHEN SUM(dtl.QTY) IS NULL THEN 0\n"
					+ "        ELSE SUM(dtl.QTY)\n" + "    END AS QTY\n" + "FROM\n"
					+ "    (SELECT @a:= 0) AS a,indent_dtl dtl\n" + "        LEFT JOIN\n"
					+ "    indent_hdr hdr ON dtl.INDENT_ID = hdr.INDENT_ID\n" + "WHERE\n"
					+ "    hdr.INDENT_ID =?  AND hdr.TENANT_ID = ? \n" + "GROUP BY dtl.INDENT_DTL_ID";
			list = this.jdbcTemplate.query(qry, new GetindentDtlcycRowMapper(), indentId, tenantId);

		} catch (Exception ex) {
			logger.error("getindentDtlcyclist Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int indentPoDtlval(String indentDtlId, String tenantId) {
		int value = 0;
		try {
			String qry = "SELECT \n" + "    CASE\n" + "        WHEN SUM(dtl.QTY) IS NULL THEN 0\n"
					+ "        ELSE SUM(dtl.QTY)\n" + "    END AS QTY\n" + "FROM\n" + "    indent_dtl indtl\n"
					+ "        LEFT JOIN\n"
					+ "    po_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID LEFT JOIN po_hdr hdr ON dtl.PO_ID = hdr.PO_ID AND hdr.IS_LATEST =1 and hdr.SEQUENCE_NO !=3 \n"
					+ "WHERE\n" + "       indtl.INDENT_DTL_ID = ? AND indtl.TENANT_ID = ?\n" + "GROUP BY indtl.INDENT_DTL_ID";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
			value = Integer.parseInt(result.get("QTY").toString());


		} catch (Exception ex) {
			logger.error("indentPoDtlval Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public int indentMiDtlval(String indentDtlId, String tenantId) {
		int value = 0;
		try {
			String qry = "SELECT \n" + "    CASE\n" + "        WHEN SUM(dtl.RECEIVED_QTY) IS NULL THEN 0\n"
					+ "        ELSE SUM(dtl.RECEIVED_QTY)\n" + "    END AS QTY\n" + "FROM\n" + "    indent_dtl indtl\n"
					+ "        LEFT JOIN\n" + "    material_inward_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
					+ "WHERE\n" + "         indtl.INDENT_DTL_ID = ? AND indtl.TENANT_ID = ?"
					+ "\n" + "GROUP BY indtl.INDENT_DTL_ID";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
			value = Integer.parseInt(result.get("QTY").toString());


		} catch (Exception ex) {
			logger.error("indentMiDtlval Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public int indentGrnDtlVal(String indentDtlId, String tenantId) {
		int value = 0;
		try {
			String qry = "SELECT \n" + "    CASE\n" + "        WHEN SUM(dtl.RECEIVED_QTY) IS NULL THEN 0\n"
					+ "        ELSE SUM(dtl.RECEIVED_QTY)\n" + "    END AS QTY\n" + "FROM\n" + "    indent_dtl indtl\n"
					+ "        LEFT JOIN\n" + "    grn_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n" + "WHERE\n"
					+ "   indtl.INDENT_DTL_ID = ? AND indtl.TENANT_ID = ?\n"
					+ "GROUP BY indtl.INDENT_DTL_ID";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
			value = Integer.parseInt(result.get("QTY").toString());


		} catch (Exception ex) {
			logger.error("indentisFlagReApproval Method Exception --->" + ex);

		}
		return value;
	}

	@Override
	public List<GetIndentDtlProductCostEntity> getIndentDtlProductCost(String productCode, String tenantId, String pmHdrId, String productId) {
		List<GetIndentDtlProductCostEntity> list = new ArrayList<GetIndentDtlProductCostEntity>();
		try {
			pmHdrId = pmHdrId != null ? "and PRODUCT_ID = '"+productId+"'and hdr.PROJECT_ID = '"+pmHdrId+"'" 
						: "and dtl.PRODUCT_CODE = '"+productCode+"'";

			String qry = "SELECT \n" + "    pohdr.VENDOR_NAME,\n" + "    pohdr.DATE,\n" + "    podtl.UNITE_RATE,\n"
					+ "    pohdr.PO_CODE\n" + "FROM\n" + " indent_hdr hdr inner join   indent_dtl dtl on dtl.INDENT_ID = hdr.INDENT_ID\n" + "        INNER JOIN\n"
					+ "    po_dtl podtl ON podtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\n" + "        INNER JOIN\n"
					+ "    po_hdr pohdr ON pohdr.PO_ID = podtl.PO_ID inner join product_mst mst on mst.PRODUCT_CODE = dtl.PRODUCT_CODE\n" + "WHERE\n"
					+ "    dtl.TENANT_ID =? "+pmHdrId+" ";
			list = this.jdbcTemplate.query(qry, new GetIndentDtlProductCostRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getIndentDtlProductCost Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public String indentPoDtlDateTime(String indentDtlId, String tenantId) {
		String list = null;
		try {
			String countCheckStr = "SELECT \n" + "    count(*) AS COUNT \n" + "FROM\n" + "    indent_dtl indtl\n"
					+ "        LEFT JOIN\n" + "    po_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
					+ "        INNER JOIN\n" + "    po_hdr hdr ON dtl.PO_ID = hdr.PO_ID\n"
					+ "        AND hdr.IS_LATEST = 1\n" + "        AND hdr.SEQUENCE_NO != 3\n" + "WHERE\n"
					+ "    indtl.INDENT_DTL_ID = ?\n" + "        AND indtl.TENANT_ID = ? and hdr.IS_APPROVED='1'\n"
					+ " AND hdr.PO_ID is not null\n" + "ORDER BY hdr.LAST_UPDATED_DATETIME desc limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \n" + "    hdr.LAST_UPDATED_DATETIME as LAST_UPDATED_DATETIME \n" + "FROM\n" + "    indent_dtl indtl\n"
						+ "        LEFT JOIN\n" + "    po_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
						+ "        INNER JOIN\n" + "    po_hdr hdr ON dtl.PO_ID = hdr.PO_ID\n"
						+ "        AND hdr.IS_LATEST = 1\n" + "        AND hdr.SEQUENCE_NO != 3\n" + "WHERE\n"
						+ "    indtl.INDENT_DTL_ID = ?\n" + "        AND indtl.TENANT_ID = ? and hdr.IS_APPROVED='1'\n"
						+ "\n" + "ORDER BY hdr.LAST_UPDATED_DATETIME desc limit 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("LAST_UPDATED_DATETIME").toString();

			}
		} catch (Exception ex) {
			logger.error("indentPoDtlDateTime Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public String indentMiDtlDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n" + "					   count(*) as COUNT \n" + "					 FROM\n"
					+ "					     indent_dtl indtl\n" + "					         LEFT JOIN\n"
					+ "					     material_inward_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
					+ "							INNER JOIN\n"
					+ "						material_inward_hdr	hdr ON hdr.MI_ID  = dtl.MI_ID\n"
					+ "					 WHERE\n" + "					          indtl.INDENT_DTL_ID = ?"
					+ " AND indtl.TENANT_ID = ? and hdr.MI_ID is not null\n"
					+ "					 order by hdr.CREATED_ON desc limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck = Integer.parseInt(result.get("COUNT").toString());
			
			if (countCheck > 0) {
				String qry = "SELECT \n" + "					   hdr.CREATED_ON AS CREATED_ON \n" + "					 FROM\n"
						+ "					     indent_dtl indtl\n" + "					         LEFT JOIN\n"
						+ "					     material_inward_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
						+ "							INNER JOIN\n"
						+ "						material_inward_hdr	hdr ON hdr.MI_ID  = dtl.MI_ID\n"
						+ "					 WHERE\n" + "					          indtl.INDENT_DTL_ID = ?"
						+ " AND indtl.TENANT_ID = ?\n"
						+ "					 order by hdr.CREATED_ON desc limit 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("CREATED_ON").toString();
			}
		} catch (Exception ex) {
			logger.error("indentMiDtlDateTime Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public String indentGrnDtlDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n" + "   count(*) AS COUNT\n" + "FROM\n" + "    indent_dtl indtl\n"
					+ "        LEFT JOIN\n" + "    grn_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
					+ "        LEFT JOIN\n" + "    grn_hdr hdr ON dtl.GRN_HDR_ID = hdr.GRN_HDR_ID\n" + "WHERE\n"
					+ "    indtl.INDENT_DTL_ID = ? and  hdr.GRN_HDR_ID is not null  \n"
					+ "        AND indtl.TENANT_ID = ?\n" + "ORDER BY hdr.CREATED_ON DESC limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());

			if (countCheck > 0) {
				String qry = "SELECT \n" + "    hdr.CREATED_ON AS CREATED_ON \n" + "FROM\n" + "    indent_dtl indtl\n"
						+ "        LEFT JOIN\n" + "    grn_dtl dtl ON dtl.INDENT_DTL_ID = indtl.INDENT_DTL_ID\n"
						+ "        LEFT JOIN\n" + "    grn_hdr hdr ON dtl.GRN_HDR_ID = hdr.GRN_HDR_ID\n" + "WHERE\n"
						+ "    indtl.INDENT_DTL_ID = ?\n" + "        AND indtl.TENANT_ID = ?"
						+ "\n" + "ORDER BY hdr.CREATED_ON DESC limit 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("CREATED_ON").toString();
			}
		} catch (Exception ex) {
			logger.error("indentGrnDtlDateTime Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int indentInspectionDtlVal(String indentDtlId, String tenantId) {
		int list = 0;
		try {
			String qry = "SELECT \n" + "    CASE\n" + "        WHEN COUNT(*) > 0 THEN SUM(QTY_INSPECTED)\n"
					+ "        ELSE 0\n" + "    END AS QTY_INSPECTED\n" + "FROM\n" + "    quality_inspection_request\n"
					+ "WHERE\n" + "    INDENT_DTL_ID = ? and TENANT_ID =?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
			list = Integer.parseInt(result.get("QTY_INSPECTED").toString());

		} catch (Exception ex) {
			logger.error("indentInspectionDtlVal Method Exception --->" + ex);

		}
		return list;
	}
	@Override
	public String indentInspectionReqDtlDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n" + "    Count(*) AS COUNT\n" + "FROM\n" + "    quality_inspection_request\n"
					+ "WHERE\n" + "    INDENT_DTL_ID = ?\n" + "        AND TENANT_ID =?"
					+ "\n" + "ORDER BY INSPECTION_REQUESTED_DATE DESC\n" + "LIMIT 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \n" + "    INSPECTION_REQUESTED_DATE\n" + "FROM\n"
						+ "    quality_inspection_request\n" + "WHERE\n" + "    INDENT_DTL_ID = ?\n"
						+ "        AND TENANT_ID = ?\n" + "ORDER BY INSPECTION_REQUESTED_DATE DESC\n"
						+ "LIMIT 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("INSPECTION_REQUESTED_DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("indentInspectionReqDtlDateTime Method Exception --->" + ex);

		}
		return list;
	}
	@Override
	public String indentInspectionDtlDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n" + "    Count(*) AS COUNT\n" + "FROM\n" + "   quality_inspection_request  qir inner join quality_inspection_hdr qih on qir.QI_ID = qih.QI_ID  \n"
					+ "WHERE\n" + "    qir.INDENT_DTL_ID = ?\n" + "        AND qir.TENANT_ID =?"
					+ "\n" + "ORDER BY qih.INSPECTED_ON DESC\n" + "LIMIT 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \n" + "    qih.INSPECTED_ON AS INSPECTED_ON \n" + "FROM\n"
						+ "    quality_inspection_request  qir inner join quality_inspection_hdr qih on qir.QI_ID = qih.QI_ID \n" + "WHERE\n" + "    INDENT_DTL_ID = ?\n"
						+ "        AND qir.TENANT_ID = ?\n" + "ORDER BY qih.INSPECTED_ON DESC\n"
						+ "LIMIT 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("INSPECTED_ON").toString();
			}
		} catch (Exception ex) {
			logger.error("indentInspectionDtlDateTime Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public List<GetIndentLifecycDtlEntity> getIndentSummaryDtlByProjectIdAndType(String projectId, String tenantId,
			String indentType,String indentQry,String pocCheck,String empId,  String fromDate, String toDate) {
		List<GetIndentLifecycDtlEntity> list = new ArrayList<GetIndentLifecycDtlEntity>();
		try {
			
			if(pocCheck.equalsIgnoreCase("1")) {
				String qry = "SELECT \r\n" + 
						"    hdr.INDENT_CODE, \r\n" + 
						"    hdr.INDENT_ID, \r\n" + 
						"    hdr.CREATED_DATE, \r\n" + 
						"    emp.EMPLOYEE_FIRSTNAME AS CREATED_BY, \r\n" + 
						"    proj.PROJECT_NAME, \r\n" + 
						"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION, \r\n" + 
						"    sbc.SBC_DESC, \r\n" + 
						"    pk.PK_DESC, \r\n" + 
						"    pksa.PKSA_ID, \r\n" + 
						"    hdr.EXPECTED_DELIVERY_DATE, \r\n" + 
						"    psk.PSK_DESC, \r\n" + 
						"    itm.INDENT_TYPE_DESC, \r\n" + 
						"    hdr.SEQUENCE_N0, hdr.CLOSED_DATE, \r\n" + 
						"    hdr.CLOSED_DATE, \r\n" + 
						"    dtl.INDENT_DTL_ID, \r\n" + 
						"    dtl.PRODUCT_CODE, \r\n" + 
						"    dtl.DESCRIPTION, \r\n" + 
						"    dtl.QTY AS INDENT_QTY,\r\n" + 
						"    hdr.REVISION_NO,\r\n" + 
						"    hdr.REVISION_DATE, \r\n" + 
						"    dtl.PO_COMPLETED_DATETIME, \r\n" + 
						"    dtl.MI_COMPLETED_DATETIME, \r\n" + 
						"    dtl.GRN_COMPLETED_DATETIME,  \r\n" + 
						"    dtl.QI_REQUESTED_DATETIME, \r\n" + 
						"    dtl.QI_COMPLETED_DATETIME, \r\n" + 
						"    dtl.PJS_COMPLETED_DATETIME, \r\n" + 
						"    (SELECT phdr2.COMPLETED_DATETIME FROM pra_hdr phdr2 \r\n" +
						"     INNER JOIN po_dtl pdtl2 ON pdtl2.PO_ID = phdr2.PO_ID \r\n" +
						"     WHERE pdtl2.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" +
						"     AND phdr2.IS_COMPLETED = 1 AND phdr2.TENANT_ID = hdr.TENANT_ID \r\n" +
						"     ORDER BY phdr2.COMPLETED_DATETIME DESC LIMIT 1) AS PRA_COMPLETED_DATETIME, \r\n" +
						"    (SELECT COALESCE(phdr2.PRA_DATE, phdr2.LAST_UPDATED_DATETIME) FROM pra_hdr phdr2 \r\n" +
						"     INNER JOIN po_dtl pdtl2 ON pdtl2.PO_ID = phdr2.PO_ID \r\n" +
						"     WHERE pdtl2.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" +
						"     AND phdr2.TENANT_ID = hdr.TENANT_ID \r\n" +
						"     ORDER BY phdr2.PRA_ID ASC LIMIT 1) AS PRA_REQUESTED_DATETIME,  mst.PRODUCT_ID,\r\n" +
						" (SELECT VENDOR_NAME FROM po_dtl pdtl inner join \r\n" + 
						"    po_hdr phdr on phdr.PO_ID = pdtl.PO_ID\r\n" + 
						"    where pdtl.INDENT_DTL_ID= dtl.INDENT_DTL_ID and phdr.IS_APPROVED=1\r\n" + 
						"    order by pdtl.INDENT_DTL_ID Desc limit 1) as VENDOR_NAME,\r\n" + 
						"    (SELECT pdtl.DELIVERY_DATE FROM po_dtl pdtl inner join\r\n" + 
						"	po_hdr phdr on phdr.PO_ID = pdtl.PO_ID\r\n" + 
						"    where pdtl.INDENT_DTL_ID= dtl.INDENT_DTL_ID  and phdr.IS_APPROVED=1\r\n" + 
						"    order by pdtl.INDENT_DTL_ID Desc limit 1) AS DELIVERY_DATE, ghdr.IS_INVENTORY, ghdr.IG_HDR_ID, \r\n " +
					    "     ghdr.CREATED_DATETIME as TYPE_CREATED_TIME\r\n" +  
						"FROM indent_hdr hdr\r\n" + 
						"INNER JOIN indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" + 
						"INNER JOIN employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n" + 
						"INNER JOIN project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + 
						"INNER JOIN document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
						"INNER JOIN document_lifecycle_mst dlm ON dlm.CURR_SEQUENCE = hdr.SEQUENCE_N0\r\n" + 
						"INNER JOIN project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n" + 
						"INNER JOIN project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
						"INNER JOIN project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n" + 
						"INNER JOIN project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" + 
						"INNER JOIN sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n" + 
						"INNER JOIN indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + 
						"LEFT JOIN product_mst mst ON mst.PRODUCT_CODE = dtl.PRODUCT_CODE \r\n" +
					    "and dtl.DESCRIPTION = mst.PRODUCT_DESCRIPTION \r\n" +
						" LEFT JOIN indent_grp_dtl gdtl ON gdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"LEFT JOIN indent_grp_hdr ghdr ON ghdr.IG_HDR_ID = gdtl.IG_HDR_ID \r\n" +
						"WHERE hdr.PROJECT_ID = ? \r\n" + 
						"    AND hdr.TENANT_ID = ? \r\n" + 
						"    AND dlm.NEXT_SEQ != '1' \r\n" + 
						"    AND hdr.INDENT_TYPE_CODE = ? "+indentQry+" and hdr.CREATED_DATE between '"+fromDate+"' and '"+toDate+"' \r\n" + 
						"GROUP BY dtl.INDENT_DTL_ID\r\n" + 
						"ORDER BY hdr.EXPECTED_DELIVERY_DATE ASC;\r\n" + 
						"";
			list = this.jdbcTemplate.query(qry, new GetIndentLifecycDtlRowMapper(), projectId, tenantId, indentType);
			}else {
				
				
				String qry="SELECT \r\n" + 
						"    hdr.INDENT_CODE,\r\n" + 
						"    hdr.INDENT_ID,\r\n" + 
						"    hdr.CREATED_DATE,\r\n" + 
						"    EMPLOYEE_FIRSTNAME AS CREATED_BY,\r\n" + 
						"    proj.PROJECT_NAME,\r\n" + 
						"    doc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
						"    sbc.SBC_DESC,\r\n" + 
						"    pk.PK_DESC,\r\n" + 
						"    pksa.PKSA_ID,\r\n" + 
						"    hdr.EXPECTED_DELIVERY_DATE,\r\n" + 
						"    psk.PSK_DESC,\r\n" + 
						"    itm.INDENT_TYPE_DESC,\r\n" + 
						"    hdr.SEQUENCE_N0,\r\n" + 
						"    hdr.CLOSED_DATE,\r\n" + 
						"    dtl.INDENT_DTL_ID,\r\n" + 
						"    dtl.PRODUCT_CODE,\r\n" + 
						"    dtl.DESCRIPTION,\r\n" + 
						"    dtl.QTY AS INDENT_QTY,\r\n" + 
						"    hdr.REVISION_NO, mst.PRODUCT_ID, \r\n" + 
						"    hdr.REVISION_DATE,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(podtl.QTY) >= dtl.QTY THEN '1'\r\n" + 
						"        ELSE 0\r\n" + 
						"    END PO_CHECK,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(midt.RECEIVED_QTY) >= dtl.QTY THEN '1'\r\n" + 
						"        ELSE 0\r\n" + 
						"    END MI_CHECK,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(gdtl.RECEIVED_QTY) >= dtl.QTY THEN '1'\r\n" + 
						"        ELSE 0\r\n" + 
						"    END GRN_CHECK,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(qir.QTY_INSPECTED) >= dtl.QTY THEN '1'\r\n" + 
						"        ELSE 0\r\n" + 
						"    END INSP_CHECK,\r\n" + 
						"    CASE\r\n" + 
						"        WHEN SUM(igd.QTY) >= dtl.QTY THEN '1'\r\n" + 
						"        ELSE 0\r\n" + 
						"    END GROUP_CHECK,\r\n" + 
						" (SELECT VENDOR_NAME FROM po_dtl pdtl inner join \r\n" + 
						"    po_hdr phdr on phdr.PO_ID = pdtl.PO_ID\r\n" + 
						"    where pdtl.INDENT_DTL_ID= dtl.INDENT_DTL_ID and phdr.IS_APPROVED=1\r\n" + 
						"    order by pdtl.PO_DTL_ID Desc limit 1) as VENDOR_NAME,\r\n" + 
						"    (SELECT pdtl.DELIVERY_DATE FROM po_dtl pdtl inner join\r\n" +
						"	po_hdr phdr on phdr.PO_ID = pdtl.PO_ID\r\n" +
						"    where pdtl.INDENT_DTL_ID= dtl.INDENT_DTL_ID  and phdr.IS_APPROVED=1\r\n" +
						"    order by pdtl.PO_DTL_ID Desc limit 1) AS DELIVERY_DATE,\r\n" +
						"    (SELECT phdr2.COMPLETED_DATETIME FROM pra_hdr phdr2 \r\n" +
						"     INNER JOIN po_dtl pdtl2 ON pdtl2.PO_ID = phdr2.PO_ID \r\n" +
						"     WHERE pdtl2.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" +
						"     AND phdr2.IS_COMPLETED = 1 AND phdr2.TENANT_ID = hdr.TENANT_ID \r\n" +
						"     ORDER BY phdr2.COMPLETED_DATETIME DESC LIMIT 1) AS PRA_COMPLETED_DATETIME,\r\n" +
						"    (SELECT COALESCE(phdr2.PRA_DATE, phdr2.LAST_UPDATED_DATETIME) FROM pra_hdr phdr2 \r\n" +
						"     INNER JOIN po_dtl pdtl2 ON pdtl2.PO_ID = phdr2.PO_ID \r\n" +
						"     WHERE pdtl2.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" +
						"     AND phdr2.TENANT_ID = hdr.TENANT_ID \r\n" +
						"     ORDER BY phdr2.PRA_ID ASC LIMIT 1) AS PRA_REQUESTED_DATETIME \r\n" +
						"FROM\r\n" + 
						"    indent_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl dtl ON dtl.INDENT_ID = hdr.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    employee_mst emp ON hdr.CREATED_BY = emp.EMPLOYEE_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    document_status_type_code doc ON hdr.SEQUENCE_STATUS = doc.DOCUMENT_STATUS_TYPE_CODE INNER JOIN document_lifecycle_mst dlm ON dlm.CURR_SEQUENCE = hdr.SEQUENCE_N0 \r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    sales_budget_category sbc ON hdr.SBC_CODE = sbc.SBC_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_type_mst itm ON hdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    po_dtl podtl ON podtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    material_inward_dtl midt ON midt.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    grn_dtl gdtl ON gdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    quality_inspection_request qir ON qir.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    indent_grp_dtl igd ON igd.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_assign_team team ON team.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
						"LEFT JOIN product_mst mst ON mst.PRODUCT_CODE = dtl.PRODUCT_CODE \r\n" +
					    "and dtl.DESCRIPTION = mst.PRODUCT_DESCRIPTION \r\n" +
						"WHERE\r\n" + 
						"    PROJECT_ID = ? \r\n" + 
						"        AND hdr.TENANT_ID = ? \r\n" + 
						"        AND dlm.NEXT_SEQ != '1' \r\n" + 
						"        AND hdr.INDENT_TYPE_CODE = ? \r\n" + 
						"        AND team.EMPLOYEE_ID = '"+empId+"' "+indentQry+"  and hdr.CREATED_DATE between '"+fromDate+"' and '"+toDate+"' \r\n" + 
						"GROUP BY dtl.INDENT_DTL_ID\r\n" + 
						"ORDER BY hdr.EXPECTED_DELIVERY_DATE ASC";
				list = this.jdbcTemplate.query(qry, new GetIndentLifecycDtlRowMapper(), projectId, tenantId, indentType);
				
			}

		} catch (Exception ex) {
			logger.error("getIndentSummaryDtlByProjectIdAndType Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public String praDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n"
					+ "   count(*) AS COUNT\n"
					+ "FROM\n"
					+ "    pra_hdr phdr\n"
					+ "        INNER JOIN\n"
					+ "    pra_dtl pdtl ON phdr.PRA_ID = pdtl.PRA_ID\n"
					+ "		INNER JOIN\n"
					+ "	grn_dtl gdtl ON gdtl.GRN_DTL_ID = pdtl.GRN_DTL_ID\n"
					+ "WHERE\n"
					+ "    gdtl.INDENT_DTL_ID = ?\n"
					+ "        AND phdr.TENANT_ID = ? and phdr.IS_COMPLETED =1  order by phdr.COMPLETED_DATETIME desc limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \n"
						+ "   phdr.COMPLETED_DATETIME AS COMPLETED_DATETIME \n"
						+ "FROM\n"
						+ "    pra_hdr phdr\n"
						+ "        INNER JOIN\n"
						+ "    pra_dtl pdtl ON phdr.PRA_ID = pdtl.PRA_ID\n"
						+ "		INNER JOIN\n"
						+ "	grn_dtl gdtl ON gdtl.GRN_DTL_ID = pdtl.GRN_DTL_ID\n"
						+ "WHERE\n"
						+ "    gdtl.INDENT_DTL_ID = ?\n"
						+ "        AND phdr.TENANT_ID =? and phdr.IS_COMPLETED =1  order by phdr.COMPLETED_DATETIME desc limit 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("COMPLETED_DATETIME").toString();
			}
		} catch (Exception ex) {
			logger.error("praDateTime Method Exception --->" + ex);

		}
		return list;
	}
	@Override
	public String praReqDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \n"
					+ "   count(*) AS COUNT\n"
					+ "FROM\n"
					+ "    pra_hdr phdr\n"
					+ "        INNER JOIN\n"
					+ "    pra_dtl pdtl ON phdr.PRA_ID = pdtl.PRA_ID\n"
					+ "		INNER JOIN\n"
					+ "	grn_dtl gdtl ON gdtl.GRN_DTL_ID = pdtl.GRN_DTL_ID\n"
					+ "WHERE\n"
					+ "    gdtl.INDENT_DTL_ID = ?\n"
					+ "        AND phdr.TENANT_ID = ? and phdr.PRA_ID is not null  order by phdr.PRA_DATE desc limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \n"
						+ "   phdr.PRA_DATE AS PRA_DATE \n"
						+ "FROM\n"
						+ "    pra_hdr phdr\n"
						+ "        INNER JOIN\n"
						+ "    pra_dtl pdtl ON phdr.PRA_ID = pdtl.PRA_ID\n"
						+ "		INNER JOIN\n"
						+ "	grn_dtl gdtl ON gdtl.GRN_DTL_ID = pdtl.GRN_DTL_ID\n"
						+ "WHERE\n"
						+ "    gdtl.INDENT_DTL_ID = ?\n"
						+ "        AND phdr.TENANT_ID =? and phdr.PRA_ID is not null  order by phdr.PRA_DATE desc limit 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("PRA_DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("praDateTime Method Exception --->" + ex);

		}
		return list;
	}
	@Override
	public String indentGroupScsDateTime(String indentDtlId, String tenantId) {
		String list = "";
		try {
			String countCheckStr = "SELECT \r\n" + 
					"    COUNT(*) AS COUNT\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_dtl dtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_hdr hdr ON hdr.IG_HDR_ID = dtl.IG_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_scs scs ON scs.IG_HDR_ID = hdr.IG_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    dtl.INDENT_DTL_ID = ? \r\n" + 
					"        AND hdr.TENANT_ID = ? \r\n" + 
					"        AND hdr.IG_HDR_ID IS NOT NULL and scs.IS_APPROVED='1'\r\n" + 
					"ORDER BY hdr.CREATED_DATETIME DESC\r\n" + 
					"LIMIT 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(countCheckStr,indentDtlId,tenantId);
			int countCheck  = Integer.parseInt(result.get("COUNT").toString());
			if (countCheck > 0) {
				String qry = "SELECT \r\n" + 
						"    scs.LAST_UPDATED_DATETIME AS CREATED_DATETIME\r\n" + 
						"FROM\r\n" + 
						"    indent_grp_dtl dtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_grp_hdr hdr ON hdr.IG_HDR_ID = dtl.IG_HDR_ID\r\n" + 
						"     INNER JOIN\r\n" + 
						"    indent_grp_scs scs ON scs.IG_HDR_ID = hdr.IG_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    dtl.INDENT_DTL_ID = ? AND\r\n" + 
						"         hdr.TENANT_ID = ? and scs.IS_APPROVED='1'\r\n" + 
						"ORDER BY hdr.CREATED_DATETIME DESC\r\n" + 
						"LIMIT 1";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
				list= resultMap.get("CREATED_DATETIME").toString();
			}
		} catch (Exception ex) {
			logger.error("indentGroupScsDateTime Method Exception --->" + ex);

		}
		return list;
	}

	@Override
	public int updateIndentDtl(String productCode, String desc, String spec, String make, String qty, String unit,
			String weight, String material, String remarks, String indentDtlId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_dtl SET PRODUCT_CODE=?, DESCRIPTION=?, SPECIFICATION=?, MAKE=?, QTY=?, UNIT=?, WEIGHT=?, MATERIAL=?, REMARKS=? WHERE INDENT_DTL_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, productCode, desc, spec, make, qty, unit, weight, material,
					remarks, indentDtlId);

		} catch (Exception ex) {
			logger.error("updateIndentDtl method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String getCurIndentSeq(String indentId) {
		String seq = "";
		try {
			String qry = "select SEQUENCE_N0 from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			seq= resultMap.get("SEQUENCE_N0").toString();

		} catch (Exception ex) {
			logger.error("getCurIndentSeq method Error" + ex);
		}
		return seq;

	}
	
	@Override
	public int getCurIndentGrpSeq(String igScsId) {
		int seq = 0; int cnt = 0;
		try { 
			
			String Qry = "select Count(SEQUENCE_NO) as SEQUENCE_NO from indent_grp_scs where IG_SCS_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(Qry,igScsId);
			cnt= Integer.valueOf(resultMap.get("SEQUENCE_NO").toString());

			if(cnt>0) {
				String qry = "select SEQUENCE_NO from indent_grp_scs where IG_SCS_ID=?";
				Map<String, Object> result = jdbcTemplate.queryForMap(qry,igScsId);
				seq= Integer.valueOf(result.get("SEQUENCE_NO").toString());
			}
			
		} catch (Exception ex) {
			logger.error("getCurIndentGrpSeq method Error" + ex);
		}
		return seq;

	}

	@Override
	public String getIndentRevNo(int indentId) {
		String seq = "";
		try {
			String qry = "select REVISION_NO from indent_hdr where INDENT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			seq= resultMap.get("REVISION_NO").toString();

		} catch (Exception ex) {
			logger.error("getIndentRevNo method Error" + ex);
		}
		return seq;
	}
	@Override
	public int getIndentRevCheck(int indentId) {
		int seq = 0;
		try {
			String qry = "Select count(*) AS REV_COUNT from indent_status_dtl where REFERENCE_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			seq=Integer.parseInt(resultMap.get("REV_COUNT").toString()) ;

		} catch (Exception ex) {
			logger.error("getIndentRevCheck method Error" + ex);
		}
		return seq;
	}

	@Override
	public void updateIndentRevNo(String revision, int indentId, String empId) {
		try {
			String qry = "UPDATE indent_hdr SET REVISION_NO=?,REVISION_DATE =?, LAST_UPDATED_BY=?, LAST_UPDATED_DATETIME=? WHERE INDENT_ID=?";
			this.jdbcTemplate.update(qry, revision,CommonMethod.getCurrentDate() ,empId, CommonMethod.getCurrentDateTime(), indentId);

		} catch (Exception ex) {
			logger.error("updateIndentRevNo method Error" + ex);
		}

	}

	@Override
	public int updateProductInProdMst(String desc, String uom, String empId, String specification, String make,
			String qty, String weight, String material, String pkaId, String pksaId, String prodCategory,
			int productId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE product_mst SET PRODUCT_UOM_CODE=?, PRODUCT_DESCRIPTION=?, LAST_UPDATED_USER_ID=?, LAST_UPDATED_DATETIME=?, SPECIFICATION=?, MAKE=?, QTY=?, UNIT=?, WEIGHT=?, MATERIAL=?, PRODUCT_CATEGORY=? WHERE PRODUCT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, uom, desc, empId, CommonMethod.getCurrentDateTime(),
					specification, make, qty, uom, weight, material, prodCategory, productId);

		} catch (Exception ex) {
			logger.error("updateProductInProdMst method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String getIndentHdrId(String indentDtlId) {
		String val="";
		try {
			String qry = "select INDENT_ID from indent_dtl where INDENT_DTL_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId);
			val= resultMap.get("INDENT_ID").toString();

		} catch (Exception ex) {
			logger.error("getIndentHdrId method Error" + ex);
			return "0";
		}
		return val;
	}

	@Override
	public void deleteIndentHdr(String indentHdrId, String tenantId) {
		try {
			String qry = "delete from indent_hdr where INDENT_ID = ? and TENANT_ID = ?";
			this.jdbcTemplate.update(qry,indentHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("deleteIndentHdr method Error" + ex);

		}

	}
	
	@Override
	public void deleteIndentStatusTbl(String indentHdrId, String tenantId) {
		try {
			String qry = "delete from indent_status_dtl where REFERENCE_ID = ? and TENANT_ID = ?";
			this.jdbcTemplate.update(qry,indentHdrId,tenantId);

		} catch (Exception ex) {
			logger.error("deleteIndentStatusTbl method Error" + ex);

		}

	}

	@Override
	public List<InventoryMaterialTransferEntity> getMaterialTransferDtls(PmHdrIdAndTenantIdRequest matDtls) {
		// TODO Auto-generated method stub
		List<InventoryMaterialTransferEntity> list = new ArrayList<InventoryMaterialTransferEntity>();
		try {
			String query = "SELECT \r\n" + "     convert(@a:=@a+1,unsigned) as SERIAL_NUMBER,\r\n"
					+ "    imt.MTR_ID,\r\n" + "    imt.PRODUCT_ID,\r\n" + "    prod.PRODUCT_CODE,\r\n"
					+ "    prod.PRODUCT_DESCRIPTION,um.UOM_LONG_DESCRIPTION,\r\n" + "    imt.FROM_PM_HDR_ID,\r\n"
					+ "    imt.TO_PM_HDR_ID,\r\n" + "    imt.TRANSFER_QUANTITY,\r\n" + "    imt.TRANSFER_DATE,\r\n"
					+ "    imt.CREATED_ON,imt.MATERIAL_TRANSFER_CODE,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS CREATED_BY,\r\n" + "    imt.REASON,\r\n"
					+ "    fph.CUSTOMER_NAME as FROM_CUSTOMER_NAME,\r\n"
					+ "    fph.PROJECT_CODE as FROM_PROJECT_CODE,\r\n"
					+ "    fph.PROJECT_NAME as FROM_PROJECT_NAME,\r\n"
					+ "    tph.CUSTOMER_NAME as TO_CUSTOMER_NAME,\r\n" + "    tph.PROJECT_CODE as TO_PROJECT_CODE,\r\n"
					+ "    tph.PROJECT_NAME as TO_PROJECT_NAME,frilm.INVENTORY_LOCATION_CODE as FROM_LOCATION_CODE,frilm.INVENTORY_LOCATION_DESCRIPTION AS FROM_LOCATION_DESC,\r\n"
					+ "    toilm.INVENTORY_LOCATION_CODE as TO_LOCATION_CODE,toilm.INVENTORY_LOCATION_DESCRIPTION AS TO_LOCATION_DESC,  imt.UINT_COST,\r\n" + 
					"    imt.OVER_ALL_COST, imt.FROM_BIN, imt.TO_BIN \r\n"
					+ "FROM (SELECT @a:= 0) AS a ,\r\n" + "    inventroy_material_transfer imt\r\n"
					+ "        INNER JOIN\r\n" + "    product_mst prod ON prod.PRODUCT_ID = imt.PRODUCT_ID\r\n"
					+ "        INNER JOIN\r\n" + "       uom_mst um ON um.UOM_CODE = prod.PRODUCT_UOM_CODE\r\n"
					+ "        INNER JOIN\r\n" + "    employee_mst em ON em.EMPLOYEE_ID = imt.CREATED_BY\r\n"
					+ "        INNER JOIN\r\n" + "    project_hdr fph ON fph.PM_HDR_ID = imt.FROM_PM_HDR_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_hdr tph ON tph.PM_HDR_ID = imt.TO_PM_HDR_ID\r\n "
					+ "        INNER JOIN\r\n"
					+ "    inventory_location_mst frilm ON frilm.INVENTORY_LOCATION_CODE = imt.FROM_INVENTORY_LOCATION_CODE\r\n"
					+ "        INNER JOIN\r\n"
					+ "    inventory_location_mst toilm ON toilm.INVENTORY_LOCATION_CODE = imt.TO_INVENTORY_LOCATION_CODE\r\n"
					+ "WHERE\r\n" + "imt.TO_PM_HDR_ID = '"+matDtls.getPmHdrId()+"' and  imt.TENANT_ID ='"+matDtls.getTenantId()+"'";

			list = this.jdbcTemplate.query(query, new InventoryMaterialTransferRowMapper());
		} catch (Exception e) {
			logger.error("getMaterialTransferDtls Error" + e);
		}

		return list;
	}

	@Override
	public String getSumOfTransferValue(HdrIdandTenantIdRequest cstDtl) {
		// TODO Auto-generated method stub
		BigDecimal valuecheck=BigDecimal.ZERO;
		try {
			
			String valQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN SUM(OVER_ALL_COST)\r\n" + 
					"        ELSE 0.00\r\n" + 
					"    END AS OVER_ALL_COST\r\n" + 
					"FROM\r\n" + 
					"    inventroy_material_transfer\r\n" + 
					"WHERE\r\n" + 
					"    TO_PM_HDR_ID = ? AND FROM_PM_HDR_ID != TO_PM_HDR_ID AND TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(valQry,cstDtl.getHdrId(),cstDtl.getTenantId());
			valuecheck= new BigDecimal(resultMap.get("OVER_ALL_COST").toString());
			
		}catch(Exception ex) {
			logger.error("getSumOfTransferValue error "+ex);
		}
		return valuecheck.toString();
	}

	@Override
	public List<IndentBudgetDtlByIndentIdEntity> getBudgetByIndentId(String indentId, String tenantId) {
		List<IndentBudgetDtlByIndentIdEntity> list = new ArrayList<IndentBudgetDtlByIndentIdEntity>();
		try {
			String query = "select * from indent_budget_dtl where INDENT_ID = ? and TENANT_ID = ? ";

			list = this.jdbcTemplate.query(query, new IndentBudgetDtlByIndentIdRowMapper(),indentId,tenantId);
		} catch (Exception e) {
			logger.error("IndentBudgetDtlByIndentIdEntity Error" + e);
		}

		return list;
	}

	@Override
	public String getPkseIdBypkaId(String pkaId, String sbExtnId) {
		String val="";
		try {
			String qry = "select case when count(*)>0 then PKSE_ID else '' end AS PKSE_ID  from project_key_area_extn where PKA_ID = ? and SB_EXTN_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,pkaId,sbExtnId);
			val= resultMap.get("PKSE_ID").toString();

		} catch (Exception ex) {
			logger.error("getPkseIdBypkaId method Error" + ex);
			return "0";
		}
		return val;
	}

	@Override
	public String filePathByDmId(String dmId) {
		String val="";
		try {
			String qry = "select FILE_PATH AS FILE_PATH from file_manager where REFERNCE_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,dmId);
			val= resultMap.get("FILE_PATH").toString();

		} catch (Exception ex) {
			logger.error("filePathByDmId method Error" + ex);
			return "0";
		}
		return val;
	}
	@Override
	public String fileNameByDmId(String dmId) {
		String val="";
		try {
			String qry = "select FILE_NAME AS FILE_NAME from file_manager where REFERNCE_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,dmId);
			val= resultMap.get("FILE_NAME").toString();

		} catch (Exception ex) {
			logger.error("fileNameByDmId method Error" + ex);
			return "0";
		}
		return val;
	}
	
	@Override
	public void updatenotification(String indentCode, String doctype, String tenantId) {
		try {
			String updateStr ="update document_approve_dtl set DESIGNATION = '' where DN_APP_ID= (select DN_APP_ID from document_approve_hdr where REFERENCE_CODE =? and DOC_TYPE_CODE =? and TENANT_ID = ?)";
			this.jdbcTemplate.update(updateStr,indentCode,doctype,tenantId);

		} catch (Exception ex) {
			logger.error("updatenotification method Error" + ex);

		}
		
	}
	
	@Override
	public void deleteBudgetExcessTbl(String indentHdrId, String tenantId) {
		try {
			String qry = "delete from budget_excess_dtl where indent_id = ? and tenant_id = ? ";
			this.jdbcTemplate.update(qry,indentHdrId,tenantId);
 
		} catch (Exception ex) {
			logger.error("deleteBudgetExcessTbl method Error" + ex);
 
		}
 
	}
	
	@Override
	public void deleteBudgetExcessStatusTbl(String indentHdrId, String tenantId) {
		try {
			String qry = " delete from budget_excess_status_dtl where BE_HDR_ID in (select BE_HDR_ID from .budget_excess_dtl where indent_id = ? and tenant_id = ?) ";
			this.jdbcTemplate.update(qry,indentHdrId,tenantId);
 
		} catch (Exception ex) {
			logger.error("deleteBudgetExcessStatusTbl method Error" + ex);
 
		}
 
	}

	@Override
	public int getCheckIndentScs(String indentDtlId) {
		int seq = 0;
		try {
			String qry = "select count(*) AS COUNT from indent_grp_dtl where INDENT_DTL_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId);
			seq=Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getCheckIndentScs method Error" + ex);
		}
		return seq;
	}

	public int getCurrentSeqByPoId(String refId, String tENANT_ID) {
		int seq = 0;
		try {
			String qry = "select SEQUENCE_NO from po_hdr where PO_ID = ? and TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,refId, tENANT_ID);
			seq=Integer.parseInt(resultMap.get("SEQUENCE_NO").toString()) ;

		} catch (Exception ex) {
			logger.error("getCurrentSeqByPoId method Error" + ex);
		}
		return seq;
	}

	public BigDecimal getDiffernceQtyByIndent(String indentDtlId, String tenantId) {
		BigDecimal qty = BigDecimal.ZERO;
		try {
			String qry = "select  idtl.QTY - sum(dtl.qty) as diffQty\r\n" + 
					"from indent_grp_dtl dtl inner join indent_dtl idtl\r\n" + 
					"    on dtl.INDENT_DTL_ID = idtl.INDENT_DTL_ID\r\n" + 
					"where dtl.INDENT_DTL_ID = ? and dtl.TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId, tenantId);
			qty = new BigDecimal(resultMap.get("diffQty").toString()) ;

		} catch (Exception ex) {
			logger.error("getCurrentSeqByPoId method Error" + ex);
		}
		return qty;
	}

	@Override
	public String getProjectInitiationMstResp(String pmId, String empId, String tenantId) {
		String res = "";
		try {
			String desig = uploadManagementDAO.getDesigCodeByEmpId(empId, "");
			String qrySelect = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    project_wbs_initiation_mst\r\n"
					+ "WHERE\r\n" + "    FIND_IN_SET( ? , MASTER_POC)\r\n" + "        AND PM_ID = ? AND TENANT_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qrySelect, desig, pmId,tenantId);
			int count = Integer.parseInt(resultMap.get("count").toString());
			if (count > 0) {
				res = "1";
			} else {
				res = "0";
			}
		} catch (Exception ex) {
			logger.error("getProjectInitiationMstResp error " + ex.getMessage());
			res = "2";
		}
		return res;
	}

	@Override
	public int updateIndentPkaAndPksaId(String pkaId, String pksaId, int indentId, String tenantId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE indent_hdr SET PKA_ID=?, PKSA_ID=? WHERE INDENT_ID=? and TENANT_ID=?";
			updateStatus = this.jdbcTemplate.update(qry, pkaId, pksaId, indentId, tenantId);

		} catch (Exception ex) {
			logger.error("updateIndentPkaAndPksaId method Error" + ex);
		}
		return updateStatus;

	}

	@Override
	public String cheackPoCreateOrNot(String indentId, String tenantId) {
	    String res = "";

	    try {
	        String qry = "SELECT INDENT_ID FROM po_hdr WHERE INDENT_ID = ? AND TENANT_ID = ?";
	        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(qry, indentId, tenantId);

	        if (!resultList.isEmpty()) {
	            res = resultList.get(0).get("INDENT_ID").toString();
	        }
	    } catch (Exception ex) {
	        logger.error("cheackPoCreateOrNot method Error", ex);
	    }

	    return res;
	}

	public int getApprovebtnEnable(String designCode, String docGroup, String processDoc, String tenantId,
			String docTypeCode, String seq) {
		int status = 0;
		try {
			String getStatus = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN 1\r\n"
					+ "        ELSE 0\r\n" + "    END AS result\r\n" + "FROM\r\n" + "    document_lifecycle_mst\r\n"
					+ "WHERE\r\n" + "    FIND_IN_SET(?, APPR_DESI)\r\n" + "        AND DOC_TYPE = ?"
					+ "\r\n" + "        AND CURR_SEQUENCE = ?\r\n"
					+ "        AND TENANT_ID = ?\r\n" + "        AND DOC_GROUP = ? and PROCESS_CODE=?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getStatus,designCode,docTypeCode,seq,tenantId,docGroup,processDoc);
			status = Integer.parseInt(result.get("result").toString());

		} catch (Exception ex) {
			logger.error("getApprovebtnEnable Error" + ex);
		}
		return status;

	}

	public String getStatusCodebySeqAndDocTypeByDocGrp(String seq, String tenantId, String docType,
			String docGrp, String processDoc) {
		String seqStatus = "";
		try {
			String getCount = "select DOC_STATUS from document_lifecycle_mst where CURR_SEQUENCE=?"
					+ " and DOC_TYPE=? and TENANT_ID=? and DOC_GROUP = ? and PROCESS_CODE=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getCount,seq,docType,tenantId,docGrp,processDoc);
			seqStatus= resultMap.get("DOC_STATUS").toString();
		} catch (Exception ex) {
			logger.error("getStatusCodebySeqAndDocTypeByDocGrp Method Exception --->" + ex);

		}
		return seqStatus;
	}

	@Override
	public String CheckIndentBudgetVal(int indentId) {
		String seq = "";
		try {
			String qry = "select case when count(*) > 0 then SUM(BUDGET_VALUE) else 0 end as COUNT from indent_budget_dtl where INDENT_ID= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,indentId);
			seq=(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("CheckIndentBudgetVal method Error" + ex);
		}
		return seq;
	}

	@Override
	public String getTypeIndentGrpScs(String igHdrId, String tenantId) {
		String type = "";
		try {
			String qry = "SELECT COALESCE(\r\n" + 
					"           (SELECT TYPE\r\n" + 
					"            FROM indent_grp_scs\r\n" + 
					"            WHERE IG_HDR_ID = ? \r\n" + 
					"              AND TENANT_ID = ?),\r\n" + 
					"           'IS_INVENTORY'\r\n" + 
					"       ) AS TYPE;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,igHdrId,tenantId);
			type=(resultMap.get("TYPE").toString());

		} catch (Exception ex) {
			logger.error("getTypeIndentGrpScs method Error" + ex);
		}
		return type;
	
	}

	@Override
	public String getPmIdbyDepCode(String tenantId, String depCode) {
		String pmId = "";
		try {
			String qry = "select PM_ID from project_wbs_initiation_mst where DEPARTMENT_CODE=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,depCode, tenantId);
			pmId=(resultMap.get("PM_ID").toString());

		} catch (Exception ex) {
			logger.error("getPmIdbyDepCode method Error" + ex);
		}
		return pmId;
	}

}
