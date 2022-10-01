This is the replicaiton package for the ESEM 2022 paper: ML and CI: Characterizing the usage of CI tools in ML projects

# Dataset:     
   
## Breadth corpus
The breadcth cropus is stored within the **ESEM 2022 - Replication package\Project Sets\ML\RQ1-BreadthCorpus.csv** and **ESEM 2022 - Replication package\Project Sets\Non-ML\RQ1-BreadthCorpus.csv** file. It contains the full names of the projects as they appear on Github. CI adoption was measured on this corpus for RQ1.

Please note that some files in this package wil lreference Applied and Tool project categories, both of these categories are considered ML projects, and since we did not find important difference in their results and due to paper length constraints, we decided to consider them as one category within the paper. They are provided as seperate cateogries in case a future work wants to investigate the difference or similarities between them.

## Depth corpus

**ESEM 2022 - Replication package\ML\Project Sets\RQ2-DepthCorpusWithCurrentAdoption.csv** 
and **ESEM 2022 - Replication package\Non-ML\Project Sets\RQ2-DepthCorpusWithCurrentAdoption.csv** 

contains the subset of the Depth Corpus with the Current CI adoption as defined within the paper. This is the set of projects from which CI tasks were extracted fro RQ2.

**ESEM 2022 - Replication package\Project Sets\RQ3-DepthCorpus.csv** contains the entirety of the Depth corpus. This the corpus used for CI issues analysis we performed. Github links and project types are provided for coveneniece


# files used for the taxonomy construction: 

These files are available under **ESEM 2022 - Replication package\Files used for taxonomy construction**

# Manually labeled files: 
**ESEM 2022 - Replication package\Manually Labeled files** contains 3 folders: *job error files* contains the error files used for the evaluation of the CI log analyzer's error detection, the labels assigned to them by the 2 authors, as well as the final labels after the discussion.  *job fail files* contains similar files used for the CI log analyzer's failure detection, and the *travis ci yml files* contains similar files for the travis ci ast analyzer.


# Source Code 

## Java Source code
Used for RQ2

This code requires  at least Java 8, and Java 14 is recommended. To execute the Java code under **ESEM 2022 - Replication package\Source code\TraVanalyzer**, follow these instructions:
a) Open the project in Eclipse IDE.
b) You can build  and run the project directly from Eclipse by running Maven install on the pom.xml file then running the **src\main\java\com\main\MainClass.java** or run though runable jar file. From Eclipse you can generate runable jar though File>>Export>>Java>>Runable Jar file.
c) After running the project it will list task menus. Select the appropriate menu id to execute the task.

This project can be configured through the Config.java file:
a) rootDir: Root directory of project data and analysis project repository. To configure set the path to the project's "./Project_Data" directory.
b) gitProjList: All Project List
c) gitProjListEval: Only Evaluation Project List
d) repoDir: Directory to download GitHub projects for analysis
e) repoDirEval: Evaluation Project Download Repository.
f) Set user's gitHubUserName, gitHubPwd to download necessary projects from GitHub.

## Python Source code
Used for RQ1, RQ3. The scripts can be configured directly via the modificaton of the paths to the aproprriate CSVs ass listed.


### Utils: 
Helepr scripts used to autmaote some processes : 
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\Utils\Github_Utils.py** used as a helper method to communicate with Github Server, must add Github token token here.
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\Utils\Travis_Utils.py** used as a helper method to communicate with Travis CI Server, must add Github Travis-ci.com and Travis-ci.org token here.
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\Utils\project_pull_git.py** can be used to automate git pulls on a set of projects.


### RQ1 - CI Adoption: 
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\CI_adoption_FileSystem_Based_Local.py** can be used to verify the adoption of CI tools by projects via the file-system method. Simply replace the path *path_to_tool_projects* with the csv containing the projects that need to be tested then execute. *CI_adoption_FileSystem_Based_Remote.py* allows the execution of the same process without the need to clone the projects locally. 

**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\GithubActions_API_adoption** and **ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\TravisCI_API_adoption** can be used to verify the existence of 1+ builds of a list of projects within GitHub Actions and Travis CI. Simply add the path to the csv containing the list of project to the script in place of the path placeholder values in the scripts, and run. Furthermore, you will get the data concerning the total duration of activity and number of builds on each server. **ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\CI_adoption_FSandAPI_diff.py** allows you to determine the difference between the results of the two scripts to determine the historical and current adoption rates. 

