package com.travis.task;

import java.util.ArrayList;
import java.util.List;

import com.travis.parser.ProjectCommand;

public class ProjectCommands {
	private String projName;	
	private List<ProjectCommand> projCmds;	
	
	public ProjectCommands(String projname)
	{
		this.projName=projname;
		projCmds=new ArrayList<ProjectCommand>();
	}
	
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
		
	public List<ProjectCommand> getProjCmds() {
		return projCmds;
	}
	
	public void addProjCmd(ProjectCommand projcmd) {
		projCmds.add(projcmd);
	}
	
	public void addProjCmds(List<ProjectCommand> projcmds) {
		projCmds.addAll(projcmds);
	}
	
	
}
