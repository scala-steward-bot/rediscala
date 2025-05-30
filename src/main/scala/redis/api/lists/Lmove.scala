package redis.api.lists

import org.apache.pekko.util.ByteString
import redis.ByteStringDeserializer
import redis.ByteStringSerializer
import redis.RedisCommandBulkOptionByteString
import redis.api.ListDirection

case class Lmove[KS, KD, R](source: KS, destination: KD, from: ListDirection, to: ListDirection)(using
  sourceSer: ByteStringSerializer[KS],
  destSer: ByteStringSerializer[KD],
  deserializerR: ByteStringDeserializer[R]
) extends RedisCommandBulkOptionByteString[R] {
  def isMasterOnly = true
  val encodedRequest: ByteString = encode(
    "LMOVE",
    Seq(
      sourceSer.serialize(source),
      destSer.serialize(destination),
      ByteString.fromString(from.value),
      ByteString.fromString(to.value),
    )
  )
  val deserializer: ByteStringDeserializer[R] = deserializerR
}
