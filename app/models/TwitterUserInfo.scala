package models

case class TwitterUserInfo(followerCount: Int, isProtected: Boolean)

import play.api.libs.json.Json

import com.github.tototoshi.play.json.JsonNaming

object TwitterUserInfo {
  implicit val TwitterUserInfoFormat = JsonNaming.snakecase(Json.format[TwitterUserInfo])
}