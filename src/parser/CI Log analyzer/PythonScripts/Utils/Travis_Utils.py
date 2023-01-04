import pandas as pd
from PyTravisCI import defaults, TravisCI

def get_travis_com_access():
    travis_access_com = TravisCI(access_token="", access_point=defaults.access_points.PRIVATE) # add trvis ci com token here
    return travis_access_com


def get_travis_org_access():
    travis_access_org = TravisCI(access_token="") # add trvis ci org token here
    return travis_access_org

def get_travis_build(buildID):
    try:
        travis_build = get_travis_com_access().get_build(buildID)
        list_of_jobs = travis_build.get_jobs().jobs
        if list_of_jobs is None or len(list_of_jobs) == 0:
            raise Exception
    except Exception as e:
        try:
            travis_build = get_travis_org_access().get_build(buildID)
            list_of_jobs = travis_build.get_jobs().jobs
            if list_of_jobs is None or len(list_of_jobs) == 0:
                raise Exception
        except Exception as e:
            print(e)
            raise e
    return travis_build


def get_travis_repo(repo_name,verboseMode=False):
        try:
            travis_repo = get_travis_org_access().get_repository(repo_name)
            list_of_builds = travis_repo.get_builds().builds
            if list_of_builds is None or len(list_of_builds) == 0:
                raise Exception
        except Exception as e1:
            try:
                travis_repo = get_travis_com_access().get_repository(repo_name)
                list_of_builds = travis_repo.get_builds().builds
                if list_of_builds is None or len(list_of_builds) == 0:
                    raise Exception

            except Exception as e2:
                if(verboseMode):
                    print('find exception')
                    print(str(e2))
                raise e2
        return travis_repo


def get_travis_job_repo(job_id):
        try:
            travis_job= get_travis_org_access().get_job(job_id)
            travis_repo = travis_job.repository
            login = str(travis_job.owner.login)
            name = str(travis_repo.name)
            travis_repo_full_name = login + '/' + name
        except:
            try:
                travis_job = get_travis_com_access().get_job(job_id)
                travis_repo = travis_job.repository
                login = str(travis_job.owner.login)
                name = str(travis_repo.name)
                travis_repo_full_name = login + '/' + name
            except Exception as e:
                raise e
        return travis_repo_full_name

def find_repos_in_travis_api_with_1_or_more_builds(travis,set):

    f_errors = open('../../CSV Outputs/errors_travis_api.csv', 'w+')
    f_errors.write('ProjectName,Exception')
    f_errors.write('\n')
    try :
        df_applied=pd.read_csv("../../CSV Inputs/repo-metadata.csv")
       
        tool_projects=list(df_applied["ProjectName"])
        for project_name in tool_projects:
     
            try:
                travis_repo = travis.get_repository(project_name)
                if travis_repo is None:
                    continue
                list_of_builds =travis_repo.get_builds()
                if list_of_builds is None:
                    continue
              
                if len(list_of_builds.dict())>0:
                    set.add(project_name)
                    print(project_name)
            except Exception as e:
                print(str(e))
                if 'not_found' not in str(e):
                    row=project_name+','+str(e)
                    f_errors.write(row)
                    f_errors.write('\n')
                    print(row)
                continue
    except Exception as e:
        print('CSV file access problem')
        print(str(e))


def find_repos_in_travis_api(travis):
    f_applied= open('CSV Outputs/applied_travis_api.csv', 'w')
    f_applied.write('ProjectName')
    f_applied.write('\n')
   
    f_errors = open('CSV Outputs/errors_travis_api.csv', 'w')
    f_errors.write('ProjectName,Exception')
    f_errors.write('\n')

    try :
        df_applied=pd.read_csv("CSV Inputs/AppliedProjects_all.csv")
        
        applied_projects=list(df_applied["ProjectName"])
     
        for project_name in applied_projects:
            try:
                travis_repo = travis.get_repository(project_name)
                f_applied.write(project_name)
                f_applied.write('\n')
                print(project_name)
            except Exception as e:
                print(str(e))
                if 'not_found' not in str(e):
                    row=project_name+','+str(e)
                    f_errors.write(row)
                    f_errors.write('\n')
                    print(row)
                continue
    except Exception as e:
        print('CSV file access problem')
        print(str(e))
