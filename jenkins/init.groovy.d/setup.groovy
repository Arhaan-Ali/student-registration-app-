import jenkins.model.Jenkins
import hudson.security.*
import hudson.tasks.Maven
import hudson.tools.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
def j = Jenkins.get()
def realm = new HudsonPrivateSecurityRealm(false)
realm.createAccount("admin", "admin123")
j.setSecurityRealm(realm)
def s = new FullControlOnceLoggedInAuthorizationStrategy()
s.setAllowAnonymousRead(false)
j.setAuthorizationStrategy(s)
def d = j.getDescriptorByType(Maven.DescriptorImpl.class)
def inst = new Maven.MavenInstallation("Maven", null, [new InstallSourceProperty([new Maven.MavenInstaller("3.9.9")])])
d.setInstallations(inst); d.save()
if (j.getItem("student-registration") == null) {
  def job = j.createProject(WorkflowJob.class, "student-registration")
  def script = "pipeline {\n agent any\n tools { maven 'Maven' }\n stages {\n  stage('Build') { steps { dir('/project') { sh 'mvn -B clean package' } } }\n }\n post { success { echo 'Jenkins Build SUCCESS' } }\n}\n"
  job.setDefinition(new CpsFlowDefinition(script, false))
  job.save()
}
j.save()
println("INIT-GROOVY-DONE")
