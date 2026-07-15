package com.vmfg.export.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class ResponseAsList {
	String responseCode;
	String responseMessage;
	List<?> responseData;
	String fileName;
	
	List<?> responseData2;
	String fileName2;
}
