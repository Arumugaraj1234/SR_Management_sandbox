package com.vmfg.inventory.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.rowmapper.productMstDropDownRowMapper;
import com.vmfg.inventory.dao.interfaces.IInventoryMaterialTransferDao;
import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.entity.PocodeAndUnitRate;
import com.vmfg.inventory.entity.ProductBinEntity;
import com.vmfg.inventory.entity.ProductMasterEntity;
import com.vmfg.inventory.rowmapper.AvailableProductsForTransferRowMapper;
import com.vmfg.inventory.rowmapper.ProductBinRowMapper;
import com.vmfg.inventory.rowmapper.InvProdRowMapper;
import com.vmfg.inventory.rowmapper.InventoryMaterialTransferRowMapper;
import com.vmfg.inventory.rowmapper.LocationDropDownRowMapper;
import com.vmfg.inventory.rowmapper.PocodeAndUnitRateRowMapper;
import com.vmfg.inventory.rowmapper.ProductMasterEntityRowMapper;
import com.vmfg.inventory.rowmapper.ProjectDrpDownRowMapper;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.request.PMInvReq;

@Transactional
@Repository
public class InventoryMaterialTransferDao implements IInventoryMaterialTransferDao {

	private static final Logger logger = LoggerFactory.getLogger(InventoryMaterialTransferDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<InventoryMaterialTransferEntity> retriveMaterial(String fromDate, String toDate, String tenantId) {
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
					+ "    toilm.INVENTORY_LOCATION_CODE as TO_LOCATION_CODE,toilm.INVENTORY_LOCATION_DESCRIPTION AS TO_LOCATION_DESC,imt.UINT_COST,\r\n" + 
					"    imt.OVER_ALL_COST ,imt.FROM_BIN,imt.TO_BIN\r\n"
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
					+ "WHERE\r\n" + "    DATE(imt.CREATED_ON) BETWEEN '" + fromDate + "' AND '" + toDate + "'\r\n"
					+ "        AND imt.TENANT_ID = '" + tenantId + "';";

			list = this.jdbcTemplate.query(query, new InventoryMaterialTransferRowMapper());
		} catch (Exception e) {
			// TODO: handle exception 
			logger.error("retriveMaterialDao Error" + e);
		}

		return list;
	}

	@Override
	public List<ProjectDtlsEntity> getProjectdropdown(String tenantId) {
		// TODO Auto-generated method stub
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
			String query = "SELECT \r\n" + "    PM_HDR_ID, PROJECT_CODE, PROJECT_NAME, CUSTOMER_NAME\r\n" + "FROM\r\n"
					+ "    project_hdr\r\n" + "WHERE\r\n" + "    TENANT_ID = '" + tenantId + "';";

			list = this.jdbcTemplate.query(query, new ProjectDrpDownRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProjectdropdownDao Error" + e);
		}
		return list;
	}

