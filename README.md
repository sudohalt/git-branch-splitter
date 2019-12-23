# Git Branch Splitter

Simple tool to split a branch into multiple sub branches.

## Idea

This tool was created to simplify code reviews by splitting large branches into smaller branches.

## Usage 

### Create the sub branches
```git-branch-splitter create -f file.yml```


### Update the sub branches
```git-branch-splitter update -f file.yml```

### Delete the sub branches
```git-branch-splitter delete -f file.yml```

### Yaml file format

```
againstMaster: true
rootBranch: "test" 
subBranches:
  - subBranch: "sbtFiles"
    files: 
      - "build.sbt"
      - "project/plugins.sbt"  
  - subBranch: "util"
    files:
      - "src/main/scala/com/sudohalt/YamlReader.scala"
      - "src/main/scala/com/sudohalt/Splitter.scala"
  - subBranch: "command-line"
    files:
      - "src/main/scala/com/sudohalt/Cli.scala"
```

#### againstMaster

This config indicates whether to create sub branches against `master` (if set to `true`), or against a new blank root branch (if set to `false`).  If `false` a new root branch will be created (`<name of root>-copy`).

#### rootBranch

Specifies the root branch to split (root branch will not be affected).

#### subBranches

List of sub branches to create.  For each sub branch the name of the sub branch (`subBranch`) and the list of the paths to files (`files`) from the root branch that it will take commits from should be specified.

## Build and run

### Building
```sbt compile assembly```

### Running
```java -jar <path to jar> <option> -f <yaml file>```

alternatively
```chmod +x <path to jar>```
```./<path to jar> <option> -f <yaml file>```
