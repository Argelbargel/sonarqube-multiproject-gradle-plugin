package argelbargel.gradle.plugins.sonarqube

import org.junit.Test

import static java.util.Collections.emptyMap
import static org.junit.Assert.*

class SonarqubeMultiProjectPropertiesAdapterTest {
    @Test
    void adaptEmptyProperties() {
        def empty = emptyMap()
        assertSame(empty, SonarqubeMultiProjectPropertiesAdapter.adapt(empty))
        assertTrue(empty.isEmpty())
    }

    @Test
    void adaptPropertiesForProjectWithoutModules() {
        def properties = [
                'sonar.projectName': 'test',
                'sonar.projectKey': 'test',
                'sonar.sources': 'src/main/java',
                'sonar.tests': 'src/test/java'
        ]

        def input = (Map<String, Object>) properties.clone()
        def adapted = SonarqubeMultiProjectPropertiesAdapter.adapt(input)
        assertSame(input, adapted)
        assertEquals(properties, adapted)
    }

    @Test
    void adaptPropertiesForProjectWithoutSources() {
        def properties = [
                'sonar.projectName': 'test',
                'sonar.projectKey': 'test',
                'sonar.modules': ':module',
                ':module.sonar.sources': 'src/main/java',
                ':module.sonar.tests': 'src/test/java'
        ]

        def input = (Map<String, Object>) properties.clone()
        def adapted = SonarqubeMultiProjectPropertiesAdapter.adapt(input)
        assertSame(input, adapted)
        assertEquals(properties, adapted)
    }

    @Test
    void adaptPropertiesForMultiModuleProjectWithoutRootModuleName() {
        testAdaptPropertiesForMultiModuleProject()
    }

    @Test
    void adaptPropertiesForMultiModuleProjectWithRootModuleNameWithoutSeparator() {
        testAdaptPropertiesForMultiModuleProject('main')
    }

    @Test
    void adaptPropertiesForMultiModuleProjectWithRootModuleNameWithSeparator() {
        testAdaptPropertiesForMultiModuleProject(':main')
    }

    private static void testAdaptPropertiesForMultiModuleProject(String rootModuleName = '') {
        Map<String, String> properties = createMultiModuleProjectProperties(rootModuleName)
        Map<String, String> expected = createAdaptedMultiProjectProperties(rootModuleName)

        def input = (Map<String, Object>) properties.clone()
        def adapted = SonarqubeMultiProjectPropertiesAdapter.adapt(input)
        assertPropertiesAdaptedAndInputNotModified(expected, adapted, input, properties)
    }

    private static void assertPropertiesAdaptedAndInputNotModified(Map<String, String> expected, Map<String, Object> adapted, Map<String, Object> input, Map<String, String> properties) {
        assertMapsHaveSameKeysAndValues(expected, adapted)
        assertNotSame(input, adapted)
        assertMapsHaveSameKeysAndValues(properties, input)
    }

    private static Map<String, String> createAdaptedMultiProjectProperties(rootModuleName = '') {
        String rmn = rootModuleName != '' ? rootModuleName : 'test'
        String rmk = rmn.startsWith(":") ? rmn : ":" + rmn

        def expected = [
                'sonar.projectName'               : 'test',
                'sonar.projectKey'                : 'test',
                'sonar.projectBaseDir'            : 'test/base',
                (rmk + '.sonar.projectBaseDir')   : 'test/base',
                (rmk + '.sonar.projectName')         : rmn,
                (rmk + '.sonar.moduleKey')           : 'test' + rmk,
                (rmk + '.sonar.sources')             : 'src/main',
                (rmk + '.sonar.tests')               : 'src/test',
                (rmk + '.sonar.sourceEncoding')      : 'UTF-8',
                (rmk + '.sonar.libraries')           : 'libs',
                (rmk + '.sonar.java.libraries')      : 'libs/java',
                (rmk + '.sonar.java.test.libraries') : 'libs/test',
                (rmk + '.sonar.binaries')            : 'classes',
                (rmk + '.sonar.java.binaries')       : 'classes/java',
                (rmk + '.sonar.java.test.binaries')  : 'classes/test',
                (rmk + '.sonar.java.source')         : '1.8',
                (rmk + '.sonar.java.target')         : '1.9',
                (rmk + '.sonar.junit.reportsPath')   : 'reports/junit',
                (rmk + '.sonar.surefire.reportsPath'): 'reports/surefire',
                (rmk + '.sonar.jacoco.reportPath')   : 'reports/jacoco',
                (rmk + '.sonar.test.exclusions')     : '**/*.ignored',
                'sonar.modules'                   : rmk + ',:module',
                ':module.sonar.sources'           : 'module/src'
        ]
        expected
    }

    private static Map<String, String> createMultiModuleProjectProperties(rootModuleName = '') {
        def properties = [
                'sonar.projectName'         : 'test',
                'sonar.projectKey'          : 'test',
                'sonar.projectBaseDir'      : 'test/base',
                'sonar.sources'             : 'src/main',
                'sonar.tests'               : 'src/test',
                'sonar.sourceEncoding'      : 'UTF-8',
                'sonar.libraries'           : 'libs',
                'sonar.java.libraries'      : 'libs/java',
                'sonar.java.test.libraries' : 'libs/test',
                'sonar.binaries'            : 'classes',
                'sonar.java.binaries'       : 'classes/java',
                'sonar.java.test.binaries'  : 'classes/test',
                'sonar.java.source'         : '1.8',
                'sonar.java.target'         : '1.9',
                'sonar.junit.reportsPath'   : 'reports/junit',
                'sonar.surefire.reportsPath': 'reports/surefire',
                'sonar.jacoco.reportPath'   : 'reports/jacoco',
                'sonar.test.exclusions'     : '**/*.ignored',
                'sonar.modules'             : ':module',
                ':module.sonar.sources'     : 'module/src'
        ]

        if (rootModuleName != '') {
            properties['sonar.rootModuleName'] = rootModuleName
        }

        return properties
    }

    private static void assertMapsHaveSameKeysAndValues(Map a, Map b) {
        assertEquals(new TreeMap(a), new TreeMap(b))
    }
}
