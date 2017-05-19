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

}
