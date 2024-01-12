package d25

import input.Input

data class Wire(
    val v1: String,
    val v2: String
)

fun parseWires(lines: List<String>): List<Wire> {
    return lines.flatMap { line ->
        val parts = line.split(": ")
        val from = parts[0]
        val toMany = parts[1].split(" ")

        toMany.map { Wire(from, it) }
    }
}

fun findHeaviestConnection(node: String, nodeConnections: Map<String, Set<Pair<String, Int>>>): String {
    val connections = nodeConnections[node]!!

    return connections.maxBy { it.second }.first
}

fun joinNodes(
    supernode: String,
    node: String,
    nodeConnections: MutableMap<String, Set<Pair<String, Int>>>,
    separator: Char
): String {
    val newConnections = (
            nodeConnections[supernode]!!.filter { it.first != node } +
                    nodeConnections[node]!!.filter { it.first != supernode }
            )

    val joinedNewConnections = mutableSetOf<Pair<String, Int>>()

    for (i in newConnections.indices) {
        var joined = false

        val conn1 = newConnections[i]

        for (j in (i + 1)..newConnections.lastIndex) {
            val conn2 = newConnections[j]

            if (conn1.first == conn2.first) {
                val newConnection = Pair(conn1.first, conn1.second + conn2.second)
                joinedNewConnections.add(newConnection)
                joined = true
                break
            }
        }

        if (!joined) joinedNewConnections.add(conn1)
    }

    val newSupernode = "$supernode$separator$node"
    nodeConnections[newSupernode] = joinedNewConnections
    nodeConnections.remove(supernode)
    nodeConnections.remove(node)

    nodeConnections.forEach { entry ->
        val updatedConnections = entry.value
            .map { conn ->
                if (conn.first == supernode || conn.first == node)
                    Pair(newSupernode, nodeConnections[newSupernode]!!.first { it.first == entry.key }.second)
                else
                    conn
            }
            .toSet()

        nodeConnections[entry.key] = updatedConnections
    }

    return newSupernode
}

fun findCut(nodeConnections: Map<String, Set<Pair<String, Int>>>): Triple<String, String, Int> {
    val mutableNodeConnections = nodeConnections.toMutableMap()

    var supernode = mutableNodeConnections.keys.first()

    while (mutableNodeConnections.size > 2) {
        val heaviestConnectedNode = findHeaviestConnection(supernode, mutableNodeConnections)

        supernode = joinNodes(supernode, heaviestConnectedNode, mutableNodeConnections, ',')
    }

    return Triple(
        supernode,
        mutableNodeConnections.keys.first { it != supernode },
        mutableNodeConnections[supernode]!!.first().second
    )
}

fun getStartingNodeConnections(wires: List<Wire>): Map<String, Set<Pair<String, Int>>> {
    val rawNodes = wires.flatMap { listOf(it.v1, it.v2) }.toSet()

    val nodeConnections = mutableMapOf<String, Set<Pair<String, Int>>>()

    rawNodes.forEach { node ->
        val connectedTo = wires.mapNotNull {
            if (it.v1 == node) it.v2
            else if (it.v2 == node) it.v1
            else null
        }

        nodeConnections[node] = connectedTo.map { Pair(it, 1) }.toSet()
    }

    return nodeConnections
}

fun getCorrectCut(startingNodeConnections: Map<String, Set<Pair<String, Int>>>): Pair<String, String>? {
    val nodeConnections = startingNodeConnections.toMutableMap()

    while (nodeConnections.size > 1) {
        val minCut = findCut(nodeConnections)

        val node1 = nodeConnections.keys.first { minCut.first.endsWith(it) }
        val node2 = nodeConnections.keys.first { minCut.second.endsWith(it) }

        joinNodes(node1, node2, nodeConnections, '-')

        if (minCut.third == 3) {
            return Pair(minCut.first, minCut.second)
        }
    }
    return null
}

fun main() {
    val lines = Input.read("input.txt")
    val wires = parseWires(lines)

    val nodeConnections = getStartingNodeConnections(wires)

    val correctCut = getCorrectCut(nodeConnections)
    if (correctCut == null) {
        println("No solution found")
        return
    }

    println(
        correctCut.first.split(",", "-").size *
        correctCut.second.split(",", "-").size
    )
}