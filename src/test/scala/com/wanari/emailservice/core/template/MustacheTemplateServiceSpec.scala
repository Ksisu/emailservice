package com.wanari.emailservice.core.template

import java.io.{File, PrintWriter}
import java.nio.file.Files

import com.wanari.emailservice.TestBase
import com.wanari.emailservice.core.testutil.DummyConfigService

import scala.concurrent.ExecutionContext.Implicits.global

class MustacheTemplateServiceSpec extends TestBase {

  val templateDir = Files.createTempDirectory("").toAbsolutePath.toString

  def createTemplateFiles(templates: Map[String, String]): Unit = {
    templates.foreach {
      case (filename, text) =>
        val pw = new PrintWriter(new File(templateDir, filename))
        pw.write(text)
        pw.close()
    }
  }

  createTemplateFiles(
    Map(
      "example-title.mustache"  -> "Hello!",
      "example2-title.mustache" -> "Hello {{name}}!",
      "example-body.mustache"   -> "!Hello!",
      "example2-body.mustache"  -> """Hello {{name}}!
        |You have just won {{value}}!
        |{{#in_ca}}
        |Well, {{taxed_value}}, after taxes.
        |{{/in_ca}}
        |{{#repo}}
        |  <b>{{name}}</b>
        |{{/repo}}
        |""".stripMargin
    )
  )

  implicit lazy val config = new DummyConfigService {
    override def getTemplateDir: String = templateDir
  }
  lazy val service = new MustacheTemplateService()

  "MustacheTemplateService" should {
    "render title" should {
      "use -title.mustache postfix" in {
        await(service.renderTitle("example", Map.empty)) shouldEqual "Hello!"
      }
      "render mustache template simple" in {
        await(service.renderTitle("example2", Map("name" -> "Tutelar"))) shouldEqual "Hello Tutelar!"
      }
    }
    "render body" should {
      "use -body.mustache postfix" in {
        await(service.renderBody("example", Map.empty)) shouldEqual "!Hello!"
      }
      "render mustache template full" in {
        val params = Map(
          "name"  -> "Tutelar",
          "value" -> 999,
          "in_ca" -> false,
          "repo" -> List(
            Map("name" -> "a1"),
            Map("name" -> "a2"),
            Map("name" -> "a3")
          )
        )
        await(service.renderBody("example2", params)) shouldEqual """Hello Tutelar!
                                                                               |You have just won 999!
                                                                               |<b>a1</b>
                                                                               |<b>a2</b>
                                                                               |<b>a3</b>
                                                                               |""".stripMargin
      }
    }
  }
}
