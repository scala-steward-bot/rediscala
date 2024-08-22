package redis.api.scripting

import redis.protocol.MultiBulk
import redis.*
import redis.RediscalaCompat.util.ByteString

case class ScriptExists(sha1: Seq[String]) extends RedisCommandMultiBulk[Seq[Boolean]] {
  def isMasterOnly = true
  val encodedRequest: ByteString = encode("SCRIPT", ByteString("EXISTS") +: sha1.map(ByteString(_)))

  def decodeReply(mb: MultiBulk) = MultiBulkConverter.toSeqBoolean(mb)
}