	@Override
	public List<ProjectDtlsEntity> getProjdtlOrdById(String tenantId) {
		// TODO Auto-generated method stub
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
		try {
			String query = "SELECT \r\n" + "    PM_HDR_ID, PROJECT_CODE, PROJECT_NAME, CUSTOMER_NAME\r\n" + "FROM\r\n"
					+ "    project_hdr\r\n" + "WHERE\r\n" + "    TENANT_ID = '" + tenantId
					+ "' and PROJECT_CODE is not null Order By TRANSACTION_NO DESC ;";

			list = this.jdbcTemplate.query(query, new ProjectDrpDownRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProjectdropdownDao Error" + e);
		}
		return list;
	}

	@Override
	public List<ProductMstDropDownEntity> getProductdropdown(String pmhdrId, String tenantId) {
		// TODO Auto-generated method stub
		List<ProductMstDropDownEntity> list = new ArrayList<ProductMstDropDownEntity>();
		try {
			String query = "SELECT \r\n" + "  PM_HDR_ID,  PRODUCT_CODE, PRODUCT_DESCRIPTION,PRODUCT_ID, SPECIFICATION\r\n" + "FROM\r\n"
					+ "    product_mst\r\n" + "WHERE\r\n" + "    PM_HDR_ID = '" + pmhdrId + "' AND TENANT_ID = '"
					+ tenantId + "';";

			list = this.jdbcTemplate.query(query, new productMstDropDownRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProductdropdownDao Error" + e);
		}
		return list;
	}
	@Override
	public List<ProductMstDropDownEntity> getAvailableProductsForTransfer(String pmHdrId, String locationCode, String tenantId) {
		List<ProductMstDropDownEntity> list = new ArrayList<ProductMstDropDownEntity>();
		try {
			String query = "SELECT \r\n"
					+ "    pm.PRODUCT_ID, pm.PRODUCT_CODE, pm.PRODUCT_DESCRIPTION, pm.SPECIFICATION, pm.PM_HDR_ID, pm.BIN,\r\n"
					+ "    ipd.PRODUCT_QUANTITY_ON_HAND AS QTY\r\n"
					+ "FROM\r\n"
					+ "    product_mst pm\r\n"
					+ "        INNER JOIN\r\n"
					+ "    inventory_product_dtl ipd ON ipd.PRODUCT_ID = pm.PRODUCT_ID\r\n"
					+ "WHERE\r\n"
					+ "    pm.PM_HDR_ID = ? AND pm.TENANT_ID = ?\r\n"
					+ "        AND ipd.INVENTORY_LOCATION_CODE = ? AND ipd.TENANT_ID = ?\r\n"
					+ "        AND ipd.PRODUCT_QUANTITY_ON_HAND > 0\r\n"
					+ "ORDER BY pm.PRODUCT_CODE;";

			list = this.jdbcTemplate.query(query, new AvailableProductsForTransferRowMapper(), pmHdrId, tenantId,
					locationCode, tenantId);
		} catch (Exception e) {
			logger.error("getAvailableProductsForTransfer Error" + e);
		}
		return list;
	}

	@Override
	public List<String> getBins(String tenantId, String pmhdrId, String productId) {
	    List<String> list = new ArrayList<>();
	    try {
	        String query = "SELECT BIN FROM product_mst " +
	                       "WHERE TENANT_ID = '" + tenantId + "' AND PM_HDR_ID = '" + pmhdrId + "' AND PRODUCT_ID = '" + productId + "'";

	        list = this.jdbcTemplate.queryForList(query, String.class);
	    } catch (Exception e) {
	        logger.error("getBins Dao Error: ", e);
	    }
	    return list;
	}
	@Override
	public List<ProductBinEntity> getBinsForProducts(String tenantId, String pmHdrId, List<String> productIds) {
	    List<ProductBinEntity> list = new ArrayList<ProductBinEntity>();
	    if (productIds == null || productIds.isEmpty()) {
	        return list;
	    }
	    try {
	        String placeholders = productIds.stream().map(id -> "?").collect(Collectors.joining(","));
	        String query = "SELECT PRODUCT_ID, BIN FROM product_mst " +
	                       "WHERE TENANT_ID = ? AND PM_HDR_ID = ? AND PRODUCT_ID IN (" + placeholders + ")";
	        List<Object> params = new ArrayList<Object>();
	        params.add(tenantId);
	        params.add(pmHdrId);
	        params.addAll(productIds);
	        list = this.jdbcTemplate.query(query, new ProductBinRowMapper(), params.toArray());
	    } catch (Exception e) {
	        logger.error("getBinsForProducts Dao Error: ", e);
	    }
	    return list;
	}
	@Override
	public int updateProductBin(int productId, String toBin) {
	    int rowsAffected = 0;
	    try {
	        String query = "UPDATE product_mst " +
	                       "SET BIN = ? " +
	                       "WHERE PRODUCT_ID = ?";
	        rowsAffected = this.jdbcTemplate.update(query, toBin,productId );
	        logger.info("Updated BIN to '{}' for Product ID: {}", toBin, productId);
	    } catch (Exception e) {
	        logger.error("updateBin Dao Error: ", e);
	    }
	    return rowsAffected;
	}
	
	@Override
	public List<LocationDropDownEntity> getLocationdropdown(String tenantId) {
		// TODO Auto-generated method stub
		List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
		try {
			String query = "SELECT \r\n" + "    INVENTORY_LOCATION_CODE, INVENTORY_LOCATION_DESCRIPTION\r\n"
					+ "FROM\r\n" + "    inventory_location_mst\r\n" + "WHERE\r\n" + "    TENANT_ID = '" + tenantId
					+ "';";

			list = this.jdbcTemplate.query(query, new LocationDropDownRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getLocationdropdownDao Error" + e);
		}
		return list;
	}
	
	@Override
	public List<LocationDropDownEntity> getQtyAvailableLocations(String productId, String tenantId) {
		List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
		try {
			String query = "SELECT \r\n" + 
					"    loc.INVENTORY_LOCATION_CODE,\r\n" + 
					"    loc.INVENTORY_LOCATION_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    inventory_location_mst loc\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_product_dtl prod ON loc.INVENTORY_LOCATION_CODE = prod.INVENTORY_LOCATION_CODE\r\n" + 
					"WHERE\r\n" + 
					"    PRODUCT_ID = '"+productId+"' and prod.TENANT_ID='"+tenantId+"'\r\n" + 
					"        AND PRODUCT_QUANTITY_ON_HAND > 0";

			list = this.jdbcTemplate.query(query, new LocationDropDownRowMapper());
		} catch (Exception e) {
			logger.error("getQtyAvailableLocations Error" + e);
		}
		return list;
	}

	@Override
	public String getAvailableQty(String productId, String location, String tenantId) {
		// TODO Auto-generated method stub
		String qty = "";
		try {
			String query = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(PRODUCT_QUANTITY_ON_HAND) > 0 THEN PRODUCT_QUANTITY_ON_HAND\r\n"
					+ "        ELSE '0'\r\n" + "    END AS PRODUCT_QUANTITY_ON_HAND\r\n" + "FROM\r\n"
					+ "    inventory_product_dtl\r\n" + "WHERE\r\n" + "    INVENTORY_LOCATION_CODE = ?"  
				    + "        AND PRODUCT_ID = ?;";

			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, location, productId);
			qty = resultMap.get("PRODUCT_QUANTITY_ON_HAND").toString();
		} catch (Exception e) {
			logger.error("getAvailableQty Error" + e);
		}
		return qty;
	}
	@Override
	public String getBinByprodCode(String productId, String pmHdrId, String tenantId) {
		String bin = "";
		try {
			String query = "select BIN from product_mst where PRODUCT_CODE= ? and PM_HDR_ID= ? and TENANT_ID= ?";

			Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, productId, pmHdrId, tenantId);
			bin = resultMap.get("BIN").toString();
		} catch (Exception e) {
			logger.error("getBinByprodCode Error" + e);
		}
		return bin;
	}

	@Override
	public int insertMaterialTransfer(String productId, String fromPmHdrId, String toPmHdrId,
			String fromInventoryLocationCode, String toInventoryLocationCode, String transferQuantity,
			String transferDate, String createdOn, String createdBy, String reason, int seq, String finainceId,
			String enquiryCode, String tenantId,BigDecimal avgQtyValue, BigDecimal overallQty,String fromBin, String toBin, String transferGroupId) {
		int insertMT = 0;
		try {

			String query = "insert into inventroy_material_transfer (PRODUCT_ID,FROM_PM_HDR_ID,TO_PM_HDR_ID,"
					+ "FROM_INVENTORY_LOCATION_CODE,TO_INVENTORY_LOCATION_CODE,TRANSFER_QUANTITY,TRANSFER_DATE,"
					+ "CREATED_ON,CREATED_BY,REASON,TRANSACTION_NO,FINANCIAL_YEAR_MST_ID,MATERIAL_TRANSFER_CODE,"
					+ "TENANT_ID,UINT_COST,OVER_ALL_COST, FROM_BIN, TO_BIN, TRANSFER_GROUP_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			insertMT = this.jdbcTemplate.update(query, productId, fromPmHdrId, toPmHdrId, fromInventoryLocationCode,
					toInventoryLocationCode, transferQuantity, transferDate, createdOn, createdBy, reason, seq,
					finainceId, enquiryCode, tenantId,avgQtyValue,overallQty, fromBin, toBin, transferGroupId);

		} catch (Exception e) {
			logger.error("insertMaterialTransfer Error" + e);
		}
		return insertMT;
	}
	@Override
	public List<InvProdEntity> getPmInv(PMInvReq invReq) {
		List<InvProdEntity> returnLi = null;
		try {

			String query = "SELECT  pm.PM_HDR_ID,   pm.PRODUCT_CODE,pm.PRODUCT_ID,pj.CUSTOMER_NAME,     pm.PRODUCT_DESCRIPTION,\r\n" + 
					"					    pj.PROJECT_CODE,     WEIGHT,     ilm.INVENTORY_LOCATION_DESCRIPTION,\r\n" + 
					"					    pdtl.PRODUCT_QUANTITY_ON_HAND,     um.UOM_SHORT_DESCRIPTION,pm.PRODUCT_ID AS PRODUCT_ID,pm.PRODUCT_COST_PER_UNIT,pm.SPECIFICATION,pm.MAKE,\r\n" + 
					"                       CASE \r\n" + 
					"                          WHEN ilm.INVENTORY_LOCATION_CODE in('ILC0002','ILC0004') THEN pm.BIN \r\n" + 
					"                                ELSE '' END AS BIN,\r\n"  +
				    "                  (SELECT \r\n" + 
					"					    pohdr.PO_CODE AS PO_CODE\r\n" + 
					"					FROM\r\n" + 
					"					 indent_hdr hdr inner join   indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"					        left JOIN\r\n" + 
					"					    po_dtl podtl ON dtl.INDENT_DTL_ID = podtl.INDENT_DTL_ID\r\n" + 
					"							left JOIN \r\n" + 
					"						po_hdr pohdr ON pohdr.PO_ID = podtl.PO_ID\r\n" +
				    "                           LEFT JOIN \r\n" + 
					"	                    grn_hdr ghdr ON ghdr.PO_ID = pohdr.PO_ID\r\n" + 
					"					WHERE\r\n" + 
					"					    dtl.PRODUCT_CODE = pm.PRODUCT_CODE AND hdr.PROJECT_ID = pm.PM_HDR_ID order by ghdr.PO_ID desc limit 1) as PO_CODE\r\n" + 
					"                        FROM product_mst pm,     inventory_product_dtl pdtl,\r\n" + 
					"					    inventory_location_mst ilm,     uom_mst um,     project_hdr pj\r\n" 
				    + "WHERE\r\n" + "    pm.PRODUCT_ID = pdtl.PRODUCT_ID\r\n"
					+ "        AND ilm.INVENTORY_LOCATION_CODE = pdtl.INVENTORY_LOCATION_CODE\r\n"
					+ "        and pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n"
					+ "        and pm.PM_HDR_ID = pj.PM_HDR_ID\r\n"
					+ "        AND PRODUCT_QUANTITY_ON_HAND > 0 and pm.TENANT_ID ='" + invReq.getTenantId()
					+ "' and pj.PROJECT_CODE like '%" + invReq.getProjectId() + "%' and ilm.INVENTORY_LOCATION_CODE like '%"+invReq.getInvLocationCode()+"%' order by pj.PM_HDR_ID";

			returnLi = this.jdbcTemplate.query(query, new InvProdRowMapper());

		} catch (Exception e) {
			logger.error("getPmInv Error" + e);
		}
		return returnLi;
	}

