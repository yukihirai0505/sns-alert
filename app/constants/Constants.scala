package constants

/**
  * author Yuki Hirai on 2017/05/18.
  */
object Constants {

  object SnsType extends Enumeration {

    case class SnsTypeVal(value: Byte) extends Val

    val Facebook = SnsTypeVal(1)
    val Instagram = SnsTypeVal(2)
    val Twitter = SnsTypeVal(3)

    def fromValue(value: Byte): SnsTypeVal = values.find(x => x.asInstanceOf[SnsTypeVal].value == value)
      .getOrElse(throw new RuntimeException("ConstantError: there is no such as SnsTypeVal")).asInstanceOf[SnsTypeVal]
  }

  object SplashType extends Enumeration {

    case class SplashTypeVal(value: Byte, label: String) extends Val

    val Short = SplashTypeVal(6, "6 hours")
    val Medium = SplashTypeVal(12, "12 hours")
    val Long = SplashTypeVal(24,"24 hours")

    def fromValue(value: Byte): SplashTypeVal = this.values.find(x => x.asInstanceOf[SplashTypeVal].value == value)
      .getOrElse(throw new RuntimeException("ConstantError: there is no such as SplashTypeVal")).asInstanceOf[SplashTypeVal]

    val getValues: Seq[SplashTypeVal] = this.values.map(x => x.asInstanceOf[SplashTypeVal])(collection.breakOut)
  }

}
