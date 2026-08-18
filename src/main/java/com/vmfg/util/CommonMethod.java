package com.vmfg.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.util.entity.FinancialYearMst;
import com.vmfg.util.entity.FinancialYearRowMapper;
import com.vmfg.util.entity.MaxQuantityorferRowMapper;
import com.vmfg.util.entity.MaxQuantityorferentity;
import com.vmfg.util.entity.ProductMstTableEntity;
import com.vmfg.util.entity.ProductMstTableRowMapper;
import com.vmfg.util.entity.TransactionMstDtls;
import com.vmfg.util.entity.TransactionMstDtlsRowMapper;
import com.vmfg.util.entity.locationinvntryRowMapper;
import com.vmfg.util.entity.locationinvntryentity;

@Transactional
@Repository
public class CommonMethod {
	private static final Logger logger = LoggerFactory.getLogger(CommonMethod.class);
	
	public static String sdfformate = "dd-MM-yyyy";
	public static String sdfformatetwo = "yyyy-MM-dd hh:mm:ss";
	public static String sdfformatethree = "yyyy-MM-dd HH:mm:ss";
	public static String sdfformatetime = "HH:mm:ss";
	public static String sdfHourMin = "HH:mm";
	public static String sdfMin = "mm";
	public static String sdfFormateDate = "yyyy-MM-dd";

	public static String getCurrentMonthYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getDBDateToViewBydayMonthYear(String d) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String dateTime = null;
		try {
			date = format.parse(d);
			dateTime = formatter.format(date);
		} catch (ParseException e) {
			logger.error("Common Method getDBDateToViewByMonthThree Method Exception-->" + e);
		}

