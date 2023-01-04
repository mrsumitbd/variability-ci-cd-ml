package com.travis.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CmdClustering {

	public List<CommandFrequency> generateCmdFrequency(List<ProjectCommand> projcmds) {
		List<CommandFrequency> cmdfreqs = new ArrayList<>();
		List<String> cmds = new ArrayList<>();

		for (ProjectCommand projcmd : projcmds) {
			cmds.add(projcmd.getBaseCmd());
		}

		List<String> uniquelist = new ArrayList<>(new HashSet<>(cmds));

		for (String cmd : uniquelist) {
			int counter = 0;
			CommandFrequency cmdfreq = new CommandFrequency();

			for (ProjectCommand projcmd : projcmds) {
				if (projcmd.getBaseCmd() != null && projcmd.getBaseCmd().equals(cmd)) {

					if(cmd!=null && cmd.equals("_"))
					{
						System.out.println(projcmd.getProjName()+"-->"+projcmd.getBaseCmd()+"-->"+projcmd.getCmdName());
					}
					counter++;
				}
				
				
			}

			if (cmd != null && cmd.length() > 0) {

				cmdfreq.setCmdName(cmd);
				cmdfreq.setFreCount(counter);
				cmdfreqs.add(cmdfreq);
			}
		}

		return cmdfreqs;
	}

}
