package com.vmfg.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AuditTrial {
	private static final Logger logger = LoggerFactory.getLogger(AuditTrial.class);

	private static final Map<Integer, String> REQUEST_TYPE = new HashMap<>();
	private static final Map<Integer, String> REQUEST_MSG = new HashMap<>();

	static {
		REQUEST_TYPE.put(1, "Create");
		REQUEST_TYPE.put(2, "Retrieve");
		REQUEST_TYPE.put(3, "Update");
		REQUEST_TYPE.put(4, "Delete");

		REQUEST_MSG.put(1, "Sample");

	}

	public static String getRequestType(int number) {
		return REQUEST_TYPE.getOrDefault(number, "Unknown");
	}

	public static String getRequestMsg(String number) {
		return REQUEST_MSG.getOrDefault(number, "Unknown");
	}

	public static void insertAuditTrial(JdbcTemplate jdbc, String screen_name, int request_type,
			List<String> messageList, String tenant_id) {

		logger.info("Audit Trail invoked");
		//

		try {
			String msg = "";
			if (messageList.size() > 0) {
				msg = getRequestMsg(messageList.get(0));

			}

			String insertQ = "INSERT INTO audit_trail (screen_name, request_type, message, tenant_id) VALUES (?, ?, ?, ?);";
			jdbc.update(insertQ, screen_name, getRequestType(request_type), msg, tenant_id);

		} catch (Exception ex) {
			logger.error(" Exception with Audit Trail invoked " + ex.getMessage());
		}

	}

}
