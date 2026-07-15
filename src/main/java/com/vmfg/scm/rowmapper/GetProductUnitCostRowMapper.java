package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GetProductUnitCostEntity;

public class GetProductUnitCostRowMapper  implements RowMapper<GetProductUnitCostEntity> {
    private static final Logger logger = LoggerFactory.getLogger(GetProductUnitCostRowMapper.class);

    @Override
    public GetProductUnitCostEntity mapRow(ResultSet row, int rowNum) throws SQLException {
    	GetProductUnitCostEntity po = new GetProductUnitCostEntity();
        try {
       //   po.setPoCode(row.getString("PO_CODE"));
          po.setProductId(row.getString("PRODUCT_ID"));
          po.setUnitCost(row.getString("UNITE_RATE"));
        } catch (Exception e) {
            logger.error("GetProductUnitCostRowMapper Exception--->" + e);
        }
        return po;
    }

}
