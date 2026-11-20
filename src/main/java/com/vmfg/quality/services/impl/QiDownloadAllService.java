package com.vmfg.quality.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.vmfg.export.dao.interfaces.IProjectReportTrackerDAO;
import com.vmfg.export.entity.DocumentManagerFileEntity;
import com.vmfg.quality.request.QiDownloadAllRequest;

/**
 * QC Inspection Report "Download All": streams ONE zip = InspectionReport.csv (sent by the screen, same
 * format as its "Export to CSV", filtered rows) and Designs/ with the LATEST design document (FC015) of each row's
 * indent line - the same document the row's own "Drawing Detail" download gives (getDmIdByLatestVerion).
 * Identical drawings on several lines of a part (same name without upload timestamp + same size) go in once. Design lookup is the same one the PO design zip uses (documentDownloadDocFile).
 * Streamed straight to the response (no base64/JSON) so a large project doesn't load every file in memory.
 */
@Service
public class QiDownloadAllService {
	private static final Logger logger = LoggerFactory.getLogger(QiDownloadAllService.class);

	@Autowired
	private IProjectReportTrackerDAO iProjectReportTrackerDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** A row's design = its own indent line's latest design (same as the row action), so rows key by indent line. */
	public static String lineKey(QiDownloadAllRequest.Row row) {
		return row.getIndentDtlId() == null ? "" : row.getIndentDtlId().trim();
	}

	/**
	 * indent line -> zip entry name of its design (null when no design file is available). Follows the
	 * row-wise "Drawing Detail" download: each indent line gets its own latest FC015 upload (VERSION desc,
	 * via documentDownloadDocFile = same rule as getDmIdByLatestVerion), not merged by part number - one
	 * part number can carry different documents on different lines/stations. Exact duplicates (same part,
	 * same name without the upload timestamp, same byte size) share ONE zip entry with the clean name.
	 */
	public Map<String, String> resolveDesigns(QiDownloadAllRequest req, Map<String, File> filesOut) {
		Map<String, String> names = new LinkedHashMap<>();
		// indent lines that have a design AND an inspection request in this project/tenant
		Set<String> linesWithDesign = new HashSet<>(jdbcTemplate.queryForList(
				"SELECT DISTINCT dm.REFERENCE_ID FROM document_management dm "
						+ "WHERE dm.UPLOAD_DOC_TYPE = 'FC015' AND dm.TENANT_ID = ? AND dm.REFERENCE_ID IN "
						+ "(SELECT CAST(q.INDENT_DTL_ID AS CHAR) FROM quality_inspection_request q "
						+ "WHERE q.PM_HDR_ID = ? AND q.TENANT_ID = ? AND q.IS_LATEST = 1)",
				String.class, req.getTenantId(), req.getPmHdrId(), req.getTenantId()));
		Set<String> usedNames = new HashSet<>();
		Map<String, String> entryByContent = new HashMap<>(); // part|clean name|size -> zip entry
		for (QiDownloadAllRequest.Row row : req.getRows()) {
			String id = lineKey(row);
			if (names.containsKey(id)) {
				continue; // several rows of the same indent line -> design added once
			}
			String entry = null;
			if (linesWithDesign.contains(id)) {
				DocumentManagerFileEntity doc = iProjectReportTrackerDAO.documentDownloadDocFile(req.getTenantId(), id);
				File f = doc.getFilePath() == null ? null : new File(doc.getFilePath());
				if (f != null && f.isFile()) {
					// stored names are "<yyyyMMddHHmmss><original name>"; the zip uses the original name
					String name = f.getName().replaceFirst("^\\d{14}(?=.)", "").replaceAll("[\\\\/:*?\"<>|]", "_");
					String part = row.getProductCode() == null ? "" : row.getProductCode().trim().toUpperCase();
					String contentKey = part + "|" + name.toLowerCase() + "|" + f.length();
					entry = entryByContent.get(contentKey);
					if (entry == null) {
						if (usedNames.contains(name.toLowerCase())) {
							int dot = name.lastIndexOf('.');
							name = dot > 0 ? name.substring(0, dot) + "_" + id + name.substring(dot) : name + "_" + id;
						}
						usedNames.add(name.toLowerCase());
						entry = "Designs/" + name;
						entryByContent.put(contentKey, entry);
						filesOut.put(entry, f);
					}
				} else {
					logger.warn("QI Download All: no design file for indentDtlId=" + id + ", path=" + doc.getFilePath());
				}
			}
			names.put(id, entry);
		}
		return names;
	}

	public void writeZip(QiDownloadAllRequest req, Map<String, File> files, OutputStream os) throws IOException {
		ZipOutputStream zip = new ZipOutputStream(os);
		// report CSV is built by the screen exactly like its "Export to CSV" - written unchanged
		zip.putNextEntry(new ZipEntry("InspectionReport.csv"));
		zip.write((req.getCsv() == null ? "" : req.getCsv()).getBytes(StandardCharsets.UTF_8));
		zip.closeEntry();

		byte[] buf = new byte[64 * 1024];
		for (Map.Entry<String, File> e : files.entrySet()) {
			try (InputStream in = new FileInputStream(e.getValue())) {
				zip.putNextEntry(new ZipEntry(e.getKey()));
				int n;
				while ((n = in.read(buf)) > 0) {
					zip.write(buf, 0, n);
				}
				zip.closeEntry();
			} catch (IOException ex) {
				logger.error("QI Download All: failed to add " + e.getValue() + " -> " + ex);
			}
		}
		zip.finish();
		zip.flush();
	}

	public static boolean hasRows(QiDownloadAllRequest req) {
		List<QiDownloadAllRequest.Row> rows = req == null ? null : req.getRows();
		return rows != null && !rows.isEmpty();
	}
}
