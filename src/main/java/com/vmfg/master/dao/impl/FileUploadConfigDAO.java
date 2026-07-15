package com.vmfg.master.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.IFileUploadConfigDAO;
import com.vmfg.master.entity.DocTypeMstEntity;
import com.vmfg.master.entity.FileUploadConfigEntity;
import com.vmfg.master.rowmapper.FileUploadConfigRowMapper;
import com.vmfg.master.rowmapper.TypeMstRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class FileUploadConfigDAO implements IFileUploadConfigDAO{
	private static final Logger logger = LoggerFactory.getLogger(FileUploadConfigDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<DocTypeMstEntity> docTypeMstDropDwn(String tenantId) {
		List<DocTypeMstEntity> list= null;
		try {
			String query="SELECT \r\n" + 
					"    DOCUMENT_TYPE_CODE, DOCUMENT_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    document_type_mst\r\n" + 
					"WHERE\r\n" + 
					"    TENANT_ID = ?;";
			RowMapper<DocTypeMstEntity> rowmapper = new TypeMstRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, tenantId);	 
		} catch (Exception ex) {
			logger.error("docTypeMstDropDwn  method exception-->" + ex);
		}
		logger.debug("docTypeMstDropDwn  method end");
		return list;
	}

	@Override
	public List<FileUploadConfigEntity> getFileUploadConfig(String docCode, String tenantId) {
		List<FileUploadConfigEntity> list= null;
		try {
			String query=" SELECT \r\n" + 
					"    FU_CODE, DOCUMENT_TYPE_CODE, DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    file_upload_config\r\n" + 
					"WHERE\r\n" + 
					"    DOCUMENT_TYPE_CODE = ? AND TENANT_ID = ?;";
			RowMapper<FileUploadConfigEntity> rowmapper = new FileUploadConfigRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper,docCode, tenantId);	 
		} catch (Exception ex) {
			logger.error("getFileUploadConfig  method exception-->" + ex);
		}
		logger.debug("getFileUploadConfig  method end");
		return list;
	}

	@Override
	public int insertFileUploadConfig(String desc, String tenantId, String descCode) {
		int resp = 0;
		String fCode =""; 
		try {
			String mstCodeQry = "select FU_CODE from file_upload_config order by FU_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(mstCodeQry);
			String mstCodeval = resultMap.get("FU_CODE").toString();
			String fuCode = CommonMethod.getUomNewCode("FC",mstCodeval, "FC001");
			
			String split = fuCode.substring(2,fuCode.length());
			if((Integer.parseInt(split)+"").length() <=2) {
				fCode = fuCode.substring(0,3) + String.valueOf(Integer.parseInt(fuCode.substring(2, fuCode.length())));
			}else {
				fCode = fuCode.substring(0,2) + String.valueOf(Integer.parseInt(fuCode.substring(2, fuCode.length())));
			}

			String insFile = " INSERT INTO file_upload_config (FU_CODE,DOCUMENT_TYPE_CODE,DESCRIPTION,TENANT_ID) VALUES(?,?,?,?);";
			int insert = this.jdbcTemplate.update(insFile, fCode, descCode, desc, tenantId );
			if (insert == 1) {
				resp = 1;
			}				
		}catch(Exception e) {
			logger.error("insertFileUploadConfig  method end" +e);
		}	
		return resp;
	}

	@Override
	public int updateFileUploadConfig(String fuCode, String desc, String tenantId) {
		int resp = 0;
		try {
			String updateFile = "update file_upload_config set DESCRIPTION =? where FU_CODE =? and TENANT_ID = ?";
			int update = this.jdbcTemplate.update(updateFile, desc, fuCode, tenantId);
			if (update == 1) {
				resp = 1;
			}
		}catch(Exception e) {
		    logger.debug("updateFileUploadConfig  method end");		
		}
		return resp;
	}
	
}