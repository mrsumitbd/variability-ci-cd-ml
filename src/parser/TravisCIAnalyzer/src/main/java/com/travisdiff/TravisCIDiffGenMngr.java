package com.travisdiff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import com.build.analyzer.entity.CSharpChange;
import com.build.commitanalyzer.CommitAnalyzer;
import com.config.Config;
import com.csharp.diff.CSharpDiffGenMngr;
import com.csharp.diff.CSharpDiffGenerator;
import com.csharp.patch.xml.PatchXMLGenerator;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Addition;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.TreeAddition;
import com.github.gumtreediff.actions.model.TreeDelete;
import com.github.gumtreediff.actions.model.TreeInsert;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;
import com.travis.parser.BashCmdAnalysis;
import com.unity.entity.PerfFixData;
import com.utility.ProjectPropertyAnalyzer;

import edu.util.fileprocess.CSVReaderWriter;
import edu.util.fileprocess.CVSReader;
import edu.util.fileprocess.TextFileReaderWriter;

import java.util.Comparator;

public class TravisCIDiffGenMngr {
	/*
	 * Load CSV File for build status change transition data
	 */
	public void generateTravisCIChangeData() {
		// CSVReaderWriter csvrw = new CSVReaderWriter();
		CVSReader csvreader = new CVSReader();
		try {
			List<TravisCommitInfo> cmtlist = csvreader.loadTravisCommitInfo(Config.csvCITransitionFile);
			TravisCIChangeBlocks changeblocks = travicCIDiffGenerate(cmtlist);
			generateStatOnChangeBlock(changeblocks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateTravisCIFailFixChangeData() {
		// CSVReaderWriter csvrw = new CSVReaderWriter();
		CVSReader csvreader = new CVSReader();
		try {
			List<TravisCommitInfo> cmtlist = csvreader.loadTravisCommitInfo(Config.csvCITransitionFile);
			List<FailFixMapping> changeblocks = travicCIFailFixMApping(cmtlist);
			CSVReaderWriter readerwriter = new CSVReaderWriter();
			readerwriter.writeFailFixMappingListBean(changeblocks,
					"G:\\Research\\ML_CI\\Project_Repo\\Data\\From_Dhia\\results\\applied.csv");
			// generateStatOnChangeBlock(changeblocks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateTravisCIFailFixChangeDataStat() {
		// CSVReaderWriter csvrw = new CSVReaderWriter();
		CVSReader csvreader = new CVSReader();
		try {
			List<TravisCommitInfo> cmtlist = csvreader.loadTravisCommitInfo(Config.csvCITransitionFile);
			List<FailFixMappingStatistics> changeblocks = travicCIFailFixMAppingStat(cmtlist);
			generateStatistics(changeblocks);
			System.out.println("\n\n************Top 10 Fix Pattern*************\n\n");
			generateFixPatternsRQ4(changeblocks);
			// generateStatOnChangeBlock(changeblocks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateFixPatterns(List<FailFixMappingStatistics> changeblocks) {
		Map<String, Integer> errordata = new HashMap<>();
		Map<String, Integer> fixblock = new HashMap<>();
		Map<String, Integer> errorfixmap = new HashMap<>();
		Map<String, Integer> cmdmap = new HashMap<>();
		// Map<String,Integer> blockcmdmap=new HashMap<>();

		for (FailFixMappingStatistics item : changeblocks) {
			if (errordata.containsKey(item.getMainProblem())) {
				int count = errordata.get(item.getMainProblem());
				errordata.put(item.getMainProblem(), count + 1);
			} else {
				errordata.put(item.getMainProblem(), 1);
			}

			/*********************************************************/
			List<String> fixblocklist = item.getFixBlockList();

			for (String block : fixblocklist) {
				if (fixblock.containsKey(block)) {
					int count = fixblock.get(block);
					fixblock.put(block, count + 1);
				} else {
					fixblock.put(block, 1);
				}
			}
			/*********************************************************/
			List<String> fixcmdlist = item.getFixCmdList();

			for (String cmd : fixcmdlist) {
				if (cmdmap.containsKey(cmd)) {
					int count = cmdmap.get(cmd);
					cmdmap.put(cmd, count + 1);
				} else {
					cmdmap.put(cmd, 1);
				}
			}
			/*********************************************************/
			String errorname = item.getMainProblem();

			for (String block : fixblocklist) {
				String mapkey = errorname + "_" + block;

				if (errorfixmap.containsKey(mapkey)) {
					int count = errorfixmap.get(mapkey);
					errorfixmap.put(mapkey, count + 1);
				} else {
					errorfixmap.put(mapkey, 1);
				}
			}

			/***************************************************************/

		}

		int size = changeblocks.size();

		Map<String, Float> errortypepercentage = new HashMap<>();
		// System.out.println("\n\n******Error Type*******");
		for (String itemkey : errordata.keySet()) {
			int count = errordata.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			/// System.out.println(itemkey + "==>" + percent);

			if (!errortypepercentage.containsKey(itemkey)) {
				errortypepercentage.put(itemkey, percent);
			}
		}

		// System.out.println("\n\n******Fix Block*******");

		Map<String, Float> fixblockpercentage = new HashMap<>();

		for (String itemkey : fixblock.keySet()) {
			int count = fixblock.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			// System.out.println(itemkey + "==>" + percent);

			if (!fixblockpercentage.containsKey(itemkey)) {
				fixblockpercentage.put(itemkey, percent);
			}
		}

		// System.out.println("\n\n******Error Fix Block Mapping*******");
		for (String itemkey : errorfixmap.keySet()) {
			int count = errorfixmap.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			// System.out.println(itemkey + "==>" + percent);
		}

		// System.out.println("\n\n******Changed Command*******");
		for (String itemkey : cmdmap.keySet()) {
			int count = cmdmap.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			// System.out.println(itemkey + "==>" + percent);
		}

		LinkedHashMap<String, Float> reverseSortedBlockMap = new LinkedHashMap<>();
		fixblockpercentage.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedBlockMap.put(x.getKey(), x.getValue()));

		LinkedHashMap<String, Float> reverseSortedErrorTypeMap = new LinkedHashMap<>();
		errortypepercentage.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedErrorTypeMap.put(x.getKey(), x.getValue()));

		List<String> listoftopblocks = new ArrayList<>();

		int count = 0;
		for (String blockkey : reverseSortedBlockMap.keySet()) {
			listoftopblocks.add(blockkey);

//			if (count >= 9)
//				break;

			count++;
		}

		Map<String, Map<String, Integer>> pattern = new HashMap<>();
		Map<String, Integer> changepattenr = new HashMap<>();
		Map<String, List<String>> patternlabel = new HashMap<>();
		for (String topblock : listoftopblocks) {
			// Map<String, Integer> changepattenr = new HashMap<>();

			for (FailFixMappingStatistics item : changeblocks) {
				// List<String> fixblocklist = item.getFixBlockList();
				List<String> fixblocklist = new ArrayList<String>(new HashSet<String>(item.getFixBlockList()));
				List<CmdStatement> cmdstmts = item.getCmdStmtList();
				for (String blockitem : fixblocklist) {
					if (topblock.equals(blockitem)) {
						// List<String> cmdlist = item.getFixCmdList();

						for (CmdStatement cmdstmt : cmdstmts) {
							List<String> cmdlist = cmdstmt.getCmdList();
							int index = 0;
							if (cmdlist.size() > 0) {
								for (String cmd : cmdlist) {
									if (changepattenr.containsKey(cmd)) {
										int val = changepattenr.get(cmd);
										changepattenr.put(cmd, val + 1);
										String label = item.getChangeLabel();
										label = cmdstmt.getStrStmt();
										patternlabel.get(cmd).add(cmdstmt.getStrStmt());
									} else {
										changepattenr.put(cmd, 1);
										String label = item.getChangeLabel();
										List<String> listlabels = new ArrayList<>();
										label = cmdstmt.getStrStmt();
										listlabels.add(cmdstmt.getStrStmt());
										patternlabel.put(cmd, listlabels);
									}

								}
							} else {
								String blockname = "Block:" + topblock;
								if (changepattenr.containsKey(blockname)) {
									int val = changepattenr.get(blockname);
									changepattenr.put(blockname, val + 1);
									if (cmdstmt.getStrStmt() != null && cmdstmt.getStrStmt().length() > 0) {
										String label = cmdstmt.getStrStmt();
										patternlabel.get(blockname).add(label);
									}
								} else {
									changepattenr.put(blockname, 1);
									List<String> listlabels = new ArrayList<>();
									if (cmdstmt.getStrStmt() != null && cmdstmt.getStrStmt().length() > 0) {
										// String label = item.getFixJsonList().get(0);
										String label = cmdstmt.getStrStmt();
										listlabels.add(label);
										// listlabels.addAll(listlabels);

									}
									patternlabel.put(blockname, listlabels);
								}

							}
							index++;
						}

					}

				}
			}

			// pattern.put(topblock, changepattenr);
		}

		LinkedHashMap<String, Integer> reverseFixPattern = new LinkedHashMap<>();
		changepattenr.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseFixPattern.put(x.getKey(), x.getValue()));

		int max = 0;
//		for(String key:reverseFixPattern.keySet())
//		{
//			int val=reverseFixPattern.get(key);
//			
//			System.out.println("Pattern:"+key+" Count:"+val);
//			
////			if(max>=9)
////				break;
//			
//			max++;
//		}

		max = 0;
		// appendLineToFile
		for (String key : reverseFixPattern.keySet()) {
			int val = reverseFixPattern.get(key);

			// System.out.println("Pattern:" + key + " Count:" + val);
			String line = "Pattern:" + key + " Count:" + val;
			String filename = "G:\\Research\\ML_CI\\Project_Repo\\Results\\labels" + "_" + key + ".txt";
			filename = filename.replace("Block:", "Block_");
			TextFileReaderWriter.appendLineToFile(filename, line);

			List<String> labels = patternlabel.get(key);
			if (key.contains("Block:"))
				System.out.println(key + "," + labels.size());

			if (labels != null) {
				for (String label : labels) {

					// System.out.println(label);
					TextFileReaderWriter.appendLineToFile(filename, label);
				}
			}

//			if(max>=9)
//				break;

			max++;
		}

	}

	public void generateFixPatternsRQ4(List<FailFixMappingStatistics> changeblocks) {
		Map<String, Integer> errordata = new HashMap<>();
		Map<String, Integer> fixblock = new HashMap<>();
		Map<String, Integer> errorfixmap = new HashMap<>();
		Map<String, Integer> cmdmap = new HashMap<>();
		// Map<String,Integer> blockcmdmap=new HashMap<>();
		int totalcount = 0;

		for (FailFixMappingStatistics item : changeblocks) {
			if (errordata.containsKey(item.getMainProblem())) {
				int count = errordata.get(item.getMainProblem());
				errordata.put(item.getMainProblem(), count + 1);
			} else {
				errordata.put(item.getMainProblem(), 1);
			}

			/*********************************************************/
			List<String> fixblocklist = item.getFixBlockList();

			for (String block : fixblocklist) {
				if (fixblock.containsKey(block)) {
					int count = fixblock.get(block);
					fixblock.put(block, count + 1);
				} else {
					fixblock.put(block, 1);
				}
			}
			/*********************************************************/
			List<String> fixcmdlist = item.getFixCmdList();

			for (String cmd : fixcmdlist) {
				if (cmdmap.containsKey(cmd)) {
					int count = cmdmap.get(cmd);
					cmdmap.put(cmd, count + 1);
				} else {
					cmdmap.put(cmd, 1);
				}
			}
			/*********************************************************/
			String errorname = item.getMainProblem();

			for (String block : fixblocklist) {
				String mapkey = errorname + "_" + block;

				if (errorfixmap.containsKey(mapkey)) {
					int count = errorfixmap.get(mapkey);
					errorfixmap.put(mapkey, count + 1);
				} else {
					errorfixmap.put(mapkey, 1);
				}
			}
			totalcount++;

			/***************************************************************/

		}

		LinkedHashMap<String, Integer> reverseFixBlock = new LinkedHashMap<>();
		fixblock.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseFixBlock.put(x.getKey(), x.getValue()));

		Map<String, String> blockcategory = loadBlockCategory();

		Map<String, Integer> categoryfreq = new HashMap<>();

		for (String key : reverseFixBlock.keySet()) {
			if (blockcategory.containsKey(key)) {
				int freq = reverseFixBlock.get(key);
				String type = blockcategory.get(key);

				if (!type.equals("Exclude")) {
					if (categoryfreq.containsKey(type)) {
						int count = categoryfreq.get(type);
						categoryfreq.put(type, count + freq);
					} else {
						categoryfreq.put(type, freq);
					}
				}
			}
		}

		DecimalFormat frmt = new DecimalFormat();
		frmt.setMaximumFractionDigits(2);

		System.out.println("**************Result******************\n\n");
		
		System.out.println("Total Commit Count==>"+totalcount);

		for (String key : categoryfreq.keySet()) {
			int count = categoryfreq.get(key);

			//float percentage = (float(count) * 100 / totalcount);
			float percentage= (float) ((float)count*100.00);
			percentage=percentage/(float)totalcount;

			System.out.println(key + "==>" +count+"==>"+ frmt.format(percentage));
		}
		
		System.out.println("**************Blocklist and Value******************\n\n");
		for (String key : reverseFixBlock.keySet()) {
			System.out.println(key+"==>"+reverseFixBlock.get(key));
		}
		
		
		
		//new code to consider Each fix location type once
		Map<Integer,List<String>> fixloccount=new HashMap<>();
		blockcategory = loadBlockCategory();
		
		int index=0;
		
		for (FailFixMappingStatistics item : changeblocks) {
			
			List<String> fixblocklist = item.getFixBlockList();
			List<String> fixlocationlist=new ArrayList<>();

			for (String block : fixblocklist) {
				
				if(blockcategory.containsKey(block))
				{
					String location=blockcategory.get(block);
					
					if(!fixlocationlist.contains(location) && !location.equals("Exclude"))
					{
						fixlocationlist.add(location);
					}
				}
				
			}
			
			fixloccount.put(index, fixlocationlist);
			index++;
		}
		
		Map<String,Integer> locationcount=new HashMap<>();
		for(Integer keyid:fixloccount.keySet()) {
			
			List<String> fixlocationlist=fixloccount.get(keyid);
				
			for(String loc:fixlocationlist)
			{
				if (locationcount.containsKey(loc)) {
					int count = locationcount.get(loc);
					locationcount.put(loc, count + 1);
				} else {
					locationcount.put(loc, 1);
				}
				
			}
		}
		
		
		System.out.println("**************New Result*************\n\n\n");
		for (String key : locationcount.keySet()) {
			int count = locationcount.get(key);

			//float percentage = (float(count) * 100 / totalcount);
			float percentage= (float) ((float)count*100.00);
			percentage=percentage/(float)totalcount;

			System.out.println(key + "==>" +count+"==>"+ frmt.format(percentage));
		}

	}

	public void generateStatistics(List<FailFixMappingStatistics> changeblocks) {

		Map<String, Integer> errordata = new HashMap<>();
		Map<String, Integer> fixblock = new HashMap<>();
		Map<String, Integer> errorfixmap = new HashMap<>();
		Map<String, Integer> cmdmap = new HashMap<>();
		// Map<String,Integer> blockcmdmap=new HashMap<>();

		for (FailFixMappingStatistics item : changeblocks) {
			if (errordata.containsKey(item.getMainProblem())) {
				int count = errordata.get(item.getMainProblem());
				errordata.put(item.getMainProblem(), count + 1);
			} else {
				errordata.put(item.getMainProblem(), 1);
			}

			/*********************************************************/
			List<String> fixblocklist = item.getFixBlockList();

			for (String block : fixblocklist) {
				if (fixblock.containsKey(block)) {
					int count = fixblock.get(block);
					fixblock.put(block, count + 1);
				} else {
					fixblock.put(block, 1);
				}
			}
			/*********************************************************/
			List<String> fixcmdlist = item.getFixCmdList();

			for (String cmd : fixcmdlist) {
				if (cmdmap.containsKey(cmd)) {
					int count = cmdmap.get(cmd);
					cmdmap.put(cmd, count + 1);
				} else {
					cmdmap.put(cmd, 1);
				}
			}
			/*********************************************************/
			String errorname = item.getMainProblem();

			for (String block : fixblocklist) {
				String mapkey = errorname + "_" + block;

				if (errorfixmap.containsKey(mapkey)) {
					int count = errorfixmap.get(mapkey);
					errorfixmap.put(mapkey, count + 1);
				} else {
					errorfixmap.put(mapkey, 1);
				}
			}

			/***************************************************************/

		}

		int size = changeblocks.size();

		Map<String, Float> errortypepercentage = new HashMap<>();
		System.out.println("\n\n******Error Type*******");
		for (String itemkey : errordata.keySet()) {
			int count = errordata.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			System.out.println(itemkey + "==>" + percent);

			if (!errortypepercentage.containsKey(itemkey)) {
				errortypepercentage.put(itemkey, percent);
			}
		}

		System.out.println("\n\n******Fix Block*******");

		Map<String, Float> fixblockpercentage = new HashMap<>();

		for (String itemkey : fixblock.keySet()) {
			int count = fixblock.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			System.out.println(itemkey + "==>" + percent + "==>" + count);

			if (!fixblockpercentage.containsKey(itemkey)) {
				fixblockpercentage.put(itemkey, percent);
			}
		}

		System.out.println("\n\n******Error Fix Block Mapping*******");
		for (String itemkey : errorfixmap.keySet()) {
			int count = errorfixmap.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			System.out.println(itemkey + "==>" + percent);
		}

		System.out.println("\n\n******Changed Command*******");
		for (String itemkey : cmdmap.keySet()) {
			int count = cmdmap.get(itemkey);
			float percent = (float) ((((float) count) / ((float) size)) * 100.00);
			System.out.println(itemkey + "==>" + percent);
		}

		LinkedHashMap<String, Float> reverseSortedBlockMap = new LinkedHashMap<>();
		fixblockpercentage.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedBlockMap.put(x.getKey(), x.getValue()));

		LinkedHashMap<String, Float> reverseSortedErrorTypeMap = new LinkedHashMap<>();
		errortypepercentage.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> reverseSortedErrorTypeMap.put(x.getKey(), x.getValue()));

