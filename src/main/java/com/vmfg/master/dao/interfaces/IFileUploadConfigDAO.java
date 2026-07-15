package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.DocTypeMstEntity;
import com.vmfg.master.entity.FileUploadConfigEntity;

public interface IFileUploadConfigDAO {

	List<DocTypeMstEntity> docTypeMstDropDwn(String tenantId);

	List<FileUploadConfigEntity> getFileUploadConfig(String docCode, String tenantId);

	int insertFileUploadConfig(String fuCode, String desc, String descCode);

	int updateFileUploadConfig(String fuCode, String desc, String tenantId);
	
}