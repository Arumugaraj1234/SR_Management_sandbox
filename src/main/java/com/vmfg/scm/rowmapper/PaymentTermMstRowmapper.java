package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PaymentTermMstEntity;

public class PaymentTermMstRowmapper implements RowMapper<PaymentTermMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(PaymentTermMstRowmapper.class);

	@Override
	public PaymentTermMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		PaymentTermMstEntity pt = new PaymentTermMstEntity();
		try {
			pt.setPaymentTermId(rs.getInt("PAYMENT_TERM_ID"));
			pt.setTermDesc(rs.getString("TERM_DESC"));
			pt.setSeqNo(rs.getInt("SEQ_NO"));
		} catch (Exception ex) {
			logger.error("PaymentTermMstRowmapper error " + ex);
		}
		return pt;
	}

}
