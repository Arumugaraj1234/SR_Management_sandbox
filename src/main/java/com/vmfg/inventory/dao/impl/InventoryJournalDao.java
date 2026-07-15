package com.vmfg.inventory.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.inventory.dao.interfaces.IInventoryJournalDao;
import com.vmfg.inventory.entity.InventoryJournalEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.request.InventoryTenantRequest;
import com.vmfg.inventory.rowmapper.InventoryJournalRowMapper;
import com.vmfg.inventory.rowmapper.LocationDropDownRowMapper;

@Transactional
@Repository
public class InventoryJournalDao implements IInventoryJournalDao {

	private static final Logger logger = LoggerFactory.getLogger(InventoryJournalDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<InventoryJournalEntity> retriveJournal(String frmDate, String toDate, String projectId,
			String tenantId) {
		// TODO Auto-generated method stub
		List<InventoryJournalEntity> list = new ArrayList<InventoryJournalEntity>();
		try {

			String query = "SELECT \r\n" + "    @a:=@a+1 SERIAL_NUMBER,\r\n" + "    ij.INVENTORY_TRANSACTION_DATE,\r\n"
					+ "    ij.INVENTORY_TRANSACTION_REFERENCE_ID, \r\n" + "    ij.INVENTORY_TRANSACTION_QUANTITY,\r\n"
					+ "    itt.INVENTORY_TRANSACTION_TYPE_DESCRIPTION,\r\n" + "    pm.PRODUCT_CODE,\r\n"
					+ "    pm.PRODUCT_DESCRIPTION, pm.SPECIFICATION,\r\n" + "    ij.PROJECT_ID,\r\n"
					+ "    ij.INVENTORY_LOCATION_CODE,\r\n" + "    ilm. INVENTORY_LOCATION_DESCRIPTION,\r\n"
					+ "    ij.OPENING_BALANCE,\r\n" + "    ij.CLOSING_BALANCE,\r\n" + "    phdr.PROJECT_NAME,\r\n"
					+ "    um.UOM_LONG_DESCRIPTION,\r\n" + "    um.UOM_SHORT_DESCRIPTION,phdr.PROJECT_CODE, \r\n"
					+ " ( select CASE WHEN COUNT(phdr.PO_ID) > 0  THEN PO_CODE ELSE (select mhdr.PO_CODE from material_inward_hdr mhdr inner join material_inward_dtl mdtl\r\n" + 
					"       on mdtl.MI_ID = mhdr.MI_ID inner join product_mst mst \r\n" + 
					"	   on mst.PRODUCT_ID = mdtl.PRODUCT_ID where mst.PRODUCT_CODE =pm.PRODUCT_CODE and mst.PM_HDR_ID='"+projectId+"' ORDER BY mdtl.MI_DTL_ID desc limit 1) END  \r\n" + 
					"       from indent_hdr ihdr inner join" +
				    "   indent_dtl dtl on dtl.INDENT_ID=ihdr.INDENT_ID inner join po_dtl pdtl\r\n" + 
					"       on pdtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
					"       inner join po_hdr phdr on phdr.PO_ID = pdtl.PO_ID\r\n" + 
					"       where product_code=pm.PRODUCT_CODE and PROJECT_ID='"+projectId+"') AS PO_CODE \r\n"
					+ "FROM\r\n"
					+ "   (SELECT @a:= 0) AS a, inventory_journal AS ij\r\n" + "        INNER JOIN\r\n"
					+ "    inventory_transaction_type itt ON ij.INVENTORY_TRANSACTION_TYPE_CODE = itt.INVENTORY_TRANSACTION_TYPE_CODE \r\n"
					+ "        INNER JOIN\r\n" + "    product_mst pm ON ij.PRODUCT_ID = pm.PRODUCT_ID\r\n"
					+ "		INNER JOIN\r\n" + "        project_hdr phdr ON ij.PROJECT_ID = phdr.PM_HDR_ID\r\n"
					+ "        INNER JOIN \r\n"
					+ "        inventory_location_mst ilm ON ij.INVENTORY_LOCATION_CODE = ilm.INVENTORY_LOCATION_CODE\r\n"
					+ "		 INNER JOIN \r\n" + "        uom_mst um ON um.UOM_CODE = pm.PRODUCT_UOM_CODE\r\n"
					+ "WHERE\r\n" + "    DATE(ij.INVENTORY_TRANSACTION_DATE) BETWEEN ? AND ? \r\n"
					+ "    AND ij.TENANT_ID = ? \r\n"
					+ "    AND ij.PROJECT_ID LIKE ? order by ij.INVENTORY_RECORD_CREATED_DATE desc";
			list = this.jdbcTemplate.query(query, new InventoryJournalRowMapper(), frmDate, toDate, tenantId,
					projectId);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("retriveJournalDao Error" + e);
		}

		return list;
	}

	@Override
	public List<LocationDropDownEntity> getInvLocationForInward(InventoryTenantRequest tenantReq) {
		// TODO Auto-generated method stub
		List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
		try {

			String query = "select * from inventory_location_mst where TENANT_ID = ? and (INVENTORY_LOCATION_CODE = ? or INVENTORY_LOCATION_PARENT_CODE = ?);";
			list = this.jdbcTemplate.query(query, new LocationDropDownRowMapper(), tenantReq.getTenantId(), "ILC0002",
					"ILC0002");
		} catch (Exception e) {

			logger.error("getInvLocationForInward Error" + e);
		}

		return list;
	}
}
