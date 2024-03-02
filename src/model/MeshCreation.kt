package model

import animation.Bone
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.joml.Vector4i
import org.lwjgl.assimp.AIBone
import org.lwjgl.assimp.AIMesh
import utility.set

private fun extractVertexData(aiMesh: AIMesh): VertexData {
    val positions = aiMesh.mVertices().map { Vector3f(it.x(), it.y(), it.z()) }
    val uv = aiMesh.mTextureCoords(0)?.map { Vector2f(it.x(), it.y()) }
    val normals = aiMesh.mNormals()?.map { Vector3f(it.x(), it.y(), it.z()) }
    val colors = aiMesh.mColors(0)?.map { Vector4f(it.r(), it.g(), it.b(), it.a()) }

    return VertexData(positions, uv, normals, colors)
}

fun createMesh(aiMesh: AIMesh): Mesh {
    val name = aiMesh.mName().dataString()

    val indices = aiMesh.mFaces().flatMap { List(it.mNumIndices()) { i -> it.mIndices()[i] } }.toIntArray()
    val vertexData = extractVertexData(aiMesh)

    return Mesh(name, indices, vertexData)
}

fun createAnimatedMesh(aiMesh: AIMesh, boneMap: Map<String, Bone>): AnimatedMesh {
    val (name, indices, vertexData) = createMesh(aiMesh)
    val numVertices = vertexData.positions.size

    val vertexWeights = List(numVertices) { Vector4f(0f) }
    val boneInfluences = List(numVertices) { Vector4i(-1) }
    val numBoneInfluences = IntArray(numVertices)

    val aiBones = List(aiMesh.mNumBones()) { AIBone.create(aiMesh.mBones()!![it]) }

    aiBones.forEach { aiBone ->
        // Must use absolute boneIndex when dealing with multiple meshes
        val boneIndex = boneMap[aiBone.mName().dataString()]?.index
            ?: throw IllegalArgumentException("Invalid bone map")

        aiBone.mWeights().forEach { aiWeight ->
            val vertexID = aiWeight.mVertexId()
            val influenceIndex = numBoneInfluences[vertexID]
            numBoneInfluences[vertexID]++

            if (influenceIndex > 4)
                throw IllegalStateException("'${name}' has more than 4 bone influences on vertex $vertexID")

            vertexWeights[vertexID][influenceIndex] = aiWeight.mWeight()
            boneInfluences[vertexID][influenceIndex] = boneIndex
        }
    }

    return AnimatedMesh(name, indices, vertexData, AnimationVertexData(vertexWeights, boneInfluences))
}