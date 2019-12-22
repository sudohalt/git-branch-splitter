package com.sudohalt

import sys.process._
import scala.jdk.CollectionConverters._

object Splitter {

  /**
   * Output error message and exit the program.
   *
   * @param message error message to display to console before exiting
   */
  def err(message: String) {
    println(message)
    System.exit(1)
  }

  /**
   * Create sub branches against master or new root branch.  For each sub branch
   * this method will get all the commits for each file it is tracking from the
   * root branch.
   *
   * @param branchToSplit Object containing details about the sub branches to create.
   */
  def createAllSubBranches(branchToSplit: BranchToSplit) {
    val branchToCheckout = if (branchToSplit.againstMaster) {
      "master"
    } else {
      println("Create new root branch...")
      val newRootBranch = branchToSplit.rootBranch + "-copy"
      val checkoutExitCode = s"git checkout master".!
      if (checkoutExitCode != 0) err(s"received following error while checking out master before creating new root branch: $checkoutExitCode")

      val newRootBranchExitCode = s"git branch ${newRootBranch}".!
      if (newRootBranchExitCode != 0) err(s"received following error code while creating new root branch: $newRootBranchExitCode")
      newRootBranch
    }

    branchToSplit.subBranches.asScala.foreach { subBranch =>
      println(s"Checking out $branchToCheckout...")
      val checkoutExitCode = s"git checkout $branchToCheckout".!
      if (checkoutExitCode != 0) err(s"received following error code while checking out $branchToCheckout: $checkoutExitCode")
      createSubBranch(branchToSplit.rootBranch, subBranch)
    }
  }

  /**
   * Create sub branch.
   *
   * @param rootBranch The root branch to retrieve commits from.
   * @param subBranch  The sub branch to create.
   */
  def createSubBranch(rootBranch: String, subBranch: SubBranch) {

    val branchCreateExitCode = s"git branch ${subBranch.subBranch}".!
    if (branchCreateExitCode != 0) err(s"received following error code while creating sub-branch: $branchCreateExitCode")

    println("Checkout sub-branch....")
    val checkoutBranchExitCode = s"git checkout ${subBranch.subBranch}".!
    if (checkoutBranchExitCode != 0) err(s"received following error code during sub-branch checkout: $checkoutBranchExitCode")

    println("Checkout files from root branch....")
    val checkoutRootBranchFileExitCode = s"git checkout ${rootBranch} ${subBranch.files.asScala.mkString(" ")}".!
    if (checkoutRootBranchFileExitCode != 0) err(s"received following error code while root branch to sub-branch checkout: $checkoutRootBranchFileExitCode")

    println("Add files....")
    val addFilesToSubBranchExitCode = s"git add ${subBranch.files.asScala.mkString(" ")}".!
    if (addFilesToSubBranchExitCode != 0) err(s"received following error code while adding files to commit: $addFilesToSubBranchExitCode")

    println("Commit....")
    val commitFilesExitCode = """git commit -m "Add util file"""".!
    if (commitFilesExitCode != 0) err(s"received following error code while committing files: $commitFilesExitCode")

    println("Push upstream....")
    val pushExitCode = s"git push --set-upstream origin ${subBranch.subBranch}".!
    if (pushExitCode != 0) err(s"received following error code while pushing upstream: $pushExitCode")

  }

  /**
   * Updates all sub branches against any updates made to the root branch.  This will update
   * each sub branch only with the changes related to the files it is tracking.
   *
   * @param branchToSplit Object containing details about the sub branches to update.
   */
  def udpateSubBranches(branchToSplit: BranchToSplit) {
    branchToSplit.subBranches.asScala.foreach { subBranch =>

      if (branchToSplit.againstMaster) {
        val checkoutMaster = "git checkout master".!

      } else {
        val newRootBranch = branchToSplit.rootBranch + "-copy"
        println("Create new root branch...")
        val checkoutNewRootExitCode = s"git checkout ${newRootBranch}".!
      }

      updateSubBranch(branchToSplit.rootBranch, subBranch)
    }
  }

  /**
   * Update sub branch against any updates made to the root branch.
   *
   * @param rootBranch The root branch to retrieve updates from.
   * @param subBranch  The sub branch to update.
   */
  def updateSubBranch(rootBranch: String, subBranch: SubBranch): Unit = {

    println("Checkout sub-branch....")
    val checkoutBranchExitCode = s"git checkout ${subBranch.subBranch}".!
    if (checkoutBranchExitCode != 0) err(s"received following error code during sub-branch checkout: $checkoutBranchExitCode")

    println("Checkout files from root branch....")
    val checkoutRootBranchFileExitCode = s"git checkout ${rootBranch} ${subBranch.files.asScala.mkString(" ")}".!
    if (checkoutRootBranchFileExitCode != 0) err(s"received following error code while root branch to sub-branch checkout: $checkoutRootBranchFileExitCode")

    println("Add files....")
    val addFilesToSubBranchExitCode = s"git add ${subBranch.files.asScala.mkString(" ")}".!
    if (addFilesToSubBranchExitCode != 0) err(s"received following error code while adding files to commit: $addFilesToSubBranchExitCode")

    println("Commit....")
    val commitFilesExitCode = """git commit -m "Add util file"""".!
    //if (commitFilesExitCode != 0) err(s"received following error code while committing files: $commitFilesExitCode")

    println("Push upstream....")
    val pushExitCode = s"git push --set-upstream origin ${subBranch.subBranch}".!
    //if (pushExitCode != 0) err(s"received following error code while pushing upstream: $pushExitCode")

  }

  /**
   * Delete sub branches created by this tool locally and remotely.
   *
   * @param branchToSplit Object containing details about the sub branches to delete.
   */
  def deleteSubBranches(branchToSplit: BranchToSplit) {
    branchToSplit.subBranches.asScala.foreach { subBranch =>
      println(s"deleting local branch ${subBranch.subBranch}")
      val deleteLocalBranchExitCode = s"git branch --delete ${subBranch.subBranch}".!
      if (deleteLocalBranchExitCode != 0) err(s"received following error code while deleting local branch ${subBranch.subBranch}: $deleteLocalBranchExitCode")

      println(s"deleting remote branch ${subBranch.subBranch}")
      val deleteRemoteBranchExitCode = s"git push origin --delete ${subBranch.subBranch}".!
      if (deleteRemoteBranchExitCode != 0) err(s"received following error code while deleting remote branch ${subBranch.subBranch}: $deleteRemoteBranchExitCode")
    }
  }

}
