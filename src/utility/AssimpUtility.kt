package utility

import animation.Keyframe
import org.joml.*
import org.lwjgl.assimp.AIMatrix4x4
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AIQuatKey
import org.lwjgl.assimp.AIVectorKey

internal fun AIMatrix4x4.toMatrix4fc(): Matrix4fc {
    return Matrix4f(
        a1(), b1(), c1(), d1(),
        a2(), b2(), c2(), d2(),
        a3(), b3(), c3(), d3(),
        a4(), b4(), c4(), d4()
    )
}

internal fun AINode.traverse(func: (AINode) -> Unit) {
    func(this)
    for (i in 0 ..<mNumChildren())
        func(AINode.create(mChildren()!![i]))
}

internal operator fun Vector4f.set(i: Int, value: Float) {
    when (i) {
        0 -> x = value
        1 -> y = value
        2 -> z = value
        3 -> w = value
    }
}

internal operator fun Vector4i.set(i: Int, value: Int) {
    when (i) {
        0 -> x = value
        1 -> y = value
        2 -> z = value
        3 -> w = value
    }
}

internal fun AIVectorKey.toKeyframe(): Keyframe<Vector3fc> =
    Keyframe(this.mTime().toFloat() / 1000f, Vector3f(this.mValue().x(), this.mValue().y(), this.mValue().z()))

internal fun AIQuatKey.toKeyframe(): Keyframe<Quaternionfc> =
    Keyframe(
        this.mTime().toFloat() / 1000f,
        Quaternionf(this.mValue().x(), this.mValue().y(), this.mValue().z(), this.mValue().w())
    )
