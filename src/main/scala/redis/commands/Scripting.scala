package redis.commands

import redis.*
import scala.concurrent.Future
import redis.api.scripting.*
import redis.actors.ReplyErrorException

trait Scripting extends Request {

  /**
   * Try EVALSHA, if NOSCRIPT returned, fallback to EVAL
   */
  def evalshaOrEval[R: RedisReplyDeserializer](
    redisScript: RedisScript,
    keys: Seq[String] = Seq.empty[String],
    args: Seq[String] = Seq.empty[String]
  ): Future[R] = {
    evalsha(redisScript.sha1, keys, args).recoverWith {
      case ReplyErrorException(message) if message.startsWith("NOSCRIPT") => eval(redisScript.script, keys, args)
    }
  }

  def eval[R: RedisReplyDeserializer](script: String, keys: Seq[String] = Seq.empty[String], args: Seq[String] = Seq.empty[String]): Future[R] = {
    send(Eval(script, keys, args))
  }

  def evalsha[R: RedisReplyDeserializer](sha1: String, keys: Seq[String] = Seq.empty[String], args: Seq[String] = Seq.empty[String]): Future[R] = {
    send(Evalsha(sha1, keys, args))
  }

  def scriptFlush(): Future[Boolean] = {
    send(ScriptFlush)
  }

  def scriptKill(): Future[Boolean] = {
    send(ScriptKill)
  }

  def scriptLoad(script: String): Future[String] = {
    send(ScriptLoad(script))
  }

  def scriptExists(sha1: String*): Future[Seq[Boolean]] = {
    send(ScriptExists(sha1))
  }
}
