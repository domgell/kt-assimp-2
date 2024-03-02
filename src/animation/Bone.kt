package animation

import org.joml.Matrix4fc

data class Bone(
    val name: String,
    val index: Int,
    val parent: String?,
    val children: List<String>,
    val boneTransform: Matrix4fc,
    val nodeTransform: Matrix4fc
)
