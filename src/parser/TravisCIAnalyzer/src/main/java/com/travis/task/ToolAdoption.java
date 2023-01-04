package com.travis.task;

public class ToolAdoption {
	private String projName;
	private boolean build;	
	private int buildCount;	
	private boolean test;
	private int testCount;
	private boolean analysis;
	private int analysisCount;
	private boolean deployment;
	private int deploymentCount;
	private boolean ci;
	private int ciCount;
	
	public ToolAdoption()
	{
		this.build=false;
		this.test=false;
		this.analysis=false;
		this.deployment=false;
		this.ci=false;				
	}
	
	public ToolAdoption(String projname)
	{
		this.projName=projname;
		this.build=false;
		this.test=false;
		this.analysis=false;
		this.deployment=false;
		this.ci=false;				
	}
	
	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}
	
	public boolean isBuild() {
		return build;
	}
	public void setBuild(boolean build) {
		this.build = build;
	}
	public boolean isTest() {
		return test;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	public boolean isAnalysis() {
		return analysis;
	}
	public void setAnalysis(boolean analysis) {
		this.analysis = analysis;
	}
	public boolean isDeployment() {
		return deployment;
	}
	public void setDeployment(boolean deployment) {
		this.deployment = deployment;
	}
	public boolean isCi() {
		return ci;
	}
	public void setCi(boolean ci) {
		this.ci = ci;
	}
	
	public int getBuildCount() {
		return buildCount;
	}

	public void setBuildCount(int buildCount) {
		this.buildCount = buildCount;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public int getAnalysisCount() {
		return analysisCount;
	}

	public void setAnalysisCount(int analysisCount) {
		this.analysisCount = analysisCount;
	}

	public int getDeploymentCount() {
		return deploymentCount;
	}

	public void setDeploymentCount(int deploymentCount) {		
		this.deploymentCount = deploymentCount;
		if(deploymentCount>0)
		{
			this.deployment=true;
		}
	}

	public int getCiCount() {
		return ciCount;
	}

	public void setCiCount(int ciCount) {
		this.ciCount = ciCount;
	}
}
