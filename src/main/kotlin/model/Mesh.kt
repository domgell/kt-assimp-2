package model

import org.joml.Vector2fc
import org.joml.Vector3fc
import org.joml.Vector4fc
import org.joml.Vector4ic

data class Mesh(val name: String, val indices: IntArray, val vertexData: VertexData)

data class VertexData(
    val positions: List<Vector3fc>,
    val uv: List<Vector2fc>?,
    val normals: List<Vector3fc>?,
    val colors: List<Vector4fc>?
)

data class AnimatedMesh(
    val name: String,
    val indices: IntArray,
    val vertexData: VertexData,
    val animationVertexData: AnimationVertexData
)

data class AnimationVertexData(val vertexWeights: List<Vector4fc>, val boneInfluences: List<Vector4ic>)