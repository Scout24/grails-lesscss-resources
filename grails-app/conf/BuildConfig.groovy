grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.repos.artifactory.url = "http://deviar10.rz.is/artifactory/simple/libs-release-local"
grails.project.repos.default = "artifactory"
grails.release.scm.enabled = false

// !!! ATTENTION - Do NOT remove the quotation marks because variable replacing does not work then anymore !!!
grails.project.war.file = "target/${appName}.war"
grails.server.port.http = 9010

coverage {
  enabledByDefault = false
  xml = true
}

grails.project.dependency.resolution = {
  checksums false
  pom true

  // inherit Grails' default dependencies
  inherits("global") {
  }

  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

  repositories {
    inherit false
    mavenRepo name: "artifactory", root: "http://deviar10.rz.is/artifactory/all"
  }

  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    runtime 'org.lesscss:lesscss:1.3.3'
    test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    test ("org.codehaus.geb:geb-spock:0.7.0") {
      export = false
    }
    test ('org.gmock:gmock:0.8.2') {
      export = false
    }
  }

  plugins {
    compile(':resources:1.2.RC2') { export = false }
    build(":release:$grailsVersion") { export = false }
    test(":spock:0.7") {
      exclude "spock-grails-support"
    }
    compile (":tomcat:$grailsVersion") { export = false }
  }
}
