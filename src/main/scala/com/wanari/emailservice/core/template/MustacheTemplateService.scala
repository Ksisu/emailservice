package com.wanari.emailservice.core.template

import java.io.{File, PrintWriter, StringWriter}

import com.wanari.emailservice.core.config.ConfigService
import org.fusesource.scalate.{DefaultRenderContext, TemplateEngine}

import scala.concurrent.{ExecutionContext, Future}

class MustacheTemplateService(implicit ec: ExecutionContext, config: ConfigService) extends TemplateService[Future] {

  private val engine: TemplateEngine = createTemplateEngine()

  override def renderTitle(templateId: String, arguments: Map[String, Any]): Future[String] = {
    render(s"$templateId-title.mustache", arguments)
  }
  override def renderBody(templateId: String, arguments: Map[String, Any]): Future[String] = {
    render(s"$templateId-body.mustache", arguments)
  }

  private def render(templateId: String, arguments: Map[String, Any]): Future[String] = {
    Future {
      val out = new StringWriter()

      val context = new DefaultRenderContext(templateId, engine, new PrintWriter(out))
      arguments.foreach {
        case (k, v) => context.attributes(k) = v
      }

      val template = engine.load(templateId)
      engine.layout(template, context)

      out.toString
    }
  }

  private def createTemplateEngine(): TemplateEngine = {
    val sourceDir = Seq(new File(config.getTemplateDir))
    val mode      = "production"
    TemplateEngine(sourceDir, mode)
  }
}
