package argelbargel.gradle.plugins.sonarqube

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.sonarqube.gradle.SonarQubeExtension
import org.sonarqube.gradle.SonarQubePlugin
import org.sonarqube.gradle.SonarQubeTask

import java.lang.reflect.Field


class SonarqubeMultiProjectPlugin implements Plugin<Project> {
    private static final String SONARQUBE_TASK_PROPERTIES_FIELDNAME = 'sonarProperties'

    @Override
    void apply(Project project) {
        project.plugins.with {
            apply SonarQubePlugin
        }

        Task task = project.tasks.findByName(SonarQubeExtension.SONARQUBE_TASK_NAME)
        if (task != null) {
            Field sonarProperties = SonarQubeTask.getDeclaredField(SONARQUBE_TASK_PROPERTIES_FIELDNAME)

            task.doFirst {
                sonarProperties.setAccessible(true)
                sonarProperties.set(it, SonarqubeMultiProjectPropertiesAdapter.adapt(it.properties))
                sonarProperties.setAccessible(false)
            }
        }
    }
}
