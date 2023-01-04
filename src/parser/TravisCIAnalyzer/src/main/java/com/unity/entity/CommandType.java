package com.unity.entity;

import com.opencsv.bean.CsvBindByName;

public class CommandType {
	@CsvBindByName(column = "cmdName", required = true)
	private String cmdName;
	@CsvBindByName(column = "categoryName", required = true)
	private String categoryName;
	@CsvBindByName(column = "subCategory",required = true)
	private String subCategory;

	public CommandType(String cmdname, String category, String subcategory) {
		this.cmdName = cmdname;
		this.categoryName = category;
		this.subCategory = subcategory;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

}