	@Override
	public List<InvProdEntity> getPmInvDtl(PMInvReq invReq) {
		List<InvProdEntity> returnLi = null;
		try {
			String query = "SELECT \r\n" + " pj.PM_HDR_ID, pj.PROJECT_CODE,pj.CUSTOMER_NAME,  pm.PRODUCT_CODE,\r\n"
					+ "    pm.PRODUCT_DESCRIPTION,\r\n" + "    \r\n" + "    WEIGHT,\r\n"
					+ "    ilm.INVENTORY_LOCATION_DESCRIPTION,\r\n" + "    pdtl.PRODUCT_QUANTITY_ON_HAND,pm.BIN,\r\n"
					+ "    um.UOM_SHORT_DESCRIPTION,pm.PRODUCT_ID AS PRODUCT_ID \r\n" + "FROM\r\n" + "    product_mst pm,\r\n"
					+ "    inventory_product_dtl pdtl,\r\n" + "    inventory_location_mst ilm,\r\n"
					+ "    uom_mst um,\r\n" + "    project_hdr pj\r\n" + "WHERE\r\n"
					+ "    pm.PRODUCT_ID = pdtl.PRODUCT_ID\r\n"
					+ "        AND ilm.INVENTORY_LOCATION_CODE = pdtl.INVENTORY_LOCATION_CODE\r\n"
					+ "        and pm.PRODUCT_UOM_CODE = um.UOM_CODE\r\n"
					+ "        and pm.PM_HDR_ID = pj.PM_HDR_ID\r\n"
					+ "        AND PRODUCT_QUANTITY_ON_HAND > 0 and pm.TENANT_ID ='" + invReq.getTenantId()
					+ "' group by pj.PM_HDR_ID order by pj.TRANSACTION_NO desc";

			returnLi = this.jdbcTemplate.query(query, new InvProdRowMapper());

		} catch (Exception e) {
			logger.error("getPmInv Error" + e);
		}
		return returnLi;
	}

