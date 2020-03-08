try:
    import matplotlib.pyplot as plt
    import networkx as nx
except ImportError:
    exit("Please install networkx and matplotlib in order to run this script, (pip install matplotlib networkx)")

isDirected = input("Is the graph directed? (y/n): ")

isDirected = isDirected == 'y' 
 
G = None
if(isDirected):
    G = nx.MultiDiGraph()
else:
    G = nx.MultiGraph()

nVertices = int(input("Enter the number of vertices: "))
for i in range(nVertices):
    G.add_node(i)

nEdges = int(input("Enter the number of edges: "))

for i in range(nEdges):
    a = int(input("Enter edge #" + str(i+1) + " a: "))
    b = int(input("Enter edge #" + str(i+1) + " b: "))
    G.add_edge(a, b)

nx.draw(G, with_labels=True)
# plt.show()
plt.savefig("graph.png")
