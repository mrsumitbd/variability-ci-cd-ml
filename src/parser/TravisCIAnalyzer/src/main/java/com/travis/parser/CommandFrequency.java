package com.travis.parser;

import com.opencsv.bean.CsvBindByName;

public class CommandFrequency {
	
	@CsvBindByName(column = "cmdName", required = true)
	public String cmdName;	
	
	@CsvBindByName(column = "freCount", required = true)
	public int freCount;
	
	
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public int getFreCount() {
		return freCount;
	}
	public void setFreCount(int freCount) {
		this.freCount = freCount;
	}

}
