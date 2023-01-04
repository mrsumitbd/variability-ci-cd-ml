package com.travis.parser;

import java.util.Map;

public class ProjectCommand {
	private String projName;	
	private String cmdName;
	private String baseCmd;	
	private Map<String,String> envs;
	private boolean matched;
	
	public ProjectCommand(String projname, String cmdname,Map<String,String> envmap)
	{
		this.projName=projname;
		this.cmdName=cmdname;
		this.envs=envmap;
		this.matched=false;
	}	

	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	
	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}
	public String getBaseCmd() {
		return baseCmd;
	}

	public void setBaseCmd(String baseCmd) {
		this.baseCmd = baseCmd;
	}
	
	public Map<String, String> getEnvs() {
		return envs;
	}

	public void setEnvs(Map<String, String> envs) {
		this.envs = envs;
	}

}
