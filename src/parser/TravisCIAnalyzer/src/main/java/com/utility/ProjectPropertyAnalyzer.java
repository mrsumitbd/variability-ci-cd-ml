package com.utility;

public class ProjectPropertyAnalyzer {
	
	public static String getProjName(String gitrepourl)
	{
		int lastslashindex=gitrepourl.lastIndexOf('/');
		int indexofdot=gitrepourl.lastIndexOf('.');
		
		String otherpart=gitrepourl.substring(0,lastslashindex-1);
		
		int secondlastindex=otherpart.lastIndexOf('/');
		
		String repoowner=gitrepourl.substring(secondlastindex+1,lastslashindex);		
		String projname=gitrepourl.substring(lastslashindex+1, indexofdot);
		
		String repoprojname=repoowner+"@"+projname;
		
		return repoprojname;		
	}
	
	public static String getProjRepoName(String gitrepourl)
	{
		int lastslashindex=gitrepourl.lastIndexOf('/');
		int indexofdot=gitrepourl.lastIndexOf('.');
		
		String otherpart=gitrepourl.substring(0,lastslashindex-1);
		
		int secondlastindex=otherpart.lastIndexOf('/');
		
		String repoowner=gitrepourl.substring(secondlastindex+1,lastslashindex);		
		String projname=gitrepourl.substring(lastslashindex+1, indexofdot);
		
		String repoprojname=repoowner+"/"+projname;
		
		return repoprojname;		
	}

}
