from pathlib import Path
# import networkx as nx
# import matplotlib.pyplot as plt
import ast
import os

from graphviz import render

def script_foler():
    return os.path.dirname(os.path.realpath(__file__))

def file_name(file):
    return os.path.basename(os.path.splitext(file)[0])

# def draw_graph(file):
#     G = nx.drawing.nx_pydot.read_dot(file)
#     # Shamelessly copied from StackOverflow:
#     #https://stackoverflow.com/questions/54439731/networkx-read-dot-does-not-inherit-attributes
#     labels = {n[0] : ast.literal_eval(n[1]['label']) for n in G.nodes(data=True)}
#     size = [int(ast.literal_eval(n[1]['size'])) for n in G.nodes(data=True)]

#     plt.figure(figsize=(20,16))
#     nx.draw(G, font_weight='bold', show_labels=True, labels=labels, node_size=size)
#     plt.savefig(f"{script_foler()}/data/{file_name(file)}.png")

def draw_dot_graph(file):
    render('dot', 'png', file)

files = Path("./").rglob("*.dot")
for file in files:
    draw_dot_graph(file)