		return dateTime;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getNextCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = calendar.getTime();
		String dateTime = formatter.format(tomorrow);
		return dateTime;
	}

	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getCurrentdate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getCurrentdateformat() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getCurrentTimeformat() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getDBDateToView(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getDBDateMinToView(String d) {
		String dateTime = "";
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateTime = formatter.format(date1);
		} catch (Exception e) {
			logger.error("Exception in getDBDateMinToView Common method" + e);
		}

		return dateTime;
	}

	public static String getDBMonDateMinToView(String d) {
		String dateTime = "";
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
			dateTime = formatter.format(date1);
		} catch (Exception e) {
			logger.error("Exception in getDBDateMinToView Common method" + e);
		}

		return dateTime;
	}

	public static String get2fixedString(String value) {
		String formattedValue = "";
		try {
			double doublevalue = Double.parseDouble(value);

			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			formattedValue = decimalFormat.format(doublevalue);
		} catch (Exception ex) {
			logger.error("Exception in get2fixedString Common method" + ex);
		}
		return formattedValue;
	}

	public static String getDBDateToViewTwo(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getDBDateToViewThird(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getDBDateToViewByMonth(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static int getNoDaysInMonth(String d) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		int daysQty = 30;
		try {
			date = sdf.parse(d);
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(date);
			daysQty = caldate.getActualMaximum(Calendar.DAY_OF_MONTH);
		} catch (Exception ex) {
			logger.error("getNoDaysInMonth Method Exception" + ex);
		}
		return daysQty;
	}

	public static String getDBLastDateOfMonth(String d) {
		String lastDate = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(d);
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(date);
			int lDate = caldate.getActualMaximum(Calendar.DATE);
			caldate.set(Calendar.DATE, lDate);
			lastDate = sdf.format(caldate.getTime());

		} catch (ParseException e) {
			logger.error("Common Method getDBLastDateOfMonth Method Exception-->" + e);
		}

		return lastDate;
	}

	public static String getWeekNofromDate(String d) {
		int week = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat(CommonMethod.sdfFormateDate);
			Date date = df.parse(d);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			week = cal.get(Calendar.WEEK_OF_YEAR);
		} catch (ParseException ex) {
			logger.error("CommonMethod getWeekNofromDate Method Exception --->" + ex);
		}
		return String.valueOf(week);
	}

	public static String getShiftHdrId(String d, JdbcTemplate jdbcTemplate, String seq) {
		String shiftHdrId = "";
		String shiftDay = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat(CommonMethod.sdfFormateDate);
			Date date = df.parse(d);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			shiftDay = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
			String shiftHdrIdQry = "SELECT \r\n" + "    sm.SHIFT_MST_HDR_ID\r\n" + "FROM\r\n"
					+ "    shift_mst_hdr AS sm\r\n" + "        INNER JOIN\r\n"
					+ "    shift_mst_type_code AS stc ON sm.SHIFT_MST_TYPE_CODE = stc.SHIFT_MST_TYPE_CODE\r\n"
					+ "WHERE\r\n" + "    sm.SHIFT_DAY = ? \r\n" + "        AND stc.SEQUENCE = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(shiftHdrIdQry,shiftDay,seq);
			shiftHdrId = resultMap.get("SHIFT_MST_HDR_ID").toString();
		} catch (Exception ex) {
			logger.error("CommonMethod getShiftHdrId Method Exception --->" + ex);
		}
		return shiftHdrId;
	}

	public static String getShiftDesc(String d, JdbcTemplate jdbcTemplate, String seq) {
		String shiftHdrId = "";
		String shiftDay = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat(CommonMethod.sdfFormateDate);
			Date date = df.parse(d);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			shiftDay = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
			String shiftHdrIdQry = "SELECT \r\n" + "    stc.SHIFT_MST_TYPE_DESCRIPTION\r\n" + "FROM\r\n"
					+ "    shift_mst_hdr AS sm\r\n" + "        INNER JOIN\r\n"
					+ "    shift_mst_type_code AS stc ON sm.SHIFT_MST_TYPE_CODE = stc.SHIFT_MST_TYPE_CODE\r\n"
					+ "WHERE\r\n" + "    sm.SHIFT_DAY = ? \r\n" + "        AND stc.SEQUENCE = ? ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(shiftHdrIdQry,shiftDay,seq);
			shiftHdrId = resultMap.get("SHIFT_MST_TYPE_DESCRIPTION").toString();
		} catch (Exception ex) {
			logger.error("CommonMethod getShiftHdrId Method Exception --->" + ex);
		}
		return shiftHdrId;
	}

	public static String addDateValue(String d, int no) {
		String addedDate = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(sdfFormateDate);
			Date date = new Date();
			date = dateFormat.parse(d);
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(date);
			caldate.add(Calendar.DATE, no);
			date = caldate.getTime();
			addedDate = dateFormat.format(date);
		} catch (Exception ex) {
			logger.error("CommonMethod addDateValue Method Exception --->" + ex);
		}
		return addedDate;
	}

	public static String addYearDateValue(String d, int no) {
		String addedDate = "";
		try {
//			DateFormat sdf = new SimpleDateFormat(sdfformate);
			SimpleDateFormat dateFormat = new SimpleDateFormat(sdfFormateDate);
			Date date = new Date();
			date = dateFormat.parse(d);
			Calendar caldate = Calendar.getInstance();
			caldate.setTime(date);
			caldate.add(Calendar.DATE, no);
			date = caldate.getTime();
			addedDate = dateFormat.format(date);
		} catch (Exception ex) {
			logger.error("CommonMethod addDateValue Method Exception --->" + ex);
		}
		return addedDate;
	}

	public static String getDBDateToViewByMonthTwo(String d) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = null;
		try {
			date = format.parse(d);
			dateTime = formatter.format(date);
		} catch (ParseException e) {
			logger.error("Common Method getDBDateToViewByMonthTwo Method Exception-->" + e);
		}

		return dateTime;
	}

	public static String getMonthInfoForDashboard(String monthYear) {
		String returnDate = "";
		try {
			String dateval = monthYear + "-01";
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(formater.parse(dateval));
			cal2.setTime(new Date());

			if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
				int currenDate = cal2.get(Calendar.DATE);
				if (currenDate < 10) {
					returnDate = monthYear + "-0" + currenDate;
				} else {
					returnDate = monthYear + "-" + currenDate;
				}
			} else {
				int lastDayOfMonth = cal1.getActualMaximum(Calendar.DATE);
				if (lastDayOfMonth < 10) {
					returnDate = monthYear + "-0" + lastDayOfMonth;
				} else {
					returnDate = monthYear + "-" + lastDayOfMonth;
				}
			}

		} catch (ParseException e) {
			logger.error("getMonthInfoForDashboard exception :" + e);
		}
		return returnDate;
	}

	public static String getDBDateToViewByMonthThree(String d) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String dateTime = null;
		try {
			date = format.parse(d);
			dateTime = formatter.format(date);
		} catch (ParseException e) {
			logger.error("Common Method getDBDateToViewByMonthThree Method Exception-->" + e);
		}

		return dateTime;
	}

	public static String getDBDateToViewByMonthYear(String d) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM");
		String dateTime = null;
		try {
			date = format.parse(d);
			dateTime = formatter.format(date);
		} catch (ParseException e) {
			logger.error("Common Method getDBDateToViewByMonthThree Method Exception-->" + e);
		}

		return dateTime;
	}

	public static String getMonthToViewByMonthThree(String d) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM");
		SimpleDateFormat formatter = new SimpleDateFormat("MMM");
		String dateTime = null;
		try {
			date = format.parse(d);
			dateTime = formatter.format(date);
		} catch (ParseException e) {
			logger.error("Common Method getMonthToViewByMonthThree Method Exception-->" + e);
		}

		return dateTime;
	}

	public static String getCurrentDateTimeForReport() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getCurrentDateTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String dateTime = formatter.format(date);
		return dateTime;
	}

	public static String getEmployeeNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(1, existingCode.length())) + 1);

		if (existingCode != null && !existingCode.isEmpty()) {
			String postfix = existingCode.substring(1, existingCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "E0010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "E0100";
			} else {
				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 4) {
					newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = prefix + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "E0001";
		return newCode;
	}

	public static String getDBDateToStringDate(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getDBYearMonthToStringMonth(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getDBMonthYearToStringMonth(Date d) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMM-yyyy");
		String dateTime = formatter.format(d);
		return dateTime;
	}

	public static String getStartEndDateOfMonth(String date) {
		String startEndDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(parse);

			Calendar calendar = Calendar.getInstance();
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
			int numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

			String startDate = sdf.format(calendar.getTime());

			calendar.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1);
			String endDate = sdf.format(calendar.getTime());

			startEndDate = startDate + "|" + endDate;
		} catch (Exception e) {
			logger.error("Common Method getStartEndDateOfMonth Method Exception-->" + e);
		}
		return startEndDate;
	}

	public static String getMonthAndDayofWeekAndYear(String date) {
		String dateDetails = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dayofweekSF = new SimpleDateFormat("EEEEE");
			SimpleDateFormat monthSF = new SimpleDateFormat("MMMMM");
			SimpleDateFormat yearSF = new SimpleDateFormat("yyyy");
			Date parse = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(parse);
			String dayOfWeek = dayofweekSF.format(c.getTime());
			String monthofYear = monthSF.format(c.getTime());
			String year = yearSF.format(c.getTime());
			dateDetails = dayOfWeek + "|" + monthofYear + "|" + year;
		} catch (Exception e) {
			logger.error("Common Method getMonthAndDayofWeekAndYear Method Exception-->" + e);
		}
		return dateDetails;
	}

	public static String getSourceFileExtension(String fileName) {
		String fileExt = "";
		try {
			if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
				fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
			else
				return fileExt;
		} catch (Exception ex) {
			logger.error("getSourceFileExtension exception-->" + ex);
		}
		return fileExt;

	}

	public static String getVendorNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(1, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(1, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = "V010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = "V100";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = "V001";
		} catch (Exception e) {
			logger.error("Common Method getVendorNewCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getReasonNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(1, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(1, existingCode.length());
				if ((Integer.parseInt(postfix) + "").length() < 4) {
					newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = prefix + (Integer.parseInt(postfix) + 1);
			} else
				newCode = "R0001";
		} catch (Exception e) {
			logger.error("Common Method getReasonNewCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getOperationtNewCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);

		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				// adding tenant based id
				String postfix = existingCode.substring(ValidprefixLength, existingCode.length());
				logger.info("postfix -->" + Integer.parseInt(postfix));
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "0010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "0100";
				} else if (Integer.parseInt(postfix) == 999) {
					newCode = prefix + "1000";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 4) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = startIdVal;
		} catch (Exception e) {
			logger.error("Common Method getOperationtNewCode Method Exception-->" + e);
		}
		logger.info("Final newCode -->" + newCode);
		return newCode;
	}

	public static String getPoTypeNewCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);

		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				// adding tenant based id
				String postfix = existingCode.substring(ValidprefixLength, existingCode.length());
				logger.info("postfix -->" + Integer.parseInt(postfix));
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "0010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "0100";
				} else if (Integer.parseInt(postfix) == 999) {
					newCode = prefix + "1000";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 4) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = startIdVal;
		} catch (Exception e) {
			logger.error("Common Method getPoTypeNewCode Method Exception-->" + e);
		}
		logger.info("Final newCode -->" + newCode);
		return newCode;
	}

	public static String getProductGroupNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(2, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(2, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "010";// "PG010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "100";// "PG100";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = "PG001";
		} catch (Exception e) {
			logger.error("Common Method getProductGroupNewCode Method Exception-->" + e);
		}
		logger.info(newCode);
		return newCode;
	}

	public static String getUomNewCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(ValidprefixLength, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "0010";// "U0010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "0100";// "U0100";
				} else if (Integer.parseInt(postfix) == 999) {
					newCode = prefix + "1000";// "U0100";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 4) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = startIdVal;
		} catch (Exception e) {
			logger.error("Common Method getUomNewCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getCustomerCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(ValidprefixLength, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "0010";// "U0010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "0100";// "U0100";
				} else if (Integer.parseInt(postfix) == 999) {
					newCode = prefix + "1000";// "U0100";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 4) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = startIdVal;
		} catch (Exception e) {
			logger.error("Common Method getCustomerCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getLineNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(1, existingCode.length())) + 1);

		if (existingCode != null && !existingCode.isEmpty()) {
			String postfix = existingCode.substring(1, existingCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "L0010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "L0100";
			} else {
				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 4) {
					newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = prefix + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "L0001";
		return newCode;
	}

	public static String getPlantLocationNewCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(ValidprefixLength, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = prefix + "0010";// "EC010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = prefix + "0100";// "EC100";
				} else if (Integer.parseInt(postfix) == 999) {
					newCode = prefix + "1000";// "EC100";
				} else {

					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 4) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else {
				newCode = startIdVal;
			}

		} catch (Exception e) {
			logger.error("Common Method getPlantLocationNewCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getProgramNewCode(String prefix, String existingCode, String startIdVal) {
		int ValidprefixLength = prefix.length() != 0 ? prefix.length() : 0;
		String newCode = prefix
				+ (Integer.parseInt(existingCode.substring(ValidprefixLength, existingCode.length())) + 1);

		if (existingCode != null && !existingCode.isEmpty()) {
			String postfix = existingCode.substring(ValidprefixLength, existingCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = prefix + "0010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = prefix + "0100";
			} else if (Integer.parseInt(postfix) == 999) {
				newCode = prefix + "1000";
			} else {
				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = prefix + "000" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 4) {
					newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = prefix + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = startIdVal;
		return newCode;
	}

	public static String getTrainingTypeNewCode(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(3, existingCode.length())) + 1);
		try {
			if (existingCode != null && !existingCode.isEmpty()) {
				String postfix = existingCode.substring(3, existingCode.length());
				if (Integer.parseInt(postfix) == 9) {
					newCode = "TTC010";
				} else if (Integer.parseInt(postfix) == 99) {
					newCode = "TTC100";
				} else {
					if ((Integer.parseInt(postfix) + "").length() < 2) {
						newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
					} else if ((Integer.parseInt(postfix) + "").length() < 3) {
						newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
					} else
						newCode = prefix + (Integer.parseInt(postfix) + 1);
				}
			} else
				newCode = "TTC001";
		} catch (Exception e) {
			logger.error("Common Method getTrainingTypeNewCode Method Exception-->" + e);
		}
		return newCode;
	}

	public static String getGaugeTypeNewCode(String gaugetypecode) {
		String newCode;

		if (gaugetypecode != null && !gaugetypecode.isEmpty()) {
			newCode = "GT" + (Integer.parseInt(gaugetypecode.substring(2, gaugetypecode.length())) + 1);
			String postfix = gaugetypecode.substring(2, gaugetypecode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "GT010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "GT100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "GT" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "GT" + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "GT" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "GT001";
		return newCode;
	}

	public static String getOperObsShtMst(String prefix, String existingCode) {
		String newCode = prefix + (Integer.parseInt(existingCode.substring(2, existingCode.length())) + 1);
		;

		if (existingCode != null && !existingCode.isEmpty()) {
			newCode = prefix + (Integer.parseInt(existingCode.substring(2, existingCode.length())) + 1);
			String postfix = existingCode.substring(2, existingCode.length());
			logger.info("postfix--->" + postfix);
			logger.info("postfix--->" + (Integer.parseInt(postfix) + "").length());
			if (Integer.parseInt(postfix) == 9) {
				newCode = "OO010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "OO100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = prefix + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = prefix + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = prefix + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "OO001";
		return newCode;
	}

	public static String calculateExtraShiftTime(Date startTime, Date endTime, Date luchTime, Date endDate) {

		String shiftEndTime = "";
		try {

			if ((luchTime.after(startTime) || luchTime.equals(startTime)) && luchTime.before(endTime)) {

				long milliseconds = (endTime.getTime()) + ((endDate.getTime() - luchTime.getTime()));
				Date actualTimeStamp = new Date(milliseconds);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(actualTimeStamp);
				int endHour = calendar.get(Calendar.HOUR_OF_DAY);
				int endMint = calendar.get(Calendar.MINUTE);
				String lunchEndHour = "" + endHour;
				String lunchEndMinute = "" + endMint;
				if (endHour < 10) {
					lunchEndHour = "0" + lunchEndHour;
				}
				if (endMint < 10) {
					lunchEndMinute = "0" + lunchEndMinute;
				}
				shiftEndTime = lunchEndHour + ":" + lunchEndMinute;
			}

		} catch (Exception e) {

			logger.error("calculateExtraShiftTime methode error" + e);
		}
		return shiftEndTime;
	}

	public static String[] generateUIId(String transactionDate, String tenantId, String transactionType,
			String branchCode, JdbcTemplate jdbcTemplate) {

		String uiIdArray[] = new String[3];
		String uiId = "";
		String transId = "";
		String financialYear = "";
		String existQry = "";
		String lastIdQry = "";
		try {

			String financialYrQry = "select FINANCIAL_YEAR from financial_year_mst where ? between START_DATE and END_DATE and IS_ACTIVE='1'";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(financialYrQry, transactionDate);
			financialYear = resultMap.get("FINANCIAL_YEAR").toString();
			
			String idParamQry = "select PREFIX_IDENTIFIER,ID_START,SUFFIX_IDENTIFIER from financial_year_transaction_mst where INVENTORY_TRANSACTION_TYPE_CODE=? and TENANT_ID=? and IS_ACTIVE=?";
			RowMapper<TransactionMstDtls> rowMapper = new TransactionMstDtlsRowMapper();
			List<TransactionMstDtls> tdList = new ArrayList<TransactionMstDtls>();

			if (transactionType.equalsIgnoreCase("GRL")) {

				existQry = "select count(*) as COUNT from grn_lite_hdr where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=?";

				tdList = jdbcTemplate.query(idParamQry, rowMapper, "GRL", tenantId, 1);
				
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(existQry, financialYear,tenantId,branchCode);
				int entryExist = Integer.parseInt(resultMap1.get("COUNT").toString());

				if (entryExist == 0) {
					transId = tdList.get(0).getIdStart();
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				} else {
					lastIdQry = "Select TRANSACTION_ID from grn_lite_hdr  where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=? order by GRN_LITE_HDR_ID desc limit 1";
					Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(lastIdQry, financialYear,tenantId,branchCode);
					String lastId  = resultMap2.get("TRANSACTION_ID").toString();
					transId = (Integer.parseInt(lastId) + 1) + "";
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				}

			} else if (transactionType.equalsIgnoreCase("TME")) {

				tdList = jdbcTemplate.query(idParamQry, rowMapper, "TME", tenantId, 1);
				existQry = "select count(*) as COUNT from task_entry_hdr where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=?";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(existQry, financialYear,tenantId,branchCode);
				int entryExist = Integer.parseInt(resultMap1.get("COUNT").toString());
				if (entryExist == 0) {
					transId = tdList.get(0).getIdStart();
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				} else {
					lastIdQry = "Select TRANSACTION_ID from task_entry_hdr  where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=? order by TE_HDR_ID desc limit 1";
					Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(lastIdQry, financialYear,tenantId,branchCode);
					String lastId  = resultMap2.get("TRANSACTION_ID").toString();
					transId = (Integer.parseInt(lastId) + 1) + "";
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				}

			} else if (transactionType.equalsIgnoreCase("MRE")) {

				tdList = jdbcTemplate.query(idParamQry, rowMapper, "MRE", tenantId, 1);
				existQry = "select count(*) as COUNT from material_return_hdr where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=?";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(existQry, financialYear,tenantId,branchCode);
				int entryExist = Integer.parseInt(resultMap1.get("COUNT").toString());
				if (entryExist == 0) {
					transId = tdList.get(0).getIdStart();
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				} else {
					lastIdQry = "Select TRANSACTION_ID from material_return_hdr  where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=? order by MATERIAL_RETURN_HDR_ID desc limit 1";
					Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(lastIdQry, financialYear,tenantId,branchCode);
					String lastId  = resultMap2.get("TRANSACTION_ID").toString();
					transId = (Integer.parseInt(lastId) + 1) + "";
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				}

			} else if (transactionType.equalsIgnoreCase("PR")) {

				tdList = jdbcTemplate.query(idParamQry, rowMapper, "PR", tenantId, 1);
				existQry = "select count(*) as COUNT from pr_hdr where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=?";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(existQry, financialYear,tenantId,branchCode);
				int entryExist = Integer.parseInt(resultMap1.get("COUNT").toString());
				if (entryExist == 0) {
					transId = tdList.get(0).getIdStart();
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				} else {
					lastIdQry = "Select TRANSACTION_ID from pr_hdr  where FINANCIAL_YEAR=? and TENANT_ID=? and BRANCH_CODE=? order by TRANSACTION_ID desc limit 1";
					Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(lastIdQry, financialYear,tenantId,branchCode);
					String lastId  = resultMap2.get("TRANSACTION_ID").toString();
					transId = (Integer.parseInt(lastId) + 1) + "";
					uiId = transId + tdList.get(0).getPrefix() + financialYear + tdList.get(0).getSuffix();
				}

			}

		} catch (Exception e) {
			logger.error("<---Common-----generateUIId---Exception------->" + e);
		}
		uiIdArray[0] = uiId;
		uiIdArray[1] = financialYear;
		uiIdArray[2] = transId;
		return uiIdArray;
	}

	public static String validateQuantity(String productCode, BigDecimal quantity, String locationCode, String branch,
			String tenantid, JdbcTemplate jdbcTemplate) {

		String returnmsg = null;
		try {
			if (productCode != null && quantity != null && locationCode != null && branch != null && tenantid != null) {

				String countcheck = "SELECT   count(ipd.PRODUCT_QUANTITY_ON_HAND) as COUNT FROM\r\n"
						+ "					inventory_product_hdr AS iph,    inventory_product_dtl AS ipd\r\n"
						+ "					WHERE    iph.INVENTORY_PRODUCT_HDR_ID = ipd.INVENTORY_PRODUCT_HDR_ID\r\n"
						+ "					       AND iph.TENANT_ID = ipd.TENANT_ID\r\n"
						+ "					      AND iph.PRODUCT_CODE =? AND iph.TENANT_ID = ? AND ipd.INVENTORY_LOCATION_CODE = ?";
				
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(countcheck, productCode,tenantid,locationCode);
				Number numbercount = Integer.parseInt(resultMap1.get("COUNT").toString());
				int checkCountRes = (numbercount != null ? numbercount.intValue() : 0);
				if (checkCountRes > 0) {
					String validationQuery = "SELECT \r\n" + "    ipd.PRODUCT_QUANTITY_ON_HAND\r\n" + "FROM\r\n"
							+ "    inventory_product_hdr AS iph,\r\n" + "    inventory_product_dtl AS ipd\r\n"
							+ "WHERE\r\n" + "    iph.INVENTORY_PRODUCT_HDR_ID = ipd.INVENTORY_PRODUCT_HDR_ID\r\n"
							+ "        AND iph.TENANT_ID = ipd.TENANT_ID\r\n" + "        AND iph.PRODUCT_CODE = ? AND iph.TENANT_ID = ?\r\n"
							+ "        AND ipd.INVENTORY_LOCATION_CODE = ? AND ipd.BRANCH_CODE = ?";
					
					Map<String, Object> resultMap = jdbcTemplate.queryForMap(validationQuery,productCode,tenantid,locationCode,branch);
					BigDecimal number = (resultMap.get("PRODUCT_QUANTITY_ON_HAND") != null) ? new BigDecimal(resultMap.get("PRODUCT_QUANTITY_ON_HAND").toString()) : BigDecimal.ZERO;


					if (quantity.compareTo(number) > 0) {
						returnmsg = "NOTOK|" + number.toString() + "|" + productCode + "|" + quantity;
					} else {
						returnmsg = "OK";
					}
				} else {
					returnmsg = "LOCATION|" + productCode;
				}
			}
		} catch (Exception ex) {
			logger.error("validateQuantity exception-->" + ex);
		}
		return returnmsg;

	}

	public static String getInstrumentTypeNewCode(String instrumenttypecode) {
		String newCode;

		if (instrumenttypecode != null && !instrumenttypecode.isEmpty()) {
			newCode = "IT" + (Integer.parseInt(instrumenttypecode.substring(2, instrumenttypecode.length())) + 1);
			String postfix = instrumenttypecode.substring(2, instrumenttypecode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "IT010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "IT100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "IT" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "IT" + "0" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "IT" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "IT001";
		return newCode;
	}

	public static List<FinancialYearMst> getFinancialYearMonthDate(String date, String type, JdbcTemplate jdbcTemplate,
			String tenantId) {
		List<FinancialYearMst> returnList = null;
		try {
			String query = "";
			if (type.equalsIgnoreCase("date")) {
				query = "select START_DATE,END_DATE from financial_year_mst where ? between START_DATE and END_DATE and IS_ACTIVE <> 0 and TENANT_ID = ?";
			} else if (type.equalsIgnoreCase("month")) {
				query = "select MONTH(START_DATE) as START_DATE,MONTH(END_DATE) as END_DATE from financial_year_mst where ? between START_DATE and END_DATE and IS_ACTIVE <> 0 and TENANT_ID = ?";
			} else if (type.equalsIgnoreCase("year")) {
				query = "select YEAR(START_DATE) as START_DATE,YEAR(END_DATE) as END_DATE from financial_year_mst where ? between START_DATE and END_DATE and IS_ACTIVE <> 0 and TENANT_ID = ?";
			}
			RowMapper<FinancialYearMst> rowmapper = new FinancialYearRowMapper();
			returnList = jdbcTemplate.query(query, rowmapper, date, tenantId);
		} catch (Exception ex) {
			logger.error("getFinancialYearMonthDate Commonmethod dao exception -->" + ex);
		}
		return returnList;
	}

	public static String generateNewPicklist(JdbcTemplate jdbcTemplate, String productCode, String tolocation,
			String wORKORDER_ID, int quantityonhand, String iNPUT_PRODUCT_QUANTITY, String tENANT_ID, String EMP_ID,
			String BRANCH_CODE) {
		String returnList = "";
		try {
			List<locationinvntryentity> locattionset = new ArrayList<locationinvntryentity>();
			RowMapper<locationinvntryentity> rowmapper = new locationinvntryRowMapper();
			String query1 = "SELECT \r\n" + "    ilm.INVENTORY_LOCATION_CODE, ipd.INVENTORY_PRODUCT_HDR_ID\r\n"
					+ "FROM\r\n" + "    inventory_product_dtl AS ipd\r\n" + "        INNER JOIN\r\n"
					+ "    inventory_product_hdr AS iph ON ipd.INVENTORY_PRODUCT_HDR_ID = iph.INVENTORY_PRODUCT_HDR_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    inventory_location_mst AS ilm ON ilm.INVENTORY_LOCATION_CODE = ipd.INVENTORY_LOCATION_CODE\r\n"
					+ "WHERE\r\n" + "    iph.PRODUCT_CODE = '" + productCode + "'\r\n"
					+ "        AND ilm.INVENTORY_LOCATION_PARENT_CODE = 'L1S1'\r\n" + "LIMIT 1;";
			locattionset = jdbcTemplate.query(query1, rowmapper);
			String pickListhdr = "INSERT INTO picklist_hdr (CREATED_DATETIME, PICKLIST_TYPE_CODE, INVENTORY_LOCATION_CODE,\r\n"
					+ " CREATED_BY, BRANCH_CODE, TENANT_ID, IS_MANUAL, STATUS)\r\n" + " VALUES (?,?,?,?,?,?,?,?);";
			String locationcodestr = locattionset.get(0).getInventorylocationcode();
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(pickListhdr, Statement.RETURN_GENERATED_KEYS);
					ps.setObject(1, CommonMethod.getCurrentDateTime());
					ps.setString(2, "PL002");
					ps.setString(3, locationcodestr);
					ps.setString(4, EMP_ID);
					ps.setString(5, BRANCH_CODE);
					ps.setString(6, tENANT_ID);
					ps.setString(7, "0");
					ps.setString(8, "DS030");
					return ps;

				}
			}, holder);
			int newLoghdrId = holder.getKey().intValue();
			logger.info("Shift Log HDR new id", newLoghdrId);
			List<MaxQuantityorferentity> MaxQuantityValue = new ArrayList<MaxQuantityorferentity>();
			RowMapper<MaxQuantityorferentity> MaxQuantityrowmapper = new MaxQuantityorferRowMapper();
			if (newLoghdrId > 0) {
				String maxQunty = "SELECT \r\n" + "    MAXIMUM_ORDER_QUANTITY\r\n" + "FROM\r\n"
						+ "    inventory_product_hdr_extn\r\n" + "WHERE\r\n" + "    INVENTORY_PRODUCT_HDR_ID = '"
						+ locattionset.get(0).getInventoryproducthdrid() + "';";
				MaxQuantityValue = jdbcTemplate.query(maxQunty, MaxQuantityrowmapper);
				int insertpklDtlid = 0;
				String pklDtlQuery = " INSERT INTO picklist_dtl \r\n"
						+ " (PL_HDR_ID, PRODUCT_CODE, WORKORDER_ID, WO_QTY, FROM_LOCATION, TRANSFER_QTY, TO_LOCATION, STATUS_CODE,\r\n"
						+ " IS_WO_APPLICABLE, AVAILABLE_QTY) \r\n" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				insertpklDtlid = jdbcTemplate.update(pklDtlQuery, newLoghdrId, productCode, wORKORDER_ID,
						iNPUT_PRODUCT_QUANTITY, locattionset.get(0).getInventorylocationcode(),
						MaxQuantityValue.get(0).getMaximumorderquantity(), tolocation, "DS030", 1,
						quantityonhand + 0.000);
				if (insertpklDtlid > 0) {
					returnList = "success";
				} else {
					returnList = "Insert Failed";
				}
			}
		} catch (Exception ex) {
			logger.error("generateNewPicklist Commonmethod dao exception -->" + ex);
		}
		return returnList;
	}

	public static String getPreviuosDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		Date oneDayBefore = new Date(date.getTime() - (24 * 3600000));

		String result = dateFormat.format(oneDayBefore);
		return result;
	}

	public static String getPrevDate(String indate) {
		String prevDate = "";
		String prevmonth = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		try {
			parse = dateFormat.parse(indate);
		} catch (ParseException e) {
			logger.error("getPrevDate exception :" + e);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(parse);
		c.add(Calendar.DATE, -1);

		int forYear = c.get(Calendar.YEAR);
		int forMonth = c.get(Calendar.MONTH);
		int forMonths = forMonth + 1;
		int forDate = c.get(Calendar.DATE);
		if (forDate < 10) {
			prevDate = "0" + Integer.toString(forDate);
		} else {
			prevDate = Integer.toString(forDate);
		}
		if (forMonths < 10) {
			prevmonth = "0" + Integer.toString(forMonths);
		} else {
			prevmonth = Integer.toString(forMonths);
		}

		String result = forYear + "-" + prevmonth + "-" + prevDate;

		return result;
	}
	public static String getPrevMonth(String monthYear) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date parse = null;
		try {
			parse = dateFormat.parse(monthYear);
		} catch (ParseException e) {
			logger.error("getPrevDate exception :" + e);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(parse);
		c.add(Calendar.MONTH, -1);
		String strMonth = dateFormat.format(c.getTime());
		return strMonth;
	}
	public static String getNextDate(String indate) {
		String prevDate = "";
		String prevmonth = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		try {
			parse = dateFormat.parse(indate);
		} catch (ParseException e) {
			logger.error("getPrevDate exception :" + e);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(parse);
		c.add(Calendar.DATE, 1);

		int forYear = c.get(Calendar.YEAR);
		int forMonth = c.get(Calendar.MONTH);
		int forMonths = forMonth + 1;
		int forDate = c.get(Calendar.DATE);
		if (forDate < 10) {
			prevDate = "0" + Integer.toString(forDate);
		} else {
			prevDate = Integer.toString(forDate);
		}
		if (forMonths < 10) {
			prevmonth = "0" + Integer.toString(forMonths);
		} else {
			prevmonth = Integer.toString(forMonths);
		}

		String result = forYear + "-" + prevmonth + "-" + prevDate;

		return result;
	}

	public static String getDateFromCal(Calendar calDate) {
		String date = "";
		try {
			int yr = calDate.get(Calendar.YEAR);
			int mo = calDate.get(Calendar.MONTH) + 1;
			int dd = calDate.get(Calendar.DATE);
			String month = "";
			if (mo < 10) {
				month = "0" + mo;
			} else {
				month = Integer.toString(mo);
			}
			String d = "";
			if (dd < 10) {
				d = "0" + dd;
			} else {
				d = Integer.toString(dd);
			}
			date = yr + "-" + month + "-" + d;
		} catch (Exception ex) {

		}
		return date;
	}

	public static String getTimeFromCal(Calendar calDate) {
		String date = "00:00";
		try {
			Date dateFromCal = calDate.getTime();
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			date = dateFormat.format(dateFromCal);
		} catch (Exception ex) {
			logger.error("getTimeFromCal Method Exception");
		}
		return date;
	}

	public static String getDateTimeFromCal(Calendar calDate) {
		String date = "00:00";
		try {
			Date dateFromCal = calDate.getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = dateFormat.format(dateFromCal);
		} catch (Exception ex) {
			logger.error("getTimeFromCal Method Exception");
		}
		return date;
	}

	public static String getTimeFromDate(String dateTime) {
		String responseText = "";
		try {
			SimpleDateFormat format2 = new SimpleDateFormat(CommonMethod.sdfformatethree);

			Date hourStart = format2.parse(dateTime);
			Calendar hourStartCal = Calendar.getInstance();
			hourStartCal.setTime(hourStart);

			SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
			responseText = format1.format(hourStart);
		} catch (Exception ex) {
			logger.error("getTimeFromDate method error :" + ex);
		}
		return responseText;

	}

	public static String formatTimeString(String timeFrmt) {
		String formatTime = "";
		try {
			String[] timeFrmtSplit = timeFrmt.split(":");
			if (timeFrmtSplit.length > 0 && timeFrmtSplit.length > 3) {
				formatTime = timeFrmtSplit[0] + ":" + timeFrmtSplit[1];
			} else if (timeFrmtSplit.length > 0 && timeFrmtSplit.length < 3) {
				formatTime = timeFrmtSplit[0] + ":" + timeFrmtSplit[1];
			} else {
				formatTime = timeFrmt;
			}
		} catch (Exception ex) {

		}
		return formatTime;
	}

	public static int getCurrentDateDayOnly() {

		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		Date date = new Date();
		String dateTime = formatter.format(date);
		int day = Integer.parseInt(dateTime);
		return day;
	}

	public static String getDateTimeStringReturnDate(String d) {
		String dateTime = "";
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateTime = formatter.format(date1);
		} catch (Exception e) {
			logger.error("Exception in getDBDateMinToView Common method" + e);
		}

		return dateTime;
	}

	public static String getInventoryProductDtlId(JdbcTemplate jdbcTemplate, String productCode, String projectId,
			String locationCode, String tenantId) {

		String returnmsg = "";
		int productId;

		try {
			String query = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(PRODUCT_ID) > 0 THEN PRODUCT_ID\r\n"
					+ "        ELSE 0\r\n" + "    END as COUNT\r\n" + "FROM\r\n" + "    product_mst\r\n" + "WHERE\r\n"
					+ "    PRODUCT_ID = ?\r\n" + "        AND PM_HDR_ID = ?\r\n"
					+ "        AND TENANT_ID = ?;";
			
			Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(query, productCode,projectId,tenantId);
			productId = Integer.parseInt(resultMap1.get("COUNT").toString());

			if (productId == 0) {
				List<ProductMstTableEntity> list = new ArrayList<ProductMstTableEntity>();
				String producmstdtl = "SELECT \r\n" + " PRODUCT_CODE,   PRODUCT_DESCRIPTION,\r\n" + "    PRODUCT_UOM_CODE,\r\n"
						+ "    TENANT_ID\r\n" + "FROM\r\n" + "    product_mst where PRODUCT_ID = '" + productCode
						+ "';";

				list = jdbcTemplate.query(producmstdtl, new ProductMstTableRowMapper());

				final String prodDesc = list.get(0).getProductDesc();
				final String prodCode = list.get(0).getProdCode();
				final String produomCode = list.get(0).getProductUomCode();
				final String tenantID = list.get(0).getTenatid();

				LocalDateTime createddatetime = LocalDateTime.now();

				final String date = String.valueOf(createddatetime);

				String productmstInsertquery = "insert into product_mst(PRODUCT_CODE,PRODUCT_DESCRIPTION,PM_HDR_ID,PRODUCT_UOM_CODE,CREATED_USER_ID,CREATED_DATETIME,LAST_UPDATED_USER_ID,LAST_UPDATED_DATETIME,TENANT_ID) values(?,?,?,?,?,?,?,?,?) ;";
				KeyHolder holder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(productmstInsertquery,
								Statement.RETURN_GENERATED_KEYS);
						ps.setObject(1, prodCode);
						ps.setString(2, prodDesc);
						ps.setString(3, projectId);
						ps.setString(4, produomCode);
						ps.setString(5, "E0001");
						ps.setString(6, date);
						ps.setString(7, "E0001");
						ps.setString(8, date);
						ps.setString(9, tenantID);
						return ps;
					}
				}, holder);

				productId = holder.getKey().intValue();

			}
			String query1 = "SELECT \r\n"
					+ "  case when COUNT(INVENTORY_PRODUCT_DTL_ID) > 0 then PRODUCT_ID else 0 end as COUNT\r\n" + "FROM\r\n"
					+ "    inventory_product_dtl\r\n" + "WHERE\r\n" + "    PRODUCT_ID = ?\r\n"
					+ "        AND INVENTORY_LOCATION_CODE = ? AND TENANT_ID = ?";
			
			Map<String, Object> resultMap2 = jdbcTemplate.queryForMap(query1, productId,locationCode,tenantId);
			int id = Integer.parseInt(resultMap2.get("COUNT").toString());
			final String productId1 = String.valueOf(productId);
			if (id == 0) {
				String productdtlinsert = "insert into inventory_product_dtl(PRODUCT_ID,INVENTORY_LOCATION_CODE,TENANT_ID) values (?,?,?);";
				KeyHolder holder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(productdtlinsert,
								Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, productId1);
						ps.setString(2, locationCode);
						ps.setString(3, tenantId);
						return ps;
					}
				}, holder);

				returnmsg = String.valueOf(productId);

			} else {
				returnmsg = String.valueOf(productId);
			}
		} catch (Exception e) {
			logger.error("getInventoryProductDtlId Method Exception --->" + e);
		}
		return returnmsg;
	}

	public static int updateProductInvDtl(String projectId, String productCode, String locationCode,
			BigDecimal transQty, String operation, String invTransType, String transRefId, String transferredBy,
			String transDateTime, String tenantId, JdbcTemplate jdbcTemplate) {
		return updateProductInvDtl(projectId, productCode, locationCode, transQty, operation, invTransType,
				transRefId, transferredBy, transDateTime, tenantId, jdbcTemplate, null, null);
	}

	// Overload used for group-sourced (Material Staging/Group Return) movements -
	// same real inventory_product_dtl update as above, but also tags the journal
	// row with the Staging Group's MS_HDR_ID/MS_NAME so it can be shown/filtered
	// as "Group" type on the Inventory Journal screen instead of a bare part row.
	public static int updateProductInvDtl(String projectId, String productCode, String locationCode,
			BigDecimal transQty, String operation, String invTransType, String transRefId, String transferredBy,
			String transDateTime, String tenantId, JdbcTemplate jdbcTemplate, String msHdrId, String msName) {
		int returnVal = 0;
		try {
			if (projectId != null) {
				String ProdId = getInventoryProductDtlId(jdbcTemplate, productCode, projectId, locationCode, tenantId);
				if (operation.equalsIgnoreCase("Subraction")) {
					transQty = transQty.negate();
				}
				String qry="select PRODUCT_QUANTITY_ON_HAND from inventory_product_dtl where INVENTORY_LOCATION_CODE = ? and PRODUCT_ID = ? and TENANT_ID = ?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,locationCode,ProdId,tenantId);
				String qtyOnHand = resultMap.get("PRODUCT_QUANTITY_ON_HAND").toString();
				String updateQry = "UPDATE inventory_product_dtl \r\n" +
						"SET \r\n" +
						"    PRODUCT_QUANTITY_ON_HAND = CASE\r\n" +
						"        WHEN (PRODUCT_QUANTITY_ON_HAND + ?) < 0 THEN 0\r\n" +
						"        ELSE PRODUCT_QUANTITY_ON_HAND + ?\r\n" +
						"    END\r\n" +
						"WHERE\r\n" +
						"    INVENTORY_LOCATION_CODE = ?\r\n" +
						"        AND PRODUCT_ID = ?\r\n" +
						"        AND TENANT_ID = ?;";
				logger.info("updateProductInvDtl in Store Inventory Balance for Transaction Type - " + invTransType
						+ " - Transaction Ref Id - " + transRefId + " - Transaction DateTime - " + transDateTime);
				returnVal = jdbcTemplate.update(updateQry,transQty,transQty,locationCode,ProdId,tenantId);
				if (returnVal > 0) {
					updateInvjournal(projectId, ProdId, locationCode, invTransType, transRefId, transDateTime,
							transferredBy, new BigDecimal(qtyOnHand), transQty, tenantId, jdbcTemplate, msHdrId, msName);
				}
			}
		} catch (Exception ex) {
			logger.error("UpdateProductInvDtl Method Exception --->" + ex);
		}
		return returnVal;
	}

	public static int updateInvjournal(String projectId, String productId, String locCode, String invTransType,
			String transRefId, String transDateTime, String transferredBy, BigDecimal openingBal, BigDecimal transQty,
			String tenantId, JdbcTemplate jdbcTemplate) {
		return updateInvjournal(projectId, productId, locCode, invTransType, transRefId, transDateTime,
				transferredBy, openingBal, transQty, tenantId, jdbcTemplate, null, null);
	}

	public static int updateInvjournal(String projectId, String productId, String locCode, String invTransType,
			String transRefId, String transDateTime, String transferredBy, BigDecimal openingBal, BigDecimal transQty,
			String tenantId, JdbcTemplate jdbcTemplate, String msHdrId, String msName) {
		int returnMsg = 0;
		try {
			BigDecimal closingBal = openingBal.add(transQty);
			String insertJournal = "insert into inventory_journal (PRODUCT_ID, INVENTORY_LOCATION_CODE, INVENTORY_TRANSACTION_TYPE_CODE, INVENTORY_TRANSACTION_REFERENCE_ID, INVENTORY_TRANSACTION_DATE, INVENTORY_TRANSACTION_USER_ID, INVENTORY_RECORD_CREATED_DATE, INVENTORY_TRANSACTION_QUANTITY, TENANT_ID, PROJECT_ID, OPENING_BALANCE, CLOSING_BALANCE, MS_HDR_ID, MS_NAME) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			returnMsg = jdbcTemplate.update(insertJournal, productId, locCode, invTransType, transRefId, transDateTime,
					transferredBy, CommonMethod.getCurrentDateTime(), transQty, tenantId, projectId, openingBal,
					closingBal, msHdrId, msName);
			logger.info("updateInvjournal for Transaction Type - " + invTransType + " - Transaction Ref Id - "
					+ transRefId + " - Transaction DateTime - " + transDateTime);
		} catch (Exception ex) {
			logger.error("updateInvjournal Method Exception --->" + ex);
		}
		return returnMsg;
	}

	// Log-only Inventory Journal entry for group events that must NOT move real
	// stock (Group Return approved / Group DC dispatched) - group items were
	// never added back into ILC0002 as loose stock in the first place (see
	// AssemblyReturnService.ApproveMreturnDtls), so there is nothing real to
	// subtract/add here. Writes a real-part row (Product/UOM/Qty) tagged with
	// MS_HDR_ID/MS_NAME for traceability, but OPENING_BALANCE/CLOSING_BALANCE are
	// both set to the product's CURRENT on-hand qty (unchanged) instead of a real
	// before/after movement, so the Journal never shows a fabricated balance
	// change for a product that didn't actually move.
	public static int logGroupInvJournal(String projectId, String productId, String locationCode,
			BigDecimal transQty, String invTransType, String transRefId, String transferredBy,
			String transDateTime, String tenantId, String msHdrId, String msName, JdbcTemplate jdbcTemplate) {
		int returnMsg = 0;
		try {
			String qry = "select PRODUCT_QUANTITY_ON_HAND from inventory_product_dtl where INVENTORY_LOCATION_CODE = ? and PRODUCT_ID = ? and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, locationCode, productId, tenantId);
			BigDecimal currentQty = new BigDecimal(resultMap.get("PRODUCT_QUANTITY_ON_HAND").toString());
			String insertJournal = "insert into inventory_journal (PRODUCT_ID, INVENTORY_LOCATION_CODE, INVENTORY_TRANSACTION_TYPE_CODE, INVENTORY_TRANSACTION_REFERENCE_ID, INVENTORY_TRANSACTION_DATE, INVENTORY_TRANSACTION_USER_ID, INVENTORY_RECORD_CREATED_DATE, INVENTORY_TRANSACTION_QUANTITY, TENANT_ID, PROJECT_ID, OPENING_BALANCE, CLOSING_BALANCE, MS_HDR_ID, MS_NAME) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			returnMsg = jdbcTemplate.update(insertJournal, productId, locationCode, invTransType, transRefId,
					transDateTime, transferredBy, CommonMethod.getCurrentDateTime(), transQty, tenantId, projectId,
					currentQty, currentQty, msHdrId, msName);
			logger.info("logGroupInvJournal for Transaction Type - " + invTransType + " - Transaction Ref Id - "
					+ transRefId + " - Group - " + msName);
		} catch (Exception ex) {
			logger.error("logGroupInvJournal Method Exception --->" + ex);
		}
		return returnMsg;
	}
	
	public static String getDesignationCode(String designCode) {
		String newCode;

		if (designCode != null && !designCode.isEmpty()) {
			newCode = "DS" + (Integer.parseInt(designCode.substring(2, designCode.length())) + 1);
			String postfix = designCode.substring(2, designCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "DS10";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "DS100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "DS" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "DS" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "DS" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "DS01";
		return newCode;
	}
	public static String getDocStatusCode(String docStatus) {
		String newCode;

		if (docStatus != null && !docStatus.isEmpty()) {
			newCode = "DS0" + (Integer.parseInt(docStatus.substring(2, docStatus.length())) + 1);
			String postfix = docStatus.substring(2, docStatus.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "DS010";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "DS100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "DS" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "DS" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "DS" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "DS01";
		return newCode;
	}
	public static String getTaskTypeNewCode(String taskCode) {
		String newCode="";
		try
		{
		if (taskCode != null && !taskCode.isEmpty()) {
			newCode = "TT" + (Integer.parseInt(taskCode.substring(2, taskCode.length())) + 1);
			String postfix = taskCode.substring(2, taskCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "TT10";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "TT100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "TT" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "TT" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "TT" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "TT001";
		
		}catch(Exception ex) {
			logger.error("getTaskTypeNewCode Method Exception --->" + ex);
		}
		return newCode;
	}
   
	public static String getTaskCategoryMasterNewCode(String taskCode) {
		String newCode="";
		try
		{
		if (taskCode != null && !taskCode.isEmpty()) {
			newCode = "TT" + (Integer.parseInt(taskCode.substring(2, taskCode.length())) + 1);
			String postfix = taskCode.substring(2, taskCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "TC10";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "TC100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "TC" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "TC" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "TC" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "TC001";
		
		}catch(Exception ex) {
			logger.error("getTaskCategoryMasterNewCode Method Exception --->" + ex);
		}
		return newCode;
	}

	public static String genNewPraCode(String praCode) {
		String newCode="";
		try
		{
		if (praCode != null && !praCode.isEmpty()) {
			newCode = "PRA" + (Integer.parseInt(praCode.substring(3, praCode.length())) + 1);
			String postfix = praCode.substring(3, praCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "PRA10";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "PRA100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "PRA" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "PRA" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "PRA" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "PRA001";
		
		}catch(Exception ex) {
			logger.error("genNewPraCode Method Exception --->" + ex);
		}
		return newCode;
	}
	public static String genNewDebitNoteCode(String debitCode) {
		String newCode="";
		try
		{
		if (debitCode != null && !debitCode.isEmpty()) {
			newCode = "DN" + (Integer.parseInt(debitCode.substring(3, debitCode.length())) + 1);
			String postfix = debitCode.substring(3, debitCode.length());

			if (Integer.parseInt(postfix) == 9) {
				newCode = "DN10";
			} else if (Integer.parseInt(postfix) == 99) {
				newCode = "DN100";
			} else {

				if ((Integer.parseInt(postfix) + "").length() < 2) {
					newCode = "DN" + "00" + (Integer.parseInt(postfix) + 1);
				} else if ((Integer.parseInt(postfix) + "").length() < 3) {
					newCode = "DN" + (Integer.parseInt(postfix) + 1);
				} else
					newCode = "DN" + (Integer.parseInt(postfix) + 1);
			}
		} else
			newCode = "DN001";

		}catch(Exception ex) {
			logger.error("genNewDebitNoteCode Method Exception --->" + ex);
		}
		return newCode;
	}
	
	public static String getPrevoiusSeq(String docType, String docGroup, String currSeq, String tenantId, JdbcTemplate jdbcTemplate) {
		String preSeq = null;
		try {
			String qry = " SELECT  CANCEL_SEQ\r\n" + 
					"	FROM   document_lifecycle_mst       \r\n" + 
					"    WHERE DOC_TYPE = ? AND CURR_SEQUENCE = ?\r\n" + 
					"		 AND DOC_GROUP = ? AND TENANT_ID=?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, docType, currSeq, docGroup, tenantId);
			preSeq = resultMap.get("CANCEL_SEQ").toString();
		} catch (Exception ex) {
			logger.error("CommonMethod getPrevoiusSeq Method Exception --->" + ex);
		}
		return preSeq;
	}

	public static String getNextSeq(String docType, String docGroup, String currSeq, String tenantId, JdbcTemplate jdbcTemplate) {
		String nextSeq = null;
		try {
			String qry = "SELECT  NEXT_SEQ\r\n" + 
					"	FROM   document_lifecycle_mst       \r\n" + 
					"    WHERE DOC_TYPE = ? AND CURR_SEQUENCE = ? \r\n" + 
					"		 AND DOC_GROUP = ? AND TENANT_ID=? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, docType, currSeq, docGroup, tenantId);
			nextSeq = resultMap.get("NEXT_SEQ").toString();
		} catch (Exception ex) {
			logger.error("CommonMethod getNextSeq Method Exception --->" + ex);
		}
		return nextSeq;
	}
	
	public static String getIsEditable(String docType, String docGroup, String currSeq, String tenantId, JdbcTemplate jdbcTemplate) {
		String isEdit = null;
		try {
			String qry = "SELECT  IS_EDITABLE   \r\n" + 
				       	 "    FROM    document_lifecycle_mst\r\n" + 
					     "	      WHERE    DOC_TYPE = ?       AND DOC_GROUP = ? AND TENANT_ID= ?\r\n" + 
				     	 "		      AND CURR_SEQUENCE = (SELECT NEXT_SEQ\r\n" + 
					     "					        FROM  document_lifecycle_mst   WHERE\r\n" + 
					     "					            DOC_TYPE = ? AND CURR_SEQUENCE = ?\r\n" + 
					     "							          AND DOC_GROUP = ? AND TENANT_ID= ?) ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, docType, docGroup, tenantId, docType, currSeq, docGroup, tenantId);
			isEdit = resultMap.get("IS_EDITABLE").toString();
		} catch (Exception ex) {
			logger.error("CommonMethod getIsEditable Method Exception --->" + ex);
		}
		return isEdit;
	}
		
	public static String getCostForNotification(String hdrId, String type, String tenantId, JdbcTemplate jdbcTemplate) {
		String cost = "";
		try {
			
			if(type.equalsIgnoreCase("Actual")) {
				String qry = " SELECT SCM_BUDGET_ALLOCATED FROM indent_hdr where INDENT_ID = ? and TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, hdrId, tenantId);
			cost = resultMap.get("SCM_BUDGET_ALLOCATED").toString();
			}else{
				String qry = "SELECT TARGET_VALUE FROM indent_hdr where INDENT_ID = ? and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, hdrId, tenantId);
			cost = resultMap.get("TARGET_VALUE").toString();
			}
			
		} catch (Exception ex) {
			logger.error("CommonMethod getCostForNotification Method Exception --->" + ex);
		}
		return cost;
	}

	public static String safeStr(String input) {
		return input == null ? "" : input;
	}

	public static String safeStrOrDefault(String input, String defaultValue) {
		return (input == null || input.trim().isEmpty()) ? defaultValue : input;
	}


}
