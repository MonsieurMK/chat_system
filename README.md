# Template for Java application

This is a template repository for a Java application built with Gradle. This is designed to be used with the *git flow* Workflow.

## Table of contents

- [Gradle](#gradle)
- [GitHub Actions workflows](#github-actions-workflows)
- [Git Flow cheatsheet](#git-flow-cheatsheet)
  - [Initialization](#initialization)
  - [Adding a feature](#adding-a-feature)
  - [Creating a new release](#creating-a-new-release)
  - [Applying a hotfix](#applying-a-hotfix)

## Gradle

This template includes :
- Executable jar generation
- Javadoc generation
- OS specific installers for Windows, Ubuntu and Macos with jpackage *(Macos installers will not be generated for pre-releases with version major lower than 1)*

## GitHub Actions workflows

There are two GitHub Actions workflows :
- Develop : Build and tests on push and pull request on the *develop* branch
- Release : Build, generate and publish the Javadoc on GitHub Pages, create release/pre-release with executable jar and installers when new tags are pushed

## Git Flow cheatsheet

### Initialization

- **To initialise the Git Flow workflow** : ```git flow init```

*Set the version tag prefix to "v"*

You should now have two branches : **main** and **develop**

### Adding a feature

- **To start a feature** : ```git flow feature start feature_name```

A new branch will be created from the develop branch.

- **To finish the feature and merge to the develop branch** : ```git flow feature finish feature_name```

- **You can also push a feature to the remote repository** : ```git flow feature publish feature_name```

- **Or pull it from the remote repository** : ```git flow feature pull origin feature_name```

### Creating a new release

- **To start a new release** : ```git flow release start 1.2.3```

A new branch will be created from the develop branch with the associated tag (here with the tag 1.2.3)

- **To finish the release and merge it to both the develop and the main branch** : ```git flow release finish 1.2.3```

- **Then you can push the corresponding tag** : ```git push --tags```

*Before pushing the tag make sure all the local branches have been pushed to remote*

### Applying a hotfix

- **To start a hotfix on the main branch** : ```git flow hotfix start 1.2.4```

This will create a hotfix branch from the main branch

- **Then to finish the hotfix and merge it to both the develop and main branch** : ```git flow hotfix finish 1.2.4```
