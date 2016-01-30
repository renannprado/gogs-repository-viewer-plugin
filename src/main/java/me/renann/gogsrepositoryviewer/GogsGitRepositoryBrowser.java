package me.renann.gogsrepositoryviewer;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.plugins.git.GitChangeSet;
import hudson.plugins.git.browser.FisheyeGitRepositoryBrowser;
import hudson.scm.RepositoryBrowser;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Renann Prado on 1/24/16.
 */
public class GogsGitRepositoryBrowser extends FisheyeGitRepositoryBrowser {

    @DataBoundConstructor
    public GogsGitRepositoryBrowser(String repoUrl) {
        super(repoUrl);
    }


    @Override
    public URL getDiffLink(GitChangeSet.Path path) throws IOException {
        // TODO will implement later
        return null;
    }

    @Override
    public URL getFileLink(GitChangeSet.Path path) throws IOException {
        // for some reason sometimes the branch won't be set
        // set "master" branch if the branch is null
        final String currentBranch = (path.getChangeSet().getBranch() == null ? "master" : path.getChangeSet().getBranch()) + "/";

        return new URL(getUrl(), getUrl().getPath() + "src/" + currentBranch + path.getPath());
    }

    @Override
    public URL getChangeSetLink(GitChangeSet gitChangeSet) throws IOException {
        return new URL(getUrl(), getUrl().getPath() + "commit/" + gitChangeSet.getId());
    }

    @Extension
    public static class GogsGitRepositoryBrowserDescriptor extends Descriptor<RepositoryBrowser<?>> {

        @Override
        public String getDisplayName() {
            return "Go Git Service (Gogs)";
        }

        @Override
        public GogsGitRepositoryBrowser newInstance(StaplerRequest req, JSONObject jsonObject) throws FormException {
            try {
                JSONObject form = req.getSubmittedForm();
            } catch (ServletException e) {
                e.printStackTrace();
            }
            return req.bindJSON(GogsGitRepositoryBrowser.class, jsonObject);
        }

        public FormValidation doCheckRepoUrl(@QueryParameter(fixEmpty = true) String value, @AncestorInPath AbstractProject project) throws IOException,
                ServletException {
            return new FormValidation.URLCheck() {
                @Override
                protected FormValidation check() throws IOException, ServletException {
                    return FormValidation.ok();
                }
            }.check();
        }
    }
}
