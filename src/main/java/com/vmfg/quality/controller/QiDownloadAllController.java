package com.vmfg.quality.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmfg.quality.request.QiDownloadAllRequest;
import com.vmfg.quality.services.impl.QiDownloadAllService;

@RestController
@RequestMapping("/")
public class QiDownloadAllController {
	private static final Logger logger = LoggerFactory.getLogger(QiDownloadAllController.class);

	@Autowired
	private QiDownloadAllService qiDownloadAllService;

	// QC -> project -> Inspection Report tab -> "Download All" (binary zip, not the usual JSON response)
	@CrossOrigin(maxAge = 3600)
	@PostMapping("downloadQiDesignZip")
	public void downloadQiDesignZip(@RequestBody QiDownloadAllRequest req, HttpServletResponse response) {
		try {
			if (req.getTenantId() == null || req.getTenantId().trim().isEmpty() || !QiDownloadAllService.hasRows(req)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No rows to download");
				return;
			}
			Map<String, File> files = new LinkedHashMap<>();
			qiDownloadAllService.resolveDesigns(req, files);
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"InspectionReport.zip\"");
			qiDownloadAllService.writeZip(req, files, response.getOutputStream());
			logger.info("QI Download All: project " + req.getPmHdrId() + ", rows " + req.getRows().size()
					+ ", designs " + files.size());
		} catch (Exception ex) {
			logger.error("downloadQiDesignZip exception " + ex);
			if (!response.isCommitted()) {
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download failed");
				} catch (Exception ignore) {
					// response already gone
				}
			}
		}
	}
}
