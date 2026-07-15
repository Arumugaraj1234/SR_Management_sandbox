package com.vmfg.export.services.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.export.dao.interfaces.IProjectReportTrackerDAO;
import com.vmfg.export.entity.DocumentFilePathEntity;
import com.vmfg.export.entity.DocumentManagerFileEntity;
import com.vmfg.export.entity.FileDownloadEntity;
import com.vmfg.export.request.IdAndTenantIdRequest;
import com.vmfg.export.request.ProjectTrackerReportRequest;
import com.vmfg.export.response.ResponseAsList;
import com.vmfg.export.services.interfaces.IProjectTrackerReportService;
import com.vmfg.finance.request.getPraDtlRequest;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.util.CommonBase64Class;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ProjectTrackerReportService implements IProjectTrackerReportService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTrackerReportService.class);
	
	@Autowired
	IProjectReportTrackerDAO iProjectReportTrackerDAO;

	@Override
	public ResponseAsList getProjectTrackerReportPDF(ProjectTrackerReportRequest projReq) {

		ResponseAsList list = new ResponseAsList();
		
		try {
			logger.info("getProjectTrackerReportPDFService Started ");
			FileDownloadEntity fdEntity = new FileDownloadEntity();
			List<FileDownloadEntity> listEnt = new ArrayList<FileDownloadEntity>();
			InputStream inputStream = null;
			String downloadPath = "";
			InputStream resource = null;
			InputStream logoResource = null;
			BufferedImage image = null;
			ImageIcon imageIcon = null;
			Map<String, Object> params = new HashMap<String, Object>();
			JasperReport report = null;
			Connection connection = null;
			File downloadFoler = null;

			downloadPath = iProjectReportTrackerDAO.getPropValueByTenant(projReq.getTenantId(),
					"DOWNLOAD_PATH_FOR_PROJECT_TRACKER");
			String orgName = iProjectReportTrackerDAO.getOrganizationInfo(projReq.getTenantId());

			downloadFoler = new File(downloadPath);
			if (!downloadFoler.exists()) {
				downloadFoler.mkdirs();
			}

			String PROJECT_CODE = iProjectReportTrackerDAO.getProjectCode(projReq.getTenantId(),
					projReq.getProjectId());
			logger.info(PROJECT_CODE.replace('/', '_'));

			downloadPath = downloadPath + File.separator + orgName + "_" + PROJECT_CODE.replace('/', '_') + "_"
					+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf";
			
			String mainReportPath = iProjectReportTrackerDAO.getProjectTrackerPath(projReq.getTenantId(),
					projReq.getKey(), "1");
			String subReportPaths = iProjectReportTrackerDAO.getProjectTrackerPath(projReq.getTenantId(),
					projReq.getKey(), "0");
			String subReportPath=subReportPaths.split(",")[0];
			String subReportTracker=subReportPaths.split(",")[1];
			String subReportTimeSheet=subReportPaths.split(",")[2];
			// String[] reportPaths = reportPath.split(",");

			File reportFile = new File(mainReportPath);
			if (reportFile.exists()) {
				resource = new FileInputStream(reportFile);
			}

			inputStream = resource;
			report = JasperCompileManager.compileReport(inputStream);

			
			String logoPath = iProjectReportTrackerDAO.getOrganizationLogoPath(projReq.getTenantId());
			File logoFileFile1 = new File(logoPath);
			if (logoFileFile1.exists()) {
				logoResource = new FileInputStream(logoFileFile1);
			}

			image = ImageIO.read(logoResource);
			imageIcon = new ImageIcon(new ImageIcon(image).getImage());

			params.put("tenantId", projReq.getTenantId());
			params.put("PROJECT_ID", projReq.getProjectId());
			params.put("LOGO", imageIcon.getImage());
			params.put("SUBREPORT",subReportPath);
			params.put("SUBREPORT2",subReportTracker);
			params.put("SUBREPORT3",subReportTimeSheet);
			
			connection = iProjectReportTrackerDAO.getConnection();
			JasperPrint print = null;
			if (connection != null) {
				print = JasperFillManager.fillReport(report, params, connection);
			}

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(downloadPath));
			exporter.exportReport();

			connection.close();
			fdEntity.setFilePath(downloadPath);
			fdEntity.setMessageCode("M0001");
			fdEntity.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity.getFilePath())));
			listEnt.add(fdEntity);

			if (listEnt.size() > 0) {
				list.setResponseData(listEnt);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setFileName(orgName + "_" + PROJECT_CODE.replace('/', '_') + "_"
						+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf");
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getProjectTrackerReportPDFService Exception " + ex);
			list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			list.setResponseMessage(ResponseMessageMap.noRecord);
			ex.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unused")
	@Override
	public ResponseAsList getPoDtlsByPoIdReportPDF(IdAndTenantIdRequest idAndTenantIdReq) {
		
		ResponseAsList list = new ResponseAsList();
		try {
			logger.info("getPoDtlsByPoIdReportPDF Started ");
			FileDownloadEntity fdEntity = new FileDownloadEntity();
			List<FileDownloadEntity> listEnt = new ArrayList<FileDownloadEntity>();
			InputStream inputStream = null;
			String downloadPath = "";
			InputStream resource = null;
			InputStream subReportResource = null;
			InputStream logoResource = null;
			BufferedImage image = null;
			ImageIcon imageIcon = null;
			Map<String, Object> params = new HashMap<String, Object>();
			JasperReport report = null;
			Connection connection = null;
			File downloadFoler = null;
			
			FileDownloadEntity fdEntity1 = new FileDownloadEntity();
			FileDownloadEntity fdEntity2 = new FileDownloadEntity();
			List<FileDownloadEntity> listEnt1 = new ArrayList<FileDownloadEntity>();
			InputStream inputStream1 = null;
			String downloadPath1 = "";
			InputStream resource1 = null;
			InputStream subReportResource1 = null;
			InputStream logoResource1 = null;
			BufferedImage image1 = null;
			ImageIcon imageIcon1 = null;
			Map<String, Object> params1= new HashMap<String, Object>();
			JasperReport report1 = null;
			Connection connection1 = null;
			File downloadFoler1 = null;
			File downloadFoler2 = null;
			String PROJECT_CODE1="";
						
        	   downloadPath = iProjectReportTrackerDAO.getPropValueByTenant(idAndTenantIdReq.getTenantId(),
   					"DOWNLOAD_PATH_FOR_PURCHASE_ORDER");
			String orgName = iProjectReportTrackerDAO.getOrganizationInfo(idAndTenantIdReq.getTenantId());

			downloadFoler = new File(downloadPath);
			if (!downloadFoler.exists()) {
				downloadFoler.mkdirs();
			}

			String PROJECT_CODE = iProjectReportTrackerDAO.getPoCode(idAndTenantIdReq.getTenantId(),
					idAndTenantIdReq.getPoId());
			logger.info(PROJECT_CODE.replace('/', '_'));
			String zipname = downloadPath+ File.separator+"PO_"+PROJECT_CODE.replace('/', '_') + "_"
				  +	iProjectReportTrackerDAO.getCurrentDateTime() + ".zip";
			String fileName = PROJECT_CODE.replace('/', '_') + "_"
			  +	iProjectReportTrackerDAO.getCurrentDateTime() + ".zip";
			downloadPath = downloadPath + File.separator + orgName + "_" + PROJECT_CODE.replace('/', '_') + "_"
					+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf";
			String fileName1 = PROJECT_CODE.replace('/', '_') + "_"+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf";
			String mainReportPath = "";
			String subReportPath = "";
             if(idAndTenantIdReq.getPoType().equalsIgnoreCase("local")) {
			String mainReportPaths = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
					idAndTenantIdReq.getKey(), "1");
			String subReportPaths = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
					idAndTenantIdReq.getKey(), "0");
			String mainReportPathsArr=mainReportPaths.split(",")[0];
			String subReportPathsArr=subReportPaths.split(",")[0];
			 mainReportPath = mainReportPathsArr;
			 subReportPath = subReportPathsArr;
             }else {
            	  mainReportPath = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
     					idAndTenantIdReq.getKey(), "1");
     			 subReportPath = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
     					idAndTenantIdReq.getKey(), "0");
             }
			
			File reportFile = new File(mainReportPath);
			if (reportFile.exists()) {
				resource = new FileInputStream(reportFile);
			}

			inputStream = resource;
			report = JasperCompileManager.compileReport(inputStream);

			File reportFile1 = new File(subReportPath);
			if (reportFile1.exists()) {
				subReportResource = new FileInputStream(reportFile1);
			}

			String logoPath = iProjectReportTrackerDAO.getOrganizationLogoPath(idAndTenantIdReq.getTenantId());
			File logoFileFile1 = new File(logoPath);
			if (logoFileFile1.exists()) {
				logoResource = new FileInputStream(logoFileFile1);
			}

			image = ImageIO.read(logoResource);
			imageIcon = new ImageIcon(new ImageIcon(image).getImage());

			params.put("TENANT_ID", idAndTenantIdReq.getTenantId());
			params.put("PO_ID", idAndTenantIdReq.getPoId());
			params.put("LOGO", imageIcon.getImage());
			params.put("SUBREPORT", subReportPath);

			connection = iProjectReportTrackerDAO.getConnection();
			JasperPrint print = null;
			if (connection != null) {
				print = JasperFillManager.fillReport(report, params, connection);
			}
      	
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(downloadPath));
			exporter.exportReport();

			connection.close();
			fdEntity.setFilePath(downloadPath);
			fdEntity.setMessageCode("M0001");
			fdEntity.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity.getFilePath())));
			listEnt.add(fdEntity);
		
			//local type
