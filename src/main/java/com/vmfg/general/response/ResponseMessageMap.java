package com.vmfg.general.response;

public class ResponseMessageMap {

	// Codes

	public static String responseCodeOk = "200";
	public static String responseCodeNotOk = "412";
	public static String responseAlreadyExists = "409";
	public static String failToupdateCode = "409";

	// msg
	public static String successCreated = "Successfully Created";
	public static String success = "Successfully Executed";
	public static String successMsg = "Success";
	public static String successUpdated = "Successfully Updated";
	public static String successInserted = "Successfully Added";
	public static String successfulDeleted = "Record Deleted";
	public static String updateNR = "Update Not Required";
	public static String partialSucess = "Partially Updated";
	public static String successUpload = "Successfully Uploaded";
	public static String linkedForDel = "Record cannot be Deleted";
	public static String indentLinkedtoKeyArea = "Record cannot be deleted, Indent has been raised";
	public static String approved = "Approved";
	public static String directlyAproved = "Directly Approved";
	public static String sucessExecuted = "Successfully Executed";
	public static String cancelled = "Material Request can't be cancelled for the issued onces";
	public static String poCancelled = "PO Cancelled";
	public static String dcCancelled = "DC Cancelled";
	public static String mrCancelled = "MR Cancelled";
	public static String mrIssued = "Material Issued";
	public static String poAlreadyCreated = "PO has already been created for the PJS";
	public static String budgetExcessReported = "Budget Excess has been reported for the PO justification";

	// error
	public static String NoData = "Record Not Available";
	public static String deleteUnSuccessful = "Delete Unsuccessful";
	public static String indentIsConsumed = "Record cannot be deleted, Budget has been consumed for indents";
	public static String noRecord = "Record Not Available";
	public static String responseAlreadyExistMsg = "Project Code already exists";
	public static String failToupdateMsg = "Fail to update";
	public static String poMICreated="Material inward has been raised against this PO";
	public static String poPRACreated="PRA has been raised against this PO";
	public static String poGstCantCreated = "Gst can't updated for the raised PRA";
	public static String failTodeleteMsg = "Fail to delete";
	public static String failTouploadMsg = "Fail to upload";
	public static String failMsg = "Failure";
	public static String failToApprove = "Error occurred. Please try again later";
	public static String alreadyExist = "Team member already exist";
	public static String reassignNP = "Reassign not possible for completed task";
	public static String grpTemplatDup = "Duplicate Template Name";
	public static String approveBudgetExcess = "Budget Excess isn't Approved Yet";
	public static String allocateBudgetFromSalesValue = "Insufficient budget at this station. Please use 'Allocate to Station' to add budget from available Sales Value.";
	public static String raiseBudgetExcessRequired = "Insufficient budget at this station and no Sales Value available to allocate. Please use 'Raise Budget Excess' to proceed.";
	public static String budgetExcessAlreadyRaised = "Budget Excess has already been raised for this indent";
	public static String reverseNotAllowedBudgetExcess = "This PJS has a Budget Excess raised against it and cannot be reversed. Please resolve the Budget Excess first.";
	public static String actionNotAllowed = "This action is not applicable for this indent";
	public static String subTaskNotCompleted = "Sub task for the current task not completed";

	public static String atleastOneTeam = "At least one team member must be assigned";
	public static String masterTeamMember = "Master team member cannot be removed";

	public static String indentAllocated = "Indent Allocated";
	public static String userNameExist = "Username already exist";
	public static String failToCreateMsg = "Fail to Create";
	public static String poCreateForScs = "Record cannot be deleted, PO has been created for the PO Justification";
	public static String maxUserCnt = "Max User Count Reached";
	public static String contactAdmin = "Email not configured.Contact admin to reset password.";
	public static String wrongOtp = "Incorrect OTP.";
	public static String reqNewOtp = "The OTP has expired. Request a new OTP to proceed.";
	public static String invalidUserName = "Invalid username.";
	public static String empIdExist = "Emp ID already exist";
	public static String empMailIdExist = "Employee Mail ID already exist";
	public static String taskcantdeleteMsg = "Milestone cannot be deleted. Task has already been created";
	public static String stageUnav = "Staged Product not in inventory";
	public static String vendorAlreadyExist = "Vendor name already exist";
	
	public static String failtoinsert = "Fail to Insert";
	public static String projectCodeDuplicate="Project code already exists";
}