	@Override
	public List<PocodeAndUnitRate> PocodeAndUnitRate(String productCode,String pmHdrId) {
		List<PocodeAndUnitRate> returnLi = new ArrayList<PocodeAndUnitRate>();
		try {
			String query = "SELECT \n"
					+ "    pohdr.PO_CODE AS PO_CODE,case when  podtl.UNITE_RATE >0 then podtl.UNITE_RATE else '1' end AS UNIT_RATE\n"
					+ "FROM\n"
					+ " indent_hdr hdr inner join   indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\n"
					+ "        left JOIN\n"
					+ "    po_dtl podtl ON dtl.INDENT_DTL_ID = podtl.INDENT_DTL_ID\n"
					+ "		left JOIN \n"
					+ "	po_hdr pohdr ON pohdr.PO_ID = podtl.PO_ID\n"
					+ "WHERE\n"
					+ "    dtl.PRODUCT_CODE = '"+productCode+"' AND hdr.PROJECT_ID = '"+pmHdrId+"' group by pohdr.PO_ID desc limit 1";
 
			returnLi = this.jdbcTemplate.query(query, new PocodeAndUnitRateRowMapper());
 
		} catch (Exception e) {
			logger.error("PocodeAndUnitRate Error" + e);
		}
		return returnLi;
	}

