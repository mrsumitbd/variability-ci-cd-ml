package com.travis.task;

public class DeploymentTask {
	private String projName;
	private boolean deploymentStatus;

	public DeploymentTask(String projname, boolean status) {
		this.projName = projname;
		this.deploymentStatus = status;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public boolean getDeploymentStatus() {
		return deploymentStatus;
	}

	public void setDeploymentStatus(boolean deploymentStatus) {
		this.deploymentStatus = deploymentStatus;
	}

}
