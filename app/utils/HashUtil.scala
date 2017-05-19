package utils

import java.security.MessageDigest
import java.util.UUID

import org.joda.time.DateTime

/**
  * Created by yukihirai on 2017/03/18.
  */
object HashUtil {

  /**
    * make hash on the specified key.
    */
  def create(key: String): String = {
    val dt = new DateTime(new java.util.Date())
    MessageDigest.getInstance("MD5").digest((key + dt.getMillis.toString).getBytes).map("%02x".format(_)).mkString
  }

  /**
    * make md5 hash on the specified current datetime.
    */
  def create: String = {
    val dt = new DateTime(new java.util.Date())
    MessageDigest.getInstance("MD5").digest(dt.getMillis.toString.getBytes).map("%02x".format(_)).mkString
  }

  /**
    * make uuid
    */
  def createUuid: String = UUID.randomUUID.toString

  /**
    * make random password on digit.
    * equally chosen from A-Z, a-z, and 0-9.
    */
  def passwordGenerator(digit: Option[Int] = None): String = {
    val d = digit.getOrElse(4)
    scala.util.Random.alphanumeric.take(d).mkString
  }

  /**
    * make md5 hash, account password.
    */
  def passwordGenerator(key: String): String = {
    MessageDigest.getInstance("MD5").digest(key.getBytes).map("%02x".format(_)).mkString
  }
}