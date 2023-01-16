from process_github import ProcessGitHub

import unittest


class TestProcessGitHub(unittest.TestCase):
    maxDiff = None

    def setUp(self):
        self.process_gh = ProcessGitHub("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")

    def tearDown(self):
        del self.process_gh

    def test_list_contents(self):
        self.assertEqual(self.process_gh.list_contents("docker-example"),
                         ['Dockerfile', 'Jenkinsfile', 'README.md', 'app', 'requirements.txt'])
        self.assertEqual(self.process_gh.list_contents('docker-example', 'app'), ['main.py'])

    def test_read_file_contents(self):
        test_contents = """dist: xenial
os: linux
language: python

python:
  - 3.6
  - 3.7
  - 3.8

cache: apt

addons:
  apt:
    packages:
    - liblapack-dev

services:
  - xvfb

before_install:
  - wget http://repo.continuum.io/miniconda/Miniconda3-latest-Linux-x86_64.sh -O miniconda.sh
  - bash miniconda.sh -b -p $HOME/miniconda
  - export PATH="$HOME/miniconda/bin:$PATH"
  - conda update --yes conda
  - conda create --yes --name testenv python=$TRAVIS_PYTHON_VERSION
  - source activate testenv

install: pip install -e ".[dev]" codecov

script: pytest --cov prince

after_success: codecov
"""

        self.assertEqual(self.process_gh.read_file_contents("prince", ".travis.yml"), test_contents)

    def test_list_branches(self):
        self.assertEqual(self.process_gh.list_branches("ahmad-abdellatif/3D-convolutional-speaker-recognition"), ['master'])


if __name__ == '__main__':
    unittest.main()
