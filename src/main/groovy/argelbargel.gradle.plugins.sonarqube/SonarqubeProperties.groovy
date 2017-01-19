package argelbargel.gradle.plugins.sonarqube

import groovy.transform.PackageScope


@PackageScope
final class SonarqubeProperties {
    private SonarqubeProperties() { /* no instances allowed */ }

    static final String PROPERTY_PREFIX = 'sonar.'
    static final String PROPERTY_MODULES = PROPERTY_PREFIX + 'modules'
    static final String PROPERTY_BASE_DIR = PROPERTY_PREFIX + 'projectBaseDir'
    static final String PROPERTY_PROJECT_KEY = PROPERTY_PREFIX + 'projectKey'
    static final String PROPERTY_PROJECT_NAME = PROPERTY_PREFIX + 'projectName'
    static final String PROPERTY_MODULE_KEY = PROPERTY_PREFIX + 'moduleKey'
    static final String PROPERTY_ROOTMODULE_NAME = PROPERTY_PREFIX + 'rootModuleName'
    static final List<String> PROPERTIES_SOURCES = ['sources', 'tests']
    static final List<String> PROPERTIES_TO_MOVE = PROPERTIES_SOURCES + [
            'sourceEncoding',
            'libraries', 'java.libraries', 'java.test.libraries',
            'binaries', 'java.binaries', 'java.test.binaries',
            'java.source', 'java.target',
            'junit.reportsPath', 'surefire.reportsPath', 'jacoco.reportPath',
            'inclusions', 'exclusions',
            'coverage.exclusions', 'cpd.exclusions',
            'test.inclusions', 'test.exclusions'
    ]

    static String prefixedProperty(String name, String module = "") {
        String property =  name.startsWith(PROPERTY_PREFIX) ? name : PROPERTY_PREFIX + name
        return (module != "") ? module + "." + property : property
    }
}
