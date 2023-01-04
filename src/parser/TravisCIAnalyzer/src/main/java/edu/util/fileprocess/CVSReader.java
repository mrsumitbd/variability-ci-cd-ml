package edu.util.fileprocess;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.travisdiff.TravisCommitInfo;
import com.unity.entity.CommandType;
import com.unity.entity.EvaluationProject;

public class CVSReader {
	public Map<String, CommandType> loadCmdType(String filepath) throws IOException {
		Map<String, CommandType> cmdmap = new HashMap<>();
		try (Reader reader = Files.newBufferedReader(Paths.get(filepath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing values by Header names
				String cmd = csvRecord.get("cmdName");
				String category = csvRecord.get("categoryName");
				String subcategory = csvRecord.get("subCategory");

				CommandType cmdtype = new CommandType(cmd, category, subcategory);

				if (!cmdmap.containsKey(cmd)) {
					cmdmap.put(cmd, cmdtype);
				}

			}
		}

		return cmdmap;
	}

	public List<EvaluationProject> loadEvaluationProjects(String filepath) throws IOException {
		List<EvaluationProject> evalprojects = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(Paths.get(filepath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing values by Header names
				String repo = csvRecord.get("Repository");
				String type = csvRecord.get("ProjectType");
				boolean build = Boolean.parseBoolean(csvRecord.get("Build"));
				boolean test = Boolean.parseBoolean(csvRecord.get("Test"));
				boolean deploy = Boolean.parseBoolean(csvRecord.get("Deploy"));
				boolean codeanalysis = Boolean.parseBoolean(csvRecord.get("CodeAnalysis"));

				EvaluationProject evalproj = new EvaluationProject();

				evalproj.setProjName(repo);
				evalproj.setProjType(type);
				evalproj.setIsbuild(build);
				evalproj.setTest(test);
				evalproj.setDeploy(deploy);
				evalproj.setCodeAnalysis(codeanalysis);

				evalprojects.add(evalproj);

			}
		}

		return evalprojects;
	}

	public List<TravisCommitInfo> loadTravisCommitInfo(String filepath) throws IOException {
		List<TravisCommitInfo> evalprojects = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(Paths.get(filepath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing values by Header names
				String repo = csvRecord.get("repoUrl");
				String failCommitid = csvRecord.get("failCommit");
				String passCommitid = csvRecord.get("passCommit");
				String mainProblem = csvRecord.get("mainProblem");
				String allProblems =csvRecord.get("allProblems");
				String problemBuildState=csvRecord.get("problemBuildState");

				TravisCommitInfo commitinfo = new TravisCommitInfo(repo, failCommitid, passCommitid);
				commitinfo.setMainProblem(mainProblem);
				commitinfo.setAllProblems(allProblems);
				commitinfo.setProblemBuildState(problemBuildState);

				evalprojects.add(commitinfo);

			}
		}

		return evalprojects;
	}
	
	public Map<String, String> loadBlockType(String filepath) throws IOException {
		Map<String, String> blocktype = new HashMap<>();
		try (Reader reader = Files.newBufferedReader(Paths.get(filepath));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing values by Header names
				String block = csvRecord.get("Block");
				String type = csvRecord.get("Type");
				
				blocktype.put(block, type);

			}
		}

		return blocktype;
	}

}
