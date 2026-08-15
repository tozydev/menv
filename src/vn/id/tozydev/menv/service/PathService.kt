package vn.id.tozydev.menv.service

import vn.id.tozydev.menv.model.AddResult
import vn.id.tozydev.menv.model.PathList
import vn.id.tozydev.menv.model.RemoveResult

class PathService(private val env: EnvironmentVariables) {

    fun add(dir: String, prepend: Boolean): Result<AddResult> =
        env.getPath()
            .map { PathList(it).add(dir, prepend) }
            .mapCatching { result ->
                if (result.changed) env.setPath(result.raw).getOrThrow()
                result
            }

    fun remove(dir: String): Result<RemoveResult> =
        env.getPath()
            .map { PathList(it).remove(dir) }
            .mapCatching { result ->
                if (result.changed) env.setPath(result.raw).getOrThrow()
                result
            }
}