	@Override
	public String getAvgUnitValue(String fromPmHdrId, String toPmHdrId, String productId) {
		// TODO Auto-generated method stub
		
		String avgVal= " ";
		try {
			String unitQry="SELECT \r\n" + 
					"       ROUND(IF(TO_UNIT > 0.00, (FROM_UNIT + TO_UNIT) / 2, FROM_UNIT), 2) AS RESULT\r\n" + 
					"FROM (\r\n" + 
					"    SELECT \r\n" + 
					"        (SELECT PRODUCT_COST_PER_UNIT \r\n" + 
					"         FROM product_mst \r\n" + 
					"         WHERE PRODUCT_ID = ?\r\n" + 
					"         AND PM_HDR_ID = ?) AS FROM_UNIT,\r\n" + 
					"        (SELECT PRODUCT_COST_PER_UNIT \r\n" + 
					"         FROM product_mst \r\n" + 
					"         WHERE PRODUCT_ID = ?\r\n" + 
					"         AND PM_HDR_ID = ?) AS TO_UNIT\r\n" + 
					") AS subquery;";
			Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(unitQry, productId, fromPmHdrId, productId, toPmHdrId);
			avgVal = ((BigDecimal) resultMap.get("RESULT")!= null ? resultMap.get("RESULT") : "0.00").toString();
					
		}catch(Exception ex) {
			logger.error("getAvgUnitValue Error" + ex);
		}
		return avgVal;
		
	}

	@Override
	public int updateUnitVal(String avgValue, String toPmHdrId, String productCode) {
		// TODO Auto-generated method stub
		int updateval=0;
		try {
			String updateQry="update product_mst set PRODUCT_COST_PER_UNIT=? where PRODUCT_CODE=? and PM_HDR_ID=?;\r\n" ;
			 updateval=this.jdbcTemplate.update(updateQry, avgValue, productCode, toPmHdrId);
		}catch(Exception ex) {
			logger.error("updateUnitVal Error" + ex);
		}
		return updateval;
	}

