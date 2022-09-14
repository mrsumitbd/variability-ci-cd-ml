#### authored by hassan


from os import chdir
from pathlib import Path
from shutil import rmtree
from subprocess import run

from joblib import delayed, Parallel
from pandas import read_csv


PATH = Path(__file__).parent.joinpath('clones/').absolute()


def import_projects():
    return read_csv('projects_with_workflows.csv', usecols=['project', 'master']).to_dict(orient='records')


def execute(command):
    run(command, capture_output=True, shell=True, check=True, text=True)


def clone_projects(project, branch):
    chdir(PATH)
    path = PATH / project.replace('/', '_')

    while True:
        try:
            print(f'{project}: Started cloning project')

            path.mkdir(parents=True, exist_ok=True)
            chdir(path)

            execute('git init')
            execute(f'git remote add origin -f https://github.com/{project}.git')
            execute('git config core.sparseCheckout true')
            execute("echo '.github' >> .git/info/sparse-checkout")
            execute(f'git pull origin {branch}')

        except Exception as exception:
            print(f'{project}: Failed cloning project due to {exception}')
            rmtree(path)

        else:
            break

    print(f'{project}: Finished cloning project')


def main():
    with Parallel(n_jobs=-1) as parallel:
        parallel(delayed(clone_projects)(project['project'], project['master']) for project in import_projects())


if __name__ == '__main__':
    main()


