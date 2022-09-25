from github import Github


g = Github("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")


github_user = g.get_user()

repo = github_user.get_repo('pic2vec')
file_content = repo.get_contents('.travis.yml')

print(file_content.decoded_content.decode())

