package com.vmfg.sales.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Thrown when one budget sheet row cannot be saved. It is unchecked so the @Transactional DAO
 * rolls back the whole upload instead of committing only the rows before the failing one.
 * The message is shown to the user, e.g. "Row 150: Element Description is too long".
 */
public class BudgetSheetRowException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Map<String, String> COLUMN_LABELS = new HashMap<>();
	static {
		COLUMN_LABELS.put("ELEMENT_HDR", "Element Header");
		COLUMN_LABELS.put("ELEMENT_DTL", "Element Description");
		COLUMN_LABELS.put("SPECIFICATION", "Specification");
		COLUMN_LABELS.put("MAKE", "Make");
		COLUMN_LABELS.put("STN_NO", "Stn No");
		COLUMN_LABELS.put("LEG", "Leg");
		COLUMN_LABELS.put("UOM", "UOM");
		COLUMN_LABELS.put("QTY", "Qty");
		COLUMN_LABELS.put("VALUE", "Value");
		COLUMN_LABELS.put("SUB_TOTAL_VALUE", "Total Value (column K)");
		COLUMN_LABELS.put("SUB_ASSY_VALUE", "Value /Sub Station");
		COLUMN_LABELS.put("CONTIGENCY", "Contigency");
		COLUMN_LABELS.put("TOTAL_VALUE", "Total Value (column N)");
		COLUMN_LABELS.put("TIMELINE_IN_WEEKS", "Timeline (In Weeks)");
		COLUMN_LABELS.put("SBC_DESC", "Sale Description");
	}

	private static final Pattern TOO_LONG = Pattern.compile("Data too long for column '(\\w+)'");
	private static final Pattern BAD_NUMBER = Pattern
			.compile("(?:Incorrect \\w+ value: '([^']*)'|Data truncated) for column '(\\w+)'");

	public BudgetSheetRowException(String message, Throwable cause) {
		super(message, cause);
	}

	/** Turns a DB/runtime error into a short reason the user can act on. */
	public static String describe(Throwable e) {
		Throwable root = e;
		while (root.getCause() != null && root.getCause() != root) {
			root = root.getCause();
		}
		String msg = root.getMessage() != null ? root.getMessage() : "";
		Matcher m = TOO_LONG.matcher(msg);
		if (m.find()) {
			return label(m.group(1)) + " is too long";
		}
		m = BAD_NUMBER.matcher(msg);
		if (m.find()) {
			String val = m.group(1) != null ? " '" + m.group(1) + "'" : "";
			return label(m.group(2)) + val + " is not a valid number";
		}
		if (root instanceof StringIndexOutOfBoundsException) {
			return "Sale Description is empty";
		}
		return "could not be saved (" + (msg.length() > 150 ? msg.substring(0, 150) + "..." : msg) + ")";
	}

	private static String label(String column) {
		String l = COLUMN_LABELS.get(column.toUpperCase());
		return l != null ? l : column;
	}
}
