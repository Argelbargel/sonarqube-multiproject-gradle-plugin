package argelbargel.gradle.plugins.sonarqube

import groovy.transform.PackageScope


@PackageScope
class SonarqubeMultiProjectPropertiesAdapter {
    private static final String PROPERTY_PREFIX = 'sonar.'
    private static final String PROPERTY_MODULES = PROPERTY_PREFIX + 'modules'
    private static final String PROPERTY_BASE_DIR = PROPERTY_PREFIX + 'projectBaseDir'
    private static final String PROPERTY_PROJECT_KEY = PROPERTY_PREFIX + 'projectKey'
    private static final String PROPERTY_PROJECT_NAME = PROPERTY_PREFIX + 'projectName'
    private static final String PROPERTY_MODULE_KEY = PROPERTY_PREFIX + 'moduleKey'
    private static final String PROPERTY_ROOTMODULE_NAME = PROPERTY_PREFIX + 'rootModuleName'
    private static final List<String> PROPERTIES_SOURCES = ['sources', 'tests']
    private static final List<String> PROPERTIES_TO_MOVE = PROPERTIES_SOURCES + [
            'sourceEncoding',
            'libraries', 'java.libraries', 'java.test.libraries',
            'binaries', 'java.binaries', 'java.test.binaries',
            'java.source', 'java.target',
            'junit.reportsPath', 'surefire.reportsPath', 'jacoco.reportPath',
            'test.exclusions'
    ]

    static Map<String, Object> adapt(Map<String, Object> properties) {
        if (!hasModules(properties) || !hasProjectSources(properties)) {
            return properties
        }

        Map<String, Object> adapted = new HashMap<>(properties)
        String rootModule = addRootModule(adapted)
        PROPERTIES_TO_MOVE.each { movePropertyToModule(adapted, prefixedProperty(it), rootModule) }

        return adapted
    }

    private static boolean hasModules(Map<String, Object> properties) {
        return hasProperty(properties, PROPERTY_MODULES)
    }

    private static boolean hasProjectSources(Map<String, Object> properties) {
        return PROPERTIES_SOURCES.inject(false, { result, property -> result | hasProperty(properties, property) })
    }

    private static String addRootModule(Map<String, Object> properties) {
        String rootModuleName = properties.containsKey(PROPERTY_ROOTMODULE_NAME) ? properties.remove(PROPERTY_ROOTMODULE_NAME) : properties.get(PROPERTY_PROJECT_NAME)
        String rootModuleKey = rootModuleName.startsWith(":") ? rootModuleName : ":" + rootModuleName
        properties.put(PROPERTY_MODULES, rootModuleKey + "," + properties.get(PROPERTY_MODULES))
        properties.put(rootModuleKey + "." + PROPERTY_PROJECT_NAME, rootModuleName)
        properties.put(rootModuleKey + "." + PROPERTY_MODULE_KEY, properties.get(PROPERTY_PROJECT_KEY) + rootModuleKey)
        properties.put(rootModuleKey + "." + PROPERTY_BASE_DIR, properties.get(PROPERTY_BASE_DIR))
        return rootModuleKey
    }

    private static boolean hasProperty(Map<String, Object> properties, String name) {
        return properties.containsKey(prefixedProperty(name)) && properties.get(prefixedProperty(name)) != ""
    }

    private static String prefixedProperty(String name, String module = "") {
        String property =  name.startsWith(PROPERTY_PREFIX) ? name : PROPERTY_PREFIX + name
        return (module != "") ? module + "." + property : property
    }

    private static movePropertyToModule(Map<String, Object> properties, String property, String module) {
        if (properties.containsKey(property)) {
            properties.put(prefixedProperty(property, module), properties.remove(property))
        }
    }
}
