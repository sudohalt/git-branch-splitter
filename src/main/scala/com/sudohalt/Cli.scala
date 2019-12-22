package com.sudohalt

import scopt.{OptionDef, OptionParser}

sealed trait Command

case object Create extends Command

case object Update extends Command

case object Delete extends Command

case class Config(command: Option[Command] = None, yamlFile: Option[String] = None)


object Cli {

  val parser: OptionParser[Config] = new OptionParser[Config]("git-branch-splitter") {

    def yamlFileOption: OptionDef[String, Config] = opt[String]('f', "file")
      .action((x, c) => c.copy(yamlFile = Some(x)))
      .text("yaml file")

    note("Tool used to split branch into multiple sub branches.")

    version("version")

    help("help")

    cmd("split").action((_, c) => c.copy(command = Some(Create))).
      text(
        """
          |create sub branches of root branch given yaml file
          |example: git-branch-splitter create -f file.yml
        """.stripMargin).
      children(yamlFileOption)

    cmd("update").action((_, c) => c.copy(command = Some(Update))).
      text(
        """
          |update the sub branches with changes made to original root branch
          |example: git-branch-splitter update -f file.yml
        """.stripMargin).
      children(yamlFileOption)

    cmd("delete").action((_, c) => c.copy(command = Some(Delete))).
      text(
        """
          |delete sub branches (does not delete the root branch)
          |example: git-branch-splitter delete -f file.yml
        """.stripMargin).
      children(yamlFileOption)

  }

  def main(args: Array[String]) {
    println("parsed config")
    println(parser.parse(args, Config()))
    parser.parse(args, Config()) match {
      case Some(config) =>
        if (config.yamlFile.isEmpty) {
          println("please specify yaml file")
          System.exit(1)
        }

        val branchToSplit: BranchToSplit = YamlReader.fromYamlFile(config.yamlFile.get)
        config.command match {
          case Some(Create) => Splitter.createAllSubBranches(branchToSplit)
          case Some(Update) => Splitter.udpateSubBranches(branchToSplit)
          case Some(Delete) => Splitter.deleteSubBranches(branchToSplit)
        }
    }
  }

}
