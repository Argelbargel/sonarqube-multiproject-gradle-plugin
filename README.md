# SonarQube-Multi-Project-Plugin for Gradle

[![Build Status](https://travis-ci.org/Argelbargel/sonarqube-multiproject-gradle-plugin.svg?branch=master)](https://travis-ci.org/Argelbargel/sonarqube-multiproject-gradle-plugin)

**Note: since SonarQube 6.4 the scanner can analyse gradle-projects containing sources and sub-projects by itself, so there is no need to use this plugin anymore!**

This plugin extends the [SonarQube Scanner for Gradle 2.5](https://github.com/SonarSource/sonar-scanner-gradle) and allows you 
to analyze multi-module builds with SonarQube **`< Version 6.4`** even when the root-project has its own source-files (that is, it implements a fix for [SONARGRADL-5](https://jira.sonarsource.com/browse/SONARGRADL-5)).
  
See https://github.com/Argelbargel/multi-module-gradle-project-sonarqube for an example project using this plugin.  


## Documentation

### 1. Apply plugin to your project

This also applies the default `sonaqube`-plugin to your project

#### Using a `buildscript`-block and `apply` (supported by all Gradle-Versions)
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.argelbargel.gradle.plugins:sonarqube-multiproject-plugin:1.1"
  }
}

apply plugin: "argelbargel.gradle.plugins.sonarqube-multiproject-plugin"  
```

#### Using the new plugin-mechanism
 
```
plugins {
    id "argelbargel.gradle.plugins.sonarqube-multiproject-plugin" version "1.1"
}
```

#### Apply the plugin via a separate, reusable script
##### `sonarqube-multiproject.gradle`
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.argelbargel.gradle.plugins:sonarqube-multiproject-plugin:1.1"
  }
}

apply plugin: Class.forName('argelbargel.gradle.plugins.sonarqube.SonarqubeMultiProjectPlugin')
```
##### `build.gradle`
```
apply from: 'sonarqube-multiproject.gradle'
```



### 2. (Optionally) configure name of root-module

The Plugin extends the default-configuration of the `sonarqube`-plugin by adding an optional 
property for the name of the module which is created if the root-project contains its own sources.
 
```
 sonarqube {
     properties {
         // ... default sonar properties
         property 'sonar.rootModuleName', 'main' 
     }
 }
``` 

The above configuration will create a Module "main" in your Sonarqube-Server below your project which
contains the analysis of the source-files in the root-module.

If no name is configured for the root-module, the name of the root-project will be used.

### License

Licensed under the [GNU Lesser General Public License, Version 3.0](http://www.gnu.org/licenses/lgpl.txt)

