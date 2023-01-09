import yaml
from anytree import Node, RenderTree, AsciiStyle
from anytree.dotexport import DotExporter
from graphviz import Source, render


def save_tree_image(tree, file_name):
    DotExporter(tree).to_dotfile(file_name)
    Source.from_file(file_name)
    render('dot', 'png', file_name)


def print_tree(tree, ascii_style=False):
    for pre, fill, node in RenderTree(tree):
        print("%s%s" % (pre, node.name))
    if ascii_style:
        print(RenderTree(tree, style=AsciiStyle()).by_attr())


class ProcessYAML:
    def __init__(self, yaml_stream):
        try:
            self.yaml_content = yaml.safe_load(yaml_stream)
        except yaml.YAMLError as exception:
            print(exception)

    def generate_parse_tree_from_yaml(self, yaml_data, root=Node(".travis.yml")):
        for key, value in yaml_data.items():
            node = Node(key, parent=root)
            if isinstance(value, dict):
                self.generate_parse_tree_from_yaml(value, root=node)
            elif isinstance(value, list):
                for item in value:
                    if isinstance(item, dict):
                        self.generate_parse_tree_from_yaml(item, root=node)
                    else:
                        Node(item, parent=node)
            else:
                Node(value, parent=node)
        return root

    def __del__(self):
        print("Collecting garbage...Done.")
