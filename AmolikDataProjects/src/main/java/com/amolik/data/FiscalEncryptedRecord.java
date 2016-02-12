package com.amolik.data;

public class FiscalEncryptedRecord {

	private String imagePath;
	private String insertTime;
	private String updateTime;
	private FiscalRecord fiscal;

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public FiscalRecord getFiscal() {
		return fiscal;
	}
	public void setFiscal(FiscalRecord fiscal) {
		this.fiscal = fiscal;
	}


}
