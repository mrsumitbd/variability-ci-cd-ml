package com.TravisCIClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.kohsuke.github.GHBlob;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeEntry;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import com.google.common.collect.Iterables;

import fr.inria.jtravis.JTravis;
import fr.inria.jtravis.entities.Build;
import fr.inria.jtravis.entities.Builds;
import fr.inria.jtravis.entities.Repository;

public class TestTravisCIClient {

	public static void main(String[] args) {
//		JTravis jTravis = new JTravis.Builder().build(); // you can specify in the builder the Github API token and/or the Travis CI API token
//		Optional<Repository> repository = jTravis.repository().fromSlug("nengo/nengo-dl");
//		
//	
//
//		if (repository.isPresent()) {
//		    Optional<Builds> optionalBuilds = jTravis.build().fromRepository(repository.get());
//
//		    if (optionalBuilds.isPresent()) {
//		        for (Build build : optionalBuilds.get().getBuilds()) {
//		            System.out.println("build id: "+build.getId()+" status: "+build.getState().name()+ " Commitid: "+build.getCommit());
//		           
//		        }
//		    }
//		}

		GitHub github;
		try {
			github = GitHub.connectUsingPassword("user","password");

			GHRepository repo = github.getRepository("nengo/nengo-dl");
			
			GHCommit commit=repo.getCommit("cf34c3c19e6c1392adf6522b3234791f95536d95");
			GHTree tree=commit.getTree();
			
			List<GHTreeEntry> ghentry=tree.getTree();
			
			for(GHTreeEntry item:ghentry)
			{
				if(item.getPath().contains("travis.yml"))
				{
					GHBlob gblob=item.asBlob();
					InputStream ist=gblob.read();
					String text = new String(ist.readAllBytes(), StandardCharsets.UTF_8);
					System.out.println(text);
				}
			}
			
			
			
			//ghentry.
			
			System.out.println(commit);
//			PagedIterable<GHCommit> commits = repo.queryCommits().list();
//			for (GHCommit commit : Iterables.limit(commits, 10)) {
//				GHCommit expected = repo.getCommit(commit.getSHA1());
//
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
