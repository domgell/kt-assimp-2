package model

import animation.Animation
import animation.Bone
import org.joml.Matrix4fc

data class StaticModel(val name: String, val meshes: List<Mesh>, val globalInverse: Matrix4fc)

data class AnimatedModel(
    val name: String,
    val meshes: List<AnimatedMesh>,
    val globalInverse: Matrix4fc,
    val bones: Map<String, Bone>,
    val animations: List<Animation>
)