**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\project_language_detector.py**
Allows you to extract stats about the primary languages of  a list of projects hosted on Github,  simply replace the path to the repos file in df_repos, and run get_langs_gh() method.  you can either put all the results in one file and then run get_frequency_of_langs to geta file listing the primary languages from  most common to least common. you also have the option to switch the output into two files by project type

**Replication package\Source code\CI Log Analyzer\PythonScripts\CI_Adoption\Travis_adoption_FSandAPI_diff.py** Allows you to apply replication and extract the projects that verify two criteria. By default this script is configured configurated to apply the triangulation method and extract files that have a *travis.yml* file and 1+ build on the Travis server

### RQ2 CI Tasks: 

Run the Java project either via eclipse or directly via the built jar file, then select the menu option: 
1 to Download the projects, then rerun the program 2 to generate the cluster Tavis.yaml file Using AST Analysis, 3 to Execute the Project Task Analysis, then 5 all projects task stat to generate task analysis to generate the stats for different CI tasks. 


### RQ3 - CI Issues: 
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_SuccessFailureRatesAnalysis_Scripts\failure_success_rate_stats_gen.py** and
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_SuccessFailureRatesAnalysis_Scripts\Travis_API_failure_success_rates.py**  the first script can be usd to pull the data concerning the build success rates for individual projects: with the second script, you can generate a file containing a summart about the builds on each line. For example, you may get projectA with 50\% succesful builds, 25\% failed builds, 24\% errored builds, and 1\% canceled builds.



**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_BuildBreak_Log_Analysis\log_downloaders_and_stats_generators\Travis_API_1year_logs_getter_and_stats_generator.py** can be used to download all the failure logs and the errors logs of a list of project, from the dates of their last builds to 365 days before. **category_summary_stats_gen.py** within the same folder can be used to generate summar stats about the logs downloaded. 

**C:\Users\dhiarzig\OneDrive - Umich\Research Projects\ML CI\3 - for ESEM\Replication Package 3.0\Source Code\CI Log analyzer\PythonScripts\TravisCI_Builds_propertiesPerProjects** *build_cleaner* can be used to find and generate the list of duplicate builds, *build_counter* will generate stats about projects while ignoring duplicate builds, and *job_extractor8 will extract jobs which are duplicate. Their results can be used directly in the following steps to ignore duplicate builds and not attempt to classify them.


**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_BuildBreak_Log_Analysis\failure_classifier.py** , 
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_BuildBreak_Log_Analysis\failure_classifier_ml_specific.py** , and
**ESEM 2022 - Replication package\Source code\CI Log Analyzer\PythonScripts\TravisCI_BuildBreak_Log_Analysis\error_classifiers\error_classifier.py** be used respectively to extract failure types from failed jobs logs, ml specific failure from failed jobs logs,and error types from error jobs logs.

# Job Logs:
The job logs used within this work are provided for tracability purposes, since they may no longer be available in the future due to the migration of travis-ci.org to travis-ci.com.

# Results:

## RQ1:
**ESEM 2022 - Replication package\Results\RQ1\CI Adoption FS.ods** contains the intermediary and final results from the csvs generated by our scripts for rq1. 

## RQ2: 
**C:\Users\dhiarzig\OneDrive - Umich\Research Projects\ML CI\3 - for ESEM\Replication Package 3.0\Results\RQ2\Command_types_freq_NonML.csv** contains all the commands found via the clustering process as well as their counts for Non ML projects, and **C:\Users\dhiarzig\OneDrive - Umich\Research Projects\ML CI\3 - for ESEM\Replication Package 3.0\Results\RQ2\Command_types_ML.csv** contains the command type for the ML pojects, while *Command_types_ML.csv* contains the frequency. 

## RQ3: 
**C:\Users\dhiarzig\OneDrive - Umich\Research Projects\ML CI\3 - for ESEM\Replication Package 3.0\Results\RQ3** contains the ods sheets for the failure and success stats of all the projects, as well as the sheets for the classification of the failures and errors of ML and Non-ML projects. 