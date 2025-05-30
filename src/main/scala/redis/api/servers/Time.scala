package redis.api.servers

import org.apache.pekko.util.ByteString
import redis.*
import redis.protocol.MultiBulk

case object Time extends RedisCommandMultiBulk[(Long, Long)] {
  def isMasterOnly: Boolean = true
  val encodedRequest: ByteString = encode("TIME")

  def decodeReply(mb: MultiBulk): (Long, Long) = {
    mb.responses
      .map(r => {
        (r.head.toByteString.utf8String.toLong, r.tail.head.toByteString.utf8String.toLong)
      })
      .get
  }
}
