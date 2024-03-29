from github import Github


class ProcessGitHub(object):
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

    def list_contents(self, repo_name, folder_name=None):  # test passing
        # this function does not list contents of a repo recursively
        # todo: add the feature to recursively list contents of a repo

        repo = self.github_user.get_repo(repo_name)
        if folder_name is None:
            contents = repo.get_contents("")

        else:
            contents = repo.get_contents(folder_name)

        return sorted([content.name for content in contents])

    def read_file_contents(self, repo_name, file_path):  # test passing
        repo = self.github_user.get_repo(repo_name)
        file_contents = repo.get_contents(file_path)
        # print(file_contents.decoded_content.decode())
        return file_contents.decoded_content.decode()

    def list_branches(self, repo_name):  # test passing
        repo = self.g.get_repo(repo_name)
        return [branch.name for branch in list(repo.get_branches())]

    def commit_level_info(self, repo_name): # test passing
        """
        :param repo_name: str -> <user>/<repo_name>
        :return: tuple -> (total number of commits (int), commit details (dictionary))
        """
        repo = self.g.get_repo(repo_name)
        commits = repo.get_commits()
        commit_info = {}
        for commit in commits:
            if commit.commit is not None:
                commit_info[commit.commit.sha] = (commit.commit.message, [file.filename for file in commit.files])
        return commits.totalCount, commit_info


    def __del__(self):
        print("Deleting object... Done!")

# if __name__=='__main__':
#     gh_obj = ProcessGitHub("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")
#     gh_obj.commit_level_info("arsalan0c/mailsense")
