from PyTravisCI import TravisCI, defaults


class ProcessTravisCIBuilds:
    def __init__(self, token):
        self.travis = TravisCI(access_token=token, access_point=defaults.access_points.PRIVATE)

    def create_build_request(self, repo_name, branch):
        repo = self.travis.get_repository(repo_name)
        repo.create_request(f"Build request created for {repo_name}, {branch} branch.", branch)

    def get_build_status(self, repo_link):
        try:
            repository = self.travis.get_repository(repo_link)
            build_status = repository.get_builds(params={"limit": 1})

            return build_status[0].state
        except:
            return "Nothing found."

    def __del__(self):
        pass
