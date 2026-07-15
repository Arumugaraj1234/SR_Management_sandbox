package com.vmfg.inventory.dao.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.inventory.dao.interfaces.IInventoryAdjustmentDao;
import com.vmfg.inventory.entity.AdjustmentTypeDropDownEntity;
import com.vmfg.inventory.entity.InventoryAdjustmentEntity;
import com.vmfg.inventory.rowmapper.AdjustmenttypeDropDownRowMapper;
import com.vmfg.inventory.rowmapper.InventoryAdjustmentRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Transactional
@Repository
public class InventoryAdjustmentDao implements IInventoryAdjustmentDao {
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryAdjustmentDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

	@Override
	public List<InventoryAdjustmentEntity> retrieveinventoryAdjustment(String fromDate, String toDate,
			String tenantId) {
		// TODO Auto-generated method stub
		List<InventoryAdjustmentEntity> list = new ArrayList<InventoryAdjustmentEntity>();
		try {
		
		String query = "SELECT \r\n"
				+ "	convert(@a:=@a+1,unsigned) as SERIAL_NUMBER,\r\n"
				+ "    iad.INV_ADJUSTMENT_CODE,\r\n"
				+ "    proj.PROJECT_CODE,\r\n"
				+ "    proj.PROJECT_DESCRIPTION,\r\n"
				+ "    prod.PRODUCT_CODE,\r\n"
				+ "    prod.PRODUCT_DESCRIPTION,\r\n"
				+ "    um.UOM_LONG_DESCRIPTION as UOM,\r\n"
				+ "    iad.INVENTORY_LOCATION_CODE,\r\n"
				+ "    ilm.INVENTORY_LOCATION_DESCRIPTION,\r\n"
				+ "    iadt.ADJUSTMENT_TYPE_DESCRIPTION as ADJUSTMENT_TYPE,\r\n"
				+ "    em.EMPLOYEE_FIRSTNAME AS ADJUSTED_BY,\r\n"
				+ "    iad.ADJUSTMENT_RECORDED_DATE,\r\n"
				+ "    iad.PRODUCT_PRIOR_QUANTITY_ON_HAND,\r\n"
				+ "    iad.PRODUCT_ADJUSTED_QUANTITY,\r\n"
				+ "    iad.PRODUCT_REVISED_QUANTITY_ON_HAND,\r\n"
				+ "    iad.ADJUSTMENT_REASON\r\n"
				+ "FROM\r\n"
				+ "	(SELECT @a:= 0) AS a,\r\n"
				+ "    inventory_adjustment AS iad\r\n"
				+ "        INNER JOIN\r\n"
				+ "    product_mst prod ON prod.PRODUCT_ID = iad.PRODUCT_ID\r\n"
				+ "        INNER JOIN\r\n"
				+ "    employee_mst em ON em.EMPLOYEE_ID = iad.ADJUSTMENT_RECORDED_BY\r\n"
				+ "        INNER JOIN\r\n"
				+ "    project_hdr proj ON proj.PM_HDR_ID = iad.PROJECT_ID\r\n"
				+ "        INNER JOIN\r\n"
				+ "    inventory_location_mst ilm ON ilm.INVENTORY_LOCATION_CODE = iad.INVENTORY_LOCATION_CODE\r\n"
				+ "        INNER JOIN\r\n"
				+ "    uom_mst um ON um.UOM_CODE = prod.PRODUCT_UOM_CODE\r\n"
				+ "        INNER JOIN\r\n"
				+ "    inventory_adjustment_type iadt ON iadt.ADJUSTMENT_TYPE_ID = iad.ADJUSTMENT_TYPE\r\n"
				+ "WHERE\r\n"
				+ "    DATE(ADJUSTMENT_RECORDED_DATE) BETWEEN '"+fromDate+"' AND '"+toDate+"'\r\n"
				+ "        AND iad.TENANT_ID = '"+tenantId+"'; ";
		
		list =this.jdbcTemplate.query(query, new InventoryAdjustmentRowMapper());
		
		
		
		}catch (Exception e) {
			logger.error("retrieveinventoryAdjustmentDao Error" + e);
		}
		
		return list;
	}

	@Override
	public List<AdjustmentTypeDropDownEntity> getadjustmettypedropdown(String tenantId) {
		  
		List<AdjustmentTypeDropDownEntity> list = new ArrayList<AdjustmentTypeDropDownEntity>();
		try {
			String query="SELECT \r\n"
					+ "    ADJUSTMENT_TYPE_ID, ADJUSTMENT_TYPE_DESCRIPTION\r\n"
					+ "FROM\r\n"
					+ "    inventory_adjustment_type\r\n"
					+ "WHERE\r\n"
					+ "    TENANT_ID = '"+tenantId+"';";
			
			list = this.jdbcTemplate.query(query, new AdjustmenttypeDropDownRowMapper());
			
		}catch (Exception e) {
			logger.error("getadjustmettypedropdown Dao Error" + e);
		}
		
		return list;
	}

	@Override
	public int insertAdjustment(String projectId, String productId, String locationCode, String adjustmentType,
			String qtyonHand, String adjustmentQty, String revisedQty, String adjustmentedBy,
			LocalDateTime adjustmentDateTime, String reason,String tenantId,String productCode) {
		
		
		
		int insertAD=0;
		
		try {
			GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(),
					tenantId, "inventory_adjustment", "5", jdbcTemplate, 1,0,null,0);
			
			String query = "insert into inventory_adjustment (PROJECT_ID,PRODUCT_ID,INVENTORY_LOCATION_CODE,ADJUSTMENT_TYPE,PRODUCT_PRIOR_QUANTITY_ON_HAND,PRODUCT_ADJUSTED_QUANTITY,PRODUCT_REVISED_QUANTITY_ON_HAND,ADJUSTMENT_RECORDED_BY,ADJUSTMENT_RECORDED_DATE,ADJUSTMENT_REASON,TENANT_ID,TRANSACTION_NO,FINANCIAL_YEAR_MST_ID,INV_ADJUSTMENT_CODE)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);       ";
			insertAD = this.jdbcTemplate.update(query,projectId,productId,locationCode,adjustmentType,qtyonHand,
												adjustmentQty,revisedQty,adjustmentedBy,adjustmentDateTime,reason,tenantId,
												gen.getSeq(),gen.getFinainceId(),gen.getEnquiryCode());
			
			if(insertAD > 0 ) {
				CommonMethod.updateProductInvDtl(projectId, productId, locationCode,new BigDecimal(adjustmentQty) ,"", "ITTC0002", gen.getEnquiryCode(), adjustmentedBy, String.valueOf(adjustmentDateTime), tenantId, jdbcTemplate);
			}
			
		}catch (Exception e) {
			logger.error("insertAdjustment Dao Error" + e);
		}
		return insertAD;
	}

}
