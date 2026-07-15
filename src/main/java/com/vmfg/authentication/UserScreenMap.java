package com.vmfg.authentication;

import java.io.Serializable;

public class UserScreenMap implements Serializable{
	
		private static final long serialVersionUID = 1L;
		private int uiScreenMstId;
		private String screenDescription;
		private String moduleDescription;
		private String screenDisplayName;
		private String tenanId;
		
		public int getUiScreenMstId() {
			return uiScreenMstId;
		}
		public void setUiScreenMstId(int uiScreenMstId) {
			this.uiScreenMstId = uiScreenMstId;
		}
		public String getScreenDescription() {
			return screenDescription;
		}
		public void setScreenDescription(String screenDescription) {
			this.screenDescription = screenDescription;
		}
		public String getModuleDescription() {
			return moduleDescription;
		}
		public void setModuleDescription(String moduleDescription) {
			this.moduleDescription = moduleDescription;
		}
		public String getScreenDisplayName() {
			return screenDisplayName;
		}
		public void setScreenDisplayName(String screenDisplayName) {
			this.screenDisplayName = screenDisplayName;
		}
		public String getTenanId() {
			return tenanId;
		}
		public void setTenanId(String tenanId) {
			this.tenanId = tenanId;
		}

}
