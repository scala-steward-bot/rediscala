package redis.api.sortedsets

import redis.RediscalaCompat.util.ByteString
import redis.*

case class Zrank[K, V](key: K, member: V)(implicit keySeria: ByteStringSerializer[K], convert: ByteStringSerializer[V])
    extends SimpleClusterKey[K]
    with RedisCommandRedisReplyOptionLong {
  def isMasterOnly = false
  val encodedRequest: ByteString = encode("ZRANK", Seq(keyAsString, convert.serialize(member)))
}