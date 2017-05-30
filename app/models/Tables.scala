package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = ReserveInstagramPost.schema ++ SplashPost.schema ++ User.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table ReserveInstagramPost
   *  @param username Database column username SqlType(varchar), Length(100,true)
   *  @param password Database column password SqlType(varchar), Length(100,true)
   *  @param caption Database column caption SqlType(varchar), Length(500,true)
   *  @param filename Database column filename SqlType(varchar), Length(500,true)
   *  @param postDatetime Database column post_datetime SqlType(timestamp)
   *  @param reserveDatetime Database column reserve_datetime SqlType(timestamp)
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey */
  case class ReserveInstagramPostRow(username: String, password: String, caption: String, filename: String, postDatetime: DateTime, reserveDatetime: DateTime, id: Option[Int] = None)
  /** GetResult implicit for fetching ReserveInstagramPostRow objects using plain SQL queries */
  implicit def GetResultReserveInstagramPostRow(implicit e0: GR[String], e1: GR[DateTime], e2: GR[Option[Int]]): GR[ReserveInstagramPostRow] = GR{
    prs => import prs._
    val r = (<<?[Int], <<[String], <<[String], <<[String], <<[String], <<[DateTime], <<[DateTime])
    import r._
    ReserveInstagramPostRow.tupled((_2, _3, _4, _5, _6, _7, _1)) // putting AutoInc last
  }
  /** Table description of table reserve_instagram_post. Objects of this class serve as prototypes for rows in queries. */
  class ReserveInstagramPost(_tableTag: Tag) extends Table[ReserveInstagramPostRow](_tableTag, "reserve_instagram_post") {
    def * = (username, password, caption, filename, postDatetime, reserveDatetime, Rep.Some(id)) <> (ReserveInstagramPostRow.tupled, ReserveInstagramPostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), Rep.Some(password), Rep.Some(caption), Rep.Some(filename), Rep.Some(postDatetime), Rep.Some(reserveDatetime), Rep.Some(id)).shaped.<>({r=>import r._; _1.map(_=> ReserveInstagramPostRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(varchar), Length(100,true) */
    val username: Rep[String] = column[String]("username", O.Length(100,varying=true))
    /** Database column password SqlType(varchar), Length(100,true) */
    val password: Rep[String] = column[String]("password", O.Length(100,varying=true))
    /** Database column caption SqlType(varchar), Length(500,true) */
    val caption: Rep[String] = column[String]("caption", O.Length(500,varying=true))
    /** Database column filename SqlType(varchar), Length(500,true) */
    val filename: Rep[String] = column[String]("filename", O.Length(500,varying=true))
    /** Database column post_datetime SqlType(timestamp) */
    val postDatetime: Rep[DateTime] = column[DateTime]("post_datetime")
    /** Database column reserve_datetime SqlType(timestamp) */
    val reserveDatetime: Rep[DateTime] = column[DateTime]("reserve_datetime")
    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table ReserveInstagramPost */
  lazy val ReserveInstagramPost = new TableQuery(tag => new ReserveInstagramPost(tag))

  /** Entity class storing rows of table SplashPost
   *  @param userId Database column user_id SqlType(int8)
   *  @param postId Database column post_id SqlType(varchar), Length(100,true)
   *  @param message Database column message SqlType(varchar), Length(500,true)
   *  @param link Database column link SqlType(varchar), Length(500,true)
   *  @param snsType Database column sns_type SqlType(int4)
   *  @param postDatetime Database column post_datetime SqlType(timestamp)
   *  @param splashDatetime Database column splash_datetime SqlType(timestamp)
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey */
  case class SplashPostRow(userId: Long, postId: String, message: String, link: String, snsType: Int, postDatetime: DateTime, splashDatetime: DateTime, id: Option[Int] = None)
  /** GetResult implicit for fetching SplashPostRow objects using plain SQL queries */
  implicit def GetResultSplashPostRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[DateTime], e4: GR[Option[Int]]): GR[SplashPostRow] = GR{
    prs => import prs._
    val r = (<<?[Int], <<[Long], <<[String], <<[String], <<[String], <<[Int], <<[DateTime], <<[DateTime])
    import r._
    SplashPostRow.tupled((_2, _3, _4, _5, _6, _7, _8, _1)) // putting AutoInc last
  }
  /** Table description of table splash_post. Objects of this class serve as prototypes for rows in queries. */
  class SplashPost(_tableTag: Tag) extends Table[SplashPostRow](_tableTag, "splash_post") {
    def * = (userId, postId, message, link, snsType, postDatetime, splashDatetime, Rep.Some(id)) <> (SplashPostRow.tupled, SplashPostRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(postId), Rep.Some(message), Rep.Some(link), Rep.Some(snsType), Rep.Some(postDatetime), Rep.Some(splashDatetime), Rep.Some(id)).shaped.<>({r=>import r._; _1.map(_=> SplashPostRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column post_id SqlType(varchar), Length(100,true) */
    val postId: Rep[String] = column[String]("post_id", O.Length(100,varying=true))
    /** Database column message SqlType(varchar), Length(500,true) */
    val message: Rep[String] = column[String]("message", O.Length(500,varying=true))
    /** Database column link SqlType(varchar), Length(500,true) */
    val link: Rep[String] = column[String]("link", O.Length(500,varying=true))
    /** Database column sns_type SqlType(int4) */
    val snsType: Rep[Int] = column[Int]("sns_type")
    /** Database column post_datetime SqlType(timestamp) */
    val postDatetime: Rep[DateTime] = column[DateTime]("post_datetime")
    /** Database column splash_datetime SqlType(timestamp) */
    val splashDatetime: Rep[DateTime] = column[DateTime]("splash_datetime")
    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table SplashPost */
  lazy val SplashPost = new TableQuery(tag => new SplashPost(tag))

  /** Entity class storing rows of table User
   *  @param email Database column email SqlType(varchar), Length(500,true)
   *  @param password Database column password SqlType(varchar), Length(1024,true)
   *  @param keywords Database column keywords SqlType(text), Default(None)
   *  @param instagramId Database column instagram_id SqlType(varchar), Length(1024,true), Default(None)
   *  @param instagramAccessToken Database column instagram_access_token SqlType(varchar), Length(100,true), Default(None)
   *  @param facebookId Database column facebook_id SqlType(varchar), Length(1024,true), Default(None)
   *  @param facebookAccessToken Database column facebook_access_token SqlType(varchar), Length(512,true), Default(None)
   *  @param twitterId Database column twitter_id SqlType(varchar), Length(1024,true), Default(None)
   *  @param twitterAccessToken Database column twitter_access_token SqlType(varchar), Length(255,true), Default(None)
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey */
  case class UserRow(email: String, password: String, keywords: Option[String] = None, instagramId: Option[String] = None, instagramAccessToken: Option[String] = None, facebookId: Option[String] = None, facebookAccessToken: Option[String] = None, twitterId: Option[String] = None, twitterAccessToken: Option[String] = None, id: Option[Int] = None)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[Int]]): GR[UserRow] = GR{
    prs => import prs._
    val r = (<<?[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String])
    import r._
    UserRow.tupled((_2, _3, _4, _5, _6, _7, _8, _9, _10, _1)) // putting AutoInc last
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "user") {
    def * = (email, password, keywords, instagramId, instagramAccessToken, facebookId, facebookAccessToken, twitterId, twitterAccessToken, Rep.Some(id)) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(email), Rep.Some(password), keywords, instagramId, instagramAccessToken, facebookId, facebookAccessToken, twitterId, twitterAccessToken, Rep.Some(id)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column email SqlType(varchar), Length(500,true) */
    val email: Rep[String] = column[String]("email", O.Length(500,varying=true))
    /** Database column password SqlType(varchar), Length(1024,true) */
    val password: Rep[String] = column[String]("password", O.Length(1024,varying=true))
    /** Database column keywords SqlType(text), Default(None) */
    val keywords: Rep[Option[String]] = column[Option[String]]("keywords", O.Default(None))
    /** Database column instagram_id SqlType(varchar), Length(1024,true), Default(None) */
    val instagramId: Rep[Option[String]] = column[Option[String]]("instagram_id", O.Length(1024,varying=true), O.Default(None))
    /** Database column instagram_access_token SqlType(varchar), Length(100,true), Default(None) */
    val instagramAccessToken: Rep[Option[String]] = column[Option[String]]("instagram_access_token", O.Length(100,varying=true), O.Default(None))
    /** Database column facebook_id SqlType(varchar), Length(1024,true), Default(None) */
    val facebookId: Rep[Option[String]] = column[Option[String]]("facebook_id", O.Length(1024,varying=true), O.Default(None))
    /** Database column facebook_access_token SqlType(varchar), Length(512,true), Default(None) */
    val facebookAccessToken: Rep[Option[String]] = column[Option[String]]("facebook_access_token", O.Length(512,varying=true), O.Default(None))
    /** Database column twitter_id SqlType(varchar), Length(1024,true), Default(None) */
    val twitterId: Rep[Option[String]] = column[Option[String]]("twitter_id", O.Length(1024,varying=true), O.Default(None))
    /** Database column twitter_access_token SqlType(varchar), Length(255,true), Default(None) */
    val twitterAccessToken: Rep[Option[String]] = column[Option[String]]("twitter_access_token", O.Length(255,varying=true), O.Default(None))
    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