	@Override
	public ProductMasterEntity getProductMstData(String fromPmHdrId, String toPmHdrId, String productCode) {
		ProductMasterEntity object = new ProductMasterEntity();
		List<ProductMasterEntity> list = new ArrayList<ProductMasterEntity>();
		
		try {
			String qry="SELECT \r\n" + 
					"    PRODUCT_CODE,\r\n" + 
					"    PRODUCT_UOM_CODE,\r\n" + 
					"    PRODUCT_DESCRIPTION,\r\n" + 
					"    TENANT_ID,\r\n" + 
					"    IS_ACTIVE,\r\n" + 
					"    CREATED_USER_ID,\r\n" + 
					"    CREATED_DATETIME,\r\n" + 
					"    LAST_UPDATED_USER_ID,\r\n" + 
					"    LAST_UPDATED_DATETIME,\r\n" + 
					"    IS_INVENTORY,\r\n" + 
					"    PKA_ID,\r\n" + 
					"    PSKA_ID,\r\n" + 
					"    PRODUCT_REORDER_LEVEL,\r\n" + 
					"    MINIMUM_ORDER_QUANTITY,\r\n" + 
					"    SAFETY_STOCK,\r\n" + 
					"    SAFETY_STOCK_DAYS,\r\n" + 
					"    PRODUCT_HSN_SAC_CODE,\r\n" + 
					"    PRODUCT_COST_PER_UNIT,\r\n" + 
					"    PRODUCT_GST_TAX_RATE,\r\n" + 
					"    SPECIFICATION,\r\n" + 
					"    MAKE,\r\n" + 
					"    QTY,\r\n" + 
					"    UNIT,\r\n" + 
					"    WEIGHT,\r\n" + 
					"    MATERIAL,\r\n" + 
					"    PRODUCT_CATEGORY,\r\n" + 
					"    BIN,\r\n" + 
					"    INWARD_DATETIME\r\n" + 
					"FROM\r\n" + 
					"    product_mst\r\n" + 
					"WHERE\r\n" + 
					"    PM_HDR_ID = '"+fromPmHdrId+"'\r\n" + 
					"        AND PRODUCT_ID = '"+productCode+"'";
			list=this.jdbcTemplate.query(qry,new ProductMasterEntityRowMapper());
			
			if (list.size() > 0) {
				for (ProductMasterEntity entity : list) {
					object.setProductCode(entity.getProductCode());
			        object.setProductUomCode(entity.getProductUomCode());
			        object.setProductDescription(entity.getProductDescription());
			        object.setTenantId(entity.getTenantId());
			        object.setIsActive(entity.getIsActive());
			        object.setCreatedUserId(entity.getCreatedUserId());
			        object.setCreatedDateTime(entity.getCreatedDateTime());
			        object.setLastUpdatedUserId(entity.getLastUpdatedUserId());
			        object.setLastUpdatedDateTime(entity.getLastUpdatedDateTime());
			        object.setIsInventory(entity.getIsInventory());
			        object.setPkaId(entity.getPkaId());
			        object.setPsksId(entity.getPsksId());
			        object.setProductReorderLevel(entity.getProductReorderLevel());
			        object.setMinimumOrderLevel(entity.getMinimumOrderLevel());
			        object.setSafetyStock(entity.getSafetyStock());
			        object.setSafetyStockDays(entity.getSafetyStockDays());
			        object.setProductHsnSacCode(entity.getProductHsnSacCode());
			        object.setProductCostPerUnit(entity.getProductCostPerUnit());
			        object.setProductGstTaxRate(entity.getProductGstTaxRate());
			        object.setSpecification(entity.getSpecification());
			        object.setMake(entity.getMake());
			        object.setQty(entity.getQty());
			        object.setUnit(entity.getUnit());
			        object.setWeight(entity.getWeight());
			        object.setMaterial(entity.getMaterial());
			        object.setProductCategory(entity.getProductCategory());
			        object.setBin(entity.getBin());
			        object.setInwardDateTime(entity.getInwardDateTime());
				}
			}
			
			
			
		}catch(Exception ex) {
			logger.error("getProductMstData Error" + ex);
		}
		return object;
	}

