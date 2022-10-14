from PyTravisCI import TravisCI,defaults
from PyTravisCI.resource_types.repository import Repository
from PyTravisCI.resource_types.builds import Builds

# We initiate our "communication" object.
travis = TravisCI(access_token="ffobOTX2aM0H2kfD1v3dZQ",access_point=defaults.access_points.PRIVATE)
# my_repository: Repository = travis.get_repository("ahmad-abdellatif/scikit-optimize")
# the_builds_of_my_repo: Builds = my_repository.get_builds(params={"limit": 3})
#
# for build in the_builds_of_my_repo:
#     print(
#         f"{build.repository.name}"
#         f"{build.state}"
#     )


my_repository = travis.get_repositories()
for repo in my_repository:
    the_builds_of_my_repo: Builds = repo.get_builds(params={"limit": 1})

    for build in the_builds_of_my_repo:
        print(
            f"{build.repository.name}"
        )
        print(f"{build.state}")
        print("==================")