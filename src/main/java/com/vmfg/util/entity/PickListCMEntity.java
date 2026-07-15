package com.vmfg.util.entity;

import java.io.Serializable;

public class PickListCMEntity  implements Serializable{
	private static final long serialVersionUID = 1L;
		private String productcode;
		private String workid;
		private String woqty;
		private String fromlcn;
		private String trasnferqty;
		private String tolcn;
		private String Statuscode;
		private String iswoapplicable;
		private String availabqty;
		private String wopenqty;
		private String tenantid;
		
		public String getWorkid() {
			return workid;
		}
		public void setWorkid(String workid) {
			this.workid = workid;
		}
		public String getWoqty() {
			return woqty;
		}
		public void setWoqty(String woqty) {
			this.woqty = woqty;
		}
		public String getFromlcn() {
			return fromlcn;
		}
		public void setFromlcn(String fromlcn) {
			this.fromlcn = fromlcn;
		}
		public String getTrasnferqty() {
			return trasnferqty;
		}
		public void setTrasnferqty(String trasnferqty) {
			this.trasnferqty = trasnferqty;
		}
		public String getTolcn() {
			return tolcn;
		}
		public void setTolcn(String tolcn) {
			this.tolcn = tolcn;
		}
		public String getStatuscode() {
			return Statuscode;
		}
		public void setStatuscode(String statuscode) {
			Statuscode = statuscode;
		}
		public String getIswoapplicable() {
			return iswoapplicable;
		}
		public void setIswoapplicable(String iswoapplicable) {
			this.iswoapplicable = iswoapplicable;
		}
		public String getAvailabqty() {
			return availabqty;
		}
		public void setAvailabqty(String availabqty) {
			this.availabqty = availabqty;
		}
		public String getWopenqty() {
			return wopenqty;
		}
		public void setWopenqty(String wopenqty) {
			this.wopenqty = wopenqty;
		}
		public String getTenantid() {
			return tenantid;
		}
		public void setTenantid(String tenantid) {
			this.tenantid = tenantid;
		}
		public String getProductcode() {
			return productcode;
		}
		public void setProductcode(String productcode) {
			this.productcode = productcode;
		}
		;
		
		
		
	
}
