# this is a python file
from github import Github


g = Github("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")


github_user = g.get_user()

repo = g.get_repo('datarobot/pic2vec')

myfork = github_user.create_fork(repo)


print(myfork)
