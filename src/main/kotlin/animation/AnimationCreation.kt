package animation

import org.lwjgl.assimp.AIAnimation
import org.lwjgl.assimp.AIBone
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AINodeAnim
import utility.toKeyframe
import utility.toMatrix4fc

fun createBoneHierarchy(aiBones: List<AIBone>): Map<String, Bone> {
    val aiBoneNames = aiBones.map { it.mName().dataString() }.toSet()

    return aiBones.mapIndexed { i, aiBone ->
        val name = aiBone.mName().dataString()
        val parentName = aiBone.mNode().mParent()?.mName()?.dataString()
        val offsetMatrix = aiBone.mOffsetMatrix().toMatrix4fc()
        val bindPoseMatrix = aiBone.mNode().mTransformation().toMatrix4fc()

        // Only include names of child nodes that are bones
        val childrenNames = aiBone.mNode().mNumChildren().let { numChildren ->
            (0..<numChildren).mapNotNull { i ->
                AINode.create(aiBone.mNode().mChildren()!![i]).mName().dataString()
                    .takeIf { childName -> childName in aiBoneNames }
            }
        }

        Bone(name, i, parentName, childrenNames, offsetMatrix, bindPoseMatrix)
    }.associateBy { it.name }
}

fun createAnimation(aiAnimation: AIAnimation, boneMap: Map<String, Bone>): Animation {
    val name = aiAnimation.mName().dataString()
    val duration = aiAnimation.mDuration().toFloat() / 1000f

    val keyframes = (0..<aiAnimation.mNumChannels()).associate { channelIndex ->
        val aiChannel = AINodeAnim.create(aiAnimation.mChannels()!![channelIndex])

        aiChannel.mNodeName().dataString() to AnimationKeyframes(
            List(aiChannel.mNumPositionKeys()) { aiChannel.mPositionKeys()!![it].toKeyframe() },
            List(aiChannel.mNumRotationKeys()) { aiChannel.mRotationKeys()!![it].toKeyframe() },
            List(aiChannel.mNumScalingKeys()) { aiChannel.mScalingKeys()!![it].toKeyframe() }
        )
    }

    return Animation(name, duration, keyframes.filter { boneMap.containsKey(it.key) })
}
