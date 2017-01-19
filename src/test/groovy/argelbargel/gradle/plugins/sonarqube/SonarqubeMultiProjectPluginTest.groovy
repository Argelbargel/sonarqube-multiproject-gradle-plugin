package argelbargel.gradle.plugins.sonarqube

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.sonarqube.gradle.SonarQubeTask

import static org.junit.Assert.assertTrue


class SonarqubeMultiProjectPluginTest {
    @Test
    void sonarqubeMultiProjectPluginAlsoAppliesSonarqubePlugin() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply SonarqubeMultiProjectPlugin

        assertTrue(project.tasks.sonarqube instanceof SonarQubeTask)
    }
}
