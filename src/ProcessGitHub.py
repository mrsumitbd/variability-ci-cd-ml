from github import Github

class ProcessGithub:
    def __init__(self, token):
        self.g = Github(token)
        self.github_user = self.g.get_user()

    def create_fork(self, repo_link):
        repo = self.g.get_repo(repo_link)
        forked_repo = self.github_user.create_fork(repo)
        return forked_repo

    def get_all_repos(self):
        repo_list = []
        for repo in self.github_user.get_repos():
            repo_list.append(repo.full_name)
        return repo_list

    def delete_repo(self, repo_name):
        repo = self.github_user.get_repo(repo_name)
        repo.delete()

    def list_contents(self, repo_name, folder_name = None):
        repo = self.github_user.get_repo(repo_name)
        if folder_name is not None:
            contents = repo.get_contents(folder_name)
            while len(contents) > 0:
                file_content = contents.pop(0)
                if file_content.type == "dir":
                    contents.extend(repo.get_contents(file_content.path))
                else:
                    print(file_content)

    def __del__(self):
        print("Freeing memory... Done!")
