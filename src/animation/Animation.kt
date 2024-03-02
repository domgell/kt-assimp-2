package animation

import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Quaternionfc
import org.joml.Vector3fc

data class Keyframe<T>(val time: Float, val value: T)

data class Animation(val name: String, val duration: Float, val keyframes: Map<String, AnimationKeyframes>)

data class AnimationKeyframes(
    val positionKeyframes: List<Keyframe<Vector3fc>>,
    val rotationKeyframes: List<Keyframe<Quaternionfc>>,
    val scaleKeyframes: List<Keyframe<Vector3fc>>
) {
    fun createTransformKeyframes(): List<Keyframe<Matrix4fc>> {
        // Get all unique times
        val uniqueTimes = positionKeyframes.map { it.time }
            .plus(rotationKeyframes.map { it.time })
            .plus(scaleKeyframes.map { it.time })
            .toSortedSet()

        // Create a transform keyframe at each unique time
        return uniqueTimes.map { time ->
            // Get the keyframe closest to the given time
            val position = positionKeyframes.last { it.time <= time }.value
            val rotation = rotationKeyframes.last { it.time <= time }.value
            val scale = scaleKeyframes.last { it.time <= time }.value

            Keyframe(time, Matrix4f().translate(position).rotate(rotation).scale(scale))
        }
    }
}