package com.unity.repodownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.build.commitanalyzer.CommitAnalyzer;
import com.config.Config;
import com.unity.entity.PerfFixData;
import com.utility.ProjectPropertyAnalyzer;

import edu.util.fileprocess.TextFileReaderWriter;

public class ProjectLoader {

	public ProjectLoader() {

	}

	public void LoadDownloadProjects() {
		String filepath = Config.gitProjList;

		List<String> projlist = TextFileReaderWriter.GetFileContentByLine(filepath);

		//RepoDownloadManager repomngr = new RepoDownloadManager();
		//repomngr.downloadProjects(projlist);
		
		
		
		SimpleDateFormat ft = new SimpleDateFormat ("MM-dd-yyyy"); 
		Date t=null;
	    try {
			t = ft.parse(Config.repoStrDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (String proj : projlist) {
			String projname = ProjectPropertyAnalyzer.getProjName(proj);

			
			CommitAnalyzer cmtanalyzer = null;

			try {
				cmtanalyzer = new CommitAnalyzer("test", projname, proj);
				cmtanalyzer.checkoutRepoBeforeDate(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(projname);
			
		}

	}

}