	@Override
	public int insertProductMstData(ProductMasterEntity prodMstResp, String fromPmHdrId, String toPmHdrId) {

		int insertVal = 0;
		try {

			String qry = "INSERT INTO `product_mst`\r\n" + "(`PRODUCT_CODE`,\r\n" + "`PM_HDR_ID`,\r\n"
					+ "`PRODUCT_UOM_CODE`,\r\n" + "`PRODUCT_DESCRIPTION`,\r\n" + "`TENANT_ID`,\r\n"
					+ "`IS_ACTIVE`,\r\n" + "`CREATED_USER_ID`,\r\n" + "`CREATED_DATETIME`,\r\n"
					+ "`LAST_UPDATED_USER_ID`,\r\n" + "`LAST_UPDATED_DATETIME`,\r\n" + "`IS_INVENTORY`,\r\n"
					+ "`PKA_ID`,\r\n" + "`PSKA_ID`,\r\n" + "`PRODUCT_REORDER_LEVEL`,\r\n"
					+ "`MINIMUM_ORDER_QUANTITY`,\r\n" + "`SAFETY_STOCK`,\r\n" + "`SAFETY_STOCK_DAYS`,\r\n"
					+ "`PRODUCT_HSN_SAC_CODE`,\r\n" + "`PRODUCT_COST_PER_UNIT`,\r\n" + "`PRODUCT_GST_TAX_RATE`,\r\n"
					+ "`SPECIFICATION`,\r\n" + "`MAKE`,\r\n" + "`QTY`,\r\n" + "`UNIT`,\r\n" + "`WEIGHT`,\r\n"
					+ "`MATERIAL`,\r\n" + "`PRODUCT_CATEGORY`,\r\n" + "`BIN`,\r\n" + "`INWARD_DATETIME`)\r\n"
					+ "VALUES\r\n" + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			insertVal = this.jdbcTemplate.update(qry, prodMstResp.getProductCode(), toPmHdrId,
					prodMstResp.getProductUomCode(), prodMstResp.getProductDescription(), prodMstResp.getTenantId(),
					prodMstResp.getIsActive(), prodMstResp.getCreatedUserId(), prodMstResp.getCreatedDateTime(),
					prodMstResp.getLastUpdatedUserId(), prodMstResp.getLastUpdatedDateTime(),
					prodMstResp.getIsInventory(), prodMstResp.getPkaId(), prodMstResp.getPsksId(),
					prodMstResp.getProductReorderLevel(), prodMstResp.getMinimumOrderLevel(),
					prodMstResp.getSafetyStock(), prodMstResp.getSafetyStockDays(), prodMstResp.getProductHsnSacCode(),
					prodMstResp.getProductCostPerUnit(), prodMstResp.getProductGstTaxRate(),
					prodMstResp.getSpecification(), prodMstResp.getMake(), prodMstResp.getQty(), prodMstResp.getUnit(),
					prodMstResp.getWeight(), prodMstResp.getMaterial(), prodMstResp.getProductCategory(),
					prodMstResp.getBin(), prodMstResp.getInwardDateTime());

		} catch (Exception ex) {
			logger.error("insertProductMstData Error" + ex);
		}
		return insertVal;
	}

	@Override
	public int getProductMstCountCheck(String toPmHdrId, String productCode,String tenantId) {
		int productId = 0;
		try {
			
			String qry = "SELECT \n" + 
                    "    CASE\n" + 
                    "        WHEN COUNT(PRODUCT_ID) > 0 THEN PRODUCT_ID\n" + 
                    "        ELSE 0 \n" + 
                    "    END as PRODUCT_ID\n" + 
                    "FROM\n" + 
                    "    product_mst\n" + 
                    "WHERE\n" + 
                    "    PM_HDR_ID = ?\n" + 
                    "    AND PRODUCT_ID = ?\n" + 
                    "    AND TENANT_ID = ?";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, toPmHdrId, productCode,tenantId);
			productId = Integer.parseInt(resultMap.get("PRODUCT_ID").toString());
			
		}catch(Exception ex) {
			logger.error("getProductMstCountCheck Error" + ex);
		}
		return productId;
	}

	


}
