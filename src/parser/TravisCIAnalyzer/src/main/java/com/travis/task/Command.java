package com.travis.task;

public class Command {
	private String cmdText;	
	private String baseCmd;
	
	public Command(String cmdtext,String basecmd)
	{
		this.cmdText=cmdtext;
		this.baseCmd=basecmd;		
	}
	
	public String getCmdText() {
		return cmdText;
	}
	public void setCmdText(String cmdText) {
		this.cmdText = cmdText;
	}
	public String getBaseCmd() {
		return baseCmd;
	}
	public void setBaseCmd(String baseCmd) {
		this.baseCmd = baseCmd;
	}
	
}