//			if(idAndTenantIdReq.getPoType().equalsIgnoreCase("local")) {
//				downloadPath1 = iProjectReportTrackerDAO.getPropValueByTenant(idAndTenantIdReq.getTenantId(),
//						"DOWNLOAD_PATH_FOR_PURCHASE_ORDER");
//				String orgName1 = iProjectReportTrackerDAO.getOrganizationInfo(idAndTenantIdReq.getTenantId());
//
//				downloadFoler1 = new File(downloadPath1);
//				if (!downloadFoler1.exists()) {
//					downloadFoler1.mkdirs();
//				}
//	
//				PROJECT_CODE1 = iProjectReportTrackerDAO.getPoCode(idAndTenantIdReq.getTenantId(),
//						idAndTenantIdReq.getPoId());
//				logger.info(PROJECT_CODE1.replace('/', '_'));
//
//				downloadPath1 = downloadPath1 + File.separator + orgName1 + "_" + PROJECT_CODE1.replace('/', '_') + "_"
//						+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf";
//			
//				String mainReportPaths1 = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
//						idAndTenantIdReq.getKey(), "1");
//				String subReportPaths1 = iProjectReportTrackerDAO.getProjectTrackerPath(idAndTenantIdReq.getTenantId(),
//						idAndTenantIdReq.getKey(), "0");
//				String mainReportPathsArr1=mainReportPaths1.split(",")[1];
//				String subReportPathsArr1=subReportPaths1.split(",")[1];
//				String mainReportPath1 = mainReportPathsArr1;
//				String subReportPath1 = subReportPathsArr1;
//
//				File reportFile2 = new File(mainReportPath1);
//				if (reportFile2.exists()) {
//					resource1 = new FileInputStream(reportFile2);
//				}
//				inputStream1 = resource1;
//				report1 = JasperCompileManager.compileReport(inputStream1);
//
//				File reportFile3 = new File(subReportPath1);
//				if (reportFile3.exists()) {
//					subReportResource1 = new FileInputStream(reportFile3);
//				}
//
//				String logoPath1 = iProjectReportTrackerDAO.getOrganizationLogoPath(idAndTenantIdReq.getTenantId());
//				File logoFileFile2 = new File(logoPath1);
//				if (logoFileFile2.exists()) {
//					logoResource1 = new FileInputStream(logoFileFile2);
//				}
//
//				image1 = ImageIO.read(logoResource1);
//				imageIcon1 = new ImageIcon(new ImageIcon(image1).getImage());
//
//				params1.put("TENANT_ID", idAndTenantIdReq.getTenantId());
//				params1.put("PO_ID", idAndTenantIdReq.getPoId());
//				params1.put("LOGO", imageIcon1.getImage());
//				params1.put("SUBREPORT1", subReportPath1);
//				
//
//				connection1 = iProjectReportTrackerDAO.getConnection();
//				JasperPrint print1 = null;
//				if (connection1 != null) {
//					print1 = JasperFillManager.fillReport(report1, params1, connection1);
//				}
//				
//				JRPdfExporter exporter1 = new JRPdfExporter();
//				exporter1.setExporterInput(new SimpleExporterInput(print1));
//				exporter1.setExporterOutput(new SimpleOutputStreamExporterOutput(downloadPath1));
//				exporter1.exportReport();
//				
//				connection1.close();
//			}
			if (!idAndTenantIdReq.getPoType().equalsIgnoreCase("local") && listEnt.size() > 0) {
				list.setResponseData(listEnt);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			    list.setFileName(fileName1);
			}
				
				if(idAndTenantIdReq.getPoType().equalsIgnoreCase("local")) {
					
                   List<DocumentFilePathEntity> IndentDtlId = iProjectReportTrackerDAO.getFileDocumentPath(idAndTenantIdReq.getTenantId(),
							idAndTenantIdReq.getPoId());
                   DocumentManagerFileEntity PDFfiles = new DocumentManagerFileEntity();
                   String[] srcFiles= {};
                   List<String> fileList = new ArrayList<>();
                   if(IndentDtlId.size() > 0) {
                   for(int i=0; i<IndentDtlId.size(); i++) {
                	   PDFfiles = iProjectReportTrackerDAO.documentDownloadDocFile(idAndTenantIdReq.getTenantId(), IndentDtlId.get(i).getIndentDtlId());
                	   if(PDFfiles.getFilePath()!=null && new File(PDFfiles.getFilePath()).exists()) {
                	   fileList.add(PDFfiles.getFilePath());  
                	   }
                   }
                   }
                   fileList.add(downloadPath);
//                   fileList.add(downloadPath1);
                   srcFiles = fileList.toArray(new String[0]);
		           ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipname));
		            for (String srcFile : srcFiles) {  
		            File fileToZip = new File(srcFile);
	                FileInputStream fis = new FileInputStream(fileToZip);
	                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
	                out.putNextEntry(zipEntry);

	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = fis.read(buffer)) >= 0) {
	                    out.write(buffer, 0, length);
	                }
	                fis.close();
	                out.closeEntry();
	                
	            }
		            out.close();
          
	            fdEntity1.setFilePath(zipname);
				fdEntity1.setMessageCode("M0001");
				fdEntity1.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity1.getFilePath())));
				listEnt1.add(fdEntity1);
				
				if(listEnt1.size() > 0) {
					list.setResponseData(listEnt1);
					list.setResponseCode(ResponseMessageMap.responseCodeOk);
					list.setResponseMessage(ResponseMessageMap.success);
					list.setFileName(fileName);
					
				}else {
					list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					list.setResponseMessage(ResponseMessageMap.noRecord);
				}
				
				 String[] downloadPaths = {downloadPath,downloadPath1,zipname};
				 for (String path : downloadPaths) {
			            File file = new File(path);
				        file.delete();
				 }
				}	
			 
		} catch (Exception ex) {
			logger.error("getPoDtlsByPoIdReportPDF Exception " + ex);
			list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			list.setResponseMessage(ResponseMessageMap.noRecord);
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public ResponseAsList getPraReportByPraId(getPraDtlRequest praIdAndTenantIdReq) {
ResponseAsList list = new ResponseAsList();
		
		try {
			logger.info("getPraReportByPraIdService Started ");
			FileDownloadEntity fdEntity = new FileDownloadEntity();
			List<FileDownloadEntity> listEnt = new ArrayList<FileDownloadEntity>();
			InputStream inputStream = null;
			String downloadPath = "";
			InputStream resource = null;
			InputStream logoResource = null;
			ImageIcon imageIcon = null;
			BufferedImage image = null;
			Map<String, Object> params = new HashMap<String, Object>();
			JasperReport report = null;
			Connection connection = null;
			File downloadFoler = null;
			
			 downloadPath = iProjectReportTrackerDAO.getPropValueByTenant(praIdAndTenantIdReq.getTenantId(),
	   					"DOWNLOAD_PATH_FOR_PRA_REPORT");
				String orgName = iProjectReportTrackerDAO.getOrganizationInfo(praIdAndTenantIdReq.getTenantId());
				
				downloadFoler = new File(downloadPath);
				if (!downloadFoler.exists()) {
					downloadFoler.mkdirs();
				}
				
				String Pra_Code = iProjectReportTrackerDAO.getPraCode(praIdAndTenantIdReq.getTenantId(),
						praIdAndTenantIdReq.getPraId());
				logger.info(Pra_Code.replace('/', '_'));

				downloadPath = downloadPath + File.separator + orgName + "_" + Pra_Code.replace('/', '_') + "_"
						+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf";
				
				String mainReportPath = iProjectReportTrackerDAO.getProjectTrackerPath(praIdAndTenantIdReq.getTenantId(),
						"PRAReport", "1");
						
			
				File reportFile = new File(mainReportPath);
				if (reportFile.exists()) {
					resource = new FileInputStream(reportFile);
				}

				inputStream = resource;
				report = JasperCompileManager.compileReport(inputStream);

				
				String logoPath = iProjectReportTrackerDAO.getOrganizationLogoPath(praIdAndTenantIdReq.getTenantId());
				File logoFileFile1 = new File(logoPath);
				if (logoFileFile1.exists()) {
					logoResource = new FileInputStream(logoFileFile1);
				}
				
				image = ImageIO.read(logoResource);
				imageIcon = new ImageIcon(new ImageIcon(image).getImage());
				params.put("TENANT_ID", praIdAndTenantIdReq.getTenantId());
				params.put("PRA_ID", praIdAndTenantIdReq.getPraId());
				params.put("LOGO", imageIcon.getImage());
				
				connection = iProjectReportTrackerDAO.getConnection();
				JasperPrint print = null;
				if (connection != null) {
					print = JasperFillManager.fillReport(report, params, connection);
				}

				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(downloadPath));
				exporter.exportReport();

				connection.close();
				fdEntity.setFilePath(downloadPath);
				fdEntity.setMessageCode("M0001");
				fdEntity.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity.getFilePath())));
				listEnt.add(fdEntity);

				if (listEnt.size() > 0) {
					list.setResponseData(listEnt);
					list.setResponseCode(ResponseMessageMap.responseCodeOk);
					list.setResponseMessage(ResponseMessageMap.success);
					list.setFileName(orgName + "_" + Pra_Code.replace('/', '_') + "_"
							+ iProjectReportTrackerDAO.getCurrentDateTime() + ".pdf");
				} else {
					list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					list.setResponseMessage(ResponseMessageMap.noRecord);
				}
			} catch (Exception ex) {
				logger.error("getPraReportByPraIdService Exception " + ex);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				ex.printStackTrace();
			}
		
	    return list;
	}
}
