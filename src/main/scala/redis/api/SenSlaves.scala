package redis.api

import org.apache.pekko.util.ByteString
import redis.*
import redis.protocol.MultiBulk

case class SenSlaves(master: String) extends RedisCommandMultiBulk[Seq[Map[String, String]]] {
  def isMasterOnly = true
  val encodedRequest: ByteString = encode(s"SENTINEL SLAVES $master")

  def decodeReply(mb: MultiBulk): Seq[Map[String, String]] = MultiBulkConverter.toSeqMapString(mb)
}