		System.out.println("\n\n******Table Data*******");
		for (String key : reverseSortedErrorTypeMap.keySet()) {
			System.out.println("\n" + key + "==>");
			int index = 0;
			for (String blockkey : reverseSortedBlockMap.keySet()) {
				String strtypeblock = key + "_" + blockkey;
				float value = (float) 0.0;

				if (errorfixmap.containsKey(strtypeblock)) {
					value = errorfixmap.get(strtypeblock);
					value = (float) ((((float) value) / ((float) size)) * 100.00);
				}

				System.out.print(value + " ");

				if (index == 9)
					break;

				index++;
			}

		}

	}

	/*
	 * For each transition data Invoke AST diff method to get changes
	 */
	public List<FailFixMapping> travicCIFailFixMApping(List<TravisCommitInfo> fixlist) {

		List<FailFixMapping> failfixmappings = new ArrayList<>();
		int id = 1;
		int count = 0;
		for (TravisCommitInfo fix : fixlist) {
			System.out.println("Record ID==>" + id++);

			List<String> parentblocks = new ArrayList<>();
			List<String> changecmds = new ArrayList<>();
			String projname = ProjectPropertyAnalyzer.getProjName(fix.getRepoUrl());
			CommitAnalyzer cmtanalyzer = null;
			try {
				cmtanalyzer = new CommitAnalyzer("test", projname);
				// need to fix here for fail commit id
				EditScript actions = cmtanalyzer.extractTravisFileChange(fix.getFailCommit(), fix.getPassCommit());

				if (actions != null) {
					count++;

					for (Action action : actions) {
						ITree treenode = action.getNode();
						String jsonblock = (String) treenode.getMetadata("json_parent");
						String label = getNodeLabel(treenode);
						String straction = "";
						if (action instanceof TreeDelete) {
							straction = "Tree-Delete";
						} else if (action instanceof TreeAddition) {
							straction = "Tree-Addition";
						} else if (action instanceof TreeInsert) {
							straction = "Tree-Insert";
						} else if (action instanceof Move) {
							straction = "Move";
						} else if (action instanceof Update) {
							straction = "Update";
						} else if (action instanceof Insert) {
							straction = "Insert";
						} else if (action instanceof Addition) {
							straction = "Addition";
						} else if (action instanceof Delete) {
							straction = "Delete";
						} else {
							System.out.println("asdasdasd");
						}

						List<String> changecmd = getCmdListFromChange(jsonblock, label);

						parentblocks.add(jsonblock);
						changecmds.addAll(changecmd);
						// NodeLabel nodelable = new NodeLabel(treenode, label, straction,
						// action,changecmd);
						// changeblocks.addItemToMap(jsonblock, nodelable);
					}

					String strblock = parentblocks.toString();
					String strcmds = changecmds.toString();

					FailFixMapping failmap = new FailFixMapping(fix.getRepoUrl(), fix.getMainProblem(),
							fix.getProblemBuildState(), strblock, strcmds);

					failmap.setPassCmt(fix.getPassCommit());
					failfixmappings.add(failmap);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			String strblock=parentblocks.toString();
//			String strcmds=changecmds.toString();
//			
//			FailFixMapping failmap=new FailFixMapping(fix.getRepoUrl(),fix.getMainProblem(),fix.getProblemBuildState(),strblock,strcmds);
//		
//			failmap.setPassCmt(fix.getPassCommit());

//			failfixmappings.add(failmap);

		}

		System.out.println("Total Diffs=" + count);
		return failfixmappings;

	}

	public List<FailFixMappingStatistics> travicCIFailFixMAppingStat(List<TravisCommitInfo> fixlist) {

		List<FailFixMappingStatistics> failfixmappings = new ArrayList<>();
		int id = 1;
		int count = 0;
		for (TravisCommitInfo fix : fixlist) {

			if (!fix.getMainProblem().toLowerCase().equals("unknown")
					&& !fix.getMainProblem().toLowerCase().equals("undetected")) {

				System.out.println("Record ID==>" + id++);

				List<String> parentblocks = new ArrayList<>();
				List<String> rootblocks = new ArrayList<>();
				List<String> changecmds = new ArrayList<>();
				List<CmdStatement> cmdstatements = new ArrayList<>();
				List<String> jsonlabel = new ArrayList<>();

				String projname = ProjectPropertyAnalyzer.getProjName(fix.getRepoUrl());
				CommitAnalyzer cmtanalyzer = null;
				try {
					cmtanalyzer = new CommitAnalyzer("test", projname);
					// need to fix here for fail commit id
					// EditScript actions = cmtanalyzer.extractTravisFileChange(fix.getFailCommit(),
					// fix.getPassCommit());
					EditScript actions = cmtanalyzer.extractTravisFileChangeV2(fix.getFailCommit(), fix.getPassCommit(),
							projname);
					if (actions != null && actions.size() > 0) {
						count++;
						String label = null;
						for (Action action : actions) {
							ITree treenode = action.getNode();
							String jsonblock = (String) treenode.getMetadata("json_parent");
							String jsonfixblockblock = (String) treenode.getMetadata("json_fixlocation");
							label = getNodeLabel(treenode);

							String straction = "";
							if (action instanceof TreeDelete) {
								straction = "Tree-Delete";
							} else if (action instanceof TreeAddition) {
								straction = "Tree-Addition";
							} else if (action instanceof TreeInsert) {
								straction = "Tree-Insert";
							} else if (action instanceof Move) {
								straction = "Move";
							} else if (action instanceof Update) {
								straction = "Update";
							} else if (action instanceof Insert) {
								straction = "Insert";
							} else if (action instanceof Addition) {
								straction = "Addition";
							} else if (action instanceof Delete) {
								straction = "Delete";
							}

//							if (jsonblock.equals("python")) {
//								System.out.println("test");
//							}
							List<String> changecmd = getCmdListFromChange(jsonblock, label);

//							if (label.contains("mxnet") && label.contains("tensorflow")) {
//								System.out.println("asdasd");
//							}

							if (changecmd != null) {
								CmdStatement cmdstament = new CmdStatement();
								cmdstament.addCmds(changecmd);
								cmdstament.setStrStmt(label);
								// changecmds.addAll(changecmd);
								cmdstatements.add(cmdstament);
							} else {
								CmdStatement cmdstament = new CmdStatement();
								// cmdstament.addCmds(changecmd);
								cmdstament.setStrStmt(label);
								// changecmds.addAll(changecmd);
								cmdstatements.add(cmdstament);
								jsonlabel.add(label);
							}

							if (!parentblocks.contains(jsonblock))
								parentblocks.add(jsonblock);

							if (!rootblocks.contains(jsonfixblockblock))
								rootblocks.add(jsonfixblockblock);

							// NodeLabel nodelable = new NodeLabel(treenode, label, straction,
							// action,changecmd);
							// changeblocks.addItemToMap(jsonblock, nodelable);
						}

						String strblock = parentblocks.toString();
						String strcmds = changecmds.toString();

						FailFixMappingStatistics failmap = new FailFixMappingStatistics(fix.getRepoUrl(),
								fix.getMainProblem(), fix.getProblemBuildState(), strblock, strcmds);
						failmap.setPassCmt(fix.getPassCommit());

						failmap.setFixBlockList(rootblocks);
						failmap.setFirstLavelBlockList(parentblocks);
						failmap.setFixCmdList(changecmds);
						failmap.setCmdStmtList(cmdstatements);
						failmap.setFixJsonList(jsonlabel);

						if (label != null)
							failmap.setChangeLabel(label);

						failfixmappings.add(failmap);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//			String strblock=parentblocks.toString();
//			String strcmds=changecmds.toString();
//			
//			FailFixMapping failmap=new FailFixMapping(fix.getRepoUrl(),fix.getMainProblem(),fix.getProblemBuildState(),strblock,strcmds);
//		
//			failmap.setPassCmt(fix.getPassCommit());

//			failfixmappings.add(failmap);
			}
		}

		System.out.println("Total Diffs=" + count);
		return failfixmappings;

	}

	public List<FailFixMappingStatistics> travicCIFailFixMAppingStatV3(List<TravisCommitInfo> fixlist) {

		List<FailFixMappingStatistics> failfixmappings = new ArrayList<>();
		int id = 1;
		int count = 0;
		for (TravisCommitInfo fix : fixlist) {

			if (!fix.getMainProblem().toLowerCase().equals("unknown")
					&& !fix.getMainProblem().toLowerCase().equals("undetected")) {

				System.out.println("Record ID==>" + id++);

				List<String> parentblocks = new ArrayList<>();
				List<String> changecmds = new ArrayList<>();
				List<CmdStatement> cmdstatements = new ArrayList<>();
				List<String> jsonlabel = new ArrayList<>();

				String projname = ProjectPropertyAnalyzer.getProjName(fix.getRepoUrl());
				CommitAnalyzer cmtanalyzer = null;
				try {
					cmtanalyzer = new CommitAnalyzer("test", projname);
					// need to fix here for fail commit id
					// EditScript actions = cmtanalyzer.extractTravisFileChange(fix.getFailCommit(),
					// fix.getPassCommit());
					EditScript actions = cmtanalyzer.extractTravisFileChangeV2(fix.getFailCommit(), fix.getPassCommit(),
							projname);
					if (actions != null && actions.size() > 0) {
						count++;
						String label = null;
						for (Action action : actions) {
							ITree treenode = action.getNode();
							String jsonblock = (String) treenode.getMetadata("json_parent");
							label = getNodeLabel(treenode);
							String straction = "";
							if (action instanceof TreeDelete) {
								straction = "Tree-Delete";
							} else if (action instanceof TreeAddition) {
								straction = "Tree-Addition";
							} else if (action instanceof TreeInsert) {
								straction = "Tree-Insert";
							} else if (action instanceof Move) {
								straction = "Move";
							} else if (action instanceof Update) {
								straction = "Update";
							} else if (action instanceof Insert) {
								straction = "Insert";
							} else if (action instanceof Addition) {
								straction = "Addition";
							} else if (action instanceof Delete) {
								straction = "Delete";
							}

						}

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//			String strblock=parentblocks.toString();
//			String strcmds=changecmds.toString();
//			
//			FailFixMapping failmap=new FailFixMapping(fix.getRepoUrl(),fix.getMainProblem(),fix.getProblemBuildState(),strblock,strcmds);
//		
//			failmap.setPassCmt(fix.getPassCommit());

//			failfixmappings.add(failmap);
			}
		}

		System.out.println("Total Diffs=" + count);
		return failfixmappings;

	}

	public List<FailFixMappingStatistics> travicCIFailFixMAppingStatV2(List<TravisCommitInfo> fixlist) {

		List<FailFixMappingStatistics> failfixmappings = new ArrayList<>();
		int id = 1;
		int count = 0;
		for (TravisCommitInfo fix : fixlist) {

			if (!fix.getMainProblem().toLowerCase().equals("unknown")
					&& !fix.getMainProblem().toLowerCase().equals("undetected")) {

				System.out.println("Record ID==>" + id++);

				List<String> parentblocks = new ArrayList<>();
				List<String> changecmds = new ArrayList<>();
				String projname = ProjectPropertyAnalyzer.getProjName(fix.getRepoUrl());
				CommitAnalyzer cmtanalyzer = null;
				try {
					cmtanalyzer = new CommitAnalyzer("test", projname);
					// need to fix here for fail commit id
					EditScript actions = cmtanalyzer.extractTravisFileChange(fix.getFailCommit(), fix.getPassCommit());

					if (actions != null) {
						count++;

						for (Action action : actions) {
							ITree treenode = action.getNode();
							String jsonblock = (String) treenode.getMetadata("json_parent");
							String label = getNodeLabel(treenode);
							String straction = "";
							if (action instanceof TreeDelete) {
								straction = "Tree-Delete";
							} else if (action instanceof TreeAddition) {
								straction = "Tree-Addition";
							} else if (action instanceof TreeInsert) {
								straction = "Tree-Insert";
							} else if (action instanceof Move) {
								straction = "Move";
							} else if (action instanceof Update) {
								straction = "Update";
							} else if (action instanceof Insert) {
								straction = "Insert";
							} else if (action instanceof Addition) {
								straction = "Addition";
							} else if (action instanceof Delete) {
								straction = "Delete";
							} else {
								System.out.println("asdasdasd");
							}

							List<String> changecmd = getCmdListFromChange(jsonblock, label);

							parentblocks.add(jsonblock);
							changecmds.addAll(changecmd);
							// NodeLabel nodelable = new NodeLabel(treenode, label, straction,
							// action,changecmd);
							// changeblocks.addItemToMap(jsonblock, nodelable);
						}

						String strblock = parentblocks.toString();
						String strcmds = changecmds.toString();

						FailFixMappingStatistics failmap = new FailFixMappingStatistics(fix.getRepoUrl(),
								fix.getMainProblem(), fix.getProblemBuildState(), strblock, strcmds);
						failmap.setPassCmt(fix.getPassCommit());

						failmap.setFixBlockList(parentblocks);
						failmap.setFixCmdList(changecmds);

						failfixmappings.add(failmap);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//			String strblock=parentblocks.toString();
//			String strcmds=changecmds.toString();
//			
//			FailFixMapping failmap=new FailFixMapping(fix.getRepoUrl(),fix.getMainProblem(),fix.getProblemBuildState(),strblock,strcmds);
//		
//			failmap.setPassCmt(fix.getPassCommit());

//			failfixmappings.add(failmap);
			}
		}

		System.out.println("Total Diffs=" + count);
		return failfixmappings;

	}

	public TravisCIChangeBlocks travicCIDiffGenerate(List<TravisCommitInfo> fixlist) {

		TravisCIChangeBlocks changeblocks = new TravisCIChangeBlocks();
		int id = 1;
		int count = 0;
		for (TravisCommitInfo fix : fixlist) {
			String projname = ProjectPropertyAnalyzer.getProjName(fix.getRepoUrl());
			CommitAnalyzer cmtanalyzer = null;

			System.out.println("test id==>" + id);

			id++;

			try {
				cmtanalyzer = new CommitAnalyzer("test", projname);
				// need to fix here for fail commit id
				EditScript actions = cmtanalyzer.extractTravisFileChange(fix.getFailCommit(), fix.getPassCommit());

				if (actions != null) {
					count++;
					for (Action action : actions) {
						ITree treenode = action.getNode();
						String jsonblock = (String) treenode.getMetadata("json_parent");
						String label = getNodeLabel(treenode);
						String straction = "";
						if (action instanceof TreeDelete) {
							straction = "Tree-Delete";
						} else if (action instanceof TreeAddition) {
							straction = "Tree-Addition";
						} else if (action instanceof TreeInsert) {
							straction = "Tree-Insert";
						} else if (action instanceof Move) {
							straction = "Move";
						} else if (action instanceof Update) {
							straction = "Update";
						} else if (action instanceof Insert) {
							straction = "Insert";
						} else if (action instanceof Addition) {
							straction = "Addition";
						} else if (action instanceof Delete) {
							straction = "Delete";
						} else {
							System.out.println("asdasdasd");
						}

						List<String> changecmd = getCmdListFromChange(jsonblock, label);
						NodeLabel nodelable = new NodeLabel(treenode, label, straction, action, changecmd);
						changeblocks.addItemToMap(jsonblock, nodelable);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Total Diffs=" + count);
		return changeblocks;

	}

	public void generateStatOnChangeBlock(TravisCIChangeBlocks changeblocks) {
		Map<String, List<NodeLabel>> changeList = changeblocks.getChangeList();

		Writer writer = null;
		String file = "G:\\Research\\ML_CI\\Project_Repo\\PatchDir\\log.txt";

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"));

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		for (String key : changeList.keySet()) {
			List<NodeLabel> nodelabel = changeList.get(key);

			for (NodeLabel node : nodelabel) {
				String line = key + "<====>" + node.getStrAction() + "<====>" + node.getLabel() + "<=====>"
						+ node.getCmdsString();
				try {
					writer.write(line);
					writer.write("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			System.out.println("Block Name==>" + key + "   Size==>" + nodelabel.size());

		}

		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNodeLabel(ITree node) {
//		StringBuilder strbuild = new StringBuilder();
//
//		strbuild.append(node.getLabel());
//		List<ITree> children = node.getChildren();
//
//		for (ITree child : children) {
//			strbuild.append(child.getLabel());
//		}
//
//		return strbuild.toString();
		List<ITree> allnodes = TreeUtils.preOrder(node);
		StringBuilder strbuild = new StringBuilder();

		// strbuild.append(node.getLabel());

		boolean isfiled = false;

		if (node.getType().toString().equals("FIELD")) {
			isfiled = true;
		}

		for (ITree child : allnodes) {
			String childlabel = child.getLabel();
			if (child.getType().toString().equals("FIELD")) {
				isfiled = true;
			}
			if (childlabel != null && childlabel.length() > 0) {

				childlabel = childlabel.replaceFirst("\"", "");
				int last = childlabel.lastIndexOf('\"');
				if (last > 0 && last < childlabel.length()) {
					childlabel = childlabel.substring(0, last);
				}

				if (isfiled) {
					strbuild.append(childlabel + ":");
					isfiled = false;
				} else {
					strbuild.append(childlabel + " ");
				}
			}

		}

		String strlabel = strbuild.toString();
		strlabel = strlabel.trim();
		// System.out.println(strlabel);
		return strlabel;

	}

	public List<String> getCmdListFromChange(String changeblock, String changestr) {

		List<String> allowedblck = new ArrayList<>();

		allowedblck.add("install");
		allowedblck.add("script");
		allowedblck.add("before_install");
		allowedblck.add("before_script");
		allowedblck.add("after_script");
		allowedblck.add("after_success");
		allowedblck.add("after_failure");

		String str = changestr;

		str = str.replace("install:", "");
		str = str.replace("script:", "");
		str = str.replace("before_install:", "");
		str = str.replace("before_script:", "");
		str = str.replace("after_script:", "");
		str = str.replace("after_success:", "");
		str = str.replace("after_failure:", "");

		if (allowedblck.contains(changeblock)) {
			BashCmdAnalysis bashcmdanalysis = new BashCmdAnalysis();
			Map<String, String> envmap = new HashMap<>();

			List<String> basecmds = bashcmdanalysis.getBashCommandTreeFromChange(str, envmap);
			return basecmds;
		} else {
			// List<String> basecmds = new ArrayList<>();
			return null;
		}
	}

	private Map<String, String> loadBlockCategory() {
		Map<String, String> blockcat = null;

		CVSReader reader = new CVSReader();

		try {
			blockcat = reader.loadBlockType(Config.csvBlockCategory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return blockcat;
	}

}
