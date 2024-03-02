package importer

import animation.createAnimation
import animation.createBoneHierarchy
import model.AnimatedModel
import model.StaticModel
import model.createAnimatedMesh
import model.createMesh
import org.lwjgl.assimp.Assimp
import utility.assimpScene

object ModelImporter {
    fun staticModel(file: String): StaticModel {
        val aiScene = Assimp.aiImportFile(file, FLAGS)
            ?: throw IllegalArgumentException("Could not import '$file'")

        val scene = aiScene.assimpScene()
        val meshes = scene.meshes.map { createMesh(it) }

        return StaticModel(scene.name, meshes, scene.globalInverse)
    }

    fun animatedModel(file: String): AnimatedModel {
        val aiScene = Assimp.aiImportFile(file, FLAGS)
            ?: throw IllegalArgumentException("Could not import '$file'")

        val scene = aiScene.assimpScene()

        if (scene.bones == null)
            throw IllegalArgumentException("'$file' has no animation data")

        val bones = createBoneHierarchy(scene.bones)

        val meshes = scene.meshes.map { createAnimatedMesh(it, bones) }
        val animations = scene.animations.map { createAnimation(it, bones) }

        return AnimatedModel(scene.name, meshes, scene.globalInverse, bones, animations)
    }

    private const val FLAGS = Assimp.aiProcess_Triangulate or Assimp.aiProcess_LimitBoneWeights or
            Assimp.aiProcess_JoinIdenticalVertices or Assimp.aiProcess_PopulateArmatureData
}