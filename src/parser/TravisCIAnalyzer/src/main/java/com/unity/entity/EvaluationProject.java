package com.unity.entity;

public class EvaluationProject {
	String projName;	
	String projType;
	boolean isbuild;
	boolean isTest;
	boolean isDeploy;
	boolean isCodeAnalysis;
	
	public EvaluationProject()
	{
		
	}
	
	
	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getProjType() {
		return projType;
	}

	public void setProjType(String projType) {
		this.projType = projType;
	}

	public boolean isIsbuild() {
		return isbuild;
	}

	public void setIsbuild(boolean isbuild) {
		this.isbuild = isbuild;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public boolean isDeploy() {
		return isDeploy;
	}

	public void setDeploy(boolean isDeploy) {
		this.isDeploy = isDeploy;
	}

	public boolean isCodeAnalysis() {
		return isCodeAnalysis;
	}

	public void setCodeAnalysis(boolean isCodeAnalysis) {
		this.isCodeAnalysis = isCodeAnalysis;
	}



}
