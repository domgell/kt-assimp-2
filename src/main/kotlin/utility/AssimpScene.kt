package utility

import org.joml.Matrix4fc
import org.lwjgl.assimp.*

data class AssimpScene(
    val name: String,
    val globalInverse: Matrix4fc,
    val meshes: List<AIMesh>,
    val nodes: List<AINode>,
    val bones: List<AIBone>?,
    val animations: List<AIAnimation>,
)

fun AIScene.assimpScene(): AssimpScene {
    val name = mRootNode()?.mName()?.dataString() ?: throw IllegalStateException("Scene has invalid root node")
    val globalInverse = mRootNode()!!.mTransformation().toMatrix4fc()

    val meshes = List(mNumMeshes()) { AIMesh.create(mMeshes()!![it]) }

    val nodes = ArrayList<AINode>()
    mRootNode()!!.traverse { nodes.add(it) }

    val boneMap = mutableMapOf<String, AIBone>()

    meshes.forEach { aiMesh ->
        (0..<aiMesh.mNumBones()).forEach {
            val aiBone = AIBone.create(aiMesh.mBones()!![it])
            boneMap[aiBone.mName().dataString()] = aiBone
        }
    }

    val bones = boneMap.ifEmpty { null }?.values?.toList()

    val animations = List(mNumAnimations()) { AIAnimation.create(mAnimations()!![it]) }

    return AssimpScene(name, globalInverse, meshes, nodes, bones, animations)
}
