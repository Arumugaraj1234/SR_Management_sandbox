package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScpVenDtlEntity;

public class IndentGrpScpVenDtlRowMapper implements RowMapper<IndentGrpScpVenDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpVenDtlRowMapper.class);

	@Override
	public IndentGrpScpVenDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpScpVenDtlEntity igs = new IndentGrpScpVenDtlEntity();
		try {
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIgScpVendtlId(rs.getString("IG_SCS_VDID"));
			igs.setIgDtlId(rs.getString("IG_DTL_ID"));
			igs.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			igs.setL1DeliveryDate(rs.getString("L1_DELIVERY_DATE"));
			igs.setL1FinalQuotedDate(rs.getString("L1_FINAL_QUOTED_DATE"));
			igs.setL1FinalQuotedRef(rs.getString("L1_FINAL_QUOTED_REF"));
			igs.setL1GstValue(rs.getString("L1_GST_VALUE"));
			igs.setL1GstValueFx(rs.getString("L1_GST_VALUE_FX"));
			igs.setL1InitialQuotedDate(rs.getString("L1_INTIAL_QUOTED_DATE"));
			igs.setL1InitialQuotedRef(rs.getString("L1_INTIAL_QUOTED_REF"));
			igs.setL1Ld(rs.getString("L1_LD"));
			igs.setL1PF(rs.getString("L1_P_F"));
			igs.setL1PFFx(rs.getString("L1_P_F_FX"));
			igs.setL1SubTotal(rs.getString("L1_SUB_TOTAL"));
			igs.setL1SubTotalFx(rs.getString("L1_SUB_TOTAL_FX"));
			igs.setL1TotalCost(rs.getString("L1_TOTAL_COST"));
			igs.setL1TotalCostFx(rs.getString("L1_TOTAL_COST_FX"));
			igs.setL1TransportCharges(rs.getString("L1_TRANSPORT_CHARGES"));
			igs.setL1TransportChargesFx(rs.getString("L1_TRANSPORT_CHARGES_FX"));
			igs.setL1Warrenty(rs.getString("L1_WARRENTY"));
			igs.setL1FinalTransportCharges(rs.getString("L1_FINAL_TRANSPORT_CHARGES"));
			igs.setL1FinalTransportChargesFx(rs.getString("L1_FX_FINAL_TRANSPORT_CHARGES"));
			igs.setL1FinalPF(rs.getString("L1_FINAL_P_F"));
			igs.setL1FinalPFFx(rs.getString("L1_FX_FINAL_P_F"));
			igs.setL1FinalSubTotal(rs.getString("L1_FINAL_SUB_TOTAL"));
			igs.setL1FinalSubTotalFx(rs.getString("L1_FINAL_SUB_TOTAL_FX"));
			igs.setL1FinalGSTValue(rs.getString("L1_FINAL_GST_VALUE"));
			igs.setL1FinalGSTValueFx(rs.getString("L1_FX_FINAL_GST_VALUE"));
			igs.setL1FinalTotalCost(rs.getString("L1_FINAL_TOTAL_COST"));
			igs.setL1FinalTotalCostFx(rs.getString("L1_FX_FINAL_TOTAL_COST"));
			igs.setL1UnitIniBasicTotal(rs.getString("L1_UNIT_INI_BASIC_TOTAL"));
			igs.setL1ExtnIniBasicTotal(rs.getString("L1_EXTN_INI_BASIC_TOTAL"));
			igs.setL1UnitFinalBasicTotal(rs.getString("L1_UNIT_FINAL_BASIC_TOTAL"));
			igs.setL1ExtnFinalBasicTotal(rs.getString("L1_EXTN_FINAL_BASIC_TOTAL"));
			igs.setL1ExtnFinalBasicTotalFx(rs.getString("L1_FX_EXTN_FINAL_BASIC_TOTAL"));

			igs.setL2DeliveryDate(rs.getString("L2_DELIVERY_DATE"));
			igs.setL2FinalQuotedDate(rs.getString("L2_FINAL_QUOTED_DATE"));
			igs.setL2FinalQuotedRef(rs.getString("L2_FINAL_QUOTED_REF"));
			igs.setL2GstValue(rs.getString("L2_GST_VALUE"));
			igs.setL2GstValueFx(rs.getString("L2_GST_VALUE_FX"));
			igs.setL2InitialQuotedDate(rs.getString("L2_INTIAL_QUOTED_DATE"));
			igs.setL2InitialQuotedRef(rs.getString("L2_INTIAL_QUOTED_REF"));
			igs.setL2Ld(rs.getString("L2_LD"));
			igs.setL2PF(rs.getString("L2_P_F"));
			igs.setL2PFFx(rs.getString("L2_P_F_FX"));
			igs.setL2SubTotal(rs.getString("L2_SUB_TOTAL"));
			igs.setL2SubTotalFx(rs.getString("L2_SUB_TOTAL_FX"));
			igs.setL2TotalCost(rs.getString("L2_TOTAL_COST"));
			igs.setL2TotalCostFx(rs.getString("L2_TOTAL_COST_FX"));
			igs.setL2TransportCharges(rs.getString("L2_TRANSPORT_CHARGES"));
			igs.setL2TransportChargesFx(rs.getString("L2_TRANSPORT_CHARGES_FX"));
			igs.setL2Warrenty(rs.getString("L2_WARRENTY"));
			igs.setL2FinalTransportCharges(rs.getString("L2_FINAL_TRANSPORT_CHARGES"));
			igs.setL2FinalTransportChargesFx(rs.getString("L2_FX_FINAL_TRANSPORT_CHARGES"));
			igs.setL2FinalPF(rs.getString("L2_FINAL_P_F"));
			igs.setL2FinalPFFx(rs.getString("L2_FX_FINAL_P_F"));
			igs.setL2FinalSubTotal(rs.getString("L2_FINAL_SUB_TOTAL"));
			igs.setL2FinalSubTotalFx(rs.getString("L2_FINAL_SUB_TOTAL_FX"));
			igs.setL2FinalGSTValue(rs.getString("L2_FINAL_GST_VALUE"));
			igs.setL2FinalGSTValueFx(rs.getString("L2_FX_FINAL_GST_VALUE"));
			igs.setL2FinalTotalCost(rs.getString("L2_FINAL_TOTAL_COST"));
			igs.setL2FinalTotalCostFx(rs.getString("L2_FX_FINAL_TOTAL_COST"));
			igs.setL2UnitIniBasicTotal(rs.getString("L2_UNIT_INI_BASIC_TOTAL"));
			igs.setL2ExtnIniBasicTotal(rs.getString("L2_EXTN_INI_BASIC_TOTAL"));
			igs.setL2UnitFinalBasicTotal(rs.getString("L2_UNIT_FINAL_BASIC_TOTAL"));
			igs.setL2ExtnFinalBasicTotal(rs.getString("L2_EXTN_FINAL_BASIC_TOTAL"));
			igs.setL2ExtnFinalBasicTotalFx(rs.getString("L2_FX_EXTN_FINAL_BASIC_TOTAL"));

			igs.setL3DeliveryDate(rs.getString("L3_DELIVERY_DATE"));
			igs.setL3FinalQuotedDate(rs.getString("L3_FINAL_QUOTED_DATE"));
			igs.setL3FinalQuotedRef(rs.getString("L3_FINAL_QUOTED_REF"));
			igs.setL3GstValue(rs.getString("L3_GST_VALUE"));
			igs.setL3GstValueFx(rs.getString("L3_GST_VALUE_FX"));
			igs.setL3InitialQuotedDate(rs.getString("L3_INTIAL_QUOTED_DATE"));
			igs.setL3InitialQuotedRef(rs.getString("L3_INTIAL_QUOTED_REF"));
			igs.setL3Ld(rs.getString("L3_LD"));
			igs.setL3PF(rs.getString("L3_P_F"));
			igs.setL3PFFx(rs.getString("L3_P_F_FX"));
			igs.setL3SubTotal(rs.getString("L3_SUB_TOTAL"));
			igs.setL3SubTotalFx(rs.getString("L3_SUB_TOTAL_FX"));
			igs.setL3TotalCost(rs.getString("L3_TOTAL_COST"));
			igs.setL3TotalCostFx(rs.getString("L3_TOTAL_COST_FX"));
			igs.setL3TransportCharges(rs.getString("L3_TRANSPORT_CHARGES"));
			igs.setL3TransportChargesFx(rs.getString("L3_TRANSPORT_CHARGES_FX"));
			igs.setL3Warrenty(rs.getString("L3_WARRENTY"));
			igs.setL3FinalTransportCharges(rs.getString("L3_FINAL_TRANSPORT_CHARGES"));
			igs.setL3FinalTransportChargesFx(rs.getString("L3_FX_FINAL_TRANSPORT_CHARGES"));
			igs.setL3FinalPF(rs.getString("L3_FINAL_P_F"));
			igs.setL3FinalPFFx(rs.getString("L3_FX_FINAL_P_F"));
			igs.setL3FinalSubTotal(rs.getString("L3_FINAL_SUB_TOTAL"));
			igs.setL3FinalSubTotalFx(rs.getString("L3_FINAL_SUB_TOTAL_FX"));
			igs.setL3FinalGSTValue(rs.getString("L3_FINAL_GST_VALUE"));
			igs.setL3FinalGSTValueFx(rs.getString("L3_FX_FINAL_GST_VALUE"));
			igs.setL3FinalTotalCost(rs.getString("L3_FINAL_TOTAL_COST"));
			igs.setL3FinalTotalCostFx(rs.getString("L3_FX_FINAL_TOTAL_COST"));
			igs.setL3UnitIniBasicTotal(rs.getString("L3_UNIT_INI_BASIC_TOTAL"));
			igs.setL3ExtnIniBasicTotal(rs.getString("L3_EXTN_INI_BASIC_TOTAL"));
			igs.setL3UnitFinalBasicTotal(rs.getString("L3_UNIT_FINAL_BASIC_TOTAL"));
			igs.setL3ExtnFinalBasicTotal(rs.getString("L3_EXTN_FINAL_BASIC_TOTAL"));
			igs.setL3ExtnFinalBasicTotalFx(rs.getString("L3_FX_EXTN_FINAL_BASIC_TOTAL"));

			igs.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			igs.setLastUpdatedDate(rs.getString("LAST_UPDATED_DATETIME"));
		} catch (Exception ex) {
			logger.error("IndentGrpScpVenDtlRowMapper error " + ex);
		}
		return igs;
	}

}
