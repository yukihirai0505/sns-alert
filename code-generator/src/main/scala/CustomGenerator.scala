import slick.codegen.SourceCodeGenerator
import slick.{model => m}

class CustomGenerator(model: m.Model) extends SourceCodeGenerator(model) {
  override def Table = new Table(_) {
    override def autoIncLastAsOption = true
  }
}