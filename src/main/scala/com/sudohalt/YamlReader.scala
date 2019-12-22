package com.sudohalt

import java.io.{File, FileInputStream}

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import scala.beans.BeanProperty

class SubBranch {
  @BeanProperty var subBranch: String = _
  @BeanProperty var files = new java.util.ArrayList[String]()
}

class BranchToSplit {
  @BeanProperty var againstMaster: Boolean = _
  @BeanProperty var rootBranch: String = _
  @BeanProperty var subBranches = new java.util.ArrayList[SubBranch]()
}

object YamlReader {

  def fromYamlFile(fileName: String): BranchToSplit = {
    val yaml = new Yaml(new Constructor(classOf[BranchToSplit]))
    yaml.load(new FileInputStream(new File(fileName))).asInstanceOf[BranchToSplit]
  }

}
