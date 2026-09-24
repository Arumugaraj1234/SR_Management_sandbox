package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPoDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String poId;
	private String pmId;
	private String masterId;
	private String projectId;
	private String igScpId;
	private String igHdrId;
	private String indentID;
	private String transactionNo;
	private String financialYearMstId;
	private String poType;
	private String billingName;
	private String billingAddressLine;
	private String billingCity;
	private String billingPincode;
	private String billingState;
	private String billingCount;
	private String billingGst;
	private String billingContactNo;
	private String vendorName;
	private String vendorAddressLine;
	private String vendorCity;
	private String vendorPincode;
	private String vendorState;
	private String vendorCount;
	private String vendorGst;
	private String vendorContactNo;
	private String deliveryName;
	private String deliveryAddressLine;
	private String deliveryCity;
	private String deliveryPincode;
	private String deliveryState;
	private String deliveryCount;
	private String deliveryGst;
	private String deliveryContact;
	private String deliveryDate;
	private String amountinwords;
	private String division;
	private String orderNo;
	private String date;
	private String revision;
	private String revisionDate;
	private String refDate;
	private String deliveryTerms;
	private String liqDamages;
	private String guarantee;
	private String warrenty;
	private String dispatchMode;
	private String transitInsurance;
	private String inspectionScope;
	private String misc;
	private String portOfDest;
	private String remarks;
	private String discount;
	private String pF;
	private String pFFX;
	private String frieght;
	private String others;
	private String basicTotal;
	private String basicTotalFx;
	private String gst;
	private String pendingGst;
	private String gstFx;
	private String cess;
	private String totalValue;
	private String totalValueFx;
	private String frieghtRemarks;
	private String pFRemarks;
	private String poTC;
	private String dwgs;
	private String qap;
	private String gtc;
	private String docCharges;
	private String inspectionCharges;
	private String insuranceValue;
	private String testingCharges;
	private String ctc;
	private String tdc;
	private String tds;
	private String tenantId;
	private String empId;
	private String sequenceNo;
	private String sequenceStatus;
	private String vendorCode;
	private String poCode;
	private String updatedOn;
	private String updatedBy;
	private String seqStatusDesc;
	private String isApproved;
	private String isEditable;
	private String divisionDesc;
	private String dispatchModeDesc;
	private String transitInsuranceDesc;
	private String inspectionScopeDesc;
    private String ranDivCommte;
    private String pan;
    private String priceBasis;
    private String igst;
    private String terminalTax;
    private String indentCode;
    // Count of distinct indents actually behind this PO's PJS (indent_grp_scs_indent_budget) -
    // INDENT_CODE/indentID above still resolve to one representative indent
    // (po_hdr.INDENT_ID), same as ever; when this is >1 the frontend shows "N indents" plus a
    // breakdown instead of the single misleading code. See project_multi_indent_pjs_grouping
    // memory, Problem 4 follow-on.
    private String indentCount;
    // PJS ref no. behind this PO (indent_grp_scs.PJS_REF_NO, joined via po_hdr.IG_SCS_ID) - for the
    // PO list's "PJS No." column.
    private String pjsRefNo;
    private String refNo;
    private	String transportCharges;
    private	String transportChargesFx;
    private String intialunitPrice;
    private String intialExtendedPrice;
    private String gstType;
    private String partCount;
    // "1" = show same Part Number + Unit Rate lines as one row (NEW-flow POs, see PoDAO.getMergeSamePartRowsFlag)
    private String mergeSamePartRows;
    private String venCode;
	private String inspectionStatus;
	private String isGstChanged;
	private String oldGst;
	private String poGst;
	private String pendingTransportChrg;
	private String pendingInsuranceChrg;
	private String pendingPfChrg;
	private String pendingOtherChrg;
	private String praStatus;

  
	private List<PoDtlEntity> poDtl = new ArrayList<PoDtlEntity>();
	private List<PoPaymentTermEntity> poPaymentTerm= new ArrayList<PoPaymentTermEntity>();
	private List<PoDispatchDocEntity> poDispatchDoc= new ArrayList<PoDispatchDocEntity>();
	private List<PoStatusEntity> poStatusList= new ArrayList<PoStatusEntity>();
	private List<DocumentStatusMstEntity> docLifeCycleMstList= new ArrayList<DocumentStatusMstEntity>();
	private List<GetPoDtlsEntity> preRevisionEntity = new ArrayList<GetPoDtlsEntity>();
	private List<DebitNoteEntity> debitNoteReasonList = new ArrayList<>();

}
	